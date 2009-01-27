package com.easyinsight.analysis;

import com.easyinsight.MaterializedFilterDefinition;
import com.easyinsight.MaterializedRollingFilterDefinition;

/**
 * User: James Boe
 * Date: Oct 28, 2008
 * Time: 11:08:41 AM
 */
public class RollingFilterDefinition extends FilterDefinition {

    private int interval;

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public PersistableFilterDefinition toPersistableFilterDefinition() {
        PersistableRollingFilterDefinition filter = new PersistableRollingFilterDefinition();
        filter.setField(getField());
        filter.setApplyBeforeAggregation(isApplyBeforeAggregation());
        filter.setInterval(getInterval());
        return filter;
    }

    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        return new MaterializedRollingFilterDefinition(getField(), interval, insightRequestMetadata == null ? null : insightRequestMetadata.getNow());
    }


}
