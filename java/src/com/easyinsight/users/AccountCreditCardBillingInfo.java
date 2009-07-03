package com.easyinsight.users;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 30, 2009
 * Time: 5:56:04 PM
 */
@Entity
@Table(name="account_credit_card_billing_info")
public class AccountCreditCardBillingInfo {

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="account_credit_card_billing_info_id")
    private long id;

    @Column(name="transaction_id")
    private String transactionID;

    @Column(name="amount")
    private String amount;

    @Column(name="response")
    private String response;

    @Column(name="transaction_time")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date transactionTime;

    @Column(name="response_string")
    private String responseString;

    @Column(name="response_code")
    private String responseCode;

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    @Column(name="account_id")
    private long accountId;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Date getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Date transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }
}
