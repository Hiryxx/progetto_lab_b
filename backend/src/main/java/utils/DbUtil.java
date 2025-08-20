package utils;

import database.connection.DbConnection;
import database.models.base.Entity;
import database.models.base.annotations.*;
import database.query.PrepareQuery;
import database.query.Query;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbUtil {

    public static String getTableName(Class<?> entityClass) {
        Table tableAnnotation = entityClass.getAnnotation(Table.class);
        return tableAnnotation != null && !tableAnnotation.name().isEmpty()
                ? tableAnnotation.name()
                : entityClass.getSimpleName() + "s";
    }
    /**
     * Initializes the database table for the given entity class.
     * This method generates a CREATE TABLE SQL query based on the entity's annotations.
     *
     * @param entityClass The entity class to initialize
     * @return PrepareQuery containing the CREATE TABLE query
     * @throws SQLException If an error occurs while generating the query
     */
    public static PrepareQuery initTable(Class<? extends Entity> entityClass) throws SQLException {
        Table tableAnnotation = entityClass.getAnnotation(Table.class);
        String tableName = tableAnnotation != null && !tableAnnotation.name().isEmpty()
                ? tableAnnotation.name()
                : entityClass.getSimpleName() + "s";

        StringBuilder query = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " (");
        StringBuilder primaryKeys = new StringBuilder();
        StringBuilder foreignKeys = new StringBuilder();
        List<String> columnDefinitions = new ArrayList<>();

        for (Field field : entityClass.getDeclaredFields()) {
            Column column = field.getAnnotation(Column.class);
            if (column == null) continue; // Skip non-column fields

            StringBuilder columnDef = new StringBuilder();
            String columnName = !column.name().isEmpty() ? column.name() : field.getName();
            String columnType = column.type();

            if (field.isAnnotationPresent(Id.class)) {
                Id idAnnotation = field.getAnnotation(Id.class);
                if (idAnnotation.autoIncrement()) {
                    String upperCaseType = columnType.toUpperCase();
                    columnType = switch (upperCaseType) {
                        case "INTEGER", "INT" -> "SERIAL";
                        case "BIGINT" -> "BIGSERIAL";
                        case "SMALLINT" -> "SMALLSERIAL";
                        case "SERIAL" -> "SERIAL";
                        default -> throw new IllegalArgumentException(
                                "Auto-increment is only supported for INTEGER, BIGINT, or SMALLINT types in PostgreSQL. Found: " + columnType
                        );
                    };
                }
            }

            columnDef.append(columnName).append(" ").append(columnType);

            if (!column.nullable()) {
                columnDef.append(" NOT NULL");
            }

            if (!column.defaultValue().isEmpty()) {
                columnDef.append(" DEFAULT ").append(column.defaultValue());
            }

            if (field.isAnnotationPresent(Id.class)) {
                if (!primaryKeys.isEmpty()) primaryKeys.append(", ");
                primaryKeys.append(columnName);
            }

            if (field.isAnnotationPresent(Unique.class)) {
                columnDef.append(" UNIQUE");
            }

            // Handle foreign key
            ForeignKey fk = field.getAnnotation(ForeignKey.class);
            if (fk != null) {
                Class<?> referencedClass = fk.references();
                // Get table name of referenced class
                Table refTableAnnotation = referencedClass.getAnnotation(Table.class);
                String referencedTable = refTableAnnotation != null && !refTableAnnotation.name().isEmpty()
                        ? refTableAnnotation.name()
                        : referencedClass.getSimpleName() + "s";

                String referencedColumn = fk.column();

                if (!foreignKeys.isEmpty()) foreignKeys.append(", ");

                foreignKeys.append("FOREIGN KEY (").append(columnName).append(") ")
                        .append("REFERENCES ").append(referencedTable).append("(").append(referencedColumn).append(") ")
                        .append("ON DELETE ").append(fk.onDelete()).append(" ")
                        .append("ON UPDATE ").append(fk.onUpdate());
            }

            columnDefinitions.add(columnDef.toString());
        }

        query.append(String.join(", ", columnDefinitions));

        if (!primaryKeys.isEmpty()) {
            query.append(", PRIMARY KEY (").append(primaryKeys).append(")");
        }

        if (!foreignKeys.isEmpty()) {
            query.append(", ").append(foreignKeys);
        }

        query.append(")");

        return new PrepareQuery(new Query(query.toString()));
    }
    /**
     * Generates an INSERT query for an entity
     *
     * @param entity The entity to insert
     * @return SQL INSERT query
     * @throws IllegalAccessException If field access fails
     */

    public static PrepareQuery insertQuery(Entity entity) throws IllegalAccessException {
        Class<?> entityClass = entity.getClass();

        Table tableAnnotation = entityClass.getAnnotation(Table.class);
        String tableName = tableAnnotation != null && !tableAnnotation.name().isEmpty()
                ? tableAnnotation.name()
                : entityClass.getSimpleName() + "s";

        StringBuilder columns = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();
        List<Object> values = new ArrayList<>();

        // Process annotated fields
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if (column == null) continue; // Skip non-column fields

            field.setAccessible(true);
            Object value = field.get(entity);

            // Skip null values and default primitive values
            if (value == null || isDefaultValue(value, field.getType())) {
                field.setAccessible(false);
                continue;
            }

            String columnName = !column.name().isEmpty() ? column.name() : field.getName();

            // Add column to the list
            if (columns.length() > 0) {
                columns.append(", ");
                placeholders.append(", ");
            }
            columns.append(columnName);

            // Format value based on type
            String formattedValue = formatValue(value);
            values.add(formattedValue);
            placeholders.append("?");

            field.setAccessible(false);
        }

        String query = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + placeholders + ")";

        return new PrepareQuery(new Query(query), values);
    }

    /**
     * Checks if a value is the default value for its type
     *
     * @param value The value to check
     * @param type The field type
     * @return true if the value is the default for its type
     */
    private static boolean isDefaultValue(Object value, Class<?> type) {
        if (value == null) return true;

        return switch (type.getName()) {
            case "boolean" -> value.equals(false);
            case "byte" -> value.equals((byte) 0);
            case "short" -> value.equals((short) 0);
            case "int" -> value.equals(0);
            case "long" -> value.equals(0L);
            case "float" -> value.equals(0.0f);
            case "double" -> value.equals(0.0d);
            case "char" -> value.equals('\u0000');
            default -> false;
        };
    }

    /**
     * Generates an UPDATE query for an entity
     *
     * @param entity The entity to update
     * @return PrepareQuery for UPDATE operation
     * @throws IllegalAccessException If field access fails
     */
    public static PrepareQuery updateQuery(Entity entity) throws IllegalAccessException {
        Class<?> entityClass = entity.getClass();

        Table tableAnnotation = entityClass.getAnnotation(Table.class);
        String tableName = tableAnnotation != null && !tableAnnotation.name().isEmpty()
                ? tableAnnotation.name()
                : entityClass.getSimpleName() + "s";

        StringBuilder setClause = new StringBuilder();
        StringBuilder whereClause = new StringBuilder();
        List<Object> values = new ArrayList<>();

        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if (column == null) continue; // Skip non-column fields

            field.setAccessible(true);
            Object value = field.get(entity);
            if (value == null) continue; // Skip null values

            String columnName = !column.name().isEmpty() ? column.name() : field.getName();
            boolean isPrimaryKey = field.isAnnotationPresent(Id.class);

            if (isPrimaryKey) {
                if (whereClause.length() > 0) {
                    whereClause.append(" AND ");
                }
                whereClause.append(columnName).append(" = ?");
                values.add(value);
            } else {
                if (setClause.length() > 0) {
                    setClause.append(", ");
                }
                setClause.append(columnName).append(" = ?");
                values.add(value);
            }

            field.setAccessible(false);
        }

        if (whereClause.length() == 0) {
            throw new IllegalArgumentException("Cannot update entity without primary key");
        }

        String query = "UPDATE " + tableName + " SET " + setClause + " WHERE " + whereClause;
        return new PrepareQuery(new Query(query), values);
    }

    /**
     * Generates a DELETE query for an entity
     *
     * @param entity The entity to delete
     * @return PrepareQuery for DELETE operation
     * @throws IllegalAccessException If field access fails
     */
    public static PrepareQuery deleteQuery(Entity entity) throws IllegalAccessException {
        Class<?> entityClass = entity.getClass();

        Table tableAnnotation = entityClass.getAnnotation(Table.class);
        String tableName = tableAnnotation != null && !tableAnnotation.name().isEmpty()
                ? tableAnnotation.name()
                : entityClass.getSimpleName() + "s";

        StringBuilder whereClause = new StringBuilder();
        List<Object> values = new ArrayList<>();

        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);
                Object value = field.get(entity);

                Column column = field.getAnnotation(Column.class);
                String columnName = (column != null && !column.name().isEmpty())
                        ? column.name()
                        : field.getName();

                if (whereClause.length() > 0) {
                    whereClause.append(" AND ");
                }
                whereClause.append(columnName).append(" = ?");
                values.add(value);

                field.setAccessible(false);
            }
        }

        if (whereClause.length() == 0) {
            throw new IllegalArgumentException("Cannot delete entity without primary key");
        }

        String query = "DELETE FROM " + tableName + " WHERE " + whereClause;
        return new PrepareQuery(new Query(query), values);
    }
    /**
     * Helper method to format values for SQL based on their Java type
     *
     * @param value The value to format
     * @return Formatted value for SQL
     */
    private static String formatValue(Object value) {
        return switch (value) {
            case null -> "NULL";
            case String s -> "'" + s.replace("'", "''") + "'";
            case java.util.Date date -> "'" + new java.sql.Timestamp(date.getTime()) + "'";
            case Boolean b -> b ? "1" : "0";
            default -> value.toString();
        };
    }

    /**
     * Initializes the database table for the given entity class
     *
     * @param entityClass The entity class to initialize
     * @throws SQLException           If database operation fails
     * @throws IllegalAccessException If field access fails
     */
    public static void init(Class<? extends Entity> entityClass) throws SQLException, IllegalAccessException {
        PrepareQuery createQuery = DbUtil.initTable(entityClass);
        System.out.println("INFO - Creating table with query: " + createQuery);
        DbConnection.executeUpdate(createQuery);
    }

    /**
     * Executes an entity's INSERT query
     *
     * @param entity The entity to insert
     * @throws IllegalAccessException If field access fails
     * @throws SQLException           If database operation fails
     */
    public static void insert(Entity entity) throws IllegalAccessException, SQLException {
        PrepareQuery query = insertQuery(entity);
        DbConnection.executeUpdate(query);
    }

    /**
     * Executes an entity's UPDATE query
     *
     * @param entity The entity to update
     * @throws IllegalAccessException If field access fails
     * @throws SQLException           If database operation fails
     */
    public static void update(Entity entity) throws IllegalAccessException, SQLException {
        PrepareQuery query = updateQuery(entity);
        DbConnection.executeUpdate(query);
    }

    /**
     * Executes an entity's DELETE query
     *
     * @param entity The entity to delete
     * @throws IllegalAccessException If field access fails
     * @throws SQLException           If database operation fails
     */
    public static void delete(Entity entity) throws IllegalAccessException, SQLException {
        PrepareQuery query = deleteQuery(entity);
        DbConnection.executeUpdate(query);
    }
    /**
        * Executes SQL commands from an InputStream.
        *
        * @param inputStream The InputStream containing SQL commands
        * @throws IllegalArgumentException If the InputStream is null
        * @throws RuntimeException If an error occurs while reading the InputStream or executing the SQL commands
        * @throws SQLException If an SQL error occurs during execution
     */
    public static void executeSqlFromStream(InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream cannot be null.");
        }
        try {
            String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            String[] queries = content.split(";\\s*\\r?\\n|;\\s*$");

            for (String query : queries) {
                String trimmedQuery = query.trim();
                if (!trimmedQuery.isEmpty()) {
                    DbConnection.executeUpdate(new PrepareQuery(new Query(trimmedQuery)));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading SQL from InputStream", e);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
