package com.easyinsight.kpi;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.CredentialFulfillment;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.logging.LogClass;
import com.easyinsight.pipeline.HistoryRun;
import com.easyinsight.scorecard.Scorecard;
import com.easyinsight.scorecard.ScorecardService;
import com.easyinsight.scorecard.ScorecardStorage;
import com.easyinsight.security.SecurityUtil;

import java.util.Date;
import java.util.List;

/**
 * User: jamesboe
 * Date: Jan 21, 2010
 * Time: 11:35:48 AM
 */
public class KPIService {

    private KPIStorage kpiStorage = new KPIStorage();

    public KPI copyKPI(KPI sourceKPI, long scorecardID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            KPI kpi = sourceKPI.clone();
            kpi.setName("Copy of " + kpi.getName());
            kpiStorage.saveKPI(kpi, conn);
            if (scorecardID > 0) {
                new ScorecardStorage().addKPIToScorecard(kpi, scorecardID, conn);
            }
            return kpi;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(false);
            Database.closeConnection(conn);
        }
    }

    public List<KPIValue> generateHistory(AnalysisMeasure analysisMeasure, List<FilterDefinition> filters, long dataSourceID, Date startDate, Date endDate,
                                           List<CredentialFulfillment> credentials) {
        try {
            return new HistoryRun().calculateHistoricalValues(dataSourceID, analysisMeasure, filters, startDate, endDate, credentials);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void installKPIsToScorecard(String dataSourceName, List<KPI> kpis, boolean newScorecard) {
        long userID = SecurityUtil.getUserID();
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            Scorecard targetScorecard;
            if (newScorecard) {
                Scorecard scorecard = new Scorecard();
                scorecard.setName(dataSourceName + " Scorecard");
                scorecard.setScorecardOrder(0);
                new ScorecardStorage().saveScorecardForUser(scorecard, userID, conn);
                targetScorecard = scorecard;
            } else {
                List<Scorecard> scorecards = new ScorecardStorage().getScorecardsForUser(userID, conn);
                targetScorecard = scorecards.get(0);
            }
            for (KPI kpi : kpis) {
                kpi.setTemporary(false);
                new KPIStorage().saveKPI(kpi, conn);
                new ScorecardStorage().addKPIToScorecard(kpi, targetScorecard.getScorecardID(), conn);                
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

    public List<KPI> getKPIsForStart(long targetDataSourceID, List<CredentialFulfillment> credentials) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            List<KPI> kpis = new FeedStorage().getFeedDefinitionData(targetDataSourceID).createKPIs();

            for (KPI kpi : kpis) {
                kpi.setTemporary(true);
                //kpi.setConnectionID(connectionID);
                kpi.setCoreFeedID(targetDataSourceID);
                kpiStorage.saveKPI(kpi, conn);
            }
            new ScorecardService().refreshValuesForList(kpis, conn, credentials);
            conn.commit();
            return kpis;
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public KPI installKPIToConnection(long targetDataSourceID, long kpiID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            KPI kpi = kpiStorage.getKPI(kpiID, conn);
            KPI clonedKPI = kpi.clone();
            clonedKPI.setCoreFeedID(targetDataSourceID);
            kpiStorage.saveKPI(clonedKPI, conn);
            conn.commit();
            return clonedKPI;
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    private AnalysisItem findAnalysisItem(AnalysisItem analysisItem, FeedDefinition targetDataSource) {
        for (AnalysisItem testItem : targetDataSource.getFields()) {
            if (testItem.getKey().equals(analysisItem.getKey()) && testItem.getType() == analysisItem.getType()) {
                return testItem;
            }
        }
        throw new RuntimeException("Couldn't locate matching item for copy operation");
    }

    public List<KPI> getKPIs() {
        try {
            return kpiStorage.getKPIsForUser(SecurityUtil.getUserID());
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void saveKPI(KPI kpi) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            kpiStorage.saveKPI(kpi, conn);
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            LogClass.error(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public List<KPI> getKPIsForDataSource(long dataSourceID) {
        try {
            return kpiStorage.getKPIsForUser(dataSourceID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void deleteKPI(long kpiID) {
        try {
            kpiStorage.deleteKPI(kpiID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
