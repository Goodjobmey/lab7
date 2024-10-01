package data;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private long x;
    private Double y; //Значение поля должно быть больше -102, Поле не может быть null

    public long getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Coordinates(long x, Double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return x + ";" + y;
    }


    public boolean validate() {
        return y != null && y >= -102;
    }
}