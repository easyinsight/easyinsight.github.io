package com.easyinsight.exchange;

/**
 * User: jamesboe
 * Date: Dec 10, 2009
 * Time: 10:41:43 AM
 */
public class ExchangePackageData extends ExchangeData {
    private long packageID;
    private String packageName;

    public long getPackageID() {
        return packageID;
    }

    public void setPackageID(long packageID) {
        this.packageID = packageID;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
