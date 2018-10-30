package utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import java.io.*;

public class FileReader
{
    private static final String bucket = System.getenv("AWS_BUCKET");
    private static final String locationsFileName = "locations.json";

    public static String readLocationsFile()
    {
        final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try
        {
            S3Object o = s3.getObject(bucket, locationsFileName);
            S3ObjectInputStream s3is = o.getObjectContent();

            br = new BufferedReader(new InputStreamReader(s3is));
            String line;

            while((line = br.readLine()) != null)
            {
                sb.append(line);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(br != null)
            {
                try
                {
                    br.close();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }
}
