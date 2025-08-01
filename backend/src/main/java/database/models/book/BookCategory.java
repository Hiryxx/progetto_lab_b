package database.models.book;

import database.models.Category;
import database.models.base.Entity;
import database.models.base.annotations.Column;
import database.models.base.annotations.ForeignKey;
import database.models.base.annotations.Id;
import database.models.base.annotations.Table;

@Table(name = "BookCategories")
public class BookCategory extends Entity {
    @Id(autoIncrement = true)
    private int id;

    @ForeignKey(references = Book.class)
    @Column(type = "SERIAL", nullable = false)
    private int bookId;

    @ForeignKey(references = Category.class)
    @Column(type = "SERIAL", nullable = false)
    private int categoryId;

    public BookCategory(int bookId, int categoryId) {
        this.bookId = bookId;
        this.categoryId = categoryId;
    }
}
