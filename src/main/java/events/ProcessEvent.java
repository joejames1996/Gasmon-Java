package events;

import models.Event;
import org.apache.log4j.Logger;
import states.CurrentState;
import states.State;

import java.util.ArrayList;
import java.util.List;

public class ProcessEvent
{
    private static long timestamp = 0L;

    private static final int minutesToDelay = 5;

    private static final Logger LOGGER = Logger.getLogger(ProcessEvent.class);

    public static void readyProcessEvent()
    {
        try
        {
            Thread.sleep(60000 * minutesToDelay);
            LOGGER.info("Done sleeping readyProcessEvent()");
            averageValues();
        }
        catch(InterruptedException e)
        {
            LOGGER.error(e);
        }
    }

    private static void averageValues()
    {
        while(CurrentState.getCurrentState() == State.RECEIVING_MESSAGES)
        {
            timestamp = ((System.currentTimeMillis() - (60000L * minutesToDelay)));
            LOGGER.info("averageValues() timestamp = " + timestamp);
            try
            {
                List<Float> values = new ArrayList<>();
                for (Event event : Event.getEventList())
                {
                    LOGGER.info("Checking event " + event);
                    LOGGER.info("Timestamp: " + timestamp + " eventTimestamp: " + event.getTimestamp() + " Timestamp+60: " + (timestamp + 60L));
                    if (event.getTimestamp() > timestamp && event.getTimestamp() < (timestamp + 60L))
                    {
                        LOGGER.info("Event " + event + " added!");
                        values.add(event.getValue());
                        LOGGER.info("Added event " + event.getEventId() + " with timestamp " + event.getTimestamp());
                    }
                }

                if (values.size() > 0)
                {
                    float value = 0.0f;
                    for (float f : values)
                    {
                        value += f;
                    }

                    LOGGER.info("Average value is " + value / values.size());
                }

                Thread.sleep(60000);
            } catch (InterruptedException e)
            {
                LOGGER.error(e);
            } catch (Exception e)
            {
                LOGGER.error(e);
            }
        }
    }

    public static long getTimestamp()
    {
        return timestamp;
    }
}
