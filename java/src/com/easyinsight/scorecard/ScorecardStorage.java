package com.easyinsight.scorecard;

import com.easyinsight.analysis.InsightRequestMetadata;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.kpi.KPI;
import com.easyinsight.kpi.KPIStorage;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.util.RandomTextGenerator;

import java.sql.*;
import java.util.*;

/**
 * User: jamesboe
 * Date: Jan 18, 2010
 * Time: 3:46:42 PM
 */
public class ScorecardStorage {
    public void saveScorecardForUser(Scorecard scorecard, long userID) throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            saveScorecardForUser(scorecard, userID, conn);
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public void saveScorecardForUser(Scorecard scorecard, long userID, EIConnection conn) throws Exception {
        if (scorecard.getUrlKey() == null) {
            scorecard.setUrlKey(RandomTextGenerator.generateText(20));
        }
        if (scorecard.getScorecardID() == 0) {
            PreparedStatement insertScorecardStmt = conn.prepareStatement("INSERT INTO SCORECARD (SCORECARD_NAME, USER_ID, URL_KEY, ACCOUNT_VISIBLE," +
                    "EXCHANGE_VISIBLE, DESCRIPTION, CREATION_DATE, UPDATE_DATE) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertScorecardStmt.setString(1, scorecard.getName());
            insertScorecardStmt.setLong(2, userID);
            insertScorecardStmt.setString(3, scorecard.getUrlKey());
            insertScorecardStmt.setBoolean(4, scorecard.isAccountVisible());
            insertScorecardStmt.setBoolean(5, scorecard.isExchangeVisible());
            insertScorecardStmt.setString(6, scorecard.getDescription());
            insertScorecardStmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            insertScorecardStmt.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
            insertScorecardStmt.execute();
            scorecard.setScorecardID(Database.instance().getAutoGenKey(insertScorecardStmt));
            insertScorecardStmt.close();
        } else {
            PreparedStatement updateScorecardStmt = conn.prepareStatement("UPDATE SCORECARD SET SCORECARD_NAME = ?," +
                    "USER_ID = ?, URL_KEY = ?, ACCOUNT_VISIBLE = ?, EXCHANGE_VISIBLE = ?, DESCRIPTION = ?, UPDATE_DATE = ? WHERE SCORECARD_ID = ?");
            updateScorecardStmt.setString(1, scorecard.getName());
            updateScorecardStmt.setLong(2, userID);
            updateScorecardStmt.setString(3, scorecard.getUrlKey());
            updateScorecardStmt.setBoolean(4, scorecard.isAccountVisible());
            updateScorecardStmt.setBoolean(5, scorecard.isExchangeVisible());
            updateScorecardStmt.setString(6, scorecard.getDescription());
            updateScorecardStmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            updateScorecardStmt.setLong(8, scorecard.getScorecardID());
            updateScorecardStmt.executeUpdate();
            updateScorecardStmt.close();
        }
        PreparedStatement clearKPILinksStmt = conn.prepareStatement("DELETE FROM SCORECARD_TO_KPI WHERE SCORECARD_ID = ?");
        clearKPILinksStmt.setLong(1, scorecard.getScorecardID());
        clearKPILinksStmt.executeUpdate();
        clearKPILinksStmt.close();
        PreparedStatement addLinkStmt = conn.prepareStatement("INSERT INTO SCORECARD_TO_KPI (SCORECARD_ID, KPI_ID) VALUES (?, ?)");
        for (KPI kpi : scorecard.getKpis()) {
            new KPIStorage().saveKPI(kpi, conn);
            addLinkStmt.setLong(1, scorecard.getScorecardID());
            addLinkStmt.setLong(2, kpi.getKpiID());
            addLinkStmt.execute();
        }
        addLinkStmt.close();
    }

    public void deleteScorecard(long scorecardID) throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM SCORECARD WHERE SCORECARD_ID = ?");
            deleteStmt.setLong(1, scorecardID);
            deleteStmt.executeUpdate();
            deleteStmt.close();
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public Scorecard getScorecard(long scorecardID) throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            Scorecard scorecard = getScorecard(scorecardID, conn);
            conn.commit();
            return scorecard;
        } catch (Exception e) {
            conn.rollback();
            LogClass.error(e);
            throw e;
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public Scorecard getScorecard(long scorecardID, EIConnection conn) throws Exception {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT SCORECARD_ID, SCORECARD_NAME, URL_KEY, ACCOUNT_VISIBLE, EXCHANGE_VISIBLE, DESCRIPTION FROM " +
                "SCORECARD WHERE SCORECARD_ID = ?");
        queryStmt.setLong(1, scorecardID);
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            Scorecard scorecard = new Scorecard();
            scorecard.setScorecardID(rs.getLong(1));
            scorecard.setName(rs.getString(2));
            String urlKey = rs.getString(3);
            if (urlKey == null) {
                urlKey = RandomTextGenerator.generateText(20);
                PreparedStatement saveStmt = conn.prepareStatement("UPDATE SCORECARD SET URL_KEY = ? WHERE SCORECARD_ID = ?");
                saveStmt.setString(1, urlKey);
                saveStmt.setLong(2, scorecard.getScorecardID());
                saveStmt.executeUpdate();
            }
            scorecard.setUrlKey(urlKey);
            scorecard.setAccountVisible(rs.getBoolean(4));
            scorecard.setExchangeVisible(rs.getBoolean(5));
            scorecard.setDescription(rs.getString(6));
            PreparedStatement getKPIStmt = conn.prepareStatement("SELECT SCORECARD_TO_KPI.KPI_ID FROM SCORECARD_TO_KPI WHERE " +
                    "scorecard_to_kpi.scorecard_id = ?");
            getKPIStmt.setLong(1, scorecard.getScorecardID());
            List<KPI> kpis = new ArrayList<KPI>();
            ResultSet kpiRS = getKPIStmt.executeQuery();
            while (kpiRS.next()) {
                long kpiID = kpiRS.getLong(1);
                kpis.add(new KPIStorage().getKPI(kpiID, conn));
            }
            getKPIStmt.close();
            scorecard.setKpis(kpis);
            return scorecard;
        }
        return null;
    }

    public boolean needsUpdate(KPI kpi, EIConnection conn) throws SQLException {
        long threshold;
        long kpiTime = 0;
        if (isLongRefresh(kpi, conn)) {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT FEED_PERSISTENCE_METADATA.SIZE, FEED_PERSISTENCE_METADATA.last_data_time FROM feed_persistence_metadata WHERE feed_id = ?");
            queryStmt.setLong(1, kpi.getCoreFeedID());
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                long size = rs.getLong(1);
                if (size == 0) {
                    PreparedStatement detailStmt = conn.prepareStatement("SELECT FEED_PERSISTENCE_METADATA.SIZE, FEED_PERSISTENCE_METADATA.last_data_time FROM " +
                            "feed_persistence_metadata, data_feed WHERE feed_persistence_metadata.feed_id = data_feed.data_feed_id AND " +
                            "data_feed.parent_source_id = ?");
                    detailStmt.setLong(1, kpi.getCoreFeedID());
                    rs = detailStmt.executeQuery();
                    if (rs.next()) {
                        kpiTime = rs.getTimestamp(2).getTime();
                    }
                    detailStmt.close();
                } else {
                    kpiTime = rs.getTimestamp(2).getTime();
                }
            }
            queryStmt.close();
            threshold = 1000 * 60 * 60 * 12;
        } else {
            threshold = 1000 * 60 * 60 * 12;
        }
        long time = System.currentTimeMillis() - threshold;
        if (kpiTime == 0 && kpi.getKpiOutcome() != null) {
            kpiTime = kpi.getKpiOutcome().getEvaluationDate().getTime();
        }
        return (kpiTime < time);
    }

    public boolean isLongRefresh(KPI kpi, EIConnection conn) throws SQLException {
        long dataSourceID = kpi.getCoreFeedID();
        FeedDefinition feedDefinition = new FeedStorage().getFeedDefinitionData(dataSourceID, conn);
        return feedDefinition.isLongRefresh();
    }

    public List<Scorecard> getScorecardsFromGroup(long userID) throws Exception {
        List<Scorecard> scorecards = new ArrayList<Scorecard>();
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);

            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        return scorecards;
    }

    public long addKPIToScorecard(KPI kpi, long scorecardID) throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            addKPIToScorecard(kpi, scorecardID, conn);
            conn.commit();
            return kpi.getKpiID();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public void addKPIToScorecard(KPI kpi, long scorecardID, EIConnection conn) throws Exception {
        new KPIStorage().saveKPI(kpi, conn);
        linkKPIToScorecard(kpi, scorecardID, conn);     
    }

    public void linkKPIToScorecard(KPI kpi, long scorecardID, EIConnection conn) throws Exception {
        PreparedStatement addLinkStmt = conn.prepareStatement("INSERT INTO SCORECARD_TO_KPI (SCORECARD_ID, KPI_ID) VALUES (?, ?)");
        
        addLinkStmt.setLong(1, scorecardID);
        addLinkStmt.setLong(2, kpi.getKpiID());
        addLinkStmt.execute();
        addLinkStmt.close();
    }

    public void removeKPIFromScorecard(long kpiID, long scorecardID) throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement deleteLinkStmt = conn.prepareStatement("DELETE FROM SCORECARD_TO_KPI WHERE SCORECARD_ID = ? AND KPI_ID = ?");
            deleteLinkStmt.setLong(1, scorecardID);
            deleteLinkStmt.setLong(2, kpiID);
            deleteLinkStmt.executeUpdate();
            deleteLinkStmt.close();
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public void updateKPI(KPI kpi) throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            new KPIStorage().saveKPI(kpi, conn);
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public long getFirstScorecard(EIConnection conn) throws SQLException {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT SCORECARD.SCORECARD_ID FROM SCORECARD, USER_SCORECARD_ORDERING WHERE " +
                "SCORECARD.SCORECARD_ID = USER_SCORECARD_ORDERING.SCORECARD_ID AND SCORECARD.USER_ID = ? ORDER BY USER_SCORECARD_ORDERING.SCORECARD_ORDER ASC LIMIT 1");
        queryStmt.setLong(1, SecurityUtil.getUserID());
        ResultSet rs = queryStmt.executeQuery();
        long id;
        if (rs.next()) {
            id = rs.getLong(1);
        } else {
            queryStmt.close();
            queryStmt = conn.prepareStatement("SELECT SCORECARD.SCORECARD_ID FROM SCORECARD WHERE SCORECARD.USER_ID = ? LIMIT 1");
            queryStmt.setLong(1, SecurityUtil.getUserID());
            rs = queryStmt.executeQuery();
            rs.next();
            id = rs.getLong(1);
        }
        queryStmt.close();
        return id;
    }
}
