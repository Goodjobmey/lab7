package commands;

import data.Route;
import database.User;
import managers.CollectionManager;
import statuses.OKResponseStatus;
import statuses.Status;

public class InfoCommand extends Command{
    private CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager) {
        super("info", "\"вывести информацию о коллекции\"");
        this.collectionManager = collectionManager;
    }

    @Override
    public Status execute(String commandParts, Route route, User user) {
        return new OKResponseStatus(this.collectionManager.info());
    }

}
