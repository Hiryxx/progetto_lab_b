package database.models;

import database.annotations.Column;
import database.annotations.ForeignKey;
import database.annotations.Id;
import database.annotations.Table;

@Table(name = "BookRatings")
public class BookRating {
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
