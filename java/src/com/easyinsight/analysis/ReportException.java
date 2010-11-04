package com.easyinsight.analysis;

/**
 * User: jamesboe
 * Date: Aug 30, 2009
 * Time: 12:24:16 PM
 */
public class ReportException extends RuntimeException {

    private ReportFault reportFault;

    public ReportException(ReportFault reportFault) {
        this.reportFault = reportFault;
    }

    public ReportFault getReportFault() {
        return reportFault;
    }
}
