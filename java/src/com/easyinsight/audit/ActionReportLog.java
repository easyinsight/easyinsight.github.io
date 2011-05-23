package com.easyinsight.audit;

import com.easyinsight.core.InsightDescriptor;

import javax.persistence.*;
import java.util.Date;

/**
 * User: jamesboe
 * Date: 5/13/11
 * Time: 7:50 PM
 */
@Entity
@Table(name="action_report_log")
@PrimaryKeyJoinColumn(name="action_log_id")
public class ActionReportLog extends ActionLog {

    public static final int EDIT = 1;
    public static final int VIEW = 2;
    public static final int EXPORT_XLS = 3;
    public static final int EXPORT_PDF = 4;
    public static final int EXPORT_PNG = 5;

    @Column(name="report_id")
    private long reportID;

    @Transient
    private InsightDescriptor insightDescriptor;

    public ActionReportLog() {
    }

    public ActionReportLog(long userID, int actionType, long reportID) {
        super(userID, actionType);
        this.reportID = reportID;
    }

    public ActionReportLog(InsightDescriptor insightDescriptor, int actionType, Date date) {
        this.insightDescriptor = insightDescriptor;
        reportID = insightDescriptor.getId();
        setActionType(actionType);
        setActionDate(date);
    }

    public InsightDescriptor getInsightDescriptor() {
        return insightDescriptor;
    }

    public void setInsightDescriptor(InsightDescriptor insightDescriptor) {
        this.insightDescriptor = insightDescriptor;
    }

    public long getReportID() {
        return reportID;
    }

    public void setReportID(long reportID) {
        this.reportID = reportID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ActionReportLog that = (ActionReportLog) o;

        if (reportID != that.reportID) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (reportID ^ (reportID >>> 32));
        return result;
    }
}

