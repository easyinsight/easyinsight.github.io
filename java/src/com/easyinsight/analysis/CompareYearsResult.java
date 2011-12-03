package com.easyinsight.analysis;

import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 12/3/11
 * Time: 9:43 AM
 */
public class CompareYearsResult {
    private Value value;
    private Value header;
    private boolean percentChange;

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public Value getHeader() {
        return header;
    }

    public void setHeader(Value header) {
        this.header = header;
    }

    public boolean isPercentChange() {
        return percentChange;
    }

    public void setPercentChange(boolean percentChange) {
        this.percentChange = percentChange;
    }
}
