package commands;

import data.Route;
import database.User;
import managers.CollectionManager;
import statuses.OKStatus;
import statuses.Status;

import java.util.ArrayDeque;

/**Класс команды clear*/
public class DistanceDownCommand extends Command{
    private final CollectionManager collectionManager;

    public DistanceDownCommand(CollectionManager collectionManager) {
        super("descending_distance", "вывести значения поля distance всех элементов в порядке убывания");
        this.collectionManager = collectionManager;
    }

    /**
     * Исполняет команду
     */
    @Override
    public Status execute(String commandParts, Route route, User user) {
        collectionManager.sortByDistanceDown(route);

        return new OKStatus();
    }
}