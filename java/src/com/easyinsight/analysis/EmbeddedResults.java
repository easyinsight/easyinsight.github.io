package com.easyinsight.analysis;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Dec 1, 2009
 * Time: 9:36:58 AM
 */
public class EmbeddedResults implements Serializable, Cloneable {
    protected WSAnalysisDefinition definition;
    private boolean dataSourceAccessible;
    protected DataSourceInfo dataSourceInfo;
    protected double ratingsAverage;
    protected int ratingsCount;
    protected int myRating;
    protected String attribution;
    private ReportFault reportFault;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public ReportFault getReportFault() {
        return reportFault;
    }

    public void setReportFault(ReportFault reportFault) {
        this.reportFault = reportFault;
    }

    public int getMyRating() {
        return myRating;
    }

    public void setMyRating(int myRating) {
        this.myRating = myRating;
    }

    @Override
    public EmbeddedResults clone() throws CloneNotSupportedException {
        return (EmbeddedResults) super.clone();
    }

    public double getRatingsAverage() {
        return ratingsAverage;
    }

    public void setRatingsAverage(double ratingsAverage) {
        this.ratingsAverage = ratingsAverage;
    }

    public int getRatingsCount() {
        return ratingsCount;
    }

    public void setRatingsCount(int ratingsCount) {
        this.ratingsCount = ratingsCount;
    }

    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    public DataSourceInfo getDataSourceInfo() {
        return dataSourceInfo;
    }

    public void setDataSourceInfo(DataSourceInfo dataSourceInfo) {
        this.dataSourceInfo = dataSourceInfo;
    }

    public boolean isDataSourceAccessible() {
        return dataSourceAccessible;
    }

    public void setDataSourceAccessible(boolean dataSourceAccessible) {
        this.dataSourceAccessible = dataSourceAccessible;
    }

    public WSAnalysisDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(WSAnalysisDefinition definition) {
        this.definition = definition;
    }
}
