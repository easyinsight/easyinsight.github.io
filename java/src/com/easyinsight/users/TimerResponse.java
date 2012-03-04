package com.easyinsight.users;

/**
 * User: jamesboe
 * Date: 2/24/12
 * Time: 12:45 PM
 */
public class TimerResponse {
    private boolean rerunReport;
    private BasicInfo basicInfo;

    public TimerResponse() {
    }

    public TimerResponse(boolean rerunReport, BasicInfo basicInfo) {
        this.rerunReport = rerunReport;
        this.basicInfo = basicInfo;
    }

    public boolean isRerunReport() {
        return rerunReport;
    }

    public void setRerunReport(boolean rerunReport) {
        this.rerunReport = rerunReport;
    }

    public BasicInfo getBasicInfo() {
        return basicInfo;
    }

    public void setBasicInfo(BasicInfo basicInfo) {
        this.basicInfo = basicInfo;
    }
}
