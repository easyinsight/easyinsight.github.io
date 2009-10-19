package com.easyinsight.notifications;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Column;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Oct 12, 2009
 * Time: 1:37:21 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="user_to_report_notification")
@PrimaryKeyJoinColumn(name="notification_id")
public class UserToReportNotification extends UserNotification {
    @Column(name="analysis_id")
    private long analysisID;
    @Column(name="analysis_action")
    private int analysisAction;
    @Column(name="analysis_role")
    private int analysisRole;


    public long getAnalysisID() {
        return analysisID;
    }

    public void setAnalysisID(long analysisID) {
        this.analysisID = analysisID;
    }

    public int getAnalysisAction() {
        return analysisAction;
    }

    public void setAnalysisAction(int analysisAction) {
        this.analysisAction = analysisAction;
    }

    public int getAnalysisRole() {
        return analysisRole;
    }

    public void setAnalysisRole(int analysisRole) {
        this.analysisRole = analysisRole;
    }
}
