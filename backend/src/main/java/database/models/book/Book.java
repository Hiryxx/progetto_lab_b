package database.models.book;


import database.models.Author;
import database.models.Category;
import database.models.base.annotations.Column;
import database.models.base.annotations.ForeignKey;
import database.models.base.annotations.Id;
import database.models.base.annotations.Table;
import database.models.base.Entity;
import database.query.SelectBuilder;

@Table(name = "Books")
public class Book extends Entity {
    @Id(autoIncrement = true)
    @Column(type = "SERIAL")
    private int id;

    @Column(type = "VARCHAR(300)", nullable = false)
    private String title;

    @Column(type = "INT", nullable = false)
    private int year;

    @Column(type = "TEXT")
    private String description;


    public Book(String title, int year, String description) {
        this.title = title;
        this.year = year;
        this.description = description;
    }

    public static SelectBuilder selectBy(String queryParameter) {
        return new SelectBuilder(queryParameter, Book.class);
    }
}
