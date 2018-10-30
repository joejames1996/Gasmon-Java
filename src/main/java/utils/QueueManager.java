package utils;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import com.amazonaws.services.sns.util.Topics;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import org.apache.log4j.Logger;

public class QueueManager
{
    private static final String queueName = System.getenv("AWS_QUEUE_NAME");
    private static final String topicArn = System.getenv("AWS_TOPIC_ARN");
    private static final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
    private static final AmazonSNS sns = AmazonSNSAsyncClientBuilder.defaultClient();

    private static final Logger LOGGER = Logger.getLogger(QueueManager.class);

    private static String currentQueueUrl;

    public static void createNewQueue()
    {
        try
        {
            final CreateQueueRequest createQueueRequest = new CreateQueueRequest(queueName);
            final String queueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
            currentQueueUrl = queueUrl;

            LOGGER.info("Created queue " + queueName + " (" + currentQueueUrl + ").");
        }
        catch(Exception e)
        {
            LOGGER.error(e);
        }
    }

    public static void subscribeQueueToTopic()
    {
        try
        {
            if(currentQueueUrl != null)
            {
                Topics.subscribeQueue(sns, sqs, topicArn, currentQueueUrl);
                LOGGER.info("Subscribed queue " + currentQueueUrl + " to topic " + topicArn + ".");
            }
            else
            {
                LOGGER.error("Can't subscribe to topic (currentQueueUrl is null).");
            }
        }
        catch(Exception e)
        {
            LOGGER.error(e);
        }
    }

    public static void deleteQueue()
    {
        try
        {
            if(currentQueueUrl != null)
            {
                sqs.deleteQueue(new DeleteQueueRequest(currentQueueUrl));
                LOGGER.info("Deleted queue " + currentQueueUrl + ".");
            }
            else
            {
                LOGGER.error("Can't delete queue (currentQueueUrl is null).");
            }
        }
        catch(Exception e)
        {
            LOGGER.error(e);
        }
    }
}
