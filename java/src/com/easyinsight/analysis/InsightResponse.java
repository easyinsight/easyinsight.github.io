package com.easyinsight.analysis;

/**
 * User: James Boe
 * Date: Sep 13, 2008
 * Time: 12:55:35 PM
 */
public class InsightResponse {
    private boolean successful;
    private WSAnalysisDefinition definition;

    public InsightResponse() {
    }

    public InsightResponse(boolean successful, WSAnalysisDefinition definition) {
        this.successful = successful;
        this.definition = definition;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public WSAnalysisDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(WSAnalysisDefinition definition) {
        this.definition = definition;
    }
}
