package com.easyinsight.datafeeds.highrise;

/**
 * User: jamesboe
 * Date: Nov 16, 2010
 * Time: 9:29:46 AM
 */
public class HighriseAdditionalToken {
    private long userID;
    private String token;

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
