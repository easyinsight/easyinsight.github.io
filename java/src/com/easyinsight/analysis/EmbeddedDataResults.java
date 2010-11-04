package com.easyinsight.analysis;

public class EmbeddedDataResults extends EmbeddedResults {
    private AnalysisItem[] headers;
    private ListRow[] rows;

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