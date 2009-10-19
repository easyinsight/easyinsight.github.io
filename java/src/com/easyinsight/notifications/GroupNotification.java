package com.easyinsight.notifications;

import com.easyinsight.groups.Group;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Oct 12, 2009
 * Time: 11:11:32 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="group_notification")
@PrimaryKeyJoinColumn(name="notification_id")
public abstract class GroupNotification extends NotificationBase {
    
    @Column(name="group_id")
    long groupID;

    public long getGroupID() {
        return groupID;
    }

    public void setGroupID(long groupID) {
        this.groupID = groupID;
    }
}
