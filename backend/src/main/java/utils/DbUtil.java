package utils;

import database.connection.DbConnection;
import database.models.Entity;
import database.types.Constraint;
import database.types.keys.ForeignKey;

import java.lang.reflect.Field;
import java.sql.SQLException;

public class DbUtil {

    public static void init(Entity fieldClass) throws SQLException, IllegalAccessException {
        String createQuery = DbUtil.createQuery(fieldClass);
        System.out.println("Final query " + createQuery);
        DbConnection.executeUpdate(createQuery);
    }

    private static String createQuery(Entity entity) throws  IllegalAccessException {
        // Gets the class name
        String className = entity.getClass().getSimpleName().toLowerCase() + "s";

        // Gets the attributes of the class
        Field[] attributesField = entity.getClass().getDeclaredFields();

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
                database.types.Field sqlField = (database.types.Field) field.get(entity);
                String sqlType = sqlField.getSqlType();

                if (fieldType == database.types.keys.ForeignKey.class) {
                    // TODO Try to implement foreign key, and then refactor this

                    //todo i need to know the class and the attributes that are connected with the fk
                    ForeignKey foreignKey = (ForeignKey) field.get(entity);
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
        // Creates the table in the database
        return "CREATE TABLE IF NOT EXISTS " + className + " (uuid CHAR(36) PRIMARY KEY, " + attributes + ");";
    }

    public static String insertQuery(Entity entity)throws  IllegalAccessException{
        String className = entity.getClass().getSimpleName().toLowerCase() + "s";

        // fills the query and executes it

        Field[] attributesField = entity.getClass().getDeclaredFields();

        StringBuilder attributes = new StringBuilder();
        StringBuilder values = new StringBuilder();

        int index = 0;

        for (Field field : attributesField) {
            field.setAccessible(true);
            Class<?> fieldType = field.getType();

            // check if field type is field or one of it's son
            if (database.types.Field.class.isAssignableFrom(fieldType)) {
                database.types.Field sqlField = (database.types.Field) field.get(entity);
                String sqlType = sqlField.getSqlType();
                attributes.append(field.getName());
                values.append(sqlField.getValue());

                // does not add a comma to the last attribute (can also be done with removing the last comma)
                if (index < attributesField.length - 1)
                    values.append(", ");
                index++;
            }
            field.setAccessible(false);
        }
        // TODO AM i sure that i am getting the uuid correctly?
        return "INSERT INTO " + className + " (uuid, " + attributes + ") VALUES ('" + Entity.getPrimaryKey() + "'," + values + ");";
    }
}
