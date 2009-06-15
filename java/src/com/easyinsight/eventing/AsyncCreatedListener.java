package com.easyinsight.eventing;

import com.easyinsight.scheduler.RefreshEventInfo;
import com.easyinsight.scheduler.ScheduledTask;
import com.easyinsight.datafeeds.FeedDefinition;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 12, 2009
 * Time: 11:08:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class AsyncCreatedListener extends EIEventListener {
    public void execute(EIEvent e) {
        AsyncCreatedEvent event = (AsyncCreatedEvent) e;
        RefreshEventInfo info = new RefreshEventInfo();
        ScheduledTask t = event.getTask();
        info.setFeedName(event.getFeedName());
        info.setFeedId(event.getFeedID());
        info.setMessage("Waiting to Process");
        info.setAction(RefreshEventInfo.CREATE);
        info.setTaskId(t.getScheduledTaskID());
        info.setUserId(event.getUserID());
        MessageUtils.sendMessage("generalNotifications", info);
    }
}
