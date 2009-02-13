package com.easyinsight.goals;

import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.analysis.Tag;
import com.easyinsight.datafeeds.FeedDescriptor;
import com.easyinsight.solutions.Solution;
import com.easyinsight.solutions.SolutionService;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.logging.LogClass;

import java.util.*;

/**
 * User: James Boe
 * Date: Oct 23, 2008
 * Time: 3:06:56 PM
 */
public class GoalService {

    private GoalStorage goalStorage = new GoalStorage();
    private GoalEvaluationStorage goalEvaluationStorage = new GoalEvaluationStorage();

    public GoalTree createGoalTree(GoalTree goalTree) {
        long userID = SecurityUtil.getUserID();
        try {
            goalStorage.addGoalTree(goalTree, userID);
            return goalTree;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }    

    public GoalTree updateGoalTree(GoalTree goalTree) {
        try {
            goalStorage.updateGoalTree(goalTree);
            return goalTree;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public AvailableSolutionList getSolutions(List<Tag> tags) {
        try {
            List<Solution> tagSolutions = new SolutionService().getSolutionsWithTags(tags);
            List<Solution> allSolutions = new SolutionService().getSolutions();
            return new AvailableSolutionList(tagSolutions, allSolutions);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public GoalTree createDataTree(long goalTreeID, Date startDate, Date endDate) {
        try {
            if (endDate == null) {
                Calendar cal = Calendar.getInstance();
                endDate = cal.getTime();
                cal.add(Calendar.DAY_OF_YEAR, -7);
                startDate = cal.getTime();
            }
            GoalTree goalTree = getGoalTree(goalTreeID);
            GoalTreeNodeData data = createDataTreeForDates(goalTree, startDate, endDate);
            goalTree.setRootNode(data);
            return goalTree;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<GoalValue> getGoalValues(final long goalTreeNodeID, final Date startDate, final Date endDate) {
        try {
            return goalEvaluationStorage.getGoalValues(goalTreeNodeID, startDate, endDate);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private GoalTreeNodeData createDataTreeForDates(GoalTree goalTree, final Date startDate, final Date endDate) {
        GoalTreeNodeData dataNode = new GoalTreeNodeDataBuilder().build(goalTree.getRootNode());
        GoalTreeVisitor visitor = new GoalTreeVisitor() {

                protected void accept(GoalTreeNode goalTreeNode) {
                    GoalTreeNodeData data = (GoalTreeNodeData) goalTreeNode;
                    data.populateCurrentValue();                    
                    data.determineOutcome(startDate, endDate, goalEvaluationStorage);
                }
            };
        visitor.visit(dataNode);
        dataNode.summarizeOutcomes();
        return dataNode;
    }

    public void evaluateGoalTrees() {
        // needs to trigger on timer of once/day

        // retrieve all goal trees

        // create thread pool to evaluate each goal tree
    }



    public void evaluateGoalTree(long goalTreeID) {
        // if the goal tree has a date in it, retrieve data for the preceding day
        // otherwise, retrieve all data
        // retrieval will be a list definition with the single specified AnalysisMeasure
        // if a date is present in the goal node definition, it will be added as a filter
        // value from that will be saved as the day's value for the goal tree

        // so we retrieve the value from that definition, save that as the day's value
    }

    public GoalTree getGoalTree(long goalTreeID) {
        try {
            return goalStorage.retrieveGoalTree(goalTreeID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<GoalTreeDescriptor> getGoalTrees() {
        try {
            return goalStorage.getTreesForUser(SecurityUtil.getUserID());
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<WSAnalysisDefinition> getInsights(long goalTreeNodeID) {
        return null;
    }

    public List<FeedDescriptor> getFeeds(long goalTreeNodeID) {
        return null;
    }

    public List<Solution> getSolutions(long goalTreeNodeID) {
        return null;
    }
}
