package com.easyinsight.analysis;

import com.easyinsight.users.DataSourceStats;

import java.util.List;

/**
 * User: jamesboe
 * Date: Nov 3, 2010
 * Time: 11:01:28 AM
 */
public class StorageLimitFault extends ReportFault {
    private String message;
    private List<DataSourceStats> statsList;

    public StorageLimitFault() {
    }

    public StorageLimitFault(String message, List<DataSourceStats> statsList) {
        this.message = message;
        this.statsList = statsList;
    }

    public List<DataSourceStats> getStatsList() {
        return statsList;
    }

    public void setStatsList(List<DataSourceStats> statsList) {
        this.statsList = statsList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
