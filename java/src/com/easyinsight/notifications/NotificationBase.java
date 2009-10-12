package com.easyinsight.notifications;

import com.easyinsight.users.User;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Oct 12, 2009
 * Time: 10:51:38 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "notification_base")
public abstract class NotificationBase {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="notification_id")
    private long notificationID;

    @Column(name="notification_date")
    private Date notificationDate;

    @Column(name="notification_type")
    private int notificationType;

    @ManyToOne
    @JoinColumn(name="acting_user_id")
    private User actingUser;

    public long getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(long notificationID) {
        this.notificationID = notificationID;
    }

    public Date getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(Date notificationDate) {
        this.notificationDate = notificationDate;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }

    public User getActingUser() {
        return actingUser;
    }

    public void setActingUser(User actingUser) {
        this.actingUser = actingUser;
    }
}
