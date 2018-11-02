import events.ProcessEvent;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import states.CurrentState;
import states.State;
import utils.FileReader;
import utils.OldValueRemover;
import utils.QueueManager;

public class Main
{
    private static final Runnable receiveAndPrintMessagesRunnable = () -> QueueManager.receiveAndPrintMessages();
    private static final Runnable oldValueRemoverRunnable = () -> OldValueRemover.removeOldValues();
    private static final Runnable cleanupRunnable = () -> cleanup();
    private static final Runnable averageValuesRunnable = () -> ProcessEvent.readyProcessEvent();

    private static final Logger LOGGER = Logger.getLogger(Main.class);

    public static void main(String[] args)
    {
        PropertyConfigurator.configure("log4j.properties");
        System.out.println("Main loaded.");

        QueueManager.createNewQueue();
        QueueManager.subscribeQueueToTopic();

        Runtime.getRuntime().addShutdownHook(new Thread(cleanupRunnable));

        FileReader.parseLocationsFile();

        CurrentState.setCurrentState(State.RECEIVING_MESSAGES);
        Thread averageValuesThread = new Thread(averageValuesRunnable);
        Thread oldValueRemoverThread = new Thread(oldValueRemoverRunnable);
        Thread receiveAndPrintMessagesThread = new Thread(receiveAndPrintMessagesRunnable);
        averageValuesThread.start();
        oldValueRemoverThread.start();
        receiveAndPrintMessagesThread.start();
    }

    private static void cleanup()
    {
        CurrentState.setCurrentState(states.State.CLOSING);
        QueueManager.deleteQueue();
    }
}
