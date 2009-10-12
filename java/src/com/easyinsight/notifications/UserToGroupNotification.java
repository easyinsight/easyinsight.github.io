package com.easyinsight.notifications;

import com.easyinsight.users.User;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Oct 12, 2009
 * Time: 11:16:22 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="user_to_group_notification")
@PrimaryKeyJoinColumn(name="group_notification_id")
public class UserToGroupNotification extends GroupNotification {

    @Column(name="user_action")
    private int userAction;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User targetUser;
    
    public int getUserAction() {
        return userAction;
    }

    public void setUserAction(int userAction) {
        this.userAction = userAction;
    }

    public User getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
    }
    
}
