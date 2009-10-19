package com.easyinsight.notifications;

import com.easyinsight.users.User;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Oct 12, 2009
 * Time: 11:04:10 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="user_notification")
@PrimaryKeyJoinColumn(name="notification_id")
public abstract class UserNotification extends NotificationBase {

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

}
