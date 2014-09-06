package com.easyinsight.groups;

import com.easyinsight.email.UserStub;
import com.easyinsight.datafeeds.FeedConsumer;
import com.easyinsight.export.ExportMetadata;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * User: James Boe
 * Date: Oct 13, 2008
 * Time: 3:19:58 PM
 */
public class GroupUser extends UserStub {
    private int role;

    public GroupUser() {
    }

    public int type() {
        return FeedConsumer.GROUP;
    }

    public GroupUser(long userID, String userName, String email, String fullName, int role, String firstName, boolean designer) {
        super(userID, userName, email, fullName, 0, firstName, designer);
        this.role = role;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        return true;
    }

    public JSONObject toJSON(ExportMetadata md) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("id", getUserID());
        jo.put("user_name", getName());
        jo.put("first_name", getFirstName());
        jo.put("last_name", getFullName());
        String role = "";
        switch(getRole()) {
            case 1:
                role = "Owner";
                break;
            case 2:
                role = "Editor";
                break;
            case 3:
                role = "Viewer";
                break;
            default:
                role = "Unknown";
        }
        jo.put("role", role);
        return jo;
    }

    public static GroupUser fromJSON(net.minidev.json.JSONObject a) {
        GroupUser g = new GroupUser();
        g.setUserID(Long.parseLong(String.valueOf(a.get("id"))));
        switch(String.valueOf(a.get("role"))) {
            case "Owner":
                g.setRole(1);
                break;
            case "Editor":
                g.setRole(2);
                break;
            case "Viewer":
                g.setRole(3);
                break;
            default:
                g.setRole(3);
        }
        return g;
    }
}
