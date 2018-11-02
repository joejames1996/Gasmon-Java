package events;

import models.Event;
import org.apache.log4j.Logger;
import utils.OldValueRemover;

import java.util.ArrayList;
import java.util.List;

public class ProcessEvent
{
    public static final int minutesToWait = 1;
    private static final Logger LOGGER = Logger.getLogger(ProcessEvent.class);

    private static long lowestTimestamp = 0L;

    public static void processEvents()
    {
        lowestTimestamp = System.currentTimeMillis() - (minutesToWait * 1000);

        LOGGER.info("Called processEvents()");

        try
        {
            List<Float> eventValues = new ArrayList<>();
            for(Event e : Event.getEventList())
            {
                if(e.getTimestamp() > lowestTimestamp && e.getTimestamp() < (lowestTimestamp + 60000))
                {
                    eventValues.add(e.getValue());
                    OldValueRemover.addEventToRemove(e);
                    LOGGER.info("Found event within timestamp range:\n" + e);
                }
            }

            if(eventValues.size() > 0)
            {
                float allVals = 0.0f;
                for(float val : eventValues)
                {
                    allVals += val;
                }
                float avgVal = allVals / eventValues.size();
                LOGGER.info("Average value = " + avgVal);
            }
        }
        catch(Exception e)
        {
            LOGGER.error(e);
        }
    }

    public static long getLowestTimestamp()
    {
        return lowestTimestamp;
    }
}
