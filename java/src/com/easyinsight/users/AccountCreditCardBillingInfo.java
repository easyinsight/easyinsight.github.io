package com.easyinsight.users;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    public static final String INVOICE_EMAIL = "Thank you for using Easy Insight.\r\n" +
            "This is an invoice for your Easy Insight account.\r\n" +
            "SUMMARY\r\nYour credit card has been automatically charged {0} to cover your subscription to Easy Insight.\r\n\r\n" +
            "NEED TO CANCEL?\r\n" +
            "Log into your Easy Insight account, go to the Accounts page, and click Cancel Account. " +
            "Once you cancel you won''t be charged again, but you are responsible for charges already incurred.\r\n\r\n===============================================\r\nINVOICE\r\n{1}         Transaction ID:{2}\r\n..............................\r\nEasy Insight LLC\r\n1401 Wewatta St Unit 606\r\nDenver, CO\r\n\r\nPlease email support@easy-insight.com with any questions or concerns.";

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

    public String toInvoiceText() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        String bill = currencyFormat.format(Double.parseDouble(amount));
        DateFormat df = SimpleDateFormat.getInstance();
        String date = df.format(getTransactionTime());
        return MessageFormat.format(INVOICE_EMAIL, bill, date, String.valueOf(transactionID));
    }
}
