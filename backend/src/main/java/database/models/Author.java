package database.models;

import database.models.base.Entity;
import database.models.base.annotations.Column;
import database.models.base.annotations.Id;
import database.models.base.annotations.Table;
import database.query.SelectBuilder;

@Table(name = "authors")
public class Author extends Entity {
    @Id(autoIncrement = true)
    @Column(type = "INTEGER")
    private int id;

    @Column(type = "VARCHAR(255)", nullable = false)
    private String name;

    public Author(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static SelectBuilder selectBy(String queryParameter) {
        return new SelectBuilder(queryParameter, Author.class);
    }
}
