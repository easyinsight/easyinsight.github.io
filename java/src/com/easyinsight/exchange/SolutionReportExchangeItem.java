package com.easyinsight.exchange;

import java.util.Date;

/**
 * User: jamesboe
 * Date: Oct 3, 2009
 * Time: 10:37:39 PM
 */
public class SolutionReportExchangeItem extends ReportExchangeItem {
    private long solutionID;
    private String solutionName;

    public SolutionReportExchangeItem() {
    }

    public SolutionReportExchangeItem(String name, long id, String attribution, double ratingAverage, double ratingCount,
                                      Date dateAdded, String description, String author, ExchangeData exchangeData, long solutionID, String solutionName) {
        super(name, id, attribution, ratingAverage, ratingCount, dateAdded, description, author, exchangeData);
        this.solutionID = solutionID;
        this.solutionName = solutionName;
    }

    public long getSolutionID() {
        return solutionID;
    }

    public void setSolutionID(long solutionID) {
        this.solutionID = solutionID;
    }

    public String getSolutionName() {
        return solutionName;
    }

    public void setSolutionName(String solutionName) {
        this.solutionName = solutionName;
    }
}
