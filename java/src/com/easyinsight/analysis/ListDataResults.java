package com.easyinsight.analysis;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.FeedMetadata;
import com.easyinsight.analysis.AnalysisItemResultMetadata;
import com.easyinsight.datafeeds.CredentialFailure;
import com.easyinsight.datafeeds.CredentialRequirement;

import java.util.List;
import java.util.Set;
import java.io.Serializable;

public class ListDataResults implements Serializable {
    private AnalysisItem[] headers;
    private AnalysisItemResultMetadata[] headerMetadata;
    private ListRow[] rows;
    private Set<CredentialRequirement> credentialRequirements;
    private boolean limitedResults;
    private int limitResults;
    private int maxResults;
    private Set<Long> invalidAnalysisItemIDs;
    private FeedMetadata feedMetadata;
    private DataSourceInfo dataSourceInfo;

    public Set<CredentialRequirement> getCredentialRequirements() {
        return credentialRequirements;
    }

    public void setCredentialRequirements(Set<CredentialRequirement> credentialRequirements) {
        this.credentialRequirements = credentialRequirements;
    }

    public DataSourceInfo getDataSourceInfo() {
        return dataSourceInfo;
    }

    public void setDataSourceInfo(DataSourceInfo dataSourceInfo) {
        this.dataSourceInfo = dataSourceInfo;
    }

    public FeedMetadata getFeedMetadata() {
        return feedMetadata;
    }

    public void setFeedMetadata(FeedMetadata feedMetadata) {
        this.feedMetadata = feedMetadata;
    }

    public Set<Long> getInvalidAnalysisItemIDs() {
        return invalidAnalysisItemIDs;
    }

    public void setInvalidAnalysisItemIDs(Set<Long> invalidAnalysisItemIDs) {
        this.invalidAnalysisItemIDs = invalidAnalysisItemIDs;
    }

    public boolean isLimitedResults() {
        return limitedResults;
    }

    public void setLimitedResults(boolean limitedResults) {
        this.limitedResults = limitedResults;
    }

    public int getLimitResults() {
        return limitResults;
    }

    public void setLimitResults(int limitResults) {
        this.limitResults = limitResults;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public AnalysisItemResultMetadata[] getHeaderMetadata() {
        return headerMetadata;
    }

    public void setHeaderMetadata(AnalysisItemResultMetadata[] headerMetadata) {
        this.headerMetadata = headerMetadata;
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

    public void toBlah() {
        String[][] grid = new String[headers.length][rows.length + 1];
        for (int x = 0; x < headers.length; x++) {
            grid[x][0] = headers[x].getKey().toDisplayName();
            for (int y = 0; y < rows.length; y++) {
                ListRow listRow = rows[y];
                grid[x][y + 1] = listRow.getValues()[x].toString(); 
            }
        }
    }
}
