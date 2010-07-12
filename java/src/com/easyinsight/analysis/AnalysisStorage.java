package com.easyinsight.analysis;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.core.PersistableValue;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.core.Key;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import com.easyinsight.scrubbing.DataScrub;
import com.easyinsight.util.RandomTextGenerator;
import org.hibernate.Session;
import org.hibernate.Query;

/**
 * User: James Boe
 * Date: Jan 10, 2008
 * Time: 6:37:10 PM
 */
public class AnalysisStorage {

    public ReportMetrics getRating(long analysisID) throws SQLException {
        double ratingAverage = 0;
        int ratingCount = 0;
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT AVG(RATING), COUNT(RATING) FROM USER_REPORT_RATING WHERE " +
                    "REPORT_ID = ?");
            queryStmt.setLong(1, analysisID);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                ratingAverage = rs.getDouble(1);
                ratingCount = rs.getInt(2);
            }
        } finally {
            Database.closeConnection(conn);
        }
        return new ReportMetrics(ratingCount, ratingAverage);
    }

    public WSAnalysisDefinition getAnalysisDefinition(long analysisID) {
        WSAnalysisDefinition analysisDefinition = null;
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            List results = session.createQuery("from AnalysisDefinition where analysisID = ?").setLong(0, analysisID).list();
            if (results.size() > 0) {
                AnalysisDefinition savedReport = (AnalysisDefinition) results.get(0);
                analysisDefinition = savedReport.createBlazeDefinition();
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

    public WSAnalysisDefinition getAnalysisDefinition(long analysisID, Connection conn) {
        WSAnalysisDefinition analysisDefinition = null;
        Session session = Database.instance().createSession(conn);
        try {
            session.beginTransaction();
            List results = session.createQuery("from AnalysisDefinition where analysisID = ?").setLong(0, analysisID).list();
            if (results.size() > 0) {
                AnalysisDefinition savedReport = (AnalysisDefinition) results.get(0);
                analysisDefinition = savedReport.createBlazeDefinition();
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

    public AnalysisDefinition cloneReport(long analysisID, Connection conn, Map<Key, Key> keyMap, List<AnalysisItem> analysisItems) {
        AnalysisDefinition analysisDefinition = null;
        Session session = Database.instance().createSession(conn);
        try {
            session.beginTransaction();
            List results = session.createQuery("from AnalysisDefinition where analysisID = ?").setLong(0, analysisID).list();
            if (results.size() > 0) {
                analysisDefinition = (AnalysisDefinition) results.get(0);
                analysisDefinition = analysisDefinition.clone(keyMap, analysisItems);
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

    public void saveAnalysis(AnalysisDefinition analysisDefinition, Session session) {
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
                if (filter instanceof FilterValueDefinition) {
                    FilterValueDefinition valueFilter = (FilterValueDefinition) filter;
                    for (PersistableValue persistableValue : valueFilter.getPersistedValues()) {
                        session.saveOrUpdate(persistableValue);
                    }
                }
            }
        }
        if (analysisDefinition.getDataScrubs() != null) {
            for (DataScrub dataScrub : analysisDefinition.getDataScrubs()) {
                dataScrub.beforeSave();
            }
        }
        if (analysisDefinition.getAddedItems() != null) {
            for (AnalysisItem analysisItem : analysisDefinition.getAddedItems()) {
                analysisItem.reportSave(session);

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

    public Collection<InsightDescriptor> getInsightDescriptors(long userID) {
        Collection<InsightDescriptor> descriptors = new ArrayList<InsightDescriptor>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT analysis.ANALYSIS_ID, TITLE, DATA_FEED_ID, REPORT_TYPE, URL_KEY FROM ANALYSIS, USER_TO_ANALYSIS WHERE " +
                    "USER_TO_ANALYSIS.analysis_id = analysis.analysis_id and user_to_analysis.user_id = ? and root_definition = ? AND temporary_report = ?");
            queryStmt.setLong(1, userID);
            queryStmt.setBoolean(2, false);
            queryStmt.setBoolean(3, false);
            
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                descriptors.add(new InsightDescriptor(rs.getLong(1), rs.getString(2), rs.getLong(3), rs.getInt(4), rs.getString(5)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        return descriptors;
    }

    public Collection<InsightDescriptor> getInsightDescriptors(long userID, long reportID) {
        Collection<InsightDescriptor> descriptors = new ArrayList<InsightDescriptor>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT analysis.ANALYSIS_ID, TITLE, DATA_FEED_ID, REPORT_TYPE FROM ANALYSIS, USER_TO_ANALYSIS WHERE " +
                    "USER_TO_ANALYSIS.analysis_id = analysis.analysis_id and user_to_analysis.user_id = ? and root_definition = ? AND temporary_report = ? AND " +
                    "analysis.data_feed_id = ?");
            queryStmt.setLong(1, userID);
            queryStmt.setBoolean(2, false);
            queryStmt.setBoolean(3, false);
            queryStmt.setLong(4, reportID);
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                // TODO: Add urlKey
                descriptors.add(new InsightDescriptor(rs.getLong(1), rs.getString(2), rs.getLong(3), rs.getInt(4), null));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        return descriptors;
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

    public List<InsightDescriptor> getMostPopularAnalyses(String genre, int maxResults) {
        Connection conn = Database.instance().getConnection();

        List<InsightDescriptor> analysisList = new ArrayList<InsightDescriptor>();
        Session session = Database.instance().createSession(conn);
        try {
            PreparedStatement analysisQueryStmt;
            if (genre == null) {
                analysisQueryStmt = conn.prepareStatement("SELECT DISTINCT ANALYSIS.ANALYSIS_ID, ANALYSIS.TITLE, ANALYSIS.DATA_FEED_ID, ANALYSIS.REPORT_TYPE FROM ANALYSIS, DATA_FEED " +
                        " WHERE ANALYSIS.DATA_FEED_ID = DATA_FEED.DATA_FEED_ID AND " +
                        "((DATA_FEED.MARKETPLACE_VISIBLE = ? AND ANALYSIS.FEED_VISIBILITY = ?) OR ANALYSIS.MARKETPLACE_VISIBLE = ?) AND " +
                        "ANALYSIS.ROOT_DEFINITION = ? ORDER BY ANALYSIS.VIEWS DESC LIMIT " + maxResults);
                analysisQueryStmt.setBoolean(1, true);
                analysisQueryStmt.setBoolean(2, true);
                analysisQueryStmt.setBoolean(3, true);
                analysisQueryStmt.setBoolean(4, false);
            } else {
                analysisQueryStmt = conn.prepareStatement("SELECT DISTINCT ANALYSIS.ANALYSIS_ID, ANALYSIS.TITLE, ANALYSIS.DATA_FEED_ID, ANALYSIS.REPORT_TYPE FROM ANALYSIS, DATA_FEED, " +
                        "FEED_TO_TAG, ANALYSIS_TAGS, ANALYSIS_TO_TAG WHERE ANALYSIS.DATA_FEED_ID = DATA_FEED.DATA_FEED_ID AND " +
                        "((DATA_FEED.MARKETPLACE_VISIBLE = ? AND ANALYSIS.FEED_VISIBILITY = ?) OR ANALYSIS.MARKETPLACE_VISIBLE = ?) AND " +
                        "((FEED_TO_TAG.FEED_ID = DATA_FEED.DATA_FEED_ID AND FEED_TO_TAG.ANALYSIS_TAGS_ID = ANALYSIS_TAGS.ANALYSIS_TAGS_ID AND ANALYSIS_TAGS.TAG = ?) OR " +
                        "(ANALYSIS_TO_TAG.ANALYSIS_ID = ANALYSIS.ANALYSIS_ID AND ANALYSIS_TO_TAG.ANALYSIS_TAGS_ID = ANALYSIS_TAGS.ANALYSIS_TAGS_ID AND ANALYSIS_TAGS.TAG = ?))" +
                        "AND ANALYSIS.ROOT_DEFINITION = ? ORDER BY ANALYSIS.VIEWS DESC LIMIT " + maxResults);
                analysisQueryStmt.setBoolean(1, true);
                analysisQueryStmt.setBoolean(2, true);
                analysisQueryStmt.setBoolean(3, true);
                analysisQueryStmt.setString(4, genre);
                analysisQueryStmt.setString(5, genre);
                analysisQueryStmt.setBoolean(6, false);
            }
            ResultSet analysisRS = analysisQueryStmt.executeQuery();
            while (analysisRS.next()) {
                long analysisID = analysisRS.getLong(1);
                String title = analysisRS.getString(2);
                long dataSourceID = analysisRS.getLong(3);
                // TODO: Add urlKey
                analysisList.add(new InsightDescriptor(analysisID, title, dataSourceID, analysisRS.getInt(4), null));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            session.close();
            Database.instance().closeConnection(conn);
        }
        return analysisList;
    }

    public List<InsightDescriptor> getBestRatedAnalyses(String genre, int maxResults) {

        Connection conn = Database.instance().getConnection();

        List<InsightDescriptor> analysisList = new ArrayList<InsightDescriptor>();
        Session session = Database.instance().createSession(conn);
        try {
            PreparedStatement analysisQueryStmt;
            if (genre == null) {
                analysisQueryStmt = conn.prepareStatement("SELECT ANALYSIS.ANALYSIS_ID, ANALYSIS.TITLE, ANALYSIS.DATA_FEED_ID, ANALYSIS.REPORT_TYPE FROM ANALYSIS, DATA_FEED " +
                        " WHERE ANALYSIS.DATA_FEED_ID = DATA_FEED.DATA_FEED_ID AND " +
                        "((DATA_FEED.MARKETPLACE_VISIBLE = ? AND ANALYSIS.FEED_VISIBILITY = ?) OR ANALYSIS.MARKETPLACE_VISIBLE = ?) AND " +
                        "ANALYSIS.ROOT_DEFINITION = ? ORDER BY ANALYSIS.VIEWS DESC LIMIT " + maxResults);
                analysisQueryStmt.setBoolean(1, true);
                analysisQueryStmt.setBoolean(2, true);
                analysisQueryStmt.setBoolean(3, true);
                analysisQueryStmt.setBoolean(4, false);
            } else {
                analysisQueryStmt = conn.prepareStatement("SELECT DISTINCT ANALYSIS.ANALYSIS_ID, ANALYSIS.TITLE, ANALYSIS.DATA_FEED_ID, ANALYSIS.REPORT_TYPE FROM ANALYSIS, DATA_FEED, " +
                        "FEED_TO_TAG, ANALYSIS_TAGS, ANALYSIS_TO_TAG WHERE ANALYSIS.DATA_FEED_ID = DATA_FEED.DATA_FEED_ID AND " +
                        "((DATA_FEED.MARKETPLACE_VISIBLE = ? AND ANALYSIS.FEED_VISIBILITY = ?) OR ANALYSIS.MARKETPLACE_VISIBLE = ?) AND " +
                        "((FEED_TO_TAG.FEED_ID = DATA_FEED.DATA_FEED_ID AND FEED_TO_TAG.ANALYSIS_TAGS_ID = ANALYSIS_TAGS.ANALYSIS_TAGS_ID AND ANALYSIS_TAGS.TAG = ?) OR " +
                        "(ANALYSIS_TO_TAG.ANALYSIS_ID = ANALYSIS.ANALYSIS_ID AND ANALYSIS_TO_TAG.ANALYSIS_TAGS_ID = ANALYSIS_TAGS.ANALYSIS_TAGS_ID AND ANALYSIS_TAGS.TAG = ?))" +
                        "AND ANALYSIS.ROOT_DEFINITION = ? ORDER BY ANALYSIS.VIEWS DESC LIMIT " + maxResults);
                analysisQueryStmt.setBoolean(1, true);
                analysisQueryStmt.setBoolean(2, true);
                analysisQueryStmt.setBoolean(3, true);
                analysisQueryStmt.setString(4, genre);
                analysisQueryStmt.setString(5, genre);
                analysisQueryStmt.setBoolean(6, false);
            }
            ResultSet analysisRS = analysisQueryStmt.executeQuery();
            while (analysisRS.next()) {
                long analysisID = analysisRS.getLong(1);
                String title = analysisRS.getString(2);
                long dataSourceID = analysisRS.getLong(3);
                // TODO: Add urlKey
                analysisList.add(new InsightDescriptor(analysisID, title, dataSourceID, analysisRS.getInt(4), null));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            session.close();
            Database.instance().closeConnection(conn);
        }
        return analysisList;
    }

    public List<InsightDescriptor> getMostRecentAnalyses(String genre, int maxResults) {

        Connection conn = Database.instance().getConnection();

        List<InsightDescriptor> analysisList = new ArrayList<InsightDescriptor>();
        Session session = Database.instance().createSession(conn);
        try {
            PreparedStatement analysisQueryStmt;
            if (genre == null) {
                analysisQueryStmt = conn.prepareStatement("SELECT ANALYSIS.ANALYSIS_ID, ANALYSIS.TITLE, ANALYSIS.DATA_FEED_ID, ANALYSIS.REPORT_TYPE FROM ANALYSIS, DATA_FEED " +
                        " WHERE ANALYSIS.DATA_FEED_ID = DATA_FEED.DATA_FEED_ID AND " +
                        "((DATA_FEED.MARKETPLACE_VISIBLE = ? AND ANALYSIS.FEED_VISIBILITY = ?) OR ANALYSIS.MARKETPLACE_VISIBLE = ?) AND " +
                        "ANALYSIS.ROOT_DEFINITION = ? ORDER BY ANALYSIS.VIEWS DESC LIMIT " + maxResults);
                analysisQueryStmt.setBoolean(1, true);
                analysisQueryStmt.setBoolean(2, true);
                analysisQueryStmt.setBoolean(3, true);
                analysisQueryStmt.setBoolean(4, false);
            } else {
                analysisQueryStmt = conn.prepareStatement("SELECT DISTINCT ANALYSIS.ANALYSIS_ID, ANALYSIS.TITLE, ANALYSIS.DATA_FEED_ID FROM ANALYSIS, DATA_FEED, " +
                        "FEED_TO_TAG, ANALYSIS_TAGS, ANALYSIS_TO_TAG WHERE ANALYSIS.DATA_FEED_ID = DATA_FEED.DATA_FEED_ID AND " +
                        "((DATA_FEED.MARKETPLACE_VISIBLE = ? AND ANALYSIS.FEED_VISIBILITY = ?) OR ANALYSIS.MARKETPLACE_VISIBLE = ?) AND " +
                        "((FEED_TO_TAG.FEED_ID = DATA_FEED.DATA_FEED_ID AND FEED_TO_TAG.ANALYSIS_TAGS_ID = ANALYSIS_TAGS.ANALYSIS_TAGS_ID AND ANALYSIS_TAGS.TAG = ?) OR " +
                        "(ANALYSIS_TO_TAG.ANALYSIS_ID = ANALYSIS.ANALYSIS_ID AND ANALYSIS_TO_TAG.ANALYSIS_TAGS_ID = ANALYSIS_TAGS.ANALYSIS_TAGS_ID AND ANALYSIS_TAGS.TAG = ?))" +
                        "AND ANALYSIS.ROOT_DEFINITION = ? ORDER BY ANALYSIS.VIEWS DESC LIMIT " + maxResults);
                analysisQueryStmt.setBoolean(1, true);
                analysisQueryStmt.setBoolean(2, true);
                analysisQueryStmt.setBoolean(3, true);
                analysisQueryStmt.setString(4, genre);
                analysisQueryStmt.setString(5, genre);
                analysisQueryStmt.setBoolean(6, false);
            }
            ResultSet analysisRS = analysisQueryStmt.executeQuery();
            while (analysisRS.next()) {
                long analysisID = analysisRS.getLong(1);
                String title = analysisRS.getString(2);
                long dataSourceID = analysisRS.getLong(3);
                // TODO: Add urlKey
                analysisList.add(new InsightDescriptor(analysisID, title, dataSourceID, analysisRS.getInt(4), null));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            session.close();
            Database.instance().closeConnection(conn);
        }
        return analysisList;
    }

    public void addAnalysisView(long analysisID) {
        Connection conn = Database.instance().getConnection();
        PreparedStatement getViewCountStmt;
        PreparedStatement updateViewsStmt;
        try {
            getViewCountStmt = conn.prepareStatement("SELECT VIEWS FROM ANALYSIS WHERE " +
                    "ANALYSIS_ID = ?");
            updateViewsStmt = conn.prepareStatement("UPDATE ANALYSIS SET VIEWS = ? WHERE " +
                    "ANALYSIS_ID = ?");
            getViewCountStmt.setLong(1, analysisID);
            ResultSet rs = getViewCountStmt.executeQuery();
            if (rs.next()) {
                int feedViews = rs.getInt(1);
                updateViewsStmt.setInt(1, feedViews + 1);
                updateViewsStmt.setLong(2, analysisID);
                updateViewsStmt.executeUpdate();
            }
            getViewCountStmt.close();
            updateViewsStmt.close();
        } catch (SQLException e) {

            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void rateAnalysis(long analysisID, long accountID, int rating) {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement getExistingRatingStmt = conn.prepareStatement("SELECT RATING_COUNT, RATING_AVERAGE FROM " +
                    "ANALYSIS WHERE ANALYSIS_ID = ?");
            PreparedStatement updateRatingStmt = conn.prepareStatement("UPDATE ANALYSIS SET RATING_COUNT = ?, " +
                    "RATING_AVERAGE = ? WHERE ANALYSIS_ID = ?");
            getExistingRatingStmt.setLong(1, analysisID);
            ResultSet rs = getExistingRatingStmt.executeQuery();
            while (rs.next()) {
                int count = rs.getInt(1);
                double average = rs.getDouble(2);
                int newCount = count + 1;
                double newAverage = ((average + rating) * newCount) / newCount;
                updateRatingStmt.setInt(1, count);
                updateRatingStmt.setDouble(2, newAverage);
                updateRatingStmt.setLong(3, analysisID);
                updateRatingStmt.executeUpdate();
            }
        } catch (SQLException e) {

            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
    }

    public List<InsightDescriptor> getAllDefinitions(String genre) {
        List<InsightDescriptor> analysisList = new ArrayList<InsightDescriptor>();
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            Query query;
            query = session.createQuery("from AnalysisDefinition as analysisDefinition where analysisDefinition.analysisPolicy = ? order by dateCreated desc").
                    setInteger(0, AnalysisPolicy.PUBLIC);
            Iterator iter = query.iterate();
            while (iter.hasNext()) {
                AnalysisDefinition analysisDefinition = (AnalysisDefinition) iter.next();
                // TODO: Add urlKey
                analysisList.add(new InsightDescriptor(analysisDefinition.getAnalysisID(), analysisDefinition.getTitle(), analysisDefinition.getDataFeedID(), analysisDefinition.getReportType(), null));
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        return analysisList;
    }

    private boolean isOwner(Long userID, AnalysisDefinition analysisDefinition) {
        for (UserToAnalysisBinding binding : analysisDefinition.getUserBindings()) {
            if (binding.getUserID() == userID && binding.getRelationshipType() == UserPermission.OWNER) {
                return true;
            }
        }
        return false;
    }

    public Tag getTag(String tagName) {
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            Tag tag;
            List results = session.createQuery("from Tag as tag where tag.tagName  = ?").setString(0, tagName).list();
            if (results.size() > 0) {
                tag = (Tag) results.get(0);
            } else {
                tag = new Tag(tagName);
            }
            session.getTransaction().commit();
            return tag;
        } catch (Exception e) {

            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
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
        } finally {
            session.close();
        }
    }

    public List<InsightDescriptor> getReportsForGroups(long userID) throws SQLException {
        List<InsightDescriptor> reports = new ArrayList<InsightDescriptor>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("select analysis.analysis_id, analysis.title, analysis.data_feed_id, analysis.report_type " +
                    "from group_to_insight, group_to_user_join, analysis where " +
                        "group_to_user_join.group_id = group_to_insight.group_id and group_to_insight.insight_id = analysis.analysis_id and group_to_user_join.user_id = ?");
            queryStmt.setLong(1, userID);
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                // TODO: Add urlKey
                reports.add(new InsightDescriptor(rs.getLong(1), rs.getString(2), rs.getLong(3), rs.getInt(4), null));
            }
        } finally {
            Database.closeConnection(conn);
        }
        return reports;
    }

    public List<InsightDescriptor> getReportsForGroup(long groupID) throws SQLException {
        List<InsightDescriptor> reports = new ArrayList<InsightDescriptor>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("select analysis.analysis_id, analysis.title, analysis.data_feed_id, analysis.report_type " +
                    "from group_to_insight, analysis where " +
                        "(group_to_insight.group_id = ? and group_to_insight.insight_id = analysis.analysis_id)");
            queryStmt.setLong(1, groupID);
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                // TODO: Add urlKey
                reports.add(new InsightDescriptor(rs.getLong(1), rs.getString(2), rs.getLong(3), rs.getInt(4), null));
            }
            PreparedStatement dsShareStmt = conn.prepareStatement("select analysis.analysis_id, analysis.title, analysis.data_feed_id, analysis.report_type " +
                    "from analysis, data_feed, upload_policy_groups where " +
                    "(analysis.feed_visibility = ? AND analysis.data_feed_id = data_feed.data_feed_id AND data_feed.data_feed_id = upload_policy_groups.feed_id AND " +
                    "upload_policy_groups.group_id = ?)");
            dsShareStmt.setBoolean(1, true);
            dsShareStmt.setLong(2, groupID);
            ResultSet shareRS = dsShareStmt.executeQuery();
            while (shareRS.next()) {
                // TODO: Add urlKey
                reports.add(new InsightDescriptor(shareRS.getLong(1), shareRS.getString(2), shareRS.getLong(3), shareRS.getInt(4), null));
            }
        } finally {
            Database.closeConnection(conn);
        }
        return reports;
    }
}
