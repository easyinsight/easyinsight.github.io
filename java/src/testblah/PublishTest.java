package testblah;

import com.xerox.amazonws.sns.NotificationService;
import com.xerox.amazonws.sns.SNSException;
import com.xerox.amazonws.sqs2.MessageQueue;
import com.xerox.amazonws.sqs2.SQSException;
import com.xerox.amazonws.sqs2.SQSUtils;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 3/11/13
 * Time: 1:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class PublishTest {
    public static void main(String[] args) {
        NotificationService ns = new NotificationService("0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
        try {
            ns.publish("arn:aws:sns:us-east-1:808335860417:develop-selenium", "asdf1234", null);
        } catch (SNSException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
