package com.easyinsight.goals;

import com.easyinsight.analysis.InsightRequestMetadata;
import com.easyinsight.analysis.Tag;

import com.easyinsight.kpi.KPI;
import com.easyinsight.scorecard.ScorecardService;
import com.easyinsight.solutions.Solution;
import com.easyinsight.solutions.SolutionService;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.Roles;
import com.easyinsight.logging.LogClass;
import com.easyinsight.email.UserStub;
import com.easyinsight.users.Account;
import com.easyinsight.database.EIConnection;
import com.easyinsight.database.Database;

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

    public GoalSaveInfo saveGoalTree(GoalTree goalTree) {
        SecurityUtil.authorizeAccountTier(Account.PROFESSIONAL);
        if (goalTree.getAdministrators() == null || goalTree.getAdministrators().size() == 0) {
            throw new RuntimeException("At least one administrator must be defined.");
        }
        long userID = SecurityUtil.getUserID();
        try {
            if (goalTree.getGoalTreeID() == 0) {
                UserStub userStub = new UserStub();
                userStub.setUserID(userID);
                return goalStorage.addGoalTree(goalTree);
            } else {
                return goalStorage.updateGoalTree(goalTree);
            }
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

    public List<GoalTreeDescriptor> getGoalTreesForInstall(long goalTreeID, long dataSourceID) {
        List<GoalTreeDescriptor> myTrees = new ArrayList<GoalTreeDescriptor>();
        try {
            List<GoalTreeDescriptor> goalTrees = getGoalTrees();
            Iterator<GoalTreeDescriptor> iter = goalTrees.iterator();
            while (iter.hasNext()) {
                GoalTreeDescriptor descriptor = iter.next();
                if (descriptor.getId() == goalTreeID) {
                    iter.remove();
                }
                if (descriptor.getDataSourceID() != dataSourceID) {
                    iter.remove();
                }
            }
            myTrees.addAll(goalTrees);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return myTrees;
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

    public GoalTree forceRefresh(long goalTreeID, boolean allSources, boolean includeSubTrees,
                                 InsightRequestMetadata insightRequestMetadata) {
        SecurityUtil.authorizeGoalTree(goalTreeID, Roles.SUBSCRIBER);
        try {
            EIConnection conn = Database.instance().getConnection();
            try {
                conn.setAutoCommit(false);                
                List<KPI> kpis = new ArrayList<KPI>(GoalUtil.getKPIs(goalTreeID, includeSubTrees, conn));
                new ScorecardService().refreshValuesForList(kpis, conn, insightRequestMetadata, allSources);
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

    public GoalTree getGoalTree(long goalTreeID) {
        SecurityUtil.authorizeGoalTreeSolutionInstall(goalTreeID);
        try {
            return goalStorage.retrieveGoalTree(goalTreeID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public KPITreeWrapper getGoalDataTree(long goalTreeID, InsightRequestMetadata insightRequestMetadata) {
        SecurityUtil.authorizeGoalTreeSolutionInstall(goalTreeID);
        KPITreeWrapper kpiTreeWrapper;
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            List<KPI> kpis = new ArrayList<KPI>(GoalUtil.getKPIs(goalTreeID, false, conn));
            kpiTreeWrapper = goalStorage.updateKPITree(kpis, conn, insightRequestMetadata, false);
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
        EIConnection conn = Database.instance().getConnection();
        try {
            return goalStorage.getTrees(SecurityUtil.getUserID(), SecurityUtil.getAccountID(), conn).values();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

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
}
