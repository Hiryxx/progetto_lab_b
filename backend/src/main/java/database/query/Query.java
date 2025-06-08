package database.query;

import database.connection.DbConnection;

import java.sql.SQLException;

/**
 * Represents a database query.
 * This class is responsible for executing the query and returning the result.
 */
public class Query {
    private final String query;

    public Query(String query) {
        this.query = query;
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
