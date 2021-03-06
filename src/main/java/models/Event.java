package models;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Event
{
    private String locationId;
    private String eventId;
    private float value;
    private long timestamp;

    private Location location = null;

    private long timeCreated;

    private static List<Event> eventList = new ArrayList<>();

    private static final Logger LOGGER = Logger.getLogger(Event.class);

    public Event(String locationId, String eventId, float value, long timestamp)
    {
        this.locationId = locationId;
        this.eventId = eventId;
        this.value = value;
        this.timestamp = timestamp;
    }

    public static List<Event> getEventList()
    {
        return eventList;
    }

    public static void addNewEventToList(Event event)
    {
        eventList.add(event);
    }

    public String getLocationId()
    {
        return locationId;
    }

    public void setLocationId(String locationId)
    {
        this.locationId = locationId;
    }

    public String getEventId()
    {
        return eventId;
    }

    public void setEventId(String eventId)
    {
        this.eventId = eventId;
    }

    public float getValue()
    {
        return value;
    }

    public void setValue(float value)
    {
        this.value = value;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    public void setTimeCreated()
    {
        this.timeCreated = (System.currentTimeMillis() / 1000L);
    }

    public void setLocationForEvent()
    {
        for(Location location : Location.getLocations())
        {
            if(location.getId().equals(this.locationId))
            {
                LOGGER.info("Event:\n" + this.toString() + "\nfound matching location:\n" + location.toString());
                this.location = location;
                break;
            }
        }
    }

    public boolean alreadyExists()
    {

        for(Event event : eventList)
        {
            if(event.getEventId().equals(this.getEventId()) || event.equals(this))
                return true;
        }
        return false;
    }

    public static void removeOldValues()
    {
        long currentTime = (System.currentTimeMillis() / 1000L);
        eventList.removeIf(e -> (e.timeCreated < currentTime - 300L));
    }

    @Override
    public String toString()
    {
        return  "    locationId : " + this.locationId + "\n" +
                "    eventId : " + this.eventId + "\n" +
                "    value : " + this.value + "\n" +
                "    timestamp : " + this.timestamp;
    }
}
