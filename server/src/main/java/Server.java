import data.Route;
import database.RouteManager;
import exeptions.ExitProgramException;
import managers.*;
import tasks.ReadRequestTask;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final CollectionManager collectionManager;
    private final RouteManager routeManager;

    /**
     * Конструктор приложения
     */
    public Server() {
        this.collectionManager = new CollectionManager();
        this.routeManager = new RouteManager();
        ConnectionManager.setCollectionManager(collectionManager);
    }


    public void run() throws IOException, ExitProgramException {
        for (var v : routeManager.getAllRoutes()) {
            collectionManager.getCollection().add(v);
        }
        System.out.println("Подключение к базе данных выполнено успешно!");
        System.out.println("--- --- -- -- - -  -  -   -");

        ExecutorService executor1 = Executors.newCachedThreadPool();
        ExecutorService executor2 = Executors.newFixedThreadPool(8);
        SocketChannel socketChannel;
        try {
            ConnectionManager.connect();
            while (true) {
                ServerConsoleInputManager.checkConsoleInput();

                socketChannel = ConnectionManager.getClientSocket();
                if (socketChannel == null) {
                    continue;
                }
                handlerSocketChannel(socketChannel, executor1, executor2);
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        } catch (RuntimeException e) {
            System.out.println(e.toString());
        }
    }

    public void handlerSocketChannel(SocketChannel socketChannel, ExecutorService executor1, ExecutorService executor2) throws IOException {
        ReadRequestTask task = new ReadRequestTask(socketChannel, executor2);
        executor1.submit(task);
    }
}
