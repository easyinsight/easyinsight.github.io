package com.easyinsight.dataengine;

import com.easyinsight.logging.LogClass;

import javax.jms.MessageListener;
import javax.jms.Message;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.util.*;

/**
 * User: James Boe
 * Date: Aug 5, 2008
 * Time: 6:22:10 PM
 */
public class EngineRequestHandler {

    private Map<Long, Object> responseMap = new HashMap<Long, Object>();
    private Map<Long, Object> notifierMap = new HashMap<Long, Object>();
    private Map<Long, List<Bid>> bidMap = new HashMap<Long, List<Bid>>();
    private IMessaging messaging = new ActiveMQMessaging();
    private Set<InstanceKey> instanceKeySet = new HashSet<InstanceKey>();
    private Timer timer = new Timer();

    private static EngineRequestHandler instance;

    public static EngineRequestHandler instance() {
        return instance;
    }

    public EngineRequestHandler() {
        try {
            messaging.start();
            messaging.subscribeToQueue("queue/bid_reply", new MessageListener() {

                public void onMessage(Message message) {
                    try {
                        ObjectMessage objectMessage = (ObjectMessage) message;
                        Bid bid = (Bid) objectMessage.getObject();
                        List<Bid> bidList = bidMap.get(bid.getEngineRequest().getInvocationID());
                        if (bidList == null) {
                            bidList = new ArrayList<Bid>();
                            bidMap.put(bid.getEngineRequest().getInvocationID(), bidList);
                        }
                        bidList.add(bid);
                        if (bidList.size() == instanceKeySet.size()) {
                            onBid(bid.getEngineRequest(), bidList);
                        }
                    } catch (Exception e) {
                        LogClass.error(e);
                        throw new RuntimeException(e);
                    }
                }
            }, null);
            timer.schedule(new TimerTask() {

                public void run() {
                    try {
                        pingInstances();
                    } catch (Exception e) {
                        LogClass.error(e);
                    }
                }
            }, new Date(), 10000);
            syncInstanceCount();
            subscribeToWork();
            instance = this;
        } catch (JMSException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        timer.cancel();
        messaging.stop();
    }

    private void pingInstances() {
        messaging.sendMessage("topic/online_request", null, null);
    }

    private void syncInstanceCount() {
        try {
            messaging.subscribeToQueue("queue/online_reply", new MessageListener() {

                public void onMessage(Message message) {
                    try {
                        ObjectMessage objectMessage = (ObjectMessage) message;
                        InstanceKey instanceKey = (InstanceKey) objectMessage.getObject();
                        instanceKeySet.add(instanceKey);
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

    // retrieval process...

    // 1. send message on topic asking for bids on handling data feed
    // if already handling data feed, respond with a bid of 100
    // otherwise, respond with a bid based on current load
    // sort the bids from high to low
    // choose the highest
    // pass it the relevant request
    // send the request via point to point delivery
    // when response comes back, we can return the rpc call

    public Object addRequest(EngineRequest engineRequest) {
        // push the engine request onto the message bus
        try {
            Object semaphore = new Object();
            notifierMap.put(engineRequest.getInvocationID(), semaphore);
            LogClass.debug("** added request with invocation ID = " + engineRequest.getInvocationID());
            messaging.sendMessage("topic/bid_request", new BidRequest(engineRequest), null);
            try {
                synchronized(semaphore) {
                    semaphore.wait(20000);
                }
            } catch (InterruptedException e) {
                // ignore this...
            }
            notifierMap.remove(engineRequest.getInvocationID());
            return responseMap.remove(engineRequest.getInvocationID());
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private void onBid(EngineRequest engineRequest, List<Bid> bids) {
        // need a barrier here that we can add bids into, needs to be pretty fast...
        Bid acceptedBid = null;
        for (Bid bid : bids) {
            if (acceptedBid == null) {
                acceptedBid = bid;
            } else {
                if (bid.getBidValue() < acceptedBid.getBidValue()) {
                    acceptedBid = bid;
                }
            }
        }
        if (acceptedBid == null) {
            // this is a problem, return an error message at this point?
        } else {
            // send engineRequest point to point to destination acceptedBid
            Map<String, String> properties = new HashMap<String, String>();
            properties.put("destination", acceptedBid.getDestination());
            messaging.sendPointToPoint("queue/work", engineRequest, properties);
        }
    }

    private void subscribeToWork() {
        try {
            messaging.subscribeToQueue("queue/work_reply", new MessageListener() {

                public void onMessage(Message message) {
                    try {
                        ObjectMessage objectMessage = (ObjectMessage) message;
                        onResponse((EngineResponse) objectMessage.getObject());
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

    private void onResponse(EngineResponse engineResponse) {
        Object semaphore = notifierMap.get(engineResponse.getInvocationID());
        if (semaphore != null) {
            responseMap.put(engineResponse.getInvocationID(), engineResponse.getOutput());
            synchronized (semaphore) {
                semaphore.notify();
            }
        }
    }
}
