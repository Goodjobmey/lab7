package commands;

import data.Route;
import database.User;
import exeptions.NoAccessException;
import managers.CollectionManager;
import statuses.ExceptionStatus;
import statuses.OKResponseStatus;
import statuses.Status;

import java.net.UnknownServiceException;
import java.util.Scanner;

/**Класс команды remove_by_id id*/
public class RemoveByIdCommand extends Command{
    CollectionManager collectionManager;
    Scanner scanner;

    public RemoveByIdCommand(CollectionManager collectionManager) {
        super("remove", "удалить элемент из коллекции по ID");
        this.collectionManager = collectionManager;
    }

    /**
     * Исполняет команду
     */
    @Override
    public Status execute(String commandParts, Route route, User user) {
        int index = Integer.parseInt(commandParts);
        if (collectionManager.checkExistence(index)) {
            try {
                collectionManager.removeById(index, user);
            } catch (NoAccessException e) {
                return new ExceptionStatus(e.toString());
            }
            return new OKResponseStatus("Элемент с заданным индексом найден и удален");
        } else {
            return new ExceptionStatus("Такого индекса не существует");
        }
    }
}