package com.easyinsight.scorecard;

import com.easyinsight.core.RolePrioritySet;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.Roles;
import com.easyinsight.security.SecurityUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: jamesboe
 * Date: Mar 31, 2010
 * Time: 5:02:48 PM
 */
public class ScorecardInternalService {
    public RolePrioritySet<ScorecardDescriptor> getScorecardDescriptors(long userID, long accountID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            return getScorecards(userID, accountID, conn);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public RolePrioritySet<ScorecardDescriptor> getScorecards(long userID, long accountID, EIConnection conn) throws SQLException {
        RolePrioritySet<ScorecardDescriptor> scorecards = new RolePrioritySet<ScorecardDescriptor>();
        PreparedStatement queryStmt = conn.prepareStatement("SELECT SCORECARD.scorecard_id, SCORECARD.scorecard_name from " +
                "scorecard where scorecard.user_id = ?");
        queryStmt.setLong(1, userID);
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            long scorecardID = rs.getLong(1);
            String scorecardName = rs.getString(2);
            ScorecardDescriptor scorecardDescriptor = new ScorecardDescriptor();
            scorecardDescriptor.setId(scorecardID);
            scorecardDescriptor.setName(scorecardName);
            scorecardDescriptor.setRole(Roles.OWNER);
            scorecards.add(scorecardDescriptor);
        }
        PreparedStatement accountStmt = conn.prepareStatement("SELECT SCORECARD.SCORECARD_ID, SCORECARD.SCORECARD_NAME FROM " +
                "SCORECARD, USER WHERE SCORECARD.USER_ID = USER.USER_ID AND USER.ACCOUNT_ID = ? and SCORECARD.ACCOUNT_VISIBLE = ?");
        accountStmt.setLong(1, accountID);
        accountStmt.setBoolean(2, true);
        ResultSet accountRS = accountStmt.executeQuery();
        while (accountRS.next()) {
            long scorecardID = accountRS.getLong(1);
            String scorecardName = accountRS.getString(2);
            ScorecardDescriptor scorecardDescriptor = new ScorecardDescriptor();
            scorecardDescriptor.setId(scorecardID);
            scorecardDescriptor.setName(scorecardName);
            scorecardDescriptor.setRole(Roles.SUBSCRIBER);
            scorecards.add(scorecardDescriptor);
        }
        PreparedStatement groupStmt = conn.prepareStatement("SELECT SCORECARD.scorecard_id, SCORECARD.scorecard_name, scorecard.group_id, " +
                "group_to_user_join.binding_type, COMMUNITY_GROUP.name from " +
                "scorecard, group_to_user_join, community_group where scorecard.group_id = group_to_user_join.group_id AND group_to_user_join.user_id = ? AND " +
                "SCORECARD.group_id = COMMUNITY_GROUP.community_group_id");
        groupStmt.setLong(1, userID);
        ResultSet groupRS = groupStmt.executeQuery();
        while (groupRS.next()) {
            long scorecardID = groupRS.getLong(1);
            String scorecardName = groupRS.getString(2);
            long groupID = groupRS.getLong(3);
            int role = groupRS.getInt(4);
            ScorecardDescriptor scorecardDescriptor = new ScorecardDescriptor();
            scorecardDescriptor.setId(scorecardID);
            scorecardDescriptor.setName(scorecardName);
            scorecardDescriptor.setRole(role);
            scorecards.add(scorecardDescriptor);
        }
        return scorecards;
    }

    public Scorecard getScorecard(long scorecardID, long userID) {
        SecurityUtil.authorizeScorecard(scorecardID, userID);
        try {
            return new ScorecardStorage().getScorecard(scorecardID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
