package server.router;

public class InvalidArgs extends RuntimeException {
    public InvalidArgs(String message) {
        super(message);
    }
}
