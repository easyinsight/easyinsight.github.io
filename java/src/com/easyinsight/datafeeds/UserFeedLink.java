package com.easyinsight.datafeeds;

/**
 * User: James Boe
 * Date: Jun 2, 2008
 * Time: 9:36:11 PM
 */
public class UserFeedLink {
    private long userID;
    private String userName;
    private int role;

    public UserFeedLink() {
    }

    public UserFeedLink(long userID, String userName, int role) {
        this.userID = userID;
        this.userName = userName;
        this.role = role;
    }
    
    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
