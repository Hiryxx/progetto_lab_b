package database.models.book;

import database.models.User;
import database.models.base.annotations.Column;
import database.models.base.annotations.ForeignKey;
import database.models.base.annotations.Id;
import database.models.base.annotations.Table;
import database.models.base.Entity;
import database.query.SelectBuilder;

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

    @Column(type = "INT")
    private int stile;

    @Column(type = "INT")
    private int contenuto;

    @Column(type = "INT")
    private int gradevolezza;

    @Column(type = "INT")
    private int originalita;

    @Column(type = "INT")
    private int edizione;

    @Column(type = "INT")
    private int votofinale;

    @Column(type = "VARCHAR(256)")
    private String recensione;

    public BookRating(int bookid, String usercf) {
        this.bookid = bookid;
        this.usercf = usercf;
    }

    public int getRatingid() {
        return ratingid;
    }

    public String getUsercf() {
        return usercf;
    }

    public int getStile() {
        return stile;
    }

    public int getContenuto() {
        return contenuto;
    }

    public int getGradevolezza() {
        return gradevolezza;
    }

    public int getOriginalita() {
        return originalita;
    }

    public int getEdizione() {
        return edizione;
    }

    public int getVotofinale() {
        return votofinale;
    }

    public String getRecensione() {
        return recensione;
    }

    public int getBookid() {
        return bookid;
    }

    public static SelectBuilder selectBy(String queryParameter) {
        return new SelectBuilder(queryParameter, BookRating.class);
    }
}
