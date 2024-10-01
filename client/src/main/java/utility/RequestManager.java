package utility;

import command.ExecuteScriptCommand;
import exeptions.ExitProgramException;
import exeptions.UnknownCommandException;
import org.apache.commons.lang3.SerializationUtils;
import statuses.Request;
import statuses.Status;
import validators.Validator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Locale;
import java.util.NoSuchElementException;

public class RequestManager {
    private static int depth = 0;

    private static boolean isInFile = false;

    /**
     * Метод обрабатывающий строку и составляющий запрос из этой строки
     * @param line строка для обработки
     * @param isInFile находимся ли мы в файле. От этого зависит способ обработки
     * @return готовый запрос с верными аргументами
     * @throws ExitProgramException исключение для выхода из программы
     * @throws NoSuchElementException исключение если не существует такого элемента
     */
    public static Request lineToRequest(String line, boolean isInFile, int user_id) throws ExitProgramException, NoSuchElementException {
        if (line.isEmpty()) {
            return null;
        }
        String[] wordsOfLine = line.trim().split(" ");
        String commandName = wordsOfLine[0].toLowerCase(Locale.ROOT);
        if (wordsOfLine.length > 2) {
            System.out.println("Некорректное количество аргументов. Для справки введите help");
        } else if (wordsOfLine.length == 1) {
            try {
                Validator.checkIfValidCommand(commandName, Client.validators.keySet());
                Validator validator = Client.validators.get(commandName);

                if (validator.getNeedParse()) {
                    return validator.validate(commandName, null, isInFile, user_id);
                }
                return validator.validate(commandName, null);
            } catch (UnknownCommandException e) {
                System.out.println("Неизвестная команда, напишите help для просмотра доступных");
            }
        } else {
            try {
                if (commandName.equals("execute_script")) {
                    handleExecuteScript(wordsOfLine[1], user_id);
                    return null;
                } else {
                    Validator.checkIfValidCommand(commandName, Client.validators.keySet());
                    Validator validator = Client.validators.get(commandName);

                    if (validator.getNeedParse()) {
                        return validator.validate(commandName, wordsOfLine[1], isInFile, user_id);
                    }
                    return validator.validate(commandName, wordsOfLine[1]);
                }
            } catch (UnknownCommandException e) {
                System.out.println("Неизвестная команда, напишите help для просмотра доступных");
            }
        }
        return null;
    }

    /**
     * Метод для обработки запроса
     * @param socketChannel сокет через который передаются данные
     * @param request запрос
     * @throws ExitProgramException исключение для выхода из программы
     */
    public static void handleRequest(SocketChannel socketChannel, Request request, int userId) throws IOException, ExitProgramException {
        if (request == null)
            return;
        if (request.getCommand().equals("execute_script")) {
            handleExecuteScript(request.getArgs(), userId);
        } else {
            makeRequest(socketChannel, request);
        }
    }

    /**
     * Метод для обработки команды execute_script
     * @param filename имя скрипта
     * @throws ExitProgramException исключение для выхода из программы
     */
    public static void handleExecuteScript(String filename, int userId) throws ExitProgramException {
        ExecuteScriptCommand.execute(filename, userId);
    }

    /**
     * Метод отправляющий запрос и получающий ответ
     * @param socketChannel сокет через который передаются данные
     * @param request запрос
     */
    public static void makeRequest(SocketChannel socketChannel, Request request) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(socketChannel.socket().getOutputStream());
        oos.writeObject(request); //отправили запрос на сервер
        ObjectInputStream ois = new ObjectInputStream(socketChannel.socket().getInputStream());
        Status status = null;
        try {
            status = (Status) ois.readObject();
        } catch (ClassNotFoundException ignored) {}

        if (status.getResult().equals("Exception")) {
            System.out.println(status.getResponse());
        } else {
            System.out.println(status.getResponse());
        }
    }

    public static Status getResponseStatus(SocketChannel socketChannel, Request request) throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(socketChannel.socket().getOutputStream());
        oos.writeObject(request); //отправили запрос на сервер
        ObjectInputStream ois = new ObjectInputStream(socketChannel.socket().getInputStream());
        return (Status) ois.readObject();
    }


    public static void setIsInFile(boolean isInFile) {
        RequestManager.isInFile = isInFile;
    }

    public static boolean isIsInFile() {
        return isInFile;
    }
}