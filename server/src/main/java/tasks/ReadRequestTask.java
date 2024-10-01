package tasks;

import statuses.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;

public class ReadRequestTask implements Runnable{
    private final SocketChannel client;
    private final ExecutorService executorService;

    public ReadRequestTask(SocketChannel client, ExecutorService executorService) {
        this.client = client;
        this.executorService = executorService;
    }

    @Override
    public void run() {

        try {
            ObjectInputStream ois = new ObjectInputStream(client.socket().getInputStream());
            Request request = (Request) ois.readObject();;
            System.out.println(request);

            ExecuteRequestTask task = new ExecuteRequestTask(request, client);
            executorService.submit(task);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}