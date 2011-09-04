package com.easyinsight.pipeline;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.DataResults;
import com.easyinsight.analysis.IRow;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

import java.util.Date;

/**
 * User: James Boe
 * Date: May 18, 2009
 * Time: 3:18:51 PM
 */
public class ACSDateTransformComponent implements IComponent {

    private AnalysisItem analysisItem;

    public ACSDateTransformComponent(AnalysisItem analysisItem) {
        this.analysisItem = analysisItem;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        for (IRow row : dataSet.getRows()) {

            Value value = row.getValue(analysisItem.createAggregateKey());
            Value transformedValue = analysisItem.transformValue(value, pipelineData.getInsightRequestMetadata(), false);
            if (transformedValue.type() == Value.DATE) {
                DateValue dateValue = (DateValue) transformedValue;
                dateValue.setDate(new Date(dateValue.getDate().getTime() - (7 * 1000 * 60 * 60)));
            }
            row.addValue(analysisItem.createAggregateKey(), transformedValue);

        }
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}
