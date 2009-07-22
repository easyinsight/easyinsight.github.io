package com.easyinsight.goals;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.DataService;
import com.easyinsight.logging.LogClass;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.core.Value;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.analysis.*;
import com.easyinsight.analysis.ListRow;
import com.easyinsight.datafeeds.CredentialFulfillment;
import com.easyinsight.pipeline.HistoryRun;

import java.util.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * User: James Boe
 * Date: Jan 4, 2009
 * Time: 2:37:57 PM
 */
public class GoalEvaluationStorage {


    public void saveGoalEvaluation(long goalTreeNodeID, double evaluationResult, Date evaluationDate, Connection conn) throws SQLException {
        if (!Double.isNaN(evaluationResult) && !Double.isInfinite(evaluationResult)) {
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO goal_history (goal_tree_node_id, evaluation_date, evaluation_result) VALUES (?, ?, ?)");
            insertStmt.setLong(1, goalTreeNodeID);
            insertStmt.setDate(2, new java.sql.Date(evaluationDate.getTime()));
            insertStmt.setDouble(3, evaluationResult);
            insertStmt.execute();
        }
    }

    public void saveGoalEvaluations(List<GoalValue> goalValues, Connection conn) throws SQLException {
        for (GoalValue goalValue : goalValues) {
            saveGoalEvaluation(goalValue.getGoalTreeNodeID(), goalValue.getValue(), goalValue.getDate(), conn);
        }
    }

    public void backPopulateGoalTree(GoalTree goalTree, Connection conn) throws SQLException {

        final Set<Long> validIDs = new HashSet<Long>();
        GoalTreeVisitor hasDateVisitor = new GoalTreeVisitor() {

            protected void accept(GoalTreeNode goalTreeNode) {
                if (goalTreeNode.getCoreFeedID() > 0) {
                    validIDs.add(goalTreeNode.getGoalTreeNodeID());
                }
            }
        };

        hasDateVisitor.visit(goalTree.getRootNode());
        
        final List<GoalValue> goalValues = new ArrayList<GoalValue>();

        final PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM GOAL_HISTORY WHERE goal_tree_node_id = ?");

        GoalTreeVisitor deleteVisitor = new GoalTreeVisitor() {

            protected void accept(GoalTreeNode goalTreeNode) {
                try {
                    if (validIDs.contains(goalTreeNode.getGoalTreeNodeID())) {
                        deleteStmt.setLong(1, goalTreeNode.getGoalTreeNodeID());
                        deleteStmt.executeUpdate();
                    }
                } catch (SQLException e) {
                    LogClass.error(e);
                }
            }
        };

        deleteVisitor.visit(goalTree.getRootNode());

        GoalTreeVisitor goalTreeVisitor = new GoalTreeVisitor() {

            protected void accept(GoalTreeNode goalTreeNode) {
                if (goalTreeNode.getCoreFeedID() > 0 && validIDs.contains(goalTreeNode.getGoalTreeNodeID())) {
                    List<Date> dates = getDates(goalTreeNode);
                    if (dates.size() > 0) {
                        for (Date date : dates) {
                            GoalValue goalValue = evaluateGoalTreeNode(goalTreeNode, date, new ArrayList<CredentialFulfillment>());
                            if (goalValue != null) {
                                goalValues.add(goalValue);
                            }
                        }
                    } else {
                        GoalValue goalValue = evaluateGoalTreeNode(goalTreeNode, new Date(), new ArrayList<CredentialFulfillment>());
                        if (goalValue != null) {
                            goalValues.add(goalValue);
                        }
                    }
                }
            }
        };

        goalTreeVisitor.visit(goalTree.getRootNode());

        saveGoalEvaluations(goalValues, conn);
    }

