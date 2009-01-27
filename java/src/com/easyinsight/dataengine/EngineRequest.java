package com.easyinsight.dataengine;

import com.easyinsight.IDataService;
import com.easyinsight.analysis.InsightRequestMetadata;

import java.io.Serializable;

/**
 * User: James Boe
 * Date: Aug 5, 2008
 * Time: 6:34:28 PM
 */
public abstract class EngineRequest implements Serializable {
    private Long invocationID;
    private static int currentInvocationID;
    private InsightRequestMetadata insightRequestMetadata;

    public abstract long getFeedID();

    public Long getInvocationID() {
        return invocationID;
    }

    public void setInvocationID(Long invocationID) {
        this.invocationID = invocationID;
    }

    public InsightRequestMetadata getInsightRequestMetadata() {
        return insightRequestMetadata;
    }

    public void setInsightRequestMetadata(InsightRequestMetadata insightRequestMetadata) {
        this.insightRequestMetadata = insightRequestMetadata;
    }

    public long generateInvocationID() {
        return Long.parseLong((currentInvocationID++) + "" + System.currentTimeMillis());
    }

    public abstract EngineResponse execute(IDataService dataService);
}
