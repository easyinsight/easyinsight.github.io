package com.easyinsight.analysis;

import com.easyinsight.core.Value;

import java.util.Map;

import java.io.Serializable;

public class ListDataResults extends DataResults implements Serializable {
    private AnalysisItem[] headers = new AnalysisItem[0];
    private AnalysisItemResultMetadata[] headerMetadata;
    private ListRow[] rows = new ListRow[0];
    private boolean limitedResults;
    private int limitResults;
    private int maxResults;
    private double[] summaries;

    public void summarize() {
        summaries = new double[headers.length];
        for (int i = 0; i < headers.length; i++) {
            AnalysisItem header = headers[i];
            if (header.hasType(AnalysisItemTypes.MEASURE)) {
                AnalysisMeasure analysisMeasure = (AnalysisMeasure) header;
                AggregationFactory aggregationFactory = new AggregationFactory(analysisMeasure, false);
                Aggregation aggregation = aggregationFactory.getAggregation();
                for (ListRow row : rows) {
                    Value value = row.getValues()[i];
                    aggregation.addValue(value);
                }
                summaries[i] = aggregation.getValue().toDouble();
            }
        }
    }

    public double[] getSummaries() {
        return summaries;
    }

    public void setSummaries(double[] summaries) {
        this.summaries = summaries;
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

    @Override
    public EmbeddedResults toEmbeddedResults() {
        EmbeddedDataResults embeddedDataResults = new EmbeddedDataResults();
        embeddedDataResults.setHeaders(headers);
        if (headers.length == 0) {
            embeddedDataResults.setRows(new ListRow[0]);
        } else {
            embeddedDataResults.setRows(rows);
        }
        
        embeddedDataResults.setAdditionalProperties(getAdditionalProperties());
        return embeddedDataResults;
    }
}
