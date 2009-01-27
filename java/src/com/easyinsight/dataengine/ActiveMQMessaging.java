package com.easyinsight.dataengine;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;

import com.easyinsight.logging.LogClass;

/**
 * User: James Boe
 * Date: Aug 5, 2008
 * Time: 9:26:11 PM
 */
public class ActiveMQMessaging implements IMessaging {

    private Connection connection;
    private Map<String, TopicMetadata> topicMap = new HashMap<String, TopicMetadata>();
    private Map<String, QueueMetadata> queueMap = new HashMap<String, QueueMetadata>();

    public void start() {
        try {

            String url = "vm://localhost:61616";
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
            connection = connectionFactory.createConnection();
            connection.start();

            /*TopicConnectionFactory topicFactory = new MantaTopicConnectionFactory();
            topicConnection = topicFactory.createTopicConnection();
            topicConnection.start();
            QueueConnectionFactory queueFactory = new MantaQueueConnectionFactory();
            queueConnection = queueFactory.createQueueConnection();
            queueConnection.start();*/
        } catch (JMSException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        try {
            LogClass.debug("at least trying to close....");
            connection.close();
        } catch (JMSException e) {
            LogClass.error(e);
        }
    }

    public void sendMessage(String topicName, Serializable engineRequest, Map<String, String> properties) {
        try {
            TopicMetadata topicMetadata = getTopicMetadata(topicName);
            ObjectMessage objectMessage = topicMetadata.topicSession.createObjectMessage();
            objectMessage.setObject(engineRequest);
            if (properties != null) {
                for (Map.Entry<String, String> entry : properties.entrySet()) {
                    objectMessage.setStringProperty(entry.getKey(), entry.getValue());
                }
            }
            topicMetadata.topicPublisher.send(objectMessage);
        } catch (JMSException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void sendPointToPoint(String queueName, Serializable engineRequest, Map<String, String> properties) {
        try {
            QueueMetadata queueMetadata = getQueueMetadata(queueName);
            ObjectMessage objectMessage = queueMetadata.queueSession.createObjectMessage();
            objectMessage.setObject(engineRequest);
            if (properties != null) {
                for (Map.Entry<String, String> entry : properties.entrySet()) {
                    objectMessage.setStringProperty(entry.getKey(), entry.getValue());
                }
            }
            queueMetadata.queueSender.send(objectMessage);
        } catch (JMSException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private QueueMetadata getQueueMetadata(String queueName) throws JMSException {
        QueueMetadata queueMetadata = queueMap.get(queueName);
        if (queueMetadata == null) {
            Session queueSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = queueSession.createQueue(queueName);
            MessageProducer queueSender = queueSession.createProducer(queue);
            queueMetadata = new QueueMetadata(queueSession, queueSender);
            queueMap.put(queueName, queueMetadata);
        }
        return queueMetadata;
    }

    public void subscribeToTopic(final String topicName, final MessageListener messageListener) throws JMSException {
        Thread thread = new Thread(new Runnable() {

            public void run() {
                try {
                    Session subscriberSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                    Topic topic = subscriberSession.createTopic(topicName);
                    MessageConsumer topicSubscriber = subscriberSession.createConsumer(topic);
                    topicSubscriber.setMessageListener(messageListener);
                } catch (Exception e) {
                    LogClass.error(e);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void subscribeToQueue(final String queueName, final MessageListener messageListener, final String selector) throws JMSException {
        Thread thread = new Thread(new Runnable() {

            public void run() {
                try {
                    Session queueSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                    Queue queue = queueSession.createQueue(queueName);
                    MessageConsumer queueReceiver = queueSession.createConsumer(queue, selector);
                    queueReceiver.setMessageListener(messageListener);
                } catch (Exception e) {
                    LogClass.error(e);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();

    }

    private TopicMetadata getTopicMetadata(String topicName) throws JMSException {
        TopicMetadata topicMetadata = topicMap.get(topicName);
        if (topicMetadata == null) {
            Session topicSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = topicSession.createTopic(topicName);
            MessageProducer topicPublisher = topicSession.createProducer(topic);
            topicMetadata = new TopicMetadata(topicSession, topicPublisher);
            topicMap.put(topicName, topicMetadata);
        }
        return topicMetadata;
    }

    private static class TopicMetadata {
        private Session topicSession;
        private MessageProducer topicPublisher;

        private TopicMetadata(Session topicSession, MessageProducer topicPublisher) {
            this.topicSession = topicSession;
            this.topicPublisher = topicPublisher;
        }
    }

    private static class QueueMetadata {
        private Session queueSession;
        private MessageProducer queueSender;

        private QueueMetadata(Session queueSession, MessageProducer queueSender) {
            this.queueSession = queueSession;
            this.queueSender = queueSender;
        }
    }
}