package com.easyinsight.api.v3;

import com.easyinsight.admin.HealthListener;
import com.easyinsight.config.ConfigLoader;
import com.easyinsight.database.EIConnection;
import com.xerox.amazonws.sqs2.Message;
import com.xerox.amazonws.sqs2.MessageQueue;
import com.xerox.amazonws.sqs2.SQSException;
import com.xerox.amazonws.sqs2.SQSUtils;
import nu.xom.Document;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 1/26/11
 * Time: 10:51 AM
 */
public class HealthServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            MessageQueue msgQueue = SQSUtils.connectToQueue("EIHealth" + ConfigLoader.instance().isProduction(), "0AWCBQ78TJR8QCY8ABG2",
                    "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
            MessageQueue messageQueue = SQSUtils.connectToQueue("EIHealthReturn" + ConfigLoader.instance().isProduction(), "0AWCBQ78TJR8QCY8ABG2",
                    "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
            String message = "blah";
            msgQueue.sendMessage(message);
            int messagesReceived = 0;
            long threshold = System.currentTimeMillis() + (1000 * 60);
            List<Response> responses = new ArrayList<Response>();
            int count = ConfigLoader.instance().isProduction() ? 2 : 1;
            while (messagesReceived < count && System.currentTimeMillis() < threshold) {
                Message response = messageQueue.receiveMessage();
                if (response == null) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        // ignore
                    }
                } else {
                    messageQueue.deleteMessage(response);
                    messagesReceived++;
                    String messageBody = response.getMessageBody();
                    String[] tokens = messageBody.split(",");
                    String host = tokens[0];
                    String code = tokens[1];
                    String messageString = tokens[2];
                    responses.add(new Response(host, code, messageString));
                }
            }
            String response = "<response><status>Success</status><message>All Good</message></response>";
            if (messagesReceived < count) {
                response = "<response><status>Failure</status><message>At least one of the application servers failed to respond within one minute.</message></response>";
            } else {
                for (Response responseObject : responses) {
                    if (responseObject.code.equals(HealthListener.FAILURE)) {
                        response = "<response><status>Failure</status><message>" + responseObject.message + "</message></response>";
                        break;
                    }
                }
            }
            resp.getOutputStream().write(response.getBytes());
            resp.getOutputStream().flush();
        } catch (SQSException e) {
            e.printStackTrace();
        }
    }

    private static class Response {
        private String host;
        private String code;
        private String message;

        private Response(String host, String code, String message) {
            this.host = host;
            this.code = code;
            this.message = message;
        }
    }
}
