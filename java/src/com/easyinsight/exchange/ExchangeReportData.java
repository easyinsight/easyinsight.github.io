package com.easyinsight.exchange;

/**
 * User: jamesboe
 * Date: Dec 10, 2009
 * Time: 10:41:24 AM
 */
public class ExchangeReportData extends ExchangeData {
    private long reportType;
    private long dataSourceID;
    private String dataSourceName;
    private boolean dataSourceAccessible;

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

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public boolean isDataSourceAccessible() {
        return dataSourceAccessible;
    }

    public void setDataSourceAccessible(boolean dataSourceAccessible) {
        this.dataSourceAccessible = dataSourceAccessible;
    }
}
