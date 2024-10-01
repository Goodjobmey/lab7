package data;

import java.io.Serializable;

public class Location implements Serializable {
    private Double x; //Поле не может быть null
    private int y;
    private int z;

    public Double getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Location(Double x, int y, int z){
        this.z = z;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return x + ";" + y + ";" + z;
    }


    public boolean validate() {
        if (x == null) return false;

        return true;

    }
}