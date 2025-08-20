package database.models.book;

import database.models.base.Entity;
import database.models.base.annotations.Column;
import database.models.base.annotations.ForeignKey;
import database.models.base.annotations.Id;
import database.models.base.annotations.Table;

@Table(name = "booksuggestions")
public class BookSuggestion extends Entity {
    @Id(autoIncrement = true)
    @Column(type = "SERIAL")
    private int id;

    @ForeignKey(references = Book.class)
    @Column(type = "SERIAL", nullable = false)
    private int sourceBookid;

    @ForeignKey(references = Book.class)
    @Column(type = "SERIAL", nullable = false)
    private int targetBookid;

    public BookSuggestion(int sourceBookid, int targetBookid) {
        this.sourceBookid = sourceBookid;
        this.targetBookid = targetBookid;
    }

}
