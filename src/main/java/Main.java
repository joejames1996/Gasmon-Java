import org.apache.log4j.PropertyConfigurator;
import utils.FileReader;

public class Main
{
    public static void main(String[] args)
    {
        PropertyConfigurator.configure("log4j.properties");
        System.out.println("Main loaded.");
        System.out.println(FileReader.readLocationsFile());
    }
}
