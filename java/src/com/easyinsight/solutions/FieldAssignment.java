package com.easyinsight.solutions;

import com.easyinsight.analysis.AnalysisItem;

/**
 * User: jamesboe
 * Date: 7/4/14
 * Time: 8:23 AM
 */
public class FieldAssignment {
    private AnalysisItem sourceField;
    private AnalysisItem targetField;

    public AnalysisItem getSourceField() {
        return sourceField;
    }

    public void setSourceField(AnalysisItem sourceField) {
        this.sourceField = sourceField;
    }

    public AnalysisItem getTargetField() {
        return targetField;
    }

    public void setTargetField(AnalysisItem targetField) {
        this.targetField = targetField;
    }
}
