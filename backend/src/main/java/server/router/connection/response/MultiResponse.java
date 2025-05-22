package server.router.connection.response;

import java.sql.ResultSet;
import java.util.stream.Stream;

public non-sealed class MultiResponse implements Sendable {
    private final Stream<ResultSet> stream;

    public MultiResponse(Stream<ResultSet> stream) {
        this.stream = stream;
    }

    public Stream<ResultSet> stream() {
        return stream;
    }
}
