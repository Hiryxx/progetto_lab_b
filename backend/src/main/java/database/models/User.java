package database.models;

import database.models.base.annotations.Column;
import database.models.base.annotations.Id;
import database.models.base.annotations.Table;
import database.models.base.annotations.Unique;
import database.models.base.Entity;
import database.query.SelectBuilder;


@Table(name = "Users")
public class User extends Entity {
    @Id
    @Column(type = "CHAR(16)")
    private String cf;

    @Column(type = "VARCHAR(255)", nullable = false)
    private String name;

    @Column(type = "VARCHAR(255)", nullable = false)
    private String lastname;

    @Column(type = "VARCHAR(255)", nullable = false)
    @Unique
    private String email;

    @Column(type = "CHAR(64)", nullable = false) // SHA-256 hash
    private String password;


    public User(
            String cf,
            String name,
            String lastname,
            String email,
            String password) {
        this.cf = cf;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public static SelectBuilder selectBy(String queryParameter) {
        return new SelectBuilder(queryParameter, User.class);
    }

    public String getCf() {
        return cf;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String hashedPassword) {
        this.password = hashedPassword;
    }
}
