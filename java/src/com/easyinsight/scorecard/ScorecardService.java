package com.easyinsight.scorecard;

import com.easyinsight.analysis.*;
import com.easyinsight.core.NumericValue;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.kpi.*;
import com.easyinsight.logging.LogClass;
import com.easyinsight.pipeline.HistoryRun;
import com.easyinsight.security.Roles;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Account;
import com.easyinsight.users.Credentials;
import com.easyinsight.userupload.UserUploadService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;

/**
 * User: jamesboe
 * Date: Jan 18, 2010
 * Time: 3:46:36 PM
 */
public class ScorecardService {

    private static JCS cache = getCache("scorecardQueue");

    private static JCS getCache(String cacheName) {

        try {
            return JCS.getInstance(cacheName);
        } catch (Exception e) {
            LogClass.error(e);
        }
        return null;
    }


    private ScorecardStorage scorecardStorage = new ScorecardStorage();

    /*private Set<Long> visibilityMatrix(EIConnection conn) throws SQLException {
        Set<Long> ids = new HashSet<Long>();
        PreparedStatement queryAnalysisStmt = conn.prepareStatement("SELECT ANALYSIS_ID FROM user_to_analysis WHERE USER_ID = ?");
        queryAnalysisStmt.setLong(1, SecurityUtil.getUserID());
        ResultSet reportRS = queryAnalysisStmt.executeQuery();
        while (reportRS.next()) {
            ids.add(reportRS.getLong(1));
        }
        PreparedStatement queryGroupReportStmt = conn.prepareStatement("SELECT GROUP_TO_INSIGHT.insight_id FROM GROUP_TO_INSIGHT, GROUP_TO_USER_JOIN " +
                "WHERE GROUP_TO_INSIGHT.group_id = group_to_user_join.group_id and group_to_user_join.user_id = ?");
        queryGroupReportStmt.setLong(1, SecurityUtil.getUserID());
        ResultSet groupRS = queryAnalysisStmt.executeQuery();
        while (groupRS.next()) {
            ids.add(groupRS.getLong(1));
        }
        return ids;
    }*/

