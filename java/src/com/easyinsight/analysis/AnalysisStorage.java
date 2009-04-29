package com.easyinsight.analysis;

import com.easyinsight.database.Database;
import com.easyinsight.core.PersistableValue;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.logging.LogClass;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.Query;

/**
 * User: James Boe
 * Date: Jan 10, 2008
 * Time: 6:37:10 PM
 */
public class AnalysisStorage {

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
            LogClass.error(e);
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
            LogClass.error(e);
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
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        return analysisDefinition;
    }

    public AnalysisDefinition cloneReport(long analysisID, Connection conn) {
        AnalysisDefinition analysisDefinition = null;
        Session session = Database.instance().createSession(conn);
        try {
            session.beginTransaction();
            List results = session.createQuery("from AnalysisDefinition where analysisID = ?").setLong(0, analysisID).list();
            if (results.size() > 0) {
                analysisDefinition = (AnalysisDefinition) results.get(0);
                analysisDefinition = analysisDefinition.clone();
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
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
            session.beginTransaction();
            List results = session.createQuery("from AnalysisDefinition where analysisID = ?").setLong(0, analysisID).list();
            if (results.size() > 0) {
                analysisDefinition = (AnalysisDefinition) results.get(0);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
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
        if (analysisDefinition.getDateCreated() == null) {
            analysisDefinition.setDateCreated(new Date());
        }
        analysisDefinition.setDateUpdated(new Date());
        if (analysisDefinition.getFilterDefinitions() != null) {
            for (PersistableFilterDefinition filter : analysisDefinition.getFilterDefinitions()) {
                if (filter instanceof PersistableValueFilterDefinition) {
                    PersistableValueFilterDefinition valueFilter = (PersistableValueFilterDefinition) filter;
                    for (PersistableValue persistableValue : valueFilter.getFilterValues()) {
                        session.saveOrUpdate(persistableValue);
                    }
                }
            }
        }
        if (analysisDefinition.getAddedItems() != null) {
            for (AnalysisItem analysisItem : analysisDefinition.getAddedItems()) {
                if (analysisItem.getKey().getKeyID() == 0) {
                    session.save(analysisItem.getKey());
                }
            }
        }
        if (analysisDefinition.getReportStructure() != null) {
            for (AnalysisItem analysisItem : analysisDefinition.getReportStructure().values()) {
                analysisItem.beforeSave();
                if (analysisItem.getAnalysisItemID() == 0) {
                    session.save(analysisItem);
                }
            }
        }
        if (analysisDefinition.getAnalysisID() == null)
            session.save(analysisDefinition);
        else
            session.merge(analysisDefinition);        
        session.flush();
    }

    public void saveAnalysis(AnalysisDefinition analysisDefinition) {

        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            saveAnalysis(analysisDefinition, session);
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
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
            PreparedStatement queryStmt = conn.prepareStatement("SELECT analysis.ANALYSIS_ID, TITLE, DATA_FEED_ID, REPORT_TYPE FROM ANALYSIS, USER_TO_ANALYSIS WHERE " +
                    "USER_TO_ANALYSIS.analysis_id = analysis.analysis_id and user_to_analysis.user_id = ? and root_definition = ?");
            queryStmt.setLong(1, userID);
            queryStmt.setBoolean(2, false);
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                descriptors.add(new InsightDescriptor(rs.getLong(1), rs.getString(2), rs.getLong(3), rs.getInt(4)));
            }
        } catch (SQLException e) {
            LogClass.error(e);
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
            LogClass.error(e);
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
                analysisQueryStmt = conn.prepareStatement("SELECT DISTINCT ANALYSIS.ANALYSIS_ID, ANALYSIS.TITLE, ANALYSIS.DATA_FEED_ID FROM ANALYSIS, DATA_FEED " +
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
                analysisList.add(new InsightDescriptor(analysisID, title, dataSourceID, analysisRS.getInt(4)));
            }
        } catch (Exception e) {
            LogClass.error(e);
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
                analysisQueryStmt = conn.prepareStatement("SELECT ANALYSIS.ANALYSIS_ID, ANALYSIS.TITLE, ANALYSIS.DATA_FEED_ID FROM ANALYSIS, DATA_FEED " +
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
                analysisList.add(new InsightDescriptor(analysisID, title, dataSourceID, analysisRS.getInt(4)));
            }
        } catch (Exception e) {
            LogClass.error(e);
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
                analysisQueryStmt = conn.prepareStatement("SELECT ANALYSIS.ANALYSIS_ID, ANALYSIS.TITLE, ANALYSIS.DATA_FEED_ID FROM ANALYSIS, DATA_FEED " +
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
                analysisList.add(new InsightDescriptor(analysisID, title, dataSourceID, analysisRS.getInt(4)));
            }
        } catch (Exception e) {
            LogClass.error(e);
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
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
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
            LogClass.error(e);
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
            if (genre == null) {
                query = session.createQuery("from AnalysisDefinition as analysisDefinition where analysisDefinition.analysisPolicy = ? order by dateCreated desc").
                    setInteger(0, AnalysisPolicy.PUBLIC);
            } else {
                query = session.createQuery("from AnalysisDefinition as analysisDefinition where analysisDefinition.analysisPolicy = ? and " +
                        "analysisDefinition.genre = ? order by dateCreated desc ").setInteger(0, AnalysisPolicy.PUBLIC).setString(1, genre);
            }
            Iterator iter = query.iterate();
            while (iter.hasNext()) {
                AnalysisDefinition analysisDefinition = (AnalysisDefinition) iter.next();
                analysisList.add(new InsightDescriptor(analysisDefinition.getAnalysisID(), analysisDefinition.getTitle(), analysisDefinition.getDataFeedID(), analysisDefinition.getReportType()));
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
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
            LogClass.error(e);
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
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }
}
