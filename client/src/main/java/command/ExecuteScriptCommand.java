package command;

import exeptions.ExitProgramException;
import exeptions.RecursiveCallException;
import statuses.Request;
import utility.Client;
import utility.RequestManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;

/**Класс команды execute_script script*/
public class ExecuteScriptCommand {
    private static BufferedReader reader = null;
    private static String lastReadFromFile = null;

    private static final Stack<String> files = new Stack<>();
    public static void execute(String filePath, int userId) throws ExitProgramException {
        try {
            File ioFile = new File(filePath);
            if (!ioFile.canWrite() || ioFile.isDirectory() || !ioFile.isFile())
                throw new IOException();
            if (files.contains(filePath))
                throw new RecursiveCallException();
            files.add(filePath);

            reader = new BufferedReader(new FileReader(filePath));

            String line = reader.readLine(); //начинаем считывать файл
            while (line != null) {
                System.out.println("\nИсполнение команды " + line + " : ");
                Request request = RequestManager.lineToRequest(line, true, userId);
                RequestManager.handleRequest(Client.getSocketChannel(), request, userId);
                while (lastReadFromFile != null) {
                    String s = new String(lastReadFromFile);
                    lastReadFromFile = null;
                    System.out.println("\nИсполнение команды " + s + " : ");
                    request = RequestManager.lineToRequest(s, true, userId);
                    RequestManager.handleRequest(Client.getSocketChannel(), request, userId);
                }
                line = reader.readLine();

            }
            files.remove(filePath);
            if (files.isEmpty())
                reader.close();
        } catch (IOException ex) {
            System.out.println("Доступ к файлу невозможен "+ ex.getMessage());
        } catch (RecursiveCallException r) {
            System.out.println("Скрипт " + filePath + " уже был вызван (Рекурсивный вызов)");
        }
    }

    /**
     * Метод считывающий значения полей для типа Route
     * @return массив полей
     */
    public static ArrayList<String> readAllArguments() {
        ArrayList<String> allArgs = new ArrayList<>();
        try {
            String line; //начинаем считывать файл
            Set<String> commands = Client.validators.keySet();
            line = reader.readLine();
            while (line != null && !commands.contains(line)) {
                allArgs.add(line);
                line = reader.readLine();
            }
            lastReadFromFile = line;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return allArgs;
    }
}