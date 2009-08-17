package com.easyinsight.preferences;

/**
 * User: jamesboe
 * Date: Aug 14, 2009
 * Time: 7:34:22 PM
 */
public class UserPreferences {
    private boolean introOption;
    private boolean myDataOption;
    private boolean marketplaceOption;
    private boolean groupsOption;
    private boolean kpiOption;
    private boolean solutionsOption;
    private boolean apiOption;
    private boolean accountOption;
    private long userPreferencesID;

    public long getUserPreferencesID() {
        return userPreferencesID;
    }

    public void setUserPreferencesID(long userPreferencesID) {
        this.userPreferencesID = userPreferencesID;
    }

    public boolean isIntroOption() {
        return introOption;
    }

    public void setIntroOption(boolean introOption) {
        this.introOption = introOption;
    }

    public boolean isMyDataOption() {
        return myDataOption;
    }

    public void setMyDataOption(boolean myDataOption) {
        this.myDataOption = myDataOption;
    }

    public boolean isMarketplaceOption() {
        return marketplaceOption;
    }

    public void setMarketplaceOption(boolean marketplaceOption) {
        this.marketplaceOption = marketplaceOption;
    }

    public boolean isGroupsOption() {
        return groupsOption;
    }

    public void setGroupsOption(boolean groupsOption) {
        this.groupsOption = groupsOption;
    }

    public boolean isKpiOption() {
        return kpiOption;
    }

    public void setKpiOption(boolean kpiOption) {
        this.kpiOption = kpiOption;
    }

    public boolean isSolutionsOption() {
        return solutionsOption;
    }

    public void setSolutionsOption(boolean solutionsOption) {
        this.solutionsOption = solutionsOption;
    }

    public boolean isApiOption() {
        return apiOption;
    }

    public void setApiOption(boolean apiOption) {
        this.apiOption = apiOption;
    }

    public boolean isAccountOption() {
        return accountOption;
    }

    public void setAccountOption(boolean accountOption) {
        this.accountOption = accountOption;
    }
}
