package commands;

import data.Route;
import database.User;
import exeptions.ExitProgramException;
import statuses.Status;

public abstract class Command {
    boolean anArgument = false;
    private String name;
    private String description;

    public Command(String name, String description){
        this.name = name;
        this.description = description;
    }
    /**
     * Абстрактный метод для выполнения тела команды
     */
    public Status execute(String commandParts, Route route, User user) throws ExitProgramException {
        return null;
    }

    /**
     * Метод, возвращающий проверку функции на наличие/отсутствие аргумента
     * @return true, если наличие/отсутствие аргумента корректно, false в обратном случае
     */
    public boolean hasAnArgument(int length) {
        if (anArgument && (length > 1)) return true;
        if (!anArgument && (length == 1)) return true;
        return false;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
