package com.easyinsight.goals;

import com.easyinsight.database.EIConnection;
import com.easyinsight.kpi.KPI;
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
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.logging.LogClass;
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
    private EIConnection conn;
    private long userID;
    private String userName;
    private long accountID;
    private GoalStorage goalStorage = new GoalStorage();
    private FeedStorage feedStorage = new FeedStorage();
    private SolutionService solutionService = new SolutionService();

    public InstallationSystem(EIConnection conn) {
        this.conn = conn;
        this.userID = SecurityUtil.getUserID();
        this.userName = SecurityUtil.getUserName();
        this.accountID = SecurityUtil.getAccountID();
    }

    public List<SolutionInstallInfo> getAllSolutions() {
        return allSolutions;
    }

    public List<AuthorizationRequirement> getAuthRequirements() {
        return authRequirements;
    }

    public void installSolution(Solution solution) throws Exception {
        allSolutions.addAll(generateFeedsForSolution(solution.getSolutionID(), userID, conn, solution.isCopyData()));
        if (solution.getGoalTreeID() > 0) {
            duplicateTree(solution.getGoalTreeID(), solution.getSolutionID());
        }
    }

    public void installUserTree(GoalTree goalTree, List<Integer> inlineSolutionIDs) throws Exception {

        for (Integer solutionID : inlineSolutionIDs) {
            Solution solution = solutionService.getSolution(solutionID, conn);
            GoalTree solutionTree = goalStorage.retrieveGoalTree(solution.getGoalTreeID(), conn);
            duplicateGoalDataSources(solutionTree, solutionID);
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

    private long duplicateTree(long goalTreeID, long solutionID) throws Exception {
        GoalTree goalTree = new GoalStorage().retrieveGoalTree(goalTreeID, conn);
        GoalTree clonedTree = goalTree.clone();
        FeedConsumer feedConsumer = new UserStub(userID, null, null, null, accountID, null);
        clonedTree.setAdministrators(Arrays.asList(feedConsumer));
        duplicateGoalDataSources(goalTree, solutionID);
        installDataSourcesAndReports(clonedTree);
        goalStorage.addGoalTree(clonedTree, conn);
        // TODO: add urlKey
        allSolutions.add(new SolutionInstallInfo(goalTree.getGoalTreeID(), new GoalTreeDescriptor(clonedTree.getGoalTreeID(), clonedTree.getName(), Roles.OWNER,
                clonedTree.getIconImage(), null), null, false));
        return clonedTree.getGoalTreeID();
    }

    private void installDataSourcesAndReports(GoalTree goalTree) throws SQLException, CloneNotSupportedException {



        GoalTreeVisitor idReplacementVisitor = new GoalTreeVisitor() {

            protected void accept(GoalTreeNode goalTreeNode) {
                if (goalTreeNode.getKpi() != null) {
                    KPI kpi = goalTreeNode.getKpi();
                    Long newID = installedObjectMap.get(new SolutionElementKey(SolutionElementKey.DATA_SOURCE, kpi.getCoreFeedID()));
                    KPI clonedKPI = kpi.clone();
                    if (newID != null) {
                        clonedKPI.setCoreFeedID(newID);
                        FeedDefinition feedDefinition;
                        try {
                            feedDefinition = feedStorage.getFeedDefinitionData(newID);
                            clonedKPI.setAnalysisMeasure((AnalysisMeasure) findItem(kpi.getAnalysisMeasure(), feedDefinition).clone());
                        } catch (Exception e) {
                            LogClass.error(e);
                            throw new RuntimeException(e);
                        }
                        if (kpi.getFilters() != null) {
                            List<FilterDefinition> newFilters = new ArrayList<FilterDefinition>();
                            for (FilterDefinition filterDefinition : kpi.getFilters()) {
                                filterDefinition.afterLoad();

                                FilterDefinition clonedPersistableFilterDefinition;
                                try {
                                    clonedPersistableFilterDefinition = filterDefinition.clone();
                                } catch (CloneNotSupportedException e) {
                                    throw new RuntimeException(e);
                                }

                                clonedPersistableFilterDefinition.setField(findItem(filterDefinition.getField(), feedDefinition));
                                newFilters.add(clonedPersistableFilterDefinition);
                            }
                            clonedKPI.setFilters(newFilters);
                        }
                        /*if (goalTreeNode.getProblemConditions() != null) {
                            List<FilterDefinition> newFilters = new ArrayList<FilterDefinition>();
                            for (FilterDefinition filterDefinition : goalTreeNode.getProblemConditions()) {
                                filterDefinition.afterLoad();

                                FilterDefinition clonedPersistableFilterDefinition;
                                try {
                                    clonedPersistableFilterDefinition = filterDefinition.clone();
                                } catch (CloneNotSupportedException e) {
                                    throw new RuntimeException(e);
                                }
                                clonedPersistableFilterDefinition.setField(findItem(filterDefinition.getField(), feedDefinition));
                                newFilters.add(clonedPersistableFilterDefinition);
                            }
                            goalTreeNode.setProblemConditions(newFilters);
                        }*/
                    }
                }
            }
        };

        idReplacementVisitor.visit(goalTree.getRootNode());
    }

    private void duplicateGoalDataSources(GoalTree goalTree, long solutionID) throws CloneNotSupportedException, SQLException {
        final Set<Long> dataSourceIDs = new HashSet<Long>();
        GoalTreeVisitor visitor = new GoalTreeVisitor() {

            protected void accept(GoalTreeNode goalTreeNode) {
                /*if (goalTreeNode.getCoreFeedID() > 0) {
                    dataSourceIDs.add(goalTreeNode.getCoreFeedID());
                }
                for (GoalFeed dataSource : goalTreeNode.getAssociatedFeeds()) {
                    dataSourceIDs.add(dataSource.getFeedID());
                }*/
            }
        };
        visitor.visit(goalTree.getRootNode());
        for (Long dataSourceID : dataSourceIDs) {
            FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(dataSourceID, conn, false);
            allSolutions.addAll(DataSourceCopyUtils.installFeed(userID, conn, true, dataSourceID, feedDefinition, true, null, solutionID, accountID, userName));
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
                            goalTreeNode.setSubTreeID(duplicateTree(goalTreeNode.getSubTreeID(), 0));
                            goalTreeNode.setNewSubTree(null);                            
                        } catch (CloneNotSupportedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } catch (Exception e) {
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

        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            long feedID = rs.getLong(1);
            FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(feedID, conn);
            descriptors.addAll(DataSourceCopyUtils.installFeed(userID, conn, copyData, feedID, feedDefinition, false, null, solutionID, accountID, userName));
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
