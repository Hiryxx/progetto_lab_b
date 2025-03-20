package database.models;

import database.types.Constraint;
import database.types.Field;

public class User extends Entity{
    private Field<Integer> id;
    private Field<String> name;
    private Field<String> email;
    private Field<String> password;

    public User(String name, String email, String password) {
        this.id = new Field<>("id", 0, "INT", new Constraint[]{Constraint.PRIMARY_KEY, Constraint.NOT_NULL, Constraint.AUTO_INCREMENT});
        this.name = new Field<>("name", name, "VARCHAR(255)", new Constraint[]{Constraint.NOT_NULL});
        this.email = new Field<>("email", email, "VARCHAR(255)", new Constraint[]{Constraint.UNIQUE, Constraint.NOT_NULL});
        this.password = new Field<>("password", password, "VARCHAR(255)", new Constraint[]{Constraint.NOT_NULL});
    }

    public User() {
    }

    public Field<Integer> getId() {
        return id;
    }

    public Field<String> getName() {
        return name;
    }

    public Field<String> getEmail() {
        return email;
    }

    public Field<String> getPassword() {
        return password;
    }
}
