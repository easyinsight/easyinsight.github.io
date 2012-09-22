package com.easyinsight.export;

import com.easyinsight.analysis.ReportFault;

/**
 * User: jamesboe
 * Date: 9/21/12
 * Time: 1:13 PM
 */
public class ExcelResponse {
    private byte[] bytes;
    private ReportFault reportFault;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public ReportFault getReportFault() {
        return reportFault;
    }

    public void setReportFault(ReportFault reportFault) {
        this.reportFault = reportFault;
    }
}
