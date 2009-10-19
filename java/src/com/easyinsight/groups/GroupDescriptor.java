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

    public int type() {
        return FeedConsumer.GROUP;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        GroupDescriptor that = (GroupDescriptor) o;

        if (groupID != that.groupID) return false;
        if (groupMembers != that.groupMembers) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (groupID ^ (groupID >>> 32));
        result = 31 * result + groupMembers;
        return result;
    }
}
