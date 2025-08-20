package data;


public class LibraryData {
    private int id;
    private String name;

    private long createdat;


    public LibraryData(){

    }

    public LibraryData(int id, String name, long createdat) {
        this.id = id;
        this.name = name;
        this.createdat = createdat;
    }

    public String getName() {
        return name;
    }

    public long getCreatedAt() {
        return createdat;
    }

    public int getId() {
        return id;
    }
}
