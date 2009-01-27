package com.easyinsight.goals;

import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;
import com.easyinsight.analysis.*;
import com.easyinsight.*;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.FeedDescriptor;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.solutions.SolutionService;
import com.easyinsight.solutions.Solution;
import com.easyinsight.solutions.SolutionInstallInfo;
import com.easyinsight.core.Value;
import com.easyinsight.core.DateValue;
import com.easyinsight.webservice.google.ListDataResults;
import com.easyinsight.webservice.google.ListRow;
import com.easyinsight.security.Roles;
import com.easyinsight.security.SecurityUtil;

import java.sql.*;
import java.util.*;
import java.util.Date;

import org.hibernate.Session;

/**
 * User: James Boe
 * Date: Oct 23, 2008
 * Time: 3:07:00 PM
 */
public class GoalStorage {

    private GoalEvaluationStorage goalEvaluationStorage = new GoalEvaluationStorage();

    public void setUserRole(long userID, long goalTreeID, int role, Connection conn) throws SQLException {
        PreparedStatement roleExistsStmt = conn.prepareStatement("SELECT USER_TO_GOAL_TREE_ID FROM USER_TO_GOAL_TREE WHERE " +
                "user_id = ? AND goal_tree_id = ?");
        roleExistsStmt.setLong(1, userID);
        roleExistsStmt.setLong(2, goalTreeID);
        ResultSet rs = roleExistsStmt.executeQuery();
        if (rs.next()) {
            PreparedStatement updateRoleStmt = conn.prepareStatement("UPDATE USER_TO_GOAL_TREE SET USER_ROLE = ? WHERE USER_TO_GOAL_TREE_ID = ?");
            updateRoleStmt.setInt(1, role);
            updateRoleStmt.setLong(2, rs.getLong(1));
            updateRoleStmt.executeUpdate();
        } else {
            PreparedStatement insertRoleStmt = conn.prepareStatement("INSERT INTO USER_TO_GOAL_TREE (USER_ID, goal_tree_id, user_role) VALUES (?, ?, ?)");
            insertRoleStmt.setLong(1, userID);
            insertRoleStmt.setLong(2, goalTreeID);
            insertRoleStmt.setInt(3, role);
            insertRoleStmt.execute();
        }
    }

