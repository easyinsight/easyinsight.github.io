package com.easyinsight.export;

import com.easyinsight.database.EIConnection;
import com.easyinsight.email.UserStub;
import com.easyinsight.groups.GroupDescriptor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: Jun 6, 2010
 * Time: 1:55:39 PM
 */
public abstract class ScheduledDelivery extends ScheduledActivity {

    private List<UserStub> users = new ArrayList<UserStub>();
    private List<GroupDescriptor> groups = new ArrayList<GroupDescriptor>();
    private List<String> emails = new ArrayList<String>();

    public List<UserStub> getUsers() {
        return users;
    }

    public void setUsers(List<UserStub> users) {
        this.users = users;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public List<GroupDescriptor> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupDescriptor> groups) {
        this.groups = groups;
    }

    protected void customSave(EIConnection conn, int utcOffset) throws SQLException {
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM delivery_to_user WHERE SCHEDULED_ACCOUNT_ACTIVITY_ID = ?");
        clearStmt.setLong(1, getScheduledActivityID());
        clearStmt.executeUpdate();
        clearStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO delivery_to_user (user_id, SCHEDULED_ACCOUNT_ACTIVITY_ID) VALUES (?, ?)");
        for (UserStub userStub : users) {
            insertStmt.setLong(1, userStub.getUserID());
            insertStmt.setLong(2, getScheduledActivityID());
            insertStmt.execute();
        }
        insertStmt.close();

        PreparedStatement clearGroupStmt = conn.prepareStatement("DELETE FROM delivery_to_group WHERE SCHEDULED_ACCOUNT_ACTIVITY_ID = ?");
        clearGroupStmt.setLong(1, getScheduledActivityID());
        clearGroupStmt.executeUpdate();
        clearGroupStmt.close();
        PreparedStatement insertGroupStmt = conn.prepareStatement("INSERT INTO delivery_to_group (group_id, SCHEDULED_ACCOUNT_ACTIVITY_ID) VALUES (?, ?)");
        for (GroupDescriptor group : groups) {
            insertGroupStmt.setLong(1, group.getGroupID());
            insertGroupStmt.setLong(2, getScheduledActivityID());
            insertGroupStmt.execute();
        }
        insertGroupStmt.close();

        PreparedStatement clearEmailStmt = conn.prepareStatement("DELETE FROM delivery_to_email WHERE SCHEDULED_ACCOUNT_ACTIVITY_ID = ?");
        clearEmailStmt.setLong(1, getScheduledActivityID());
        clearEmailStmt.executeUpdate();
        clearEmailStmt.close();
        PreparedStatement insertEmailStmt = conn.prepareStatement("INSERT INTO delivery_to_email (email_address, SCHEDULED_ACCOUNT_ACTIVITY_ID) VALUES (?, ?)");
        for (String email : emails) {
            insertEmailStmt.setString(1, email);
            insertEmailStmt.setLong(2, getScheduledActivityID());
            insertEmailStmt.execute();
        }
        insertEmailStmt.close();
    }

    protected void customLoad(EIConnection conn) throws SQLException {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT USER.user_id, USER.username, USER.email, USER.name, USER.account_id, USER.first_name FROM " +
                "delivery_to_user, user WHERE " +
                "SCHEDULED_ACCOUNT_ACTIVITY_ID = ? AND delivery_to_user.user_id = USER.user_id");
        queryStmt.setLong(1, getScheduledActivityID());
        ResultSet rs = queryStmt.executeQuery();

        List<UserStub> users = new ArrayList<UserStub>();
        while (rs.next()) {
            UserStub userStub = new UserStub(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getLong(5), rs.getString(6));
            users.add(userStub);
        }
        queryStmt.close();

        PreparedStatement groupStmt = conn.prepareStatement("SELECT COMMUNITY_GROUP_ID, NAME FROM COMMUNITY_GROUP, DELIVERY_TO_GROUP WHERE " +
                "DELIVERY_TO_GROUP.GROUP_ID = COMMUNITY_GROUP.COMMUNITY_GROUP_ID AND DELIVERY_TO_GROUP.SCHEDULED_ACCOUNT_ACTIVITY_id = ?");
        groupStmt.setLong(1, getScheduledActivityID());
        ResultSet groupRS = groupStmt.executeQuery();
        List<GroupDescriptor> groups = new ArrayList<GroupDescriptor>();
        while (groupRS.next()) {
            GroupDescriptor groupDescriptor = new GroupDescriptor(groupRS.getString(2), groupRS.getLong(1), 0, null);
            groups.add(groupDescriptor);
        }
        groupStmt.close();

        PreparedStatement queryEmailStmt = conn.prepareStatement("SELECT EMAIL_ADDRESS FROM delivery_to_email where SCHEDULED_ACCOUNT_ACTIVITY_ID = ?");
        queryEmailStmt.setLong(1, getScheduledActivityID());
        List<String> emails = new ArrayList<String>();
        ResultSet emailRS = queryEmailStmt.executeQuery();
        while (emailRS.next()) {
            String email = emailRS.getString(1);
            emails.add(email);
        }
        queryEmailStmt.close();
        setUsers(users);
        setEmails(emails);
        setGroups(groups);
    }
}
