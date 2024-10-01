package managers;

import data.Route;
import database.RouteManager;
import database.User;
import exeptions.NoAccessException;
import statuses.ExceptionStatus;
import statuses.OKStatus;
import statuses.Status;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Класс для манипулирования коллекцией
 */
public class CollectionManager {
    ArrayDeque<Route> collection;
    LocalDateTime init;
    RouteManager routeManager;


    public CollectionManager(){
        this.collection = new ArrayDeque<>();
        init = LocalDateTime.now();
        this.routeManager = new RouteManager();
    }

    /**
     * Возвращает коллекцию
     * @return ArrayDeque
     */
    public ArrayDeque<Route> getCollection() {
        return this.collection;
    }

    /**
     * Выводит информацию о коллекции
     */
    public String info() {
        StringBuilder result = new StringBuilder();
        result.append("Коллекция: ");
        result.append(collection.getClass().getSimpleName());
        result.append("\nТип элементов коллекции: ");
        result.append(Route.class.getName());
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        result.append("\nВремя инициализации коллекции: ");
        result.append(init.format(timeFormatter));
        result.append("\nКолличество элементов: ");
        result.append(collection.size());
        return result.toString();
    }

    /**
     * Выводит элементы коллекции
     */
    public String show() {
        StringBuilder result = new StringBuilder();
        if (collection.isEmpty()) {
            return "Коллекция пуста";
        } else {
            for (Route r: collection) {
                result.append(r.toString()).append('\n');
            }
        }
        return result.toString();
    }

    /**
     * Проверяет, существует ли элемент, id которого равно данному
     * @param index индекс, по которому ищется элемент
     * @return true если существует, иначе false
     */
    public boolean checkExistence(int index) {
        return collection.stream().anyMatch((x) -> x.getId() == index);
    }

    /**
     * Добавляет новый элемент в коллекцию
     * @param route элемент, который нужно добавить
     */
    public Status add(Route route) {
        if (route == null) {
            return new ExceptionStatus("Объект типа Route равен null");
        } else {
            if (!route.validate()) {
                return new ExceptionStatus("Объект типа Route не соответствует требованиям. " +
                        "Проверьте корректность введенных значений и попробуйте еще раз.");
            } else {
                try {
                    Integer route1_id = routeManager.addRoute(route);
                    if (route1_id == null) {
                        return new ExceptionStatus("Ошибка при добавлении элемента в базу данных");
                    }
                    route.setId(route1_id);
                    collection.add(route);
                    return new OKStatus();
                } catch (Exception e) {
                    return new ExceptionStatus("Ошибка при добавлении элемента в базу данных: " + e);
                }
            }
        }
    }

    /**
     * Очищает коллекцию
     */
    public void clear(User owner) {
        collection.removeIf(route -> route.getOwnerUserId() == owner.getId());
        routeManager.deleteAllUserRoutes(owner.getId());
    }

    /**
     * Возвращает элемент коллекции у которого id соответствует данному индексу
     * @param index индекс, по которому ищется элемент
     * @return найденный элемент
     */
    public Route getById (int index) {
        for (Route element : collection) {
            if (element.getId() == index)
                return element;
        }
        return null;
    }

    /**
     * Обновляет все поля элемента коллекции, если его id равен данному
     * @param index индекс, по которому ищется элемент
     */
    public void updateById(int index, Route newRoute) {
        Route lastRoute = this.getById(index);
        if (lastRoute.getOwnerUserId() != newRoute.getOwnerUserId()) {
            throw new NoAccessException("Нельзя изменить элемент коллекции, которым владеет другой пользователь");
        }
        routeManager.updateRoute(lastRoute.getId(), newRoute);
        collection.remove(lastRoute);
        collection.add(newRoute);
    }

    /**
     * Удаляет элемент, id которого равен данному
     * @param index индекс, по которому ищется элемент
     */
    public void removeById(int index, User owner) throws NoAccessException {
        var route = routeManager.getRoute(index);
        if (route.getOwnerUserId() != owner.getId()) {
            throw new NoAccessException("Нельзя удалить элемент коллекции, которым владеет другой пользователь");
        }
        routeManager.deleteRoute(route.getId());
        collection.remove(getById(index));
    }

    public void sortByDistanceDown(Route route) {
        List<Route> list = new ArrayList<>(collection);

        list.sort(Comparator.comparingDouble(Route::getDistance).reversed());

        collection.clear();
        collection.addAll(list);

        for (Route r : collection) {
            System.out.println("id: " + r.getId() + " | дистанция: " + r.getDistance());
        }

    }

}
