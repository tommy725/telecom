package backend;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SenderSocket {
    private Socket socket;
    private ServerSocket servsock;

    public SenderSocket() {
        try {
            servsock = new ServerSocket(12345);
            socket = servsock.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(byte[] bytesToSend) throws IOException {
        OutputStream socketOutputStream = socket.getOutputStream();
        socketOutputStream.write(bytesToSend); //send compressed message
    }
}
