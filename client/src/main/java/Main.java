import exeptions.ExitProgramException;
import utility.Client;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.start();
        } catch (ExitProgramException exit) {
            System.out.println("Спасибо что использовали меня. До скорой встречи! (\u2060≧\u2060▽\u2060≦\u2060)");
        } catch (IOException e) {
            System.out.println("Что-то пошло не так: " + e.getMessage() + ". Завершение работы...");
            System.out.println("Спасибо что использовали меня. До скорой встречи! (\u2060≧\u2060▽\u2060≦\u2060)");
        }
    }
}