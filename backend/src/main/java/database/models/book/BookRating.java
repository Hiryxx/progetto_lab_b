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
    @Column(name = "rating_id", type = "INT")
    private String ratingId;

    @ForeignKey(references = Book.class, column = "isbn")
    @Column(name = "book_isbn", type = "CHAR(36)", nullable = false)
    private String bookIsbn;

    @ForeignKey(references = User.class, column = "cf")
    @Column(name = "user_cf", type = "CHAR(36)", nullable = false)
    private String userCf;

    public BookRating(String bookIsbn, String userCf) {
        this.bookIsbn = bookIsbn;
        this.userCf = userCf;
    }

}
