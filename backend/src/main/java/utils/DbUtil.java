package utils;

import database.connection.DbConnection;
import database.models.Entity;
import database.types.Constraint;
import database.types.keys.ForeignKey;
import database.types.keys.PrimaryKey;

import java.lang.reflect.Field;
import java.sql.SQLException;

public class DbUtil {

    public static void init(Entity fieldClass) throws SQLException, IllegalAccessException {
        String createQuery = DbUtil.createQuery(fieldClass);
        System.out.println("Final query " + createQuery);
        DbConnection.executeUpdate(createQuery);
    }

    private static String createQuery(Entity entity) throws IllegalAccessException {
        // Gets the class name
        String className = entity.tableName();

        // Gets the attributes of the class
        Field[] attributesField = entity.getClass().getDeclaredFields();

        StringBuilder attributes = new StringBuilder();

        System.out.println("Creating table for " + className);

        int index = 0;

        //StringBuilder primaryKeys = new StringBuilder();
        StringBuilder primaryKeys = new StringBuilder();
        StringBuilder foreignKeys = new StringBuilder();

        primaryKeys.append("PRIMARY KEY (");

        // TODO For now it is ok. I need to check if foreign key works and then do unique key
        for (Field field : attributesField) {
            System.out.println(field.getName());
            field.setAccessible(true);
            Class<?> fieldType = field.getType();

            // check if field type is field or one of it's son

            if (database.types.Field.class.isAssignableFrom(fieldType)) {
                database.types.Field sqlField = (database.types.Field) field.get(entity);
                String sqlType = sqlField.getSqlType();

                if (fieldType == database.types.keys.ForeignKey.class) {
                    ForeignKey<?, ?> foreignKey = (ForeignKey<?, ?>) field.get(entity);
                    PrimaryKey<?> pk = foreignKey.referencedPrimaryKey();
                    foreignKeys.append("FOREIGN KEY(").append(foreignKey.getName()).append(") REFERENCES ").append(foreignKey.getReferencedEntityClassName()).append("(");
                    foreignKeys.append(pk.getName()).append(") ");
                    foreignKeys.append("ON DELETE CASCADE").append(" ").append("ON UPDATE CASCADE");
                } else if (fieldType == database.types.keys.PrimaryKey.class) {
                    PrimaryKey<?> pk = (PrimaryKey<?>) field.get(entity);
                    primaryKeys.append(pk.getName()).append(", ");
                } else if (fieldType == database.types.keys.UniqueKey.class) {
                    // TODO add unique key
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
        // removes the last comma from the primary keys
        if (primaryKeys.length() > 0) {
            primaryKeys.delete(primaryKeys.length() - 2, primaryKeys.length());
        }
        primaryKeys.append(")");
        // Creates the table in the database
        return "CREATE TABLE IF NOT EXISTS " + className + " ("  + attributes + ") " + primaryKeys + " " + foreignKeys + ";";
    }

    public static String insertQuery(Entity entity) throws IllegalAccessException {
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
        // TODO Check that is valid
        return "INSERT INTO " + className + " (" + attributes + ") VALUES (" + values + ");";
    }
}
