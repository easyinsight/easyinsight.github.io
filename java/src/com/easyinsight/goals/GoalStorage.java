package com.easyinsight.goals;

import com.easyinsight.core.RolePrioritySet;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.analysis.*;
import com.easyinsight.datafeeds.*;
import com.easyinsight.groups.GroupDescriptor;
import com.easyinsight.email.UserStub;
import com.easyinsight.kpi.KPI;
import com.easyinsight.kpi.KPIStorage;
import com.easyinsight.scorecard.*;
import com.easyinsight.security.Roles;
import com.easyinsight.security.SecurityUtil;

import java.sql.*;
import java.util.*;

import com.easyinsight.util.RandomTextGenerator;

/**
 * User: James Boe
 * Date: Oct 23, 2008
 * Time: 3:07:00 PM
 */
public class GoalStorage {

     public KPITreeWrapper updateKPITree(List<KPI> kpis, EIConnection conn, InsightRequestMetadata insightRequestMetadata, boolean forceRefresh) throws Exception {
        KPITreeWrapper scorecardWrapper = new KPITreeWrapper();

        try {
            new ScorecardService().refreshValuesForList(kpis, conn, insightRequestMetadata, forceRefresh);
        } catch (ReportException re) {
            scorecardWrapper.setReportFault(re.getReportFault());
        }
        return scorecardWrapper;
    }

