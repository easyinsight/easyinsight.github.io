package com.easyinsight.groups;

/**
 * User: James Boe
 * Date: Mar 26, 2009
 * Time: 6:05:24 PM
 */
public class GroupResponse {

    public static final int SUCCESS = 1;
    public static final int NEED_LOGIN = 2;
    public static final int REJECTED = 3;

    private int status;
    private long groupID;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getGroupID() {
        return groupID;
    }

    public void setGroupID(long groupID) {
        this.groupID = groupID;
    }

    public GroupResponse(int status, long groupID) {
        this.status = status;
        this.groupID = groupID;
    }

    public GroupResponse() {

    }
}
