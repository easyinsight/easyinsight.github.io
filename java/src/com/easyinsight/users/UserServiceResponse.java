package com.easyinsight.users;


import com.easyinsight.analysis.ReportTypeOptions;
import com.easyinsight.core.EIDescriptor;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.dashboard.DashboardDescriptor;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.export.ExportService;
import com.easyinsight.logging.LogClass;
import com.easyinsight.preferences.*;
import com.easyinsight.security.Roles;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.userupload.UserUploadService;
import org.hibernate.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * User: jboe
 * Date: Jan 2, 2008
 * Time: 5:35:16 PM
 */
public class UserServiceResponse {
    private boolean googleAuth;
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
    private boolean reportMode;
    private Date newsDate;
    private Date newsDismissDate;
    private boolean defaultHTML;
    private boolean accountOverSize;
    private boolean accountReports;
    private boolean tagsAndCopyEnabled;
    private boolean hourlyRefreshEnabled;
    private String thousandsSeperator;
    private String decimalSeperator;
    private String defaultFontFamily;
    private String newsSubject;
    private List<EIDescriptor> topReports;
    private boolean onlyShowTopReports;

    public boolean isOnlyShowTopReports() {
        return onlyShowTopReports;
    }

    public void setOnlyShowTopReports(boolean onlyShowTopReports) {
        this.onlyShowTopReports = onlyShowTopReports;
    }

    public String getNewsSubject() {
        return newsSubject;
    }

    public void setNewsSubject(String newsSubject) {
        this.newsSubject = newsSubject;
    }

    public List<EIDescriptor> getTopReports() {
        return topReports;
    }

    public void setTopReports(List<EIDescriptor> topReports) {
        this.topReports = topReports;
    }

    public String getDefaultFontFamily() {
        return defaultFontFamily;
    }

    public void setDefaultFontFamily(String defaultFontFamily) {
        this.defaultFontFamily = defaultFontFamily;
    }

    public boolean isDefaultHTML() {
        return defaultHTML;
    }

    public void setDefaultHTML(boolean defaultHTML) {
        this.defaultHTML = defaultHTML;
    }

    public String getThousandsSeperator() {
        return thousandsSeperator;
    }

    public void setThousandsSeperator(String thousandsSeperator) {
        this.thousandsSeperator = thousandsSeperator;
    }

    public String getDecimalSeperator() {
        return decimalSeperator;
    }

    public void setDecimalSeperator(String decimalSeperator) {
        this.decimalSeperator = decimalSeperator;
    }

    public boolean isHourlyRefreshEnabled() {
        return hourlyRefreshEnabled;
    }

    public void setHourlyRefreshEnabled(boolean hourlyRefreshEnabled) {
        this.hourlyRefreshEnabled = hourlyRefreshEnabled;
    }

    public boolean isTagsAndCopyEnabled() {
        return tagsAndCopyEnabled;
    }

    public void setTagsAndCopyEnabled(boolean tagsAndCopyEnabled) {
        this.tagsAndCopyEnabled = tagsAndCopyEnabled;
    }

    public boolean isAccountReports() {
        return accountReports;
    }

    public void setAccountReports(boolean accountReports) {
        this.accountReports = accountReports;
    }

    public boolean isGoogleAuth() {
        return googleAuth;
    }

