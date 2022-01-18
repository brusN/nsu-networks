package handler;

import counter.DownloadSpeedCounter;
import descriptor.FileDescriptor;
import logger.TCPClientHandlerLogger;
import response.ServerResponse;
import response.ServerResponseStatus;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TCPClientHandler implements Runnable {
    private long totalReceivedBytes;
    private FileDescriptor exceptedFileDescriptor;
    private final DownloadSpeedCounter speedCounter;
    private final TCPClientHandlerLogger logger;
    private final Socket socket;

    public TCPClientHandler(Socket clientSocket) {
        this.socket = clientSocket;
        this.speedCounter = new DownloadSpeedCounter(socket.getInetAddress().toString(), 3000);
        this.logger = new TCPClientHandlerLogger(clientSocket.getInetAddress().toString());
    }

    public FileDescriptor getExpectedFileDescriptor(Socket clientSocket) throws IOException, ClassNotFoundException {
        ObjectInputStream clientInputStream = new ObjectInputStream(clientSocket.getInputStream());
        return (FileDescriptor) clientInputStream.readObject();
    }

    public void sendResponseToClient(Socket client, ServerResponse response) throws IOException {
        ObjectOutputStream clientInputStream = new ObjectOutputStream(client.getOutputStream());
        clientInputStream.writeObject(response);
    }

    public Path createFileOnServer(FileDescriptor fileDescriptor) throws IOException {
        // Checking for exist uploads directory. If not, then create new directory with name "uploads"
        Path downloadDirPath = Paths.get("uploads");
        if (!Files.exists(downloadDirPath)) {
            Files.createDirectory(downloadDirPath);
        }

        // If a file with the name of the receiving file exists in the uploads folder, then we change the name of the received file
        String extension = fileDescriptor.getExtension();
        StringBuilder basePath = new StringBuilder("uploads/" + fileDescriptor.getName());
        Path downloadFilePath = Paths.get(basePath.toString() + '.' + extension);
        while (Files.exists(downloadFilePath)) {
            basePath.append("(copy)");
            downloadFilePath = Paths.get(basePath.toString() + '.' + extension);
        }
        Files.createFile(downloadFilePath);
        return downloadFilePath;
    }

    public void receiveFileFromClient(Path downloadPath) throws IOException {
        InputStream inputStream = socket.getInputStream();
        FileOutputStream fileOutputStream = new FileOutputStream(downloadPath.toString());
        byte[] buffer = new byte[4096];
        long exceptedFileSize = exceptedFileDescriptor.getSize();
        totalReceivedBytes = 0;
        speedCounter.start();
        int receivedBytes = inputStream.read(buffer, 0, buffer.length);
        while (receivedBytes != -1) {
            totalReceivedBytes += receivedBytes;
            speedCounter.updateReceivedBytes(receivedBytes);
            fileOutputStream.write(buffer, 0, receivedBytes);
            if (totalReceivedBytes >= exceptedFileSize) {
                break;
            }
            receivedBytes = inputStream.read(buffer, 0, buffer.length);
        }
        speedCounter.stop();
        fileOutputStream.close();
    }

    void verifyReceivedFile(FileDescriptor exceptedFileDescriptor) throws IOException {
        if (totalReceivedBytes != exceptedFileDescriptor.getSize()) {
            sendResponseToClient(socket, new ServerResponse(ServerResponseStatus.FAIL, "The expected file size does not match the actual"));
            throw new IllegalStateException("The expected file size does not match the actual");
        }
        sendResponseToClient(socket, new ServerResponse(ServerResponseStatus.SUCCESS, "File has accepted successfully"));
    }

    @Override
    public void run() {
        try {
            // Getting the file descriptor of the file that is expected to be received
            exceptedFileDescriptor = getExpectedFileDescriptor(socket);
            logger.log("Accepted file descriptor from client [" + socket.getInetAddress() + "]");
            sendResponseToClient(socket, new ServerResponse(ServerResponseStatus.SUCCESS, "File descriptor has accepted successfully"));

            // Receiving and verifying file
            Path downloadPath = createFileOnServer(exceptedFileDescriptor);
            receiveFileFromClient(downloadPath);
            verifyReceivedFile(exceptedFileDescriptor);
            logger.log("File [" + exceptedFileDescriptor.getFullName() + "] has accepted from client [" + socket.getInetAddress() + "]");
            logger.log("File [" + exceptedFileDescriptor.getFullName() + "] has saved in [" + downloadPath.toRealPath() + "]");
        } catch (IOException | ClassNotFoundException | IllegalStateException e) {
            logger.logError(e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                logger.logError(e.getMessage());
            }
        }
    }
}