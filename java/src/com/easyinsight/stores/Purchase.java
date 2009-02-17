package com.easyinsight.stores;

import com.easyinsight.users.User;

import javax.persistence.*;
import java.util.Date;

/**
 * User: James Boe
 * Date: May 27, 2008
 * Time: 11:13:34 PM
 */
@Entity
@Table(name="purchase")
public class Purchase {

    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column (name="purchase_id")
    private Long purchaseID;
    @Column (name="feed_id")
    private long feedID;

    @Column (name="buyer_account_id")
    private long buyerID;

    @Column (name="merchant_id")
    private long merchantID;

    @Column (name="purchase_date")
    private Date purchaseDate;

    public Purchase() {
    }

    public Purchase(long feedID, User buyer, Merchant merchant, Date purchaseDate) {
        this.feedID = feedID;
        //this.buyerID = buyer.getAccountID().getAccountID();
        this.merchantID = merchant.getMerchantID();
        this.purchaseDate = purchaseDate;
    }

    public Long getPurchaseID() {
        return purchaseID;
    }

    public void setPurchaseID(Long purchaseID) {
        this.purchaseID = purchaseID;
    }

    public long getFeedID() {
        return feedID;
    }

    public void setFeedID(long feedID) {
        this.feedID = feedID;
    }

    public long getBuyerID() {
        return buyerID;
    }

    public void setBuyerID(long buyerID) {
        this.buyerID = buyerID;
    }

    public long getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(long merchantID) {
        this.merchantID = merchantID;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
