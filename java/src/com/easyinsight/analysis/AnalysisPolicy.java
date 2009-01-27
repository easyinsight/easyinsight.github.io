package com.easyinsight.analysis;

/**
 * User: James Boe
 * Date: May 19, 2008
 * Time: 11:00:51 AM
 */
public class AnalysisPolicy {
    public static final int PUBLIC = 1;
    public static final int PRIVATE = 2;

    private int policyType;

    public int getPolicyType() {
        return policyType;
    }

    public void setPolicyType(int policyType) {
        this.policyType = policyType;
    }
}
