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

    @ForeignKey(references = Author.class)
    @Column(type = "VARCHAR(255)", nullable = false)
    private String author_id;

    @Column(type = "INT", nullable = false)
    private int year;

    @Column(type = "VARCHAR(255)")
    private String description;

    @ForeignKey(references = Category.class, column = "id")
    @Column(type = "VARCHAR(255)", nullable = false)
    private String category_id;

    public Book(String title, String authorId, int year, String description, String categoryId) {
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
