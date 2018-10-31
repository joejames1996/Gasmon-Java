package models;

import java.util.ArrayList;
import java.util.List;

public class Location
{
    private float x;
    private float y;
    private String id;

    private static List<Location> locations = new ArrayList<>();

    public Location(float x, float y, String id)
    {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public static List<Location> getLocations()
    {
        return locations;
    }

    public static void setLocations(List<Location> locations)
    {
        Location.locations = locations;
    }

    public float getX()
    {
        return x;
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public float getY()
    {
        return y;
    }

    public void setY(float y)
    {
        this.y = y;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return "ID: " + this.id + " | X: " + this.x + " | Y: " + this.y;
    }
}
