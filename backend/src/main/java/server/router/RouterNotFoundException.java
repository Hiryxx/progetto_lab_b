package server.router;

public class RouterNotFoundException extends Exception{
    public RouterNotFoundException(String message) {
        super(message);
    }

    public RouterNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RouterNotFoundException(Throwable cause) {
        super(cause);
    }
}
