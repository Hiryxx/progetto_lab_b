package data;

public class BookRatingData {
    public int bookid;

    public int stile;

    public int contenuto;

    public int gradevolezza;

    public int originalita;

    public int edizione;

    public int votofinale;

    public String recensione;

    public int rating_count;
    
    public BookRatingData() {

    }

    public BookRatingData(int bookid, int stile, int contenuto, int gradevolezza, int originalita, int edizione, int votofinale, String recensione) {
        this.bookid = bookid;
        this.stile = stile;
        this.contenuto = contenuto;
        this.gradevolezza = gradevolezza;
        this.originalita = originalita;
        this.edizione = edizione;
        this.votofinale = votofinale;
        this.recensione = recensione;
    }


    
}

