package com.easyinsight.goals;

import com.easyinsight.analysis.Tag;

import com.easyinsight.kpi.KPI;
import com.easyinsight.scorecard.ScorecardService;
import com.easyinsight.solutions.Solution;
import com.easyinsight.solutions.SolutionService;
import com.easyinsight.solutions.SolutionGoalTreeDescriptor;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.Roles;
import com.easyinsight.logging.LogClass;
import com.easyinsight.email.UserStub;
import com.easyinsight.users.Account;
import com.easyinsight.database.EIConnection;
import com.easyinsight.database.Database;
import com.easyinsight.datafeeds.*;

import java.util.*;

/**
 * User: James Boe
 * Date: Oct 23, 2008
 * Time: 3:06:56 PM
 */
public class GoalService {

    private GoalStorage goalStorage = new GoalStorage();

    public long canAccessGoalTree(String urlKey) {
        try {
            return SecurityUtil.authorizeGoalTree(urlKey, Roles.SUBSCRIBER);
        } catch (SecurityException e) {
            return 0;
        }
    }                                                                

    public GoalSaveInfo createGoalTree(GoalTree goalTree) {
        SecurityUtil.authorizeAccountTier(Account.PROFESSIONAL);
        if (goalTree.getAdministrators() == null || goalTree.getAdministrators().size() == 0) {
            throw new RuntimeException("At least one administrator must be defined.");
        }
        long userID = SecurityUtil.getUserID();
        try {
            UserStub userStub = new UserStub();
            userStub.setUserID(userID);
            return goalStorage.addGoalTree(goalTree);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void deleteMilestone(long milestoneID) {
        SecurityUtil.authorizeMilestone(milestoneID);
        try {
            goalStorage.deleteMilestone(milestoneID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public long saveMilestone(GoalTreeMilestone goalTreeMilestone) {
        long accountID = SecurityUtil.getAccountID();
        try {
            return goalStorage.addMilestone(goalTreeMilestone, accountID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<GoalTreeMilestone> getMilestones() {
        long accountID = SecurityUtil.getAccountID();
        try {
            return goalStorage.getMilestones(accountID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void updateMilestone(GoalTreeMilestone goalTreeMilestone) {
        SecurityUtil.authorizeMilestone(goalTreeMilestone.getMilestoneID());
        try {
            goalStorage.updateMilestone(goalTreeMilestone);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void deleteGoalTree(long goalTreeID) {
        SecurityUtil.authorizeGoalTree(goalTreeID, Roles.OWNER);
        try {
            goalStorage.deleteGoalTree(goalTreeID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    /*public List<GoalValue> generateHistory(AnalysisMeasure analysisMeasure, List<FilterDefinition> filters, long dataSourceID, Date startDate, Date endDate,
                                           List<CredentialFulfillment> credentials) {
        try {
            return new HistoryRun().calculateHistoricalValues(dataSourceID, analysisMeasure, filters, startDate, endDate, credentials);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }*/

    public GoalSaveInfo updateGoalTree(GoalTree goalTree) {
        SecurityUtil.authorizeGoalTree(goalTree.getGoalTreeID(), Roles.OWNER);
        try {
            return goalStorage.updateGoalTree(goalTree);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public GoalSaveInfo splitGoalTree(GoalTree originalTree, GoalTree newTree, GoalTreeNode parentNode) {
        SecurityUtil.authorizeGoalTree(originalTree.getGoalTreeID(), Roles.OWNER);
        EIConnection conn = Database.instance().getConnection();
        try {
            // TODO: finish implementation
            conn.setAutoCommit(false);
            // make the initial save of the tree
            GoalSaveInfo initialSaveInfo;
            if (originalTree.getGoalTreeID() == 0) {
                goalStorage.addGoalTree(originalTree, conn);
            } else {
                initialSaveInfo = goalStorage.updateGoalTree(originalTree, conn);
            }
            
            goalStorage.addGoalTree(newTree, conn);
            parentNode.setSubTreeID(newTree.getGoalTreeID());

            conn.commit();
            //GoalSaveInfo goalSaveInfo = new GoalSaveInfo(newTree, );
            //return newTree.getGoalTreeID();
            return null;
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public AvailableGoalTreeList getGoalTreesForInstall(long goalTreeID) {
        AvailableGoalTreeList availableGoalTreeList = new AvailableGoalTreeList();
        List<GoalTreeDescriptor> myTrees = new ArrayList<GoalTreeDescriptor>();
        List<SolutionGoalTreeDescriptor> solutionTrees;
        try {
            List<GoalTreeDescriptor> goalTrees = getGoalTrees();
            Iterator<GoalTreeDescriptor> iter = goalTrees.iterator();
            while (iter.hasNext()) {
                GoalTreeDescriptor descriptor = iter.next();
                if (descriptor.getId() == goalTreeID) {
                    iter.remove();
                }
            }
            myTrees.addAll(goalTrees);
            solutionTrees = new SolutionService().getTreesFromSolutions();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        availableGoalTreeList.setMyTrees(myTrees);
        availableGoalTreeList.setSolutionTrees(solutionTrees);
        return availableGoalTreeList;
    }

    public AvailableSolutionList getSolutionsByTags(List<Tag> tags) {
        try {
            List<Solution> tagSolutions;
            if (tags == null) {
                tagSolutions = new ArrayList<Solution>();
            } else {
                tagSolutions = new SolutionService().getSolutionsWithTags(tags);
            }
            List<Solution> allSolutions = new SolutionService().getSolutions();
            return new AvailableSolutionList(tagSolutions, allSolutions);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<CredentialRequirement> getCredentialsForGoalTree(long goalTreeID, boolean allSources, final boolean includeSubTrees, List<CredentialFulfillment> existingCredentials) {
        final EIConnection conn = Database.instance().getConnection();
        Map<Long, CredentialRequirement> credentialMap = new HashMap<Long, CredentialRequirement>();
        final Set<Long> dataSourceIDs;
        try {
            dataSourceIDs = GoalUtil.getDataSourceIDs(goalTreeID, includeSubTrees, conn);
            new ScorecardService().getCredentialsForDataSources(allSources, existingCredentials, credentialMap, dataSourceIDs);
            return new ArrayList<CredentialRequirement>(credentialMap.values());
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }    

    public GoalTree forceRefresh(long goalTreeID, List<CredentialFulfillment> credentialsList, boolean allSources, boolean includeSubTrees) {
        SecurityUtil.authorizeGoalTree(goalTreeID, Roles.SUBSCRIBER);
        try {
            EIConnection conn = Database.instance().getConnection();
            try {
                conn.setAutoCommit(false);                
                List<KPI> kpis = new ArrayList<KPI>(GoalUtil.getKPIs(goalTreeID, includeSubTrees, conn));
                new ScorecardService().refreshValuesForList(kpis, conn, credentialsList, allSources);
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw new RuntimeException(e);
            } finally {
                conn.setAutoCommit(true);
                Database.closeConnection(conn);
            }
            return getGoalTree(goalTreeID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    /*public GoalTree createDataTree(long goalTreeID) {
        SecurityUtil.authorizeGoalTree(goalTreeID, Roles.SUBSCRIBER);
        try {
            GoalTree goalTree = getGoalTree(goalTreeID);
            goalStorage.decorateDataTree(goalTree);
            GoalTreeNode data = createDataTreeForDates(goalTree);
            goalTree.setRootNode(data);
            return goalTree;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }*/

    /*public List<GoalValue> getGoalValues(final long goalTreeNodeID, final Date startDate, final Date endDate, List<CredentialFulfillment> credentialsList) {
        SecurityUtil.authorizeGoal(goalTreeNodeID, Roles.SUBSCRIBER);
        try {
            return goalEvaluationStorage.getGoalValuesFromDatabase(goalTreeNodeID, startDate, endDate, credentialsList);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }*/

    /*public List<CredentialRequirement> getCredentialsForNode(long goalTreeNodeID, List<CredentialFulfillment> existingCredentials, boolean forceRefresh) {
        Map<Long, CredentialRequirement> credentialMap = new HashMap<Long, CredentialRequirement>();
        EIConnection conn = Database.instance().getConnection();
        GoalTreeNode goalTreeNode;
        try {
            goalTreeNode = goalStorage.retrieveNode(goalTreeNodeID, conn);
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        try {
            long dataSourceID = goalTreeNode.getCoreFeedID();
            if (dataSourceID > 0) {
                if (forceRefresh) {
                    FeedDefinition feedDefinition = new FeedStorage().getFeedDefinitionData(dataSourceID);
                    if (feedDefinition.getCredentialsDefinition() == CredentialsDefinition.STANDARD_USERNAME_PW) {
                        IServerDataSourceDefinition dataSource = (IServerDataSourceDefinition) feedDefinition;
                        Credentials credentials = null;
                        for (CredentialFulfillment fulfillment : existingCredentials) {
                            if (fulfillment.getDataSourceID() == feedDefinition.getDataFeedID()) {
                                credentials = fulfillment.getCredentials();
                            }
                        }
                        boolean noCredentials = true;
                        if (credentials != null) {
                            noCredentials = dataSource.validateCredentials(credentials) != null;
                        }
                        if (noCredentials) {
                            credentialMap.put(feedDefinition.getDataFeedID(), new CredentialRequirement(feedDefinition.getDataFeedID(), feedDefinition.getFeedName(),
                                    CredentialsDefinition.STANDARD_USERNAME_PW));
                        }
                    }
                } else {
                    Feed feed = FeedRegistry.instance().getFeed(dataSourceID);
                    Credentials credentials = null;
                    for (CredentialFulfillment fulfillment : existingCredentials) {
                        if (fulfillment.getDataSourceID() == dataSourceID) {
                            credentials = fulfillment.getCredentials();
                        }
                    }
                    boolean noCredentials = true;
                    if (credentials != null) {
                        noCredentials = new FeedStorage().getFeedDefinitionData(dataSourceID).validateCredentials(credentials) != null;
                    }
                    if (noCredentials) {
                        Set<CredentialRequirement> credentialRequirements = feed.getCredentialRequirement(false);
                        for (CredentialRequirement credentialRequirement : credentialRequirements) {
                            credentialMap.put(credentialRequirement.getDataSourceID(), credentialRequirement);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return new ArrayList<CredentialRequirement>(credentialMap.values());
    }*/

    /*private GoalTreeNodeData createDataTreeForDates(GoalTree goalTree) {
        GoalTreeNodeData dataNode = new GoalTreeNodeDataBuilder().build(goalTree.getRootNode());
        GoalTreeVisitor visitor = new GoalTreeVisitor() {

            protected void accept(GoalTreeNode goalTreeNode) {
                GoalTreeNodeData data = (GoalTreeNodeData) goalTreeNode;
                try {
                    if (data.getSubTreeID() > 0) {
                        GoalStorage goalStorage = new GoalStorage();
                        GoalTree subTree = goalStorage.retrieveGoalTree(data.getSubTreeID());
                        GoalTreeNodeData subData = createDataTreeForDates(subTree);
                        *//*if (data.getAnalysisMeasure() == null) {
                            data.setGoalOutcome(subData.getGoalOutcome());
                        } else {
                            data.populateCurrentValue();
                        }*//*
                    } else {
                        data.populateCurrentValue();
                    }
                } catch (Exception e) {
                    LogClass.error(e);
                }
            }
        };
        visitor.visit(dataNode);
        dataNode.summarizeOutcomes();
        return dataNode;
    }*/

    public GoalTree getGoalTree(long goalTreeID) {
        SecurityUtil.authorizeGoalTreeSolutionInstall(goalTreeID);
        try {
            return goalStorage.retrieveGoalTree(goalTreeID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public KPITreeWrapper getGoalDataTree(long goalTreeID, List<CredentialFulfillment> credentialsList) {
        SecurityUtil.authorizeGoalTreeSolutionInstall(goalTreeID);
        KPITreeWrapper kpiTreeWrapper;
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            List<KPI> kpis = new ArrayList<KPI>(GoalUtil.getKPIs(goalTreeID, false, conn));
            kpiTreeWrapper = goalStorage.updateKPITree(kpis, goalTreeID, conn, credentialsList, false);
            kpiTreeWrapper.setGoalTree(goalStorage.retrieveGoalTree(goalTreeID, conn));
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        return kpiTreeWrapper;
    }

    public List<GoalTreeDescriptor> getGoalTrees() {
        try {
            return goalStorage.getTreesForUser(SecurityUtil.getUserID());
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public boolean subscribeToGoal(long goalTreeNodeID) {
        SecurityUtil.authorizeGoal(goalTreeNodeID, Roles.SUBSCRIBER);
        long userID = SecurityUtil.getUserID();
        try {
            return goalStorage.addUserToGoal(userID, goalTreeNodeID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void unsubscribeToGoal(long goalTreeNodeID) {
        long userID = SecurityUtil.getUserID();
        try {
            goalStorage.removeUserFromGoal(userID, goalTreeNodeID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    // what do we want to see here...
    // we want to see the latest value
    // we want to understand some context around that number
    // context is relevant to the goal that we're trying to meet
    // sparkline seems useful as one thing there


    /*public List<GoalTreeNodeData> getGoals() {
        long userID = SecurityUtil.getUserID();
        try {
            Calendar cal = Calendar.getInstance();
            Date endDate = cal.getTime();
            cal.add(Calendar.DAY_OF_YEAR, -7);
            Date startDate = cal.getTime();
            return goalStorage.getGoalsForUser(userID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }*/

    public List<GoalDescriptor> getGoalsForTree(long treeID) {
        final List<GoalDescriptor> nodes = new ArrayList<GoalDescriptor>();
        SecurityUtil.authorizeGoalTree(treeID, Roles.SUBSCRIBER);
        try {
            GoalTree goalTree = goalStorage.retrieveGoalTree(treeID);
            GoalTreeVisitor visitor = new GoalTreeVisitor() {

                protected void accept(GoalTreeNode goalTreeNode) {
                    nodes.add(new GoalDescriptor(goalTreeNode.getName(), goalTreeNode.getGoalTreeNodeID()));
                }
            };
            visitor.visit(goalTree.getRootNode());
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return nodes;
    }

    /*public List<GoalValue> calculateSlope(long goalID, Date startDate, Date endDate) {
        try {
            return goalEvaluationStorage.calculateSlope(goalID, startDate, endDate);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }*/
}
