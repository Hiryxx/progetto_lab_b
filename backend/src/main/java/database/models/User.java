package database.models;

import database.annotations.Column;
import database.annotations.Id;
import database.annotations.Table;
import database.annotations.Unique;
import database.query.SelectBuilder;

@Table(name = "Users")
public class User extends Entity {
    @Id
    @Column(type = "CHAR(36)")
    private String cf;

    @Column(type = "VARCHAR(255)", nullable = false)
    private String name;

    @Column(type = "VARCHAR(255)", nullable = false)
    @Unique
    private String email;

    @Column(type = "VARCHAR(255)", nullable = false)
    private String password;


    public User(
            String cf,
            String name,
            String email,
            String password) {
        this.cf = cf;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static SelectBuilder selectBy(String queryParameter) {
        return new SelectBuilder(queryParameter, User.class);
    }
}
