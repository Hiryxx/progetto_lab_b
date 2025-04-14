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

    // TODO FOR THE FUTURE: if i dont like this uuid i can use polymorphism to get all the primary keys and foreign keys...
    // TODO i am afraid and almost sure that since this is a static field it will be the same for all entities
    // Random uuid of 36 characters
    private static PrimaryKey<String> uuid = new PrimaryKey<>("uuid", UUID.randomUUID().toString(), "CHAR(36)", new Constraint[]{Constraint.AUTO_INCREMENT});

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

    public static QueryBuilder selectBy() {
        return new QueryBuilder();
    }

    public static PrimaryKey<String> getPrimaryKey() {
        return uuid;
    }


}
