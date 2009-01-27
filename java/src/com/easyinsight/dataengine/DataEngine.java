package com.easyinsight.dataengine;

import com.easyinsight.IDataService;
import com.easyinsight.DataService;
import com.easyinsight.logging.LogClass;

import javax.jms.MessageListener;
import javax.jms.Message;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;

/**
 * User: James Boe
 * Date: Aug 5, 2008
 * Time: 10:47:10 PM
 */
public class DataEngine {
    private IMessaging messaging = new ActiveMQMessaging();
    private String myDestination = "me";
    private InstanceKey identity = new InstanceKey(myDestination);
    private IDataService dataService = new DataService();
    private static DataEngine instance;

    public static DataEngine instance() {
        return instance;
    }

    public DataEngine() {
        messaging.start();
        subscribeToPing();
        subscribeToBidRequest();
        subscribeToWork();
        instance = this;
    }

    public void stop() {
        messaging.stop();
    }

    private void subscribeToPing() {
        try {
            messaging.subscribeToTopic("topic/online_request", new MessageListener() {

                public void onMessage(Message message) {
                    try {
                        messaging.sendPointToPoint("queue/online_reply", identity, null);
                    } catch (Exception e) {
                        LogClass.error(e);
                    }
                }
            });
        } catch (JMSException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private void subscribeToBidRequest() {
        try {
            messaging.subscribeToTopic("topic/bid_request", new MessageListener() {

                public void onMessage(Message message) {
                    try {
                        ObjectMessage objectMessage = (ObjectMessage) message;
                        BidRequest bidRequest = (BidRequest) objectMessage.getObject();
                        int bidValue;
                        if (cached(bidRequest.getEngineRequest().getFeedID())) {
                            bidValue = 0;
                        } else {
                            bidValue = getCacheSize();
                        }
                        Bid bid = new Bid(bidValue, myDestination, bidRequest.getEngineRequest());
                        messaging.sendPointToPoint("queue/bid_reply", bid, null);
                    } catch (Exception e) {
                        LogClass.error(e);
                    }
                }
            });
        } catch (JMSException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private void subscribeToWork() {
        try {
            messaging.subscribeToQueue("queue/work", new MessageListener() {

                public void onMessage(Message message) {
                    try {
                        ObjectMessage objectMessage = (ObjectMessage) message;
                        EngineRequest engineRequest = (EngineRequest) objectMessage.getObject();
                        EngineResponse engineResponse = engineRequest.execute(dataService);
                        messaging.sendPointToPoint("queue/work_reply", engineResponse, null);
                    } catch (Exception e) {
                        LogClass.error(e);
                    }
                }
            }, null);
        } catch (JMSException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private int getCacheSize() {
        return 0;
    }

    private boolean cached(long feedID) {
        return false;
    }
}
