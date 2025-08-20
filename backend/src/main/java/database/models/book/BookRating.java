package database.models.book;

import database.models.User;
import database.models.base.annotations.Column;
import database.models.base.annotations.ForeignKey;
import database.models.base.annotations.Id;
import database.models.base.annotations.Table;
import database.models.base.Entity;

@Table(name = "bookratings")
public class BookRating extends Entity {
    @Id(autoIncrement = true)
    @Column( type = "SERIAL")
    private int ratingid;

    @ForeignKey(references = Book.class)
    @Column( type = "SERIAL", nullable = false)
    private int bookid;

    @ForeignKey(references = User.class, column = "cf")
    @Column(type = "CHAR(36)", nullable = false)
    private String usercf;

    // add stuff

    public BookRating(int bookid, String usercf) {
        this.bookid = bookid;
        this.usercf = usercf;
    }

}
