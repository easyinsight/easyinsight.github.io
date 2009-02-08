package com.easyinsight.goals;

import com.easyinsight.analysis.RollingFilterDefinition;
import com.easyinsight.analysis.WSListDefinition;
import com.easyinsight.analysis.InsightRequestMetadata;
import com.easyinsight.AnalysisDateDimension;
import com.easyinsight.AnalysisItemTypes;
import com.easyinsight.AnalysisItem;
import com.easyinsight.DataService;
import com.easyinsight.logging.LogClass;
import com.easyinsight.database.Database;
import com.easyinsight.core.Value;
import com.easyinsight.core.DateValue;
import com.easyinsight.webservice.google.ListDataResults;
import com.easyinsight.webservice.google.ListRow;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
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
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO goal_history (goal_tree_node_id, evaluation_date, evaluation_result) VALUES (?, ?, ?)");
        insertStmt.setLong(1, goalTreeNodeID);
        insertStmt.setDate(2, new java.sql.Date(evaluationDate.getTime()));
        insertStmt.setDouble(3, evaluationResult);
        insertStmt.execute();
    }

    public void saveGoalEvaluations(List<GoalValue> goalValues, Connection conn) throws SQLException {
        for (GoalValue goalValue : goalValues) {
            saveGoalEvaluation(goalValue.getGoalTreeNodeID(), goalValue.getValue(), goalValue.getDate(), conn);
        }
    }

    public void backPopulateGoalTree(GoalTree goalTree, Connection conn) throws SQLException {
        
        final List<GoalValue> goalValues = new ArrayList<GoalValue>();

        final PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM GOAL_HISTORY WHERE goal_tree_node_id = ?");

        GoalTreeVisitor deleteVisitor = new GoalTreeVisitor() {

            protected void accept(GoalTreeNode goalTreeNode) {
                try {
                    deleteStmt.setLong(1, goalTreeNode.getGoalTreeNodeID());
                    deleteStmt.executeUpdate();
                } catch (SQLException e) {
                    LogClass.error(e);
                }
            }
        };

        deleteVisitor.visit(goalTree.getRootNode());

        GoalTreeVisitor goalTreeVisitor = new GoalTreeVisitor() {

            protected void accept(GoalTreeNode goalTreeNode) {
                if (goalTreeNode.getCoreFeedID() > 0) {
                    List<Date> dates = getDates(goalTreeNode);
                    for (Date date : dates) {
                        GoalValue goalValue = evaluateGoalTreeNode(goalTreeNode, date);
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

    private List<Date> getDates(GoalTreeNode goalTreeNode) {
        List<Date> dates = new ArrayList<Date>();
        RollingFilterDefinition rollingFilterDefinition = (RollingFilterDefinition) goalTreeNode.getFilterDefinition();
        AnalysisDateDimension dateItem = (AnalysisDateDimension) rollingFilterDefinition.getField();
        dateItem.setDateLevel(AnalysisItemTypes.DAY_LEVEL);
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

    public GoalValue evaluateGoalTreeNode(GoalTreeNode goalTreeNode, Date date) {
        GoalValue goalValue = null;
        if (goalTreeNode.getCoreFeedID() > 0) {
            WSListDefinition listDefinition = new WSListDefinition();
            listDefinition.setDataFeedID(goalTreeNode.getCoreFeedID());
            List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
            analysisItems.add(goalTreeNode.getAnalysisMeasure());
            listDefinition.setColumns(analysisItems);
            if (goalTreeNode.getFilterDefinition() != null) {
                listDefinition.setFilterDefinitions(Arrays.asList(goalTreeNode.getFilterDefinition()));
            }
            InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
            insightRequestMetadata.setNow(date);
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

    public GoalOutcome getEvaluations(long goalTreeNodeID, Date startDate, Date endDate, double goalValue, boolean highIsGood, int importance) {
        // key here is what's the value at the start date, what's the value at the end date
        // delta between those two values
        // outcomes:
        // end value is > or = goal
        // end value is < goal
        //   is delta positive and > epsilon
        //   is delta ~= epsilon
        //   is delta negative and < epsilon
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT evaluation_result, evaluation_date FROM goal_history " +
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
            int resultLength = goalValues.size();
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
                return new ConcreteGoalOutcome(outcome, goalValue, endValue, startValue, percentChange, outcomeWeight);
            } else {
                return new GoalOutcome(GoalOutcome.NO_DATA, 0);
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
    }

    private int determineOutcome(double goalValue, boolean highIsGood, double delta, double endValue) {
        int outcome;
        if (highIsGood) {
            if (endValue >= goalValue) {
                outcome = GoalOutcome.EXCEEDING_GOAL;
            } else if (Math.abs(delta) < Math.abs(goalValue * .02)) {
                outcome = GoalOutcome.NEUTRAL;
            } else if (delta > 0) {
                outcome = GoalOutcome.POSITIVE;
            } else {
                outcome = GoalOutcome.NEGATIVE;
            }
        } else {
            if (endValue <= goalValue) {
                outcome = GoalOutcome.EXCEEDING_GOAL;
            } else if (Math.abs(delta) < Math.abs(goalValue * .02)) {
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
