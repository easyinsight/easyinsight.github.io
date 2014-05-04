package com.easyinsight.users;

import java.util.Date;

/**
 * User: jamesboe
 * Date: Oct 13, 2010
 * Time: 10:49:27 AM
 */
public class InvoiceInfo {
    

    private Date date;
    private String invoiceText;
    private long invoiceID;

    public InvoiceInfo(Date date, String invoiceText, long invoiceID) {
        this.date = date;
        this.invoiceText = invoiceText;
        this.invoiceID = invoiceID;
    }

    public InvoiceInfo() {

    }

    public long getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(long invoiceID) {
        this.invoiceID = invoiceID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getInvoiceText() {
        return invoiceText;
    }

    public void setInvoiceText(String invoiceText) {
        this.invoiceText = invoiceText;
    }
}
