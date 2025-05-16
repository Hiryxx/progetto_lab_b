package database.models;


import database.annotations.Column;
import database.annotations.Id;
import database.annotations.Table;
import database.annotations.Unique;
import database.query.SelectBuilder;

@Table(name = "Books")
public class Book extends Entity{
    @Id
    @Column(type = "CHAR(36)")
    private String isbn;

    @Column(type = "VARCHAR(255)", nullable = false)
    private String title;

    @Column(type = "VARCHAR(255)", nullable = false)
    private String author;

    @Column(type = "VARCHAR(255)", nullable = false)
    private String publisher;

    @Column(type = "INT", nullable = false)
    private int year;

    @Column(type = "VARCHAR(255)", nullable = false)
    @Unique
    private String genre;

    public Book(String isbn, String title, String author, String publisher, int year, String genre) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.genre = genre;
    }

    public static SelectBuilder selectBy(String queryParameter) {
        return Entity.selectBy(queryParameter, Book.class);
    }
}
