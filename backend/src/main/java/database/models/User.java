package database.models;

import database.types.Constraint;
import database.types.Field;
import database.types.keys.ForeignKey;
import database.types.keys.UniqueKey;

public class User extends Entity {
    //should pk be static?
    private Field<String> name;
    private Field<String> email;
    private Field<String> password;
    //private ForeignKey<String, User> role;


    public User(String name, String email, String password) {
        this.name = new Field<>("name", name, "VARCHAR(255)", new Constraint[]{Constraint.NOT_NULL});
        this.email = new UniqueKey<>("email", email, "VARCHAR(255)", new Constraint[]{Constraint.NOT_NULL});
        this.password = new Field<>("password", password, "VARCHAR(255)", new Constraint[]{Constraint.NOT_NULL});
    }

    public User() {
    }

}
