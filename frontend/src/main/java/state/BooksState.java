package state;

import classes.MainFrame;
import connection.SocketConnection;
import data.BookData;
import json.JsonObject;
import json.JsonUtil;

import java.util.ArrayList;
import java.util.List;

public class BooksState {
    public static ArrayList<BookData> books = new ArrayList<>();

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

            System.out.println("Book fetched: " + book.getTitle() + " by " + book.getAuthors());
        }

        return booksList;

    }

    public static void fetchBooks() throws Exception {
        SocketConnection sc = MainFrame.getSocketConnection();

        sc.send("GET_BOOKS");

        List<String> books = sc.receiveUntilStop();

        if (books == null || books.isEmpty()) {
            System.out.println("No books found in the library.");
            return;
        }

        BooksState.books.clear();
        for (String bookJson : books) {
            BookData book = JsonUtil.fromString(bookJson, BookData.class);
            BooksState.books.add(book);

            System.out.println("Book fetched: " + book.getTitle() + " by " + book.getAuthors());
        }

    }
}
