package com.easyinsight.kpi;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.InsightRequestMetadata;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.logging.LogClass;
import com.easyinsight.pipeline.HistoryRun;
import com.easyinsight.scorecard.Scorecard;
import com.easyinsight.scorecard.ScorecardService;
import com.easyinsight.scorecard.ScorecardStorage;
import com.easyinsight.security.SecurityUtil;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * User: jamesboe
 * Date: Jan 21, 2010
 * Time: 11:35:48 AM
 */
public class KPIService {

    private KPIStorage kpiStorage = new KPIStorage();

    public KPI copyKPI(KPI sourceKPI) {
        EIConnection conn = Database.instance().getConnection();
        try {
            KPI kpi = sourceKPI.clone();
            kpi.setKpiOutcome(null);
            kpi.setKpiUsers(Arrays.asList(KPIUtil.defaultUser()));
            kpi.setName("Copy of " + kpi.getName());
            kpiStorage.saveKPI(kpi, conn);
            return kpi;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(false);
            Database.closeConnection(conn);
        }
    }

    /*public List<KPIValue> generateHistory(AnalysisMeasure analysisMeasure, List<FilterDefinition> filters, long dataSourceID, Date startDate, Date endDate,
                                           List<CredentialFulfillment> credentials) {
        try {
            return new HistoryRun().calculateHistoricalValues(dataSourceID, analysisMeasure, filters, startDate, endDate, credentials, null);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }*/

    public void installKPIsToScorecard(String dataSourceName, List<KPI> kpis, boolean newScorecard) {
        long userID = SecurityUtil.getUserID();
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            Scorecard scorecard = new Scorecard();
            scorecard.setName(dataSourceName + " Scorecard");
            scorecard.setKpis(kpis);
            new ScorecardStorage().saveScorecardForUser(scorecard, userID, conn);
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

    public List<KPIValue> generateHistory(AnalysisMeasure analysisMeasure, List<FilterDefinition> filters, long dataSourceID, Date startDate, Date endDate) {
        try {
            return new HistoryRun().calculateHistoricalValues(dataSourceID, analysisMeasure, filters, startDate, endDate);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<KPI> getKPIsForStart(long targetDataSourceID, InsightRequestMetadata insightRequestMetadata) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            List<KPI> kpis = new FeedStorage().getFeedDefinitionData(targetDataSourceID).createKPIs();

            for (KPI kpi : kpis) {
                kpi.setTemporary(true);
                kpi.setCoreFeedID(targetDataSourceID);
                kpiStorage.saveKPI(kpi, conn);
            }
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

    public KPI saveKPI(KPI kpi, InsightRequestMetadata insightRequestMetadata) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            kpiStorage.saveKPI(kpi, conn);
            new ScorecardService().getValues(Arrays.asList(kpi), conn, insightRequestMetadata);
            conn.commit();
            return kpi;
        } catch (Exception e) {
            conn.rollback();
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public List<KPI> getKPIsForDataSource(long dataSourceID) {
        try {
            return kpiStorage.getKPIsForDataSource(dataSourceID);
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
