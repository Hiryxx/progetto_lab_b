package database.models;

import database.connection.DbConnection;
import database.query.PrepareQuery;
import database.query.SelectBuilder;
import utils.DbUtil;

import java.sql.SQLException;

/**
 * Represents an entity in the database
 */
public abstract class Entity {
    // todo i need to understand how i want to see Query and PrepareQuery classes
    // todo for example i want also the select builder to make sense. should i make a prepare method for the builder?
    //todo do i want the query to be before or after the prepare? i think before, so i can see the query before it is prepared
    //todo i could make a prepare method that returns a PrepareQuery object, so i can see the query before it is prepared

    // ----- after modifications -----

    // todo i added the prepare method. now i would have to execute the query but do i need to execute it in that class? or in some others?
    // todo i could use the helper class DbConnection to execute the query


    // --- after modifications ---


    // todo i dont know if i like that the util gets the prepare statement and executes it

    /**
     * Inserts an entity into the database
     */
    public void create() throws IllegalAccessException, SQLException {
        PrepareQuery insertQuery = DbUtil.insertQuery(this);
        System.out.println("Insert query: " + insertQuery);

        // Inserts the entity into the database
        DbConnection.executeUpdate(insertQuery);
    }

    /**
     * Updates an entity in the database
     */
    public void update() throws IllegalAccessException, SQLException {
        PrepareQuery updateQuery = DbUtil.updateQuery(this);
        System.out.println("Update query: " + updateQuery);
        // Updates the entity in the database
        DbConnection.executeUpdate(updateQuery);
    }

    /**
     * Deletes an entity from the database
     */
    public void delete() throws IllegalAccessException, SQLException {
        PrepareQuery deleteQuery = DbUtil.deleteQuery(this);
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
    public static <T extends Entity> SelectBuilder selectBy(String queryParameter, Class<T> entityClass){
        throw new UnsupportedOperationException("This method must be implemented in the subclass.");
    }

}
