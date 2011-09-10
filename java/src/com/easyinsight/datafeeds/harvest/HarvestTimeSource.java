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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 3/21/11
 * Time: 7:37 PM
 */
public class HarvestTimeSource extends HarvestBaseSource {
    public static final String XMLDATEFORMAT = "yyyy-MM-dd";
    public static DateFormat DATE_FORMAT = new SimpleDateFormat(XMLDATEFORMAT);
    public static final String QUERYDATEFORMAT = "yyyyMMdd";
    public static DateFormat OUT_DATE = new SimpleDateFormat(QUERYDATEFORMAT);

    public static final String HOURS = "Hours";
    public static final String TIME_ID = "Time Tracking ID";
    public static final String NOTES = "Time Tracking Notes";
    public static final String PROJECT_ID = "Time Tracking Project ID";
    public static final String SPENT_AT = "Time Spent At";
    public static final String TASK_ID = "Time Tracking Task ID";
    public static final String USER_ID = "Time Tracking User ID";
    public static final String IS_BILLED = "Time Tracking Invoiced";
    public static final String IS_CLOSED = "Time Approved";
    public static final String CREATED_AT = "Time Tracking Created At";
    public static final String TIME_COUNT = "Time Tracking Count";

    public HarvestTimeSource() {
        setFeedName("Time Tracking");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HARVEST_TIME;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(HOURS, TIME_ID, NOTES, PROJECT_ID, SPENT_AT,
                TASK_ID, USER_ID, IS_BILLED, IS_CLOSED, CREATED_AT, TIME_COUNT);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();

        AnalysisItem timeIdDim = new AnalysisDimension(keys.get(TIME_ID), true);
        timeIdDim.setHidden(true);
        analysisItems.add(timeIdDim);

        AnalysisItem projectIdDim = new AnalysisDimension(keys.get(PROJECT_ID), true);
        projectIdDim.setHidden(true);
        analysisItems.add(projectIdDim);

        AnalysisItem taskIdDim = new AnalysisDimension(keys.get(TASK_ID), true);
        taskIdDim.setHidden(true);
        analysisItems.add(taskIdDim);

        AnalysisItem userIdDim = new AnalysisDimension(keys.get(USER_ID), true);
        userIdDim.setHidden(true);
        analysisItems.add(userIdDim);

        analysisItems.add(new AnalysisMeasure(keys.get(HOURS), AggregationTypes.SUM));
        analysisItems.add(new AnalysisText(keys.get(NOTES)));
        analysisItems.add(new AnalysisDateDimension(keys.get(SPENT_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDimension(keys.get(IS_BILLED), true));
        analysisItems.add(new AnalysisDimension(keys.get(IS_CLOSED), true));
        analysisItems.add(new AnalysisDateDimension(keys.get(CREATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisMeasure(keys.get(TIME_COUNT), AggregationTypes.SUM));

        return analysisItems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
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
                String latestRecord = queryField(curProject, "hint-latest-record-at/text()");
                String earliestRecord = queryField(curProject, "hint-earliest-record-at/text()");

                Document entries = runRestRequest("/projects/" + projectId + "/entries?from=" + OUT_DATE.format(DATE_FORMAT.parse(earliestRecord)) + "&to=" + OUT_DATE.format(DATE_FORMAT.parse(latestRecord)), client, builder, source.getUrl(), true, source, false);
                Nodes entryNodes = entries.query("/day-entries/day-entry");
                for(int j = 0;j < entryNodes.size();j++) {
                    Node timeEntry = entryNodes.get(j);
                    String hours = queryField(timeEntry, "hours/text()");
                    String entryId = queryField(timeEntry, "id/text()");
                    String notes = queryField(timeEntry, "notes/text()");
                    String curProjectId = queryField(timeEntry, "project-id/text()");
                    String spentAt = queryField(timeEntry, "spent-at/text()");
                    String taskId = queryField(timeEntry, "task-id/text()");
                    String userId = queryField(timeEntry, "user-id/text()");
                    String isBilled = queryField(timeEntry, "is-billed/text()");
                    String isClosed = queryField(timeEntry, "is-closed/text()");
                    String createdAt = queryField(timeEntry, "created-at/text()");
                    IRow row = dataSet.createRow();
                    row.addValue(keys.get(HOURS), Double.parseDouble(hours));
                    row.addValue(keys.get(TIME_ID), entryId);
                    row.addValue(keys.get(NOTES), notes);
                    row.addValue(keys.get(PROJECT_ID), curProjectId);
                    row.addValue(keys.get(SPENT_AT), DATE_FORMAT.parse(spentAt));
                    row.addValue(keys.get(TASK_ID), taskId);
                    row.addValue(keys.get(USER_ID), userId);
                    row.addValue(keys.get(IS_BILLED), isBilled);
                    row.addValue(keys.get(IS_CLOSED), isClosed);
                    row.addValue(keys.get(TIME_COUNT), 1.0);
                    row.addValue(keys.get(CREATED_AT), javax.xml.bind.DatatypeConverter.parseDateTime(createdAt).getTime());
                }
            }
        } catch (ParsingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return dataSet;
    }
}
