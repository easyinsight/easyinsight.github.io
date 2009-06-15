package com.easyinsight.eventing;

import com.easyinsight.scheduler.RefreshEventInfo;
import com.easyinsight.scheduler.ScheduledTask;
import com.easyinsight.datafeeds.FeedDefinition;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 12, 2009
 * Time: 2:29:00 PM
 */
public class AsyncCompletedListener extends EIEventListener {
    public void execute(EIEvent e) {
        AsyncCompletedEvent event = (AsyncCompletedEvent) e;
        RefreshEventInfo info = new RefreshEventInfo();
        FeedDefinition definition = event.getFeedDefinition();
        ScheduledTask t = event.getTask();
        info.setFeedName(definition.getFeedName());
        info.setFeedId(definition.getDataFeedID());
        info.setMessage("Completed!");
        info.setAction(RefreshEventInfo.COMPLETE);
        info.setTaskId(t.getScheduledTaskID());
        info.setUserId(event.getUserID());
        MessageUtils.sendMessage("generalNotifications", info);
    }
}
