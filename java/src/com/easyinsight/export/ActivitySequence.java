package com.easyinsight.export;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.users.ActivitySequenceTask;
import com.easyinsight.users.ActivitySequenceTaskGenerator;
import org.hibernate.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: jamesboe
 * Date: 1/7/15
 * Time: 1:55 PM
 */
public class ActivitySequence extends ScheduledActivity {

    private List<ScheduledActivity> activities;

    public List<ScheduledActivity> getActivities() {
        return activities;
    }

    public void setActivities(List<ScheduledActivity> activities) {
        this.activities = activities;
    }

    @Override
    public int retrieveType() {
        return SEQUENCE;
    }

    @Override
    public void setup(EIConnection conn) throws SQLException {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT TASK_GENERATOR_ID FROM activity_sequence_task_generator where scheduled_activity_id = ?");
        queryStmt.setLong(1, getScheduledActivityID());
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM TASK_GENERATOR WHERE TASK_GENERATOR_ID = ?");
            deleteStmt.setLong(1, rs.getLong(1));
            deleteStmt.executeUpdate();
        }
        Session session = Database.instance().createSession(conn);
        try {
            ActivitySequenceTaskGenerator generator = new ActivitySequenceTaskGenerator();
            generator.setStartTaskDate(new Date());
            generator.setActivityID(getScheduledActivityID());
            generator.setTaskInterval(24 * 1000 * 60 * 60);
            session.save(generator);
            session.flush();
        } finally {
            session.close();
        }
    }

    public void taskNow(EIConnection connection) throws Exception {
        try {
            connection.setAutoCommit(false);
            ActivitySequenceTask deliveryScheduledTask = new ActivitySequenceTask();
            deliveryScheduledTask.setActivityID(getScheduledActivityID());
            deliveryScheduledTask.internalExecute(new Date(), connection);
            if (!connection.getAutoCommit()) {
                connection.commit();
            }
        } catch (Exception e) {
            LogClass.error(e);
            if (!connection.getAutoCommit()) {
                connection.rollback();
            }
        } finally {
            connection.setAutoCommit(false);
        }
    }

    @Override
    protected void customLoad(EIConnection conn) throws SQLException {
        activities = new ArrayList<>();
        PreparedStatement load = conn.prepareStatement("SELECT child_activity_id FROM sequence_to_scheduled_activity WHERE parent_activity_id = ? ORDER BY activity_index");
        PreparedStatement query = conn.prepareStatement("SELECT scheduled_account_activity.activity_type FROM scheduled_account_activity WHERE scheduled_account_activity_id = ?");
        load.setLong(1, getScheduledActivityID());
        ResultSet rs = load.executeQuery();
        while (rs.next()) {
            long activityID = rs.getLong(1);
            query.setLong(1, activityID);
            ResultSet qRS = query.executeQuery();
            if (qRS.next()) {
                int activityType = qRS.getInt(1);
                ScheduledActivity activity = ScheduledActivity.createActivity(activityType, activityID, conn);
                activities.add(activity);
            }
        }
        query.close();
        load.close();
    }

    @Override
    protected void customSave(EIConnection conn, int utcOffset) throws SQLException {
        PreparedStatement clear = conn.prepareStatement("DELETE FROM sequence_to_scheduled_activity WHERE parent_activity_id = ?");
        clear.setLong(1, getScheduledActivityID());
        clear.executeUpdate();
        clear.close();
        PreparedStatement insert = conn.prepareStatement("INSERT INTO sequence_to_scheduled_activity (parent_activity_id, child_activity_id, activity_index) VALUES (?, ?, ?)");
        for (int i = 0; i < activities.size(); i++) {
            ScheduledActivity scheduledActivity = activities.get(i);
            scheduledActivity.save(conn, utcOffset);
            insert.setLong(1, getScheduledActivityID());
            insert.setLong(2, scheduledActivity.getScheduledActivityID());
            insert.setInt(3, i);
            insert.execute();
        }
        insert.close();
    }

    @Override
    public boolean authorize() {
        return true;
    }

    @Override
    public String describe() {
        return null;
    }
}
