package managers;

import commands.*;
import data.Route;
import database.UserManager;
import exeptions.ExitProgramException;
import exeptions.UnknownCommandException;
import statuses.ExceptionStatus;
import statuses.Status;

import java.util.*;

/**
 * Класс для хранения команд
 */
public class CommandManager {


    private  static Map<String, Command> commands;
    private final CollectionManager cm;
    private final UserManager userManager;

    public CommandManager(CollectionManager cm) {
        this.cm = cm;
        commands = new HashMap<>();
        this.userManager = new UserManager();
        this.setCommands();
    }


    public void setCommands(){
        commands.put("show", new ShowCommand(cm));
        commands.put("info", new InfoCommand(cm));
        commands.put("help", new HelpCommand(this));
        commands.put("add", new AddCommand(cm));
        commands.put("update", new UpdateCommand(cm));
        commands.put("remove", new RemoveByIdCommand(cm));
        commands.put("clear", new ClearCommand(cm));
        commands.put("descending_distance", new DistanceDownCommand(cm));
        commands.put("add_if_min", new AddIfMinCommand(cm));

        commands.put("login", new LoginCommand(cm, userManager));
        commands.put("register", new RegisterUserCommand(cm, userManager));
    }

    public Status execute(String commandName, String args, Route route, String login, String password) throws ExitProgramException {
        Command command = commands.get(commandName);
        var user = userManager.getUser(login, password);
        if (user == null && command != commands.get("login") && command != commands.get("register")) {
            return new ExceptionStatus("Вы не авторизованы");
        }
        Status status = command.execute(args, route, user);
        return status;
    }

    /**
     * Метод для получения команды по её названию
     * @param arg название команды, введенное пользователем
     * @return ссылку на объект команды
     */
    public static Command getCommand(String arg) throws UnknownCommandException {
        var command = commands.get(arg);
        if (command == null)
            throw new UnknownCommandException("");
        return commands.get(arg);
    }

    /**
     * Метод для получения списка всех команд
     * @return список команд
     */
    public static Collection<Command> getCommands() {
        return commands.values();
    }

    public CollectionManager getCollectionManager() {
        return cm;
    }

}
