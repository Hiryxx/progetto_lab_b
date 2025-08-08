package database.models.book;

import database.models.User;
import database.models.base.annotations.Column;
import database.models.base.annotations.ForeignKey;
import database.models.base.annotations.Id;
import database.models.base.annotations.Table;
import database.models.base.Entity;

@Table(name = "BookRatings")
public class BookRating extends Entity {
    @Id(autoIncrement = true)
    @Column( type = "SERIAL")
    private int ratingId;

    @ForeignKey(references = Book.class)
    @Column( type = "SERIAL", nullable = false)
    private int bookId;

    @ForeignKey(references = User.class, column = "cf")
    @Column(type = "CHAR(36)", nullable = false)
    private String userCf;

    public BookRating(int bookId, String userCf) {
        this.bookId = bookId;
        this.userCf = userCf;
    }

}