    public void evaluateGoalTrees() {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement getTreesStmt = conn.prepareStatement("SELECT GOAL_TREE_ID FROM GOAL_TREE");
            ResultSet trees = getTreesStmt.executeQuery();
            while (trees.next()) {
                long treeID = trees.getLong(1);
                GoalTree goalTree = new GoalStorage().retrieveGoalTree(treeID);
                evaluateGoalTree(goalTree, conn, new ArrayList<CredentialFulfillment>());
            }
            conn.commit();
        } catch (SQLException e) {
            LogClass.error(e);
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public void evaluateGoalTree(GoalTree goalTree, Connection conn, final List<CredentialFulfillment> credentials) throws SQLException {
        final List<GoalValue> goalValues = new ArrayList<GoalValue>();

        GoalTreeVisitor goalTreeVisitor = new GoalTreeVisitor() {

            protected void accept(GoalTreeNode goalTreeNode) {
                if (goalTreeNode.getCoreFeedID() > 0) {
                    List<Date> dates = getDates(goalTreeNode);
                    for (Date date : dates) {
                        GoalValue goalValue = evaluateGoalTreeNode(goalTreeNode, date, credentials);
                        if (goalValue != null) {
                            goalValues.add(goalValue);
                        }                        
                    }
                }
            }
        };

        goalTreeVisitor.visit(goalTree.getRootNode());

        saveGoalEvaluations(goalValues, conn);
    }

    public void forceEvaluate(GoalTree goalTree, Connection conn, final List<CredentialFulfillment> credentials) throws SQLException {
        final List<GoalValue> goalValues = new ArrayList<GoalValue>();

        GoalTreeVisitor goalTreeVisitor = new GoalTreeVisitor() {

            protected void accept(GoalTreeNode goalTreeNode) {
                if (goalTreeNode.getCoreFeedID() > 0) {
                    GoalValue goalValue = evaluateGoalTreeNode(goalTreeNode, new Date(), credentials);
                    if (goalValue != null) {
                        goalValues.add(goalValue);
                    }                        
                }
            }
        };

        goalTreeVisitor.visit(goalTree.getRootNode());

        saveGoalEvaluations(goalValues, conn);
    }

    private List<Date> getDates(GoalTreeNode goalTreeNode) {
        List<Date> dates = new ArrayList<Date>();
        AnalysisDateDimension dateItem = null;
        for (FilterDefinition filter : goalTreeNode.getFilters()){
            if (filter instanceof RollingFilterDefinition) {
                RollingFilterDefinition rollingFilterDefinition = (RollingFilterDefinition) filter;
                dateItem = (AnalysisDateDimension) rollingFilterDefinition.getField();
                dateItem.setDateLevel(AnalysisDateDimension.DAY_LEVEL);
            }
        }
        if (dateItem == null) {
            return dates;
        }
        if (new DataService().getFeedMetadata(goalTreeNode.getCoreFeedID()).getCredentials().size() > 0) {
            return new ArrayList<Date>();
        }
        WSListDefinition dateDefinition = new WSListDefinition();
        dateDefinition.setDataFeedID(goalTreeNode.getCoreFeedID());
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(dateItem);
        dateDefinition.setColumns(analysisItems);
        ListDataResults results = new DataService().list(dateDefinition, new InsightRequestMetadata());
        for (ListRow listRow : results.getRows()) {
            Value value = listRow.getValues()[0];
            if (value.type() == Value.DATE) {
                DateValue dateValue = (DateValue) value;
                dates.add(dateValue.getDate());
            }
        }
        return dates;
    }

    public GoalValue getLatestGoalValue(GoalTreeNode goalTreeNode) throws SQLException {
        GoalValue goalValue = null;
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement findLatestHistoryStmt = conn.prepareStatement("SELECT MAX(evaluation_date) FROM goal_history WHERE goal_tree_node_id = ?");
            findLatestHistoryStmt.setLong(1, goalTreeNode.getGoalTreeNodeID());
            ResultSet rs = findLatestHistoryStmt.executeQuery();
            if (rs.next()) {
                java.sql.Date date = rs.getDate(1);
                if (!rs.wasNull()) {
                    PreparedStatement getValueStmt = conn.prepareStatement("SELECT evaluation_result FROM goal_history WHERE evaluation_date = ? and goal_tree_node_id = ?");
                    getValueStmt.setDate(1, date);
                    getValueStmt.setLong(2, goalTreeNode.getGoalTreeNodeID());
                    ResultSet valueRS = getValueStmt.executeQuery();
                    if (valueRS.next()) {
                        goalValue = new GoalValue(goalTreeNode.getGoalTreeNodeID(), date, valueRS.getDouble(1));
                    } else {
                        throw new RuntimeException("Couldn't find value for MAX(date), should never happen");
                    }
                }
            }
        } finally {
            Database.closeConnection(conn);
        }
        return goalValue;
    }

    public GoalValue evaluateGoalTreeNode(GoalTreeNode goalTreeNode, Date date, List<CredentialFulfillment> credentials) {
        GoalValue goalValue = null;
        if (goalTreeNode.getCoreFeedID() > 0) {
            WSListDefinition listDefinition = new WSListDefinition();
            listDefinition.setDataFeedID(goalTreeNode.getCoreFeedID());
            List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
            analysisItems.add(goalTreeNode.getAnalysisMeasure());
            listDefinition.setColumns(analysisItems);
            listDefinition.setFilterDefinitions(goalTreeNode.getFilters());
            InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
            insightRequestMetadata.setNow(date);
            insightRequestMetadata.setCredentialFulfillmentList(credentials);
            ListDataResults results = new DataService().list(listDefinition, insightRequestMetadata);
            if (results.getRows().length == 1) {
                ListRow listRow = results.getRows()[0];
                Value value = listRow.getValues()[0];
                Double doubleValue = value.toDouble();
                if (doubleValue != null) {
                    goalValue = new GoalValue(goalTreeNode.getGoalTreeNodeID(), date, doubleValue);
                }
            }
        }
        return goalValue;
    }

