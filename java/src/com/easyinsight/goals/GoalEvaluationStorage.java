package com.easyinsight.goals;

import com.easyinsight.database.Database;
import com.easyinsight.core.NumericValue;
import com.easyinsight.analysis.*;
import com.easyinsight.datafeeds.CredentialFulfillment;
import com.easyinsight.pipeline.HistoryRun;

import java.util.*;
import java.util.Date;
import java.sql.*;

/**
 * User: James Boe
 * Date: Jan 4, 2009
 * Time: 2:37:57 PM
 */
public class GoalEvaluationStorage {


    public void saveGoalEvaluation(long goalTreeNodeID, Double newValue, Double oldValue, Date evaluationDate, int outcomeValue,
                                   int direction, boolean problemEvaluated, Connection conn) throws SQLException {
        if (newValue != null && (Double.isNaN(newValue) || Double.isInfinite(newValue))) {
            newValue = null;
        }
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO goal_outcome (goal_tree_node_id, start_value, end_value," +
                    "evaluation_date, outcome_value, direction, problem_evaluated) VALUES (?, ?, ?, ?, ?, ?, ?)");
        insertStmt.setLong(1, goalTreeNodeID);
        if (oldValue == null) {
            insertStmt.setNull(2, Types.DOUBLE);
        } else {
            insertStmt.setDouble(2, oldValue);
        }
        if (newValue == null) {
            insertStmt.setNull(3, Types.DOUBLE);
        } else {
            insertStmt.setDouble(3, newValue);
        }
        insertStmt.setTimestamp(4, new java.sql.Timestamp(evaluationDate.getTime()));
        insertStmt.setInt(5, outcomeValue);
        insertStmt.setInt(6, direction);
        insertStmt.setBoolean(7, problemEvaluated);
        insertStmt.execute();
    }

    public void saveGoalEvaluations(List<GoalOutcome> goalValues, Connection conn) throws SQLException {
        for (GoalOutcome goalValue : goalValues) {
            saveGoalEvaluation(goalValue.getGoalTreeNodeID(), goalValue.getOutcomeValue(), goalValue.getPreviousValue(),
                    goalValue.getEvaluationDate(), goalValue.getOutcomeState(), goalValue.getDirection(), goalValue.isProblemEvaluated(), conn);
        }
    }

