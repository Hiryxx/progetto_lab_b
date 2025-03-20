package database.models;

import database.types.Constraint;
import database.types.keys.ForeignKey;
import database.types.query.QueryBuilder;

import java.lang.reflect.Field;
import java.sql.SQLException;

/**
 * Represents an entity in the database
 */
public abstract class Entity {

    /**
     * Creates a new entity in the database
     */
    public void create() throws SQLException, IllegalAccessException {
        // Gets the class name
        String className = this.getClass().getSimpleName();

        // Gets the attributes of the class
        Field[] attributesField = this.getClass().getDeclaredFields();

        StringBuilder attributes = new StringBuilder();

        System.out.println("Creating table for " + className);

        int index = 0;

        StringBuilder primaryKeys = new StringBuilder();
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

                if (fieldType == database.types.keys.PrimaryKey.class) {
                    primaryKeys.append(field.getName()).append(",");
                } else if (fieldType == database.types.keys.ForeignKey.class) {
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
        // removes the last comma in a bad way
        // todo maybe change this?
        if (primaryKeys.toString().endsWith(",")){
            primaryKeys = new StringBuilder(primaryKeys.substring(0, primaryKeys.length() - 1));
        }
        System.out.println("Final string " + "CREATE TABLE IF NOT EXISTS " + className + " (" + attributes + " PRIMARY KEY(" + primaryKeys  +  "));");

        // Creates the table in the database
        // Ignores the output
        // DbConnection.executeQuery("CREATE TABLE IF NOT EXISTS " + className + " (" + attributes + ");");
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


}
