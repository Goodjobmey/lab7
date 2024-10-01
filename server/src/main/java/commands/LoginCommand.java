package commands;

import data.Route;
import database.User;
import database.UserManager;
import managers.CollectionManager;
import statuses.ExceptionStatus;
import statuses.OKResponseStatus;
import statuses.Status;

public class LoginCommand extends Command{

    public final CollectionManager collectionManager;
    public final UserManager userManager;

    public LoginCommand(CollectionManager collectionManager, UserManager userManager) {
        super("login", "Авторизация");
        this.collectionManager = collectionManager;
        this.userManager = userManager;
    }

    @Override
    public Status execute(String commandParts, Route route, User user) {
        try {
            var credentials = commandParts.split("\\|");
            user = userManager.getUser(credentials[0], credentials[1]);
            if (user != null) {
                return new OKResponseStatus(Integer.toString(user.getId()));
            }
            return new ExceptionStatus("Логин или пароль неверны");
        } catch (Exception e) {
            return new ExceptionStatus("Произошла ошибка при авторизации: " + e);
        }
    }
}