    // given a start value, a end value, a start date, and an end date

    private List<GoalValue> calculateSlope(long goalTreeNodeID, double startValue, double endValue, Date startDate, Date endDate, int interval) {
        List<GoalValue> values = new ArrayList<GoalValue>();
        long timeDelta = endDate.getTime() - startDate.getTime();
        long intervals = timeDelta / interval;
        double valueDelta = endValue - startValue;
        double perIntervalChange = valueDelta / intervals;
        for (int i = 0; i < intervals; i++) {
            Date intervalDate = new Date(startDate.getTime() + (i * interval));
            double intervalValue = startValue + perIntervalChange * i;
            values.add(new GoalValue(goalTreeNodeID, intervalDate, intervalValue));
        }
        return values;
    }

    public List<GoalValue> calculateSlope(long goalID, Date startDate, Date endDate) throws SQLException {
        Connection conn = Database.instance().getConnection();
        try {
            GoalTreeNode node = new GoalStorage().retrieveNode(goalID, conn);
            GoalTreeMilestone milestone = node.getMilestone();
            Date milestoneEndDate = milestone.getMilestoneDate();
            double milestoneValue = node.getGoalValue();
            double startValue = 0;
            List<GoalValue> values = calculateSlope(node.getGoalTreeNodeID(), startValue, milestoneValue, startDate, milestoneEndDate, 60 * 60 * 24 * 1000);
            List<GoalValue> rangedValues = new ArrayList<GoalValue>();
            for (GoalValue goalValue : values) {
                if (goalValue.getDate().getTime() >= startDate.getTime() && goalValue.getDate().getTime() <= endDate.getTime()) {
                    rangedValues.add(goalValue);
                }
            }
            return rangedValues;
        } finally {
            Database.closeConnection(conn);
        }
    }

    public List<GoalValue> getGoalValues(long goalTreeNodeID, Date startDate, Date endDate, List<CredentialFulfillment> credentials) throws SQLException {
        GoalTreeNode goalTreeNode = null;
        Connection conn = Database.instance().getConnection();
        try {
            goalTreeNode = new GoalStorage().retrieveNode(goalTreeNodeID, conn);
            /*PreparedStatement queryStmt = conn.prepareStatement("SELECT evaluation_result, evaluation_date FROM goal_history " +
                    "WHERE evaluation_date >= ? AND evaluation_date <= ? AND goal_tree_node_id = ? ORDER BY evaluation_date");
            queryStmt.setDate(1, new java.sql.Date(startDate.getTime()));
            queryStmt.setDate(2, new java.sql.Date(endDate.getTime()));
            queryStmt.setLong(3, goalTreeNodeID);
            List<GoalValue> goalValues = new ArrayList<GoalValue>();
            ResultSet startRS = queryStmt.executeQuery();
            while (startRS.next()) {
                double evaluationResult = startRS.getDouble(1);
                Date evaluationDate = new Date(startRS.getDate(2).getTime());
                goalValues.add(new GoalValue(goalTreeNodeID, evaluationDate, evaluationResult));
            }
            return goalValues;*/
        } finally {
            Database.closeConnection(conn);
        }
        List<GoalValue> goalValues;
        if (goalTreeNode != null && goalTreeNode.getCoreFeedID() > 0) {
            goalValues = new HistoryRun().calculateHistoricalValues(goalTreeNode.getCoreFeedID(), goalTreeNode.getAnalysisMeasure(),
                goalTreeNode.getFilters(), startDate, endDate, credentials);
        } else {
            goalValues = new ArrayList<GoalValue>();
        }
        return goalValues;
    }

    public List<GoalValue> getGoalValuesFromDatabase(long goalTreeNodeID, Date startDate, Date endDate, List<CredentialFulfillment> credentials) throws SQLException {
        return getGoalValuesFromDatabase(goalTreeNodeID, startDate, endDate, credentials, true);
    }

    public List<GoalValue> getGoalValuesFromDatabase(long goalTreeNodeID, Date startDate, Date endDate) throws SQLException {
        return getGoalValuesFromDatabase(goalTreeNodeID, startDate, endDate, null, false); 
    }

