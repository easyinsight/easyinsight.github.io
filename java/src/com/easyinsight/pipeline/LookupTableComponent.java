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

    private AnalysisItem findMatch(Key key, Collection<AnalysisItem> reportItems, Collection<AnalysisItem> analysisItems) {
        for (AnalysisItem analysisItem : reportItems) {
            Key analysisItemKey = analysisItem.getKey();
            if (analysisItemKey.matchesOrContains(key)) {
                return analysisItem;
            }
        }
        for (AnalysisItem analysisItem : analysisItems) {
            Key analysisItemKey = analysisItem.getKey();
            if (analysisItemKey.matchesOrContains(key)) {
                return analysisItem;
            }
        }
        return null;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        AnalysisItem sourceField = findMatch(lookupTable.getSourceField().getKey(), pipelineData.getReportItems(), pipelineData.getAllItems());
        AnalysisItem targetField = findMatch(lookupTable.getTargetField().getKey(), pipelineData.getReportItems(), pipelineData.getAllItems());
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
