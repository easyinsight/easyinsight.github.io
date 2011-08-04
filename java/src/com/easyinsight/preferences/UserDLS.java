package com.easyinsight.preferences;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 8/1/11
 * Time: 8:05 PM
 */
public class UserDLS {
    private long dlsID;
    private String dataSourceName;
    private long dataSourceID;
    private List<UserDLSFilter> userDLSFilterList = new ArrayList<UserDLSFilter>();

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public long getDlsID() {
        return dlsID;
    }

    public void setDlsID(long dlsID) {
        this.dlsID = dlsID;
    }

    public List<UserDLSFilter> getUserDLSFilterList() {
        return userDLSFilterList;
    }

    public void setUserDLSFilterList(List<UserDLSFilter> userDLSFilterList) {
        this.userDLSFilterList = userDLSFilterList;
    }
}
