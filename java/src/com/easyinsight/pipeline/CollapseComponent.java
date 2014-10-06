package com.easyinsight.pipeline;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.DataResults;
import com.easyinsight.analysis.IRow;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

import java.util.*;

/**
 * User: jamesboe
 * Date: 9/26/14
 * Time: 7:15 AM
 */
public class CollapseComponent implements IComponent {

    private AnalysisItem collapseOn;

    public CollapseComponent(AnalysisItem collapseOn) {
        this.collapseOn = collapseOn;
    }

    @Override
    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {

        Map<Value, List<IRow>> map = new HashMap<>();
        for (IRow row : dataSet.getRows()) {
            Value collapseOnValue = row.getValue(collapseOn);
            List<IRow> rows = map.get(collapseOnValue);
            if (rows == null) {
                rows = new ArrayList<>();
                map.put(collapseOnValue, rows);
            }
            rows.add(row);
        }
        DataSet newSet = new DataSet();
        for (Map.Entry<Value, List<IRow>> entry : map.entrySet()) {
            IRow newRow = newSet.createRow();
            for (IRow row : entry.getValue()) {
                for (AnalysisItem item : pipelineData.getReportItems()) {
                    Value value = row.getValue(item);
                    if (value.type() != Value.EMPTY) {
                        newRow.addValue(item.createAggregateKey(), value);
                    }
                }
            }
        }
        return newSet;
    }

    @Override
    public void decorate(DataResults listDataResults) {

    }
}
