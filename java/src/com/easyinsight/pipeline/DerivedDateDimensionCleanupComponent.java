package com.easyinsight.pipeline;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.DataResults;
import com.easyinsight.analysis.DerivedAnalysisDateDimension;
import com.easyinsight.analysis.DerivedAnalysisDimension;
import com.easyinsight.dataset.DataSet;

import java.util.List;

/**
 * User: jamesboe
 * Date: Jul 21, 2010
 * Time: 10:11:07 AM
 */
public class DerivedDateDimensionCleanupComponent implements IComponent {

    private DerivedAnalysisDateDimension dimension;

    public DerivedDateDimensionCleanupComponent(DerivedAnalysisDateDimension dimension) {
        this.dimension = dimension;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        /*List<AnalysisItem> items = dimension.getAnalysisItems(pipelineData.getAllItems(),
                pipelineData.getReportItems(), false, true, false);
        for (AnalysisItem item : items) {
            if (item != dimension) {
                if (pipelineData.decrementReferenceCount(item)) {
                    pipelineData.getReportItems().remove(item);
                }
            }
        }*/
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}