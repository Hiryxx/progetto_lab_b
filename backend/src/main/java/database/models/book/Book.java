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
    @Column(type = "INTEGER")
    private int id;

    @Column(type = "VARCHAR(255)", nullable = false)
    private String title;

    // this is wrong since the book can have more than one user
    @ForeignKey(references = Author.class)
    @Column(type = "SERIAL", nullable = false)
    private int author_id;

    @Column(type = "INT", nullable = false)
    private int year;

    @Column(type = "VARCHAR(255)")
    private String description;

    @ForeignKey(references = Category.class)
    @Column(type = "SERIAL", nullable = false)
    private int category_id;

    public Book(String title, int authorId, int year, String description, int categoryId) {
        this.title = title;
        this.author_id = authorId;
        this.year = year;
        this.description = description;
        this.category_id = categoryId;
    }

    public static SelectBuilder selectBy(String queryParameter) {
        return new SelectBuilder(queryParameter, Book.class);
    }
}
