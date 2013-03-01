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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDLS userDLS = (UserDLS) o;

        if (dataSourceID != userDLS.dataSourceID) return false;
        if (!dataSourceName.equals(userDLS.dataSourceName)) return false;
        if (!userDLSFilterList.equals(userDLS.userDLSFilterList)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = dataSourceName.hashCode();
        result = 31 * result + (int) (dataSourceID ^ (dataSourceID >>> 32));
        result = 31 * result + userDLSFilterList.hashCode();
        return result;
    }
}
