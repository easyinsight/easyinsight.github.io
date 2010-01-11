package com.easyinsight.users;

import java.util.Date;
import java.util.List;

/**
 * User: James Boe
 * Date: Jul 28, 2008
 * Time: 12:37:50 AM
 */
public class UserTransferObject {
    private String userName;
    private long userID;
    private String email;
    private String name;
    private Date lastLoginDate;
    private List<SubscriptionLicense> licenses;

    private boolean accountAdmin;
    private boolean dataSourceCreator;
    private boolean insightCreator;

    public UserTransferObject() {
    }

    public UserTransferObject(String userName, long userID, String email, String name,
                              List<SubscriptionLicense> licenses) {
        this.userName = userName;
        this.userID = userID;
        this.email = email;
        this.name = name;
        this.licenses = licenses;
    }        

    public boolean isAccountAdmin() {
        return accountAdmin;
    }

    public void setAccountAdmin(boolean accountAdmin) {
        this.accountAdmin = accountAdmin;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public boolean isDataSourceCreator() {
        return dataSourceCreator;
    }

    public void setDataSourceCreator(boolean dataSourceCreator) {
        this.dataSourceCreator = dataSourceCreator;
    }

    public boolean isInsightCreator() {
        return insightCreator;
    }

    public void setInsightCreator(boolean insightCreator) {
        this.insightCreator = insightCreator;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SubscriptionLicense> getLicenses() {
        return licenses;
    }

    public void setLicenses(List<SubscriptionLicense> licenses) {
        this.licenses = licenses;
    }

    public User toUser() {
        User user = new User();
        user.setAccountAdmin(isAccountAdmin());
        user.setDataSourceCreator(isDataSourceCreator());
        user.setEmail(getEmail());
        user.setInsightCreator(isInsightCreator());
        user.setName(getName());
        user.setUserName(getUserName());
        return user;
    }
}
