package database.models;

import database.types.Constraint;
import database.types.Field;
import database.types.keys.ForeignKey;
import database.types.keys.PrimaryKey;
import database.types.keys.UniqueKey;

public class User extends Entity {
    private static PrimaryKey<String> cf = new PrimaryKey<>("cf", null,"CHAR(36)", new Constraint[]{Constraint.AUTO_INCREMENT});
    private Field<String> name;
    private Field<String> email;
    private Field<String> password;
    //private ForeignKey<String, User> role;


    public User(String name, String email, String password) {
        this.name = new Field<>("name", name, "VARCHAR(255)", new Constraint[]{Constraint.NOT_NULL});
        this.email = new UniqueKey<>("email", email, "VARCHAR(255)", new Constraint[]{Constraint.NOT_NULL});
        this.password = new Field<>("password", password, "VARCHAR(255)", new Constraint[]{Constraint.NOT_NULL});
        var prova = new ForeignKey<>("role", null, "CHAR(36)", new Constraint[]{Constraint.NOT_NULL}, User.cf);
    }


    public static PrimaryKey<?>[] getPrimaryKeys() {
        return new PrimaryKey[]{cf};
    }

    public static String getTableName() {
        return "Users";
    }

    public String tableName() {
        return "Users";
    }
}
