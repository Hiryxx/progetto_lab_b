package server.router;

/**
 * Exception thrown when a requested router is not found.
 */
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
