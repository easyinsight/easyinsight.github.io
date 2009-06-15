package com.easyinsight.eventing;

import com.easyinsight.scheduler.RefreshEventInfo;
import com.easyinsight.scheduler.ScheduledTask;
import com.easyinsight.datafeeds.FeedDefinition;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 12, 2009
 * Time: 1:43:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class AsyncRunningListener extends EIEventListener {
    public void execute(EIEvent e) {
        AsyncRunningEvent event = (AsyncRunningEvent) e;
        RefreshEventInfo info = new RefreshEventInfo();
        ScheduledTask task = event.getTask();
        info.setAction(RefreshEventInfo.ADD);
        info.setMessage(null);
        info.setFeedId(event.getFeedID());
        info.setFeedName(event.getFeedName());
        info.setTaskId(task.getScheduledTaskID());
        info.setUserId(event.getUserID());
        MessageUtils.sendMessage("generalNotifications", info);
    }
}
