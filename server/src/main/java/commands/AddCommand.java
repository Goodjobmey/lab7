package commands;

import data.Route;
import database.User;
import managers.CollectionManager;
import statuses.Status;

/**Класс команды add {element}*/

public class AddCommand extends Command {
    private final CollectionManager collectionManager;

    public AddCommand(CollectionManager collectionManager) {
        super("add", "добавить новый элемент в коллекцию");
        this.collectionManager = collectionManager;
    }

    /**
     * Исполняет команду
     * @param route объект который нужно добавить
     */
    @Override
    public Status execute(String commandParts, Route route, User user) {
        return collectionManager.add(route);
    }
}