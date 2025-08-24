package state;

import classes.MainFrame;
import com.fasterxml.jackson.core.JsonProcessingException;
import connection.Response;
import connection.SocketConnection;
import data.BookData;
import data.BookRatingData;
import data.FilterData;
import json.JsonObject;
import json.JsonUtil;

import java.util.ArrayList;
import java.util.List;

public class BooksState {
    public static ArrayList<BookData> books = new ArrayList<>();
    public static BookData bookDetail = null;
    public static FilterData[] categories = null;
    public static FilterData[] authors = null;

    public static ArrayList<BookData> fetchLibraryBooks(String libraryId) throws Exception {
        SocketConnection sc = MainFrame.getSocketConnection();
        JsonObject request = new JsonObject();
        request.put("libraryId", libraryId);

        sc.send("GET_LIBRARY_BOOKS", request, UserState.cf);

        List<String> books = sc.receiveUntilStop();

        if (books == null || books.isEmpty()) {
            System.out.println("No books found for library: " + libraryId);
            return new ArrayList<>();
        }

        ArrayList<BookData> booksList = new ArrayList<>();
        for (String bookJson : books) {
            BookData book = JsonUtil.fromString(bookJson, BookData.class);
            booksList.add(book);

            // System.out.println("Book fetched: " + book.getTitle() + " by " + book.getAuthors());
        }

        return booksList;

    }

    public static void fetchBooks() throws Exception {
        SocketConnection sc = MainFrame.getSocketConnection();

        sc.send("GET_BOOKS;12");

        List<String> books = sc.receiveUntilStop();

        BooksState.books.clear();

        if (books == null || books.isEmpty()) {
            System.out.println("No books found in the library.");
            return;
        }

        for (String bookJson : books) {
            BookData book = JsonUtil.fromString(bookJson, BookData.class);
            BooksState.books.add(book);

            // System.out.println("Book fetched: " + book.getTitle() + " by " + book.getAuthors());
        }

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

        List<String> cats = sc.receiveUntilStop();

        if (cats == null || cats.isEmpty()) {
            System.out.println("No categories found.");
            return;
        }

        categories = new FilterData[cats.size() + 1];

        categories[0] = new FilterData(-1, "Tutte le categorie");

        for (int i = 1; i < cats.size(); i++) {
            String jsonCategory = cats.get(i);
            FilterData filter;
            try {
                FilterData filterData = JsonUtil.fromString(jsonCategory, FilterData.class);
                filter = filterData;
            } catch (JsonProcessingException e) {
                System.out.println("Error parsing category JSON: " + jsonCategory);
                continue;
            }
            categories[i] = filter;
        }

    }

    public static void fetchAuthors() {
        if (authors != null) {
            return;
        }
        SocketConnection sc = MainFrame.getSocketConnection();

        sc.send("GET_AUTHORS");

        List<String> aut = sc.receiveUntilStop();

        if (aut == null || aut.isEmpty()) {
            System.out.println("No authors found.");
            return;
        }

        authors = new FilterData[aut.size() + 1];

        authors[0] = new FilterData(-1, "Tutti gli autori");

        for (int i = 1; i < aut.size(); i++) {
            String jsonAuthor = aut.get(i);
            FilterData filter;
            try {
                FilterData filterData = JsonUtil.fromString(jsonAuthor, FilterData.class);
                filter = filterData;
            } catch (JsonProcessingException e) {
                System.out.println("Error parsing category JSON: " + jsonAuthor);
                continue;
            }
            authors[i] = filter;
        }

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

        List<String> books = sc.receiveUntilStop();

        BooksState.books.clear();
        if (books == null || books.isEmpty()) {
            System.out.println("No books found in the library.");

            return;
        }

        for (String bookJson : books) {
            BookData book = JsonUtil.fromString(bookJson, BookData.class);
            BooksState.books.add(book);

            System.out.println("Book fetched: " + book.getTitle() + " by " + book.getAuthors());
        }
    }

    public static List<BookData> getBookSuggestions(int bookId) {
        SocketConnection sc = MainFrame.getSocketConnection();
        JsonObject request = new JsonObject();
        request.put("bookid", bookId);

        sc.send("GET_BOOK_SUGGESTIONS", request);

        List<String> books = sc.receiveUntilStop();

        List<BookData> suggestions = new ArrayList<>();

        if (books == null || books.isEmpty()) {
            System.out.println("No book suggestions found.");
            return suggestions;
        }

        for (String bookJson : books) {
            BookData book;
            try {
                book = JsonUtil.fromString(bookJson, BookData.class);
            } catch (JsonProcessingException e) {
                System.out.println("Error parsing book suggestion JSON: " + bookJson);
                continue;
            }
            suggestions.add(book);

            System.out.println("Book suggestion fetched: " + book.getTitle() + " by " + book.getAuthors());
        }

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
}
