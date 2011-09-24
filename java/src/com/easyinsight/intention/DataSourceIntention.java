package com.easyinsight.intention;

/**
 * User: jamesboe
 * Date: 9/20/11
 * Time: 2:15 PM
 */
public class DataSourceIntention extends Intention {
    private boolean refreshData;
    private boolean adminData;

    public boolean isAdminData() {
        return adminData;
    }

    public void setAdminData(boolean adminData) {
        this.adminData = adminData;
    }

    public boolean isRefreshData() {
        return refreshData;
    }

    public void setRefreshData(boolean refreshData) {
        this.refreshData = refreshData;
    }
}
