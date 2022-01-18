package logger;

public class TCPServerLogger implements Logger {
    @Override
    public void log(String msg) {
        System.out.println("[SERVER] >> " + msg);
    }

    @Override
    public void logError(String msg) {
        System.err.println("[SERVER] >> " + msg);
    }
}

