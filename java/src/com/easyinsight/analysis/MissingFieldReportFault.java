package com.easyinsight.analysis;

import java.util.Set;

/**
 * User: jamesboe
 * Date: 9/24/12
 * Time: 10:25 AM
 */
public class MissingFieldReportFault extends ReportFault {
    private Set<String> fields;

    public MissingFieldReportFault() {
    }

    public MissingFieldReportFault(Set<String> fields) {
        this.fields = fields;
    }

    public Set<String> getFields() {
        return fields;
    }

    public void setFields(Set<String> fields) {
        this.fields = fields;
    }
}
