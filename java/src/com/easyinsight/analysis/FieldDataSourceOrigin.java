package com.easyinsight.analysis;

import java.util.Set;

/**
 * User: jamesboe
 * Date: 2/3/14
 * Time: 1:11 PM
 */
public class FieldDataSourceOrigin {
    private long report;
    private Set<Long> additionalReports;

    public Set<Long> getAdditionalReports() {
        return additionalReports;
    }

    public void setAdditionalReports(Set<Long> additionalReports) {
        this.additionalReports = additionalReports;
    }

    public long getReport() {
        return report;
    }

    public void setReport(long report) {
        this.report = report;
    }
}
