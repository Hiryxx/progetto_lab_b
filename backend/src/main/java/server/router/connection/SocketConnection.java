package server.router.connection;


import server.router.connection.response.MultiResponse;
import server.router.connection.response.Sendable;
import server.router.connection.response.SingleResponse;
import utils.JSONUtil;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SocketConnection {
    Socket socket;
    PrintWriter out;
    BufferedReader in;

    public SocketConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public Socket socket() {
        return socket;
    }

    //todo fix bad print writer usage

    /***
     * Sends a response to the client.
     * @param response
     * @throws IOException
     */
    public void send(Sendable response) throws IOException {
        switch (response){
            case SingleResponse singleResponse -> {
                out.println(singleResponse.object());
                out.flush();
            }
            case MultiResponse multiResponse -> {
                var stream = multiResponse.stream();
                stream.forEach(item -> {
                    try {
                        out.println(JSONUtil.resultSetToJson(item));
                        out.flush();
                    } catch (SQLException e) {
                        System.err.println("Error converting ResultSet to JSON: " + e.getMessage());
                    }
                });
            }
        }

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
