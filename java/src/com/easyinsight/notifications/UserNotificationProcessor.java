package com.easyinsight.notifications;

import flex.messaging.client.FlexClientOutboundQueueProcessor;
import flex.messaging.client.FlushResult;
import flex.messaging.client.FlexClient;
import flex.messaging.messages.Message;
import flex.messaging.config.ConfigMap;
import flex.messaging.MessageClient;
import flex.messaging.FlexContext;

import java.util.List;

import com.easyinsight.scheduler.RefreshEventInfo;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.UserPrincipal;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: May 29, 2009
 * Time: 10:25:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class UserNotificationProcessor extends FlexClientOutboundQueueProcessor {

    @Override
    public FlushResult flush(List list) {
        for(Object o : list) {
            Message m = (Message) o;
            RefreshEventInfo info = (RefreshEventInfo)m.getBody();
            if(((UserPrincipal) FlexContext.getFlexSession().getUserPrincipal()).getUserID() != info.getUserId()) {
                list.remove(o);
            }
        }
        return super.flush(list);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
