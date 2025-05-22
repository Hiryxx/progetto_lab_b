package server.router.connection.response;

public non-sealed class SingleResponse implements Sendable {
    private final String object;

    public SingleResponse(String object) {
        this.object = object;
    }

    public String object() {
        return object;
    }
}
