package managers;

import exeptions.ExitProgramException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;

public class ServerConsoleInputManager {

    public static void checkConsoleInput() throws ExitProgramException{
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            if (reader.ready()) {
                String arg = reader.readLine();
                if (arg.equals("exit")) {
                    throw new ExitProgramException();
                } else {
                    System.out.println("Такой команды не существует.");
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Достигнут конец ввода, завершение работы программы...");
            throw new ExitProgramException();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}