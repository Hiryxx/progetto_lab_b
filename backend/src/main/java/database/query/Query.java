package database.query;

import database.connection.DbConnection;

import java.sql.SQLException;

public class Query {
    private String query;
    private QueryResult queryResult;

    public Query(String query) {
        this.query = query;
    }

    /**
     * Executes the query and returns the result
     *
     * @return the result of the query
     * @throws SQLException if there is an error executing the query
     */
    public QueryResult execute() throws SQLException {
        return DbConnection.executeQuery(query);
    }

    /**
     * Returns the query string
     *
     * @return the query string
     */
    public String getInnerQuery() {
        return query;
    }

}
