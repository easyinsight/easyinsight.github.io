package com.easyinsight.salesautomation;

import java.util.Date;

/**
* User: jamesboe
* Date: 6/27/14
* Time: 1:42 PM
*/
public class SalesAccount {
    private String email;
    private String firstName;
    private String lastName;
    private long accountID;
    private Date creationDate;

    public SalesAccount(String email, String firstName, String lastName, long accountID, Date creationDate) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountID = accountID;
        this.creationDate = creationDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getAccountID() {
        return accountID;
    }

    public void setAccountID(long accountID) {
        this.accountID = accountID;
    }
}
