package com.easyinsight.scrubbing;

import com.easyinsight.IRow;
import com.easyinsight.core.Value;

/**
 * User: James Boe
 * Date: Oct 15, 2008
 * Time: 4:02:50 PM
 */
public class RowBasedScrubber implements IScrubber {

    private RowBasedScrub rowBasedScrub;

    public void apply(IRow row) {
        Value fromValue = row.getValue(rowBasedScrub.getTargetKey());
    }
}
