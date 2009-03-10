package com.easyinsight.groups;

import com.easyinsight.audit.AuditMessage;

/**
 * User: James Boe
 * Date: Jan 23, 2009
 * Time: 9:15:56 PM
 */
public class GroupComment extends AuditMessage {
    private long groupID;
    private long groupCommentID;
    private String groupName;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public long getGroupCommentID() {
        return groupCommentID;
    }

    public void setGroupCommentID(long groupCommentID) {
        this.groupCommentID = groupCommentID;
    }

    public long getGroupID() {
        return groupID;
    }

    public void setGroupID(long groupID) {
        this.groupID = groupID;
    }
}
