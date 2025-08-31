package state;

import classes.MainFrame;
import connection.Response;
import connection.SocketConnection;
import data.BookData;
import json.JsonObject;

import java.util.List;


public class LibraryDetailState {
    public static int libraryId = -1;
    public static String libraryName = null;

    public static List<BookData> libraryBooks;

    public static void fetchLibraryBooks(int libraryId) {
        SocketConnection sc = MainFrame.getSocketConnection();
        JsonObject libraryDetail = new JsonObject();
        libraryDetail.put("id", libraryId);

        sc.send("GET_LIBRARY_BOOKS", libraryDetail, UserState.cf);

        libraryBooks = sc.receiveUntilStop(BookData.class);
    }


    public static boolean isBookInLibrary(int bookId) {
        SocketConnection sc = MainFrame.getSocketConnection();

        sc.send("IS_BOOK_IN_LIBRARY"+ ";" + bookId +  ";" + UserState.cf);
        Response response = sc.receive();

        if (response.isError()) {
            System.err.println("Error checking if book is in library: " + response.getResponseText());
            return false;
        }

        return response.getResponseText().equalsIgnoreCase("true");
    }

    public static boolean isbBookRated(int bookId) {
        SocketConnection sc = MainFrame.getSocketConnection();

        sc.send("IS_BOOK_RATED"+ ";" + bookId  + ";" + UserState.cf);
        Response response = sc.receive();

        if (response.isError()) {
            System.err.println("Error checking if book is rated: " + response.getResponseText());
            return false;
        }

        return response.getResponseText().equalsIgnoreCase("true");
    }

    public static boolean isBookSuggested(int bookId) {
        SocketConnection sc = MainFrame.getSocketConnection();

        sc.send("IS_BOOK_SUGGESTED"+ ";" + bookId  +  ";" + UserState.cf);
        Response response = sc.receive();

        if (response.isError()) {
            System.err.println("Error checking if book is suggested: " + response.getResponseText());
            return false;
        }

        return response.getResponseText().equalsIgnoreCase("true");
    }
}
