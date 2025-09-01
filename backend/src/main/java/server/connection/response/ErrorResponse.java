package server.connection.response;

/**
 * ErrorResponse is a class that represents an error response to be sent over a socket connection.
 * It implements the Sendable interface, allowing it to be sent as a response to a command execution failure.
 */
public non-sealed class ErrorResponse implements Sendable {
    private final String errorMessage;

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Gets the error message that was returned by the command function and needs to be sent into the socket
     * @return the error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}
