import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import states.CurrentState;
import states.State;
import utils.FileReader;
import utils.QueueManager;

public class Main
{
    private static final Runnable receiveAndPrintMessagesRunnable = () -> QueueManager.receiveAndPrintMessages();
    private static final Runnable cleanupRunnable = () -> cleanup();

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
        Thread receiveAndPrintMessagesThread = new Thread(receiveAndPrintMessagesRunnable);
        receiveAndPrintMessagesThread.start();
    }

    private static void cleanup()
    {
        CurrentState.setCurrentState(states.State.CLOSING);
        QueueManager.deleteQueue();
    }
}
