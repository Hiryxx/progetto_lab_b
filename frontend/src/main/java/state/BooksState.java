package state;

import classes.MainFrame;
import com.fasterxml.jackson.core.JsonProcessingException;
import connection.Response;
import connection.SocketConnection;
import data.BookData;
import data.BookRatingData;
import data.FavouriteCategoryBook;
import data.FilterData;
import json.JsonObject;
import json.JsonUtil;

import java.util.ArrayList;
import java.util.List;

public class BooksState {
    public static List<BookData> books = new ArrayList<>();
    public static BookData bookDetail = null;
    public static FilterData[] categories = null;
    public static FilterData[] authors = null;

    public static void fetchBooks() throws Exception {
        SocketConnection sc = MainFrame.getSocketConnection();

        sc.send("GET_BOOKS;12");

        BooksState.books.clear();

         books = sc.receiveUntilStop(BookData.class);

    }

    public static BookData getDetailBook() {
        if (bookDetail != null) {
            return bookDetail;
        } else {
            // get that book
            System.out.println("No book detail available.");
            return null;
        }
    }


    public static void fetchCategories() {
        if (categories != null) {
            return;
        }
        SocketConnection sc = MainFrame.getSocketConnection();

        sc.send("GET_CATEGORIES");

        List<FilterData> categoryList = sc.receiveUntilStop(FilterData.class);

        if (categoryList == null || categoryList.isEmpty()) {
            System.out.println("No categories found.");
            return;
        }

        categoryList.addFirst(new FilterData(-1, "Tutte le categorie"));

        categories = categoryList.toArray(new FilterData[0]);

    }

    public static void fetchAuthors() {
        if (authors != null) {
            return;
        }
        SocketConnection sc = MainFrame.getSocketConnection();

        sc.send("GET_AUTHORS");

        List<FilterData> authorsList = sc.receiveUntilStop(FilterData.class);

        if (authorsList == null || authorsList.isEmpty()) {
            System.out.println("No authors found.");
            return;
        }

        authorsList.addFirst(new FilterData(-1, "Tutti gli autori"));

        authors = authorsList.toArray(new FilterData[0]);
    }

    public static void fetchFilteredBooks(String title, String yearText, int authorId, int categoryid) throws JsonProcessingException {
        SocketConnection sc = MainFrame.getSocketConnection();
        JsonObject request = new JsonObject();
        int year;
        try {
            year = Integer.parseInt(yearText);
        } catch (NumberFormatException e) {
            year = -1;
        }

        request.put("title", title);
        request.put("year", year);
        request.put("authorId", authorId);
        request.put("categoryId", categoryid);
        request.put("limit", 16);

        System.out.println("Fetching books with filters: " + request);
        sc.send("GET_FILTERED_BOOKS", request);

        BooksState.books.clear();

        books = (ArrayList<BookData>) sc.receiveUntilStop(BookData.class);

    }

    public static List<BookData> getBookSuggestions(int bookId) {
        SocketConnection sc = MainFrame.getSocketConnection();
        JsonObject request = new JsonObject();
        request.put("id", bookId);

        sc.send("GET_BOOK_SUGGESTIONS", request);

        List<BookData> suggestions = sc.receiveUntilStop(BookData.class);

        return suggestions;

    }

    public static void saveBookRating(BookRatingData rating) {
        SocketConnection sc = MainFrame.getSocketConnection();
        JsonObject request = new JsonObject();

        request.put("bookid", rating.bookid);
        request.put("contenuto", rating.contenuto);
        request.put("edizione", rating.edizione);
        request.put("originalita", rating.originalita);
        request.put("gradevolezza", rating.gradevolezza);
        request.put("recensione", rating.recensione);
        request.put("stile", rating.stile);
        request.put("votofinale", rating.votofinale);
        request.put("usercf", UserState.cf);

        sc.send("SAVE_BOOK_RATING", request, UserState.cf);

        Response response = sc.receive();
        if (response.isError()) {
            System.out.println("Error setting book rating: " + response.getResponseText().substring(6));
        } else {
            System.out.println("Book rating set successfully for book ID: " + rating.bookid);
        }

    }


    public static BookRatingData getBookRatings(int bookId) {
        SocketConnection sc = MainFrame.getSocketConnection();
        JsonObject request = new JsonObject();
        request.put("bookid", bookId);

        sc.send("GET_BOOK_RATINGS", request);

        Response response = sc.receive();
        if (response.isError()) {
            System.out.println("Error fetching book ratings: " + response.getResponseText().substring(6));
            return null;
        } else {
            String responseText = response.getResponseText();
            System.out.println("RATING response: " + responseText);
            try {
                BookRatingData rating = JsonUtil.fromString(responseText, BookRatingData.class);
                if (rating.rating_count == 0) {
                    System.out.println("No ratings found for book ID: " + bookId);
                    return null;
                }
                System.out.println("Book ratings fetched successfully for book ID: " + bookId);
                return rating;
            } catch (JsonProcessingException e) {
                System.out.println("Error parsing book ratings JSON: " + responseText);
                return null;
            }
        }
    }

    public static void suggestBooks(int id, List<BookData> selectedBooks) {
        SocketConnection sc = MainFrame.getSocketConnection();

        String bookIds =  selectedBooks.stream()
                .map(book -> String.valueOf(book.getId()))
                .reduce((id1, id2) -> id1 + "," + id2)
                .orElse("");
        System.out.println("Suggesting books with IDs: " + bookIds + " for book ID: " + id);

        sc.send("SUGGEST_BOOK;" + id + "," + bookIds + ";" + UserState.cf);

        Response response = sc.receive();
        if (response.isError()) {
            System.out.println("Error suggesting books: " + response.getResponseText().substring(6));
        } else {
            System.out.println("Books suggested successfully for book ID: " + id);
        }
    }

    public static List<FavouriteCategoryBook> fetchProfileBooks() throws Exception {
        SocketConnection sc = MainFrame.getSocketConnection();

        sc.send("GET_PROFILE_BOOKS; " + " ;" + UserState.cf);

        return sc.receiveUntilStop(FavouriteCategoryBook.class);
    }
}
