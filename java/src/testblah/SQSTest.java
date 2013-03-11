package testblah;

import com.xerox.amazonws.common.Result;
import com.xerox.amazonws.sns.NotificationService;
import com.xerox.amazonws.sns.SNSException;
import com.xerox.amazonws.sqs2.*;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 3/11/13
 * Time: 1:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class SQSTest {

    public static void main(String[] args) {
        try {
            int timeout = 0;
            String s = "EISeleniumAlanDbBlah" + System.currentTimeMillis();
            NotificationService ns = new NotificationService("0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
            MessageQueue mq = SQSUtils.connectToQueue(s, "0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
            mq.addPermission("Allow", "808335860417", "SendMessage");

            mq.setQueueAttribute("Policy", "{\n" +
                    "  \"Version\": \"2008-10-17\",\n" +
                    "  \"Id\": \"arn:aws:sqs:us-east-1:808335860417:" + s + "/SQSDefaultPolicy\",\n" +
                    "  \"Statement\": [\n" +
                    "    {\n" +
                    "      \"Sid\": \"Sid1363034700583\",\n" +
                    "      \"Effect\": \"Allow\",\n" +
                    "      \"Principal\": {\n" +
                    "        \"AWS\": \"*\"\n" +
                    "      },\n" +
                    "      \"Action\": \"SQS:SendMessage\",\n" +
                    "      \"Resource\": \"arn:aws:sqs:us-east-1:808335860417:" + s + "\",\n" +
                    "      \"Condition\": {\n" +
                    "        \"ArnEquals\": {\n" +
                    "          \"aws:SourceArn\": \"arn:aws:sns:us-east-1:808335860417:develop-selenium\"\n" +
                    "        }\n" +
                    "      }\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}");
            Result<String> rs = ns.subscribe("arn:aws:sns:us-east-1:808335860417:develop-selenium", "sqs", "arn:aws:sqs:us-east-1:808335860417:" + s);
            System.out.println(rs.getResult());



            while (timeout < 1200) {
                Message message = mq.receiveMessage();
                System.out.println("message...");
                if (message == null) {
                    timeout++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        // ignore
                    }
                } else {
                    System.out.println("recieved.");

                    System.out.println(new String(message.getMessageBody().getBytes(), Charset.forName("US-ASCII")));
                    mq.deleteMessage(message);
                }
            }
        } catch (SQSException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SNSException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
