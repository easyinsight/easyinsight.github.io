package com.easyinsight.email;

import com.easyinsight.security.SecurityUtil;
import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;

import java.util.*;
import java.sql.*;
import java.text.MessageFormat;

/**
 * User: James Boe
 * Date: Aug 17, 2008
 * Time: 11:14:06 PM
 */
public class ShareService {

    private static final String USERS_FROM_FEEDS = "SELECT DISTINCT USER.USER_ID, USERNAME, NAME, EMAIL FROM UPLOAD_POLICY_USERS, " +
                    "USER WHERE FEED_ID IN {0} AND UPLOAD_POLICY_USERS.USER_ID = USER.USER_ID";

    private static final String USERS_FROM_ANALYSIS = "SELECT DISTINCT USER.USER_ID, USERNAME, NAME, EMAIL FROM " +
                    "USER_TO_ANALYSIS, USER WHERE ANALYSIS_ID IN {0} AND USER_TO_ANALYSIS.USER_ID = USER.USER_ID";

    private static final String USERS_IN_ACCOUNT = "SELECT USER_ID, USERNAME, NAME, EMAIL FROM USER WHERE ACCOUNT_ID = ?";

    public List<UserStub> getUserStubs() {
        long userID = SecurityUtil.getUserID();
        Connection conn = Database.instance().getConnection();
        try {

            PreparedStatement findFeedsStmt = conn.prepareStatement("SELECT FEED_ID FROM UPLOAD_POLICY_USERS WHERE USER_ID = ?");
            PreparedStatement findAnalysisStmt = conn.prepareStatement("SELECT ANALYSIS_ID FROM USER_TO_ANALYSIS WHERE USER_ID = ?");

            Statement findUsersStmt = conn.createStatement();

            Set<Long> feedIDSet = new HashSet<Long>();
            findFeedsStmt.setLong(1, userID);
            ResultSet feedsRS = findFeedsStmt.executeQuery();
            while (feedsRS.next()) {
                long feedID = feedsRS.getLong(1);
                feedIDSet.add(feedID);
            }

            Set<Long> analysisIDSet = new HashSet<Long>();
            findAnalysisStmt.setLong(1, userID);
            ResultSet analysisRS = findAnalysisStmt.executeQuery();
            while (analysisRS.next()) {
                long analysisID = analysisRS.getLong(1);
                analysisIDSet.add(analysisID);
            }

            String feedInClause = createInClause(feedIDSet);
            String analysisInClause = createInClause(analysisIDSet);

            Set<UserStub> userStubs = new HashSet<UserStub>();
            String usersFromFeeds = MessageFormat.format(USERS_FROM_FEEDS, feedInClause);
            ResultSet userFeedRS = findUsersStmt.executeQuery(usersFromFeeds);
            while (userFeedRS.next()) {
                UserStub userStub = new UserStub(userFeedRS.getLong(1), userFeedRS.getString(2), userFeedRS.getString(3),
                        userFeedRS.getString(4));
                userStubs.add(userStub);
            }

            String usersFromAnalysis = MessageFormat.format(USERS_FROM_ANALYSIS, analysisInClause);
            ResultSet userAnalysisRS = findUsersStmt.executeQuery(usersFromAnalysis);
            while (userAnalysisRS.next()) {
                UserStub userStub = new UserStub(userAnalysisRS.getLong(1), userAnalysisRS.getString(2), userAnalysisRS.getString(3),
                        userAnalysisRS.getString(4));
                userStubs.add(userStub);
            }

            PreparedStatement usersStmt = conn.prepareStatement(USERS_IN_ACCOUNT);
            usersStmt.setLong(1, SecurityUtil.getAccountID());
            ResultSet userAccountRS = usersStmt.executeQuery();
            while (userAccountRS.next()) {
                UserStub userStub = new UserStub(userAccountRS.getLong(1), userAccountRS.getString(2), userAccountRS.getString(3),
                        userAccountRS.getString(4));
                userStubs.add(userStub);
            }

            UserStub userStub = new UserStub();
            userStub.setUserID(SecurityUtil.getUserID());
            userStubs.remove(userStub);

            return new ArrayList<UserStub>(userStubs);
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
    }

    private String createInClause(Set<Long> feedIDSet) {
        StringBuilder feedInClauseBuilder = new StringBuilder("(");
        Iterator<Long> feedIDIter = feedIDSet.iterator();
        while (feedIDIter.hasNext()) {
            feedInClauseBuilder.append(feedIDIter.next());
            if (feedIDIter.hasNext()) {
                feedInClauseBuilder.append(",");
            }
        }
        feedInClauseBuilder.append(")");
        return feedInClauseBuilder.toString();
    }
}
