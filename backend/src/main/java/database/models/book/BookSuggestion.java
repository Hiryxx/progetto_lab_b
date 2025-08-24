package database.models.book;

import database.models.User;
import database.models.base.Entity;
import database.models.base.annotations.Column;
import database.models.base.annotations.ForeignKey;
import database.models.base.annotations.Id;
import database.models.base.annotations.Table;
import database.query.SelectBuilder;

@Table(name = "booksuggestions")
public class BookSuggestion extends Entity {
    @Id(autoIncrement = true)
    @Column(type = "SERIAL")
    private int id;

    @ForeignKey(references = Book.class)
    @Column(type = "SERIAL", nullable = false)
    private int sourcebookid;

    @ForeignKey(references = Book.class)
    @Column(type = "SERIAL", nullable = false)
    private int targetbookid;

    @ForeignKey(references = User.class, column = "cf")
    @Column(type = "VARCHAR(16)", nullable = false)
    private String usercf;

    public BookSuggestion(int sourcebookid, int targetbookid, String usercf) {
        this.sourcebookid = sourcebookid;
        this.targetbookid = targetbookid;
        this.usercf = usercf;
    }

    public static SelectBuilder selectBy(String queryParameter) {
        return new SelectBuilder(queryParameter, BookSuggestion.class);
    }
}
