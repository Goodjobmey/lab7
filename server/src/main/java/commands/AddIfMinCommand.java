package commands;

import data.Route;
import database.User;
import managers.CollectionManager;
import statuses.OKResponseStatus;
import statuses.Status;

import java.util.Collection;
import java.util.Objects;

public class AddIfMinCommand extends Command{

    private final CollectionManager collectionManager;

    public AddIfMinCommand(CollectionManager collectionManager) {
        super("add_if_min", "добавить элемент в коллекцию если его поле distanse наименьшее");
        this.collectionManager = collectionManager;
    }


    public boolean checkIfMin(Route newRoute) {
        Collection<Route> collection = collectionManager.getCollection();
        if (collection == null) {
            return true;
        }
        return newRoute.compareTo(collection.stream().filter(Objects::nonNull).
                min(Route::compareTo).orElse(null)) < 0;
    }

    /**
     * Исполняет команду
     */
    @Override
    public Status execute(String args, Route newRoute, User user) {
        if (checkIfMin(newRoute)) {
            return collectionManager.add(newRoute);
        }
        return new OKResponseStatus("Объект оказался больше минимального.");
    }

}
