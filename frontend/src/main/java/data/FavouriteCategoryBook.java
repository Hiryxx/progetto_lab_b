package data;

public class FavouriteCategoryBook extends BookData{
    private String favorite_category;
    private int category_book_count;

    public FavouriteCategoryBook(){

    }

    public String getFavoriteCategory() {
        return favorite_category;
    }


    public int getCategoryBookCount() {
        return category_book_count;
    }
}
