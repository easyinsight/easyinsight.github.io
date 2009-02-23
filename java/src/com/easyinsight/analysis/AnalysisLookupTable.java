package com.easyinsight.analysis;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.scrubbing.LookupTableScrub;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Value;
import com.easyinsight.core.EmptyValue;

import java.util.Set;
import java.util.List;
import java.util.HashSet;

/**
 * User: James Boe
 * Date: Oct 14, 2008
 * Time: 11:54:45 AM
 */
public class AnalysisLookupTable extends AnalysisItem {
    private AnalysisItem wrappedItem;
    private AnalysisItem sourceItem;
    private List<LookupTableScrub> scrubs;

    public Set<AnalysisItem> getNeededKeys(List<AnalysisItem> fields) {
        Set<AnalysisItem> items = new HashSet<AnalysisItem>();
        items.add(sourceItem);
        return items;
    }



    public Value calculate(DataSet dataSet, IRow row) {
        Value sourceValue = row.getValue(sourceItem.getKey());
        Value value;
        if (sourceValue == null) {
            value = new EmptyValue();
        } else {
            return null;
        }
        return null;
    }

    public int getType() {
        return AnalysisItemTypes.LOOKUP_TABLE;
    }
}
