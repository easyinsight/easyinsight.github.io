package com.easyinsight.analysis;

/**
 * User: jamesboe
 * Date: Nov 30, 2009
 * Time: 9:29:09 AM
 */
public class ListData {
    private AnalysisItem[] headers;
    private AnalysisItemResultMetadata[] headerMetadata;
    private ListRow[] rows;
    private boolean limitedResults;
    private int limitResults;
    private int maxResults;

    public AnalysisItem[] getHeaders() {
        return headers;
    }

    public void setHeaders(AnalysisItem[] headers) {
        this.headers = headers;
    }

    public AnalysisItemResultMetadata[] getHeaderMetadata() {
        return headerMetadata;
    }

    public void setHeaderMetadata(AnalysisItemResultMetadata[] headerMetadata) {
        this.headerMetadata = headerMetadata;
    }

    public ListRow[] getRows() {
        return rows;
    }

    public void setRows(ListRow[] rows) {
        this.rows = rows;
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
}
