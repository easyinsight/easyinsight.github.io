package com.easyinsight.stores;

import com.easyinsight.users.User;
import com.easyinsight.users.Account;

import javax.persistence.*;

/**
 * User: James Boe
 * Date: May 28, 2008
 * Time: 11:57:13 AM
 */

@Entity
@Table(name = "account_to_merchant")
public class UserMerchantBinding {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="account_to_merchant_id")
    private Long userMerchantBindingID;
    @OneToOne
    @JoinColumn(name="account_id")
    private Account accountID;
    @OneToOne
    @JoinColumn(name="merchant_id")
    private Merchant merchantID;
    @Column(name="binding_type")
    private int bindingType;

    public UserMerchantBinding() {
    }

    public UserMerchantBinding(Account accountID, Merchant merchantID, int bindingType) {
        this.accountID = accountID;
        this.merchantID = merchantID;
        this.bindingType = bindingType;
    }

    public Long getUserMerchantBindingID() {
        return userMerchantBindingID;
    }

    public void setUserMerchantBindingID(Long userMerchantBindingID) {
        this.userMerchantBindingID = userMerchantBindingID;
    }

    public Account getAccountID() {
        return accountID;
    }

    public void setAccountID(Account accountID) {
        this.accountID = accountID;
    }

    public Merchant getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(Merchant merchantID) {
        this.merchantID = merchantID;
    }

    public int getBindingType() {
        return bindingType;
    }

    public void setBindingType(int bindingType) {
        this.bindingType = bindingType;
    }
}
