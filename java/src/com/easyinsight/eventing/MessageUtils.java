package com.easyinsight.eventing;

import flex.messaging.messages.AsyncMessage;
import flex.messaging.MessageBroker;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 5, 2009
 * Time: 11:19:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class MessageUtils {

    public static void sendMessage(String destination, Object messageBody) {
        AsyncMessage message = new AsyncMessage();
        message.setDestination(destination);
        message.setMessageId(UUID.randomUUID().toString());
        message.setBody(messageBody);
        if (MessageBroker.getMessageBroker(null) != null) {
            MessageBroker.getMessageBroker(null).routeMessageToService(message, null);
        }
    }
}
