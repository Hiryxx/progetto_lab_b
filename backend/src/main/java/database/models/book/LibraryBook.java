package database.models.book;

import database.models.Library;
import database.models.User;
import database.models.base.Entity;
import database.models.base.annotations.Column;
import database.models.base.annotations.ForeignKey;
import database.models.base.annotations.Id;
import database.models.base.annotations.Table;
import database.query.SelectBuilder;

@Table(name = "LibraryBooks")
public class LibraryBook extends Entity {
    @Id
    @Column(type = "SERIAL")
    @ForeignKey(references = Library.class)
    private int libraryId;

    @Id
    @Column(type = "SERIAL")
    @ForeignKey(references = Book.class)
    private int bookId;

    public LibraryBook(int libraryId, int bookId) {
        this.libraryId = libraryId;
        this.bookId = bookId;
    }

    public static SelectBuilder selectBy(String queryParameter) {
        return new SelectBuilder(queryParameter, LibraryBook.class);
    }

}
