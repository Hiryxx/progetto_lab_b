package server.connection;


import server.connection.response.ErrorResponse;
import server.connection.response.MultiResponse;
import server.connection.response.Sendable;
import server.connection.response.SingleResponse;
import utils.JSONUtil;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.SQLException;

/**
 * SocketConnection is a class that represents a connection to a client socket.
 * It provides methods to send and receive data over the socket.
 */
public class SocketConnection {
    Socket socket;
    PrintWriter out;
    BufferedReader in;

    public SocketConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public Socket getSocket() {
        return socket;
    }

    /***
     * Sends a response to the client.
     * @param response the response to send
     * @throws IOException if an I/O error occurs
     */
    public void send(Sendable response) throws IOException {
        switch (response){
            case SingleResponse singleResponse -> {
                out.println(singleResponse.object());
                out.flush();
            }
            case MultiResponse multiResponse -> {
                var stream = multiResponse.getQueryResult().stream();
                stream.forEach(item -> {
                    try {
                        out.println(JSONUtil.resultSetToJson(item));
                        out.flush();
                    } catch (SQLException e) {
                        System.err.println("Error converting ResultSet to JSON: " + e.getMessage());
                    }
                });
            }
            case ErrorResponse errorResponse -> {
                out.println("ERROR: " + errorResponse.getErrorMessage());
                out.flush();
            }
        }

    }

    public void sendStopMessage() throws IOException {
        out.println("STOP");
        out.flush();
    }


    public InetAddress getInetAddress() {
        return socket.getInetAddress();
    }

    public void close() throws IOException {
        socket.close();
    }

    public BufferedReader getIn() {
        return in;
    }

    public PrintWriter getOut() {
        return out;
    }
}
