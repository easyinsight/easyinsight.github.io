package com.easyinsight.groups;

import com.easyinsight.email.UserStub;
import com.easyinsight.datafeeds.FeedConsumer;

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

    public GroupUser(long userID, String userName, String email, String fullName, int role) {
        super(userID, userName, email, fullName, 0);
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
}