    private Double findLastGoalValue(Connection conn, long goalTreeNodeID) {
        Double oldValue = null;
        try {
            PreparedStatement findMaxStmt = conn.prepareStatement("SELECT MAX(EVALUATION_DATE) FROM GOAL_OUTCOME WHERE GOAL_TREE_NODE_ID = ? AND EVALUATION_DATE < ?");
            findMaxStmt.setLong(1, goalTreeNodeID);
            findMaxStmt.setDate(2, new java.sql.Date(System.currentTimeMillis()));
            ResultSet dateRS = findMaxStmt.executeQuery();
            if (dateRS.next()) {
                Timestamp timestampDate = dateRS.getTimestamp(1);
                if (!dateRS.wasNull()) {
                    PreparedStatement lastValStmt = conn.prepareStatement("SELECT END_VALUE, MAX(EVALUATION_DATE) FROM GOAL_OUTCOME WHERE GOAL_TREE_NODE_ID = ? AND " +
                        "EVALUATION_DATE < ?");
                    lastValStmt.setLong(1, goalTreeNodeID);
                    lastValStmt.setTimestamp(2, timestampDate);
                    ResultSet rs = lastValStmt.executeQuery();
                    if (rs.next()) {
                        if (!rs.wasNull()) {
                            oldValue = rs.getDouble(1);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return oldValue;
    }

    private class PopulationGoalTreeVisitor extends GoalTreeVisitor {

        final List<GoalOutcome> goalOutcomes = new ArrayList<GoalOutcome>();
        Connection conn;
        List<CredentialFulfillment> credentials; 
        boolean includeSubtrees;

        private PopulationGoalTreeVisitor(Connection conn, List<CredentialFulfillment> credentials, boolean includeSubtrees) {
            this.conn = conn;
            this.credentials = credentials;
            this.includeSubtrees = includeSubtrees;
        }

        protected void accept(GoalTreeNode goalTreeNode) {
                if (goalTreeNode.getCoreFeedID() > 0) {
                    // if there's no date filter, we're screwed on this...
                    // can we retrieve the last outcome value?

                    // exactly what do we actually want to be looking at here
                    // first query we need to figure out is "what's the last day before today?"
                    List<GoalValue> lastTwoValues = new HistoryRun().lastTwoValues(goalTreeNode.getCoreFeedID(), goalTreeNode.getAnalysisMeasure(),
                            goalTreeNode.getFilters(), credentials);
                    Double newValue = null;
                    Double oldValue = null;
                    boolean failedCondition = false;
                    int outcomeState = GoalOutcome.NO_DATA;
                    int direction = GoalOutcome.NO_DIRECTION;
                    if (lastTwoValues.size() > 0) {
                        if (lastTwoValues.size() > 1) {
                            GoalValue previousGoalValue = lastTwoValues.get(0);
                            oldValue = previousGoalValue.getValue();
                            GoalValue newGoalValue = lastTwoValues.get(1);
                            newValue = newGoalValue.getValue();
                            if (goalTreeNode.isGoalDefined()) {
                                double delta = newValue - oldValue;
                                outcomeState = determineOutcome(goalTreeNode.getGoalValue(), goalTreeNode.isHighIsGood(), delta, newValue);
                                if (goalTreeNode.isHighIsGood()) {
                                    direction = GoalOutcome.UP_DIRECTION;
                                } else {
                                    direction = GoalOutcome.DOWN_DIRECTION;
                                }
                            }
                        } else {
                            oldValue = findLastGoalValue(conn, goalTreeNode.getGoalTreeNodeID());
                            GoalValue goalValue = lastTwoValues.get(0);
                            newValue = goalValue.getValue();
                            if (oldValue != null && newValue != null && goalTreeNode.isGoalDefined()) {
                                double delta = newValue - oldValue;
                                outcomeState = determineOutcome(goalTreeNode.getGoalValue(), goalTreeNode.isHighIsGood(), delta, newValue);
                                if (goalTreeNode.isHighIsGood()) {
                                    direction = GoalOutcome.UP_DIRECTION;
                                } else {
                                    direction = GoalOutcome.DOWN_DIRECTION;
                                }
                            }
                        }

                        if (goalTreeNode.getProblemConditions().size() > 0) {
                            for (FilterDefinition problemCondition : goalTreeNode.getProblemConditions()) {
                                MaterializedFilterDefinition filter = problemCondition.materialize(null);
                                NumericValue numericValue = new NumericValue(newValue);
                                if (filter.allows(numericValue)) {
                                    failedCondition = true;
                                }
                            }
                        }
                    }
                    GoalOutcome goalOutcome = new GoalOutcome(outcomeState, direction, oldValue, failedCondition, newValue, new Date(), goalTreeNode.getGoalTreeNodeID());
                    goalOutcomes.add(goalOutcome);

                }
            if (includeSubtrees && goalTreeNode.getSubTreeID() > 0) {
                        GoalTree subTree;
                        try {
                            subTree = new GoalStorage().retrieveGoalTree(goalTreeNode.getSubTreeID(), conn);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        if (subTree != null) {
                            PopulationGoalTreeVisitor visitor = new PopulationGoalTreeVisitor(conn, credentials, includeSubtrees);
                            visitor.visit(subTree.getRootNode());
                            goalOutcomes.addAll(visitor.goalOutcomes);
                        }

                    }
            }
    }

    public void forceEvaluate(GoalTree goalTree, Connection conn, final List<CredentialFulfillment> credentials, boolean includeSubtrees) throws SQLException {
        final List<GoalOutcome> goalOutcomes = new ArrayList<GoalOutcome>();

        PopulationGoalTreeVisitor goalTreeVisitor = new PopulationGoalTreeVisitor(conn, credentials, includeSubtrees);

        goalTreeVisitor.visit(goalTree.getRootNode());

        goalOutcomes.addAll(goalTreeVisitor.goalOutcomes);

        saveGoalEvaluations(goalOutcomes, conn);
    }

    public GoalOutcome getLatestGoalValue(GoalTreeNode goalTreeNode) throws SQLException {
        GoalOutcome goalOutcome = null;
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement findLastDateStmt = conn.prepareStatement("SELECT MAX(EVALUATION_DATE) FROM GOAL_OUTCOME WHERE GOAL_TREE_NODE_ID = ?");
            findLastDateStmt.setLong(1, goalTreeNode.getGoalTreeNodeID());
            ResultSet dateRS = findLastDateStmt.executeQuery();
            if (dateRS.next()) {
                Timestamp maxDate = dateRS.getTimestamp(1);
                if (!dateRS.wasNull()) {
                    PreparedStatement queryStmt = conn.prepareStatement("SELECT DIRECTION, END_VALUE, EVALUATION_DATE," +
                        "OUTCOME_VALUE, PROBLEM_EVALUATED, START_VALUE FROM GOAL_OUTCOME WHERE GOAL_TREE_NODE_ID = ? AND EVALUATION_DATE = ?");
                    queryStmt.setLong(1, goalTreeNode.getGoalTreeNodeID());
                    queryStmt.setTimestamp(2, maxDate);
                    ResultSet rs = queryStmt.executeQuery();
                    if (rs.next()) {
                        int direction = rs.getInt(1);
                        Double endValue = rs.getDouble(6);
                        if (rs.wasNull()) {
                            endValue = null;
                        }
                        Timestamp timestamp = rs.getTimestamp(3);
                        Date evaluationDate = null;
                        if (!rs.wasNull()) {
                            evaluationDate = new Date(timestamp.getTime());
                        }
                        int outcome = rs.getInt(4);
                        Double value = rs.getDouble(2);
                        if (rs.wasNull()) {
                            value = null;
                        }
                        boolean problem = rs.getBoolean(5);
                        goalOutcome = new GoalOutcome(outcome, direction, endValue, problem, value, evaluationDate, goalTreeNode.getGoalTreeNodeID());
                    }
                }
            }
        } finally {
            Database.closeConnection(conn);
        }
        return goalOutcome;
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
            if (hasDate) {
                goalValues = new HistoryRun().calculateHistoricalValues(goalTreeNode.getCoreFeedID(), goalTreeNode.getAnalysisMeasure(),
                    goalTreeNode.getFilters(), startDate, endDate, credentials);
            }
        } finally {
            Database.closeConnection(conn);
        }
        if (hasDate) {

        }
        return goalValues;
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
