package data;

public class BookData {
    private String title;
    private int year;
    private String description;

    private String authors;
    private String categories;
    //...

    public BookData(){

    }

    public BookData(String title, int year, String description, String authors, String categories) {
        this.title = title;
        this.year = year;
        this.description = description;
        this.authors = authors;
        this.categories = categories;
    }

    public int getYear() {
        return year;
    }

    public String getAuthors() {
        return authors;
    }

    public String getCategories() {
        return categories;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "BookData{" +
                "title='" + title + '\'' +
                ", year=" + year +
                ", description='" + description + '\'' +
                ", authors='" + authors + '\'' +
                ", categories='" + categories + '\'' +
                '}';
    }
}
