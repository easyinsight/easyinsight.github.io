package com.easyinsight.datafeeds.harvest;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataStorage;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: 3/24/11
 * Time: 6:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class HarvestTaskAssignmentSource extends HarvestBaseSource {
    public static final String TASK_ASSIGNMENT_ID = "Task Assignment ID";
    public static final String PROJECT_ID = "Task Assignment Project ID";
    public static final String TASK_ID = "Task Assignment Task ID";
    public static final String BILLABLE = "Task Assignment Billable";
    public static final String DEACTIVATED = "Task Assignment Deactivated";
    public static final String BUDGET = "Task Assignment Budget";
    public static final String HOURLY = "Task Assignment Hourly Rate";
    public static final String TASK_ASSIGNMENT_COUNT = "Task Assignment Count";

    public HarvestTaskAssignmentSource() {
        setFeedName("Task Assignment");
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        AnalysisItem assignmentId = new AnalysisDimension(keys.get(TASK_ASSIGNMENT_ID), true);
        assignmentId.setHidden(true);
        analysisItems.add(assignmentId);
        AnalysisItem projectId = new AnalysisDimension(keys.get(PROJECT_ID), true);
        projectId.setHidden(true);
        analysisItems.add(projectId);
        AnalysisItem taskId = new AnalysisDimension(keys.get(TASK_ID), true);
        taskId.setHidden(true);
        analysisItems.add(taskId);
        analysisItems.add(new AnalysisDimension(keys.get(BILLABLE), true));
        analysisItems.add(new AnalysisDimension(keys.get(DEACTIVATED), true));
        analysisItems.add(new AnalysisMeasure(keys.get(BUDGET), AggregationTypes.SUM));
        analysisItems.add(new AnalysisMeasure(keys.get(HOURLY), HOURLY, AggregationTypes.AVERAGE, true, FormattingConfiguration.CURRENCY));
        analysisItems.add(new AnalysisMeasure(keys.get(TASK_ASSIGNMENT_COUNT), AggregationTypes.SUM));
        return analysisItems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();

        HarvestCompositeSource source = (HarvestCompositeSource) parentDefinition;
        HttpClient client = getHttpClient(source.getUsername(), source.getPassword());
        Builder builder = new Builder();
        try {
            Document projects = source.getOrRetrieveProjects(client, builder);
            Nodes projectNodes = projects.query("/projects/project");
            for(int i = 0;i < projectNodes.size();i++) {
                Node curProject = projectNodes.get(i);
                String projectId = queryField(curProject, "id/text()");
                Document assignments = runRestRequest("/projects/" + projectId + "/task_assignments", client, builder, source.getUrl(), true, source, false);
                Nodes assignmentNodes = assignments.query("/task-assignments/task-assignment");
                for(int j = 0;j < assignmentNodes.size();j++) {
                    Node curAssignment = assignmentNodes.get(j);
                    String assignmentId = queryField(curAssignment, "id/text()");
                    String curProjectId = queryField(curAssignment, "project-id/text()");
                    String taskId = queryField(curAssignment, "task-id/text()");
                    String billable = queryField(curAssignment, "billable/text()");
                    String deactivated = queryField(curAssignment, "deactivated/text()");
                    String budget = queryField(curAssignment, "budget/text()");
                    String hourlyRate = queryField(curAssignment, "hourly-rate/text()");
                    IRow row = dataSet.createRow();
                    row.addValue(keys.get(TASK_ASSIGNMENT_ID), assignmentId);
                    row.addValue(keys.get(PROJECT_ID), curProjectId);
                    row.addValue(keys.get(TASK_ID), taskId);
                    row.addValue(keys.get(BILLABLE), billable);
                    row.addValue(keys.get(DEACTIVATED), deactivated);
                    if(budget != null && budget.length() > 0)
                        row.addValue(keys.get(BUDGET), Double.parseDouble(budget));
                    if(hourlyRate != null && hourlyRate.length() > 0)
                        row.addValue(keys.get(HOURLY), hourlyRate);
                    row.addValue(keys.get(TASK_ASSIGNMENT_COUNT), 1.0);
                }
            }
        } catch (ParsingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return dataSet;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(TASK_ASSIGNMENT_ID, PROJECT_ID, TASK_ID, BILLABLE, DEACTIVATED, BUDGET, HOURLY, TASK_ASSIGNMENT_COUNT);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HARVEST_TASK_ASSIGNMENTS;
    }
}
