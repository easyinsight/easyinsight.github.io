package com.easyinsight.export;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.scheduler.DataSourceTaskGenerator;
import com.easyinsight.security.Roles;
import com.easyinsight.security.SecurityUtil;
import org.hibernate.Session;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * User: jamesboe
 * Date: Jun 2, 2010
 * Time: 9:36:52 PM
 */
public class DataSourceRefreshActivity extends ScheduledActivity {

    public static final int DAILY = 0;
    public static final int HOURLY = 1;

    private long dataSourceID;
    private String dataSourceName;

    private int intervalNumber;
    private int intervalUnits;

    public String toURL() {
        return "dataSourceRefreshSetup.jsp?activityID=" + getScheduledActivityID();
    }

    public int getIntervalUnits() {
        return intervalUnits;
    }

    public void setIntervalUnits(int intervalUnits) {
        this.intervalUnits = intervalUnits;
    }

    public int getIntervalNumber() {
        return intervalNumber;
    }

    public void setIntervalNumber(int intervalNumber) {
        this.intervalNumber = intervalNumber;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    @Override
    public int retrieveType() {
        return ScheduledActivity.DATA_SOURCE_REFRESH;
    }

    protected void customSave(EIConnection conn, int utcOffset) throws SQLException {
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM SCHEDULED_DATA_SOURCE_REFRESH WHERE SCHEDULED_ACCOUNT_ACTIVITY_ID = ?");
        clearStmt.setLong(1, getScheduledActivityID());
        clearStmt.executeUpdate();
        clearStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO SCHEDULED_DATA_SOURCE_REFRESH (DATA_SOURCE_ID, SCHEDULED_ACCOUNT_ACTIVITY_ID, " +
                "INTERVAL_TYPE, INTERVAL_UNITS) VALUES (?, ?, ?, ?)");
        insertStmt.setLong(1, dataSourceID);
        insertStmt.setLong(2, getScheduledActivityID());
        insertStmt.setInt(3, intervalNumber);
        insertStmt.setInt(4, intervalUnits);
        insertStmt.execute();
        insertStmt.close();
    }

    protected void customLoad(EIConnection conn) throws SQLException {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_SOURCE_ID, DATA_FEED.FEED_NAME, INTERVAL_TYPE, INTERVAL_UNITS FROM " +
                "SCHEDULED_DATA_SOURCE_REFRESH, DATA_FEED WHERE " +
                "SCHEDULED_ACCOUNT_ACTIVITY_ID = ? AND DATA_SOURCE_ID = DATA_FEED.DATA_FEED_ID");
        try {
            queryStmt.setLong(1, getScheduledActivityID());
            ResultSet rs = queryStmt.executeQuery();
            rs.next();
            dataSourceID = rs.getLong(1);
            dataSourceName = rs.getString(2);
            intervalNumber = rs.getInt(3);
            intervalUnits = rs.getInt(4);
        } finally {
            queryStmt.close();
        }
        PreparedStatement queryProblemStmt = conn.prepareStatement("SELECT problem_text FROM DATA_SOURCE_PROBLEM WHERE DATA_SOURCE_ID = ?");
        queryProblemStmt.setLong(1, getDataSourceID());
        ResultSet problemRS = queryProblemStmt.executeQuery();
        int count = 0;
        String message = null;
        while (problemRS.next()) {
            count++;
            message = problemRS.getString(1);
        }
        if (count > 1) {
            setProblemLevel(2);
            setProblemMessage(message);
        } else if (count == 1) {
            setProblemLevel(1);
            setProblemMessage(message);
        }
        queryProblemStmt.close();
    }

    @Override
    public boolean authorize() {
        try {
            SecurityUtil.authorizeFeed(dataSourceID, Roles.SHARER);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void setup(EIConnection conn) throws SQLException {                
        // nuke the existing generator
        PreparedStatement queryStmt = conn.prepareStatement("SELECT TASK_GENERATOR_ID FROM data_activity_task_generator where SCHEDULED_ACTIVITY_ID = ?");
        queryStmt.setLong(1, getScheduledActivityID());
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM TASK_GENERATOR WHERE TASK_GENERATOR_ID = ?");
            deleteStmt.setLong(1, rs.getLong(1));
            deleteStmt.executeUpdate();
            deleteStmt.close();
            PreparedStatement arghStmt = conn.prepareStatement("DELETE FROM DATA_ACTIVITY_TASK_GENERATOR WHERE SCHEDULED_ACTIVITY_ID = ?");
            arghStmt.setLong(1, getScheduledActivityID());
            arghStmt.executeUpdate();
            arghStmt.close();
        }
        queryStmt.close();
        Session session = Database.instance().createSession(conn);
        try {    
            DataSourceTaskGenerator generator = new DataSourceTaskGenerator();
            generator.setStartTaskDate(new Date());
            generator.setActivityID(getScheduledActivityID());
            if (intervalNumber == DAILY) {
                generator.setTaskInterval(24 * 1000 * 60 * 60);
            } else if (intervalNumber == HOURLY) {
                generator.setTaskInterval(1000 * 60 * 60 * intervalUnits);
            }
            session.save(generator);
            session.flush();
        } finally {
            session.close();
        }
    }

    @Override
    public String describe() {
        return "Refresh " + dataSourceName;
    }

    @Override
    public JSONObject toJSON(ExportMetadata md) throws JSONException {
        JSONObject jo = super.toJSON(md);
        jo.put("data_source", dataSourceID);
        jo.put("label", toString());
        jo.put("interval_number", getIntervalNumber());
        jo.put("interval_units", getIntervalUnits());
        return jo;
    }

    public DataSourceRefreshActivity() {
    }

    public DataSourceRefreshActivity(long id, net.minidev.json.JSONObject jsonObject) {
        super(id, jsonObject);
        setDataSourceID(Long.parseLong(String.valueOf(jsonObject.get("data_source"))));
        setIntervalNumber(Integer.parseInt(String.valueOf(jsonObject.get("interval_number"))));
        setIntervalUnits(Integer.parseInt(String.valueOf(jsonObject.get("interval_units"))));
    }
}
