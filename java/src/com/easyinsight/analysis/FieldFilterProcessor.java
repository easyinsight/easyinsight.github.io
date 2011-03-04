package com.easyinsight.analysis;

import com.easyinsight.core.EmptyValue;
import com.easyinsight.dataset.DataSet;

/**
 * User: jamesboe
 * Date: Sep 13, 2010
 * Time: 6:46:15 PM
 */
public class FieldFilterProcessor implements IFilterProcessor {

    private AnalysisItem analysisItem;

    public FieldFilterProcessor(AnalysisItem analysisItem) {
        this.analysisItem = analysisItem;
    }

    public void createRow(DataSet resultDataSet, IRow row, FilterDefinition filterDefinition, boolean matched) {
        IRow newRow = resultDataSet.createRow();
        if (!matched) {
            if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                row.addValue(analysisItem.createAggregateKey(), new EmptyValue());
            } else {
                row.getValues().clear();
            }
        }
        newRow.addValues(row);
    }
}
