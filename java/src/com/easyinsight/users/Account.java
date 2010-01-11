package com.easyinsight.users;

import com.easyinsight.billing.BrainTreeBillingSystem;
import com.easyinsight.config.ConfigLoader;
import com.easyinsight.logging.LogClass;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Date;

/**
 * User: James Boe
 * Date: Jun 23, 2008
 * Time: 6:48:32 PM
 */
@Entity
@Table(name="account")
public class Account {

    public static final int PERSONAL = 1;
    public static final int BASIC = 2;
    public static final int PROFESSIONAL = 3;
    public static final int PREMIUM = 4;
    public static final int ENTERPRISE = 5;
    public static final int ADMINISTRATOR = 6;

    public static final int INACTIVE = 1;
    public static final int ACTIVE = 2;
    public static final int DELINQUENT = 3;
    public static final int SUSPENDED = 4;
    public static final int CLOSED = 5;
    public static final int PENDING_BILLING = 6;
    public static final int PREPARING = 7;
    public static final int BETA = 8;
    public static final int TRIAL = 9;
    public static final int CLOSING = 10;

    public static final long FREE_MAX = 1000000;
    public static final long INDIVIDUAL_MAX = 100000000;
    public static final long GROUP_MAX = 500000000;
    public static final long PROFESSIONAL_MAX = 10000000000L;
    public static final long ENTERPRISE_MAX = 1000000000;
    public static final long ADMINISTRATOR_MAX = Long.MAX_VALUE;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="account_id")
    private long accountID;

    @JoinColumn(name="account_id")
    @OneToMany (cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<User>();

    @OneToMany (cascade = CascadeType.ALL)
    @JoinColumn(name="account_id")
    private List<SubscriptionLicense> licenses = new ArrayList<SubscriptionLicense>();

    @Column(name="activated")
    private boolean activated;

    @Column(name="group_id")
    private Long groupID;

    @Column(name="account_type")
    private int accountType;

    @Column(name="max_size")
    private long maxSize;

    @Column(name="max_users")
    private int maxUsers;

    @Column(name="api_enabled")
    private boolean apiEnabled;

    @Column(name="name")
    private String name;

    @Column(name="account_state")
    private int accountState;

    @Column(name="account_key")
    private String accountKey;

    @Column(name="account_secret_key")
    private String accountSecretKey;

    @Column(name="billing_information_given")
    private Boolean billingInformationGiven;

    @Column(name="creation_date")
    private Date creationDate;

    @Column(name="billing_day_of_month")
    private Integer billingDayOfMonth;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="account_to_guest_user",
        joinColumns = @JoinColumn(name="account_id", nullable = false),
        inverseJoinColumns = @JoinColumn(name="guest_user_id", nullable = false))
    private List<Consultant> guestUsers = new ArrayList<Consultant>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="account_id")
    private List<BandwidthUsage> historicBandwidthUsage = new ArrayList<BandwidthUsage>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="account_id")
    private List<AccountCreditCardBillingInfo> billingInfo = new ArrayList<AccountCreditCardBillingInfo>();

    @Column(name="opt_in_email")
    private boolean optInEmail;

    private static final double GROUP_BILLING_AMOUNT = 200.00;
    private static final double INDIVIDUAL_BILLING_AMOUNT = 25.00;

    //private BillingParty billingParty;

    public List<AccountCreditCardBillingInfo> getBillingInfo() {
        return billingInfo;
    }

    public void setBillingInfo(List<AccountCreditCardBillingInfo> billingInfo) {
        this.billingInfo = billingInfo;
    }

    public boolean isApiEnabled() {
        return apiEnabled;
    }

    public void setApiEnabled(boolean apiEnabled) {
        this.apiEnabled = apiEnabled;
    }

    /*public BillingParty getBillingParty() {
        return billingParty;
    }

    public void setBillingParty(BillingParty billingParty) {
        this.billingParty = billingParty;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<SubscriptionLicense> getLicenses() {
        return licenses;
    }

    public void setLicenses(List<SubscriptionLicense> licenses) {
        this.licenses = licenses;
    }

    public void addLicense(SubscriptionLicense subscriptionLicense) {
        this.licenses.add(subscriptionLicense);
    }

    public boolean isOptInEmail() {
        return optInEmail;
    }

    public void setOptInEmail(boolean optInEmail) {
        this.optInEmail = optInEmail;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public String getAccountSecretKey() {
        return accountSecretKey;
    }

    public void setAccountSecretKey(String accountSecretKey) {
        this.accountSecretKey = accountSecretKey;
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void removeUser(User user) {
        this.users.remove(user);
    }

    public int getAccountState() {
        return accountState;
    }

    public void setAccountState(int accountState) {
        this.accountState = accountState;
    }

    public AccountTransferObject toTransferObject() {
        AccountTransferObject transfer = new AccountTransferObject();
        transfer.setAccountID(accountID);
        List<SubscriptionLicense> subscriptionList = new ArrayList<SubscriptionLicense>(licenses);
        transfer.setLicenses(subscriptionList);
        //transfer.setUsers(users);
        transfer.setAccountType(accountType);
        transfer.setMaxSize(maxSize);
        transfer.setCreationDate(creationDate);
        transfer.setName(name);
        transfer.setMaxUsers(maxUsers);
        transfer.setAccountState(accountState);
        transfer.setApiEnabled(apiEnabled);
        return transfer;
    }

    public AccountAdminTO toAdminTO() {
        AccountAdminTO transfer = new AccountAdminTO();
        transfer.setAccountID(accountID);
        List<SubscriptionLicense> subscriptionList = new ArrayList<SubscriptionLicense>(licenses);
        transfer.setLicenses(subscriptionList);
        //transfer.setUsers(users);
        transfer.setAccountType(accountType);
        transfer.setMaxSize(maxSize);
        transfer.setName(name);
        transfer.setMaxUsers(maxUsers);
        if (groupID != null) {
            transfer.setGroupID(groupID);
        }
        long latestLoginDate = 0;
        List<UserTransferObject> adminUsers = new ArrayList<UserTransferObject>();
        for (User user : getUsers()) {
            if (user.isAccountAdmin()) {
                adminUsers.add(user.toUserTransferObject());
            }
            if (user.getLastLoginDate() != null) {
                if (user.getLastLoginDate().getTime() > latestLoginDate) {
                    latestLoginDate = user.getLastLoginDate().getTime();
                }
            }
        }
        List<ConsultantTO> consultants = new ArrayList<ConsultantTO>();
        for (Consultant consultant : getGuestUsers()) {
            consultants.add(consultant.toConsultantTO());
        }
        transfer.setCreationDate(getCreationDate());
        transfer.setLastUserLoginDate(new Date(latestLoginDate));
        transfer.setConsultants(consultants);
        transfer.setAdminUsers(adminUsers);
        transfer.setAccountState(accountState);
        transfer.setApiEnabled(apiEnabled);
        return transfer;
    }
    
    public long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }

    public int getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
    }

    public List<Consultant> getGuestUsers() {
        return guestUsers;
    }

    public void setGuestUsers(List<Consultant> guestUsers) {
        this.guestUsers = guestUsers;
    }

    public Long getGroupID() {
        return groupID;
    }

    public void setGroupID(Long groupID) {
        this.groupID = groupID;
    }

    public static long getMaxCount(int tier) {
        switch(tier) {
            case Account.PERSONAL:
                return FREE_MAX;
            case Account.BASIC:
                return INDIVIDUAL_MAX;
            case Account.PROFESSIONAL:
                return PROFESSIONAL_MAX;
            case Account.PREMIUM:
                return PROFESSIONAL_MAX;
            case Account.ENTERPRISE:
                return ENTERPRISE_MAX;
            case Account.ADMINISTRATOR:
                return ADMINISTRATOR_MAX;
            default:
                throw new RuntimeException("Unknown account type " + tier);
        }
    }

    public Boolean isBillingInformationGiven() {
        return billingInformationGiven;
    }

    public void setBillingInformationGiven(Boolean billingInformationGiven) {
        this.billingInformationGiven = billingInformationGiven;
    }

    public Integer getBillingDayOfMonth() {
        return billingDayOfMonth;
    }

    public void setBillingDayOfMonth(Integer billingDayOfMonth) {
        this.billingDayOfMonth = billingDayOfMonth;
    }

    public AccountCreditCardBillingInfo bill() {

        LogClass.info("Starting billing for account ID:" + this.getAccountID());
        if(getAccountType() == Account.PERSONAL)
            setAccountState(Account.ACTIVE);
        // the indirection here is to support invoice billingSystem later
        BrainTreeBillingSystem billingSystem = new BrainTreeBillingSystem();
//        billingSystem.setUsername("testapi");
//        billingSystem.setPassword("password1");
        billingSystem.setUsername(ConfigLoader.instance().getBillingUsername());
        billingSystem.setPassword(ConfigLoader.instance().getBillingPassword());
        Map<String, String> params = billingSystem.billAccount(this.getAccountID(), this.monthlyCharge());
        if(!params.get("response").equals("1")) {
            setAccountState(Account.DELINQUENT);
            LogClass.info("Billing failed!");
        }
        else
            LogClass.info("Success!");
        AccountCreditCardBillingInfo info = new AccountCreditCardBillingInfo();
        info.setAmount(String.valueOf(monthlyCharge()));
        info.setAccountId(this.getAccountID());
        info.setResponse(params.get("response"));
        info.setResponseCode(params.get("response_code"));
        info.setResponseString(params.get("responsetext"));
        info.setTransactionID(params.get("transactionid"));
        info.setTransactionTime(new Date());
        LogClass.info("Completed billing Account ID:" + this.getAccountID());
        return info;
    }

    public double monthlyCharge() {
        switch(getAccountType()) {
            case Account.BASIC:
                return INDIVIDUAL_BILLING_AMOUNT;
            case Account.PROFESSIONAL:
                return GROUP_BILLING_AMOUNT;
            default:
                throw new RuntimeException("Only doing credit card billing for Individual and Group accounts at the moment.");
        }
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}
