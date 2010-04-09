package com.easyinsight.pipeline;

import com.easyinsight.analysis.AnalysisItem;
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
public class LookupTableComponent implements IComponent {

    private LookupTable lookupTable;
    private Map<String, Value> lookupMap = new HashMap<String, Value>();

    public LookupTableComponent(LookupTable lookupTable) {
        this.lookupTable = lookupTable;
        for (LookupPair lookupPair : lookupTable.getLookupPairs()) {
            lookupMap.put(lookupPair.getSourceValue().toString(), lookupPair.getTargetValue());
        }
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        for (IRow row : dataSet.getRows()) {
            Value sourceValue = row.getValue(lookupTable.getSourceField().createAggregateKey());
            Value targetValue = lookupMap.get(sourceValue.toString());
            if (targetValue == null) {
                targetValue = new EmptyValue();
            }
            row.addValue(lookupTable.getTargetField().createAggregateKey(), targetValue);
        }
        List<AnalysisItem> allRequestedAnalysisItems = new ArrayList<AnalysisItem>(pipelineData.getReport().getAllAnalysisItems());
        boolean itemFound = findItem(lookupTable.getSourceField(), allRequestedAnalysisItems);
        if (!itemFound) {
            pipelineData.getReportItems().remove(lookupTable.getSourceField());
        }        
        return dataSet;
    }

    private boolean findItem(AnalysisItem field, List<AnalysisItem> allRequestedAnalysisItems) {
        int found = 0;
        for (AnalysisItem item : allRequestedAnalysisItems) {
            List<AnalysisItem> items = item.getAnalysisItems(allRequestedAnalysisItems, new ArrayList<AnalysisItem>(), false);
            found += items.contains(field) ? 1 : 0;
        }
        return found > 1;
    }

    public void decorate(DataResults listDataResults) {
    }
}
