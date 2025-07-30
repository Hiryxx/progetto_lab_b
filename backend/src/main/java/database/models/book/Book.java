package database.models.book;


import database.models.base.annotations.Column;
import database.models.base.annotations.Id;
import database.models.base.annotations.Table;
import database.models.base.Entity;
import database.query.SelectBuilder;

@Table(name = "Books")
public class Book extends Entity {
    @Id(autoIncrement = true)
    @Column(type = "INTEGER")
    private int id;

    @Column(type = "VARCHAR(255)", nullable = false)
    private String title;

    // todo relationship with author table
    @Column(type = "VARCHAR(255)", nullable = false)
    private String author;

    // todo relationship with publisher table
    @Column(type = "VARCHAR(255)", nullable = false)
    private String publisher;

    @Column(type = "INT", nullable = false)
    private int year;

    @Column(type = "VARCHAR(255)")
    private String description;

    // todo relationship with genre table
    @Column(type = "VARCHAR(255)", nullable = false)
    private String category;

    public Book(int id, String title, String author, String publisher, int year, String genre) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.genre = genre;
    }

    public static SelectBuilder selectBy(String queryParameter) {
        return new SelectBuilder(queryParameter, Book.class);
    }
}
