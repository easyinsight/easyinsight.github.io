package com.easyinsight.groups;

import com.easyinsight.datafeeds.FeedConsumer;

/**
 * User: James Boe
 * Date: Sep 9, 2008
 * Time: 2:21:59 PM
 */
public class GroupDescriptor extends FeedConsumer {
    private long groupID;
    private int groupMembers;
    private String description;

    public GroupDescriptor() {
    }

    public GroupDescriptor(String groupName, long groupID, int groupMembers, String description) {
        super(groupName);
        this.groupID = groupID;
        this.groupMembers = groupMembers;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(int groupMembers) {
        this.groupMembers = groupMembers;
    }

    public long getGroupID() {
        return groupID;
    }

    public void setGroupID(long groupID) {
        this.groupID = groupID;
    }
}
