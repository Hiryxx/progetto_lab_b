package server.connection.request;

/**
 * StringRequest is a class that represents a request containing a single string argument.
 * It extends the abstract Request class and provides a constructor to initialize the argument.
 */
public class StringRequest extends Request {
    private final String argument;

    public StringRequest(String argument) {
        this.argument = argument;
    }

    public String getArgument() {
        return argument;
    }
}
