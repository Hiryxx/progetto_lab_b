package database.connection;

import database.query.PrepareQuery;
import database.query.QueryResult;

import java.sql.*;

/***
 * DbConnection is a utility class for executing SQL queries using a connection pool.
 * It provides methods to execute queries that return results and those that do not.
 */
public class DbConnection {

    /***
     * Executes a query that doesn't return a result set
     * @param prepareQuery query to be executed
     * @throws SQLException General SQL exception
     */
    public static void executeUpdate(PrepareQuery prepareQuery) throws SQLException {
        Connection conn = null;
        try {
            // Gets a connection from the pool
            conn = DbConnectionPool.getConnection();

            // Creates the table in the database
            prepareQuery.prepareStmt(conn);
            var stmt = prepareQuery.getStatement();

            stmt.executeUpdate();
        } finally {
            if (conn != null) {
                conn.close(); // Returns to pool, doesn't actually close
            }

        }
    }

    /***
     * Executes a query that returns a result set
     * @param prepareQuery query to be executed
     * @return Result of the query
     * @throws SQLException General SQL exception
     */
    public static QueryResult executeQuery(PrepareQuery prepareQuery) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DbConnectionPool.getConnection();
            prepareQuery.prepareStmt(conn);
            stmt = prepareQuery.getStatement();

            rs = stmt.executeQuery();
            return new QueryResult(conn, stmt, rs);
        } catch (SQLException e) {
            // Clean up resources on error
            if (rs != null) try {
                rs.close();
            } catch (SQLException ignored) {
            }
            if (stmt != null) try {
                stmt.close();
            } catch (SQLException ignored) {
            }
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignored) {
            }
            throw e;
        }
    }
}
