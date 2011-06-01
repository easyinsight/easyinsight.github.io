package com.easyinsight.scorecard;

import com.easyinsight.analysis.*;
import com.easyinsight.core.NumericValue;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.kpi.*;
import com.easyinsight.logging.LogClass;
import com.easyinsight.pipeline.HistoryRun;
import com.easyinsight.security.Roles;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Account;
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

    public long canAccessScorecard(String urlKey) {
        EIConnection conn = Database.instance().getConnection();
        long scorecardID = 0;
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT SCORECARD.scorecard_id FROM SCORECARD where url_key = ?");
            queryStmt.setString(1, urlKey);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                scorecardID = rs.getLong(1);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        if (scorecardID != 0) {
            try {
                SecurityUtil.authorizeScorecard(scorecardID);
            } catch (com.easyinsight.security.SecurityException e) {
                scorecardID = 0;
            }
        }
        return scorecardID;
    }

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

    public List<ScorecardDescriptor> getScorecardDescriptors() {
        long userID = SecurityUtil.getUserID();
        long accountID = SecurityUtil.getAccountID();
        return new ScorecardInternalService().getScorecardDescriptors(userID, accountID).values();
    }

    public List<ScorecardDescriptor> getScorecardDescriptorsForDataSource(long dataSourceID) {
        long userID = SecurityUtil.getUserID();
        long accountID = SecurityUtil.getAccountID();
        List<ScorecardDescriptor> scorecards = new ScorecardInternalService().getScorecardDescriptors(userID, accountID).values();
        List<ScorecardDescriptor> matchedScorecards = new ArrayList<ScorecardDescriptor>();
        for (ScorecardDescriptor scorecard : scorecards) {
            if (scorecard.getDataSourceID() == dataSourceID) {
                matchedScorecards.add(scorecard);
            }
        }
        return matchedScorecards;
    }

    public Scorecard getScorecard(long scorecardID) {
        SecurityUtil.authorizeScorecard(scorecardID);
        try {
            return scorecardStorage.getScorecard(scorecardID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public ScorecardResults getValues(Scorecard scorecard, InsightRequestMetadata insightRequestMetadata) {
        EIConnection conn = Database.instance().getConnection();
        try {
            List<KPIOutcome> kpis = getValues(scorecard.getKpis(), conn, insightRequestMetadata);
            ScorecardResults scorecardResults = new ScorecardResults();
            scorecardResults.setOutcomes(kpis);
            return scorecardResults;
        } catch (ReportException re) {
            ScorecardResults scorecardResults = new ScorecardResults();
            scorecardResults.setReportFault(re.getReportFault());
            return scorecardResults;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public Scorecard saveScorecard(Scorecard scorecard) {
        long userID = SecurityUtil.getUserID();
        try {
            scorecardStorage.saveScorecardForUser(scorecard, userID);
            return scorecard;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void addKPIsToDefaultScorecard() {

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

    public KPI updateKPI(KPI kpi, InsightRequestMetadata insightRequestMetadata) {
        SecurityUtil.authorizeKPI(kpi.getKpiID());
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            new KPIStorage().saveKPI(kpi, conn);
            //refreshValuesForList(Arrays.asList(kpi), conn, insightRequestMetadata, false);
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
                kpi.getFilters(), kpi.getDayWindow(), insightRequestMetadata, conn);
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
        return new KPIOutcome(outcomeState, direction, oldValue, failedCondition, newValue, new Date(), kpi.getName(), percentChange, directional);
    }

    public List<KPIOutcome> getValues(List<KPI> kpis, EIConnection conn, InsightRequestMetadata insightRequestMetadata) throws Exception {
        /*if (allSources) {
            Set<Long> dataSourceIDs = new HashSet<Long>();
            for (KPI kpi : kpis) {
                dataSourceIDs.add(kpi.getCoreFeedID());
            }
            for (Long dataSourceID : dataSourceIDs) {
                FeedDefinition feedDefinition = new FeedStorage().getFeedDefinitionData(dataSourceID, conn);
                if (feedDefinition instanceof IServerDataSourceDefinition) {
                    IServerDataSourceDefinition dataSource = (IServerDataSourceDefinition) feedDefinition;
                    if (DataSourceMutex.mutex().lock(dataSource.getDataFeedID())) {
                        try {
                            dataSource.refreshData(SecurityUtil.getAccountID(), new Date(), null, null);
                        } finally {
                            DataSourceMutex.mutex().unlock(dataSource.getDataFeedID());
                        }
                    }
                }
            }
        }*/
        List<KPIOutcome> outcomes = new ArrayList<KPIOutcome>();
        for (KPI kpi : kpis) {
            KPIOutcome kpiValue = refreshKPIValue(kpi, conn, insightRequestMetadata);

            kpiValue.setKpiName(kpi.getName());
            outcomes.add(kpiValue);
            //kpi.setKpiValue(kpiValue);
            /*new KPIStorage().saveKPIOutcome(kpi.getKpiID(), kpiValue.getOutcomeValue(), kpiValue.getPreviousValue(),
                            kpiValue.getEvaluationDate(), kpiValue.getOutcomeState(), kpiValue.getDirection(), kpiValue.isProblemEvaluated(),
                    kpiValue.getPercentChange(), kpiValue.isDirectional(), conn);*/
        }
        return outcomes;
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
