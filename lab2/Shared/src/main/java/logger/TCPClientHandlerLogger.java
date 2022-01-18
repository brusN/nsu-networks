package logger;

public class TCPClientHandlerLogger implements Logger {
    private final String clientName;
    @Override
    public void log(String msg) {
        System.out.println("[Handler " + clientName + "] >> " + msg);
    }

    @Override
    public void logError(String msg) {
        System.err.println("[Handler " + clientName + "] >> " + msg);
    }

    public TCPClientHandlerLogger(String clientName) {
        this.clientName = clientName;
    }
}
