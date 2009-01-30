package com.easyinsight.users;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: Jun 23, 2008
 * Time: 6:48:32 PM
 */
@Entity
@Table(name="account")
public class Account {

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

    @Column(name="account_type")
    private int accountType;

    @Column(name="max_size")
    private long maxSize;

    @Column(name="max_users")
    private int maxUsers;

    @Column(name="unchecked_api_enabled")
    private boolean uncheckedAPIEnabled;

    @Column(name="validated_api_enabled")
    private boolean validatedAPIEnabled;

    @Column(name="unchecked_api_allowed")
    private boolean uncheckedAPIAllowed;

    @Column(name="validated_api_allowed")
    private boolean validatedAPIAllowed;

    @Column(name="dynamic_api_enabled")
    private boolean dynamicAPIAllowed;

    @Column(name="basic_auth_allowed")
    private boolean basicAuthAllowed;

    @Column(name="name")
    private String name;

    //private BillingParty billingParty;

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

    public List<SubscriptionLicense> getLicenses() {
        return licenses;
    }

    public void setLicenses(List<SubscriptionLicense> licenses) {
        this.licenses = licenses;
    }

    public void addLicense(SubscriptionLicense subscriptionLicense) {
        this.licenses.add(subscriptionLicense);
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

    public AccountTransferObject toTransferObject() {
        AccountTransferObject transfer = new AccountTransferObject();
        transfer.setAccountID(accountID);
        List<SubscriptionLicense> subscriptionList = new ArrayList<SubscriptionLicense>(licenses);
        transfer.setLicenses(subscriptionList);
        List<User> users = new ArrayList<User>();
        for (User user : this.users) {
            List<SubscriptionLicense> userLicenses = new ArrayList<SubscriptionLicense>(user.getLicenses());
            user.setLicenses(userLicenses);
            user.setAccountID(null);
            users.add(user);
        }
        //transfer.setUsers(users);
        transfer.setAccountType(accountTypeObject());
        return transfer;
    }

    public AccountType accountTypeObject() {
        AccountType accountTypeObject;
        switch (accountType) {
            case AccountType.FREE:
                accountTypeObject = new FreeAccount();
                break;
            case AccountType.INDIVIDUAL:
                accountTypeObject = new IndividualAccount();
                break;
            case AccountType.COMMERCIAL:
                accountTypeObject = new CommercialAccount();
                break;
            default:
                throw new RuntimeException("Unknown account type " + accountType);
        }
        return accountTypeObject;
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

    public boolean isUncheckedAPIEnabled() {
        return uncheckedAPIEnabled;
    }

    public void setUncheckedAPIEnabled(boolean uncheckedAPIEnabled) {
        this.uncheckedAPIEnabled = uncheckedAPIEnabled;
    }

    public boolean isValidatedAPIEnabled() {
        return validatedAPIEnabled;
    }

    public void setValidatedAPIEnabled(boolean validatedAPIEnabled) {
        this.validatedAPIEnabled = validatedAPIEnabled;
    }

    public boolean isUncheckedAPIAllowed() {
        return uncheckedAPIAllowed;
    }

    public void setUncheckedAPIAllowed(boolean uncheckedAPIAllowed) {
        this.uncheckedAPIAllowed = uncheckedAPIAllowed;
    }

    public boolean isValidatedAPIAllowed() {
        return validatedAPIAllowed;
    }

    public void setValidatedAPIAllowed(boolean validatedAPIAllowed) {
        this.validatedAPIAllowed = validatedAPIAllowed;
    }

    public boolean isDynamicAPIAllowed() {
        return dynamicAPIAllowed;
    }

    public void setDynamicAPIAllowed(boolean dynamicAPIAllowed) {
        this.dynamicAPIAllowed = dynamicAPIAllowed;
    }

    public boolean isBasicAuthAllowed() {
        return basicAuthAllowed;
    }

    public void setBasicAuthAllowed(boolean basicAuthAllowed) {
        this.basicAuthAllowed = basicAuthAllowed;
    }
}
