package com.easyinsight.datafeeds.harvest;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
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
public class HarvestUserAssignmentSource extends HarvestBaseSource {
    public static final String USER_ASSIGNMENT_ID = "User Assignment ID";
    public static final String PROJECT_ID = "User Assignment Project ID";
    public static final String USER_ID = "User Assignment User ID";
    public static final String DEACTIVATED = "User Assignment Deactivated";
    public static final String HOURLY = "User Assignment Hourly Rate";
    public static final String USER_ASSIGNMENT_COUNT = "User Assignment Count";

    public HarvestUserAssignmentSource() {
        setFeedName("User Assignment");
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        AnalysisItem assignmentId = new AnalysisDimension(keys.get(USER_ASSIGNMENT_ID), true);
        assignmentId.setHidden(true);
        analysisItems.add(assignmentId);
        AnalysisItem projectId = new AnalysisDimension(keys.get(PROJECT_ID), true);
        projectId.setHidden(true);
        analysisItems.add(projectId);
        AnalysisItem taskId = new AnalysisDimension(keys.get(USER_ID), true);
        taskId.setHidden(true);
        analysisItems.add(taskId);
        analysisItems.add(new AnalysisDimension(keys.get(DEACTIVATED), true));
        analysisItems.add(new AnalysisMeasure(keys.get(HOURLY), HOURLY, AggregationTypes.AVERAGE, true, FormattingConfiguration.CURRENCY));
        analysisItems.add(new AnalysisMeasure(keys.get(USER_ASSIGNMENT_COUNT), AggregationTypes.SUM));
        return analysisItems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();

        HarvestCompositeSource source = (HarvestCompositeSource) parentDefinition;
        HttpClient client = getHttpClient(source.getUsername(), source.getPassword());
        Builder builder = new Builder();
        try {
            Document projects = source.getOrRetrieveProjects(client, builder, false);
            Nodes projectNodes = projects.query("/projects/project");
            for(int i = 0;i < projectNodes.size();i++) {
                Node curProject = projectNodes.get(i);
                String projectId = queryField(curProject, "id/text()");
                Document assignments = runRestRequest("/projects/" + projectId + "/user_assignments", client, builder, source.getUrl(), true, source, false);
                Nodes assignmentNodes = assignments.query("/user-assignments/user-assignment");
                for(int j = 0;j < assignmentNodes.size();j++) {
                    Node curAssignment = assignmentNodes.get(j);
                    String assignmentId = queryField(curAssignment, "id/text()");
                    String curProjectId = queryField(curAssignment, "project-id/text()");
                    String userId = queryField(curAssignment, "user-id/text()");
                    String deactivated = queryField(curAssignment, "deactivated/text()");
                    String hourlyRate = queryField(curAssignment, "hourly-rate/text()");
                    IRow row = dataSet.createRow();
                    row.addValue(keys.get(USER_ASSIGNMENT_ID), assignmentId);
                    row.addValue(keys.get(PROJECT_ID), curProjectId);
                    row.addValue(keys.get(USER_ID), userId);
                    row.addValue(keys.get(DEACTIVATED), deactivated);
                    if(hourlyRate != null && hourlyRate.length() > 0)
                        row.addValue(keys.get(HOURLY), hourlyRate);
                    row.addValue(keys.get(USER_ASSIGNMENT_COUNT), 1.0);
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
        return Arrays.asList(USER_ASSIGNMENT_ID, PROJECT_ID, USER_ID, DEACTIVATED, HOURLY, USER_ASSIGNMENT_COUNT);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HARVEST_USER_ASSIGNMENTS;
    }
}
