package com.easyinsight.analysis;

import com.easyinsight.core.Value;

import java.util.List;

/**
 * User: James Boe
 * Date: Feb 27, 2009
 * Time: 8:31:34 AM
 */
public class MaterializedLastNFilterDefinition extends MaterializedFilterDefinition {

    private int max;
    private int limit;

    public MaterializedLastNFilterDefinition(AnalysisItem key, int limit) {
        super(key);
        this.limit = limit - 1;
    }

    public boolean allows(Value value, Value preTransformValue) {
        // need the maximum value...
        return (value.toDouble() >= this.max);
    }

    @Override
    public boolean requiresDataEarly() {
        return true;
    }

    @Override
    public void handleEarlyData(List<IRow> rows) {
        int max = 0;
        for (IRow row : rows) {
            Value value = row.getValue(getKey().getKey());
            max = (int) Math.max(max, value.toDouble());
        }
        this.max = max - limit;
    }
}
