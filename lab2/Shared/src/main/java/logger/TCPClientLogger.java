package logger;

public class TCPClientLogger implements Logger {
    @Override
    public void log(String msg) {
        System.out.println("[CLIENT] >> " + msg);
    }

    @Override
    public void logError(String msg) {
        System.err.println("[CLIENT] >> " + msg);
    }
}
