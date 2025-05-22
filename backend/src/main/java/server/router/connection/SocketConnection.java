package server.router.connection;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import server.router.connection.response.MultiResponse;
import server.router.connection.response.Sendable;
import server.router.connection.response.SingleResponse;
import utils.JSONUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class SocketConnection {
    Socket socket;

    public SocketConnection(Socket socket) {
        this.socket = socket;
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
    public void send(PrintWriter out, Sendable response) throws IOException {
        switch (response){
            case SingleResponse singleResponse -> {
                out.write(singleResponse.object());
                out.flush();
            }
            case MultiResponse multiResponse -> {
                var stream = multiResponse.stream();
                stream.forEach(item -> {
                    try {
                        if (item instanceof ResultSet resultSet) {
                            out.println(JSONUtil.resultSetToJson(resultSet));
                        } else {
                            out.println((Object) null);
                        }
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

    public InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    public OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }
}
