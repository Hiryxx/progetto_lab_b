package database.models.book;

import database.models.Library;
import database.models.User;
import database.models.base.Entity;
import database.models.base.annotations.Column;
import database.models.base.annotations.ForeignKey;
import database.models.base.annotations.Id;
import database.models.base.annotations.Table;
import database.query.SelectBuilder;

@Table(name = "librarybooks")
public class LibraryBook extends Entity {
    @Id
    @Column(type = "SERIAL")
    @ForeignKey(references = Library.class)
    private int libraryid;

    @Id
    @Column(type = "SERIAL")
    @ForeignKey(references = Book.class)
    private int bookid;

    public LibraryBook(int libraryid, int bookid) {
        this.libraryid = libraryid;
        this.bookid = bookid;
    }

    public static SelectBuilder selectBy(String queryParameter) {
        return new SelectBuilder(queryParameter, LibraryBook.class);
    }

    public int getBookid() {
        return bookid;
    }

    public int getLibraryid() {
        return libraryid;
    }
}