    public void saveScorecardOrdering(List<ScorecardDescriptor> descriptors) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM USER_SCORECARD_ORDERING WHERE USER_ID = ?");
            clearStmt.setLong(1, SecurityUtil.getUserID());
            clearStmt.executeUpdate();
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO USER_SCORECARD_ORDERING (USER_ID, SCORECARD_ID, SCORECARD_ORDER) " +
                    "VALUES (?, ?, ?)");
            for (int i = 0; i < descriptors.size(); i++) {
                ScorecardDescriptor scorecardDescriptor = descriptors.get(i);
                insertStmt.setLong(1, SecurityUtil.getUserID());
                insertStmt.setLong(2, scorecardDescriptor.getId());
                insertStmt.setInt(3, i);
                insertStmt.execute();
            }
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public long determineInitialDisplay() {
        // process here ->
        // is the user explicitly stated to use their own scorecard?
        // if yes, we just return that
        // if no, is there a default group for the user...
        //      if pro or higher account, is there a default group for the persona?
        //      otherwise, is there a default account group?
        //          if yes, is there a scorecard set up on that default group?

        int accountType = SecurityUtil.getAccountTier();
        if (accountType == Account.PERSONAL) {
            return 0;
        }
        long accountID = SecurityUtil.getAccountID();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT GROUP_ID FROM ACCOUNT WHERE ACCOUNT_ID = ?");
            queryStmt.setLong(1, accountID);
            ResultSet rs = queryStmt.executeQuery();
            rs.next();
            long groupID = rs.getLong(1);
            if (rs.wasNull()) {

                // No group assigned
                return 0;
            }
            return groupID;
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public ScorecardList getScorecardDescriptors(boolean includeGroups) {
        long userID = SecurityUtil.getUserID();
        return new ScorecardInternalService().getScorecardDescriptors(userID, includeGroups);
    }

    public ScorecardList getScorecardDescriptorsForGroup(long groupID) {
        SecurityUtil.authorizeGroup(groupID, Roles.SUBSCRIBER);
        List<ScorecardDescriptor> scorecards = new ArrayList<ScorecardDescriptor>();

        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT SCORECARD.scorecard_id, SCORECARD.scorecard_name, GROUP_TO_USER_JOIN.binding_type," +
                    "COMMUNITY_GROUP.name from " +
                    "scorecard, GROUP_TO_USER_JOIN, community_group where scorecard.group_id = ? AND scorecard.group_id = GROUP_TO_USER_JOIN.group_id AND " +
                    "GROUP_TO_USER_JOIN.user_id = ? AND SCORECARD.GROUP_ID = COMMUNITY_GROUP.community_group_id");
            queryStmt.setLong(1, groupID);
            queryStmt.setLong(2, SecurityUtil.getUserID());
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                long scorecardID = rs.getLong(1);
                String scorecardName = rs.getString(2);
                int role = rs.getInt(3);
                String groupName = rs.getString(4);
                ScorecardDescriptor scorecardDescriptor = new ScorecardDescriptor();
                scorecardDescriptor.setGroupID(groupID);
                scorecardDescriptor.setId(scorecardID);
                scorecardDescriptor.setName(scorecardName);
                scorecardDescriptor.setGroupRole(role);
                scorecardDescriptor.setGroupName(groupName);
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

    public ScorecardWrapper getScorecard(long scorecardID, List<CredentialFulfillment> credentials, boolean forceRefresh, InsightRequestMetadata insightRequestMetadata) {
        SecurityUtil.authorizeScorecard(scorecardID);
        try {
            insightRequestMetadata.setCredentialFulfillmentList(credentials);
            return scorecardStorage.getScorecard(scorecardID, insightRequestMetadata, forceRefresh);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public long saveScorecardForUser(Scorecard scorecard) {
        long userID = SecurityUtil.getUserID();
        try {
            scorecardStorage.saveScorecardForUser(scorecard, userID);
            return scorecard.getScorecardID();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public long saveScorecardForGroup(Scorecard scorecard, long groupID) {
        SecurityUtil.authorizeGroup(groupID, Roles.SUBSCRIBER);
        try {
            scorecardStorage.saveScorecardForGroup(scorecard, groupID);
            return scorecard.getScorecardID();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    /*public List<Scorecard> getScorecardsForUser(List<CredentialFulfillment> credentials) {
        long userID = SecurityUtil.getUserID();
        try {
            return scorecardStorage.getScorecardsForUser(userID, credentials);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }*/

    public void addKPIsToDefaultScorecard() {

    }

    public KPI addKPIToScorecard(KPI kpi, long scorecardID, List<CredentialFulfillment> credentials, InsightRequestMetadata insightRequestMetadata) {
        SecurityUtil.authorizeScorecard(scorecardID);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            scorecardStorage.addKPIToScorecard(kpi, scorecardID, conn);
            insightRequestMetadata.setCredentialFulfillmentList(credentials);
            refreshValuesForList(Arrays.asList(kpi), conn, insightRequestMetadata, false);
            conn.commit();
            return kpi;
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public void deleteScorecard(long scorecardID) {
        SecurityUtil.authorizeScorecard(scorecardID);
        try {
            scorecardStorage.deleteScorecard(scorecardID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void removeKPIFromScorecard(long kpiID, long scorecardID) {
        SecurityUtil.authorizeScorecard(scorecardID);
        try {
            scorecardStorage.removeKPIFromScorecard(kpiID, scorecardID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public KPI updateKPI(KPI kpi, List<CredentialFulfillment> credentials, InsightRequestMetadata insightRequestMetadata) {
        SecurityUtil.authorizeKPI(kpi.getKpiID());
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            new KPIStorage().saveKPI(kpi, conn);
            insightRequestMetadata.setCredentialFulfillmentList(credentials);
            refreshValuesForList(Arrays.asList(kpi), conn, insightRequestMetadata, false);
            conn.commit();
            return kpi;
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public void getCredentialsForDataSources(boolean allSources, List<CredentialFulfillment> existingCredentials, Map<Long, CredentialRequirement> credentialMap, Set<Long> dataSourceIDs) throws SQLException {
        if (allSources) {
            for (Long dataSourceID : dataSourceIDs) {
                FeedDefinition feedDefinition = new FeedStorage().getFeedDefinitionData(dataSourceID);
                if (feedDefinition.getCredentialsDefinition() == CredentialsDefinition.STANDARD_USERNAME_PW) {
                    IServerDataSourceDefinition dataSource = (IServerDataSourceDefinition) feedDefinition;
                    Credentials credentials = null;
                    for (CredentialFulfillment fulfillment : existingCredentials) {
                        if (fulfillment.getDataSourceID() == feedDefinition.getDataFeedID()) {
                            credentials = fulfillment.getCredentials();
                        }
                    }
                    boolean noCredentials = true;
                    if (credentials != null) {
                        noCredentials = dataSource.validateCredentials(credentials) != null;
                    }
                    if (noCredentials) {
                        credentialMap.put(feedDefinition.getDataFeedID(), new CredentialRequirement(feedDefinition.getDataFeedID(), feedDefinition.getFeedName(),
                                CredentialsDefinition.STANDARD_USERNAME_PW));
                    }
                }
            }
        } else {
            for (Long dataSourceID : dataSourceIDs) {
                Feed feed = FeedRegistry.instance().getFeed(dataSourceID);
                Credentials credentials = null;
                for (CredentialFulfillment fulfillment : existingCredentials) {
                    if (fulfillment.getDataSourceID() == dataSourceID) {
                        credentials = fulfillment.getCredentials();
                    }
                }
                boolean noCredentials = true;
                if (credentials != null) {
                    noCredentials = new FeedStorage().getFeedDefinitionData(dataSourceID).validateCredentials(credentials) != null;
                }
                if (noCredentials) {
                    Set<CredentialRequirement> credentialRequirements = feed.getCredentialRequirement(false);
                    for (CredentialRequirement credentialRequirement : credentialRequirements) {
                        credentialMap.put(credentialRequirement.getDataSourceID(), credentialRequirement);
                    }
                }
            }
        }
    }

    private int determineOutcome(double goalValue, int highIsGood, double delta, double endValue, double tolerance) {
        int outcome;
        if (highIsGood == KPI.GOOD) {
            if (endValue >= goalValue) {
                outcome = KPIOutcome.EXCEEDING_GOAL;
            } else if (Math.abs(delta) < Math.abs(goalValue * (tolerance / 100))) {
                outcome = KPIOutcome.NEUTRAL;
            } else if (delta > 0) {
                outcome = KPIOutcome.POSITIVE;
            } else {
                outcome = KPIOutcome.NEGATIVE;
            }
        } else if (highIsGood == KPI.BAD) {
            if (endValue <= goalValue) {
                outcome = KPIOutcome.EXCEEDING_GOAL;
            } else if (Math.abs(delta) < Math.abs(goalValue * (tolerance / 100))) {
                outcome = KPIOutcome.NEUTRAL;
            } else if (delta < 0) {
                outcome = KPIOutcome.POSITIVE;
            } else {
                outcome = KPIOutcome.NEGATIVE;
            }
        } else {
            outcome = KPIOutcome.NEUTRAL;
        }
        return outcome;
    }

    private int determineSimpleOutcome(int highIsGood, double endValue, double delta, double tolerance) {
        int outcome;
        double thresholdValue = Math.abs(endValue * (tolerance / 100));
        if (highIsGood == KPI.GOOD) {
            if (Math.abs(delta) <= thresholdValue) {
                outcome = KPIOutcome.NEUTRAL;
            } else if (delta > 0) {
                outcome = KPIOutcome.POSITIVE;
            } else {
                outcome = KPIOutcome.NEGATIVE;
            }
        } else if (highIsGood == KPI.BAD) {
            if (Math.abs(delta) <= thresholdValue) {
                outcome = KPIOutcome.NEUTRAL;
            } else if (delta < 0) {
                outcome = KPIOutcome.POSITIVE;
            } else {
                outcome = KPIOutcome.NEGATIVE;
            }
        } else {
            outcome = KPIOutcome.NEUTRAL;
        }
        return outcome;
    }

    private KPIOutcome refreshKPIValue(KPI kpi, EIConnection conn, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        List<KPIValue> lastTwoValues = new HistoryRun().lastTwoValues(kpi.getCoreFeedID(), kpi.getAnalysisMeasure(),
                kpi.getFilters(), kpi.getDayWindow(), insightRequestMetadata);
        Double newValue = null;
        Double oldValue = null;
        Double percentChange = null;
        boolean failedCondition = false;
        boolean directional = false;
        int outcomeState = KPIOutcome.NO_DATA;
        int direction = KPIOutcome.NO_DIRECTION;
        if (lastTwoValues.size() > 0) {
            int highIsGood = kpi.getHighIsGood();
            if (lastTwoValues.size() > 1) {
                directional = true;
                KPIValue previousGoalValue = lastTwoValues.get(0);
                oldValue = previousGoalValue.getValue();
                KPIValue newGoalValue = lastTwoValues.get(1);
                newValue = newGoalValue.getValue();

                double delta = newValue - oldValue;
                percentChange = delta / oldValue * 100;
                if (delta > 0) {
                    direction = KPIOutcome.UP_DIRECTION;
                } else if (delta < 0) {
                    direction = KPIOutcome.DOWN_DIRECTION;
                } else {
                    direction = KPIOutcome.NO_DIRECTION;
                }
                if (kpi.isGoalDefined()) {
                    outcomeState = determineOutcome(kpi.getGoalValue(), highIsGood, delta, newValue, kpi.getThreshold());
                } else {
                    outcomeState = determineSimpleOutcome(highIsGood, newValue, delta, kpi.getThreshold());
                }
            } else {
                oldValue = new KPIStorage().findLastGoalValue(conn, kpi.getKpiID());
                KPIValue goalValue = lastTwoValues.get(0);
                newValue = goalValue.getValue();
                if (oldValue != null && newValue != null && kpi.isGoalDefined()) {
                    double delta = newValue - oldValue;
                    percentChange = delta / oldValue * 100;
                    outcomeState = determineOutcome(kpi.getGoalValue(), highIsGood, delta, newValue, kpi.getThreshold());
                    if (delta > 0) {
                        direction = KPIOutcome.UP_DIRECTION;
                    } else if (delta < 0) {
                        direction = KPIOutcome.DOWN_DIRECTION;
                    } else {
                        direction = KPIOutcome.NO_DIRECTION;
                    }
                }
            }

            if (kpi.getProblemConditions().size() > 0) {
                for (FilterDefinition problemCondition : kpi.getProblemConditions()) {
                    MaterializedFilterDefinition filter = problemCondition.materialize(null);
                    NumericValue numericValue = new NumericValue(newValue);
                    if (filter.allows(numericValue)) {
                        failedCondition = true;
                    }
                }
            }
        }
        if (percentChange != null && Double.isNaN(percentChange)) {
            percentChange = null;
        }
        return new KPIOutcome(outcomeState, direction, oldValue, failedCondition, newValue, new Date(), kpi.getKpiID(), percentChange, directional);
    }

    public List<KPI> refreshValuesForList(List<KPI> kpis, EIConnection conn, InsightRequestMetadata insightRequestMetadata, boolean allSources) throws Exception {
        if (allSources) {
            Set<Long> dataSourceIDs = new HashSet<Long>();
            for (KPI kpi : kpis) {
                dataSourceIDs.add(kpi.getCoreFeedID());
            }
            for (Long dataSourceID : dataSourceIDs) {
                FeedDefinition feedDefinition = new FeedStorage().getFeedDefinitionData(dataSourceID, conn);
                if (feedDefinition.getCredentialsDefinition() == CredentialsDefinition.STANDARD_USERNAME_PW) {
                    IServerDataSourceDefinition dataSource = (IServerDataSourceDefinition) feedDefinition;
                    Credentials credentials = null;
                    for (CredentialFulfillment fulfillment : insightRequestMetadata.getCredentialFulfillmentList()) {
                        if (fulfillment.getDataSourceID() == feedDefinition.getDataFeedID()) {
                            credentials = fulfillment.getCredentials();
                        }
                    }
                    if (credentials != null && credentials.isEncrypted()) {
                        credentials = credentials.decryptCredentials();
                    }
                    if (DataSourceMutex.mutex().lock(dataSource.getDataFeedID())) {
                        try {
                            dataSource.refreshData(credentials, SecurityUtil.getAccountID(), new Date(), null);
                        } finally {
                            DataSourceMutex.mutex().unlock(dataSource.getDataFeedID());
                        }
                    }
                }
            }
        }
        for (KPI kpi : kpis) {
            KPIOutcome kpiValue = refreshKPIValue(kpi, conn, insightRequestMetadata);
            kpi.setKpiOutcome(kpiValue);
            kpiValue.setKpiID(kpi.getKpiID());
            //kpi.setKpiValue(kpiValue);
            new KPIStorage().saveKPIOutcome(kpi.getKpiID(), kpiValue.getOutcomeValue(), kpiValue.getPreviousValue(),
                            kpiValue.getEvaluationDate(), kpiValue.getOutcomeState(), kpiValue.getDirection(), kpiValue.isProblemEvaluated(),
                    kpiValue.getPercentChange(), kpiValue.isDirectional(), conn);
        }
        return kpis;
    }

    public void storeScorecardForGoogle(long scorecardID) {
        try {
            cache.put(SecurityUtil.getUserID(), scorecardID);
        } catch (CacheException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
