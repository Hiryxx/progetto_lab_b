package server.router;

/**
 * Custom exception class to handle invalid arguments in requests.
 */
public class InvalidArgs extends RuntimeException {
    public InvalidArgs(String message) {
        super(message);
    }
}
