package com.easyinsight.export;

import com.easyinsight.database.EIConnection;
import com.easyinsight.email.UserStub;

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

    public List<UserStub> getUsers() {
        return users;
    }

    public void setUsers(List<UserStub> users) {
        this.users = users;
    }

    protected void customSave(EIConnection conn) throws SQLException {
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM delivery_to_user WHERE SCHEDULED_ACCOUNT_ACTIVITY_ID = ?");
        clearStmt.setLong(1, getScheduledActivityID());
        clearStmt.executeUpdate();
        clearStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO delivery_to_user (user_id, SCHEDULED_ACCOUNT_ACTIVITY_ID) VALUES (?, ?)");
        for (UserStub userStub : users) {
            insertStmt.setLong(1, userStub.getUserID());
            insertStmt.setLong(2, getScheduledActivityID());
            insertStmt.execute();
            insertStmt.close();
        }
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
        setUsers(users);
    }
}
