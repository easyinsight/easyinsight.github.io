package com.easyinsight.pipeline;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisList;
import com.easyinsight.analysis.DataResults;
import com.easyinsight.analysis.IRow;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.etl.LookupPair;
import com.easyinsight.etl.LookupTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Apr 4, 2010
 * Time: 7:13:06 PM
 */
public class TagLookupTableComponent implements IComponent {

    private List<LookupTable> lookupTables;
    private AnalysisList tagField;
    private Map<String, AnalysisItem> targetFieldMap = new HashMap<>();
    private Map<String, Map<String, Value>> lookupTableToLookupMap = new HashMap<>();

    public TagLookupTableComponent(List<LookupTable> lookupTables, AnalysisList tagField) {
        this.lookupTables = lookupTables;
        this.tagField = tagField;
        for (LookupTable lookupTable : lookupTables) {
            Map<String, Value> lookupMap = new HashMap<>();
            for (LookupPair lookupPair : lookupTable.getLookupPairs()) {
                lookupMap.put(lookupPair.getSourceValue().toString(), lookupPair.getTargetValue());
            }
            lookupTableToLookupMap.put(lookupTable.getName(), lookupMap);

        }
    }

    private AnalysisItem findMatch(AnalysisItem sourceItem, Map<String, List<AnalysisItem>> displayMap, Map<String, List<AnalysisItem>> keyMap) {
        
        List<AnalysisItem> items = displayMap.get(sourceItem.toDisplay());
        if (items == null) {
            items = keyMap.get(sourceItem.toDisplay());
        }
        if (items == null) {
            return null;
        } else {
            return items.get(0);
        }
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        Map<String, List<AnalysisItem>> keyMap = new HashMap<String, List<AnalysisItem>>();
        Map<String, List<AnalysisItem>> displayMap = new HashMap<String, List<AnalysisItem>>();
        for (AnalysisItem analysisItem : pipelineData.getReportItems()) {
            List<AnalysisItem> items = keyMap.get(analysisItem.getKey().toKeyString());
            if (items == null) {
                items = new ArrayList<AnalysisItem>(1);
                keyMap.put(analysisItem.getKey().toKeyString(), items);
            }
            items.add(analysisItem);
            List<AnalysisItem> displayMapItems = displayMap.get(analysisItem.toDisplay());
            if (displayMapItems == null) {
                displayMapItems = new ArrayList<AnalysisItem>(1);
                displayMap.put(analysisItem.toDisplay(), displayMapItems);
            }
            displayMapItems.add(analysisItem);
        }
        for (AnalysisItem analysisItem : pipelineData.getAllItems()) {
            List<AnalysisItem> items = keyMap.get(analysisItem.getKey().toKeyString());
            if (items == null) {
                items = new ArrayList<AnalysisItem>(1);
                keyMap.put(analysisItem.getKey().toKeyString(), items);
            }
            items.add(analysisItem);
            List<AnalysisItem> displayMapItems = displayMap.get(analysisItem.toDisplay());
            if (displayMapItems == null) {
                displayMapItems = new ArrayList<AnalysisItem>(1);
                displayMap.put(analysisItem.toDisplay(), displayMapItems);
            }
            displayMapItems.add(analysisItem);
        }

        for (LookupTable lookupTable : lookupTables) {
            AnalysisItem targetField = findMatch(lookupTable.getTargetField(), displayMap, keyMap);
            targetFieldMap.put(lookupTable.getName(), targetField);
        }

        for (IRow row : dataSet.getRows()) {

            Value value = row.getValue(tagField);
            Value[] values = tagField.transformToMultiple(value);
            for (LookupTable table : lookupTables) {
                Value targetValue = null;
                Map<String, Value> lookupMap = lookupTableToLookupMap.get(table.getName());
                for (Value val : values) {
                    Value testTargetValue = lookupMap.get(val.toString());
                    if (testTargetValue != null && testTargetValue.type() != Value.EMPTY && !"".equals(testTargetValue.toString().trim())) {
                        targetValue = testTargetValue;
                        break;
                    }
                }
                if (targetValue == null) {
                    targetValue = new EmptyValue();
                }
                row.addValue(targetFieldMap.get(table.getName()).createAggregateKey(), targetValue);
            }
        }
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}
