package database.models.book;

import database.models.Category;
import database.models.base.Entity;
import database.models.base.annotations.Column;
import database.models.base.annotations.ForeignKey;
import database.models.base.annotations.Id;
import database.models.base.annotations.Table;

@Table(name = "bookcategories")
public class BookCategory extends Entity {
    @Id(autoIncrement = true)
    private int id;

    @ForeignKey(references = Book.class)
    @Column(type = "SERIAL", nullable = false)
    private int bookid;

    @ForeignKey(references = Category.class)
    @Column(type = "SERIAL", nullable = false)
    private int categoryid;

    public BookCategory(int bookid, int categoryid) {
        this.bookid = bookid;
        this.categoryid = categoryid;
    }
}
