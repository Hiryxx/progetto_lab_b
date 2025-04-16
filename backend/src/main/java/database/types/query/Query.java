package database.types.query;

import database.connection.DbConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

public class Query implements Iterator<ResultSet> {
    private String query;
    private ResultSet queryResult;

    public Query(String query) {
        this.query = query;
    }

    public Query execute() throws SQLException {
        queryResult = DbConnection.executeQuery(query);
        // todo return result class and implement iterable and iterator
        return this;
    }

    public String getInnerQuery() {
        return query;
    }

    @Override
    public boolean hasNext() {
        try {
            return queryResult != null && !queryResult.isLast() && !queryResult.isAfterLast();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public ResultSet next() {
        try {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException("No more rows in ResultSet");
            }
            queryResult.next();
            return queryResult;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
