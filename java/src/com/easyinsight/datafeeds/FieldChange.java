package com.easyinsight.datafeeds;

import com.easyinsight.analysis.AnalysisItem;

/**
* User: jamesboe
* Date: 12/29/14
* Time: 3:57 PM
*/
public class FieldChange {
    private AnalysisItem fromField;
    private AnalysisItem toField;
    private long dataSourceID;

    public FieldChange() {
    }

    public FieldChange(AnalysisItem fromField, AnalysisItem toField, long dataSourceID) {
        this.fromField = fromField;
        this.toField = toField;
        this.dataSourceID = dataSourceID;
    }

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    public AnalysisItem getFromField() {
        return fromField;
    }

    public void setFromField(AnalysisItem fromField) {
        this.fromField = fromField;
    }

    public AnalysisItem getToField() {
        return toField;
    }

    public void setToField(AnalysisItem toField) {
        this.toField = toField;
    }
}
