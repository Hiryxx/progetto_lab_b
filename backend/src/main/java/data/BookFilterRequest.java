package data;

public class BookFilterRequest {
    private String title;
    private Integer year;
    private String author;
    private String category;
    private Integer limit;

    public BookFilterRequest() {
    }

    public String getTitle() { return title; }
    public Integer getYear() { return year; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public Integer getLimit() { return limit; }

    public void setTitle(String title) { this.title = title; }
    public void setYear(Integer year) { this.year = year; }
    public void setAuthor(String author) { this.author = author; }
    public void setCategory(String category) { this.category = category; }
    public void setLimit(Integer limit) { this.limit = limit; }
}
