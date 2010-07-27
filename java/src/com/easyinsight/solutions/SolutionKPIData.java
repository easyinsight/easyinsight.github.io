package com.easyinsight.solutions;

import com.easyinsight.export.DataSourceRefreshActivity;

/**
 * User: jamesboe
 * Date: Jul 26, 2010
 * Time: 4:20:17 PM
 */
public class SolutionKPIData {

    private boolean addDataSourceToGroup;
    private DataSourceRefreshActivity activity;
    private long dataSourceID;
    private int utcOffset;

    public int getUtcOffset() {
        return utcOffset;
    }

    public void setUtcOffset(int utcOffset) {
        this.utcOffset = utcOffset;
    }

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    public boolean isAddDataSourceToGroup() {
        return addDataSourceToGroup;
    }

    public void setAddDataSourceToGroup(boolean addDataSourceToGroup) {
        this.addDataSourceToGroup = addDataSourceToGroup;
    }

    public DataSourceRefreshActivity getActivity() {
        return activity;
    }

    public void setActivity(DataSourceRefreshActivity activity) {
        this.activity = activity;
    }
}
