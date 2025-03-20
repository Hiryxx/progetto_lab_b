package database.models;

import database.connection.DbConnection;
import database.connection.DbConnectionPool;
import database.types.Constraint;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;


public class Entity {

    /**
     * Creates a new entity in the database
     */
    public void create() throws SQLException, IllegalAccessException {


        // TODO Extract the class name and attributes to a method

        // Gets the class name
        String className = this.getClass().getSimpleName();

        // Gets the attributes of the class
        Field[] attributesField = this.getClass().getDeclaredFields();

        StringBuilder attributes = new StringBuilder();

        System.out.println("Creating table for " + className);

        int index = 0;

        // TODO fields here would be in another class that holds stuff like primary key, unique, not null, etc
        for (Field field : attributesField) {
            System.out.println(field.getName());
            field.setAccessible(true);
            if (field.getType() == database.types.Field.class) {
                // gets the sql type constraints of each field
                // fixme is "this" ok?
                database.types.Field sqlField = (database.types.Field) field.get(this);
                String sqlType = sqlField.getSqlType();
                System.out.println("SQL Type: " + sqlType);

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
        System.out.println("Final string " + "CREATE TABLE IF NOT EXISTS " + className + " (" + attributes + ");");

        // Creates the table in the database
        // Ignores the output
       // DbConnection.executeQuery("CREATE TABLE IF NOT EXISTS " + className + " (" + attributes + ");");
    }


    // TODO IF auto increment, it does not have to be in the insert
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


}
