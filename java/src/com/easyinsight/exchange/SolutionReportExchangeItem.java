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

    public SolutionReportExchangeItem(String name, long id, long reportType, long dataSourceID,
                                      double ratingAverage, double ratingCount, Date dateAdded, String description,
                                      String author, String dataSourceName, boolean dataSourceAccessible, long solutionID, String solutionName) {
        super(name, id, reportType, dataSourceID, null, ratingAverage, ratingCount, dateAdded, description, author, dataSourceName, dataSourceAccessible);
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
