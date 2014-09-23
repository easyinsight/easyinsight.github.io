package com.easyinsight.users;

import com.easyinsight.export.ExportMetadata;
import org.json.JSONException;
import org.json.JSONObject;

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
    private boolean consultant;

    private boolean invoiceRecipient;
    private boolean autoRefreshReports;
    private boolean analyst;

    private boolean testAccountVisible;

    private int currency;
    private String userLocale = "0";
    private int dateFormat = 6;

    public UserTransferObject() {
    }

    public UserTransferObject(String userName, long userID, String email, String name, String firstName) {
        this.userName = userName;
        this.userID = userID;
        this.email = email;
        this.name = name;
        this.firstName = firstName;
    }

    public boolean isConsultant() {
        return consultant;
    }

    public void setConsultant(boolean consultant) {
        this.consultant = consultant;
    }

    public boolean isTestAccountVisible() {
        return testAccountVisible;
    }

    public void setTestAccountVisible(boolean testAccountVisible) {
        this.testAccountVisible = testAccountVisible;
    }

    public boolean isAnalyst() {
        return analyst;
    }

    public void setAnalyst(boolean analyst) {
        this.analyst = analyst;
    }

    public boolean isInvoiceRecipient() {
        return invoiceRecipient;
    }

    public void setInvoiceRecipient(boolean invoiceRecipient) {
        this.invoiceRecipient = invoiceRecipient;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public String getUserLocale() {
        return userLocale;
    }

    public void setUserLocale(String userLocale) {
        this.userLocale = userLocale;
    }

    public int getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(int dateFormat) {
        this.dateFormat = dateFormat;
    }

    public boolean isAutoRefreshReports() {
        return autoRefreshReports;
    }

    public void setAutoRefreshReports(boolean autoRefreshReports) {
        this.autoRefreshReports = autoRefreshReports;
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
        user.setRefreshReports(isAutoRefreshReports());
        user.setInvoiceRecipient(isInvoiceRecipient());
        user.setTestAccountVisible(isTestAccountVisible());
        user.setAnalyst(isAnalyst());
        user.setCurrency(getCurrency());
        user.setDateFormat(getDateFormat());
        user.setUserLocale(getUserLocale());
        if (getFixedDashboardID() > 0) {
            user.setFixedDashboardID(getFixedDashboardID());
        }
        return user;
    }

    public JSONObject toJSON(ExportMetadata md) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("id", getUserID());
        jo.put("admin", isAccountAdmin());
        jo.put("email", getEmail());
        jo.put("first_name", getFirstName());
        jo.put("title", getTitle());
        jo.put("last_name", getName());
        jo.put("username", getUserName());
        jo.put("designer", isAnalyst());
        jo.put("all_reports", isTestAccountVisible());
        jo.put("invoice_recipient", isInvoiceRecipient());
        jo.put("newsletter", isOptInEmail());
        jo.put("consultant", isConsultant());
        return jo;
    }

    public static UserTransferObject fromJSON(net.minidev.json.JSONObject jo) {
        UserTransferObject user = new UserTransferObject();
        if(jo.containsKey("id"))
            user.setUserID(Long.valueOf(String.valueOf(jo.get("id"))));
        user.setAccountAdmin((Boolean) jo.get("admin"));
        user.setEmail((String) jo.get("email"));
        user.setFirstName((String) jo.get("first_name"));
        user.setTitle((String) jo.get("title"));
        user.setName((String) jo.get("last_name"));
        user.setUserName((String) jo.get("username"));
        user.setAnalyst((Boolean) jo.get("designer"));
        user.setTestAccountVisible((Boolean) jo.get("all_reports"));
        user.setInvoiceRecipient((Boolean) jo.get("invoice_recipient"));
        user.setOptInEmail((Boolean) jo.get("newsletter"));
        //user.setConsultant((Boolean) jo.get("consultant"));
        return user;
    }
}