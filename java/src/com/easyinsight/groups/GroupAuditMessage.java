package com.easyinsight.groups;

import com.easyinsight.audit.AuditMessage;

import java.util.Date;

/**
 * User: James Boe
 * Date: Jan 23, 2009
 * Time: 9:25:50 PM
 */
public class GroupAuditMessage extends AuditMessage {
    private long groupID;
    private String groupName;

    public GroupAuditMessage(long userID, Date timestamp, String message, long groupID, String groupName) {
        super(userID, timestamp, message);
        this.groupID = groupID;
        this.groupName = groupName;
    }

    public GroupAuditMessage() {
    }

    public long getGroupID() {
        return groupID;
    }

    public void setGroupID(long groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
