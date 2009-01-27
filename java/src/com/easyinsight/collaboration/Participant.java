package com.easyinsight.collaboration;

/**
 * User: James Boe
 * Date: May 29, 2008
 * Time: 2:32:08 PM
 */
public class Participant {
    private String name;
    private long accountID;
    private int role;

    public Participant() {
    }

    public Participant(long accountID, int role, String name) {
        this.accountID = accountID;
        this.role = role;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAccountID() {
        return accountID;
    }

    public void setAccountID(long accountID) {
        this.accountID = accountID;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
