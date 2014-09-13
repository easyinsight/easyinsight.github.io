package com.easyinsight.datafeeds.infusionsoft;

import java.util.List;

/**
 * User: jamesboe
 * Date: 9/12/14
 * Time: 7:15 PM
 */
public class InfusionsoftReportInfo {
    private List<InfusionsoftReport> infusionsoftReportList;
    private List<InfusionsoftUser> infusionsoftUserList;


    public InfusionsoftReportInfo() {
    }

    public InfusionsoftReportInfo(List<InfusionsoftReport> infusionsoftReportList, List<InfusionsoftUser> infusionsoftUserList) {
        this.infusionsoftReportList = infusionsoftReportList;
        this.infusionsoftUserList = infusionsoftUserList;
    }

    public List<InfusionsoftReport> getInfusionsoftReportList() {
        return infusionsoftReportList;
    }

    public void setInfusionsoftReportList(List<InfusionsoftReport> infusionsoftReportList) {
        this.infusionsoftReportList = infusionsoftReportList;
    }

    public List<InfusionsoftUser> getInfusionsoftUserList() {
        return infusionsoftUserList;
    }

    public void setInfusionsoftUserList(List<InfusionsoftUser> infusionsoftUserList) {
        this.infusionsoftUserList = infusionsoftUserList;
    }
}
