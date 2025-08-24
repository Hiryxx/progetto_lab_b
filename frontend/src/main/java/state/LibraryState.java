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
    public static ArrayList<LibraryData> libraries = new ArrayList<>();

    public static void fetchLibraries(){
        if (!libraries.isEmpty()) return;

        String userCf = UserState.cf;

        System.out.println("Requesting libraries for user: " + userCf);
        SocketConnection sc = MainFrame.getSocketConnection();

        sc.send("GET_LIBRARIES; " + " ;" + userCf);

        List<String> libraries = sc.receiveUntilStop();

        for (String libraryJson : libraries) {
            if (libraryJson.startsWith("ERROR:")) {
                System.err.println("Error fetching libraries: " + libraryJson.substring(6));
                return;
            } else {
                System.out.println("Received library data: " + libraryJson);
                LibraryData library = null;
                try {
                    library = JsonUtil.fromString(libraryJson, LibraryData.class);
                    System.out.println("Parsed library: " + library.getName());
                    System.out.println("Created at : " + library.getCreatedAt());
                } catch (JsonProcessingException e) {
                    System.out.println("Error parsing library JSON: " + e.getMessage());
                }
                if (library != null)
                    LibraryState.libraries.add(library);

            }
        }
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
}
