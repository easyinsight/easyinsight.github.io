package com.easyinsight.users;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

/**
 * User: James Boe
 * Date: Jun 24, 2008
 * Time: 12:20:16 PM
 */
public class AccountTransferObject {
    private int accountType;
    private long accountID;
    private int maxUsers;
    private long maxSize;
    private String name;
    private int accountState;
    private boolean apiEnabled;
    private Date creationDate;
    private boolean optInEmail;
    private String googleAppsDomain;
    private int pricingModel;
    private Double nextBillAmount;
    private Date nextBillDate;
    private boolean defaultHTML;

    public boolean isDefaultHTML() {
        return defaultHTML;
    }

    public void setDefaultHTML(boolean defaultHTML) {
        this.defaultHTML = defaultHTML;
    }

    public int getPricingModel() {
        return pricingModel;
    }

    public void setPricingModel(int pricingModel) {
        this.pricingModel = pricingModel;
    }

    public Double getNextBillAmount() {
        return nextBillAmount;
    }

    public void setNextBillAmount(Double nextBillAmount) {
        this.nextBillAmount = nextBillAmount;
    }

    public Date getNextBillDate() {
        return nextBillDate;
    }

    public void setNextBillDate(Date nextBillDate) {
        this.nextBillDate = nextBillDate;
    }

    public String getGoogleAppsDomain() {
        return googleAppsDomain;
    }

    public void setGoogleAppsDomain(String googleAppsDomain) {
        this.googleAppsDomain = googleAppsDomain;
    }

    public String getSnappCloudId() {
        return snappCloudId;
    }

    public void setSnappCloudId(String snappCloudId) {
        this.snappCloudId = snappCloudId;
    }

    private String snappCloudId;

    public boolean isOptInEmail() {
        return optInEmail;
    }

    public void setOptInEmail(boolean optInEmail) {
        this.optInEmail = optInEmail;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isApiEnabled() {
        return apiEnabled;
    }

    public void setApiEnabled(boolean apiEnabled) {
        this.apiEnabled = apiEnabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAccountState() {
        return accountState;
    }

    public void setAccountState(int accountState) {
        this.accountState = accountState;
    }

    public int getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
    }

    public long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public long getAccountID() {
        return accountID;
    }

    public void setAccountID(long accountID) {
        this.accountID = accountID;
    }

    public Account toAccount() {
        Account account = new Account();
        account.setGoogleDomainName(googleAppsDomain);
        account.setMaxUsers(maxUsers);
        account.setMaxSize(maxSize);
        account.setOptInEmail(optInEmail);
        account.setAccountState(accountState);
        account.setAccountType(accountType);
        account.setAccountID(accountID);
        account.setName(name);
        account.setApiEnabled(apiEnabled);
        account.setCreationDate(creationDate);
        account.setSnappCloudId(snappCloudId); // TODO: NUKE THIS
        return account;
    }

    public JSONObject toJSON(ExportMetadata md) throws JSONException {
        DateFormat dateFormat = ExportService.getDateFormatForAccount(AnalysisDateDimension.DAY_LEVEL, null, md.dateFormat);
        JSONObject jo = new JSONObject();
        jo.put("name", getName());
        jo.put("google_domain_name", getGoogleAppsDomain());
        jo.put("max_users", getMaxUsers());
        jo.put("max_size", getMaxSize());
        jo.put("opt_in_email", isOptInEmail());
        jo.put("account_state", getAccountState()); // TODO: BETTER
        switch(getAccountType()) {
            case Account.ENTERPRISE:
                jo.put("account_type", "enterprise");
                break;
            case Account.PREMIUM:
                jo.put("account_type", "premium");
                break;
            case Account.BASIC:
                jo.put("account_type", "basic");
                break;
            case Account.ADMINISTRATOR:
                jo.put("account_type", "administrator");
                break;
            case Account.PLUS:
                jo.put("account_type", "plus");
                break;
            case Account.PROFESSIONAL:
                jo.put("account_type", "professional");
                break;
            default:
                jo.put("account_type", getAccountType());
            break;
        }
        jo.put("pricing_model", getPricingModel());
        jo.put("account_id", getAccountID());
        jo.put("api_enabled", isApiEnabled());
        jo.put("creation_date", dateFormat.format(getCreationDate()));
        if(getNextBillDate() != null) {
            JSONObject bill = new JSONObject();
            jo.put("next_bill", bill);
            bill.put("amount", NumberFormat.getCurrencyInstance(Locale.US).format(getNextBillAmount()));
            bill.put("date", dateFormat.format(getNextBillDate()));
        }
        return jo;
    }

}
