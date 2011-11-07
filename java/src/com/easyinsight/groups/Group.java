package com.easyinsight.groups;

import java.util.List;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: Aug 27, 2008
 * Time: 3:59:33 PM
 */
public class Group {
    private long groupID;
    private String name;
    private String description;
    private List<GroupUser> groupUsers = new ArrayList<GroupUser>();
    private String urlKey;

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public List<GroupUser> getGroupUsers() {
        return groupUsers;
    }

    public void setGroupUsers(List<GroupUser> groupUsers) {
        this.groupUsers = groupUsers;
    }

    public long getGroupID() {
        return groupID;
    }

    public void setGroupID(long groupID) {
        this.groupID = groupID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
