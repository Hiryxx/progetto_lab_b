package connection;

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

    public void send(String message) {
        out.println(message);
        //out.flush();
    }

    public String receive() throws IOException {
        String line = in.readLine();
        in.readLine();
        return line;
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
