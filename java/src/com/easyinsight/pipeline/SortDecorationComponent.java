package com.easyinsight.pipeline;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.DataResults;
import com.easyinsight.analysis.IRow;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

/**
 * User: jamesboe
 * Date: 2/1/12
 * Time: 9:41 PM
 */
public class SortDecorationComponent implements IComponent {

    private AnalysisItem analysisItem;

    public SortDecorationComponent(AnalysisItem analysisItem) {
        this.analysisItem = analysisItem;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        for (IRow row : dataSet.getRows()) {
            Value sortValue = row.getValue(analysisItem.getSortItem());
            Value value = row.getValue(analysisItem);
            value.setSortValue(sortValue);
        }
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {

    }
}
