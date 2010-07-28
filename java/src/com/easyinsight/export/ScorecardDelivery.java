package com.easyinsight.export;

import com.easyinsight.database.EIConnection;
import com.easyinsight.security.SecurityUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: jamesboe
 * Date: Jun 10, 2010
 * Time: 2:46:01 PM
 */
public class ScorecardDelivery extends ScheduledActivity {
    @Override
    public int retrieveType() {
        return ScheduledActivity.SCORECARD_DELIVERY;
    }

    @Override
    protected void customSave(EIConnection conn) throws SQLException {
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM USER_SCORECARD_DISPLAY WHERE SCHEDULED_ACCOUNT_ACTIVITY_ID = ?");
        clearStmt.setLong(1, getScheduledActivityID());
        clearStmt.executeUpdate();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO USER_SCORECARD_DISPLAY (SCHEDULED_ACCOUNT_ACTIVITY_ID, USER_ID) VALUES (?, ?)");
        insertStmt.setLong(1, getScheduledActivityID());
        insertStmt.setLong(2, SecurityUtil.getUserID());
        insertStmt.execute();
    }

    @Override
    protected void customLoad(EIConnection conn) throws SQLException {

    }

    @Override
    public void setup(EIConnection conn) throws SQLException {
        
    }
}
