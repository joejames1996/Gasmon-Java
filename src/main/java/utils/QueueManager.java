package utils;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import com.amazonaws.services.sns.util.Topics;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.Notification;
import org.apache.log4j.Logger;
import states.CurrentState;
import states.State;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class QueueManager
{
    private static final Random random = new Random();
    private static final String queueName = System.getenv("AWS_QUEUE_NAME") + "-" + random.nextInt(999999);
    private static final String topicArn = System.getenv("AWS_TOPIC_ARN");
    private static final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
    private static final AmazonSNS sns = AmazonSNSAsyncClientBuilder.defaultClient();

    private static final Logger LOGGER = Logger.getLogger(QueueManager.class);

    private static String currentQueueUrl;

    private static void deleteUnusedQueues()
    {
        for(final String queueUrl : sqs.listQueues().getQueueUrls())
        {
            LOGGER.info("Found existing queue: " + queueUrl);
            if(queueUrl.contains(System.getenv("AWS_QUEUE_NAME")))
            {
                try
                {
                    sqs.deleteQueue(queueUrl);
                    LOGGER.info("Deleted unused queue: " + queueUrl);
                }
                catch(Exception e)
                {
                    LOGGER.error("Could not delete queue " + queueUrl + ": " + e);
                }
            }
        }
    }

    public static void createNewQueue()
    {
        deleteUnusedQueues();
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

    public static void receiveAndPrintMessages()
    {
        try
        {
            while(CurrentState.getCurrentState() == State.RECEIVING_MESSAGES)
            {
                Thread.sleep(1000);
                if (currentQueueUrl != null)
                {
                    List<Message> messages = sqs.receiveMessage(new ReceiveMessageRequest(currentQueueUrl)).getMessages();
                    if (messages.size() > 0)
                    {
                        for (final Message message : messages)
                        {
                            String body = message.getBody();
                            Notification notification = new Gson().fromJson(body, Notification.class);
                            Notification.addNewNotificationToList(notification);

                            LOGGER.info("Created notification:\n" + notification.toString());

                            notification.createEventFromNotification();
                        }
                    }
                    else
                    {
                        LOGGER.info("No messages are available.");
                    }
                }
                else
                {
                    LOGGER.error("Can't receive messages (currentQueueUrl is null).");
                }
            }
        }
        catch(Exception e)
        {
            LOGGER.error(e);
        }
    }
}
