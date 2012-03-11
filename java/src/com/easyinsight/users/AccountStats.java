package com.easyinsight.users;

import java.util.List;

/**
 * User: James Boe
 * Date: Mar 27, 2009
 * Time: 12:34:56 PM
 */
public class AccountStats {
    private long usedSpace;
    private long maxSpace;
    private int availableUsers;
    private int currentUsers;
    private long apiUsedToday;
    private long apiMaxToday;
    private List<DataSourceStats> statsList;

    public List<DataSourceStats> getStatsList() {
        return statsList;
    }

    public void setStatsList(List<DataSourceStats> statsList) {
        this.statsList = statsList;
    }

    public int getAvailableUsers() {
        return availableUsers;
    }

    public void setAvailableUsers(int availableUsers) {
        this.availableUsers = availableUsers;
    }

    public int getCurrentUsers() {
        return currentUsers;
    }

    public void setCurrentUsers(int currentUsers) {
        this.currentUsers = currentUsers;
    }

    public long getApiUsedToday() {
        return apiUsedToday;
    }

    public void setApiUsedToday(long apiUsedToday) {
        this.apiUsedToday = apiUsedToday;
    }

    public long getApiMaxToday() {
        return apiMaxToday;
    }

    public void setApiMaxToday(long apiMaxToday) {
        this.apiMaxToday = apiMaxToday;
    }

    public long getUsedSpace() {
        return usedSpace;
    }

    public void setUsedSpace(long usedSpace) {
        this.usedSpace = usedSpace;
    }

    public long getMaxSpace() {
        return maxSpace;
    }

    public void setMaxSpace(long maxSpace) {
        this.maxSpace = maxSpace;
    }
}
