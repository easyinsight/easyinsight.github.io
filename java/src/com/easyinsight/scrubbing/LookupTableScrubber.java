package com.easyinsight.scrubbing;

import com.easyinsight.IRow;
import com.easyinsight.core.Value;
import com.easyinsight.core.StringValue;

import java.util.Map;
import java.util.HashMap;

/**
 * User: James Boe
 * Date: Jul 30, 2008
 * Time: 1:20:37 PM
 */
public class LookupTableScrubber implements IScrubber {

    private LookupTableScrub lookupTableScrub;
    private Map<String, String> pairs;

    public LookupTableScrubber(LookupTableScrub lookupTableScrub) {
        this.lookupTableScrub = lookupTableScrub;
        pairs = new HashMap<String, String>();
        for (LookupTablePair pair : lookupTableScrub.getLookupTablePairs()) {
            pairs.put(pair.getSourceValue(), pair.getReplaceValue());
        }
    }

    public void apply(IRow row) {
        Value sourceValue = row.getValue(lookupTableScrub.getSourceKey());
        if (sourceValue.type() == Value.STRING) {
            StringValue stringValue = (StringValue) sourceValue;
            String sourceString = stringValue.getValue();
            String targetString = pairs.get(sourceString);
            if (targetString != null) {
                row.addValue(lookupTableScrub.getTargetKey(), targetString);
            }
        }
    }
}
