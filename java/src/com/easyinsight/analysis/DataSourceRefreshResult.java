package com.easyinsight.analysis;

import java.util.Date;

/**
 * User: jamesboe
 * Date: 11/12/12
 * Time: 3:24 PM
 */
public class DataSourceRefreshResult {
    private Date date;
    private ReportFault warning;
    private boolean newFields;

    public boolean isNewFields() {
        return newFields;
    }

    public void setNewFields(boolean newFields) {
        this.newFields = newFields;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ReportFault getWarning() {
        return warning;
    }

    public void setWarning(ReportFault warning) {
        this.warning = warning;
    }
}
