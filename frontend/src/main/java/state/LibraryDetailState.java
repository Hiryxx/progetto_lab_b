package state;

import classes.MainFrame;
import connection.Response;
import connection.SocketConnection;


public class LibraryDetailState {
    public static int libraryId = -1;
    public static String libraryName = null;


    public static boolean isBookInLibrary(int bookId) {
        SocketConnection sc = MainFrame.getSocketConnection();

        sc.send("IS_BOOK_IN_LIBRARY"+ ";" + bookId  + "," + UserState.cf +  ";" + UserState.cf);
        Response response = sc.receive();

        if (response.isError()) {
            System.err.println("Error checking if book is in library: " + response.getResponseText());
            return false;
        }

        return response.getResponseText().equalsIgnoreCase("true");
    }
}
