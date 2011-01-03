package com.easyinsight.userupload;

import com.easyinsight.analysis.ReportFault;
import com.easyinsight.users.Credentials;

/**
 * User: James Boe
 * Date: Jul 14, 2008
 * Time: 4:22:07 PM
 */
public class CredentialsResponse {
    private boolean successful;
    private String failureMessage;
    private Credentials encryptedResponse;
    private long dataSourceID;
    private ReportFault reportFault;
    private String callDataID;

    public CredentialsResponse() {
    }

    public CredentialsResponse(boolean successful, long dataSourceID) {
        this.successful = successful;
        this.dataSourceID = dataSourceID;
    }

    public CredentialsResponse(boolean successful, ReportFault reportFault) {
        this.successful = successful;
        this.reportFault = reportFault;
    }

    public CredentialsResponse(boolean successful, String failureMessage, long dataSourceID) {
        this.successful = successful;
        this.failureMessage = failureMessage;
        this.dataSourceID = dataSourceID;
    }

    public String getCallDataID() {
        return callDataID;
    }

    public void setCallDataID(String callDataID) {
        this.callDataID = callDataID;
    }

    public ReportFault getReportFault() {
        return reportFault;
    }

    public void setReportFault(ReportFault reportFault) {
        this.reportFault = reportFault;
    }

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    public Credentials getEncryptedResponse() {
        return encryptedResponse;
    }

    public void setEncryptedResponse(Credentials encryptedResponse) {
        this.encryptedResponse = encryptedResponse;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }
}
