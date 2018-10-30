import org.apache.log4j.PropertyConfigurator;
import utils.FileReader;
import utils.QueueManager;

public class Main
{
    public static void main(String[] args)
    {
        PropertyConfigurator.configure("log4j.properties");
        System.out.println("Main loaded.");
        QueueManager.createNewQueue();
        QueueManager.subscribeQueueToTopic();
        System.out.println(FileReader.readLocationsFile());
        QueueManager.deleteQueue();
    }
}
