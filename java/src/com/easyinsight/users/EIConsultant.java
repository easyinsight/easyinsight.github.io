package com.easyinsight.users;

/**
 * User: James Boe
 * Date: Mar 4, 2009
 * Time: 3:13:53 PM
 */
public class EIConsultant extends ConsultantTO {
    private String accountName;
    private long accountID;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public long getAccountID() {
        return accountID;
    }

    public void setAccountID(long accountID) {
        this.accountID = accountID;
    }
}
