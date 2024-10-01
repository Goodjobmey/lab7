package managers;

import statuses.Request;
import exeptions.ExitProgramException;
import org.apache.commons.lang3.SerializationUtils;
import statuses.Status;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ConnectionManager {
    private static ServerSocketChannel serverSocketChanel;
    private static Selector selector;
    private static int port;
    private static CollectionManager collectionManager;

    /**
     * Метод для установки подключения
     */
    public static void connect() throws IOException {
        port = System.getenv("PORT") != null ? Integer.parseInt(System.getenv("PORT")) : 8090;

        serverSocketChanel = ServerSocketChannel.open();
        serverSocketChanel.bind(new InetSocketAddress("localhost", port));
        serverSocketChanel.configureBlocking(false);
    }

    public static SocketChannel getClientSocket() throws IOException {
        return serverSocketChanel.accept();
    }
    public static Selector getSelector() {
        return selector;
    }

    public static ServerSocketChannel getServerSocketChanel() {
        return serverSocketChanel;
    }

    public static void setCollectionManager(CollectionManager collectionManager) {
        ConnectionManager.collectionManager = collectionManager;
    }

    public static CollectionManager getCollectionManager() {
        return collectionManager;
    }
}

