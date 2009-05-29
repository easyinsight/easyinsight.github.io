package com.easyinsight.eventing;

import flex.messaging.client.FlexClientOutboundQueueProcessor;
import flex.messaging.messages.Message;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: May 28, 2009
 * Time: 10:54:40 AM
 */
public class ClientAuthenticationProcessor extends FlexClientOutboundQueueProcessor{
    @Override
    public void add(List list, Message message) {
    }
}
