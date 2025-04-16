package database.types.query;

import database.connection.DbConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

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
        var result = DbConnection.executeQuery(query);
        return new QueryResult(result);
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
