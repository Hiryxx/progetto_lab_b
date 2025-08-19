package data;

public class LibraryData {
    private String id;
    private String name;
    private String lastModified;

    public LibraryData(String id, String name, String lastModified) {
        this.id = id;
        this.name = name;
        this.lastModified = lastModified;
    }

    public String getName() {
        return name;
    }

    public String getLastModified() {
        return lastModified;
    }
}
