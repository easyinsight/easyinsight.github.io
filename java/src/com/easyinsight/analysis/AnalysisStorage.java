package com.easyinsight.analysis;

import com.easyinsight.cache.MemCachedManager;
import com.easyinsight.calculations.FunctionFactory;
import com.easyinsight.core.*;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import com.easyinsight.datafeeds.*;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.Roles;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.util.RandomTextGenerator;
import org.hibernate.Session;
import org.hibernate.exception.LockAcquisitionException;

/**
 * User: James Boe
 * Date: Jan 10, 2008
 * Time: 6:37:10 PM
 */
public class AnalysisStorage {

    public ReportMetrics getRating(long analysisID, EIConnection conn) throws SQLException {
        double ratingAverage = 0;
        int ratingCount = 0;
        int myRating = 0;
        PreparedStatement queryStmt = conn.prepareStatement("SELECT AVG(RATING), COUNT(RATING) FROM USER_REPORT_RATING WHERE " +
                "REPORT_ID = ?");
        queryStmt.setLong(1, analysisID);
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            ratingAverage = rs.getDouble(1);
            ratingCount = rs.getInt(2);
        }
        queryStmt.close();
        PreparedStatement myRatingStmt = conn.prepareStatement("SELECT RATING FROM USER_REPORT_RATING WHERE USER_ID = ? AND " +
                "REPORT_ID = ?");
        myRatingStmt.setLong(1, SecurityUtil.getUserID());
        myRatingStmt.setLong(2, analysisID);
        ResultSet myRS = myRatingStmt.executeQuery();
        if (myRS.next()) {
            myRating = myRS.getInt(1);
        }
        myRatingStmt.close();
        return new ReportMetrics(ratingCount, ratingAverage, myRating);
    }

    private WSAnalysisDefinition fromCache(long reportID) {
        try {
            byte[] bytes = (byte[]) MemCachedManager.getReport(reportID);
            if (bytes != null) {
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
                return (WSAnalysisDefinition) ois.readObject();
            }
        } catch (Exception e) {
            LogClass.error(e);
        }
        return null;
    }

    public WSAnalysisDefinition getAnalysisDefinition(long analysisID) {
        WSAnalysisDefinition analysisDefinition = fromCache(analysisID);
        if (analysisDefinition != null) return analysisDefinition;
        int retries = 0;
        while (retries < MAX_RETRIES && analysisDefinition == null) {
            retries++;
            Session session = Database.instance().createSession();
            try {
                session.beginTransaction();
                List results = session.createQuery("from AnalysisDefinition where analysisID = ?").setLong(0, analysisID).list();
                if (results.size() > 0) {
                    AnalysisDefinition savedReport = (AnalysisDefinition) results.get(0);
                    savedReport.validate();
                    analysisDefinition = savedReport.createBlazeDefinition();
                }
                InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
                if (analysisDefinition.getMarmotScript() != null) {
                    StringTokenizer toker = new StringTokenizer(analysisDefinition.getMarmotScript(), "\r\n");
                    while (toker.hasMoreTokens()) {
                        String line = toker.nextToken();
                        if (FunctionFactory.functionRunsOnReportLoad(line)) {
                            try {
                                new ReportCalculation(line).apply(analysisDefinition, new ArrayList<AnalysisItem>(), new HashMap<String, List<AnalysisItem>>(),
                                    new HashMap<String, List<AnalysisItem>>(), new HashMap<String, List<AnalysisItem>>(), null, null, new ArrayList<FilterDefinition>(), insightRequestMetadata);
                            } catch (Exception e) {
                                LogClass.error(e);
                            }
                        }
                    }
                }
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                if (e instanceof LockAcquisitionException && retries < MAX_RETRIES) {
                    // lock acquisition exception, ignore
                } else {
                    throw new RuntimeException(e);
                }
            } finally {
                session.close();
            }
        }
        cacheReport(analysisDefinition);
        return analysisDefinition;
    }

    public AnalysisDefinition getPersistableReport(long analysisID) {
        AnalysisDefinition analysisDefinition = null;
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            List results = session.createQuery("from AnalysisDefinition where analysisID = ?").setLong(0, analysisID).list();
            if (results.size() > 0) {
                analysisDefinition = (AnalysisDefinition) results.get(0);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        return analysisDefinition;
    }

    public static final int MAX_RETRIES = 5;

    public WSAnalysisDefinition getAnalysisDefinition(long analysisID, Connection conn) {
        WSAnalysisDefinition analysisDefinition = fromCache(analysisID);
        if (analysisDefinition != null) return analysisDefinition;
        int retries = 0;
        while (retries < MAX_RETRIES && analysisDefinition == null) {
            retries++;
            Session session = Database.instance().createSession(conn);
            try {

                List results = session.createQuery("from AnalysisDefinition where analysisID = ?").setLong(0, analysisID).list();
                if (results.size() > 0) {
                    AnalysisDefinition savedReport = (AnalysisDefinition) results.get(0);
                    savedReport.validate();
                    analysisDefinition = savedReport.createBlazeDefinition();
                }
                InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
                if (analysisDefinition.getMarmotScript() != null) {
                    StringTokenizer toker = new StringTokenizer(analysisDefinition.getMarmotScript(), "\r\n");
                    while (toker.hasMoreTokens()) {
                        String line = toker.nextToken();
                        if (FunctionFactory.functionRunsOnReportLoad(line)) {
                            try {
                                new ReportCalculation(line).apply(analysisDefinition, new ArrayList<AnalysisItem>(), new HashMap<String, List<AnalysisItem>>(),
                                        new HashMap<String, List<AnalysisItem>>(), new HashMap<String, List<AnalysisItem>>(),
                                        null, null, new ArrayList<FilterDefinition>(), insightRequestMetadata);
                            } catch (Exception e) {
                                LogClass.error(e);
                            }
                        }
                    }
                }

            } catch (Exception e) {
                if (e instanceof LockAcquisitionException && retries < MAX_RETRIES) {
                    // lock acquisition exception, ignore
                } else {
                    throw new RuntimeException(e);
                }
            } finally {
                session.close();
            }
        }
        cacheReport(analysisDefinition);
        return analysisDefinition;
    }

    public WSAnalysisDefinition getAnalysisDefinition(long analysisID, Connection conn, boolean noCache) {
        WSAnalysisDefinition analysisDefinition;
        if (!noCache) {
            analysisDefinition = fromCache(analysisID);
        } else {
            analysisDefinition = null;
        }

        if (analysisDefinition != null) return analysisDefinition;
        int retries = 0;
        while (retries < MAX_RETRIES && analysisDefinition == null) {
            retries++;
            Session session = Database.instance().createSession(conn);
            try {
                session.beginTransaction();
                List results = session.createQuery("from AnalysisDefinition where analysisID = ?").setLong(0, analysisID).list();
                if (results.size() > 0) {
                    AnalysisDefinition savedReport = (AnalysisDefinition) results.get(0);
                    savedReport.validate();
                    analysisDefinition = savedReport.createBlazeDefinition();
                }
                InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
                if (analysisDefinition.getMarmotScript() != null) {
                    StringTokenizer toker = new StringTokenizer(analysisDefinition.getMarmotScript(), "\r\n");
                    while (toker.hasMoreTokens()) {
                        String line = toker.nextToken();
                        if (FunctionFactory.functionRunsOnReportLoad(line)) {
                            try {
                                new ReportCalculation(line).apply(analysisDefinition, new ArrayList<AnalysisItem>(), new HashMap<String, List<AnalysisItem>>(),
                                        new HashMap<String, List<AnalysisItem>>(), new HashMap<String, List<AnalysisItem>>(),
                                        null, null, new ArrayList<FilterDefinition>(), insightRequestMetadata);
                            } catch (Exception e) {
                                LogClass.error(e);
                            }
                        }
                    }
                }
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                if (e instanceof LockAcquisitionException && retries < MAX_RETRIES) {
                    // lock acquisition exception, ignore
                } else {
                    throw new RuntimeException(e);
                }
            } finally {
                session.close();
            }
        }
        cacheReport(analysisDefinition);
        return analysisDefinition;
    }

    private void cacheReport(WSAnalysisDefinition analysisDefinition) {
        if (analysisDefinition != null) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(analysisDefinition);
                oos.flush();
                MemCachedManager.addReport(analysisDefinition.getAnalysisID(), baos.toByteArray());
            } catch (Exception e) {
                LogClass.error(e);
            }
        }
    }

    public AnalysisDefinition getPersistableReport(long analysisID, Connection conn) {
        AnalysisDefinition analysisDefinition = null;
        Session session = Database.instance().createSession(conn);
        try {
            List results = session.createQuery("from AnalysisDefinition where analysisID = ?").setLong(0, analysisID).list();
            if (results.size() > 0) {
                analysisDefinition = (AnalysisDefinition) results.get(0);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        return analysisDefinition;
    }

    public AnalysisDefinition getPersistableReport(long analysisID, Session session) {
        AnalysisDefinition analysisDefinition = null;
        List results = session.createQuery("from AnalysisDefinition where analysisID = ?").setLong(0, analysisID).list();
        if (results.size() > 0) {
            analysisDefinition = (AnalysisDefinition) results.get(0);
        }
        return analysisDefinition;
    }

    public void saveAnalysis(AnalysisDefinition analysisDefinition, Connection conn) {
        Session session = Database.instance().createSession(conn);
        try {
            saveAnalysis(analysisDefinition, session);
        } finally {
            session.close();
        }
    }

    public void clearCache(long reportID, long dataSourceID) {
        MemCachedManager.deleteReport(reportID);
        try {
            ReportCache.instance().flushResults(dataSourceID);
        } catch (Exception e) {
            LogClass.error(e);
        }
    }

    public void saveAnalysis(AnalysisDefinition analysisDefinition, Session session) {
        if (analysisDefinition.getAnalysisID() != null) {
            clearCache(analysisDefinition.getAnalysisID(), analysisDefinition.getDataFeedID());
        }
        if (analysisDefinition.getAnalysisID() != null && analysisDefinition.getAnalysisID() == 0) {
            analysisDefinition.setAnalysisID(null);
        }
        if (analysisDefinition.getUrlKey() == null) {
            analysisDefinition.setUrlKey(RandomTextGenerator.generateText(20));
        }
        if (analysisDefinition.getDateCreated() == null) {
            analysisDefinition.setDateCreated(new Date());
        }
        analysisDefinition.setDateUpdated(new Date());
        if (analysisDefinition.getFilterDefinitions() != null) {
            for (FilterDefinition filter : analysisDefinition.getFilterDefinitions()) {                
                filter.beforeSave(session);
            }
        }
        if (analysisDefinition.getAddedItems() != null) {
            for (AnalysisItem analysisItem : analysisDefinition.getAddedItems()) {
                analysisItem.reportSave(session);

            }
        }
        if (analysisDefinition.getJoinOverrides() != null) {
            for (JoinOverride joinOverride : analysisDefinition.getJoinOverrides()) {
                joinOverride.reportSave(session);
            }
        }
        if (analysisDefinition.getReportStructure() != null) {
            for (AnalysisItem analysisItem : analysisDefinition.getReportStructure().values()) {
                analysisItem.reportSave(session);
                if (analysisItem.getKey().getKeyID() == 0) {
                    session.save(analysisItem.getKey());
                }
                if (analysisItem.getAnalysisItemID() == 0) {
                    session.save(analysisItem);
                } else {
                    session.update(analysisItem);
                }
            }
        }
        analysisDefinition.getAnalysisDefinitionState().beforeSave(session);
        if (analysisDefinition.getAnalysisID() == null)
            session.save(analysisDefinition);
        else
            session.update(analysisDefinition);
        session.flush();
    }

    public void saveAnalysis(AnalysisDefinition analysisDefinition) {

        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            saveAnalysis(analysisDefinition, session);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public RolePrioritySet<InsightDescriptor> getReports(long userID, long accountID, EIConnection conn, boolean testAccountVisible) throws SQLException {
        RolePrioritySet<InsightDescriptor> descriptors = new RolePrioritySet<InsightDescriptor>();
        PreparedStatement ownerStmt = conn.prepareStatement("SELECT user.first_name, user.name from user, user_to_analysis where " +
                "user.user_id = user_to_analysis.user_id and user_to_analysis.analysis_id = ? and user_to_analysis.relationship_type = ?");
        PreparedStatement queryStmt = conn.prepareStatement("SELECT analysis.ANALYSIS_ID, TITLE, DATA_FEED_ID, REPORT_TYPE, URL_KEY, " +
                "ANALYSIS.create_date, analysis.account_visible, analysis.folder, analysis.description, analysis.update_date FROM ANALYSIS, USER_TO_ANALYSIS WHERE " +
                "USER_TO_ANALYSIS.analysis_id = analysis.analysis_id and user_to_analysis.user_id = ? AND temporary_report = ?");
        PreparedStatement userStmt = conn.prepareStatement("SELECT USER.FIRST_NAME, USER.NAME FROM USER WHERE USER_ID = ?");
        userStmt.setLong(1, userID);
        ResultSet nameRS = userStmt.executeQuery();
        nameRS.next();
        String firstName = nameRS.getString(1);
        String lastName = nameRS.getString(2);
        String name = firstName != null ? firstName + " " + lastName : lastName;
        userStmt.close();
        queryStmt.setLong(1, userID);
        queryStmt.setBoolean(2, false);
        Set<Long> reportIDs = new HashSet<Long>();
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            long reportID = rs.getLong(1);
            reportIDs.add(reportID);
            InsightDescriptor descriptor = new InsightDescriptor(reportID, rs.getString(2), rs.getLong(3), rs.getInt(4), rs.getString(5), new Date(rs.getTimestamp(6).getTime()), name, Roles.OWNER,
                    rs.getBoolean(7), rs.getInt(8), rs.getString(9), new Date(rs.getTimestamp(10).getTime()));
            descriptors.add(descriptor);
        }
        queryStmt.close();
        if (testAccountVisible) {
            PreparedStatement queryAccountStmt = conn.prepareStatement("SELECT analysis.ANALYSIS_ID, analysis.TITLE, DATA_FEED_ID, REPORT_TYPE, URL_KEY, create_date, " +
                    "account_visible, analysis.folder, analysis.description, analysis.update_date FROM ANALYSIS, USER_TO_ANALYSIS, USER WHERE " +
                    "USER_TO_ANALYSIS.analysis_id = analysis.analysis_id and user_to_analysis.user_id = user.user_id and user.account_id = ? and analysis.account_visible = ? and temporary_report = ?");
            queryAccountStmt.setLong(1, accountID);
            queryAccountStmt.setBoolean(2, true);
            queryAccountStmt.setBoolean(3, false);
            ResultSet accountRS = queryAccountStmt.executeQuery();
            while (accountRS.next()) {
                long reportID = accountRS.getLong(1);
                if (reportIDs.contains(reportID)) {
                    continue;
                }
                ownerStmt.setLong(1, accountRS.getLong(1));
                ownerStmt.setInt(2, Roles.OWNER);
                ResultSet ownerRS = ownerStmt.executeQuery();
                String ownerName;
                if (ownerRS.next()) {
                    String ownerFirstName = ownerRS.getString(1);
                    String ownerLastName = ownerRS.getString(2);
                    ownerName = ownerFirstName != null ? ownerFirstName + " " + ownerLastName : ownerLastName;
                } else {
                    ownerName = "";
                }
                descriptors.add(new InsightDescriptor(reportID, accountRS.getString(2), accountRS.getLong(3), accountRS.getInt(4), accountRS.getString(5),
                        new Date(accountRS.getTimestamp(6).getTime()), ownerName, Roles.SHARER, accountRS.getBoolean(7), accountRS.getInt(8), accountRS.getString(9),
                        new Date(accountRS.getTimestamp(10).getTime())));
            }
            queryAccountStmt.close();
        }
        PreparedStatement userGroupStmt = conn.prepareStatement("SELECT analysis.ANALYSIS_ID, analysis.TITLE, DATA_FEED_ID, REPORT_TYPE, URL_KEY, " +
                "group_to_user_join.binding_type, create_date, account_visible, folder, analysis.description, analysis.update_date FROM ANALYSIS, group_to_user_join," +
                "group_to_insight WHERE " +
                "analysis.analysis_id = group_to_insight.insight_id and group_to_insight.group_id = group_to_user_join.group_id and group_to_user_join.user_id = ? and analysis.temporary_report = ?");
        userGroupStmt.setLong(1, userID);
        userGroupStmt.setBoolean(2, false);
        ResultSet groupRS = userGroupStmt.executeQuery();
        while (groupRS.next()) {
            ownerStmt.setLong(1, groupRS.getLong(1));
            ownerStmt.setInt(2, Roles.OWNER);
            ResultSet ownerRS = ownerStmt.executeQuery();
            String ownerName;
            if (ownerRS.next()) {
                String ownerFirstName = ownerRS.getString(1);
                String ownerLastName = ownerRS.getString(2);
                ownerName = ownerFirstName != null ? ownerFirstName + " " + ownerLastName : ownerLastName;
            } else {
                ownerName = "";
            }
            descriptors.add(new InsightDescriptor(groupRS.getLong(1), groupRS.getString(2), groupRS.getLong(3), groupRS.getInt(4), groupRS.getString(5),
                    new Date(groupRS.getTimestamp(7).getTime()), ownerName, groupRS.getInt(6), groupRS.getBoolean(8), groupRS.getInt(9), groupRS.getString(10),
                    new Date(groupRS.getTimestamp(11).getTime())));
        }
        userGroupStmt.close();
        PreparedStatement lastChanceGroupStmt = conn.prepareStatement("SELECT analysis.ANALYSIS_ID, analysis.TITLE, DATA_FEED_ID, REPORT_TYPE, ANALYSIS.URL_KEY, " +
                "group_to_user_join.binding_type, create_date, account_visible, folder, analysis.description, analysis.update_date FROM ANALYSIS, group_to_user_join," +
                "community_group, upload_policy_groups WHERE " +
                "analysis.data_feed_id = upload_policy_groups.feed_id AND upload_policy_groups.group_id = community_group.community_group_id AND " +
                "community_group.data_source_include_report = ? AND community_group.community_group_id = group_to_user_join.group_id and group_to_user_join.user_id = ? " +
                "and analysis.temporary_report = ?");
        lastChanceGroupStmt.setBoolean(1, true);
        lastChanceGroupStmt.setLong(2, userID);
        lastChanceGroupStmt.setBoolean(3, false);
        ResultSet lastChanceGroupRS = lastChanceGroupStmt.executeQuery();
        while (lastChanceGroupRS.next()) {
            ownerStmt.setLong(1, lastChanceGroupRS.getLong(1));
            ownerStmt.setInt(2, Roles.OWNER);
            ResultSet ownerRS = ownerStmt.executeQuery();
            String ownerName;
            if (ownerRS.next()) {
                String ownerFirstName = ownerRS.getString(1);
                String ownerLastName = ownerRS.getString(2);
                ownerName = ownerFirstName != null ? ownerFirstName + " " + ownerLastName : ownerLastName;
            } else {
                ownerName = "";
            }
            descriptors.add(new InsightDescriptor(lastChanceGroupRS.getLong(1), lastChanceGroupRS.getString(2), lastChanceGroupRS.getLong(3), lastChanceGroupRS.getInt(4), lastChanceGroupRS.getString(5),
                    new Date(lastChanceGroupRS.getTimestamp(7).getTime()), ownerName, lastChanceGroupRS.getInt(6), lastChanceGroupRS.getBoolean(8), lastChanceGroupRS.getInt(9), lastChanceGroupRS.getString(10),
                    new Date(lastChanceGroupRS.getTimestamp(11).getTime())));
        }
        lastChanceGroupStmt.close();
        ownerStmt.close();
        return descriptors;
    }

    public RolePrioritySet<InsightDescriptor> getReportsForGroup(long groupID, EIConnection conn) throws SQLException {
        RolePrioritySet<InsightDescriptor> descriptors = new RolePrioritySet<InsightDescriptor>();
        PreparedStatement userGroupStmt = conn.prepareStatement("SELECT analysis.ANALYSIS_ID, analysis.TITLE, DATA_FEED_ID, REPORT_TYPE, URL_KEY, create_date, account_visible FROM ANALYSIS, " +
                "group_to_insight WHERE " +
                "analysis.analysis_id = group_to_insight.insight_id and group_to_insight.group_id = ?");
        userGroupStmt.setLong(1, groupID);
        ResultSet groupRS = userGroupStmt.executeQuery();
        while (groupRS.next()) {
            descriptors.add(new InsightDescriptor(groupRS.getLong(1), groupRS.getString(2), groupRS.getLong(3), groupRS.getInt(4), groupRS.getString(5), new Date(groupRS.getTimestamp(6).getTime()), "", Roles.SUBSCRIBER, groupRS.getBoolean(7)));
        }
        userGroupStmt.close();
        return descriptors;
    }

    public List<InsightDescriptor> getInsightDescriptorsForDataSource(long userID, long accountID, long dataSourceID, EIConnection conn, boolean testAccountVisible) throws SQLException {
        Collection<InsightDescriptor> descriptors = new HashSet<InsightDescriptor>();
        FeedDefinition feedDefinition = new FeedStorage().getFeedDefinitionData(dataSourceID, conn);
        if (feedDefinition.getFeedType().getType() == FeedType.COMPOSITE.getType()) {
            CompositeFeedDefinition compositeFeedDefinition = (CompositeFeedDefinition) feedDefinition;
            for (CompositeFeedNode node : compositeFeedDefinition.getCompositeFeedNodes()) {
                descriptors.addAll(getInsightDescriptorsForDataSource(userID, accountID, node.getDataFeedID(), conn, testAccountVisible));
            }
        }
        PreparedStatement queryStmt = conn.prepareStatement("SELECT analysis.ANALYSIS_ID, TITLE, DATA_FEED_ID, REPORT_TYPE, URL_KEY, ACCOUNT_VISIBLE, FOLDER FROM ANALYSIS, USER_TO_ANALYSIS WHERE " +
                "USER_TO_ANALYSIS.analysis_id = analysis.analysis_id and user_to_analysis.user_id = ? AND temporary_report = ? AND " +
                "analysis.data_feed_id = ?");
        queryStmt.setLong(1, userID);
        queryStmt.setBoolean(2, false);
        queryStmt.setLong(3, dataSourceID);
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            descriptors.add(new InsightDescriptor(rs.getLong(1), rs.getString(2), rs.getLong(3), rs.getInt(4), rs.getString(5), new Date(), "", Roles.OWNER, rs.getBoolean(6), rs.getInt(7), ""));
        }
        queryStmt.close();
        if (testAccountVisible) {
            PreparedStatement queryAccountStmt = conn.prepareStatement("SELECT analysis.ANALYSIS_ID, analysis.TITLE, DATA_FEED_ID, REPORT_TYPE, URL_KEY, ACCOUNT_VISIBLE FROM ANALYSIS, USER_TO_ANALYSIS, USER WHERE " +
                    "USER_TO_ANALYSIS.analysis_id = analysis.analysis_id and user_to_analysis.user_id = user.user_id and user.account_id = ? and analysis.account_visible = ? and temporary_report = ? AND " +
                    "analysis.data_feed_id = ?");
            queryAccountStmt.setLong(1, accountID);
            queryAccountStmt.setBoolean(2, true);
            queryAccountStmt.setBoolean(3, false);
            queryAccountStmt.setLong(4, dataSourceID);
            ResultSet accountRS = queryAccountStmt.executeQuery();
            while (accountRS.next()) {
                descriptors.add(new InsightDescriptor(accountRS.getLong(1), accountRS.getString(2), accountRS.getLong(3), accountRS.getInt(4), accountRS.getString(5), Roles.SHARER, accountRS.getBoolean(6)));
            }
            queryAccountStmt.close();
        }
        return new ArrayList<InsightDescriptor>(descriptors);
    }

    public Collection<WSAnalysisDefinition> getAllDefinitions(long userID) {
        Collection<WSAnalysisDefinition> analysisDefinitionList = new ArrayList<WSAnalysisDefinition>();
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            List results = session.createQuery("from AnalysisDefinition as analysisDefinition left join " +
                    "analysisDefinition.userBindings as userBinding where userBinding.userID = ?").
                    setLong(0, userID).list();
            for (Object result : results) {
                Object[] elements = (Object[]) result;
                AnalysisDefinition analysisDefinition = (AnalysisDefinition) elements[0];
                boolean owner = isOwner(userID, analysisDefinition);
                WSAnalysisDefinition wsAnalysisDefinition = analysisDefinition.createBlazeDefinition();
                wsAnalysisDefinition.setCanSaveDirectly(owner);
                analysisDefinitionList.add(wsAnalysisDefinition);                
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        return analysisDefinitionList;
    }

    private boolean isOwner(Long userID, AnalysisDefinition analysisDefinition) {
        for (UserToAnalysisBinding binding : analysisDefinition.getUserBindings()) {
            if (binding.getUserID() == userID && binding.getRelationshipType() == UserPermission.OWNER) {
                return true;
            }
        }
        return false;
    }

    public boolean canUserDelete(long userID, AnalysisDefinition analysisDefinition) {
        for (UserToAnalysisBinding binding : analysisDefinition.getUserBindings()) {
            if (binding.getUserID() == userID && binding.getRelationshipType() == UserPermission.OWNER) {
                return true;
            }
        }
        return false;
    }

    public void deleteAnalysisDefinition(AnalysisDefinition analysisDefinition) {
        Session session = Database.instance().createSession();      
        try {
            session.beginTransaction();
            session.delete(analysisDefinition);
            session.getTransaction().commit();
            clearCache(analysisDefinition.getAnalysisID(), analysisDefinition.getDataFeedID());
        } catch (Exception e) {
            
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public void deleteAnalysisDefinition(AnalysisDefinition analysisDefinition, Connection conn) {
        Session session = Database.instance().createSession(conn);
        try {
            session.delete(analysisDefinition);
            if (analysisDefinition.getAnalysisID() != null) {
                clearCache(analysisDefinition.getAnalysisID(), analysisDefinition.getDataFeedID());
            }
        } finally {
            session.close();
        }
    }

    public Set<ValidationID> populateValidationIDs(long reportID, Set<Long> ids) {
        Set<ValidationID> validationIDs;
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            List results = session.createQuery("from AnalysisDefinition where analysisID = ?").setLong(0, reportID).list();
            if (results.size() > 0) {
                AnalysisDefinition analysisDefinition = (AnalysisDefinition) results.get(0);
                validationIDs = analysisDefinition.populateValidationIDs(ids);
            } else {
                throw new RuntimeException();
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        return validationIDs;
    }
}
