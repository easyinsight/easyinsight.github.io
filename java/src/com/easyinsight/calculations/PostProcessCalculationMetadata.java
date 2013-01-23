package com.easyinsight.calculations;

import com.easyinsight.analysis.PostProcessOperation;

/**
 * User: jamesboe
 * Date: 12/17/12
 * Time: 1:39 PM
 */
public class PostProcessCalculationMetadata extends CalculationMetadata {
    private PostProcessOperation op;
    private long dataSourceID;

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    public PostProcessOperation getOp() {
        return op;
    }

    public void setOp(PostProcessOperation op) {
        this.op = op;
    }
}
