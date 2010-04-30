package com.easyinsight.scorecard;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.eventing.MessageUtils;
import com.easyinsight.eventing.EventDispatcher;
import com.easyinsight.kpi.KPI;
import com.easyinsight.kpi.KPIStorage;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Credentials;

import java.sql.*;
import java.util.*;
import java.util.Date;

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

    public void saveScorecardForGroup(Scorecard scorecard, long groupID) throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            saveScorecardForGroup(scorecard, groupID, conn);
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
            insertScorecardStmt.close();
        } else {
            PreparedStatement updateScorecardStmt = conn.prepareStatement("UPDATE SCORECARD SET SCORECARD_NAME = ?," +
                    "USER_ID = ?, SCORECARD_ORDER = ? WHERE SCORECARD_ID = ?");
            updateScorecardStmt.setString(1, scorecard.getName());
            updateScorecardStmt.setLong(2, userID);
            updateScorecardStmt.setInt(3, scorecard.getScorecardOrder());
            updateScorecardStmt.setLong(4, scorecard.getScorecardID());
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

    public void saveScorecardForGroup(Scorecard scorecard, long groupID, EIConnection conn) throws Exception {
        if (scorecard.getScorecardID() == 0) {
            PreparedStatement insertScorecardStmt = conn.prepareStatement("INSERT INTO SCORECARD (SCORECARD_NAME, GROUP_ID, SCORECARD_ORDER, USER_ID) " +
                    "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertScorecardStmt.setString(1, scorecard.getName());
            insertScorecardStmt.setLong(2, groupID);
            insertScorecardStmt.setInt(3, scorecard.getScorecardOrder());
            insertScorecardStmt.setNull(4, Types.BIGINT);
            insertScorecardStmt.execute();
            scorecard.setScorecardID(Database.instance().getAutoGenKey(insertScorecardStmt));
            insertScorecardStmt.close();
        } else {
            PreparedStatement updateScorecardStmt = conn.prepareStatement("UPDATE SCORECARD SET SCORECARD_NAME = ?," +
                    "GROUP_ID = ?, SCORECARD_ORDER = ? WHERE SCORECARD_ID = ?");
            updateScorecardStmt.setString(1, scorecard.getName());
            updateScorecardStmt.setLong(2, groupID);
            updateScorecardStmt.setInt(3, scorecard.getScorecardOrder());
            updateScorecardStmt.setLong(4, scorecard.getScorecardID());
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

    public ScorecardWrapper getScorecard(long scorecardID, List<CredentialFulfillment> credentials, boolean forceRefresh) throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            ScorecardWrapper scorecard = getScorecard(scorecardID, conn, credentials, forceRefresh);
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

    public ScorecardWrapper getScorecard(long scorecardID, EIConnection conn, List<CredentialFulfillment> credentials, boolean forceRefresh) throws Exception {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT SCORECARD_ID, SCORECARD_NAME FROM SCORECARD WHERE SCORECARD_ID = ?");
        queryStmt.setLong(1, scorecardID);
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            return loadScorecoard(rs, conn, credentials, forceRefresh);
        }
        return null;
    }

    private ScorecardWrapper updateScorecard(Scorecard scorecard, EIConnection conn, List<CredentialFulfillment> existingCredentials, boolean forceRefresh) throws Exception {
        List<KPI> longRefreshKPIs = new ArrayList<KPI>();
        List<KPI> shortRefreshKPIs = new ArrayList<KPI>();
        for (KPI kpi : scorecard.getKpis()) {
            if (forceRefresh || needsUpdate(kpi, conn)) {
                if (isLongRefresh(kpi, conn)) {
                    longRefreshKPIs.add(kpi);
                } else {
                    shortRefreshKPIs.add(kpi);
                }
            }
        }
        List<CredentialRequirement> credentialRequirements = new ArrayList<CredentialRequirement>();

        List<KPI> credentialedLongRefreshKPIs = pareKPIs(longRefreshKPIs, existingCredentials, credentialRequirements);
        List<KPI> credentialedShortRefreshKPIs = pareKPIs(shortRefreshKPIs, existingCredentials, credentialRequirements);

        ScorecardWrapper scorecardWrapper = new ScorecardWrapper();

        if (credentialRequirements.isEmpty()) {
            new ScorecardService().refreshValuesForList(credentialedShortRefreshKPIs, conn, existingCredentials, false);
            if (credentialedLongRefreshKPIs != null && credentialedLongRefreshKPIs.size() > 0) {
                scorecardWrapper.setAsyncRefresh(true);
                scorecardWrapper.setAsyncRefreshKpis(credentialedLongRefreshKPIs);
                longKPIs(credentialedLongRefreshKPIs, existingCredentials, scorecard.getScorecardID());
            }
        }

        scorecardWrapper.setCredentials(credentialRequirements);
        return scorecardWrapper;
    }

    public List<CredentialRequirement> blah(final long dataSourceID, final List<CredentialFulfillment> credentialsList) throws SQLException {
        List<CredentialRequirement> credentialRequirements = new ArrayList<CredentialRequirement>();
        FeedDefinition feedDefinition = new FeedStorage().getFeedDefinitionData(dataSourceID);
        if (feedDefinition.needsCredentials(credentialsList)) {
            credentialRequirements.add(new CredentialRequirement(feedDefinition.getDataFeedID(), feedDefinition.getFeedName(),
                        CredentialsDefinition.STANDARD_USERNAME_PW));
            return credentialRequirements;
        }

        final long userID = SecurityUtil.getUserID();
        final long accountID = SecurityUtil.getAccountID();
        final int accountType = SecurityUtil.getAccountTier();
        final boolean accountAdmin = SecurityUtil.isAccountAdmin();
        final String userName = SecurityUtil.getUserName();

        final Set<Long> dataSourceIDs = new HashSet<Long>();
        dataSourceIDs.add(dataSourceID);
        Thread thread = new Thread(new Runnable() {

            public void run() {
                SecurityUtil.populateThreadLocal(userName, userID, accountID, accountType, accountAdmin);
                try {
                    for (Long dataSourceID : dataSourceIDs) {
                        FeedDefinition feedDefinition = new FeedStorage().getFeedDefinitionData(dataSourceID);
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
                        DataSourceRefreshEvent info = new DataSourceRefreshEvent();
                        info.setDataSourceID(dataSourceID);
                        info.setDataSourceName(feedDefinition.getFeedName());
                        info.setType(DataSourceRefreshEvent.DATA_SOURCE_NAME);
                        info.setUserId(userID);
                        MessageUtils.sendMessage("generalNotifications", info);
                        if (DataSourceMutex.mutex().lock(dataSource.getDataFeedID())) {
                            try {
                            dataSource.refreshData(credentials, accountID, new Date(), null);
                            } finally {
                                DataSourceMutex.mutex().unlock(dataSource.getDataFeedID());    
                            }
                        }
                    }
                    DataSourceRefreshEvent info = new DataSourceRefreshEvent();
                    info.setDataSourceID(dataSourceID);
                    info.setType(DataSourceRefreshEvent.DONE);
                    info.setUserId(userID);
                    System.out.println("*** Sending done notification");
                    MessageUtils.sendMessage("generalNotifications", info);
                } catch (Exception e) {
                    LogClass.error(e);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        return credentialRequirements;
    }

    private void longKPIs(final List<KPI> kpiList, final List<CredentialFulfillment> credentialsList, final long scorecardID) {
        final Map<Long, List<KPI>> kpiMap = new HashMap<Long, List<KPI>>();
        for (KPI kpi : kpiList) {
            List<KPI> kpis = kpiMap.get(kpi.getCoreFeedID());
            if (kpis == null) {
                kpis = new ArrayList<KPI>();
                kpiMap.put(kpi.getCoreFeedID(), kpis);
            }
            kpis.add(kpi);
        }
        final long userID = SecurityUtil.getUserID();
        final long accountID = SecurityUtil.getAccountID();
        final int accountType = SecurityUtil.getAccountTier();
        final boolean accountAdmin = SecurityUtil.isAccountAdmin();
        final String userName = SecurityUtil.getUserName();

        Thread thread = new Thread(new Runnable() {

            public void run() {
                SecurityUtil.populateThreadLocal(userName, userID, accountID, accountType, accountAdmin);
                try {
                    for (Long dataSourceID : kpiMap.keySet()) {
                        FeedDefinition feedDefinition = new FeedStorage().getFeedDefinitionData(dataSourceID);
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
                        ScorecardRefreshEvent info = new ScorecardRefreshEvent();
                        info.setScorecardID(scorecardID);
                        info.setDataSourceName(feedDefinition.getFeedName());
                        info.setType(ScorecardRefreshEvent.DATA_SOURCE_NAME);
                        info.setUserId(userID);
                        MessageUtils.sendMessage("generalNotifications", info);
                        if (DataSourceMutex.mutex().lock(dataSource.getDataFeedID())) {
                            try {
                                dataSource.refreshData(credentials, accountID, new Date(), null);
                            } finally {
                                DataSourceMutex.mutex().unlock(dataSource.getDataFeedID());    
                            }
                        }
                    }
                    List<KPI> kpis;
                    EIConnection conn = Database.instance().getConnection();
                    try {
                        conn.setAutoCommit(false);
                        new ScorecardService().refreshValuesForList(kpiList, conn, credentialsList, false);
                        PreparedStatement getKPIStmt = conn.prepareStatement("SELECT SCORECARD_TO_KPI.KPI_ID FROM SCORECARD_TO_KPI WHERE " +
                                "scorecard_to_kpi.scorecard_id = ?");
                        getKPIStmt.setLong(1, scorecardID);

                        kpis = new ArrayList<KPI>();
                        ResultSet kpiRS = getKPIStmt.executeQuery();
                        while (kpiRS.next()) {
                            long kpiID = kpiRS.getLong(1);
                            kpis.add(new KPIStorage().getKPI(kpiID, conn));
                        }
                        conn.commit();
                    } catch (Exception e) {
                        conn.rollback();
                        throw new RuntimeException(e);
                    } finally {
                        conn.setAutoCommit(true);
                        Database.closeConnection(conn);
                    }
                    ScorecardRefreshEvent info = new ScorecardRefreshEvent();
                    info.setScorecardID(scorecardID);
                    info.setType(ScorecardRefreshEvent.DONE);
                    info.setKpis(kpis);
                    info.setUserId(userID);
                    LongKPIRefreshEvent event = new LongKPIRefreshEvent();
                    event.setEvent(info);
                    event.setSendDate(new Date());
                    EventDispatcher.instance().dispatch(event);
                    MessageUtils.sendMessage("generalNotifications", info);
                } catch (Exception e) {
                    LogClass.error(e);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public List<KPI> pareKPIs(List<KPI> kpiList, List<CredentialFulfillment> existingCredentials, List<CredentialRequirement> credentialRequirements) throws SQLException {
        List<KPI> credentialedKPIs = new ArrayList<KPI>();
        Map<Long, List<KPI>> kpiMap = new HashMap<Long, List<KPI>>();
        for (KPI kpi : kpiList) {
            List<KPI> kpis = kpiMap.get(kpi.getCoreFeedID());
            if (kpis == null) {
                kpis = new ArrayList<KPI>();
                kpiMap.put(kpi.getCoreFeedID(), kpis);
            }
            kpis.add(kpi);
        }
        for (Long dataSourceID : kpiMap.keySet()) {
            FeedDefinition feedDefinition = new FeedStorage().getFeedDefinitionData(dataSourceID);
            if (feedDefinition.needsCredentials(existingCredentials)) {
                credentialRequirements.add(new CredentialRequirement(feedDefinition.getDataFeedID(), feedDefinition.getFeedName(),
                            CredentialsDefinition.STANDARD_USERNAME_PW));
            } else {
                credentialedKPIs.addAll(kpiMap.get(dataSourceID));
            }            
        }
        return credentialedKPIs;
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
            threshold = 1000 * 60 * 60;
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

    private ScorecardWrapper loadScorecoard(ResultSet rs, EIConnection conn, List<CredentialFulfillment> credentials, boolean forceRefresh) throws Exception {
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
        getKPIStmt.close();
        scorecard.setKpis(kpis);
        ScorecardWrapper scorecardWrapper = updateScorecard(scorecard, conn, credentials, forceRefresh);
        scorecardWrapper.setScorecard(scorecard);
        return scorecardWrapper;
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
