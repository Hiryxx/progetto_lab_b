package database.models;

import database.models.base.Entity;
import database.models.base.annotations.Column;
import database.models.base.annotations.Id;
import database.models.base.annotations.Table;

@Table(name = "Authors")
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
}
