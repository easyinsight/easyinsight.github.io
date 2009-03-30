package com.easyinsight.analysis;

import com.easyinsight.core.InsightDescriptor;

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
    private InsightDescriptor insightDescriptor;

    public InsightResponse() {
    }

    public InsightResponse(int status, InsightDescriptor insightDescriptor) {
        this.status = status;
        this.insightDescriptor = insightDescriptor;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public InsightDescriptor getInsightDescriptor() {
        return insightDescriptor;
    }

    public void setInsightDescriptor(InsightDescriptor insightDescriptor) {
        this.insightDescriptor = insightDescriptor;
    }
}
