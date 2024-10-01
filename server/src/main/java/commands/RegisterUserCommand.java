package commands;

import data.Route;
import database.User;
import database.UserManager;
import managers.CollectionManager;
import statuses.ExceptionStatus;
import statuses.OKResponseStatus;
import statuses.Status;

public class RegisterUserCommand extends Command {

    public final CollectionManager collectionManager;
    public final UserManager userManager;

    public RegisterUserCommand(CollectionManager collectionManager, UserManager userManager) {
        super("regiater", "регистрация");
        this.collectionManager = collectionManager;
        this.userManager = userManager;
    }

    @Override
    public Status execute(String commandParts, Route route, User user) {
        try {
            var credentials = commandParts.split("\\|");
            var userId = userManager.addUser(credentials[0], credentials[1]);
            return new OKResponseStatus(Integer.toString(userId));
        } catch (Exception e) {
            return new ExceptionStatus("Произошла ошибка при авторизации: " + e);
        }
    }
}