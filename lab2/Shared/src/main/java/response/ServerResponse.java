package response;

import java.io.Serializable;

public class ServerResponse implements Serializable {
    private final ServerResponseStatus status;
    private final String message;

    public ServerResponse(ServerResponseStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public boolean isSuccess() {
        return status == ServerResponseStatus.SUCCESS;
    }

    public ServerResponseStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}