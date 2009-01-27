package com.easyinsight.stores;

import com.easyinsight.datafeeds.FeedStorage;
//import com.easyinsight.userupload.CommercialUploadPolicy;

import javax.persistence.*;
import java.util.Date;

/**
 * User: James Boe
 * Date: May 28, 2008
 * Time: 11:30:45 AM
 */
@Entity
@Table(name="payment")
public class Payment {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column (name="payment_id")
    private Long paymentID;
    @Column (name="paid_amount")
    private double paidAmount;
    @Column (name="payment_date")
    private Date paymentDate;
    @Column (name="purchase_id")
    private Long purchaseID;

    public Payment() { }

    public Payment(Purchase purchase) {
        this.purchaseID = purchase.getPurchaseID();
        //CommercialUploadPolicy policy = (CommercialUploadPolicy) new FeedStorage().getFeedDefinitionData(purchase.getFeedID()).
        //        getUploadPolicy();
        //paidAmount = policy.getPrice().getCost();
        paymentDate = new Date();
    }

    public Long getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(Long paymentID) {
        this.paymentID = paymentID;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Long getPurchaseID() {
        return purchaseID;
    }

    public void setPurchaseID(Long purchaseID) {
        this.purchaseID = purchaseID;
    }
}
