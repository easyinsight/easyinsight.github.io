package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

/**
 * User: James Boe
 * Date: May 18, 2009
 * Time: 2:45:33 PM
 */
public class RangeComponent implements IComponent {

    private AnalysisRangeDimension analysisRangeDimension;

    public RangeComponent(AnalysisRangeDimension analysisRangeDimension) {
        this.analysisRangeDimension = analysisRangeDimension;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        for (IRow row : dataSet.getRows()) {
            Value value = row.getValue(analysisRangeDimension.createAggregateKey());
            row.addValue(analysisRangeDimension.createAggregateKey(), analysisRangeDimension.toRange(value));
        }
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}
