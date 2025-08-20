package database.models.book;

import database.models.Author;
import database.models.base.Entity;
import database.models.base.annotations.Column;
import database.models.base.annotations.ForeignKey;
import database.models.base.annotations.Id;
import database.models.base.annotations.Table;

@Table(name = "bookauthors")
public class BookAuthor extends Entity {
    @Id(autoIncrement = true)
    private int id;

    @ForeignKey(references = Book.class)
    @Column(type = "SERIAL", nullable = false)
    private int bookid;

    @ForeignKey(references = Author.class)
    @Column(type = "SERIAL", nullable = false)
    private int authorid;

    public BookAuthor(int bookid, int authorid) {
        this.bookid = bookid;
        this.authorid = authorid;
    }
}
