package database.query;

import database.connection.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.sql.JDBCType;

public class PrepareQuery {
    private final Query query;

    private List<Object> values = null;
    private PreparedStatement stmt;

    public PrepareQuery(Query query, List<Object> values) {
        this.query = query;
        this.values = values;
    }

    public PrepareQuery(Query query) {
        this.query = query;
    }

    public void prepareStmt(Connection conn) throws SQLException {
        stmt = conn.prepareStatement(this.query.getInnerQuery());
        if (values != null) {
            for (int i = 0; i < values.size(); i++) {
                Object value = values.get(i);

                value = getTypedValue(value);

                stmt.setObject(i + 1, value);
            }
        }
    }

    private Object getTypedValue(Object startingValue){
        return switch (startingValue){
            case String strValue-> {
                if (strValue.startsWith("'") && strValue.endsWith("'")) {
                    yield strValue.substring(1, strValue.length() - 1);
                }
                yield strValue;
            }
            default -> startingValue;
        };
    }

    /**
     * Executes the update query.
     *
     * @throws SQLException if there is an error executing the update.
     */
    public void executeUpdate() throws SQLException {
        DbConnection.executeUpdate(this);
    }

    /**
     * Executes the query and returns the result.
     *
     * @return QueryResult containing the result of the query execution.
     * @throws SQLException if there is an error executing the query.
     */
    public QueryResult executeResult() throws SQLException {
        return DbConnection.executeQuery(this);
    }


    public PreparedStatement getStatement() {
        return stmt;
    }

    private JDBCType getJDBCType(Object data) {
        return switch (data.getClass().getSimpleName()) {
            case "String" -> JDBCType.VARCHAR;
            case "Integer", "int" -> JDBCType.INTEGER;
            case "Long", "long" -> JDBCType.BIGINT;
            case "Double", "double" -> JDBCType.DOUBLE;
            case "Float", "float" -> JDBCType.FLOAT;
            case "Boolean", "boolean" -> JDBCType.BOOLEAN;
            default -> throw new IllegalArgumentException("Unsupported data type: " + data.getClass().getSimpleName());
        };
    }

    // Hides the query values when printing
    @Override
    public String toString() {
        return query.getInnerQuery();
    }
}
