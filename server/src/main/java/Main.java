import exeptions.ExitProgramException;
import managers.ConnectionManager;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            //Test.test();
            System.out.println("--- --- -- -- - -  -  -   -");
                Server server = new Server();
                server.run();
            } catch (ExitProgramException exit) {
                ConnectionManager.getServerSocketChanel().close();
            System.out.println("Спасибо что использовали меня. До скорой встречи! (\u2060≧\u2060▽\u2060≦\u2060)");
                System.exit(0);
            } catch (IOException | IllegalArgumentException e) {
                ConnectionManager.getServerSocketChanel().close();
            System.out.println("Что-то пошло не так: " + e.getMessage() + ".\nЗавершение работы...");
            System.out.println("Спасибо что использовали меня. До скорой встречи! (\u2060≧\u2060▽\u2060≦\u2060)");
                System.exit(0);
            }
    }
}