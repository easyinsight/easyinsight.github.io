package com.easyinsight.groups;

import com.easyinsight.analysis.Tag;

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
    private boolean publiclyVisible;
    private boolean publiclyJoinable;
    private String description;
    private List<Tag> tags;
    private List<GroupUser> groupUsers = new ArrayList<GroupUser>();

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

    public boolean isPubliclyVisible() {
        return publiclyVisible;
    }

    public void setPubliclyVisible(boolean publiclyVisible) {
        this.publiclyVisible = publiclyVisible;
    }

    public boolean isPubliclyJoinable() {
        return publiclyJoinable;
    }

    public void setPubliclyJoinable(boolean publiclyJoinable) {
        this.publiclyJoinable = publiclyJoinable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }    
}
