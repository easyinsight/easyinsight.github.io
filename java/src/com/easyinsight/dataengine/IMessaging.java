package com.easyinsight.dataengine;

import javax.jms.MessageListener;
import javax.jms.JMSException;
import java.io.Serializable;
import java.util.Map;

/**
 * User: James Boe
 * Date: Aug 5, 2008
 * Time: 6:51:36 PM
 */
public interface IMessaging {

    void start();

    void stop();

    void sendMessage(String topicName, Serializable engineRequest, Map<String, String> properties);

    void sendPointToPoint(String queueName, Serializable engineRequest, Map<String, String> properties);

    void subscribeToTopic(String topicName, MessageListener messageListener) throws JMSException;

    void subscribeToQueue(String queueName, MessageListener messageListener, String selector) throws JMSException;
}
