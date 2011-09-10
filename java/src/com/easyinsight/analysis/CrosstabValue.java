package com.easyinsight.analysis;

import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 9/10/11
 * Time: 11:47 AM
 */
public class CrosstabValue {
    private Value value;
    private AnalysisItem header;

    public CrosstabValue(Value value, AnalysisItem header) {
        this.value = value;
        this.header = header;
    }

    public Value getValue() {
        return value;
    }

    public AnalysisItem getHeader() {
        return header;
    }
}
