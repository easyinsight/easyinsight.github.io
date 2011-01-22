package com.easyinsight.analysis;

import com.easyinsight.core.*;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import com.easyinsight.security.Roles;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.util.RandomTextGenerator;
import org.hibernate.Session;

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
                analysisDefinition = analysisDefinition.clone(keyMap, analysisItems, false);
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

    public RolePrioritySet<InsightDescriptor> getReports(long userID, long accountID, EIConnection conn) throws SQLException {
        RolePrioritySet<InsightDescriptor> descriptors = new RolePrioritySet<InsightDescriptor>();
        PreparedStatement queryStmt = conn.prepareStatement("SELECT analysis.ANALYSIS_ID, TITLE, DATA_FEED_ID, REPORT_TYPE, URL_KEY, ANALYSIS.update_date FROM ANALYSIS, USER_TO_ANALYSIS WHERE " +
                "USER_TO_ANALYSIS.analysis_id = analysis.analysis_id and user_to_analysis.user_id = ? AND temporary_report = ?");
        queryStmt.setLong(1, userID);
        queryStmt.setBoolean(2, false);

        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            descriptors.add(new InsightDescriptor(rs.getLong(1), rs.getString(2), rs.getLong(3), rs.getInt(4), rs.getString(5), new Date(rs.getTimestamp(6).getTime()), Roles.OWNER));
        }
        queryStmt.close();
        PreparedStatement queryAccountStmt = conn.prepareStatement("SELECT analysis.ANALYSIS_ID, analysis.TITLE, DATA_FEED_ID, REPORT_TYPE, URL_KEY FROM ANALYSIS, USER_TO_ANALYSIS, USER WHERE " +
                "USER_TO_ANALYSIS.analysis_id = analysis.analysis_id and user_to_analysis.user_id = user.user_id and user.account_id = ? and analysis.account_visible = ? and temporary_report = ?");
        queryAccountStmt.setLong(1, accountID);
        queryAccountStmt.setBoolean(2, true);
        queryAccountStmt.setBoolean(3, false);
        ResultSet accountRS = queryAccountStmt.executeQuery();
        while (accountRS.next()) {
            descriptors.add(new InsightDescriptor(accountRS.getLong(1), accountRS.getString(2), accountRS.getLong(3), accountRS.getInt(4), accountRS.getString(5), Roles.SHARER));
        }
        queryAccountStmt.close();
        PreparedStatement userGroupStmt = conn.prepareStatement("SELECT analysis.ANALYSIS_ID, analysis.TITLE, DATA_FEED_ID, REPORT_TYPE, URL_KEY, group_to_user_join.binding_type FROM ANALYSIS, group_to_user_join," +
                "group_to_insight WHERE " +
                "analysis.analysis_id = group_to_insight.insight_id and group_to_insight.group_id = group_to_user_join.group_id and group_to_user_join.user_id = ? and analysis.temporary_report = ?");
        userGroupStmt.setLong(1, userID);
        userGroupStmt.setBoolean(2, false);
        ResultSet groupRS = userGroupStmt.executeQuery();
        while (groupRS.next()) {
            descriptors.add(new InsightDescriptor(groupRS.getLong(1), groupRS.getString(2), groupRS.getLong(3), groupRS.getInt(4), groupRS.getString(5), groupRS.getInt(6)));
        }
        userGroupStmt.close();
        return descriptors;
    }

    public RolePrioritySet<InsightDescriptor> getReportsForGroup(long groupID, EIConnection conn) throws SQLException {
        RolePrioritySet<InsightDescriptor> descriptors = new RolePrioritySet<InsightDescriptor>();
        PreparedStatement userGroupStmt = conn.prepareStatement("SELECT analysis.ANALYSIS_ID, analysis.TITLE, DATA_FEED_ID, REPORT_TYPE, URL_KEY FROM ANALYSIS, " +
                "group_to_insight WHERE " +
                "analysis.analysis_id = group_to_insight.insight_id and group_to_insight.group_id = ?");
        userGroupStmt.setLong(1, groupID);
        ResultSet groupRS = userGroupStmt.executeQuery();
        while (groupRS.next()) {
            descriptors.add(new InsightDescriptor(groupRS.getLong(1), groupRS.getString(2), groupRS.getLong(3), groupRS.getInt(4), groupRS.getString(5), Roles.SUBSCRIBER));
        }
        userGroupStmt.close();
        return descriptors;
    }

    public List<InsightDescriptor> getInsightDescriptorsForDataSource(long userID, long accountID, long dataSourceID, EIConnection conn) throws SQLException {
        Collection<InsightDescriptor> descriptors = new HashSet<InsightDescriptor>();
        PreparedStatement queryStmt = conn.prepareStatement("SELECT analysis.ANALYSIS_ID, TITLE, DATA_FEED_ID, REPORT_TYPE, URL_KEY FROM ANALYSIS, USER_TO_ANALYSIS WHERE " +
                "USER_TO_ANALYSIS.analysis_id = analysis.analysis_id and user_to_analysis.user_id = ? AND temporary_report = ? AND " +
                "analysis.data_feed_id = ?");
        queryStmt.setLong(1, userID);
        queryStmt.setBoolean(2, false);
        queryStmt.setLong(3, dataSourceID);
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            descriptors.add(new InsightDescriptor(rs.getLong(1), rs.getString(2), rs.getLong(3), rs.getInt(4), rs.getString(5), Roles.OWNER));
        }
        queryStmt.close();
        PreparedStatement queryAccountStmt = conn.prepareStatement("SELECT analysis.ANALYSIS_ID, analysis.TITLE, DATA_FEED_ID, REPORT_TYPE, URL_KEY FROM ANALYSIS, USER_TO_ANALYSIS, USER WHERE " +
                "USER_TO_ANALYSIS.analysis_id = analysis.analysis_id and user_to_analysis.user_id = user.user_id and user.account_id = ? and analysis.account_visible = ? and temporary_report = ? AND " +
                "analysis.data_feed_id = ?");
        queryAccountStmt.setLong(1, accountID);
        queryAccountStmt.setBoolean(2, true);
        queryAccountStmt.setBoolean(3, false);
        queryAccountStmt.setLong(4, dataSourceID);
        ResultSet accountRS = queryAccountStmt.executeQuery();
        while (accountRS.next()) {
            descriptors.add(new InsightDescriptor(accountRS.getLong(1), accountRS.getString(2), accountRS.getLong(3), accountRS.getInt(4), accountRS.getString(5), Roles.SHARER));
        }
        queryAccountStmt.close();
        PreparedStatement queryVizStmt = conn.prepareStatement("SELECT analysis.ANALYSIS_ID, analysis.TITLE, DATA_FEED_ID, REPORT_TYPE, URL_KEY FROM ANALYSIS, USER_TO_ANALYSIS, USER WHERE " +
                "analysis.feed_visibility = ? and temporary_report = ? AND " +
                "analysis.data_feed_id = ?");
        queryVizStmt.setBoolean(1, true);
        queryVizStmt.setBoolean(2, false);
        queryVizStmt.setLong(3, dataSourceID);
        ResultSet vizRS = queryVizStmt.executeQuery();
        while (vizRS.next()) {
            descriptors.add(new InsightDescriptor(vizRS.getLong(1), vizRS.getString(2), vizRS.getLong(3), vizRS.getInt(4), vizRS.getString(5), Roles.SUBSCRIBER));
        }
        queryVizStmt.close();
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
}
