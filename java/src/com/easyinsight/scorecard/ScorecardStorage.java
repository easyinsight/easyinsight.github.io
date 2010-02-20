package com.easyinsight.scorecard;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.kpi.KPI;
import com.easyinsight.kpi.KPIStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
        if (scorecard.getScorecardID() == 0) {
            PreparedStatement insertScorecardStmt = conn.prepareStatement("INSERT INTO SCORECARD (SCORECARD_NAME, USER_ID, SCORECARD_ORDER) " +
                    "VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertScorecardStmt.setString(1, scorecard.getName());
            insertScorecardStmt.setLong(2, userID);
            insertScorecardStmt.setInt(3, scorecard.getScorecardOrder());
            insertScorecardStmt.execute();
            scorecard.setScorecardID(Database.instance().getAutoGenKey(insertScorecardStmt));
        } else {
            PreparedStatement updateScorecardStmt = conn.prepareStatement("UPDATE SCORECARD SET SCORECARD_NAME = ?," +
                    "USER_ID = ?, SCORECARD_ORDER = ? WHERE SCORECARD_ID = ?");
            updateScorecardStmt.setString(1, scorecard.getName());
            updateScorecardStmt.setLong(2, userID);
            updateScorecardStmt.setInt(3, scorecard.getScorecardOrder());
            updateScorecardStmt.setLong(4, scorecard.getScorecardID());
            updateScorecardStmt.executeUpdate();
        }
        PreparedStatement clearKPILinksStmt = conn.prepareStatement("DELETE FROM SCORECARD_TO_KPI WHERE SCORECARD_ID = ?");
        clearKPILinksStmt.setLong(1, scorecard.getScorecardID());
        clearKPILinksStmt.executeUpdate();
        PreparedStatement addLinkStmt = conn.prepareStatement("INSERT INTO SCORECARD_TO_KPI (SCORECARD_ID, KPI_ID) VALUES (?, ?)");
        for (KPI kpi : scorecard.getKpis()) {
            new KPIStorage().saveKPI(kpi, conn);
            addLinkStmt.setLong(1, scorecard.getScorecardID());
            addLinkStmt.setLong(2, kpi.getKpiID());
            addLinkStmt.execute();
        }
    }


    public void deleteScorecard(long scorecardID) throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM SCORECARD WHERE SCORECARD_ID = ?");
            deleteStmt.setLong(1, scorecardID);
            deleteStmt.executeUpdate();
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
            throw e;
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public Scorecard getScorecard(long scorecardID, EIConnection conn) throws Exception {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT SCORECARD_ID, SCORECARD_NAME FROM SCORECARD WHERE SCORECARD_ID = ?");
        queryStmt.setLong(1, scorecardID);
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            return loadScorecoard(rs, conn);
        }
        return null;
    }

    public List<Scorecard> getScorecardsForUser(long userID) throws Exception {
        List<Scorecard> scorecards = new ArrayList<Scorecard>();
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement queryStmt = conn.prepareStatement("SELECT SCORECARD_ID, SCORECARD_NAME FROM SCORECARD WHERE USER_ID = ? ORDER BY scorecard_order");
            queryStmt.setLong(1, userID);
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                scorecards.add(loadScorecoard(rs, conn));
            }
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

    public List<Scorecard> getScorecardsForUser(long userID, EIConnection conn) throws Exception {
        List<Scorecard> scorecards = new ArrayList<Scorecard>();
        PreparedStatement queryStmt = conn.prepareStatement("SELECT SCORECARD_ID, SCORECARD_NAME FROM SCORECARD WHERE USER_ID = ? ORDER BY scorecard_order");
            queryStmt.setLong(1, userID);
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                scorecards.add(loadScorecoard(rs, conn));
            }
        return scorecards;
    }

    private Scorecard loadScorecoard(ResultSet rs, EIConnection conn) throws Exception {
        Scorecard scorecard = new Scorecard();
        scorecard.setScorecardID(rs.getLong(1));
        scorecard.setName(rs.getString(2));
        PreparedStatement getKPIStmt = conn.prepareStatement("SELECT SCORECARD_TO_KPI.KPI_ID FROM SCORECARD_TO_KPI WHERE " +
                "scorecard_to_kpi.scorecard_id = ?");
        getKPIStmt.setLong(1, scorecard.getScorecardID());
        List<KPI> kpis = new ArrayList<KPI>();
        ResultSet kpiRS = getKPIStmt.executeQuery();
        while (kpiRS.next()) {
            long kpiID = kpiRS.getLong(1);
            kpis.add(new KPIStorage().getKPI(kpiID, conn));
        }
        scorecard.setKpis(kpis);
        return scorecard;
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
    }

    public void removeKPIFromScorecard(long kpiID, long scorecardID) throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement deleteLinkStmt = conn.prepareStatement("DELETE FROM SCORECARD_TO_KPI WHERE SCORECARD_ID = ? AND KPI_ID = ?");
            deleteLinkStmt.setLong(1, scorecardID);
            deleteLinkStmt.setLong(2, kpiID);
            deleteLinkStmt.executeUpdate();
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
}
