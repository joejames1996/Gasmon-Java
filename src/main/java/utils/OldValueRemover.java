package utils;

import models.Event;
import models.Notification;
import org.apache.log4j.Logger;
import states.CurrentState;
import states.State;

public class OldValueRemover
{
    private static final Logger LOGGER = Logger.getLogger(OldValueRemover.class);

    public static void removeOldValues()
    {
        while(CurrentState.getCurrentState() == State.RECEIVING_MESSAGES)
        {
            try
            {
                Thread.sleep(10000);

//                LOGGER.info("Removing old values... Notifications size before: " + Notification.getNotifications().size() + " | Events size before: " + Event.getEventList().size());
                LOGGER.info("Removing old values... Events size before: " + Event.getEventList().size());
                //Notification.removeOldValues();
                Event.removeOldValues();
//                LOGGER.info("Removed old values... Notifications size after: " + Notification.getNotifications().size() + " | Events size after: " + Event.getEventList().size());
                LOGGER.info("Removed old values... Events size after: " + Event.getEventList().size());

            }
            catch(Exception e)
            {
                LOGGER.error(e);
            }
        }
    }
}
