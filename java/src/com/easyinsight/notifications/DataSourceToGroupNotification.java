package com.easyinsight.notifications;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Column;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Oct 12, 2009
 * Time: 12:32:16 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "data_source_to_group_notification")
@PrimaryKeyJoinColumn(name="notification_id")
public class DataSourceToGroupNotification extends GroupNotification {
    @Column(name="feed_id")
    private long feedID;
    @Column(name="feed_role")
    private int feedRole;
    @Column(name="feed_action")
    private int feedAction;

    public long getFeedID() {
        return feedID;
    }

    public void setFeedID(long feedID) {
        this.feedID = feedID;
    }

    public int getFeedRole() {
        return feedRole;
    }

    public void setFeedRole(int feedRole) {
        this.feedRole = feedRole;
    }

    public int getFeedAction() {
        return feedAction;
    }

    public void setFeedAction(int feedAction) {
        this.feedAction = feedAction;
    }

}
