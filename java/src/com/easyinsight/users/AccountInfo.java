package com.easyinsight.users;

import java.util.Date;

/**
 * User: James Boe
 * Date: Apr 28, 2009
 * Time: 9:44:02 AM
 */
public class AccountInfo {
    private int accountState;
    private int accountType;
    private Date createdDate;
    private Date trialEndDate;
    private Date lastBillingPaidDate;

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public int getAccountState() {
        return accountState;
    }

    public void setAccountState(int accountState) {
        this.accountState = accountState;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getTrialEndDate() {
        return trialEndDate;
    }

    public void setTrialEndDate(Date trialEndDate) {
        this.trialEndDate = trialEndDate;
    }

    public Date getLastBillingPaidDate() {
        return lastBillingPaidDate;
    }

    public void setLastBillingPaidDate(Date lastBillingPaidDate) {
        this.lastBillingPaidDate = lastBillingPaidDate;
    }
}
