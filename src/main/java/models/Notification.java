package models;

import com.google.gson.Gson;
import events.ProcessEvent;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Notification
{
    private String Type;
    private String MessageId;
    private String TopicArn;
    private String Message;
    private String Timestamp;
    private int SignatureVersion;
    private String Signature;
    private String SigningCertURL;
    private String UnsubscribeURL;

    private long timeCreated;

    //private static List<Notification> notifications = new ArrayList<>();

    private static final Logger LOGGER = Logger.getLogger(Notification.class);

    public Notification(String type, String messageId, String topicArn, String message, String timestamp, int signatureVersion, String signature, String SigningCertURL, String unsubscribeUrl)
    {
        this.Type = type;
        this.MessageId = messageId;
        this.TopicArn = topicArn;
        this.Message = message;
        this.Timestamp = timestamp;
        this.SignatureVersion = signatureVersion;
        this.Signature = signature;
        this.SigningCertURL = SigningCertURL;
        this.UnsubscribeURL = unsubscribeUrl;
    }

//    public static List<Notification> getNotifications()
//    {
//        return notifications;
//    }
//
//    public static void addNewNotificationToList(Notification notification)
//    {
//        notifications.add(notification);
//    }

    public String getType()
    {
        return Type;
    }

    public void setType(String type)
    {
        this.Type = type;
    }

    public String getMessageId()
    {
        return MessageId;
    }

    public void setMessageId(String messageId)
    {
        this.MessageId = messageId;
    }

    public String getTopicArn()
    {
        return TopicArn;
    }

    public void setTopicArn(String topicArn)
    {
        this.TopicArn = topicArn;
    }

    public String getMessage()
    {
        return Message;
    }

    public void setMessage(String message)
    {
        this.Message = message;
    }

    public String getTimestamp()
    {
        return Timestamp;
    }

    public void setTimestamp(String timestamp)
    {
        this.Timestamp = timestamp;
    }

    public int getSignatureVersion()
    {
        return SignatureVersion;
    }

    public void setSignatureVersion(int signatureVersion)
    {
        this.SignatureVersion = signatureVersion;
    }

    public String getSignature()
    {
        return Signature;
    }

    public void setSignature(String signature)
    {
        this.Signature = signature;
    }

    public String getSigningCertURL()
    {
        return SigningCertURL;
    }

    public void setSigningCertURL(String signingCertURL)
    {
        this.SigningCertURL = signingCertURL;
    }

    public String getUnsubscribeURL()
    {
        return UnsubscribeURL;
    }

    public void setUnsubscribeURL(String unsubscribeURL)
    {
        this.UnsubscribeURL = unsubscribeURL;
    }

    public void setTimeCreated()
    {
        this.timeCreated = (System.currentTimeMillis() / 1000L);
    }

    public void createEventFromNotification()
    {
        Event event = new Gson().fromJson(this.Message, Event.class);
        event.setTimeCreated();

        if(!event.alreadyExists())
        {
            event.setLocationForEvent();
            Event.addNewEventToList(event);

            LOGGER.debug("Created new event:\n" + event.toString());
        }
        else
        {
            LOGGER.debug(event.toString() + "\n...already exists and was not remade.");
        }
    }

//    public static void removeOldValues()
//    {
//        //long currentTime = (System.currentTimeMillis() / 1000L);
//        notifications.removeIf(n -> (n.getTimestampAsLong() < ProcessEvent.getTimestamp()));
//    }

    public long getTimestampAsLong()
    {
        LOGGER.debug("Converting " + this.Timestamp + " ...");
        try
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date parsedDate = dateFormat.parse(this.Timestamp);
            java.sql.Timestamp timestamp = new Timestamp(parsedDate.getTime());
            LOGGER.debug("...to " + timestamp.getTime());
            return timestamp.getTime();
        }
        catch(Exception e)
        {
            LOGGER.error(e);
        }
        return 0L;
    }

    @Override
    public String toString()
    {
        return  "    Type : " + Type + "\n" +
                "    MessageId : " + MessageId + "\n" +
                "    TopicArn : " + TopicArn + "\n" +
                "    Message : " + Message + "\n" +
                "    Timestamp : " + Timestamp + "\n" +
                "    SignatureVersion : " + SignatureVersion + "\n" +
                "    Signature : " + Signature + "\n" +
                "    SigningCertURL : " + SigningCertURL + "\n" +
                "    UnsubscribeURL : " + UnsubscribeURL;
    }
}
