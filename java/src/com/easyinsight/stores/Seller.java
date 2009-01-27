package com.easyinsight.stores;

/**
 * User: James Boe
 * Date: May 27, 2008
 * Time: 11:15:53 PM
 */
public class Seller {
    private Long sellerID;
    private long accountID;
    // merchant account

    public Long getSellerID() {
        return sellerID;
    }

    public void setSellerID(Long sellerID) {
        this.sellerID = sellerID;
    }

    public long getAccountID() {
        return accountID;
    }

    public void setAccountID(long accountID) {
        this.accountID = accountID;
    }
}
