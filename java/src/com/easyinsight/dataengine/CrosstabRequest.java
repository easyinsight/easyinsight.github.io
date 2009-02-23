package com.easyinsight.dataengine;

import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.analysis.InsightRequestMetadata;
import com.easyinsight.analysis.CrossTabDataResults;
import com.easyinsight.analysis.IDataService;

import java.io.Serializable;

/**
 * User: James Boe
 * Date: Aug 5, 2008
 * Time: 5:55:14 PM
 */
public class CrosstabRequest extends EngineRequest implements Serializable {
    private WSAnalysisDefinition analysisDefinition;
    private boolean preview;

    public CrosstabRequest(WSAnalysisDefinition analysisDefinition, boolean preview, InsightRequestMetadata insightRequestMetadata) {
        setInvocationID(generateInvocationID());
        this.analysisDefinition = analysisDefinition;
        this.preview = preview;
        setInsightRequestMetadata(insightRequestMetadata);
    }

    public WSAnalysisDefinition getAnalysisDefinition() {
        return analysisDefinition;
    }

    public void setAnalysisDefinition(WSAnalysisDefinition analysisDefinition) {
        this.analysisDefinition = analysisDefinition;
    }

    public boolean isPreview() {
        return preview;
    }

    public void setPreview(boolean preview) {
        this.preview = preview;
    }

    public long getFeedID() {
        return analysisDefinition.getDataFeedID();
    }

    public EngineResponse execute(IDataService dataService) {
        CrossTabDataResults results = dataService.pivot(
                analysisDefinition, preview, getInsightRequestMetadata());
        return new EngineResponse(getInvocationID(), results);
    }
}
