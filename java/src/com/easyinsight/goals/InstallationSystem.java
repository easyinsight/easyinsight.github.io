package com.easyinsight.goals;

import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.Roles;
import com.easyinsight.security.AuthorizationRequirement;
import com.easyinsight.solutions.SolutionService;
import com.easyinsight.solutions.SolutionElementKey;
import com.easyinsight.solutions.SolutionInstallInfo;
import com.easyinsight.solutions.Solution;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedConsumer;
import com.easyinsight.datafeeds.DataSourceCopyUtils;
import com.easyinsight.core.EIDescriptor;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.PersistableFilterDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.logging.LogClass;
import com.easyinsight.notifications.ConfigureDataFeedTodo;
import com.easyinsight.notifications.ConfigureDataFeedInfo;
import com.easyinsight.notifications.TodoEventInfo;
import com.easyinsight.eventing.MessageUtils;
import com.easyinsight.email.UserStub;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
 * User: James Boe
 * Date: Jul 2, 2009
 * Time: 2:55:01 PM
 */
public class InstallationSystem {

    private Map<SolutionElementKey, Long> installedObjectMap = new HashMap<SolutionElementKey, Long>();
    private List<SolutionInstallInfo> allSolutions = new ArrayList<SolutionInstallInfo>();
    private List<AuthorizationRequirement> authRequirements = new ArrayList<AuthorizationRequirement>();
    private Connection conn;
    private long userID;
    private GoalStorage goalStorage = new GoalStorage();
    private FeedStorage feedStorage = new FeedStorage();
    private SolutionService solutionService = new SolutionService();

    public InstallationSystem(Connection conn) {
        this.conn = conn;
        this.userID = SecurityUtil.getUserID();
    }

    public List<SolutionInstallInfo> getAllSolutions() {
        return allSolutions;
    }

    public List<AuthorizationRequirement> getAuthRequirements() {
        return authRequirements;
    }

    public void installSolution(Solution solution) throws SQLException, CloneNotSupportedException {
        allSolutions.addAll(generateFeedsForSolution(solution.getSolutionID(), userID, conn, solution.isCopyData()));
        if (solution.getGoalTreeID() > 0) {
            duplicateTree(solution.getGoalTreeID());
        }
    }

    public void installUserTree(GoalTree goalTree, List<Integer> inlineSolutionIDs) throws SQLException, CloneNotSupportedException {

        for (Integer solutionID : inlineSolutionIDs) {
            Solution solution = solutionService.getSolution(solutionID, conn);
            GoalTree solutionTree = goalStorage.retrieveGoalTree(solution.getGoalTreeID(), conn);
            duplicateGoalDataSources(solutionTree);
            allSolutions.addAll(generateFeedsForSolution(solutionID, userID, conn, solution.isCopyData()));
        }

        for (SolutionInstallInfo solutionInstallInfo : allSolutions) {
            installedObjectMap.put(new SolutionElementKey(solutionInstallInfo.getDescriptor().getType(), solutionInstallInfo.getPreviousID()), solutionInstallInfo.getDescriptor().getId());
        }

        goalTree.setNewSolutions(new ArrayList<Integer>());

        installSubTrees(goalTree);

        installDataSourcesAndReports(goalTree);

        //sendTodos();
    }

    private long duplicateTree(long goalTreeID) throws SQLException, CloneNotSupportedException {
        GoalTree goalTree = new GoalStorage().retrieveGoalTree(goalTreeID, conn);
        GoalTree clonedTree = goalTree.clone();
        FeedConsumer feedConsumer = new UserStub(userID, null, null, null);
        clonedTree.setAdministrators(Arrays.asList(feedConsumer));
        duplicateGoalDataSources(goalTree);
        installDataSourcesAndReports(clonedTree);
        goalStorage.addGoalTree(clonedTree, conn);
        allSolutions.add(new SolutionInstallInfo(goalTree.getGoalTreeID(), new GoalTreeDescriptor(clonedTree.getGoalTreeID(), clonedTree.getName(), Roles.OWNER,
                clonedTree.getIconImage()), null, false));        
        return clonedTree.getGoalTreeID();
    }

