package database.connection;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

// TODO Rename class
public class DbConnection {


    public static void executeQuery(String query)throws SQLException{
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

    public static ResultSet executeQueryWithResult(String query) throws SQLException {
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
