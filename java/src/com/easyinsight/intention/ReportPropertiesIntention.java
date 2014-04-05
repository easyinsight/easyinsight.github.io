package com.easyinsight.intention;

/**
 * User: jamesboe
 * Date: 9/18/11
 * Time: 2:28 PM
 */
public class ReportPropertiesIntention extends Intention {
    private int intentionType;

    public ReportPropertiesIntention() {
    }

    public ReportPropertiesIntention(int intentionType) {
        this.intentionType = intentionType;
    }

    public int getIntentionType() {
        return intentionType;
    }

    public void setIntentionType(int intentionType) {
        this.intentionType = intentionType;
    }
}