    private void sendTodos() {
        for (SolutionInstallInfo info : allSolutions) {
            if (info.getDescriptor().getType() == EIDescriptor.DATA_SOURCE && info.getTodoItem() != null) {
                ConfigureDataFeedTodo todo = info.getTodoItem();
                ConfigureDataFeedInfo todoInfo = new ConfigureDataFeedInfo();
                todoInfo.setTodoID(todo.getId());
                todoInfo.setAction(TodoEventInfo.ADD);
                todoInfo.setUserId(todo.getUserID());
                todoInfo.setFeedID(todo.getFeedID());
                todoInfo.setFeedName(info.getFeedName());
                MessageUtils.sendMessage("generalNotifications", todoInfo);
            }
        }
    }

    private void installDataSourcesAndReports(GoalTree goalTree) throws SQLException, CloneNotSupportedException {



        GoalTreeVisitor idReplacementVisitor = new GoalTreeVisitor() {

            protected void accept(GoalTreeNode goalTreeNode) {
                if (goalTreeNode.getCoreFeedID() > 0) {
                    Long newID = installedObjectMap.get(new SolutionElementKey(SolutionElementKey.DATA_SOURCE, goalTreeNode.getCoreFeedID()));
                    if (newID != null) {
                        goalTreeNode.setCoreFeedID(newID);
                        FeedDefinition feedDefinition;
                        try {
                            feedDefinition = feedStorage.getFeedDefinitionData(newID);
                            goalTreeNode.setAnalysisMeasure((AnalysisMeasure) findItem(goalTreeNode.getAnalysisMeasure(), feedDefinition).clone());
                        } catch (Exception e) {
                            LogClass.error(e);
                            throw new RuntimeException(e);
                        }
                        if (goalTreeNode.getFilters() != null) {
                            List<FilterDefinition> newFilters = new ArrayList<FilterDefinition>();
                            for (FilterDefinition filterDefinition : goalTreeNode.getFilters()) {
                                PersistableFilterDefinition persistableFilterDefinition = filterDefinition.toPersistableFilterDefinition();
                                PersistableFilterDefinition clonedPersistableFilterDefinition;
                                try {
                                    clonedPersistableFilterDefinition = persistableFilterDefinition.clone();
                                } catch (CloneNotSupportedException e) {
                                    throw new RuntimeException(e);
                                }
                                FilterDefinition clonedDefinition = clonedPersistableFilterDefinition.toFilterDefinition();
                                clonedDefinition.setField(findItem(filterDefinition.getField(), feedDefinition));
                                newFilters.add(clonedDefinition);
                            }
                            goalTreeNode.setFilters(newFilters);
                        }
                        if (goalTreeNode.getProblemConditions() != null) {
                            List<FilterDefinition> newFilters = new ArrayList<FilterDefinition>();
                            for (FilterDefinition filterDefinition : goalTreeNode.getProblemConditions()) {
                                PersistableFilterDefinition persistableFilterDefinition = filterDefinition.toPersistableFilterDefinition();
                                PersistableFilterDefinition clonedPersistableFilterDefinition;
                                try {
                                    clonedPersistableFilterDefinition = persistableFilterDefinition.clone();
                                } catch (CloneNotSupportedException e) {
                                    throw new RuntimeException(e);
                                }
                                FilterDefinition clonedDefinition = clonedPersistableFilterDefinition.toFilterDefinition();
                                clonedDefinition.setField(findItem(filterDefinition.getField(), feedDefinition));
                                newFilters.add(clonedDefinition);
                            }
                            goalTreeNode.setProblemConditions(newFilters);
                        }
                    }
                }
                for (GoalFeed goalFeed : goalTreeNode.getAssociatedFeeds()) {
                    Long newID = installedObjectMap.get(new SolutionElementKey(SolutionElementKey.DATA_SOURCE, goalFeed.getFeedID()));
                    if (newID != null) {
                        goalFeed.setFeedID(newID);
                    }
                }
                for (InsightDescriptor goalInsight : goalTreeNode.getAssociatedInsights()) {
                    Long newID = installedObjectMap.get(new SolutionElementKey(SolutionElementKey.INSIGHT, goalInsight.getId()));
                    if (newID != null) {
                        goalInsight.setId(newID);
                    }
                }
            }
        };

        idReplacementVisitor.visit(goalTree.getRootNode());
    }

