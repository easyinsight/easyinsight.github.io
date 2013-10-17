package com.easyinsight.servlet;

import flex.messaging.MessageBrokerServlet;

/**
 * User: jamesboe
 * Date: 10/14/13
 * Time: 1:25 PM
 */
public class EIFlex extends MessageBrokerServlet {
    @Override
    public void destroy() {
        System.out.println("Destroying Flex message broker servlet.");
        super.destroy();
        System.out.println("Destroyed Flex message broker servlet.");
    }
}
