package database.connection;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

// TODO Rename class. DBSession? DBConnectionManager? DBService?
public class DbConnection {

    /***
     * Executes a query that doesn't return a result set
     * @param query query to be executed
     * @throws SQLException General SQL exception
     */
    public static void executeUpdate(String query) throws SQLException {
        Connection conn = null;
        try {
            // Gets a connection from the pool
            conn = DbConnectionPool.getConnection();

            // Creates the table in the database
            var stmt = conn.createStatement();
            stmt.executeUpdate(query);

        } finally {
            if (conn != null) {
                conn.close(); // Returns to pool, doesn't actually close
            }

        }
    }

    /***
     * Executes a query that returns a result set
     * @param query query to be executed
     * @return Result of the query
     * @throws SQLException General SQL exception
     */
    public static ResultSet executeQuery(String query) throws SQLException {
        Connection conn = null;
        ResultSet result;
        try {
            // Gets a connection from the pool
            conn = DbConnectionPool.getConnection();

            // Creates the table in the database
            var stmt = conn.createStatement();
            result = stmt.executeQuery(query);

        } finally {
            if (conn != null) {
                conn.close(); // Returns to pool, doesn't actually close
            }

        }
        return result;
    }
}
