package com.easyinsight.collaboration;

import flex.messaging.MessageBroker;
import flex.messaging.util.UUIDUtils;
import flex.messaging.messages.AsyncMessage;

/**
 * User: James Boe
 * Date: Feb 9, 2009
 * Time: 1:17:29 PM
 */
public class UserNotification {
    public static void sendNotification(long userID, String message) {
        MessageBroker msgBroker = MessageBroker.getMessageBroker(null);
        String clientID = UUIDUtils.createUUID();
        AsyncMessage msg = new AsyncMessage();
        msg.setDestination("generalNotification");
        msg.setHeader("userid", String.valueOf(userID));
        msg.setMessageId(clientID);
        msg.setTimestamp(System.currentTimeMillis());
        msgBroker.routeMessageToService(msg, null);
    }
}