    private void duplicateGoalDataSources(GoalTree goalTree) throws CloneNotSupportedException, SQLException {
        final Set<Long> dataSourceIDs = new HashSet<Long>();
        GoalTreeVisitor visitor = new GoalTreeVisitor() {

            protected void accept(GoalTreeNode goalTreeNode) {
                if (goalTreeNode.getCoreFeedID() > 0) {
                    dataSourceIDs.add(goalTreeNode.getCoreFeedID());
                }
                for (GoalFeed dataSource : goalTreeNode.getAssociatedFeeds()) {
                    dataSourceIDs.add(dataSource.getFeedID());
                }
            }
        };
        visitor.visit(goalTree.getRootNode());
        for (Long dataSourceID : dataSourceIDs) {
            FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(dataSourceID, conn, false);
            allSolutions.addAll(DataSourceCopyUtils.installFeed(userID, conn, true, dataSourceID, feedDefinition, true, null));
        }

        for (SolutionInstallInfo solutionInstallInfo : allSolutions) {
            installedObjectMap.put(new SolutionElementKey(solutionInstallInfo.getDescriptor().getType(), solutionInstallInfo.getPreviousID()), solutionInstallInfo.getDescriptor().getId());
        }
    }

    private void installSubTrees(GoalTree goalTree) {
        GoalTreeVisitor solutionInstallationVisitor = new GoalTreeVisitor() {

            protected void accept(GoalTreeNode goalTreeNode) {
                try {

                    // if the subtree is a new solution

                    if (goalTreeNode.getNewSubTree() != null) {
                        try {
                            goalTreeNode.setSubTreeID(duplicateTree(goalTreeNode.getSubTreeID()));
                            goalTreeNode.setNewSubTree(null);                            
                        } catch (CloneNotSupportedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        solutionInstallationVisitor.visit(goalTree.getRootNode());
    }

    private List<SolutionInstallInfo> generateFeedsForSolution(long solutionID, long userID, Connection conn, boolean copyData) throws SQLException, CloneNotSupportedException {
        List<SolutionInstallInfo> descriptors = new ArrayList<SolutionInstallInfo>();
        PreparedStatement queryStmt = conn.prepareStatement("SELECT FEED_ID FROM SOLUTION_TO_FEED WHERE SOLUTION_ID = ?");
        queryStmt.setLong(1, solutionID);
        PreparedStatement installStmt = conn.prepareStatement("INSERT INTO SOLUTION_INSTALL (SOLUTION_ID, installed_data_source_id, original_data_source_id) VALUES (?, ?, ?)");
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            long feedID = rs.getLong(1);
            FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(feedID, conn);
            descriptors.addAll(DataSourceCopyUtils.installFeed(userID, conn, copyData, feedID, feedDefinition, true, null));

            for (SolutionInstallInfo info : descriptors) {
                if (info.getDescriptor().getType() == EIDescriptor.DATA_SOURCE) {
                    installStmt.setLong(1, solutionID);
                    installStmt.setLong(2, info.getDescriptor().getId());
                    installStmt.setLong(3, info.getPreviousID());
                    installStmt.execute();
                }
            }
        }
        return descriptors;
    }

    private AnalysisItem findItem(AnalysisItem analysisItem, FeedDefinition feedDefinition) {
        AnalysisItem foundItem = null;
        for (AnalysisItem feedItem : feedDefinition.getFields()) {
            if (analysisItem.getDisplayName() != null) {
                if (analysisItem.getDisplayName().equals(feedItem.getDisplayName())) {
                    foundItem = feedItem;
                }
            } else {
                if (feedItem.getKey().toKeyString().equals(analysisItem.getKey().toKeyString())) {
                    foundItem = feedItem;
                }
            }
        }
        return foundItem;
    }
}
