package com.easyinsight.groups;

/**
 * User: James Boe
 * Date: Aug 27, 2008
 * Time: 4:12:16 PM
 */
public class GroupToUserBinding {

    public static final int OWNER = 1;
    public static final int EDITOR = 2;
    public static final int VIEWER = 3;    

    private long groupToUserBindingID;
    private Group group;
    private long user;
    private int bindingType;

    public long getGroupToUserBindingID() {
        return groupToUserBindingID;
    }

    public void setGroupToUserBindingID(long groupToUserBindingID) {
        this.groupToUserBindingID = groupToUserBindingID;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public int getBindingType() {
        return bindingType;
    }

    public void setBindingType(int bindingType) {
        this.bindingType = bindingType;
    }
}