    private void populateUsers(GoalTree goalTree, Connection conn) throws SQLException {
        List<FeedConsumer> administrators = new ArrayList<FeedConsumer>();
        PreparedStatement getUsersStmt = conn.prepareStatement("SELECT USER.USER_ID, USER_ROLE, USER.email, USER.name, USER.username, USER.account_id, USER.first_name FROM " +
                "USER_TO_GOAL_TREE, USER WHERE GOAL_TREE_ID = ? AND USER_TO_GOAL_TREE.USER_ID = USER.USER_ID");
        getUsersStmt.setLong(1, goalTree.getGoalTreeID());
        ResultSet userRS = getUsersStmt.executeQuery();
        while (userRS.next()) {
            long userID = userRS.getLong(1);
            int role = userRS.getInt(2);
            String email = userRS.getString(3);
            String fullName = userRS.getString(4);
            String userName = userRS.getString(5);
            long accountID = userRS.getLong(6);
            String firstName = userRS.getString(7);
            if (role == Roles.OWNER) {
                administrators.add(new UserStub(userID, userName, email, fullName, accountID, firstName));
            }
        }
        getUsersStmt.close();
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
            }
        }
        goalTree.setAdministrators(administrators);
        getGroupsStmt.close();
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

    public RolePrioritySet<GoalTreeDescriptor> getTrees(long userID, long accountID, EIConnection conn) throws SQLException {
        RolePrioritySet<GoalTreeDescriptor> descriptors = new RolePrioritySet<GoalTreeDescriptor>();

        PreparedStatement getTreesStmt = conn.prepareStatement("SELECT NAME, GOAL_TREE.GOAL_TREE_ID, USER_ROLE, GOAL_TREE.goal_tree_icon, GOAL_TREE.URL_KEY, GOAL_TREE.DATA_SOURCE_ID FROM GOAL_TREE, user_to_goal_tree WHERE " +
                "user_id = ? AND user_to_goal_tree.goal_tree_id = goal_tree.goal_tree_id");
        getTreesStmt.setLong(1, userID);
        ResultSet rs = getTreesStmt.executeQuery();
        while (rs.next()) {
            String name = rs.getString(1);
            long treeID = rs.getLong(2);
            int role = rs.getInt(3);
            descriptors.add(new GoalTreeDescriptor(treeID, name, Roles.OWNER, rs.getString(4), rs.getString(5), rs.getLong(6)));
        }
        getTreesStmt.close();
        PreparedStatement accountStmt = conn.prepareStatement("SELECT GOAL_TREE.NAME, GOAL_TREE.GOAL_TREE_ID, USER_ROLE, GOAL_TREE.goal_tree_icon, GOAL_TREE.URL_KEY, GOAL_TREE.DATA_SOURCE_ID " +
                "FROM GOAL_TREE, user_to_goal_tree, user WHERE " +
                "user_to_goal_tree.user_id = user.user_id AND user_to_goal_tree.goal_tree_id = goal_tree.goal_tree_id and user.account_id = ? and goal_tree.account_visible = ?");
        accountStmt.setLong(1, accountID);
        accountStmt.setBoolean(2, true);
        ResultSet accountRS = accountStmt.executeQuery();
        while (accountRS.next()) {
            String name = accountRS.getString(1);
            long treeID = accountRS.getLong(2);
            int role = accountRS.getInt(3);
            descriptors.add(new GoalTreeDescriptor(treeID, name, Roles.SHARER, accountRS.getString(4), accountRS.getString(5), accountRS.getLong(6)));
        }
        accountStmt.close();
        return descriptors;
    }

    public GoalSaveInfo addGoalTree(GoalTree goalTree) throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            InstallationSystem installationSystem = new InstallationSystem(conn);
            installationSystem.installUserTree(goalTree, new ArrayList<Integer>());
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
        if (goalTree.getUrlKey() == null) {
            goalTree.setUrlKey(RandomTextGenerator.generateText(20));
        }
        PreparedStatement insertTreeStmt = conn.prepareStatement("INSERT INTO GOAL_TREE (NAME, DESCRIPTION, goal_tree_icon, URL_KEY, ACCOUNT_VISIBLE, EXCHANGE_VISIBLE, DATA_SOURCE_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        insertTreeStmt.setString(1, goalTree.getName());
        insertTreeStmt.setString(2, goalTree.getDescription());
        insertTreeStmt.setString(3, goalTree.getIconImage());
        insertTreeStmt.setString(4, goalTree.getUrlKey());
        insertTreeStmt.setBoolean(5, goalTree.isAccountVisible());
        insertTreeStmt.setBoolean(6, goalTree.isExchangeVisible());
        insertTreeStmt.setLong(7, goalTree.getDataSourceID());
        insertTreeStmt.execute();
        long treeID = Database.instance().getAutoGenKey(insertTreeStmt);
        goalTree.setGoalTreeID(treeID);
        long nodeID = saveGoalTreeNode(goalTree.getRootNode(), conn, goalTree.getGoalTreeID());
        PreparedStatement updateStmt = conn.prepareStatement("UPDATE GOAL_TREE SET ROOT_NODE = ? WHERE GOAL_TREE_ID = ?");
        updateStmt.setLong(1, nodeID);
        updateStmt.setLong(2, treeID);
        updateStmt.executeUpdate();
        saveUsers(treeID, goalTree.getAdministrators(), Roles.OWNER, conn);
        //setUserRole(userID, treeID, Roles.OWNER, conn);
        //goalEvaluationStorage.backPopulateGoalTree(goalTree, conn);
    }

    public GoalSaveInfo updateGoalTree(GoalTree goalTree) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            updateGoalTree(goalTree, conn);
            conn.commit();
            return new GoalSaveInfo(goalTree, null);
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
            installationSystem.installUserTree(goalTree, new ArrayList<Integer>());
            long nodeID = saveGoalTreeNode(goalTree.getRootNode(), conn, goalTree.getGoalTreeID());
            // deleteOldNodes(goalTree.getRootNode(), conn);
            PreparedStatement updateTreeStmt = conn.prepareStatement("UPDATE GOAL_TREE SET NAME = ?, DESCRIPTION = ?, ROOT_NODE = ?, GOAL_TREE_ICON = ?," +
                    "ACCOUNT_VISIBLE = ?, EXCHANGE_VISIBLE = ? " +
                    "WHERE GOAL_TREE_ID = ?");
            updateTreeStmt.setString(1, goalTree.getName());
            updateTreeStmt.setString(2, goalTree.getDescription());
            updateTreeStmt.setLong(3, nodeID);
            updateTreeStmt.setString(4, goalTree.getIconImage());
            updateTreeStmt.setBoolean(5, goalTree.isAccountVisible());
            updateTreeStmt.setBoolean(6, goalTree.isExchangeVisible());
            updateTreeStmt.setLong(7, goalTree.getGoalTreeID());
            updateTreeStmt.executeUpdate();
            saveUsers(goalTree.getGoalTreeID(), goalTree.getAdministrators(), Roles.OWNER, conn);
            //goalEvaluationStorage.backPopulateGoalTree(goalTree, conn);
            updateTreeStmt.close();
            return new GoalSaveInfo(goalTree, installationSystem.getAllSolutions());
    }

    public void deleteGoalTree(long goalTreeID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement deleteNodeStmt = conn.prepareStatement("DELETE FROM GOAL_TREE_NODE WHERE GOAL_TREE_ID = ?");
            deleteNodeStmt.setLong(1, goalTreeID);
            deleteNodeStmt.executeUpdate();
            deleteNodeStmt.close();
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM GOAL_TREE WHERE GOAL_TREE_ID = ?");
            deleteStmt.setLong(1, goalTreeID);
            deleteStmt.executeUpdate();
            deleteStmt.close();
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
            childQueryStmt.close();
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
            queryNodeStmt.close();
            querySubTreeStmt.close();
            return goalTreeNode;
        } else {
            throw new RuntimeException("Could not find node " + nodeID);
        }
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
        insightQueryStmt.close();
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
        PreparedStatement retrieveGoalTreeStmt = conn.prepareStatement("SELECT NAME, DESCRIPTION, ROOT_NODE, GOAL_TREE_ICON, URL_KEY, " +
                "DATA_SOURCE_ID, ACCOUNT_VISIBLE, EXCHANGE_VISIBLE FROM GOAL_TREE WHERE GOAL_TREE_ID = ?");
        retrieveGoalTreeStmt.setLong(1, goalTreeID);
        ResultSet rs = retrieveGoalTreeStmt.executeQuery();
        if (rs.next()) {
            String name = rs.getString(1);
            String description = rs.getString(2);
            long rootNodeID = rs.getLong(3);
            String iconPath = rs.getString(4);
            String urlKey = rs.getString(5);
            long dataSourceID = rs.getLong(6);
            boolean accountVisible = rs.getBoolean(7);
            boolean exchangeVisible = rs.getBoolean(8);
            GoalTreeNode rootNode = retrieveNode(rootNodeID, conn);
            goalTree = new GoalTree();
            goalTree.setName(name);
            goalTree.setDescription(description);
            goalTree.setGoalTreeID(goalTreeID);
            goalTree.setRootNode(rootNode);
            goalTree.setIconImage(iconPath);
            goalTree.setUrlKey(urlKey);
            goalTree.setDataSourceID(dataSourceID);
            goalTree.setAccountVisible(accountVisible);
            goalTree.setExchangeVisible(exchangeVisible);
            populateUsers(goalTree, conn);
        }
        retrieveGoalTreeStmt.close();
        return goalTree;
    }

    private long saveGoalTreeNode(GoalTreeNode goalTreeNode, EIConnection conn, long goalTreeID) throws Exception {
        long nodeID;
        if (goalTreeNode.getKpi() != null) {
            new KPIStorage().saveKPI(goalTreeNode.getKpi(), conn);
        }
        if (goalTreeNode.getGoalTreeNodeID() == 0) {
            PreparedStatement insertNodeStmt = conn.prepareStatement("INSERT INTO GOAL_TREE_NODE (PARENT_GOAL_TREE_NODE_ID, " +
                    "NAME, DESCRIPTION, ICON_IMAGE, GOAL_TREE_ID, SUB_TREE_ID, KPI_ID) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
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
            if (goalTreeNode.getKpi() == null) {
                insertNodeStmt.setNull(7, Types.BIGINT);
            } else {
                insertNodeStmt.setLong(7, goalTreeNode.getKpi().getKpiID());
            }
            insertNodeStmt.execute();
            nodeID = Database.instance().getAutoGenKey(insertNodeStmt);
            goalTreeNode.setGoalTreeNodeID(nodeID);
            insertNodeStmt.close();
        } else {
           nodeID = goalTreeNode.getGoalTreeNodeID();
            PreparedStatement updateNodeStmt = conn.prepareStatement("UPDATE GOAL_TREE_NODE SET PARENT_GOAL_TREE_NODE_ID = ?, NAME = ?, DESCRIPTION = ?, " +
                    "ICON_IMAGE = ?, GOAL_TREE_ID = ?, SUB_TREE_ID = ?, KPI_ID = ? " +
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
            if (goalTreeNode.getKpi() == null) {
                updateNodeStmt.setNull(7, Types.BIGINT);
            } else {
                updateNodeStmt.setLong(7, goalTreeNode.getKpi().getKpiID());
            }
            updateNodeStmt.setLong(8, goalTreeNode.getGoalTreeNodeID());
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

    public List<GoalTreeNode> getGoalsForGroup(long groupID) throws Exception {
        List<GoalTreeNode> nodes = new ArrayList<GoalTreeNode>();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT GOAL_TREE_NODE_ID FROM GROUP_TO_GOAL_TREE_NODE_JOIN WHERE GROUP_ID = ?");
            queryStmt.setLong(1, groupID);
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                long goalTreeNodeID = rs.getLong(1);
                nodes.add(retrieveNode(goalTreeNodeID, conn));                
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
                // TODO: add urlKey
                descriptors.add(new GoalTreeDescriptor(subTreeRS.getLong(1), subTreeRS.getString(2), subTreeRS.getInt(3), subTreeRS.getString(4), null, 0));
            }
            goalTree.setSubTreeParents(descriptors);
            queryStmt.close();
        } finally {
            Database.closeConnection(conn);
        }

    }
}
