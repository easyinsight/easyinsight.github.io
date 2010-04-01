package com.easyinsight.scorecard;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.CredentialFulfillment;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.userupload.UserUploadService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: Mar 31, 2010
 * Time: 5:02:48 PM
 */
public class ScorecardInternalService {
    public ScorecardList getScorecardDescriptors(long userID) {
        List<ScorecardDescriptor> scorecards = new ArrayList<ScorecardDescriptor>();

        EIConnection conn = Database.instance().getConnection();
        try {
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
                scorecards.add(scorecardDescriptor);
            }

        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }

        boolean hasData = true;
        if (scorecards.isEmpty()) {
            hasData = (new UserUploadService().getFeedAnalysisTree(true, true).getObjects().size() > 0);
        }
        return new ScorecardList(scorecards, hasData);
    }

    public ScorecardWrapper getScorecard(long scorecardID, long userID, List<CredentialFulfillment> credentials, boolean forceRefresh) {
        SecurityUtil.authorizeScorecard(scorecardID, userID);
        try {
            return new ScorecardStorage().getScorecard(scorecardID, credentials, forceRefresh);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
