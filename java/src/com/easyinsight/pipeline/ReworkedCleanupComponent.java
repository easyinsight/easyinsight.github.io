package com.easyinsight.pipeline;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.DataResults;
import com.easyinsight.analysis.IRow;
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
        Iterator<AnalysisItem> iter = pipelineData.getAllRequestedItems().iterator();
        while (iter.hasNext()) {
            AnalysisItem analysisItem = iter.next();
            if (analysisItem.getPipelineSections().contains(pipelineName)) {
                analysisItem.getPipelineSections().remove(pipelineName);
                if (analysisItem.getPipelineSections().isEmpty()) {
                    iter.remove();
                    for (IRow row : dataSet.getRows()) {
                        row.addValue(analysisItem.createAggregateKey(), new EmptyValue());
                    }
                }
            }

        }
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {

    }
}