    private List<GoalValue> getGoalValuesFromDatabase(long goalTreeNodeID, Date startDate, Date endDate, List<CredentialFulfillment> credentials, boolean liveData) throws SQLException {
        List<GoalValue> goalValues = new ArrayList<GoalValue>();
        boolean hasDate = false;
        GoalTreeNode goalTreeNode;
        Connection conn = Database.instance().getConnection();
        try {
            goalTreeNode = new GoalStorage().retrieveNode(goalTreeNodeID, conn);
            for (FilterDefinition filterDefinition : goalTreeNode.getFilters()) {
                if (filterDefinition instanceof RollingFilterDefinition) {
                    hasDate = true;
                }
            }
            if (!hasDate || !liveData) {
                PreparedStatement queryStmt = conn.prepareStatement("SELECT evaluation_result, evaluation_date FROM goal_history " +
                        "WHERE evaluation_date >= ? AND evaluation_date <= ? AND goal_tree_node_id = ? ORDER BY evaluation_date");
                queryStmt.setDate(1, new java.sql.Date(startDate.getTime()));
                queryStmt.setDate(2, new java.sql.Date(endDate.getTime()));
                queryStmt.setLong(3, goalTreeNodeID);

                ResultSet startRS = queryStmt.executeQuery();
                while (startRS.next()) {
                    double evaluationResult = startRS.getDouble(1);
                    Date evaluationDate = new Date(startRS.getDate(2).getTime());
                    goalValues.add(new GoalValue(goalTreeNodeID, evaluationDate, evaluationResult));
                }
            }
        } finally {
            Database.closeConnection(conn);
        }
        if (hasDate) {
            goalValues = new HistoryRun().calculateHistoricalValues(goalTreeNode.getCoreFeedID(), goalTreeNode.getAnalysisMeasure(),
                    goalTreeNode.getFilters(), startDate, endDate, credentials);
        }
        return goalValues;
    }

    public GoalOutcome getEvaluations(GoalTreeNode goalTreeNode, Date startDate, Date endDate, double goalValue, boolean highIsGood, List<CredentialFulfillment> credentials) throws SQLException {
        // key here is what's the value at the start date, what's the value at the end date
        // delta between those two values
        // outcomes:
        // end value is > or = goal
        // end value is < goal
        //   is delta positive and > epsilon
        //   is delta ~= epsilon
        //   is delta negative and < epsilon

        

        List<GoalValue> goalValues = getGoalValuesFromDatabase(goalTreeNode.getGoalTreeNodeID(), startDate, endDate, credentials);
        int resultLength = goalValues.size();
        boolean failedProblemConditions = false;
        if (goalTreeNode.getProblemConditions().size() > 0 && resultLength >= 1) {
            GoalValue value = goalValues.get(goalValues.size() - 1);
            for (FilterDefinition problemCondition : goalTreeNode.getProblemConditions()) {
                MaterializedFilterDefinition filter = problemCondition.materialize(null);
                NumericValue numericValue = new NumericValue(value.getValue());
                if (filter.allows(numericValue)) {
                    failedProblemConditions = true;
                }
            }
        }
        if (resultLength >= 2) {
            for (int i = 1; i < (resultLength - 1); i++) {
                goalValues.remove(1);
            }
            double endValue = goalValues.get(1).getValue();
            double startValue = goalValues.get(0).getValue();
            double delta = endValue - startValue;
            int outcome = determineOutcome(goalValue, highIsGood, delta, endValue);
            double percentChange = delta / startValue * 100;
            double outcomeWeight = (outcome == GoalOutcome.EXCEEDING_GOAL || outcome == GoalOutcome.POSITIVE) ? 1 : (outcome == GoalOutcome.NEUTRAL ? 0 : -1);
            return new ConcreteGoalOutcome(failedProblemConditions ? GoalOutcome.NEGATIVE : outcome, goalValue, endValue, startValue, percentChange, outcomeWeight);
        } else {
            return new GoalOutcome(failedProblemConditions ? GoalOutcome.NEGATIVE : GoalOutcome.NO_DATA, 0);
        }
    }

    private int determineOutcome(double goalValue, boolean highIsGood, double delta, double endValue) {
        int outcome;
        if (highIsGood) {
            if (endValue >= goalValue) {
                outcome = GoalOutcome.EXCEEDING_GOAL;
            } else if (Math.abs(delta) < Math.abs(goalValue * .002)) {
                outcome = GoalOutcome.NEUTRAL;
            } else if (delta > 0) {
                outcome = GoalOutcome.POSITIVE;
            } else {
                outcome = GoalOutcome.NEGATIVE;
            }
        } else {
            if (endValue <= goalValue) {
                outcome = GoalOutcome.EXCEEDING_GOAL;
            } else if (Math.abs(delta) < Math.abs(goalValue * .002)) {
                outcome = GoalOutcome.NEUTRAL;
            } else if (delta < 0) {
                outcome = GoalOutcome.POSITIVE;
            } else {
                outcome = GoalOutcome.NEGATIVE;
            }
        }
        return outcome;
    }
}
