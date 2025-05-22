package server.router.connection.response;

import database.query.QueryResult;

public non-sealed class MultiResponse implements Sendable {
    private final QueryResult queryResult;

    public MultiResponse(QueryResult queryResult) {
        this.queryResult = queryResult;
    }

    public QueryResult queryResult() {
        return queryResult;
    }
}
