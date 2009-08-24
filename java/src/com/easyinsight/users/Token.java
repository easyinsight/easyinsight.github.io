package com.easyinsight.users;

/**
 * User: jamesboe
 * Date: Aug 24, 2009
 * Time: 9:18:04 AM
 */
public class Token {
    private long userID;
    private int tokenType;
    private String tokenValue;

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public int getTokenType() {
        return tokenType;
    }

    public void setTokenType(int tokenType) {
        this.tokenType = tokenType;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }
}
