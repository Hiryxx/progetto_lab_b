package data;

public class BookFilterRequest {
    private String title;
    private Integer year;
    private int authorId;
    private int categoryId;
    private Integer limit;

    public BookFilterRequest() {
    }

    public String getTitle() { return title; }
    public Integer getYear() { return year; }

    public int getAuthorId() {
        return authorId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public Integer getLimit() { return limit; }

    public void setTitle(String title) { this.title = title; }
    public void setYear(Integer year) { this.year = year; }
    public void setLimit(Integer limit) { this.limit = limit; }
}
