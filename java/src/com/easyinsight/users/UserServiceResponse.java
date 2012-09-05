package com.easyinsight.users;

import com.easyinsight.analysis.ReportTypeOptions;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.preferences.*;
import org.hibernate.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private String apiKey;
    private String apiSecretKey;
    private boolean newsletterEnabled;
    private long fixedDashboardID;
    private ReportTypeOptions reportTypeOptions;
    private boolean subdomainEnabled;
    private String personaName;
    private byte[] reportImage;
    private boolean refreshReports;
    private boolean analyst;
    private int pricingModel;

    public static UserServiceResponse createResponseWithUISettings(User user, ApplicationSkin applicationSkin, String personaName) {
        return createResponse(user, applicationSkin, personaName);
    }

    public static UserServiceResponse createResponse(User user, Session session, EIConnection conn) throws SQLException {
        return createResponse(user, ApplicationSkinSettings.retrieveSkin(user.getUserID(), session, user.getAccount().getAccountID()), conn);
    }

    public static UserServiceResponse createResponse(User user, ApplicationSkin applicationSkin, EIConnection conn) throws SQLException {
        Account account = user.getAccount();
        if (user.getPersonaID() != null) {
            user.setUiSettings(UISettingRetrieval.getUISettings(user.getPersonaID(), conn, account));
        }
        PreparedStatement stmt = conn.prepareStatement("SELECT PERSONA.persona_name FROM USER, PERSONA WHERE USER.PERSONA_ID = PERSONA.PERSONA_ID AND USER.USER_ID = ?");
        stmt.setLong(1, user.getUserID());
        ResultSet rs = stmt.executeQuery();

        String personaName = null;
        if (rs.next()) {
            personaName = rs.getString(1);
        }
        byte[] bytes = null;
        if (applicationSkin.getReportHeaderImage() != null) {
            bytes = new PreferencesService().getImage(applicationSkin.getReportHeaderImage().getId(), conn);
        }
        UserServiceResponse response = createResponse(user, applicationSkin, personaName);
        response.setReportImage(bytes);
        return response;
    }

    private static UserServiceResponse createResponse(User user, ApplicationSkin applicationSkin, String personaName)  {
        Account account = user.getAccount();
        System.out.println("Log in from " + user.getUserID() + " - " + user.getEmail());
        byte[] bytes = null;
        if (applicationSkin.getReportHeaderImage() != null) {
            EIConnection conn = Database.instance().getConnection();
            try {
                bytes = new PreferencesService().getImage(applicationSkin.getReportHeaderImage().getId(), conn);    
            } finally {
                Database.closeConnection(conn);
            }
        }
        UserServiceResponse response = new UserServiceResponse(true, user.getUserID(), user.getAccount().getAccountID(), user.getName(),
                            user.getAccount().getAccountType(), account.getMaxSize(), user.getEmail(), user.getUserName(), user.isAccountAdmin(),
                                (user.getAccount().isBillingInformationGiven() != null && user.getAccount().isBillingInformationGiven()), user.getAccount().getAccountState(),
                                user.getUiSettings(), user.getFirstName(), !account.isUpgraded(), !user.isInitialSetupDone(), user.getLastLoginDate(), account.getName(),
                                user.getPersonaID(), account.getDateFormat(), account.isDefaultReportSharing(), true, user.isGuestUser(),
                                account.getCurrencySymbol(), applicationSkin, account.getFirstDayOfWeek(),
                                user.getUserKey(), user.getUserSecretKey(), user.isOptInEmail(), user.getFixedDashboardID(),
                    new ReportTypeOptions(), user.getAccount().isSubdomainEnabled(), personaName, user.isRefreshReports(), user.isAnalyst(), account.getPricingModel());
        response.setReportImage(bytes);
        return response;
    }

    public UserServiceResponse(boolean successful, String failureMessage) {
        this.successful = successful;
        this.failureMessage = failureMessage;
    }

    private UserServiceResponse(boolean successful, long userID, long accountID, String name, int accountType,
                               long spaceAllowed, String email, String userName, boolean accountAdmin,
                               boolean billingInformationGiven, int accountState,
                               UISettings uiSettings, String firstName, boolean freeUpgradePossible,
                               boolean firstLogin, Date lastLoginDate, String accountName,
                               Long personaID, int dateFormat, boolean defaultReportSharing, boolean cookieLogin,
                               boolean guestUser, String currencySymbol, ApplicationSkin applicationSkin, int firstDayOfWeek,
                               String apiKey, String apiSecretKey, boolean newsletterEnabled, Long fixedDashboardID, ReportTypeOptions reportTypeOptions,
                               boolean subdomainEnabled, String personaName, boolean refreshReports, boolean analyst, int pricingModel) {
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
        this.apiKey = apiKey;
        this.apiSecretKey = apiSecretKey;
        this.newsletterEnabled = newsletterEnabled;
        if (fixedDashboardID == null) {
            fixedDashboardID = 0L;
        }
        this.fixedDashboardID = fixedDashboardID;
        this.reportTypeOptions = reportTypeOptions;
        this.subdomainEnabled = subdomainEnabled;
        this.personaName = personaName;
        this.refreshReports = refreshReports;
        this.analyst = analyst;
        this.pricingModel = pricingModel;
    }

    public boolean isAnalyst() {
        return analyst;
    }

    public void setAnalyst(boolean analyst) {
        this.analyst = analyst;
    }

    public boolean isRefreshReports() {
        return refreshReports;
    }

    public void setRefreshReports(boolean refreshReports) {
        this.refreshReports = refreshReports;
    }

    public byte[] getReportImage() {
        return reportImage;
    }

    public void setReportImage(byte[] reportImage) {
        this.reportImage = reportImage;
    }

    public String getPersonaName() {
        return personaName;
    }

    public void setPersonaName(String personaName) {
        this.personaName = personaName;
    }

    public int getPricingModel() {
        return pricingModel;
    }

    public void setPricingModel(int pricingModel) {
        this.pricingModel = pricingModel;
    }

    public ReportTypeOptions getReportTypeOptions() {
        return reportTypeOptions;
    }

    public void setReportTypeOptions(ReportTypeOptions reportTypeOptions) {
        this.reportTypeOptions = reportTypeOptions;
    }

    public long getFixedDashboardID() {
        return fixedDashboardID;
    }

    public void setFixedDashboardID(long fixedDashboardID) {
        this.fixedDashboardID = fixedDashboardID;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecretKey() {
        return apiSecretKey;
    }

    public void setApiSecretKey(String apiSecretKey) {
        this.apiSecretKey = apiSecretKey;
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

    public boolean isNewsletterEnabled() {
        return newsletterEnabled;
    }

    public void setNewsletterEnabled(boolean newsletterEnabled) {
        this.newsletterEnabled = newsletterEnabled;
    }

    public boolean isSubdomainEnabled() {
        return subdomainEnabled;
    }

    public void setSubdomainEnabled(boolean subdomainEnabled) {
        this.subdomainEnabled = subdomainEnabled;
    }
}
