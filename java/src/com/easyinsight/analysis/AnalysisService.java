package com.easyinsight.analysis;

import com.easyinsight.calculations.generated.CalculationsParser;
import com.easyinsight.calculations.generated.CalculationsLexer;
import com.easyinsight.calculations.NodeFactory;
import com.easyinsight.security.*;
import com.easyinsight.security.SecurityException;
import com.easyinsight.logging.LogClass;
import com.easyinsight.database.Database;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.cache.Cache;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.datafeeds.Feed;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.hibernate.Session;
import org.apache.jcs.access.exception.CacheException;

/**
 * User: James Boe
 * Date: Jan 10, 2008
 * Time: 7:32:24 PM
 */
public class AnalysisService {

    private AnalysisStorage analysisStorage = new AnalysisStorage();

    public Collection<InsightDescriptor> getInsightDescriptors() {
        long userID = SecurityUtil.getUserID();
        try {
            return analysisStorage.getInsightDescriptors(userID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public WSAnalysisDefinition saveAs(long reportID, String newName) {
        SecurityUtil.authorizeInsight(reportID);
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            AnalysisDefinition analysisDefinition = analysisStorage.getPersistableReport(reportID, session);
            // TODO: fix me
            AnalysisDefinition clone = analysisDefinition.clone(null, new ArrayList<AnalysisItem>());
            clone.setAuthorName(SecurityUtil.getUserName());
            clone.setTitle(newName);
            List<UserToAnalysisBinding> bindings = new ArrayList<UserToAnalysisBinding>();
            bindings.add(new UserToAnalysisBinding(SecurityUtil.getUserID(), UserPermission.OWNER));
            clone.setUserBindings(bindings);
            analysisStorage.saveAnalysis(clone, session);
            WSAnalysisDefinition returnDef = clone.createBlazeDefinition();
            session.getTransaction().commit();
            return returnDef;
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public Collection<WSAnalysisDefinition> getAnalysisDefinitions() {
        long userID = SecurityUtil.getUserID();
        try {
            Collection<WSAnalysisDefinition> analysisDefinitions = new ArrayList<WSAnalysisDefinition>();
            for (WSAnalysisDefinition analysisDefinition : analysisStorage.getAllDefinitions(userID)) {
                if (analysisDefinition.isRootDefinition()) {
                    continue;
                }
                analysisDefinitions.add(analysisDefinition);
            }
            return analysisDefinitions;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<InsightDescriptor> getWidgetAnalyses() {
        List<InsightDescriptor> descriptorList = new ArrayList<InsightDescriptor>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement getInsightsStmt = conn.prepareStatement("SELECT ANALYSIS.ANALYSIS_ID, TITLE, DATA_FEED_ID, REPORT_TYPE FROM ANALYSIS, user_to_analysis WHERE " +
                    "ANALYSIS.ANALYSIS_ID = USER_TO_ANALYSIS.ANALYSIS_ID AND USER_TO_ANALYSIS.user_id = ? AND analysis.root_definition = ?");
            getInsightsStmt.setLong(1, SecurityUtil.getUserID());
            getInsightsStmt.setBoolean(2, false);
            ResultSet reportRS = getInsightsStmt.executeQuery();
            while (reportRS.next()) {
                descriptorList.add(new InsightDescriptor(reportRS.getLong(1), reportRS.getString(2), reportRS.getLong(3), reportRS.getInt(4)));
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        return descriptorList;
    }

    public String validateCalculation(String calculationString) {
        String validationString = null;
        try {
            CalculationsLexer lexer = new CalculationsLexer(new ANTLRStringStream(calculationString));
            CommonTokenStream tokes = new CommonTokenStream();
            tokes.setTokenSource(lexer);
            CalculationsParser parser = new CalculationsParser(tokes);
            parser.setTreeAdaptor(new NodeFactory());
            parser.expr();
        } catch (Exception e) {
            validationString = e.getMessage();
        }
        return validationString;
    }

    public void addAnalysisView(long analysisID) {
        analysisStorage.addAnalysisView(analysisID);
    }

    public ReportMetrics rateReport(long feedID, int rating) throws SQLException {
        long userID = SecurityUtil.getUserID();
        double ratingAverage;
        int ratingCount;
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement getExistingRatingStmt = conn.prepareStatement("SELECT user_report_rating_id FROM " +
                    "user_report_rating WHERE user_id = ? AND report_id = ?");
            getExistingRatingStmt.setLong(1, userID);
            getExistingRatingStmt.setLong(2, feedID);
            ResultSet rs = getExistingRatingStmt.executeQuery();
            if (rs.next()) {
                PreparedStatement updateRatingStmt = conn.prepareStatement("UPDATE user_report_rating " +
                        "SET RATING = ? WHERE user_report_rating_id = ?");
                updateRatingStmt.setInt(1, rating);
                updateRatingStmt.setLong(2, rs.getLong(1));
                updateRatingStmt.executeUpdate();
            } else {
                PreparedStatement insertRatingStmt = conn.prepareStatement("INSERT INTO user_report_rating " +
                        "(USER_ID, report_id, rating) values (?, ?, ?)");
                insertRatingStmt.setLong(1, userID);
                insertRatingStmt.setLong(2, feedID);
                insertRatingStmt.setInt(3, rating);
                insertRatingStmt.execute();
            }
            PreparedStatement queryStmt = conn.prepareStatement("SELECT AVG(RATING), COUNT(RATING) FROM USER_REPORT_RATING WHERE " +
                    "REPORT_ID = ?");
            queryStmt.setLong(1, feedID);
            ResultSet queryRS = queryStmt.executeQuery();
            queryRS.next();
            ratingAverage = queryRS.getDouble(1);
            ratingCount = queryRS.getInt(2);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return new ReportMetrics(ratingCount, ratingAverage);
    }

    public WSAnalysisDefinition saveAnalysisDefinition(WSAnalysisDefinition wsAnalysisDefinition) {
        return saveAnalysisDefinition(wsAnalysisDefinition, null);
    }

    public WSAnalysisDefinition saveAnalysisDefinition(WSAnalysisDefinition wsAnalysisDefinition, byte[] image) {

        long userID = SecurityUtil.getUserID();
        if (wsAnalysisDefinition.getAnalysisID() > 0) {
            SecurityUtil.authorizeInsight(wsAnalysisDefinition.getAnalysisID());
        }
        try {
            Cache.getCache(Cache.EMBEDDED_REPORTS).remove(wsAnalysisDefinition.getDataFeedID());
        } catch (CacheException e) {
            LogClass.error(e);
        }
        Connection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            for (Tag tag : wsAnalysisDefinition.getTagCloud()) {
                if (tag.getTagID() != null && tag.getTagID() == 0) {
                    tag.setTagID(null);
                }
            }

            PreparedStatement getBindingsStmt = conn.prepareStatement("SELECT USER_ID, RELATIONSHIP_TYPE FROM USER_TO_ANALYSIS WHERE ANALYSIS_ID = ?");
            getBindingsStmt.setLong(1, wsAnalysisDefinition.getAnalysisID());
            ResultSet rs = getBindingsStmt.executeQuery();
            List<UserToAnalysisBinding> bindings = new ArrayList<UserToAnalysisBinding>();
            while (rs.next()) {
                long bindingUserID = rs.getLong(1);
                int relationshipType = rs.getInt(2);
                bindings.add(new UserToAnalysisBinding(bindingUserID, relationshipType));
            }
            if (bindings.isEmpty()) {
                bindings.add(new UserToAnalysisBinding(userID, UserPermission.OWNER));
            }
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM USER_TO_ANALYSIS WHERE ANALYSIS_ID = ?");
            stmt.setLong(1, wsAnalysisDefinition.getAnalysisID());
            stmt.executeUpdate();

            AnalysisDefinition analysisDefinition = AnalysisDefinitionFactory.fromWSDefinition(wsAnalysisDefinition);
            analysisDefinition.setUserBindings(bindings);
            analysisDefinition.setAuthorName(SecurityUtil.getUserName());
            analysisStorage.saveAnalysis(analysisDefinition, session);
            session.flush();
            /*if (image != null) {
                saveImage(image, analysisDefinition.getAnalysisID(), conn);
            }*/
            conn.commit();
            return analysisDefinition.createBlazeDefinition();
        } catch (Exception e) {
            LogClass.error(e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LogClass.error(e);
            }
            session.close();
            Database.instance().closeConnection(conn);
        }
    }

    /*private void saveImage(byte[] image, long reportID, Connection conn) throws SQLException {
        PreparedStatement saveImageStmt = conn.prepareStatement("INSERT INTO REPORT_IMAGE (REPORT_ID, REPORT_IMAGE) VALUES (?, ?)");
        saveImageStmt.setLong(1, reportID);
        saveImageStmt.setBytes(2, image);
        saveImageStmt.execute();
    }*/

    public void deleteAnalysisDefinition(long reportID) {
        long userID = SecurityUtil.getUserID();
        SecurityUtil.authorizeInsight(reportID);
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            AnalysisDefinition dbAnalysisDef = analysisStorage.getPersistableReport(reportID, session);
            boolean canDelete = analysisStorage.canUserDelete(userID, dbAnalysisDef);
            if (canDelete) {
                session.delete(dbAnalysisDef);
            } else {
                Iterator<UserToAnalysisBinding> bindingIter = dbAnalysisDef.getUserBindings().iterator();
                while (bindingIter.hasNext()) {
                    UserToAnalysisBinding binding = bindingIter.next();
                    if (binding.getUserID() == userID) {
                        bindingIter.remove();
                    }
                }
                analysisStorage.saveAnalysis(dbAnalysisDef, session);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public boolean subscribeToAnalysis(long analysisID) {
        SecurityUtil.authorizeInsight(analysisID);
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            long userID = SecurityUtil.getUserID();
            AnalysisDefinition analysisDefinition = analysisStorage.getPersistableReport(analysisID, session);
            boolean found = false;
            for (UserToAnalysisBinding existingBinding : analysisDefinition.getUserBindings()) {
                if (existingBinding.getUserID() == userID) {
                    found = true;
                }
            }
            if (!found) {
                UserToAnalysisBinding binding = new UserToAnalysisBinding(userID, Roles.SUBSCRIBER);
                analysisDefinition.getUserBindings().add(binding);
                analysisStorage.saveAnalysis(analysisDefinition, session);
            }
            session.getTransaction().commit();
            return !found;
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public WSAnalysisDefinition openAnalysisDefinition(long analysisID) {
        try {
            SecurityUtil.authorizeInsight(analysisID);
            addAnalysisView(analysisID);
            return analysisStorage.getAnalysisDefinition(analysisID);
        } catch (Exception e) {
            LogClass.error(e);
            return null;
        }
    }

    public InsightResponse openAnalysisIfPossible(long analysisID) {
        InsightResponse insightResponse;
        try {
            try {
                SecurityUtil.authorizeInsight(analysisID);
                addAnalysisView(analysisID);
                AnalysisDefinition analysisDefinition = analysisStorage.getPersistableReport(analysisID);
                insightResponse = new InsightResponse(InsightResponse.SUCCESS, new InsightDescriptor(analysisID, analysisDefinition.getTitle(),
                        analysisDefinition.getDataFeedID(), analysisDefinition.getReportType()));
            } catch (SecurityException e) {
                if (e.getReason() == InsightResponse.NEED_LOGIN)
                    insightResponse = new InsightResponse(InsightResponse.NEED_LOGIN, null);
                else
                    insightResponse = new InsightResponse(InsightResponse.REJECTED, null);
            }
        } catch (Exception e) {
            LogClass.error(e);
            return null;
        }
        return insightResponse;
    }

    public UserCapabilities getUserCapabilitiesForFeed(long feedID) {
        if (SecurityUtil.getUserID(false) == 0) {
            return new UserCapabilities(Roles.NONE, Roles.NONE, false);
        }
        long userID = SecurityUtil.getUserID();
        int feedRole = Integer.MAX_VALUE;
        boolean groupMember = false;
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement existingLinkQuery = conn.prepareStatement("SELECT ROLE FROM UPLOAD_POLICY_USERS WHERE " +
                    "USER_ID = ? AND FEED_ID = ?");
            existingLinkQuery.setLong(1, userID);
            existingLinkQuery.setLong(2, feedID);
            ResultSet rs = existingLinkQuery.executeQuery();
            if (rs.next()) {
                feedRole = rs.getInt(1);
            }
            PreparedStatement queryStmt = conn.prepareStatement("SELECT COUNT(COMMUNITY_GROUP.COMMUNITY_GROUP_ID) FROM COMMUNITY_GROUP, GROUP_TO_USER_JOIN WHERE " +
                    "USER_ID = ? AND GROUP_TO_USER_JOIN.GROUP_ID = COMMUNITY_GROUP.COMMUNITY_GROUP_ID");
            queryStmt.setLong(1, userID);
            ResultSet groupRS = queryStmt.executeQuery();
            groupRS.next();
            groupMember = groupRS.getInt(1) > 0;
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        return new UserCapabilities(Integer.MAX_VALUE, feedRole, groupMember);
    }

    public UserCapabilities getUserCapabilitiesForInsight(long feedID, long insightID) {
        if (SecurityUtil.getUserID(false) == 0) {
            return new UserCapabilities(Roles.NONE, Roles.NONE, false);
        }
        UserCapabilities userCapabilities = getUserCapabilitiesForFeed(feedID);
        long userID = SecurityUtil.getUserID();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement existingLinkQuery = conn.prepareStatement("SELECT RELATIONSHIP_TYPE FROM USER_TO_ANALYSIS WHERE " +
                    "USER_ID = ? AND ANALYSIS_ID = ?");
            existingLinkQuery.setLong(1, userID);
            existingLinkQuery.setLong(2, insightID);
            ResultSet rs = existingLinkQuery.executeQuery();
            if (rs.next()) {
                userCapabilities.setAnalysisRole(rs.getInt(1));
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        return userCapabilities;
    }
}
