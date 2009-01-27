package com.easyinsight.dataengine;

import org.apache.activemq.broker.BrokerService;
import com.easyinsight.logging.LogClass;

/**
 * User: James Boe
 * Date: Aug 6, 2008
 * Time: 11:31:06 AM
 */
public class MessagingBroker {

    public static MessagingBroker instance;

    private BrokerService broker;
    
    public MessagingBroker() {
        try {
            broker = new BrokerService();

                        

            // configure the broker
            broker.addConnector("tcp://localhost:61616");

            broker.start();

            instance = this;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        try {
            broker.stop();
        } catch (Exception e) {
            LogClass.error(e);
        }
    }

    public static MessagingBroker instance() {
        return instance;
    }

    public BrokerService getBroker() {
        return broker;
    }
}
