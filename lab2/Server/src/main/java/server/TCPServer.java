package server;

import handler.TCPClientHandler;
import logger.TCPServerLogger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer implements Runnable {
    private final int port;
    private final ExecutorService executorService;
    private final static TCPServerLogger logger = new TCPServerLogger();

    public TCPServer(int port) {
        this.port = port;
        this.executorService = Executors.newCachedThreadPool();
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.log("Server launched with IP: " + serverSocket.getInetAddress() + " and port " + port);
            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                logger.log("Starting file transfer session with client [" + clientSocket.getInetAddress() + "]");
                executorService.execute(new TCPClientHandler(clientSocket));
            }
            executorService.shutdown();
        } catch (IOException e) {
            logger.logError(e.getMessage());
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            logger.logError("Illegal input arguments. Expected port number");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);
        Thread thread = new Thread(new TCPServer(port));
        thread.start();
    }
}
