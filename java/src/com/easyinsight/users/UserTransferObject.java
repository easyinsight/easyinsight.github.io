package com.easyinsight.users;

import java.util.Date;

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
    private boolean defaultReportSharing;

    private long personaID;

    private String firstName;
    private String title;

    private boolean accountAdmin;
    private boolean optInEmail;
    private long fixedDashboardID;
    private boolean initialSetupDone;

    public UserTransferObject() {
    }

    public UserTransferObject(String userName, long userID, String email, String name, String firstName) {
        this.userName = userName;
        this.userID = userID;
        this.email = email;
        this.name = name;
        this.firstName = firstName;
    }

    public boolean isInitialSetupDone() {
        return initialSetupDone;
    }

    public void setInitialSetupDone(boolean initialSetupDone) {
        this.initialSetupDone = initialSetupDone;
    }

    public long getFixedDashboardID() {
        return fixedDashboardID;
    }

    public void setFixedDashboardID(long fixedDashboardID) {
        this.fixedDashboardID = fixedDashboardID;
    }

    public boolean isOptInEmail() {
        return optInEmail;
    }

    public void setOptInEmail(boolean optInEmail) {
        this.optInEmail = optInEmail;
    }

    public long getPersonaID() {
        return personaID;
    }

    public void setPersonaID(long personaID) {
        this.personaID = personaID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public User toUser() {
        User user = new User();
        user.setUserID(getUserID());
        user.setAccountAdmin(isAccountAdmin());
        user.setEmail(getEmail());
        user.setPersonaID(getPersonaID() > 0 ? getPersonaID() : null);
        user.setFirstName(getFirstName());
        user.setTitle(getTitle());
        user.setName(getName());
        user.setUserName(getUserName());
        user.setOptInEmail(isOptInEmail());
        user.setInitialSetupDone(isInitialSetupDone());
        if (getFixedDashboardID() > 0) {
            user.setFixedDashboardID(getFixedDashboardID());
        }
        return user;
    }
}
