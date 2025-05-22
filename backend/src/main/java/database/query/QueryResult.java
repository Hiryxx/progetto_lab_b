package database.query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class QueryResult implements Iterable<ResultSet>, AutoCloseable {
    private final Connection connection;
    private final Statement statement;
    private final ResultSet resultSet;
    private boolean closed = false;

    public QueryResult(Connection connection, Statement statement, ResultSet resultSet) {
        this.connection = connection;
        this.statement = statement;
        this.resultSet = resultSet;
    }

    /**
     * Returns the underlying ResultSet.
     *
     * @return the ResultSet
     */
    @Override
    public Iterator<ResultSet> iterator() {
        if (closed) {
            throw new IllegalStateException("QueryResult is closed");
        }

        return new Iterator<>() {
            private boolean hasNextChecked = false;
            private boolean hasNextValue = false;

            @Override
            public boolean hasNext() {
                if (closed) return false;
                if (hasNextChecked) return hasNextValue;

                try {
                    hasNextValue = resultSet.next();
                    hasNextChecked = true;

                    if (!hasNextValue) {
                        // Auto-close when done iterating
                        try {
                            close();
                        } catch (Exception e) {
                            // Ignore
                        }
                    }

                    return hasNextValue;
                } catch (SQLException e) {
                    try {
                        close();
                    } catch (Exception ignored) {
                    }
                    throw new RuntimeException("Error accessing result set", e);
                }
            }

            @Override
            public ResultSet next() {
                if (closed) throw new NoSuchElementException("QueryResult is closed");

                if (!hasNextChecked) {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                }

                hasNextChecked = false;
                return resultSet;
            }
        };
    }

    /***
     * Closes the connection, statement, and result set.
     * @throws Exception if an error occurs while closing
     */
    @Override
    public void close() throws Exception {
        if (!closed) {
            System.out.println("Closing QueryResult");
            try {
                if (resultSet != null) resultSet.close();
            } finally {
                try {
                    if (statement != null) statement.close();
                } finally {
                    if (connection != null) connection.close();
                    closed = true;
                }
            }
        }
    }

    /**
     * Returns a stream of ResultSet objects.
     * The iterator consumes the ResultSet and closes the connection pool when done.
     *
     * @return a stream of ResultSet objects
     */
    public Stream<ResultSet> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}