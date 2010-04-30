package com.easyinsight.users;

/**
 * User: abaldwin
 * Date: Jul 6, 2009
 * Time: 2:19:34 PM
 */
public class UpgradeAccountResponse {
    private UserTransferObject user;
    private boolean successful;
    private String resultMessage;
    private boolean billingInformationNeeded;
    private int newAccountType;

    public int getNewAccountType() {
        return newAccountType;
    }

    public void setNewAccountType(int newAccountType) {
        this.newAccountType = newAccountType;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public boolean isBillingInformationNeeded() {
        return billingInformationNeeded;
    }

    public void setBillingInformationNeeded(boolean billingInformationNeeded) {
        this.billingInformationNeeded = billingInformationNeeded;
    }

    public UserTransferObject getUser() {
        return user;
    }

    public void setUser(UserTransferObject user) {
        this.user = user;
    }
}
