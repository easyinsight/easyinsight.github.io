package com.easyinsight.datafeeds.teamwork;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import org.apache.commons.httpclient.HttpClient;

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 11/2/14
 * Time: 5:50 PM
 */
public class TeamworkTimeSource extends TeamworkBaseSource {

    public static final String TIME_ENTRY_ID = "Time Entry ID";
    public static final String TIME_ENTRY_TODO_ID = "Time Entry Task ID";
    public static final String TIME_ENTRY_BILLABLE = "Time Entry Billable";
    public static final String TIME_ENTRY_TODO_LIST_ID = "Time Entry Task List ID";
    public static final String TIME_ENTRY_PROJECT_ID = "Time Entry Project ID";
    public static final String TIME_ENTRY_PERSON = "Time Entry Person";
    public static final String TIME_ENTRY_DATE = "Time Entry Date";
    public static final String TIME_ENTRY_COUNT = "Time Entry Count";
    public static final String TIME_ENTRY_TIME = "Time Entry Hours";

    public TeamworkTimeSource() {
        setFeedName("Time Tracking");
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(TIME_ENTRY_ID, new AnalysisDimension());
        fieldBuilder.addField(TIME_ENTRY_TODO_ID, new AnalysisDimension());
        fieldBuilder.addField(TIME_ENTRY_BILLABLE, new AnalysisDimension());
        fieldBuilder.addField(TIME_ENTRY_TODO_LIST_ID, new AnalysisDimension());
        fieldBuilder.addField(TIME_ENTRY_PROJECT_ID, new AnalysisDimension());
        fieldBuilder.addField(TIME_ENTRY_PERSON, new AnalysisDimension());
        fieldBuilder.addField(TIME_ENTRY_DATE, new AnalysisDateDimension());
        fieldBuilder.addField(TIME_ENTRY_COUNT, new AnalysisMeasure());
        AnalysisMeasure time = new AnalysisMeasure();
        time.setPrecision(2);
        time.setMinPrecision(2);
        fieldBuilder.addField(TIME_ENTRY_TIME, time);
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            DataSet dataSet = new DataSet();
            TeamworkCompositeSource teamworkCompositeSource = (TeamworkCompositeSource) parentDefinition;
            HttpClient httpClient = getHttpClient(teamworkCompositeSource.getTeamworkApiKey());
            int resultCount;
            int page = 1;
            do {
                resultCount = 0;
                Map results = runRestRequestForMap("time_entries.json?page=" + page + "&pageSize=250", httpClient, teamworkCompositeSource);
                List<Map> projects = (List<Map>) results.get("time-entries");
                for (Map project : projects) {
                    IRow row = dataSet.createRow();
                    row.addValue(keys.get(TIME_ENTRY_TODO_ID), getValue(project, "todo-item-id"));
                    row.addValue(keys.get(TIME_ENTRY_ID), getValue(project, "id"));
                    row.addValue(keys.get(TIME_ENTRY_TODO_LIST_ID), getValue(project, "todo-list-id"));
                    row.addValue(keys.get(TIME_ENTRY_BILLABLE), "0".equals(getValue(project, "isbillable")) ? "Not Billable" : "Billable");
                    row.addValue(keys.get(TIME_ENTRY_COUNT), 1);
                    row.addValue(keys.get(TIME_ENTRY_DATE), getDate(project, "date"));
                    row.addValue(keys.get(TIME_ENTRY_PERSON), getValue(project, "person-first-name") + " " + getValue(project, "person-last-name"));
                    row.addValue(keys.get(TIME_ENTRY_PROJECT_ID), getValue(project, "project-id"));
                    row.addValue(keys.get(TIME_ENTRY_TIME), Double.parseDouble(getValue(project, "hours")) + Double.parseDouble(getValue(project, "minutes")) / 60);
                    resultCount++;
                }
            } while (resultCount == 250);
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.TEAMWORK_TIME;
    }
}
