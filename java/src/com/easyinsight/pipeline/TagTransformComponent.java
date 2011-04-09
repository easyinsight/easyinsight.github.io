package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Value;
import com.easyinsight.core.Key;

import java.util.*;

/**
 * User: James Boe
 * Date: May 18, 2009
 * Time: 2:05:09 PM
 */
public class TagTransformComponent implements IComponent {

    private AnalysisList analysisItem;

    public TagTransformComponent(AnalysisList analysisList) {
        this.analysisItem = analysisList;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        List<IRow> rows = new ArrayList<IRow>(dataSet.getRows());
        DataSet resultSet = new DataSet();
        
        List<IRow> tempRows = new ArrayList<IRow>();
        for (IRow row : rows) {
            Value value = row.getValue(analysisItem);
            Value[] transformedValues = analysisItem.transformToMultiple(value);
            Map<Key, Value> existingContents = row.getValues();
            for (Value multipleVal : transformedValues) {
                multipleVal.setOriginalValue(value);
                Map<Key, Value> newRowContents = new HashMap<Key, Value>(existingContents);
                newRowContents.put(analysisItem.createAggregateKey(), multipleVal);
                IRow tempRow = resultSet.createRow();
                tempRow.addValues(newRowContents);
                tempRows.add(tempRow);
            }
        }
        rows = tempRows;

        return new DataSet(rows);
    }

    public void decorate(DataResults listDataResults) {
    }
}
