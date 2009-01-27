package com.easyinsight.dataengine;

import com.easyinsight.AnalysisItem;
import com.easyinsight.IDataService;
import com.easyinsight.analysis.AnalysisItemResultMetadata;

import java.io.Serializable;

/**
 * User: James Boe
 * Date: Aug 5, 2008
 * Time: 5:54:40 PM
 */
public class AnalysisItemMetadataRequest extends EngineRequest implements Serializable {
    private AnalysisItem analysisItem;
    private long feedID;

    public AnalysisItemMetadataRequest() {
    }

    public AnalysisItemMetadataRequest(AnalysisItem analysisItem, long feedID) {
        setInvocationID(generateInvocationID());
        this.analysisItem = analysisItem;
        this.feedID = feedID;
    }

    public long getFeedID() {
        return feedID;
    }

    public void setFeedID(long feedID) {
        this.feedID = feedID;
    }

    public AnalysisItem getAnalysisItem() {
        return analysisItem;
    }

    public void setAnalysisItem(AnalysisItem analysisItem) {
        this.analysisItem = analysisItem;
    }

    public EngineResponse execute(IDataService dataService) {
        AnalysisItemResultMetadata analysisItemMetadata = dataService.getAnalysisItemMetadata(feedID, analysisItem);
        return new EngineResponse(getInvocationID(), analysisItemMetadata);
    }
}
