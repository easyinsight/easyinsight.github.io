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
    private boolean headerLabel;
    private boolean summaryValue;

    public CrosstabValue() {
    }

    public CrosstabValue(Value value, AnalysisItem header) {
        this.value = value;
        this.header = header;
    }

    public CrosstabValue(Value value, AnalysisItem header, boolean headerLabel, boolean summaryValue) {
        this.value = value;
        this.header = header;
        this.headerLabel = headerLabel;
        this.summaryValue = summaryValue;
    }

    public boolean isHeaderLabel() {
        return headerLabel;
    }

    public void setHeaderLabel(boolean headerLabel) {
        this.headerLabel = headerLabel;
    }

    public boolean isSummaryValue() {
        return summaryValue;
    }

    public void setSummaryValue(boolean summaryValue) {
        this.summaryValue = summaryValue;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public void setHeader(AnalysisItem header) {
        this.header = header;
    }

    public Value getValue() {
        return value;
    }

    public AnalysisItem getHeader() {
        return header;
    }
}
