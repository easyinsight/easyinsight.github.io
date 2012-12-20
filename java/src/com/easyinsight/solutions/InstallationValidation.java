package com.easyinsight.solutions;

/**
 * User: jamesboe
 * Date: 12/13/12
 * Time: 8:59 AM
 */
public class InstallationValidation {
    private long existingConnectionID;
    private boolean atSizeLimit;
    private boolean enterpriseLimit;

    public boolean isEnterpriseLimit() {
        return enterpriseLimit;
    }

    public void setEnterpriseLimit(boolean enterpriseLimit) {
        this.enterpriseLimit = enterpriseLimit;
    }

    public long getExistingConnectionID() {
        return existingConnectionID;
    }

    public void setExistingConnectionID(long existingConnectionID) {
        this.existingConnectionID = existingConnectionID;
    }

    public boolean isAtSizeLimit() {
        return atSizeLimit;
    }

    public void setAtSizeLimit(boolean atSizeLimit) {
        this.atSizeLimit = atSizeLimit;
    }
}
