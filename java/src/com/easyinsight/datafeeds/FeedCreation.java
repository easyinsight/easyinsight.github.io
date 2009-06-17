package com.easyinsight.datafeeds;

import com.easyinsight.storage.DataStorage;
import com.easyinsight.analysis.*;
import com.easyinsight.security.Roles;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.database.Database;
import com.easyinsight.util.RandomTextGenerator;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;

/**
 * User: James Boe
 * Date: Nov 9, 2008
 * Time: 9:22:36 PM
 */
public class FeedCreation {

    private FeedStorage feedStorage = new FeedStorage();

     public FeedCreationResult createFeed(FeedDefinition feedDefinition, Connection conn, DataSet dataSet, long userID) throws SQLException {
        return createFeed(feedDefinition, conn, dataSet, userID, SecurityUtil.getAccountID());    
     }

    public FeedCreationResult createFeed(FeedDefinition feedDefinition, Connection conn, DataSet dataSet, long userID, long accountID) throws SQLException {
        long feedID = feedStorage.addFeedDefinitionData(feedDefinition, conn);
        DataStorage tableDef = null;
        if (dataSet != null) {
            tableDef = DataStorage.writeConnection(feedDefinition, conn, accountID);
            tableDef.createTable();
            tableDef.insertData(dataSet);
        }
        AnalysisDefinition baseDefinition = new AnalysisDefinition();
        baseDefinition.setDataFeedID(feedID);
        baseDefinition.setRootDefinition(true);
        baseDefinition.setAnalysisDefinitionState(new ListDefinitionState());
        baseDefinition.setUserBindings(Arrays.asList(new UserToAnalysisBinding(userID, UserPermission.OWNER)));
        new AnalysisStorage().saveAnalysis(baseDefinition, conn);
        feedDefinition.setAnalysisDefinitionID(baseDefinition.getAnalysisID());
        feedDefinition.setApiKey(RandomTextGenerator.generateText(12));
        feedStorage.updateDataFeedConfiguration(feedDefinition, conn);
        createUserFeedLink(userID, feedDefinition.getDataFeedID(), Roles.OWNER, conn);
        return new FeedCreationResult(feedID, tableDef);
    }

    private void createUserFeedLink(long userID, long dataFeedID, int owner, Connection conn) {
        try {
            PreparedStatement existingLinkQuery = conn.prepareStatement("SELECT UPLOAD_POLICY_USERS_ID FROM UPLOAD_POLICY_USERS WHERE " +
                    "USER_ID = ? AND FEED_ID = ?");
            existingLinkQuery.setLong(1, userID);
            existingLinkQuery.setLong(2, dataFeedID);
            ResultSet existingRS = existingLinkQuery.executeQuery();
            if (existingRS.next()) {
                long existingID = existingRS.getLong(1);
                PreparedStatement updateLinkStmt = conn.prepareStatement("UPDATE UPLOAD_POLICY_USERS SET ROLE = ? WHERE " +
                        "UPLOAD_POLICY_USERS_ID = ?");
                updateLinkStmt.setLong(1, owner);
                updateLinkStmt.setLong(2, existingID);
                updateLinkStmt.executeUpdate();
            } else {
                PreparedStatement insertFeedStmt = conn.prepareStatement("INSERT INTO UPLOAD_POLICY_USERS (USER_ID, FEED_ID, ROLE) " +
                        "VALUES (?, ?, ?)");
                insertFeedStmt.setLong(1, userID);
                insertFeedStmt.setLong(2, dataFeedID);
                insertFeedStmt.setLong(3, owner);
                insertFeedStmt.execute();
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private void createUserFeedLink(long userID, long dataFeedID, int owner) {
        Connection conn = Database.instance().getConnection();
        try {
            createUserFeedLink(userID, dataFeedID, owner, conn);
        } finally {
            Database.instance().closeConnection(conn);
        }
    }
}
