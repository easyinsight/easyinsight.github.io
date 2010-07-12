package com.easyinsight.pipeline;

import com.easyinsight.analysis.DataResults;
import com.easyinsight.analysis.IRow;
import com.easyinsight.core.EmptyValue;
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
    private int refs;

    public LookupTableComponent(LookupTable lookupTable, int refs) {
        this.lookupTable = lookupTable;
        for (LookupPair lookupPair : lookupTable.getLookupPairs()) {
            lookupMap.put(lookupPair.getSourceValue().toString(), lookupPair.getTargetValue());
        }
        this.refs = refs;
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
        if (pipelineData.decreaseReferenceCount(lookupTable.getSourceField(), refs)) {
            pipelineData.getReportItems().remove(lookupTable.getSourceField());
        }
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}
