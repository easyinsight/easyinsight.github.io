package com.easyinsight.analysis;

import java.io.Serializable;

public class EmbeddedDataResults implements Serializable {
    private AnalysisItem[] headers;
    private WSAnalysisDefinition definition;
    private ListRow[] rows;

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