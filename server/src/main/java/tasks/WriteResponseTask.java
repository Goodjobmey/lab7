package tasks;

import statuses.Status;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.channels.SocketChannel;

public class WriteResponseTask implements Runnable {
    private Status status;
    private final SocketChannel client;


    public WriteResponseTask (Status status, SocketChannel client) {
        this.status = status;
        this.client = client;
    }
    @Override
    public void run() {
        try (client) {
            ObjectOutputStream oos = new ObjectOutputStream(client.socket().getOutputStream());
            oos.writeObject(status); //отправляем клиенту
        } catch (IOException e) {
            System.out.println("Не получилось передать данные клиенту" + e);
        }
    }
}