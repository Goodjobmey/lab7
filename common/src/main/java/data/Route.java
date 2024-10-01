package data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Objects;

public class Route implements Validator, Comparable<Route>, Serializable {

    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private final String name; //Поле не может быть null, Строка не может быть пустой
    private final Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private final Location from; //Поле может быть null
    private final Location to; //Поле может быть null
    private final Float distance; //Поле может быть null, Значение поля должно быть больше 1
    public static int uniqueId;
    private int ownerUserId;

    public Route(int id, String name, Coordinates coordinates, java.time.LocalDateTime creationDate, Location from, Location to, Float distance, int ownerUserId) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.from = from;
        this.to = to;
        this.distance = distance;
        this.ownerUserId = ownerUserId;
    }

    public Route(String name, Coordinates coordinates, Location from, Location to, Float distance, int ownerUserId){
        this.name = name;
        this.coordinates = coordinates;
        this.from = from;
        this.to = to;
        this.distance = distance;
        this.creationDate = LocalDateTime.now();
        this.ownerUserId = ownerUserId;
    }

    public int getId() {
        return this.id;
    }

    public Float getDistance() {
        return distance;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }

    public int getOwnerUserId() {
        return ownerUserId;
    }

    /**
     * Устанавливает id равное данному
     * @param newId новое id
     */
    public void setId(int newId) {
        this.id = newId;
    }

    @Override
    public boolean validate() {
        if (id < 0) return false;
        if (name == null || name.isEmpty()) return false;
        if (coordinates == null) return false;
        if (from == null) return false;
        if (to == null) return false;
        if (distance == null || distance < 1) return false;

        return true;
    }

    /**
     * Обновляет минимальный не использованный id
     * @param collection коллекция, для которой происходит обновление уникального id
     */
    public static void updateUniqueId (ArrayDeque<Route> collection) {
        if (collection == null)
            uniqueId = 1;
        else
            uniqueId = collection.stream().filter(Objects::nonNull)
                    .map(Route::getId)
                    .mapToInt(Integer::intValue)
                    .max().orElse(0) + 1;
    }

    @Override
    public String toString() {
        return "route{\"id\": " + id + ", " +
                "\"name\": " + name + ", "+
                "\"date\" " + creationDate + ", " +
                "\"location\": " + coordinates + ", "+
                "\"from\": " + from + ", "+
                "\"to\": " + to + ", "+
                "\"distance\": " + distance + ", " +
                "\"owner_id\": " + ownerUserId + ", "+"}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return Double.compare(route.distance, distance) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }

    /**
     * Сравнивает два объекта на основе поля distanse
     * @param other объект для сравнения
     */
    @Override
    public int compareTo(Route other) {
        if (Objects.isNull(other)) return 1;
        return Float.compare(this.distance, other.distance);
    }
}