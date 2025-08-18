package connection;

import json.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketConnection implements AutoCloseable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public SocketConnection(Socket socket) {
        this.socket = socket;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String command) {
        out.println(command);
    }

    public void send(String command, JsonObject jsonObject) {
        // Format is: <command>;?<json>;?<userId>
        String jsonString = jsonObject.toString();
        String formattedCommand = String.format("%s;%s", command, jsonString);
        out.println(formattedCommand);
    }

    public void send(String command, JsonObject jsonObject, String userId) {
        // Format is: <command>;?<json>;?<userId>
        String jsonString = jsonObject.toString();
        String formattedCommand = String.format("%s;%s;%s", command, jsonString, userId);
        out.println(formattedCommand);
    }

    public Response receive() {
        String line;
        try {
            line = in.readLine();
           // in.readLine();
        } catch (IOException e) {
            System.err.println("Error reading from socket: " + e.getMessage());
            return new Response("Error reading from socket", true);
        }

        if (line.startsWith("ERROR:")) {
            return new Response(line.substring(6).trim(), true);
        }
        return new Response(line, false);
    }

    // try method for now
    public void receiveUntilStop() throws IOException {
        String line;
        System.out.println("Receiving messages...");
        while ((line = in.readLine()) != null) {
            System.out.println("Received: " + line);
            if (line.equalsIgnoreCase("STOP")) {
                break;
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public PrintWriter getOut() {
        return out;
    }

    public BufferedReader getIn() {
        return in;
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
