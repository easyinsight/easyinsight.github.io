package com.easyinsight.email;

import com.easyinsight.datafeeds.FeedConsumer;
import com.easyinsight.export.ExportMetadata;
import org.json.JSONException;
import org.json.JSONObject;

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
    private String userKey;
    private boolean designer;

    public UserStub() {
    }

    public UserStub(long userID, String userName, String email, String fullName, long accountID,
                    String firstName, boolean designer) {
        super(userName);
        this.userID = userID;
        this.fullName = fullName;
        this.email = email;
        this.accountID = accountID;
        this.firstName = firstName;
        this.designer = designer;
    }

    /*
        pubObject alic function get displayName():String {
            return ;
        }
     */

    public String displayName() {
        String name = firstName != null ? (firstName + " " + fullName) : fullName;
        return name + " ( " + email + " )";
    }

    public int type() {
        return FeedConsumer.USER;
    }

    public boolean isDesigner() {
        return designer;
    }

    public void setDesigner(boolean designer) {
        this.designer = designer;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
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

    public JSONObject toJSON(ExportMetadata md) throws JSONException {
        JSONObject jo = super.toJSON(md);
        jo.put("user_id", getUserID());
        jo.put("last_name", getFullName());
        jo.put("email", getEmail());
        jo.put("account_id", getAccountID());
        jo.put("first_name", getFirstName());
        jo.put("user_key", getUserKey());
        jo.put("designer", isDesigner());
        return jo;
    }

    public UserStub(net.minidev.json.JSONObject jsonObject) {
        super(jsonObject);
        if(jsonObject.containsKey("user_id"))
            setUserID(Long.parseLong(String.valueOf(jsonObject.get("user_id"))));
        else if(jsonObject.containsKey("id"))
            setUserID(Long.parseLong(String.valueOf(jsonObject.get("id"))));
        setFullName(String.valueOf(jsonObject.get("last_name")));
        setEmail(String.valueOf(jsonObject.get("email")));
        if(jsonObject.containsKey("account_id"))
            setAccountID(Long.valueOf(String.valueOf(jsonObject.get("account_id"))));
        setFirstName(String.valueOf(jsonObject.get("first_name")));
        setUserKey(String.valueOf(jsonObject.get("user_key")));
        if(jsonObject.containsKey("designer"))
            setDesigner(Boolean.valueOf(String.valueOf(jsonObject.get("designer"))));
    }
}
