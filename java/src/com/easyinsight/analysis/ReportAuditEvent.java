package com.easyinsight.analysis;

import java.io.Serializable;

/**
 * User: jamesboe
 * Date: 10/8/13
 * Time: 12:28 PM
 */
public class ReportAuditEvent implements Serializable {

    public static final String JOIN_FILTER = "Join Filter";
    public static final String JOIN = "Join";
    public static final String QUERY = "Query";
    public static final String WARNING = "Warning";
    public static final String FIELD = "Field";
    public static final String FILTER = "Filter";

    private String auditType;
    private String eventLabel;


    public ReportAuditEvent() {
    }

    public ReportAuditEvent(String auditType, String eventLabel) {
        this.auditType = auditType;
        this.eventLabel = eventLabel;
    }

    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    public String getEventLabel() {
        return eventLabel;
    }

    public void setEventLabel(String eventLabel) {
        this.eventLabel = eventLabel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReportAuditEvent that = (ReportAuditEvent) o;

        if (auditType != null ? !auditType.equals(that.auditType) : that.auditType != null) return false;
        if (eventLabel != null ? !eventLabel.equals(that.eventLabel) : that.eventLabel != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = auditType != null ? auditType.hashCode() : 0;
        result = 31 * result + (eventLabel != null ? eventLabel.hashCode() : 0);
        return result;
    }
}
