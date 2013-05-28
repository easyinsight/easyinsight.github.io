package com.easyinsight.analysis;

public class EmbeddedDataResults extends EmbeddedResults {
    private AnalysisItem[] headers = new AnalysisItem[0];
    private ListRow[] rows = new ListRow[0];
    private String uid;

    public EmbeddedDataResults() {
    }

    public EmbeddedDataResults(EmbeddedDataResults results) {
        this.headers = results.headers;
        this.definition = results.definition;
        this.rows = results.rows;
        this.dataSourceInfo = results.dataSourceInfo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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