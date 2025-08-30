package connection;

import json.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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

    public void send(String command, JsonObject jsonObject, String userCf) {
        // Format is: <command>;?<json>;?<userId>
        String jsonString = jsonObject.toString();
        String formattedCommand = String.format("%s;%s;%s", command, jsonString, userCf);
        out.println(formattedCommand);
    }

    public Response receive() {
        String line = readLineSafe();
        if (line == null) {
            return new Response("Error reading from socket", true);
        }

        if (line.startsWith("ERROR:")) {
            return new Response(line.substring(6).trim(), true);
        }
        return new Response(line, false);
    }

    public List<String> receiveUntilStop() {
        String line;
        List<String> messages = new ArrayList<>();
        System.out.println("Receiving messages...");

        while ((line = readLineSafe()) != null) {
            System.out.println("Received: " + line);
            if (line.equalsIgnoreCase("STOP")) {
                break;
            }
            if (line.startsWith("ERROR:")) {
                System.err.println("Error received from server: " + line);
                break;
            }
            messages.add(line);
        }

        return messages;
    }

    public <T> List<T> receiveUntilStop(Class<T> clazz) {
        String line;
        List<T> messages = new ArrayList<>();
        System.out.println("Receiving messages...");
        try {
            while ((line = readLineSafe()) != null) {
                System.out.println("Received: " + line);
                if (line.equalsIgnoreCase("STOP")) {
                    break;
                }
                if (line.startsWith("ERROR:")) {
                    System.err.println("Error received from server: " + line);
                    break;
                }
                T parsed = json.JsonUtil.fromString(line, clazz);
                messages.add(parsed);
            }
        } catch (IOException e) {
            System.err.println("Error reading from socket: " + e.getMessage());
            return messages; // Return what we have so far
        }
        return messages;
    }

    private String readLineSafe() {
        try {
            StringBuilder line = new StringBuilder();
            int ch;

            while ((ch = in.read()) != -1) {
                char c = (char) ch;

                if (c == '\\') {
                    // Check if next character is 'n'
                    int next = in.read();
                    if (next == 'n') {
                        // Found our delimiter \\n
                        break;
                    } else {
                        // Not our delimiter, add both characters
                        line.append(c);
                        if (next != -1) {
                            line.append((char) next);
                        }
                    }
                } else {
                    line.append(c);
                }
            }

            return line.toString();
        } catch (IOException e) {
            System.err.println("Error reading from socket: " + e.getMessage());
            return null;
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
