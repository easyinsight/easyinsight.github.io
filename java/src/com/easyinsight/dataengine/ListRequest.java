package com.easyinsight.dataengine;

import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.analysis.InsightRequestMetadata;
import com.easyinsight.analysis.IDataService;
import com.easyinsight.analysis.ListDataResults;

import java.io.Serializable;

/**
 * User: James Boe
 * Date: Aug 5, 2008
 * Time: 5:55:04 PM
 */
public class ListRequest extends EngineRequest implements Serializable {
    private WSAnalysisDefinition listDefinition;
    private boolean preview;

    public ListRequest() {
    }

    public ListRequest(WSAnalysisDefinition listDefinition, boolean preview, InsightRequestMetadata insightRequestMetadata) {
        setInvocationID(generateInvocationID());        
        this.listDefinition = listDefinition;
        this.preview = preview;
        setInsightRequestMetadata(insightRequestMetadata);
    }

    public WSAnalysisDefinition getListDefinition() {
        return listDefinition;
    }

    public void setListDefinition(WSAnalysisDefinition listDefinition) {
        this.listDefinition = listDefinition;
    }

    public boolean isPreview() {
        return preview;
    }

    public void setPreview(boolean preview) {
        this.preview = preview;
    }

    public long getFeedID() {
        return listDefinition.getDataFeedID();
    }

    public EngineResponse execute(IDataService dataService) {
        ListDataResults listDataResults = dataService.list(listDefinition, preview, getInsightRequestMetadata());
        return new EngineResponse(getInvocationID(), listDataResults);
    }
}
