package client;

import constraints.Constraints;
import descriptor.FileDescriptor;
import logger.TCPClientLogger;
import response.ServerResponse;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TCPClient {
    private final Socket socket;
    private static final TCPClientLogger logger = new TCPClientLogger();

    public TCPClient(InetAddress serverIPAddress, int port) throws IOException {
        this.socket = new Socket(serverIPAddress, port);
    }

    public ServerResponse getServerResponse() throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        return (ServerResponse) inputStream.readObject();
    }

    public void sendFileDescriptor(FileDescriptor fileDescriptor) throws IOException, ClassNotFoundException {
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.writeObject(fileDescriptor);

        // Getting response from server about file descriptor transfer status
        ServerResponse response = getServerResponse();
        if (!response.isSuccess()) {
            throw new IOException("Error while sending file descriptor to server");        }
        outputStream.flush();
    }

    private void sendFileToServer(Path file) throws IOException, ClassNotFoundException {
        if (!Files.exists(file)) {
            throw new FileNotFoundException("File with path " + file + "doesn't exist");
        }
        OutputStream outputStream = socket.getOutputStream();
        FileInputStream fileInputStream = new FileInputStream(file.toString());
        byte[] buffer = new byte[4096];
        int countRiddenBytes = fileInputStream.read(buffer, 0, buffer.length);
        while (countRiddenBytes != -1) {
            outputStream.write(buffer, 0, countRiddenBytes);
            countRiddenBytes = fileInputStream.read(buffer, 0, buffer.length);
        }

        // Getting response from server about file descriptor transfer status
        ServerResponse response = getServerResponse();
        if (!response.isSuccess()) {
            throw new IOException("Error while sending file to server");
        }
    }

    public void closeConnection() throws IOException {
        socket.close();
    }

    public static void verifyInputArgs(String[] args) throws IllegalArgumentException, IOException {
        // Should be 3 input arguments
        if (args.length != 3) {
            throw new IllegalArgumentException("Server address, port and file path are expected");
        }

        // Checking for file existence
        Path filePath = Paths.get(args[2]);
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("File with path " + args[2] + " not found");
        }

        // Checking is file regular
        if (!Files.isRegularFile(filePath)) {
            throw new FileNotFoundException("File with path " + args[2] + " not a regular file");
        }

        // Excess file name length check
        int fileNameLength = filePath.toString().getBytes(StandardCharsets.UTF_8).length;
        if (fileNameLength > Constraints.MAX_FILE_NAME_LENGTH) {
            throw new IllegalArgumentException("File length limit exceeded");
        }

        // Excess file size check
        long fileSize = Files.size(filePath);
        if (fileSize > Constraints.MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size limit exceeded");
        }
    }

    public static void main(String[] args) {
        try {
            // Verifying input arguments
            verifyInputArgs(args);
            String serverIPAddress = args[0];
            int serverPort = Integer.parseInt(args[1]);
            Path filePath = Paths.get(args[2]);

            // By first send file descriptor to server and wait for answer
            TCPClient client = new TCPClient(InetAddress.getByName(serverIPAddress), serverPort);
            FileDescriptor sendingFileDescriptor = new FileDescriptor(filePath);
            logger.log("Sending file descriptor of [" + sendingFileDescriptor.getFullName() + "] + to server + [" + client.socket.getInetAddress() + "]");
            client.sendFileDescriptor(sendingFileDescriptor);
            logger.log("File descriptor has sent to server successfully");

            // Sending file
            logger.log("Sending file [" + sendingFileDescriptor.getFullName() + "] + to server + [" + client.socket.getInetAddress() + "]");
            client.sendFileToServer(filePath);
            logger.log("File [" + sendingFileDescriptor.getFullName() + "] has sent server [" + client.socket.getInetAddress() + "] successfully");

            // Closing connection with server
            logger.log("Close connection with server...");
            client.closeConnection();

        } catch (IOException | ClassNotFoundException e) {
            logger.logError(e.getMessage());
            System.exit(1);
        }
    }
}