    public void setGoogleAuth(boolean googleAuth) {
        this.googleAuth = googleAuth;
    }

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
        /*Session session = Database.instance().createSession(conn);
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT CUSTOM_ROLLING_INTERVAL_ID FROM ACCOUNT_TO_CUSTOM_ROLLING_INTERVAL WHERE ACCOUNT_ID = ?");
            queryStmt.setLong(1, SecurityUtil.getAccountID());
            ResultSet intervalRS = queryStmt.executeQuery();
            List<CustomRollingInterval> intervals = new ArrayList<CustomRollingInterval>();
            while (intervalRS.next()) {
                long intervalID = intervalRS.getLong(1);
                CustomRollingInterval interval = (CustomRollingInterval) session.createQuery("from CustomRollingInterval where customRollingIntervalID = ?").setLong(0, intervalID).list().get(0);
                intervals.add(interval);
            }
            applicationSkin.setRollingFilterOptions(intervals);
            queryStmt.close();
        } finally {
            session.close();
        }*/
        UserServiceResponse response = createResponse(user, applicationSkin, personaName);
        response.setReportImage(bytes);
        return response;
    }

    private static UserServiceResponse createResponse(User user, ApplicationSkin applicationSkin, String personaName)  {
        Account account = user.getAccount();
        LogClass.info("Log in from " + user.getUserID() + " - " + user.getEmail());
        byte[] bytes = null;
        Date newsDate = null;
        String newsTitle = null;
        boolean accountOverSize = false;
        List<EIDescriptor> topReports = new ArrayList<>();
        EIConnection conn = Database.instance().getConnection();
        try {
            if (applicationSkin.getReportHeaderImage() != null) {
                bytes = new PreferencesService().getImage(applicationSkin.getReportHeaderImage().getId(), conn);
            }
            PreparedStatement dateStmt = conn.prepareStatement("SELECT ENTRY_TIME, NEWS_ENTRY.news_entry_title FROM NEWS_ENTRY ORDER BY NEWS_ENTRY.ENTRY_TIME DESC LIMIT 1");
            ResultSet rs = dateStmt.executeQuery();
            if (rs.next()) {
                newsDate = new Date(rs.getTimestamp(1).getTime());
                newsTitle = rs.getString(2);
            }
            if (account.getPricingModel() == 0) {
                if (account.getMaxDaysOverSizeBoundary() != -1) {
                    accountOverSize = account.getUsedSize() > account.getMaxSize();
                }
            } else {
                if (account.getMaxDaysOverSizeBoundary() != -1) {
                    accountOverSize = account.getUsedSize() > (account.getCoreStorage() + account.getAddonStorageUnits() * 250000000L);
                }
            }
            topReports = getAccountReportsWithConn(conn, user.getAccount().getAccountID());
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
        UserServiceResponse response = new UserServiceResponse(true, user.getUserID(), user.getAccount().getAccountID(), user.getName(),
                            user.getAccount().getAccountType(), account.getMaxSize(), user.getEmail(), user.getUserName(), user.isAccountAdmin(),
                                (user.getAccount().isBillingInformationGiven() != null && user.getAccount().isBillingInformationGiven()), user.getAccount().getAccountState(),
                                user.getUiSettings(), user.getFirstName(), !account.isUpgraded(), !user.isInitialSetupDone(), user.getLastLoginDate(), account.getName(),
                                user.getPersonaID(), account.getDateFormat(), true, user.isGuestUser(),
                                account.getCurrencySymbol(), applicationSkin, account.getFirstDayOfWeek(),
                                user.getUserKey(), user.getUserSecretKey(), user.isOptInEmail(), user.getFixedDashboardID(),
                    new ReportTypeOptions(), user.getAccount().isSubdomainEnabled(), personaName, user.isRefreshReports(), user.isAnalyst(), account.getPricingModel(),
                account.isHeatMapEnabled(), newsDate, user.getNewsDismissDate(), accountOverSize, user.isTestAccountVisible(), account.isTagsAndCopyEnabled(),
                account.isHourlyRefreshEnabled(), account.isUseHTMLVersion(), account.getDefaultFontFamily(), topReports, newsTitle, user.isAssignedDashboardIsFixedView());
        response.setReportImage(bytes);
        String accountLocale;
        if (!"0".equals(user.getUserLocale())) {
            accountLocale = user.getUserLocale();
        } else {
            accountLocale = account.getAccountLocale();
        }
        Locale locale = ExportService.createLocale(accountLocale);
        NumberFormat nf = NumberFormat.getInstance(locale);
        String string = nf.format(5000.1);
        String thousandsSeperator = String.valueOf(string.charAt(1));
        String decimalSeperator = String.valueOf(string.charAt(5));
        response.setThousandsSeperator(thousandsSeperator);
        response.setDecimalSeperator(decimalSeperator);
        if (user.getDateFormat() != 6) {
            if (user.getDateFormat() == 8) {
                response.setDateFormat(0);
            } else {
                response.setDateFormat(user.getDateFormat());
            }
        }
        if (user.getCurrency() == 1) {
            response.setCurrencySymbol("$");
        } else if (user.getCurrency() == 2) {
            response.setCurrencySymbol("EUR");
        } else if (user.getCurrency() == 3) {
            response.setCurrencySymbol("GBP");
        }
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
                               Long personaID, int dateFormat, boolean cookieLogin,
                               boolean guestUser, String currencySymbol, ApplicationSkin applicationSkin, int firstDayOfWeek,
                               String apiKey, String apiSecretKey, boolean newsletterEnabled, Long fixedDashboardID, ReportTypeOptions reportTypeOptions,
                               boolean subdomainEnabled, String personaName, boolean refreshReports, boolean analyst, int pricingModel, boolean reportMode,
                               Date newsDate, Date newsDismissDate, boolean accountOverSize, boolean accountReports, boolean tagsAndCopyEnabled,
                               boolean hourlyRefreshEnabled, boolean defaultHTML, String defaultFontFamily, List<EIDescriptor> topReports, String newsTitle,
                               boolean onlyShowTopReports) {
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
        this.reportMode = reportMode;
        this.newsDate = newsDate;
        this.newsDismissDate = newsDismissDate;
        this.accountOverSize = accountOverSize;
        this.accountReports = accountReports;
        this.tagsAndCopyEnabled = tagsAndCopyEnabled;
        this.hourlyRefreshEnabled = hourlyRefreshEnabled;
        this.defaultHTML = defaultHTML;
        this.defaultFontFamily = defaultFontFamily;
        this.topReports = topReports;
        this.newsSubject = newsTitle;
        this.onlyShowTopReports = onlyShowTopReports;
    }

    public boolean isAccountOverSize() {
        return accountOverSize;
    }

    public void setAccountOverSize(boolean accountOverSize) {
        this.accountOverSize = accountOverSize;
    }

    public Date getNewsDismissDate() {
        return newsDismissDate;
    }

    public void setNewsDismissDate(Date newsDismissDate) {
        this.newsDismissDate = newsDismissDate;
    }

    public Date getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(Date newsDate) {
        this.newsDate = newsDate;
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

    public boolean isReportMode() {
        return reportMode;
    }

    public void setReportMode(boolean reportMode) {
        this.reportMode = reportMode;
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

    public static List<EIDescriptor> getAccountReportsWithConn(EIConnection conn, long accountID) throws SQLException {

        List<EIDescriptor> reports = new ArrayList<EIDescriptor>();
        PreparedStatement getReportStmt = conn.prepareStatement("SELECT ANALYSIS.TITLE, ANALYSIS.ANALYSIS_ID, ANALYSIS.DATA_FEED_ID, ANALYSIS.REPORT_TYPE," +
                "ANALYSIS.URL_KEY, ANALYSIS.DESCRIPTION FROM " +
                "ANALYSIS, ACCOUNT_TO_REPORT WHERE ACCOUNT_TO_REPORT.ACCOUNT_ID = ? AND ACCOUNT_TO_REPORT.REPORT_ID = ANALYSIS.ANALYSIS_ID");
        getReportStmt.setLong(1, accountID);
        ResultSet reportRS = getReportStmt.executeQuery();
        while (reportRS.next()) {
            String title = reportRS.getString(1);
            long reportID = reportRS.getLong(2);
            long dataSourceID = reportRS.getLong(3);
            int reportType = reportRS.getInt(4);
            String urlKey = reportRS.getString(5);
            /*try {*/
                InsightDescriptor id = new InsightDescriptor(reportID, title, dataSourceID, reportType, urlKey, Roles.OWNER, true);
                id.setDescription(reportRS.getString(6));
                reports.add(id);
            /*} catch (com.easyinsight.security.SecurityException e) {
                // ignore
            }*/
        }
        getReportStmt.close();
        PreparedStatement getDashboardStmt = conn.prepareStatement("SELECT DASHBOARD.DASHBOARD_NAME, DASHBOARD.DASHBOARD_ID, DASHBOARD.DATA_SOURCE_ID, " +
                "DASHBOARD.URL_KEY, DASHBOARD.DESCRIPTION FROM " +
                "DASHBOARD, ACCOUNT_TO_DASHBOARD WHERE ACCOUNT_TO_DASHBOARD.ACCOUNT_ID = ? AND ACCOUNT_TO_DASHBOARD.DASHBOARD_ID = DASHBOARD.DASHBOARD_ID");
        getDashboardStmt.setLong(1, accountID);
        ResultSet dashboardRS = getDashboardStmt.executeQuery();
        while (dashboardRS.next()) {
            String title = dashboardRS.getString(1);
            long reportID = dashboardRS.getLong(2);
            long dataSourceID = dashboardRS.getLong(3);
            String urlKey = dashboardRS.getString(4);
            /*try {
                SecurityUtil.authorizeDashboard(reportID);*/
                DashboardDescriptor dd = new DashboardDescriptor(title, reportID, urlKey, dataSourceID, Roles.OWNER, "", true);
                dd.setDescription(dashboardRS.getString(5));
                reports.add(dd);
            /*} catch (com.easyinsight.security.SecurityException e) {
                // ignore
            }*/
        }
        getDashboardStmt.close();
        return reports;
    }
}
