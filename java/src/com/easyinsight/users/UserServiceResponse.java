package com.easyinsight.users;

import com.easyinsight.preferences.ApplicationSkin;
import com.easyinsight.preferences.UISettings;

import java.util.Date;

/**
 * User: jboe
 * Date: Jan 2, 2008
 * Time: 5:35:16 PM
 */
public class UserServiceResponse {
    private boolean successful;
    private String failureMessage;
    private boolean guestUser;
    private long userID;
    private long accountID;
    private String userName;
    private String name;
    private int accountType;
    private long spaceAllowed;
    private String email;
    private boolean accountAdmin;
    private boolean billingInformationGiven;
    private int accountState;
    private UISettings uiSettings;
    private String firstName;
    private boolean freeUpgradePossible;
    private boolean firstLogin;
    private Date lastLoginDate;
    private String accountName;
    private long personaID;
    private int dateFormat;
    private boolean defaultReportSharing;
    private String sessionCookie;
    private boolean cookieLogin;
    private Scenario scenario;
    private String currencySymbol;
    private ApplicationSkin applicationSkin;
    private int firstDayOfWeek;

    public UserServiceResponse(boolean successful, String failureMessage) {
        this.successful = successful;
        this.failureMessage = failureMessage;
    }

    public UserServiceResponse(boolean successful, long userID, long accountID, String name, int accountType,
                               long spaceAllowed, String email, String userName, boolean accountAdmin,
                               boolean billingInformationGiven, int accountState,
                               UISettings uiSettings, String firstName, boolean freeUpgradePossible,
                               boolean firstLogin, Date lastLoginDate, String accountName,
                               Long personaID, int dateFormat, boolean defaultReportSharing, boolean cookieLogin,
                               boolean guestUser, String currencySymbol, ApplicationSkin applicationSkin, int firstDayOfWeek) {
        this.successful = successful;
        this.userID = userID;
        this.accountID = accountID;
        this.name = name;
        this.accountType = accountType;
        this.spaceAllowed = spaceAllowed;
        this.email = email;
        this.userName = userName;
        this.accountAdmin = accountAdmin;
        this.billingInformationGiven = billingInformationGiven;
        this.accountState = accountState;
        this.uiSettings = uiSettings;
        this.firstName = firstName;
        this.freeUpgradePossible = freeUpgradePossible;
        this.firstLogin = firstLogin;
        this.lastLoginDate = lastLoginDate;
        this.accountName = accountName;
        this.personaID = personaID == null ? 0 : personaID;
        this.dateFormat = dateFormat;
        this.defaultReportSharing = defaultReportSharing;
        this.cookieLogin = cookieLogin;
        this.guestUser = guestUser;
        this.currencySymbol = currencySymbol;
        this.applicationSkin = applicationSkin;
        this.firstDayOfWeek = firstDayOfWeek;
    }

    public int getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    public void setFirstDayOfWeek(int firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
    }

    public ApplicationSkin getApplicationSkin() {
        return applicationSkin;
    }

    public void setApplicationSkin(ApplicationSkin applicationSkin) {
        this.applicationSkin = applicationSkin;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    public String getSessionCookie() {
        return sessionCookie;
    }

    public void setSessionCookie(String sessionCookie) {
        this.sessionCookie = sessionCookie;
    }

    public boolean isGuestUser() {
        return guestUser;
    }

    public void setGuestUser(boolean guestUser) {
        this.guestUser = guestUser;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public boolean isDefaultReportSharing() {
        return defaultReportSharing;
    }

    public void setDefaultReportSharing(boolean defaultReportSharing) {
        this.defaultReportSharing = defaultReportSharing;
    }

    public boolean isCookieLogin() {
        return cookieLogin;
    }

    public void setCookieLogin(boolean cookieLogin) {
        this.cookieLogin = cookieLogin;
    }

    public int getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(int dateFormat) {
        this.dateFormat = dateFormat;
    }

    public long getPersonaID() {
        return personaID;
    }

    public void setPersonaID(long personaID) {
        this.personaID = personaID;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstname) {
        this.firstName = firstname;
    }

    public UserServiceResponse() {
    }

    public boolean isFreeUpgradePossible() {
        return freeUpgradePossible;
    }

    public void setFreeUpgradePossible(boolean freeUpgradePossible) {
        this.freeUpgradePossible = freeUpgradePossible;
    }

    public UISettings getUiSettings() {
        return uiSettings;
    }

    public void setUiSettings(UISettings uiSettings) {
        this.uiSettings = uiSettings;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public long getAccountID() {
        return accountID;
    }

    public void setAccountID(long accountID) {
        this.accountID = accountID;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public long getSpaceAllowed() {
        return spaceAllowed;
    }

    public void setSpaceAllowed(long spaceAllowed) {
        this.spaceAllowed = spaceAllowed;
    }

    public boolean isAccountAdmin() {
        return accountAdmin;
    }

    public void setAccountAdmin(boolean accountAdmin) {
        this.accountAdmin = accountAdmin;
    }

    public boolean isBillingInformationGiven() {
        return billingInformationGiven;
    }

    public void setBillingInformationGiven(boolean billingInformationGiven) {
        this.billingInformationGiven = billingInformationGiven;
    }

    public int getAccountState() {
        return accountState;
    }

    public void setAccountState(int accountState) {
        this.accountState = accountState;
    }
}
