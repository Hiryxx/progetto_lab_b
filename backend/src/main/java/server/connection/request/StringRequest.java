package server.connection.request;

public class StringRequest extends Request {
    private final String argument;

    public StringRequest(String argument) {
        this.argument = argument;
    }

    public String getArgument() {
        return argument;
    }
}
