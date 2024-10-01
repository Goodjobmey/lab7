package commands;

import data.Route;
import database.User;
import managers.CollectionManager;
import managers.CommandManager;
import statuses.OKResponseStatus;
import statuses.Status;

import java.util.Collection;
import java.util.Map;

public class HelpCommand extends Command{
    private CommandManager commandManager;
    private static Map<Command, String> commands;

    public HelpCommand(CommandManager commandManager) {
        super("help", "вывести список доступных команд");
        this.commandManager = commandManager;
    }

    @Override
    public Status execute(String commandParts, Route route, User user) {
        StringBuilder result = new StringBuilder();
        for (Command c : CommandManager.getCommands()) {
            result.append(c.getName());
            result.append(":  ");
            result.append(c.getDescription());
            result.append("\n");
        }

        return new OKResponseStatus(result.toString());
    }

}
