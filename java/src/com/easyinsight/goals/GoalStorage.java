package com.easyinsight.goals;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.analysis.*;
import com.easyinsight.groups.GroupDescriptor;
import com.easyinsight.email.UserStub;
import com.easyinsight.datafeeds.FeedConsumer;
import com.easyinsight.kpi.KPIStorage;
import com.easyinsight.security.Roles;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.core.InsightDescriptor;

import java.sql.*;
import java.util.*;
import java.util.Date;

import org.hibernate.Session;
import org.jetbrains.annotations.Nullable;

/**
 * User: James Boe
 * Date: Oct 23, 2008
 * Time: 3:07:00 PM
 */
public class GoalStorage {

    public void deleteMilestone(long milestoneID) throws SQLException {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM MILESTONE WHERE MILESTONE_ID = ?");
            deleteStmt.setLong(1, milestoneID);
            deleteStmt.executeUpdate();
        } finally {
            Database.closeConnection(conn);
        }
    }

    public List<GoalTreeMilestone> getMilestones(long accountID) throws SQLException {
        List<GoalTreeMilestone> milestones = new ArrayList<GoalTreeMilestone>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT MILESTONE_ID, MILESTONE_NAME, MILESTONE_DATE FROM " +
                    "MILESTONE WHERE ACCOUNT_ID = ?");
            queryStmt.setLong(1, accountID);
            ResultSet milestoneRS = queryStmt.executeQuery();
            while (milestoneRS.next()) {
                long milestoneID = milestoneRS.getLong(1);
                String milestoneName = milestoneRS.getString(2);
                Date milestoneDate = new Date(milestoneRS.getDate(3).getTime());
                milestones.add(new GoalTreeMilestone(milestoneDate, milestoneID, milestoneName));
            }
        } finally {
            Database.closeConnection(conn);
        }
        return milestones;
    }

    @Nullable
    private GoalTreeMilestone getMilestoneForGoal(long goalMilestoneID, Connection conn) throws SQLException {
        GoalTreeMilestone goalTreeMilestone = null;
        PreparedStatement queryStmt = conn.prepareStatement("SELECT MILESTONE_DATE, MILESTONE_NAME FROM MILESTONE WHERE MILESTONE_ID = ?");
        queryStmt.setLong(1, goalMilestoneID);
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            goalTreeMilestone = new GoalTreeMilestone(new Date(rs.getDate(1).getTime()), goalMilestoneID, rs.getString(2));
        }
        return goalTreeMilestone;
    }

    public long addMilestone(GoalTreeMilestone goalTreeMilestone, long accountID) throws SQLException {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO MILESTONE (MILESTONE_NAME, MILESTONE_DATE, ACCOUNT_ID) vALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            insertStmt.setString(1, goalTreeMilestone.getMilestoneName());
            insertStmt.setDate(2, new java.sql.Date(goalTreeMilestone.getMilestoneDate().getTime()));
            insertStmt.setLong(3, accountID);
            insertStmt.execute();
            long id = Database.instance().getAutoGenKey(insertStmt);
            goalTreeMilestone.setMilestoneID(id);
            return id;
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void updateMilestone(GoalTreeMilestone goalTreeMilestone) throws SQLException {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE MILESTONE SET MILESTONE_NAME = ? AND MILESTONE_DATE = ? WHERE " +
                    "GOAL_TREE_MILESTONE_ID = ?");
            updateStmt.setString(1, goalTreeMilestone.getMilestoneName());
            updateStmt.setDate(2, new java.sql.Date(goalTreeMilestone.getMilestoneDate().getTime()));
            updateStmt.setLong(3, goalTreeMilestone.getMilestoneID());
            updateStmt.executeUpdate();
        } finally {
            Database.closeConnection(conn);
        }

    }

    private void populateUsers(GoalTree goalTree, Connection conn) throws SQLException {
        List<FeedConsumer> administrators = new ArrayList<FeedConsumer>();
        List<FeedConsumer> consumers = new ArrayList<FeedConsumer>();
        PreparedStatement getUsersStmt = conn.prepareStatement("SELECT USER.USER_ID, USER_ROLE, USER.email, USER.name, USER.username FROM " +
                "USER_TO_GOAL_TREE, USER WHERE GOAL_TREE_ID = ? AND USER_TO_GOAL_TREE.USER_ID = USER.USER_ID");
        getUsersStmt.setLong(1, goalTree.getGoalTreeID());
        ResultSet userRS = getUsersStmt.executeQuery();
        while (userRS.next()) {
            long userID = userRS.getLong(1);
            int role = userRS.getInt(2);
            String email = userRS.getString(3);
            String fullName = userRS.getString(4);
            String userName = userRS.getString(5);
            if (role == Roles.OWNER) {
                administrators.add(new UserStub(userID, userName, email, fullName));
            } else {
                consumers.add(new UserStub(userID, userName, email, fullName));
            }
        }
        PreparedStatement getGroupsStmt = conn.prepareStatement("SELECT COMMUNITY_GROUP.COMMUNITY_GROUP_ID, ROLE, COMMUNITY_GROUP.name " +
                "FROM GOAL_TREE_TO_GROUP, COMMUNITY_GROUP WHERE GOAL_TREE_ID = ? AND " +
                "COMMUNITY_GROUP.COMMUNITY_GROUP_ID = GOAL_TREE_TO_GROUP.GROUP_ID");
        getGroupsStmt.setLong(1, goalTree.getGoalTreeID());
        ResultSet groupRS = getGroupsStmt.executeQuery();
        while (groupRS.next()) {
            long groupID = groupRS.getLong(1);
            int role = groupRS.getInt(2);
            String name = groupRS.getString(3);
            if (role == Roles.OWNER) {
                administrators.add(new GroupDescriptor(name, groupID, 0, null));
            } else {
                consumers.add(new GroupDescriptor(name, groupID, 0, null));
            }
        }
        goalTree.setAdministrators(administrators);
        goalTree.setConsumers(consumers);
    }

    private void saveUsers(long goalTreeID, List<FeedConsumer> users, int role, Connection conn) throws SQLException {
        PreparedStatement deleteUsersStmt = conn.prepareStatement("DELETE FROM USER_TO_GOAL_TREE WHERE GOAL_TREE_ID = ? AND USER_ROLE = ?");
        deleteUsersStmt.setLong(1, goalTreeID);
        deleteUsersStmt.setInt(2, role);
        deleteUsersStmt.executeUpdate();
        PreparedStatement deleteGroupsStmt = conn.prepareStatement("DELETE FROM GOAL_TREE_TO_GROUP WHERE GOAL_TREE_ID = ? AND ROLE = ?");
        deleteGroupsStmt.setLong(1, goalTreeID);
        deleteGroupsStmt.setInt(2, role);
        deleteGroupsStmt.executeUpdate();
        PreparedStatement addUserStmt = conn.prepareStatement("INSERT INTO USER_TO_GOAL_TREE (USER_ID, goal_tree_id, user_role) VALUES (?, ?, ?)");
        PreparedStatement addGroupStmt = conn.prepareStatement("INSERT INTO GOAL_TREE_TO_GROUP (GROUP_ID, goal_tree_id, role) VALUES (?, ?, ?)");
        for (FeedConsumer feedConsumer : users) {
            if (feedConsumer.type() == FeedConsumer.USER) {
                UserStub userStub = (UserStub) feedConsumer;
                addUserStmt.setLong(1, userStub.getUserID());
                addUserStmt.setLong(2, goalTreeID);
                addUserStmt.setInt(3, role);
                addUserStmt.execute();
            } else if (feedConsumer.type() == FeedConsumer.GROUP) {
                GroupDescriptor groupDescriptor = (GroupDescriptor) feedConsumer;
                addGroupStmt.setLong(1, groupDescriptor.getGroupID());
                addGroupStmt.setLong(2, goalTreeID);
                addGroupStmt.setInt(3, role);
                addGroupStmt.execute();
            }
        }
        addUserStmt.close();
        addGroupStmt.close();
    }

    public List<GoalTreeDescriptor> getTreesForUser(long userID) throws SQLException {
        List<GoalTreeDescriptor> descriptors = new ArrayList<GoalTreeDescriptor>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement getTreesStmt = conn.prepareStatement("SELECT NAME, GOAL_TREE.GOAL_TREE_ID, USER_ROLE, GOAL_TREE.GOAL_TREE_ICON FROM GOAL_TREE, user_to_goal_tree WHERE " +
                    "user_id = ? AND user_to_goal_tree.goal_tree_id = goal_tree.goal_tree_id");
            getTreesStmt.setLong(1, userID);
            ResultSet rs = getTreesStmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString(1);
                long treeID = rs.getLong(2);
                int role = rs.getInt(3);
                descriptors.add(new GoalTreeDescriptor(treeID, name, role, rs.getString(4)));
            }
        } finally {
            Database.closeConnection(conn);
        }
        return descriptors;
    }

    public GoalSaveInfo addGoalTree(GoalTree goalTree) throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            InstallationSystem installationSystem = new InstallationSystem(conn);
            installationSystem.installUserTree(goalTree, goalTree.getNewSolutions());
            addGoalTree(goalTree, conn);
            conn.commit();
            return new GoalSaveInfo(goalTree, installationSystem.getAllSolutions());
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public void addGoalTree(GoalTree goalTree, EIConnection conn) throws Exception {
        if (goalTree.getRootNode() == null) {
            throw new RuntimeException("You must have a root node on a goal tree.");
        }
        PreparedStatement insertTreeStmt = conn.prepareStatement("INSERT INTO GOAL_TREE (NAME, DESCRIPTION, goal_tree_icon) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        insertTreeStmt.setString(1, goalTree.getName());
        insertTreeStmt.setString(2, goalTree.getDescription());
        insertTreeStmt.setString(3, goalTree.getIconImage());
        insertTreeStmt.execute();
        long treeID = Database.instance().getAutoGenKey(insertTreeStmt);
        goalTree.setGoalTreeID(treeID);
        long nodeID = saveGoalTreeNode(goalTree.getRootNode(), conn, goalTree.getGoalTreeID());
        PreparedStatement updateStmt = conn.prepareStatement("UPDATE GOAL_TREE SET ROOT_NODE = ? WHERE GOAL_TREE_ID = ?");
        updateStmt.setLong(1, nodeID);
        updateStmt.setLong(2, treeID);
        updateStmt.executeUpdate();
        saveUsers(treeID, goalTree.getAdministrators(), Roles.OWNER, conn);
        saveUsers(treeID, goalTree.getConsumers(), Roles.SUBSCRIBER, conn);
        //setUserRole(userID, treeID, Roles.OWNER, conn);
        //goalEvaluationStorage.backPopulateGoalTree(goalTree, conn);
    }

    public GoalSaveInfo updateGoalTree(GoalTree goalTree) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            InstallationSystem installationSystem = new InstallationSystem(conn);
            installationSystem.installUserTree(goalTree, goalTree.getNewSolutions());
            long nodeID = saveGoalTreeNode(goalTree.getRootNode(), conn, goalTree.getGoalTreeID());
            deleteOldNodes(goalTree.getRootNode(), conn);
            PreparedStatement updateTreeStmt = conn.prepareStatement("UPDATE GOAL_TREE SET NAME = ?, DESCRIPTION = ?, ROOT_NODE = ?, GOAL_TREE_ICON = ? " +
                    "WHERE GOAL_TREE_ID = ?");
            updateTreeStmt.setString(1, goalTree.getName());
            updateTreeStmt.setString(2, goalTree.getDescription());
            updateTreeStmt.setLong(3, nodeID);
            updateTreeStmt.setString(4, goalTree.getIconImage());
            updateTreeStmt.setLong(5, goalTree.getGoalTreeID());
            updateTreeStmt.executeUpdate();
            saveUsers(goalTree.getGoalTreeID(), goalTree.getAdministrators(), Roles.OWNER, conn);
            saveUsers(goalTree.getGoalTreeID(), goalTree.getConsumers(), Roles.SUBSCRIBER, conn);
            //goalEvaluationStorage.backPopulateGoalTree(goalTree, conn);
            conn.commit();
            return new GoalSaveInfo(goalTree, installationSystem.getAllSolutions());
        } catch (Exception e) {
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public GoalSaveInfo updateGoalTree(GoalTree goalTree, EIConnection conn) throws Exception {

            InstallationSystem installationSystem = new InstallationSystem(conn);
            installationSystem.installUserTree(goalTree, goalTree.getNewSolutions());
            long nodeID = saveGoalTreeNode(goalTree.getRootNode(), conn, goalTree.getGoalTreeID());
            // deleteOldNodes(goalTree.getRootNode(), conn);
            PreparedStatement updateTreeStmt = conn.prepareStatement("UPDATE GOAL_TREE SET NAME = ?, DESCRIPTION = ?, ROOT_NODE = ?, GOAL_TREE_ICON = ? " +
                    "WHERE GOAL_TREE_ID = ?");
            updateTreeStmt.setString(1, goalTree.getName());
            updateTreeStmt.setString(2, goalTree.getDescription());
            updateTreeStmt.setLong(3, nodeID);
            updateTreeStmt.setString(4, goalTree.getIconImage());
            updateTreeStmt.setLong(5, goalTree.getGoalTreeID());
            updateTreeStmt.executeUpdate();
            saveUsers(goalTree.getGoalTreeID(), goalTree.getAdministrators(), Roles.OWNER, conn);
            saveUsers(goalTree.getGoalTreeID(), goalTree.getConsumers(), Roles.SUBSCRIBER, conn);
            //goalEvaluationStorage.backPopulateGoalTree(goalTree, conn);
            
            return new GoalSaveInfo(goalTree, installationSystem.getAllSolutions());
    }

    public void deleteGoalTree(long goalTreeID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement deleteNodeStmt = conn.prepareStatement("DELETE FROM GOAL_TREE_NODE WHERE GOAL_TREE_ID = ?");
            deleteNodeStmt.setLong(1, goalTreeID);
            deleteNodeStmt.executeUpdate();
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM GOAL_TREE WHERE GOAL_TREE_ID = ?");
            deleteStmt.setLong(1, goalTreeID);
            deleteStmt.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    GoalTreeNode retrieveNode(long nodeID, EIConnection conn) throws Exception {
        PreparedStatement queryNodeStmt = conn.prepareStatement("SELECT NAME, DESCRIPTION, ICON_IMAGE, KPI_ID FROM " +
                "GOAL_TREE_NODE WHERE GOAL_TREE_NODE_ID = ?");
        PreparedStatement querySubTreeStmt = conn.prepareStatement("SELECT SUB_TREE_ID, GOAL_TREE.NAME, GOAL_TREE.goal_tree_icon FROM GOAL_TREE_NODE, GOAL_TREE WHERE " +
                "GOAL_TREE_NODE.SUB_TREE_ID = GOAL_TREE.GOAL_TREE_ID AND GOAL_TREE_NODE.GOAL_TREE_NODE_ID = ?");
        queryNodeStmt.setLong(1, nodeID);
        ResultSet nodeRS = queryNodeStmt.executeQuery();
        if (nodeRS.next()) {
            GoalTreeNode goalTreeNode = new GoalTreeNode();
            String name = nodeRS.getString(1);
            String description = nodeRS.getString(2);
            String iconImage = nodeRS.getString(3);
            long kpiID = nodeRS.getLong(4);
            if (!nodeRS.wasNull()) {
                goalTreeNode.setKpi(new KPIStorage().getKPI(kpiID, conn));
            }


            goalTreeNode.setName(name);
            goalTreeNode.setGoalTreeNodeID(nodeID);
            goalTreeNode.setDescription(description);

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
            querySubTreeStmt.setLong(1, nodeID);
            ResultSet subTreeRS = querySubTreeStmt.executeQuery();
            if (subTreeRS.next()) {
                long subTreeID = subTreeRS.getLong(1);
                String subTreeName = subTreeRS.getString(2);
                String treeIconImage = subTreeRS.getString(3);
                goalTreeNode.setSubTreeID(subTreeID);
                goalTreeNode.setSubTreeName(subTreeName);
                goalTreeNode.setSubTreeIcon(treeIconImage);
            }
            return goalTreeNode;
        } else {
            throw new RuntimeException("Could not find node " + nodeID);
        }
    }

    private List<InsightDescriptor> getGoalInsights(long goalTreeNodeID, Connection conn) throws SQLException {
        List<InsightDescriptor> insights = new ArrayList<InsightDescriptor>();
        PreparedStatement insightQueryStmt = conn.prepareStatement("SELECT INSIGHT_ID, TITLE, DATA_FEED_ID, REPORT_TYPE  FROM GOAL_TREE_NODE_TO_INSIGHT, ANALYSIS WHERE goal_tree_node_id = ? AND " +
                "goal_tree_node_to_insight.insight_id = analysis.analysis_id");
        insightQueryStmt.setLong(1, goalTreeNodeID);
        ResultSet rs = insightQueryStmt.executeQuery();
        while (rs.next()) {
            long insightID = rs.getLong(1);
            String name = rs.getString(2);
            InsightDescriptor goalInsight = new InsightDescriptor(insightID, name, rs.getLong(3), rs.getInt(4));
            insights.add(goalInsight);
        }
        return insights;
    }

    private List<Integer> getGoalUsers(long goalTreeNodeID, Connection conn) throws SQLException {
        List<Integer> users = new ArrayList<Integer>();
        PreparedStatement feedQueryStmt = conn.prepareStatement("SELECT USER_ID FROM GOAL_NODE_TO_USER WHERE goal_tree_node_id = ?");
        feedQueryStmt.setLong(1, goalTreeNodeID);
        ResultSet rs = feedQueryStmt.executeQuery();
        while (rs.next()) {
            int userID = rs.getInt(1);
            users.add(userID);
        }
        return users;
    }

    private List<FilterDefinition> getGoalFilters(long goalTreeNodeID, Connection conn) throws SQLException {
        List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
        PreparedStatement feedQueryStmt = conn.prepareStatement("SELECT FILTER_ID FROM GOAL_TO_FILTER WHERE goal_tree_node_id = ?");
        feedQueryStmt.setLong(1, goalTreeNodeID);
        ResultSet rs = feedQueryStmt.executeQuery();
        Session session = Database.instance().createSession(conn);
        try {
            while (rs.next()) {
                List results = session.createQuery("from FilterDefinition where filterID = ?").setLong(0, rs.getLong(1)).list();
                if (results.size() > 0) {
                    FilterDefinition filter = (FilterDefinition) results.get(0);
                    filter.getField().afterLoad();
                    filter.afterLoad();
                    filters.add(filter);
                }
            }
        } finally {
            session.close();
        }
        return filters;
    }

    private List<FilterDefinition> getGoalProblemFilters(long goalTreeNodeID, Connection conn) throws SQLException {
        List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
        PreparedStatement feedQueryStmt = conn.prepareStatement("SELECT FILTER_ID FROM GOAL_TO_PROBLEM_FILTER WHERE goal_tree_node_id = ?");
        feedQueryStmt.setLong(1, goalTreeNodeID);
        ResultSet rs = feedQueryStmt.executeQuery();
        Session session = Database.instance().createSession(conn);
        try {
            while (rs.next()) {
                List results = session.createQuery("from FilterDefinition where filterID = ?").setLong(0, rs.getLong(1)).list();
                if (results.size() > 0) {
                    FilterDefinition filter = (FilterDefinition) results.get(0);
                    filter.getField().afterLoad();
                    filter.afterLoad();
                    filters.add(filter);
                }
            }
        } finally {
            session.close();
        }
        return filters;
    }

    private List<GoalFeed> getGoalFeeds(long goalTreeNodeID, Connection conn) throws SQLException {
        List<GoalFeed> feeds = new ArrayList<GoalFeed>();
        PreparedStatement feedQueryStmt = conn.prepareStatement("SELECT FEED_ID, feed_name FROM GOAL_TREE_NODE_TO_FEED, DATA_FEED WHERE goal_tree_node_id = ? AND " +
                "goal_tree_node_to_feed.feed_id = data_feed.data_feed_id");
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
        GoalTree goalTree;
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            goalTree = retrieveGoalTree(goalTreeID, conn);
            conn.commit();
            return goalTree;
        } catch (Exception e) {
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public GoalTree retrieveGoalTree(long goalTreeID, EIConnection conn) throws Exception {
        GoalTree goalTree = null;
        PreparedStatement retrieveGoalTreeStmt = conn.prepareStatement("SELECT NAME, DESCRIPTION, ROOT_NODE, GOAL_TREE_ICON FROM GOAL_TREE WHERE GOAL_TREE_ID = ?");
        retrieveGoalTreeStmt.setLong(1, goalTreeID);
        ResultSet rs = retrieveGoalTreeStmt.executeQuery();
        if (rs.next()) {
            String name = rs.getString(1);
            String description = rs.getString(2);
            long rootNodeID = rs.getLong(3);
            String iconPath = rs.getString(4);
            GoalTreeNode rootNode = retrieveNode(rootNodeID, conn);
            goalTree = new GoalTree();
            goalTree.setName(name);
            goalTree.setDescription(description);
            goalTree.setGoalTreeID(goalTreeID);
            goalTree.setRootNode(rootNode);
            goalTree.setIconImage(iconPath);
            populateUsers(goalTree, conn);
        }
        return goalTree;
    }

    private long saveGoalTreeNode(GoalTreeNode goalTreeNode, EIConnection conn, long goalTreeID) throws Exception {
        long nodeID;
        if (goalTreeNode.getKpi() != null) {
            new KPIStorage().saveKPI(goalTreeNode.getKpi(), conn);
        }
        if (goalTreeNode.getGoalTreeNodeID() == 0) {
            PreparedStatement insertNodeStmt = conn.prepareStatement("INSERT INTO GOAL_TREE_NODE (PARENT_GOAL_TREE_NODE_ID, NAME, DESCRIPTION, ICON_IMAGE, GOAL_TREE_ID, SUB_TREE_ID) " +
                        "VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            if (goalTreeNode.getParent() == null) {
                insertNodeStmt.setNull(1, Types.BIGINT);
            } else {
                insertNodeStmt.setLong(1, goalTreeNode.getParent().getGoalTreeNodeID());
            }
            insertNodeStmt.setString(2, goalTreeNode.getName());
            insertNodeStmt.setString(3, goalTreeNode.getDescription());
            insertNodeStmt.setString(4, goalTreeNode.getIconImage());
            insertNodeStmt.setLong(5, goalTreeID);
            if (goalTreeNode.getSubTreeID() == 0) {
                insertNodeStmt.setNull(6, Types.BIGINT);
            } else {
                insertNodeStmt.setLong(6, goalTreeNode.getSubTreeID());
            }
            insertNodeStmt.execute();
            nodeID = Database.instance().getAutoGenKey(insertNodeStmt);
            goalTreeNode.setGoalTreeNodeID(nodeID);
            insertNodeStmt.close();
        } else {
           nodeID = goalTreeNode.getGoalTreeNodeID();
            PreparedStatement updateNodeStmt = conn.prepareStatement("UPDATE GOAL_TREE_NODE SET PARENT_GOAL_TREE_NODE_ID = ?, NAME = ?, DESCRIPTION = ?, " +
                    "ICON_IMAGE = ?, GOAL_TREE_ID = ?, SUB_TREE_ID = ? " +
                    "WHERE GOAL_TREE_NODE_ID = ?");
            if (goalTreeNode.getParent() == null) {
                updateNodeStmt.setNull(1, Types.BIGINT);
            } else {
                updateNodeStmt.setLong(1, goalTreeNode.getParent().getGoalTreeNodeID());
            }
            updateNodeStmt.setString(2, goalTreeNode.getName());
            updateNodeStmt.setString(3, goalTreeNode.getDescription());
            updateNodeStmt.setString(4, goalTreeNode.getIconImage());
            updateNodeStmt.setLong(5, goalTreeID);

            if (goalTreeNode.getSubTreeID() == 0) {
                updateNodeStmt.setNull(6, Types.BIGINT);
            } else {
                updateNodeStmt.setLong(6, goalTreeNode.getSubTreeID());
            }
            updateNodeStmt.setLong(7, goalTreeNode.getGoalTreeNodeID());
            updateNodeStmt.executeUpdate();
            updateNodeStmt.close();

        }
        saveTags(goalTreeNode, conn);
        if (goalTreeNode.getChildren() != null) {
            for (GoalTreeNode childNode : goalTreeNode.getChildren()) {
                saveGoalTreeNode(childNode, conn, goalTreeID);
            }
        }
        return nodeID;
    }

    private void deleteOldNodes(GoalTreeNode goalTreeNode, Connection conn) throws SQLException {
        if (goalTreeNode.getChildren() != null) {
            for (GoalTreeNode child : goalTreeNode.getChildren()) {
                deleteOldNodes(child, conn);
            }
        }
        PreparedStatement queryStmt = conn.prepareStatement("SELECT GOAL_TREE_NODE_ID FROM GOAL_TREE_NODE WHERE PARENT_GOAL_TREE_NODE_ID = ?");
        queryStmt.setLong(1, goalTreeNode.getGoalTreeNodeID());
        Set<Long> deleteIDs = new HashSet<Long>();
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            long childID = rs.getLong(1);
            boolean found = false;
            for (GoalTreeNode child : goalTreeNode.getChildren()) {
                if (child.getGoalTreeNodeID() == childID) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                deleteIDs.add(childID);
            }
        }
        if (!deleteIDs.isEmpty()) {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM GOAL_TREE_NODE WHERE GOAL_TREE_NODE_ID = ?");
            for (long id : deleteIDs) {
                deleteStmt.setLong(1, id);
                deleteStmt.executeUpdate();
            }
            deleteStmt.close();
        }
        queryStmt.close();
    }

    public boolean addUserToGoal(long userID, long goalTreeNodeID) throws SQLException {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT goal_node_to_user_id from goal_node_to_user where goal_tree_node_id = ? AND " +
                    "user_id = ?");
            queryStmt.setLong(1, goalTreeNodeID);
            queryStmt.setLong(2, userID);
            ResultSet rs = queryStmt.executeQuery();
            if (!rs.next()) {
                PreparedStatement saveUserStmt = conn.prepareStatement("INSERT INTO goal_node_to_user (GOAL_TREE_NODE_ID, user_id) VALUES (?, ?)");
                saveUserStmt.setLong(1, goalTreeNodeID);
                saveUserStmt.setLong(2, userID);
                saveUserStmt.execute();
                saveUserStmt.close();
                return true;
            }
            return false;
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void removeUserFromGoal(long userID, long goalTreeNodeID) throws SQLException {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement saveUserStmt = conn.prepareStatement("DELETE FROM goal_node_to_user WHERE GOAL_TREE_NODE_ID = ? AND user_id = ?");
            saveUserStmt.setLong(1, goalTreeNodeID);
            saveUserStmt.setLong(2, userID);
            saveUserStmt.executeUpdate();
            saveUserStmt.close();
        } finally {
            Database.closeConnection(conn);
        }
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
        saveTagStmt.close();
    }

    public List<GoalTreeNodeData> getGoalsForUser(long userID) throws Exception {
        List<GoalTreeNodeData> nodes = new ArrayList<GoalTreeNodeData>();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT GOAL_TREE_NODE_ID FROM goal_node_to_user WHERE USER_ID = ?");
            stmt.setLong(1, userID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                long goalTreeNodeID = rs.getLong(1);
                populateGoal(nodes, conn, goalTreeNodeID);
            }
            stmt.close();
        } finally {
            Database.closeConnection(conn);
        }
        return nodes;
    }

    private void populateGoal(List<GoalTreeNodeData> nodes, EIConnection conn, long goalTreeNodeID) throws Exception {
        GoalTreeNode goalTreeNode = retrieveNode(goalTreeNodeID, conn);
        GoalTreeNodeData dataNode = new GoalTreeNodeDataBuilder().build(goalTreeNode);
        nodes.add(dataNode);
        GoalTreeVisitor visitor = new GoalTreeVisitor() {

            protected void accept(GoalTreeNode goalTreeNode) {
                GoalTreeNodeData data = (GoalTreeNodeData) goalTreeNode;
                data.populateCurrentValue();
            }
        };
        visitor.visit(dataNode);
        dataNode.summarizeOutcomes();
    }

    public List<GoalTreeNodeData> getGoalsForGroup(long groupID) throws Exception {
        List<GoalTreeNodeData> nodes = new ArrayList<GoalTreeNodeData>();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT GOAL_TREE_NODE_ID FROM GROUP_TO_GOAL_TREE_NODE_JOIN WHERE GROUP_ID = ?");
            queryStmt.setLong(1, groupID);
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                long goalTreeNodeID = rs.getLong(1);
                populateGoal(nodes, conn, goalTreeNodeID);
            }
            queryStmt.close();
        } finally {
            Database.closeConnection(conn);
        }
        return nodes;
    }

    public void decorateDataTree(GoalTree goalTree) throws SQLException {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT GOAL_TREE.GOAL_TREE_ID, GOAL_TREE.NAME, user_role, GOAL_TREE.GOAL_TREE_ICON FROM " +
                    "GOAL_TREE, GOAL_TREE_NODE, user_to_goal_tree WHERE GOAL_TREE_NODE.SUB_TREE_ID = ? AND GOAL_TREE_NODE.GOAL_TREE_ID = GOAL_TREE.GOAL_TREE_ID AND " +
                    "user_id = ? AND user_to_goal_tree.goal_tree_id = goal_tree.goal_tree_id");
            queryStmt.setLong(1, goalTree.getGoalTreeID());
            queryStmt.setLong(2, SecurityUtil.getUserID());
            List<GoalTreeDescriptor> descriptors = new ArrayList<GoalTreeDescriptor>();
            ResultSet subTreeRS = queryStmt.executeQuery();
            while (subTreeRS.next()) {
                descriptors.add(new GoalTreeDescriptor(subTreeRS.getLong(1), subTreeRS.getString(2), subTreeRS.getInt(3), subTreeRS.getString(4)));
            }
            goalTree.setSubTreeParents(descriptors);
            queryStmt.close();
        } finally {
            Database.closeConnection(conn);
        }

    }
}
