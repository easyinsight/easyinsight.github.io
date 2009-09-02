package com.easyinsight.analysis;

import com.easyinsight.datafeeds.CredentialRequirement;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class EmbeddedDataResults implements Serializable {
    private AnalysisItem[] headers;
    private WSAnalysisDefinition definition;
    private ListRow[] rows;
    private boolean dataSourceAccessible;
    private DataSourceInfo dataSourceInfo;
    private double ratingsAverage;
    private int ratingsCount;
    private String attribution;
    private Set<CredentialRequirement> credentialRequirements;

    public EmbeddedDataResults() {
    }

    public EmbeddedDataResults(EmbeddedDataResults results) {
        this.headers = results.headers;
        this.definition = results.definition;
        this.rows = results.rows;
        this.dataSourceInfo = results.dataSourceInfo;
        this.ratingsAverage = results.ratingsAverage;
        this.ratingsCount = results.ratingsCount;
        this.attribution = results.attribution;
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

    public Set<CredentialRequirement> getCredentialRequirements() {
        return credentialRequirements;
    }

    public void setCredentialRequirements(Set<CredentialRequirement> credentialRequirements) {
        this.credentialRequirements = credentialRequirements;
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

    public AnalysisItem[] getHeaders() {
        return headers;
    }

    public void setHeaders(AnalysisItem[] headers) {
        this.headers = headers;
    }

    public ListRow[] getRows() {
        return rows;
    }

    public void setRows(ListRow[] rows) {
        this.rows = rows;
    }
}