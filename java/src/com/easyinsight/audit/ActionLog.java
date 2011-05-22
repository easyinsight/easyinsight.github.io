package com.easyinsight.audit;

import javax.persistence.*;
import java.util.Date;

/**
 * User: jamesboe
 * Date: 5/13/11
 * Time: 7:50 PM
 */
@Entity
@Table(name="action_log")
@Inheritance(strategy=InheritanceType.JOINED)
public class ActionLog {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column (name="action_log_id")
    private long actionLogID;
    @Column (name="user_id")
    private long userID;
    @Column (name="action_type")
    private int actionType;

    @Column (name="action_date")
    private Date actionDate;

    public ActionLog() {
    }

    public ActionLog(long userID, int actionType) {
        this.userID = userID;
        this.actionType = actionType;
        actionDate = new Date();
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public long getActionLogID() {
        return actionLogID;
    }

    public void setActionLogID(long actionLogID) {
        this.actionLogID = actionLogID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionLog actionLog = (ActionLog) o;

        if (actionType != actionLog.actionType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return actionType;
    }
}
