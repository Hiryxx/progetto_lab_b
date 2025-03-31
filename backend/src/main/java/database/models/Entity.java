package database.models;

import database.connection.DbConnection;
import database.types.Constraint;
import database.types.keys.ForeignKey;
import database.types.keys.PrimaryKey;
import database.types.query.QueryBuilder;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Represents an entity in the database
 */
public abstract class Entity {


    // Random uuid of 36 characters
    private static PrimaryKey<String> uuid = new PrimaryKey<>("uuid", UUID.randomUUID().toString(), "CHAR(36)", new Constraint[]{Constraint.AUTO_INCREMENT});


    /**
     * Creates a new entity in the database
     */
    public void create() throws SQLException, IllegalAccessException {
        // Gets the class name
        String className = this.getClass().getSimpleName().toLowerCase() + "s";

        // Gets the attributes of the class
        Field[] attributesField = this.getClass().getDeclaredFields();

        StringBuilder attributes = new StringBuilder();

        System.out.println("Creating table for " + className);

        int index = 0;

        //StringBuilder primaryKeys = new StringBuilder();
        StringBuilder foreignKeys = new StringBuilder();

        // TODO fields here would be in another class that holds stuff like primary key, unique, not null, etc
        for (Field field : attributesField) {
            System.out.println(field.getName());
            field.setAccessible(true);
            Class<?> fieldType = field.getType();

            // check if field type is field or one of it's son

            if (database.types.Field.class.isAssignableFrom(fieldType)) {
                // todo is this ok?
                database.types.Field sqlField = (database.types.Field) field.get(this);
                String sqlType = sqlField.getSqlType();

                if (fieldType == database.types.keys.ForeignKey.class) {
                    // todo
                    ForeignKey foreignKey = (ForeignKey) field.get(this);

                    foreignKey.referencedPrimaryKey();
                    // foreignKeys.append(field.getName()).append(", ");
                }
                // gets the sql type constraints of each field

                //System.out.println("SQL Type: " + sqlType);

                // gets the constraints of each field
                Constraint[] constraints = sqlField.getConstraints();

                attributes.append(field.getName()).append(" ").append(sqlType);
                for (Constraint constraint : constraints) {
                    attributes.append(" ").append(constraint.toString());
                }

                // does not add a comma to the last attribute (can also be done with removing the last comma)
                if (index < attributesField.length - 1)
                    attributes.append(", ");
                index++;
            }


            field.setAccessible(false);
        }
        // todo put this ina a variable
        String createQuery = "CREATE TABLE IF NOT EXISTS " + className + " (uuid CHAR(36) PRIMARY KEY, " + attributes + ");";
        System.out.println("Final query " + createQuery);

        // Creates the table in the database
        // Ignores the output
        DbConnection.executeQuery(createQuery);
    }


    // TODO IF auto increment, it does not have to be in the inserted

    /**
     * Updates an entity in the database
     */
    public void insert() {
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
