package com.easyinsight.users;

import java.util.List;

/**
 * User: James Boe
 * Date: Mar 27, 2009
 * Time: 12:34:56 PM
 */
public class AccountStats {

    private long coreSpace;
    private long usedSpace;
    private long maxSpace;
    private String usedSpaceString;
    private String maxSpaceString;
    private int addonStorageUnits;

    // new

    private int coreDesigners;
    private int addonDesigners;
    private int usedDesigners;

    private int reportViewers;

    // old

    private int availableUsers;
    private int currentUsers;

    private long apiUsedToday;
    private long apiMaxToday;

    // new

    private int coreSmallBizConnections;
    private int currentSmallBizConnections;
    private int addonSmallBizConnections;

    private boolean unlimitedQuickbaseConnections;
    private int addonQuickbaseConnections;
    private int addonSalesforceConnections;
    private int usedQuickbaseConnections;
    private int usedSalesforceConnections;

    private List<DataSourceStats> statsList;

    public boolean isUnlimitedQuickbaseConnections() {
        return unlimitedQuickbaseConnections;
    }

    public void setUnlimitedQuickbaseConnections(boolean unlimitedQuickbaseConnections) {
        this.unlimitedQuickbaseConnections = unlimitedQuickbaseConnections;
    }

    public int getAddonQuickbaseConnections() {
        return addonQuickbaseConnections;
    }

    public void setAddonQuickbaseConnections(int addonQuickbaseConnections) {
        this.addonQuickbaseConnections = addonQuickbaseConnections;
    }

    public int getAddonSalesforceConnections() {
        return addonSalesforceConnections;
    }

    public void setAddonSalesforceConnections(int addonSalesforceConnections) {
        this.addonSalesforceConnections = addonSalesforceConnections;
    }

    public int getUsedQuickbaseConnections() {
        return usedQuickbaseConnections;
    }

    public void setUsedQuickbaseConnections(int usedQuickbaseConnections) {
        this.usedQuickbaseConnections = usedQuickbaseConnections;
    }

    public int getUsedSalesforceConnections() {
        return usedSalesforceConnections;
    }

    public void setUsedSalesforceConnections(int usedSalesforceConnections) {
        this.usedSalesforceConnections = usedSalesforceConnections;
    }

    public int getAddonDesigners() {
        return addonDesigners;
    }

    public void setAddonDesigners(int addonDesigners) {
        this.addonDesigners = addonDesigners;
    }

    public int getUsedDesigners() {
        return usedDesigners;
    }

    public void setUsedDesigners(int usedDesigners) {
        this.usedDesigners = usedDesigners;
    }

    public int getReportViewers() {
        return reportViewers;
    }

    public void setReportViewers(int reportViewers) {
        this.reportViewers = reportViewers;
    }

    public String getUsedSpaceString() {
        return usedSpaceString;
    }

    public void setUsedSpaceString(String usedSpaceString) {
        this.usedSpaceString = usedSpaceString;
    }

    public String getMaxSpaceString() {
        return maxSpaceString;
    }

    public void setMaxSpaceString(String maxSpaceString) {
        this.maxSpaceString = maxSpaceString;
    }

    public int getAddonStorageUnits() {
        return addonStorageUnits;
    }

    public void setAddonStorageUnits(int addonStorageUnits) {
        this.addonStorageUnits = addonStorageUnits;
    }

    public long getCoreSpace() {
        return coreSpace;
    }

    public void setCoreSpace(long coreSpace) {
        this.coreSpace = coreSpace;
    }

    public int getCoreDesigners() {
        return coreDesigners;
    }

    public void setCoreDesigners(int coreDesigners) {
        this.coreDesigners = coreDesigners;
    }

    public int getCoreSmallBizConnections() {
        return coreSmallBizConnections;
    }

    public void setCoreSmallBizConnections(int coreSmallBizConnections) {
        this.coreSmallBizConnections = coreSmallBizConnections;
    }

    public int getCurrentSmallBizConnections() {
        return currentSmallBizConnections;
    }

    public void setCurrentSmallBizConnections(int currentSmallBizConnections) {
        this.currentSmallBizConnections = currentSmallBizConnections;
    }

    public int getAddonSmallBizConnections() {
        return addonSmallBizConnections;
    }

    public void setAddonSmallBizConnections(int addonSmallBizConnections) {
        this.addonSmallBizConnections = addonSmallBizConnections;
    }

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
