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

/**
 * User: jamesboe
 * Date: Jan 18, 2010
 * Time: 3:46:36 PM
 */
public class ScorecardService {

    private ScorecardStorage scorecardStorage = new ScorecardStorage();

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

    public ScorecardList getScorecardDescriptors() {
        long userID = SecurityUtil.getUserID();
        return new ScorecardInternalService().getScorecardDescriptors(userID);
    }

    public ScorecardList getScorecardDescriptorsForGroup(long groupID) {
        SecurityUtil.authorizeGroup(groupID, Roles.SUBSCRIBER);
        List<ScorecardDescriptor> scorecards = new ArrayList<ScorecardDescriptor>();

        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT SCORECARD.scorecard_id, SCORECARD.scorecard_name from " +
                    "scorecard where scorecard.group_id = ?");
            queryStmt.setLong(1, groupID);
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

    public ScorecardWrapper getScorecard(long scorecardID, List<CredentialFulfillment> credentials, boolean forceRefresh) {
        SecurityUtil.authorizeScorecard(scorecardID);
        try {
            return scorecardStorage.getScorecard(scorecardID, credentials, forceRefresh);
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

    public KPI addKPIToScorecard(KPI kpi, long scorecardID, List<CredentialFulfillment> credentials) {
        SecurityUtil.authorizeScorecard(scorecardID);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            scorecardStorage.addKPIToScorecard(kpi, scorecardID, conn);
            refreshValuesForList(Arrays.asList(kpi), conn, credentials, false);
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

    public KPI updateKPI(KPI kpi, List<CredentialFulfillment> credentials) {
        SecurityUtil.authorizeKPI(kpi.getKpiID());
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            new KPIStorage().saveKPI(kpi, conn);
            refreshValuesForList(Arrays.asList(kpi), conn, credentials, false);
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

    private KPIOutcome refreshKPIValue(KPI kpi, List<CredentialFulfillment> credentials, EIConnection conn) throws SQLException {
        List<KPIValue> lastTwoValues = new HistoryRun().lastTwoValues(kpi.getCoreFeedID(), kpi.getAnalysisMeasure(),
                kpi.getFilters(), credentials, kpi.getDayWindow());
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

    public List<KPI> refreshValuesForList(List<KPI> kpis, EIConnection conn, List<CredentialFulfillment> credentialsList, boolean allSources) throws Exception {
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
                    for (CredentialFulfillment fulfillment : credentialsList) {
                        if (fulfillment.getDataSourceID() == feedDefinition.getDataFeedID()) {
                            credentials = fulfillment.getCredentials();
                        }
                    }
                    if (credentials != null && credentials.isEncrypted()) {
                        credentials = credentials.decryptCredentials();
                    }
                    dataSource.refreshData(credentials, SecurityUtil.getAccountID(), new Date(), null);
                }
            }
        }
        for (KPI kpi : kpis) {
            KPIOutcome kpiValue = refreshKPIValue(kpi, credentialsList, conn);
            kpi.setKpiOutcome(kpiValue);
            kpiValue.setKpiID(kpi.getKpiID());
            //kpi.setKpiValue(kpiValue);
            new KPIStorage().saveKPIOutcome(kpi.getKpiID(), kpiValue.getOutcomeValue(), kpiValue.getPreviousValue(),
                            kpiValue.getEvaluationDate(), kpiValue.getOutcomeState(), kpiValue.getDirection(), kpiValue.isProblemEvaluated(),
                    kpiValue.getPercentChange(), kpiValue.isDirectional(), conn);
        }
        return kpis;
    }
}
