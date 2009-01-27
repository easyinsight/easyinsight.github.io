package com.easyinsight.billing;

import flex.messaging.messages.AsyncMessage;
import flex.messaging.MessageBroker;
import flex.messaging.util.UUIDUtils;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * User: James Boe
 * Date: Aug 3, 2008
 * Time: 3:10:36 PM
 */
public class TransactionStorage {
    public void addTransaction(Transaction transaction) {

    }

    private void poll() {
        // are there any transactions in our storage waiting on update?
        List<Transaction> transactions = getOpenTransactions();
        if (!transactions.isEmpty()) {
            /*List something = Pay.updateTransactions(transactions);
            for (Transaction transaction : transactions) {
                if (transaction.isClosed()) {
                    MessageBroker msgBroker = MessageBroker.getMessageBroker(null);
                    String clientID = UUIDUtils.createUUID();
                    AsyncMessage msg = new AsyncMessage();
                    msg.setDestination("orderManagement");
                    msg.setHeader("DSSubtopic", transaction.getCallerReference());
                    msg.setClientId(clientID);
                    msg.setMessageId(UUIDUtils.createUUID());
                    msg.setTimestamp(System.currentTimeMillis());
                    Map<String, String> properties = new HashMap<String, String>();
                    properties.put("status", transaction.getStatus());
                    properties.put("failureMessage", transaction.getFailureMessage());
                    msg.setBody(properties);
                }
            } */
        }
    }

    public List<Transaction> getOpenTransactions() {
        return null;
    }
}
