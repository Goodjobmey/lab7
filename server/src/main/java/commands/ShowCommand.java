package commands;

import data.Route;
import database.User;
import managers.CollectionManager;
import statuses.OKResponseStatus;
import statuses.Status;

public class ShowCommand extends Command{
    private final CollectionManager collectionManager;

    public ShowCommand(CollectionManager collectionManager) {
        super("show", "ввысести все элементы коллекции");
        this.collectionManager = collectionManager;
    }

    public Status execute(String commandParts, Route route, User user) {
        return new OKResponseStatus(collectionManager.show());
    }
}
