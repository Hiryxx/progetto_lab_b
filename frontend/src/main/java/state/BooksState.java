package state;

import classes.MainFrame;
import com.fasterxml.jackson.core.JsonProcessingException;
import connection.SocketConnection;
import data.BookData;
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

}
