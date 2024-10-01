package utility;

import data.Route;
import exeptions.ExitProgramException;
import statuses.Request;
import validators.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {

    private String login;
    private String password;
    private Integer userId;


    public static Map<String, Validator> validators;
    private static SocketChannel socketChannel;
    private final int port = System.getenv("PORT") != null ?
            Integer.parseInt(System.getenv("PORT")) : 8090;


    public Client() {
        validators = new HashMap<>() {
            {
                put("add", new AddAndRemoveLowerValidator());
                put("clear", new NoArgumentsValidator());
                put("execute_script", new ExecuteScriptValidator());
                put("help", new NoArgumentsValidator());
                put("info", new NoArgumentsValidator());
                put("descending_distance", new NoArgumentsValidator());
                put("remove", new OneIntArgValidator());
                put("show", new NoArgumentsValidator());
                put("update", new UpdateByIdValidator());
                put("add_if_min", new OneIntArgValidator());
            }
        };
    }

    private Integer processAuthorizationDialog(Scanner scanner) throws IOException, ClassNotFoundException {
        String line;

        System.out.println("Для авторизации введите 1, для регистрации введите 2:");
        System.out.println(">> ");
        line = scanner.nextLine();
        while (!line.equals("1") && !line.equals("2")) {
            System.out.println("Некорректный ввод. Попробуйте еще раз.");
            System.out.println(">> ");
            line = scanner.nextLine();
        }

        System.out.println("\nВведите логин пользователя:");
        System.out.println(">> ");
        this.login = scanner.nextLine();
        System.out.println("Введите пароль пользователя:");
        System.out.println(">> ");
        this.password = scanner.nextLine();

        if (line.equals("1")) {
            var req = new Request("login",  login + "|" + password, null);
            var resp = RequestManager.getResponseStatus(socketChannel, req);


            if (resp.getResult().equals("Exception")) {
                System.out.println("Ошибка авторизации: " + resp.getResponse());
                return null;
            }
            System.out.println("Авторизация прошла успешно. Ваш id: " + resp.getResponse());
            return Integer.parseInt(resp.getResponse());
        }

        var req = new Request("register",  login + "|" + password, null);
        var resp = RequestManager.getResponseStatus(socketChannel, req);
        if (resp.getResult().equals("Exception")) {
            System.out.println("Ошибка при регистрации: " + resp.getResponse());
            return null;
        }
        System.out.println("Регистрация прошла успешно. Ваш id: " + resp.getResponse());
        return Integer.parseInt(resp.getResponse());
    }

    public void start () throws ExitProgramException, IOException { //тут ввод с консоли
        try {
            openSocket();
            socketChannel.connect(new InetSocketAddress("localhost", port));

            Scanner scanner = new Scanner(System.in);
            String line;

            userId = processAuthorizationDialog(scanner);
            closeSocket();
            openSocket();
            socketChannel.connect(new InetSocketAddress("localhost", port));

            if (userId == null) {
                closeSocket();
                return;
            }

            System.out.println("\nДанное консольное приложение реализует управление коллекцией объектов типа " +
                    "Route.\nДля справки о командах введите help.");

            while (true) {
                try {
                    System.out.println("\nВведите название команды:");
                    System.out.print(">> ");
                    line = scanner.nextLine();
                    line = line.replaceAll("\\s{2,}", " ");
                    Request request = RequestManager.lineToRequest(line, false, userId);
                    if (request == null) {
                        continue;
                    }
                    request.setLogin(login);
                    request.setPassword(password);
                    RequestManager.handleRequest(socketChannel, request, userId);
                    closeSocket();
                    openSocket();
                    socketChannel.connect(new InetSocketAddress("localhost", port));
                } catch (NoSuchElementException e) {
                    closeSocket();
                    System.out.println("Достигнут конец ввода, завершение работы программы...");
                    throw new ExitProgramException();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            closeSocket();
        }
    }

    public void openSocket() throws IOException {
        socketChannel = SocketChannel.open();
    }

    public void closeSocket() throws IOException {
        if (socketChannel != null && socketChannel.isOpen()) {
            socketChannel.close();
        }
    }

    public static SocketChannel getSocketChannel() {
        return socketChannel;
    }
}