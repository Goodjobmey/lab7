package commands;

import data.Route;
import database.User;
import exeptions.NoAccessException;
import managers.CollectionManager;
import statuses.ExceptionStatus;
import statuses.OKResponseStatus;
import statuses.Status;

/**Класс команды update id {element}*/
public class UpdateCommand extends Command {
    private final CollectionManager collectionManager;

    public UpdateCommand(CollectionManager collectionManager) {
        super("update {element}", "обновить значение элемента коллекции по ID");
        this.collectionManager = collectionManager;
    }

    /**
     * Исполняет команду
     */
    @Override
    public Status execute(String commandParts, Route route, User user) {
        int indexOfElement = Integer.parseInt(commandParts);

        if (collectionManager.checkExistence(indexOfElement)) {
            try {
                collectionManager.updateById(indexOfElement, route);
            } catch (NoAccessException e) {
                return new ExceptionStatus(e.toString());
            }
            return new OKResponseStatus("Элемент с данным индексом был обновлен");
        } else {
            return new ExceptionStatus("Такого индекса не существует");
        }
    }
}