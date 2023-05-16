import org.alessandrosalerno.framedtcp.FramedWriter;
import org.alessandrosalerno.framedtcp.FramedReader;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Test1 {
    public static void main(String[] args) {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(8000)) {
                Socket client = serverSocket.accept();
                FramedReader reader = new FramedReader(new InputStreamReader(client.getInputStream()));
                FramedWriter writer = new FramedWriter(new OutputStreamWriter(client.getOutputStream()));
                writer.writeBytes(reader.readBytes());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try (Socket socket = new Socket("", 8000)) {
                    FramedReader reader = new FramedReader(new InputStreamReader(socket.getInputStream()));
                    FramedWriter writer = new FramedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    writer.writeString("Hello world");
                    System.out.println("ECHO: " + reader.readString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}