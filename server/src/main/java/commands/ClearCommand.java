package commands;

import data.Route;
import database.User;
import managers.CollectionManager;
import statuses.OKStatus;
import statuses.Status;

/**Класс команды clear*/
public class ClearCommand extends Command{
    private final CollectionManager collectionManager;

    public ClearCommand(CollectionManager collectionManager) {
        super("clear", "очистить коллекцию");
        this.collectionManager = collectionManager;
    }


    /**
     * Исполняет команду
     */
    @Override
    public Status execute(String commandParts, Route route, User user) {
        collectionManager.clear(user);
        return new OKStatus();
    }
}
