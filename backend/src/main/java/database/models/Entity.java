package database.models;

import database.connection.DbConnection;
import database.query.SelectBuilder;
import utils.DbUtil;

import java.sql.SQLException;

/**
 * Represents an entity in the database
 */
public abstract class Entity {

    /**
     * Inserts an entity into the database
     */
    public void create() throws IllegalAccessException, SQLException {
        String insertQuery = DbUtil.insertQuery(this);
        System.out.println("Insert query: " + insertQuery);

        // Inserts the entity into the database
        DbConnection.executeUpdate(insertQuery);
    }

    /**
     * Updates an entity in the database
     */
    public void update() throws IllegalAccessException, SQLException {
        String updateQuery = DbUtil.updateQuery(this);
        System.out.println("Update query: " + updateQuery);
        // Updates the entity in the database
        DbConnection.executeUpdate(updateQuery);
    }

    /**
     * Deletes an entity from the database
     */
    public void delete() throws IllegalAccessException, SQLException {
        String deleteQuery = DbUtil.deleteQuery(this);
        System.out.println("Delete query: " + deleteQuery);
        // Deletes the entity from the database
        DbConnection.executeUpdate(deleteQuery);
    }

    /**
     * Selects an entity from the database
     *
     * @param queryParameter the query parameter to select by
     * @param entityClass    the class of the entity to select
     * @param <T>           the type of the entity
     * @return a SelectBuilder object to build the select query
     */
    public static <T extends Entity> SelectBuilder selectBy(String queryParameter, Class<T> entityClass) {
        return new SelectBuilder(queryParameter, DbUtil.getTableName(entityClass));
    }

}
