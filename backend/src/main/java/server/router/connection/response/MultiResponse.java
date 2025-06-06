package server.router.connection.response;

import database.query.QueryResult;

/**
 * MultiResponse is a class that represents a response containing multiple results.
 * It implements the Sendable interface, allowing it to be sent over a network connection.
 */
public non-sealed class MultiResponse implements Sendable {
    private final QueryResult queryResult;

    public MultiResponse(QueryResult queryResult) {
        this.queryResult = queryResult;
    }

    /**
     * Gets the query result that was returned by the command function and needs to be sent into the socket
     * @return the query result
     */
    public QueryResult getQueryResult() {
        return queryResult;
    }
}
