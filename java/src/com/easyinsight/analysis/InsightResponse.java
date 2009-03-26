package com.easyinsight.analysis;

/**
 * User: James Boe
 * Date: Sep 13, 2008
 * Time: 12:55:35 PM
 */
public class InsightResponse {

    public static final int SUCCESS = 1;
    public static final int NEED_LOGIN = 2;
    public static final int REJECTED = 3;

    private int status;
    private WSAnalysisDefinition definition;

    public InsightResponse() {
    }

    public InsightResponse(int status, WSAnalysisDefinition definition) {
        this.status = status;
        this.definition = definition;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public WSAnalysisDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(WSAnalysisDefinition definition) {
        this.definition = definition;
    }
}
