package state;

import classes.MainFrame;
import com.fasterxml.jackson.core.JsonProcessingException;
import connection.Response;
import connection.SocketConnection;
import data.BookData;
import data.LibraryData;
import json.JsonObject;
import json.JsonUtil;

import java.util.ArrayList;
import java.util.List;

public class LibraryState {
    public static List<LibraryData> libraries = new ArrayList<>();

    public static void fetchLibraries(){
        if (!libraries.isEmpty()) return;

        String userCf = UserState.cf;

        System.out.println("Requesting libraries for user: " + userCf);
        SocketConnection sc = MainFrame.getSocketConnection();

        sc.send("GET_LIBRARIES; " + " ;" + userCf);

        libraries = sc.receiveUntilStop(LibraryData.class);
    }

    public static Response addBookToLibrary(BookData bookData, int libraryId) {
        SocketConnection sc = MainFrame.getSocketConnection();

        JsonObject request = new JsonObject();

        System.out.println("Adding book to library: " + bookData.getId() + " to libraryId: " + libraryId);

        request.put("libraryid", libraryId);
        request.put("bookid", bookData.getId());

        sc.send("ADD_BOOK", request, UserState.cf);

       return sc.receive();

    }

    public static List<BookData> getRecommendableBooks(int bookId) {
        SocketConnection sc = MainFrame.getSocketConnection();
        JsonObject request = new JsonObject();
        request.put("bookid", bookId);

        sc.send("GET_RECOMMENDABLE_BOOKS", request, UserState.cf);

        List<BookData> recommendableBooks = sc.receiveUntilStop(BookData.class);

        if (recommendableBooks == null || recommendableBooks.isEmpty()) {
            System.out.println("No recommendable books found.");
        }

        return recommendableBooks;
    }
}
