package connection;

public class Response {
    private String response;
    private boolean isError;

    public Response(String response, boolean isError) {
        this.response = response;
        this.isError = isError;
    }

    public String getResponse() {
        return response;
    }

    public boolean isError() {
        return isError;
    }
}
