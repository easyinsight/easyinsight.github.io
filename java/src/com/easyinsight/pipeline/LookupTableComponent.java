package com.easyinsight.pipeline;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.DataResults;
import com.easyinsight.analysis.IRow;
import com.easyinsight.core.DerivedKey;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.etl.LookupPair;
import com.easyinsight.etl.LookupTable;

import java.util.*;

/**
 * User: jamesboe
 * Date: Apr 4, 2010
 * Time: 7:13:06 PM
 */
public class LookupTableComponent implements IComponent {

    private LookupTable lookupTable;
    private Map<String, Value> lookupMap = new HashMap<String, Value>();

    public LookupTableComponent(LookupTable lookupTable) {
        this.lookupTable = lookupTable;
        for (LookupPair lookupPair : lookupTable.getLookupPairs()) {
            lookupMap.put(lookupPair.getSourceValue().toString(), lookupPair.getTargetValue());
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
        AnalysisItem sourceField = findMatch(lookupTable.getSourceField(), displayMap, keyMap);
        AnalysisItem targetField = findMatch(lookupTable.getTargetField(), displayMap, keyMap);
        for (IRow row : dataSet.getRows()) {
            Value sourceValue = row.getValue(sourceField.createAggregateKey());
            Value targetValue = lookupMap.get(sourceValue.toString());
            if (targetValue == null) {
                targetValue = new EmptyValue();
            }
            row.addValue(targetField.createAggregateKey(), targetValue);
        }
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}
