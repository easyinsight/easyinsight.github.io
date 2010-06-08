package com.easyinsight.email;

import com.easyinsight.datafeeds.FeedConsumer;

/**
 * User: James Boe
* Date: Aug 18, 2008
* Time: 11:02:50 AM
*/
public class UserStub extends FeedConsumer {
    private long userID;
    private String fullName;
    private String email;
    private long accountID;
    private String firstName;

    public UserStub() {
    }

    public UserStub(long userID, String userName, String email, String fullName, long accountID,
                    String firstName) {
        super(userName);
        this.userID = userID;
        this.fullName = fullName;
        this.email = email;
        this.accountID = accountID;
        this.firstName = firstName;
    }

    public int type() {
        return FeedConsumer.USER;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public long getAccountID() {
        return accountID;
    }

    public void setAccountID(long accountID) {
        this.accountID = accountID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserStub userStub = (UserStub) o;

        if (userID != userStub.userID) return false;

        return true;
    }

    public int hashCode() {
        return (int) (userID ^ (userID >>> 32));
    }
}
