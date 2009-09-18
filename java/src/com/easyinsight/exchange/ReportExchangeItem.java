package com.easyinsight.exchange;

import java.util.Date;

/**
 * User: jamesboe
 * Date: Sep 14, 2009
 * Time: 7:18:12 PM
 */
public class ReportExchangeItem extends ExchangeItem {
    private long reportType;
    private long dataSourceID;
    private String dataSourceName;
    private boolean dataSourceAccessible;

    public ReportExchangeItem() {
    }

    public ReportExchangeItem(String name, long id, long reportType, long dataSourceID, String attribution, double ratingAverage,
                              double ratingCount, Date dateAdded, String description, String author, String dataSourceName,
                              boolean dataSourceAccessible) {
        super(name, id, attribution, ratingAverage, ratingCount, dateAdded, description, author);
        this.reportType = reportType;
        this.dataSourceID = dataSourceID;
        this.dataSourceName = dataSourceName;
        this.dataSourceAccessible = dataSourceAccessible;
    }

    public boolean isDataSourceAccessible() {
        return dataSourceAccessible;
    }

    public void setDataSourceAccessible(boolean dataSourceAccessible) {
        this.dataSourceAccessible = dataSourceAccessible;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public long getReportType() {
        return reportType;
    }

    public void setReportType(long reportType) {
        this.reportType = reportType;
    }

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }
}