    public List<GoalTreeDescriptor> getTreesForUser(long userID) {
        List<GoalTreeDescriptor> descriptors = new ArrayList<GoalTreeDescriptor>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement getTreesStmt = conn.prepareStatement("SELECT NAME, GOAL_TREE.GOAL_TREE_ID, USER_ROLE FROM GOAL_TREE, user_to_goal_tree WHERE " +
                    "user_id = ? AND user_to_goal_tree.goal_tree_id = goal_tree.goal_tree_id");
            getTreesStmt.setLong(1, userID);
            ResultSet rs = getTreesStmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString(1);
                long treeID = rs.getLong(2);
                int role = rs.getInt(3);
                descriptors.add(new GoalTreeDescriptor(treeID, name, role));
            }
        } catch (SQLException e) {
            LogClass.error(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        return descriptors;
    }

    public void addGoalTree(GoalTree goalTree, long userID) {
        Connection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            if (goalTree.getRootNode() == null) {
                throw new RuntimeException("You must have a root node on a goal tree.");
            }
            installSolutions(goalTree, conn);
            long nodeID = saveGoalTreeNode(goalTree.getRootNode(), conn);
            PreparedStatement insertTreeStmt = conn.prepareStatement("INSERT INTO GOAL_TREE (NAME, DESCRIPTION, ROOT_NODE) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertTreeStmt.setString(1, goalTree.getName());
            insertTreeStmt.setString(2, goalTree.getDescription());
            insertTreeStmt.setLong(3, nodeID);
            insertTreeStmt.execute();
            long treeID = Database.instance().getAutoGenKey(insertTreeStmt);
            goalTree.setGoalTreeID(treeID);
            setUserRole(userID, treeID, Roles.OWNER, conn);
            goalEvaluationStorage.backPopulateGoalTree(goalTree, conn);
            conn.commit();
        } catch (SQLException e) {
            LogClass.error(e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LogClass.error(e);
            }
            Database.instance().closeConnection(conn);
        }
    }

    public void updateGoalTree(GoalTree goalTree) {
        Connection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            installSolutions(goalTree, conn);            
            long nodeID = saveGoalTreeNode(goalTree.getRootNode(), conn);
            PreparedStatement updateTreeStmt = conn.prepareStatement("UPDATE GOAL_TREE SET NAME = ?, DESCRIPTION = ?, ROOT_NODE = ? WHERE GOAL_TREE_ID = ?");
            updateTreeStmt.setString(1, goalTree.getName());
            updateTreeStmt.setString(2, goalTree.getDescription());
            updateTreeStmt.setLong(3, nodeID);
            updateTreeStmt.setLong(4, goalTree.getGoalTreeID());
            updateTreeStmt.executeUpdate();
            goalEvaluationStorage.backPopulateGoalTree(goalTree, conn);
            conn.commit();
        } catch (SQLException e) {
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
            Database.instance().closeConnection(conn);
        }
    }

    public void deleteGoalTree(long goalTreeID) {
        Connection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM GOAL_TREE WHERE GOAL_TREE_ID = ?");
            deleteStmt.setLong(1, goalTreeID);
            deleteStmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            LogClass.error(e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LogClass.error(e);
            }
            Database.instance().closeConnection(conn);
        }
    }

    private GoalTreeNode retrieveNode(long nodeID, Connection conn) throws SQLException {
        PreparedStatement queryNodeStmt = conn.prepareStatement("SELECT FEED_ID, GOAL_VALUE, ANALYSIS_MEASURE_ID, FILTER_ID, NAME, DESCRIPTION, high_is_good, ICON_IMAGE FROM " +
                "GOAL_TREE_NODE WHERE GOAL_TREE_NODE_ID = ?");
        queryNodeStmt.setLong(1, nodeID);
        ResultSet nodeRS = queryNodeStmt.executeQuery();
        if (nodeRS.next()) {
            GoalTreeNode goalTreeNode = new GoalTreeNode();
            long analysisMeasureID = nodeRS.getLong(3);
            if (!nodeRS.wasNull()) {
                Session session = Database.instance().createSession(conn);
                List measureResults = session.createQuery("from AnalysisItem where analysisItemID = ?").setLong(0, analysisMeasureID).list();
                if (measureResults.size() > 0) {
                    AnalysisMeasure analysisMeasure = (AnalysisMeasure) measureResults.get(0);
                    long feedID = nodeRS.getLong(1);
                    double goalValue = nodeRS.getDouble(2);
                    goalTreeNode.setAnalysisMeasure(analysisMeasure);
                    goalTreeNode.setCoreFeedID(feedID);
                    goalTreeNode.setHighIsGood(nodeRS.getBoolean(7));
                    PreparedStatement getFeedNameStmt = conn.prepareStatement("SELECT FEED_NAME FROM DATA_FEED WHERE DATA_FEED_ID = ?");
                    getFeedNameStmt.setLong(1, feedID);
                    ResultSet rs = getFeedNameStmt.executeQuery();
                    rs.next();
                    goalTreeNode.setCoreFeedName(rs.getString(1));
                    goalTreeNode.setGoalValue(goalValue);
                }
                long filterID = nodeRS.getLong(4);
                if (!nodeRS.wasNull()) {
                    List results = session.createQuery("from PersistableFilterDefinition where filterId = ?").setLong(0, filterID).list();
                    if (results.size() > 0) {
                        PersistableFilterDefinition filter = (PersistableFilterDefinition) results.get(0);
                        goalTreeNode.setFilterDefinition(filter.toFilterDefinition());
                    }
                }
            }
            String name = nodeRS.getString(5);
            String description = nodeRS.getString(6);
            String iconImage = nodeRS.getString(8);
            goalTreeNode.setName(name);
            goalTreeNode.setGoalTreeNodeID(nodeID);
            goalTreeNode.setDescription(description);
            goalTreeNode.setAssociatedFeeds(getGoalFeeds(nodeID, conn));
            goalTreeNode.setAssociatedInsights(getGoalInsights(nodeID, conn));
            goalTreeNode.setAssociatedSolutions(getGoalSolutions(nodeID, conn));
            goalTreeNode.setTags(getGoalTags(nodeID, conn));
            goalTreeNode.setIconImage(iconImage);
            PreparedStatement childQueryStmt = conn.prepareStatement("SELECT GOAL_TREE_NODE_ID FROM GOAL_TREE_NODE WHERE PARENT_GOAL_TREE_NODE_ID = ?");
            childQueryStmt.setLong(1, nodeID);
            List<GoalTreeNode> children = new ArrayList<GoalTreeNode>();
            ResultSet rs = childQueryStmt.executeQuery();
            while (rs.next()) {
                long childID = rs.getLong(1);
                GoalTreeNode childNode = retrieveNode(childID, conn);
                childNode.setParent(goalTreeNode);
                children.add(childNode);
            }
            goalTreeNode.setChildren(children);
            return goalTreeNode;
        } else {
            throw new RuntimeException("Could not find node " + nodeID);
        }
    }

    private List<GoalInsight> getGoalInsights(long goalTreeNodeID, Connection conn) throws SQLException {
        List<GoalInsight> insights = new ArrayList<GoalInsight>();
        PreparedStatement insightQueryStmt = conn.prepareStatement("SELECT INSIGHT_ID, TITLE FROM GOAL_TREE_NODE_TO_INSIGHT, ANALYSIS WHERE goal_tree_node_id = ? AND " +
                "goal_tree_node_to_insight.insight_id = analysis.analysis_id");
        insightQueryStmt.setLong(1, goalTreeNodeID);
        ResultSet rs = insightQueryStmt.executeQuery();
        while (rs.next()) {
            long insightID = rs.getLong(1);
            String name = rs.getString(2);
            GoalInsight goalInsight = new GoalInsight();
            goalInsight.setInsightID(insightID);
            goalInsight.setInsightName(name);
            insights.add(goalInsight);
        }
        return insights;
    }

    private List<GoalFeed> getGoalFeeds(long goalTreeNodeID, Connection conn) throws SQLException {
        List<GoalFeed> feeds = new ArrayList<GoalFeed>();
        PreparedStatement feedQueryStmt = conn.prepareStatement("SELECT FEED_ID, feed_name FROM GOAL_TREE_NODE_TO_FEED, DATA_FEED WHERE goal_tree_node_id = ? AND " +
                "goal_tree_node_to_feed.goal_tree_node_to_feed_id = data_feed.data_feed_id");
        feedQueryStmt.setLong(1, goalTreeNodeID);
        ResultSet rs = feedQueryStmt.executeQuery();
        while (rs.next()) {
            long feedID = rs.getLong(1);
            String name = rs.getString(2);
            GoalFeed goalFeed = new GoalFeed();
            goalFeed.setFeedID(feedID);
            goalFeed.setFeedName(name);
            feeds.add(goalFeed);
        }
        return feeds;
    }

    private List<GoalSolution> getGoalSolutions(long goalTreeNodeID, Connection conn) throws SQLException {
        List<GoalSolution> solutions = new ArrayList<GoalSolution>();
        PreparedStatement insightQueryStmt = conn.prepareStatement("SELECT GOAL_TREE_NODE_TO_SOLUTION.SOLUTION_ID, name FROM GOAL_TREE_NODE_TO_SOLUTION, SOLUTION WHERE goal_tree_node_id = ? AND " +
                "goal_tree_node_to_solution.solution_id = solution.solution_id");
        insightQueryStmt.setLong(1, goalTreeNodeID);
        ResultSet rs = insightQueryStmt.executeQuery();
        while (rs.next()) {
            long solutionID = rs.getLong(1);
            String name = rs.getString(2);
            GoalSolution goalSolution = new GoalSolution();
            goalSolution.setSolutionID(solutionID);
            goalSolution.setSolutionName(name);
            solutions.add(goalSolution);
        }
        return solutions;
    }

    private List<Tag> getGoalTags(long goalTreeNodeID, Connection conn) throws SQLException {
        List<Tag> tags = new ArrayList<Tag>();
        PreparedStatement insightQueryStmt = conn.prepareStatement("SELECT TAG FROM GOAL_TREE_NODE_TAG WHERE goal_tree_node_id = ?");
        insightQueryStmt.setLong(1, goalTreeNodeID);
        ResultSet rs = insightQueryStmt.executeQuery();
        while (rs.next()) {
            String tag = rs.getString(1);
            tags.add(new Tag(tag));
        }
        return tags;
    }

    public GoalTree retrieveGoalTree(long goalTreeID) {
        GoalTree goalTree = null;
        Connection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement retrieveGoalTreeStmt = conn.prepareStatement("SELECT NAME, DESCRIPTION, ROOT_NODE FROM GOAL_TREE WHERE GOAL_TREE_ID = ?");
            retrieveGoalTreeStmt.setLong(1, goalTreeID);
            ResultSet rs = retrieveGoalTreeStmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString(1);
                String description = rs.getString(2);
                long rootNodeID = rs.getLong(3);
                GoalTreeNode rootNode = retrieveNode(rootNodeID, conn);
                goalTree = new GoalTree();
                goalTree.setName(name);
                goalTree.setDescription(description);
                goalTree.setGoalTreeID(goalTreeID);
                goalTree.setRootNode(rootNode);
            }
            conn.commit();
        } catch (SQLException e) {
            LogClass.error(e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LogClass.error(e);
            }
            Database.instance().closeConnection(conn);
        }
        return goalTree;
    }

    private long saveGoalTreeNode(GoalTreeNode goalTreeNode, Connection conn) throws SQLException {
        long nodeID;
        if (goalTreeNode.getGoalTreeNodeID() == 0) {
            if (goalTreeNode.getAnalysisMeasure() == null) {
                PreparedStatement insertNodeStmt = conn.prepareStatement("INSERT INTO GOAL_TREE_NODE (PARENT_GOAL_TREE_NODE_ID, NAME, DESCRIPTION, ICON_IMAGE) " +
                        "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                if (goalTreeNode.getParent() == null) {
                    insertNodeStmt.setNull(1, Types.BIGINT);
                } else {
                    insertNodeStmt.setLong(1, goalTreeNode.getParent().getGoalTreeNodeID());
                }
                insertNodeStmt.setString(2, goalTreeNode.getName());
                insertNodeStmt.setString(3, goalTreeNode.getDescription());
                insertNodeStmt.setString(4, goalTreeNode.getIconImage());
                insertNodeStmt.execute();
                nodeID = Database.instance().getAutoGenKey(insertNodeStmt);
                goalTreeNode.setGoalTreeNodeID(nodeID);
            } else {
                PreparedStatement insertNodeStmt = conn.prepareStatement("INSERT INTO GOAL_TREE_NODE (PARENT_GOAL_TREE_NODE_ID, NAME, DESCRIPTION," +
                        "FEED_ID, GOAL_VALUE, ANALYSIS_MEASURE_ID, FILTER_ID, high_is_good, ICON_IMAGE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                if (goalTreeNode.getParent() == null) {
                    insertNodeStmt.setNull(1, Types.BIGINT);
                } else {
                    insertNodeStmt.setLong(1, goalTreeNode.getParent().getGoalTreeNodeID());
                }
                insertNodeStmt.setString(2, goalTreeNode.getName());
                insertNodeStmt.setString(3, goalTreeNode.getDescription());
                insertNodeStmt.setLong(4, goalTreeNode.getCoreFeedID());
                insertNodeStmt.setDouble(5, goalTreeNode.getGoalValue());
                insertNodeStmt.setLong(6, goalTreeNode.getAnalysisMeasure().getAnalysisItemID());
                if (goalTreeNode.getFilterDefinition() == null) {
                    insertNodeStmt.setNull(7, Types.BIGINT);
                } else {
                    Session session = Database.instance().createSession(conn);
                    try {
                        PersistableFilterDefinition persistableFilterDefinition = goalTreeNode.getFilterDefinition().toPersistableFilterDefinition();
                        session.save(persistableFilterDefinition);
                        insertNodeStmt.setLong(7, persistableFilterDefinition.getFilterId());
                    } finally {
                        session.close();
                    }
                }
                insertNodeStmt.setBoolean(8, goalTreeNode.isHighIsGood());
                insertNodeStmt.setString(9, goalTreeNode.getIconImage());
                System.out.println("connecting node to " + goalTreeNode.getCoreFeedID());                
                insertNodeStmt.execute();
                nodeID = Database.instance().getAutoGenKey(insertNodeStmt);
                goalTreeNode.setGoalTreeNodeID(nodeID);
            }
        } else {
            nodeID = goalTreeNode.getGoalTreeNodeID();
            PreparedStatement updateNodeStmt = conn.prepareStatement("UPDATE GOAL_TREE_NODE SET PARENT_GOAL_TREE_NODE_ID = ?, NAME = ?, DESCRIPTION = ?, FEED_ID = ?," +
                    "GOAL_VALUE = ?, ANALYSIS_MEASURE_ID = ?, FILTER_ID = ?, HIGH_IS_GOOD = ?, ICON_IMAGE = ? WHERE GOAL_TREE_NODE_ID = ?");
            if (goalTreeNode.getParent() == null) {
                updateNodeStmt.setNull(1, Types.BIGINT);
            } else {
                updateNodeStmt.setLong(1, goalTreeNode.getParent().getGoalTreeNodeID());
            }
            updateNodeStmt.setString(2, goalTreeNode.getName());
            updateNodeStmt.setString(3, goalTreeNode.getDescription());
            if (goalTreeNode.getCoreFeedID() > 0) {
                updateNodeStmt.setLong(4, goalTreeNode.getCoreFeedID());
                updateNodeStmt.setDouble(5, goalTreeNode.getGoalValue());
                updateNodeStmt.setLong(6, goalTreeNode.getAnalysisMeasure().getAnalysisItemID());
                if (goalTreeNode.getFilterDefinition() == null) {
                    updateNodeStmt.setNull(7, Types.BIGINT);
                } else {
                    Session session = Database.instance().createSession(conn);
                    try {
                        PersistableFilterDefinition persistableFilterDefinition = goalTreeNode.getFilterDefinition().toPersistableFilterDefinition();
                        session.save(persistableFilterDefinition);
                        updateNodeStmt.setLong(7, persistableFilterDefinition.getFilterId());
                    } finally {
                        session.close();
                    }
                }
                updateNodeStmt.setBoolean(8, goalTreeNode.isHighIsGood());
                updateNodeStmt.setString(9, goalTreeNode.getIconImage());
                updateNodeStmt.setLong(10, goalTreeNode.getGoalTreeNodeID());
            } else {
                updateNodeStmt.setNull(5, Types.BIGINT);
                updateNodeStmt.setNull(4, Types.BIGINT);
                updateNodeStmt.setNull(6, Types.DOUBLE);
                updateNodeStmt.setNull(7, Types.BIGINT);
                updateNodeStmt.setNull(8, Types.BOOLEAN);
                updateNodeStmt.setString(9, goalTreeNode.getIconImage());
                updateNodeStmt.setLong(10, goalTreeNode.getGoalTreeNodeID());
            }
            updateNodeStmt.executeUpdate();
        }
        saveTags(goalTreeNode, conn);
        saveInsights(goalTreeNode, conn);
        saveFeeds(goalTreeNode, conn);
        saveSolutions(goalTreeNode, conn);
        if (goalTreeNode.getChildren() != null) {
            for (GoalTreeNode childNode : goalTreeNode.getChildren()) {
                saveGoalTreeNode(childNode, conn);
            }
        }
        return nodeID;
    }

    private void saveTags(GoalTreeNode goalTreeNode, Connection conn) throws SQLException {
        PreparedStatement clearExistingStmt = conn.prepareStatement("DELETE FROM GOAL_TREE_NODE_TAG WHERE GOAL_TREE_NODE_ID = ?");
        clearExistingStmt.setLong(1, goalTreeNode.getGoalTreeNodeID());
        clearExistingStmt.executeUpdate();
        PreparedStatement saveTagStmt = conn.prepareStatement("INSERT INTO GOAL_TREE_NODE_TAG (GOAL_TREE_NODE_ID, TAG) VALUES (?, ?)");
        for (Tag tag : goalTreeNode.getTags()) {
            saveTagStmt.setLong(1, goalTreeNode.getGoalTreeNodeID());
            saveTagStmt.setString(2, tag.getTagName());
            saveTagStmt.execute();
        }
    }

    private void saveInsights(GoalTreeNode goalTreeNode, Connection conn) throws SQLException {
        PreparedStatement clearExistingStmt = conn.prepareStatement("DELETE FROM GOAL_TREE_NODE_TO_INSIGHT WHERE GOAL_TREE_NODE_ID = ?");
        clearExistingStmt.setLong(1, goalTreeNode.getGoalTreeNodeID());
        clearExistingStmt.executeUpdate();
        PreparedStatement saveInsightLinkStmt = conn.prepareStatement("INSERT INTO GOAL_TREE_NODE_TO_INSIGHT (GOAL_TREE_NODE_ID, INSIGHT_ID) VALUES (?, ?)");
        for (GoalInsight goalInsight : goalTreeNode.getAssociatedInsights()) {
            saveInsightLinkStmt.setLong(1, goalTreeNode.getGoalTreeNodeID());
            saveInsightLinkStmt.setLong(2, goalInsight.getInsightID());
            saveInsightLinkStmt.execute();
        }
    }

    private void saveFeeds(GoalTreeNode goalTreeNode, Connection conn) throws SQLException {
        PreparedStatement clearExistingStmt = conn.prepareStatement("DELETE FROM GOAL_TREE_NODE_TO_FEED WHERE GOAL_TREE_NODE_ID = ?");
        clearExistingStmt.setLong(1, goalTreeNode.getGoalTreeNodeID());
        clearExistingStmt.executeUpdate();
        PreparedStatement saveFeedLinkStmt = conn.prepareStatement("INSERT INTO GOAL_TREE_NODE_TO_FEED (GOAL_TREE_NODE_ID, FEED_ID) VALUES (?, ?)");
        for (GoalFeed goalFeed : goalTreeNode.getAssociatedFeeds()) {
            saveFeedLinkStmt.setLong(1, goalTreeNode.getGoalTreeNodeID());
            saveFeedLinkStmt.setLong(2, goalFeed.getFeedID());
            saveFeedLinkStmt.execute();
        }
    }

    private void saveSolutions(GoalTreeNode goalTreeNode, Connection conn) throws SQLException {
        PreparedStatement clearExistingStmt = conn.prepareStatement("DELETE FROM GOAL_TREE_NODE_TO_SOLUTION WHERE GOAL_TREE_NODE_ID = ?");
        clearExistingStmt.setLong(1, goalTreeNode.getGoalTreeNodeID());
        clearExistingStmt.executeUpdate();
        PreparedStatement saveSolutionLinkStmt = conn.prepareStatement("INSERT INTO GOAL_TREE_NODE_TO_SOLUTION (GOAL_TREE_NODE_ID, SOLUTION_ID) VALUES (?, ?)");
        for (GoalSolution goalSolution : goalTreeNode.getAssociatedSolutions()) {
            saveSolutionLinkStmt.setLong(1, goalTreeNode.getGoalTreeNodeID());
            saveSolutionLinkStmt.setLong(2, goalSolution.getSolutionID());
            saveSolutionLinkStmt.execute();
        }
    }

    private void installSolutions(GoalTree goalTree, final Connection conn) {
        final long userID = SecurityUtil.getUserID();
        final SolutionService solutionService = new SolutionService();
        final FeedStorage feedStorage = new FeedStorage();

        final Map<SolutionElementKey, Long> installedObjectMap = new HashMap<SolutionElementKey, Long>();

        GoalTreeVisitor solutionInstallationVisitor = new GoalTreeVisitor() {

            protected void accept(GoalTreeNode goalTreeNode) {
                try {
                    if (goalTreeNode.getNewSolutions() != null && goalTreeNode.getNewSolutions().size() > 0) {
                        List<Integer> newSolutionIDs = goalTreeNode.getNewSolutions();
                        for (Integer solutionID : newSolutionIDs) {
                            Solution solution = solutionService.getSolution(solutionID, conn);
                            List<Object> objects = solutionService.installSolution(userID, solution, conn);
                            for (Object object : objects) {
                                if (object instanceof SolutionInstallInfo) {
                                    SolutionInstallInfo feedDescriptor = (SolutionInstallInfo) object;
                                    installedObjectMap.put(new SolutionElementKey(SolutionElementKey.DATA_SOURCE, feedDescriptor.getPreviousID()), feedDescriptor.getNewID());
                                }
                            }
                        }
                    }
                } catch (SQLException e) {
                    LogClass.error(e);
                    throw new RuntimeException(e);
                } catch (CloneNotSupportedException e) {
                    LogClass.error(e);
                    throw new RuntimeException(e);
                }
            }
        };

        solutionInstallationVisitor.visit(goalTree.getRootNode());

        // once the solutions are installed...

        GoalTreeVisitor idReplacementVisitor = new GoalTreeVisitor() {

            protected void accept(GoalTreeNode goalTreeNode) {
                if (goalTreeNode.getCoreFeedID() > 0) {
                    Long newID = installedObjectMap.get(new SolutionElementKey(SolutionElementKey.DATA_SOURCE, goalTreeNode.getCoreFeedID()));
                    if (newID != null) {
                        goalTreeNode.setCoreFeedID(newID);
                        FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(newID);
                        goalTreeNode.setAnalysisMeasure((AnalysisMeasure) findItem(goalTreeNode.getAnalysisMeasure(), feedDefinition));
                        if (goalTreeNode.getFilterDefinition() != null) {
                            goalTreeNode.getFilterDefinition().setField(findItem(goalTreeNode.getFilterDefinition().getField(), feedDefinition));
                        }
                    }
                    //saveGoalTreeNode(goalTreeNode, conn);
                }
            }
        };

        idReplacementVisitor.visit(goalTree.getRootNode());
    }

    private AnalysisItem findItem(AnalysisItem analysisItem, FeedDefinition feedDefinition) {
        AnalysisItem foundItem = null;
        for (AnalysisItem feedItem : feedDefinition.getFields()) {
            if (feedItem.getKey().equals(analysisItem.getKey())) {
                foundItem = feedItem;
            }
        }
        return foundItem;
    }

    private static class SolutionElementKey {

        public static final int DATA_SOURCE = 1;
        public static final int INSIGHT = 2;

        private int type;
        private long id;

        private SolutionElementKey(int type, long id) {
            this.type = type;
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SolutionElementKey that = (SolutionElementKey) o;

            return id == that.id && type == that.type;

        }

        @Override
        public int hashCode() {
            int result = type;
            result = 31 * result + (int) (id ^ (id >>> 32));
            return result;
        }
    }
}
