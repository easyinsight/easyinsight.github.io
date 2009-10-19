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
@Inheritance(strategy= InheritanceType.JOINED)
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

    public static final int ADD = 1;
    public static final int REMOVE = 2;
    
    public static final int VIEWER = 1;
    public static final int OWNER = 2;
    
    public static final int USER_TO_DATA_SOURCE = 1;
    public static final int DATA_SOURCE_TO_GROUP = 2;
    public static final int REPORT_TO_GROUP = 3;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NotificationBase that = (NotificationBase) o;

        if (notificationID != that.notificationID) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (notificationID ^ (notificationID >>> 32));
        result = 31 * result + (notificationDate != null ? notificationDate.hashCode() : 0);
        result = 31 * result +  notificationType;
        result = 31 * result + (actingUser != null ? actingUser.hashCode() : 0);
        return result;
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
