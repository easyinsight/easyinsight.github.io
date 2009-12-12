package com.easyinsight.reportpackage;

/**
 * User: jamesboe
 * Date: Dec 9, 2009
 * Time: 5:28:13 PM
 */
public class ReportPackageResponse {

    public static final int SUCCESS = 1;
    public static final int NEED_LOGIN = 2;
    public static final int REJECTED = 3;

    private int status;
    private ReportPackageDescriptor reportPackageDescriptor;

    public ReportPackageResponse() {
    }

    public ReportPackageResponse(int status, ReportPackageDescriptor reportPackageDescriptor) {
        this.status = status;
        this.reportPackageDescriptor = reportPackageDescriptor;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ReportPackageDescriptor getReportPackageDescriptor() {
        return reportPackageDescriptor;
    }

    public void setReportPackageDescriptor(ReportPackageDescriptor reportPackageDescriptor) {
        this.reportPackageDescriptor = reportPackageDescriptor;
    }
}
