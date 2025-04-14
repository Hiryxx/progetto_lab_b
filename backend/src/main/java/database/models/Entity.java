package database.models;

import database.connection.DbConnection;
import database.types.Constraint;
import database.types.keys.ForeignKey;
import database.types.keys.PrimaryKey;
import database.types.query.QueryBuilder;
import utils.DbUtil;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Represents an entity in the database
 */
public abstract class Entity {

    /**
     * Inserts an entity into the database
     */
    public void create() throws IllegalAccessException, SQLException {
        String insertQuery = DbUtil.insertQuery(this);
        // Inserts the entity into the database
        DbConnection.executeUpdate(insertQuery);

    }

    /**
     * Updates an entity in the database
     */
    public void update() {
    }

    /**
     * Deletes an entity from the database
     */
    public void delete() {
    }

    public static QueryBuilder selectBy(String queryParameter) {
        return new QueryBuilder(queryParameter, getTableName());
    }
    /***
     * This method should be overridden in subclasses to return the primary keys of the entity.
     * It is used to get the primary keys of the entity.
     */
    public static PrimaryKey<?>[] getPrimaryKeys() {
        throw new UnsupportedOperationException("This method should be overridden in subclasses");
    }

    public static String getTableName() {
        throw new UnsupportedOperationException("This method should be overridden in subclasses");
    }

    public String tableName() {
        throw new UnsupportedOperationException("This method should be overridden in subclasses");
    }


}
