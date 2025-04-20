package database.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

public class QueryResult implements Iterable<ResultSet> {
    private ResultSet queryResult;

    public QueryResult(ResultSet queryResult) {
        this.queryResult = queryResult;
    }

    /**
     * Returns the result set of the query
     *
     * @return the result set of the query
     */
    @Override
    public Iterator<ResultSet> iterator() {
        return new Iterator<>() {
            private boolean hasNext = true;

            @Override
            public boolean hasNext() {
                try {
                    return hasNext && !queryResult.isAfterLast();
                } catch (SQLException e) {
                    return false;
                }
            }

            @Override
            public ResultSet next() {
                try {
                    if (!hasNext) {
                        return null;
                    }

                    if (queryResult.isBeforeFirst()) {
                        queryResult.next();
                    } else {
                        hasNext = queryResult.next();
                    }

                    return queryResult;
                } catch (SQLException e) {
                    hasNext = false;
                    return null;
                }
            }
        };
    }
}
