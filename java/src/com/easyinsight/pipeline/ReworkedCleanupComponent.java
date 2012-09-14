package com.easyinsight.pipeline;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.DataResults;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.InsightRequestMetadata;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.dataset.DataSet;

import java.util.Iterator;

/**
 * User: jamesboe
 * Date: 7/18/12
 * Time: 12:05 PM
 */
public class ReworkedCleanupComponent implements IComponent {

    private String pipelineName;

    public ReworkedCleanupComponent(String pipelineName) {
        this.pipelineName = pipelineName;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        InsightRequestMetadata insightRequestMetadata = pipelineData.getInsightRequestMetadata();
        Iterator<AnalysisItem> iter = pipelineData.getAllRequestedItems().iterator();
        while (iter.hasNext()) {
            AnalysisItem analysisItem = iter.next();
            if (insightRequestMetadata.getPipelines(analysisItem).contains(pipelineName)) {
                insightRequestMetadata.getPipelines(analysisItem).remove(pipelineName);
                if (insightRequestMetadata.getPipelines(analysisItem).isEmpty()) {
                    iter.remove();
                    for (IRow row : dataSet.getRows()) {
                        row.addValue(analysisItem.createAggregateKey(), new EmptyValue());
                    }
                }
            }
            /*
            identified edge from Basecamp to Harvest
defining filter between Basecamp and Harvest
adding filter for Harvest
identified edge from Users to Time Tracking
identified edge from Users to Time Tracking
identified edge from Projects to Time Tracking
defining filter between Projects and Time Tracking
adding filter for Time Tracking
defining filter between Time Tracking and Users
Swapping Time Tracking and Projects
joining Projects to Time Tracking with 1 and 2
defining filter between Users and Time Tracking
joining Users to Time Tracking with 127 and 2
Swapping Harvest and Basecamp
joining Basecamp to Harvest with 409 and 2
             */
            /*
            identified edge from Basecamp (Atn) to Harvest
defining filter between Basecamp (Atn) and Harvest
adding filter for Harvest
identified edge from Projects to Time Tracking
identified edge from Users to Time Tracking
identified edge from Projects to Time Tracking
defining filter between Projects and Time Tracking
adding filter for Time Tracking
defining filter between Time Tracking and Users
Swapping Time Tracking and Projects
joining Projects to Time Tracking with 1 and 2
defining filter between Users and Time Tracking
joining Users to Time Tracking with 127 and 2
Swapping Harvest and Basecamp (Atn)
joining Basecamp (Atn) to Harvest with 67 and 2
             */
        }
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {

    }
}
