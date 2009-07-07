package com.easyinsight.users;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jul 6, 2009
 * Time: 2:19:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class UpgradeAccountResponse {
    private UserTransferObject user;
    private boolean billingInformationNeeded;

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
