package com.easyinsight.intention;

import com.easyinsight.analysis.AnalysisItem;

/**
 * User: jamesboe
 * Date: 9/18/11
 * Time: 1:49 PM
 */
public class CustomFieldIntention extends Intention {
    private AnalysisItem field;

    public CustomFieldIntention(AnalysisItem field) {
        this.field = field;
    }

    public CustomFieldIntention() {
    }

    public AnalysisItem getField() {
        return field;
    }

    public void setField(AnalysisItem field) {
        this.field = field;
    }
}
