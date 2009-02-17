package com.easyinsight.users;

/**
 * User: jboe
 * Date: Jan 2, 2008
 * Time: 5:35:16 PM
 */
public class UserServiceResponse {
    private boolean successful;
    private String failureMessage;
    private long userID;
    private long accountID;
    private String userName;
    private String encryptedPassword;
    private String name;
    private int accountType;
    private long spaceAllowed;
    private String email;
    private boolean accountAdmin;
    private boolean dataSourceCreator;
    private boolean insightCreator;

    public UserServiceResponse(boolean successful, String failureMessage) {
        this.successful = successful;
        this.failureMessage = failureMessage;
    }

    public UserServiceResponse(boolean successful, long userID, long accountID, String name, int accountType,
                               long spaceAllowed, String email, String userName, String encryptedPassword, boolean accountAdmin,
                               boolean dataSourceCreator, boolean insightCreator) {
        this.successful = successful;
        this.userID = userID;
        this.accountID = accountID;
        this.name = name;
        this.accountType = accountType;
        this.spaceAllowed = spaceAllowed;
        this.email = email;
        this.userName = userName;
        this.encryptedPassword = encryptedPassword;
        this.accountAdmin = accountAdmin;
        this.dataSourceCreator = dataSourceCreator;
        this.insightCreator = insightCreator;
    }

    public UserServiceResponse() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
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
}
