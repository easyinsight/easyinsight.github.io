package com.easyinsight.datafeeds.pivotaltrackerv5;

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
 * Date: 2/6/14
 * Time: 4:48 PM
 */
public class PivotalTrackerV5ProjectSource extends PivotalTrackerV5BaseSource {

    public static final String ID = "ID";
    public static final String NAME = "Name";
    public static final String ITERATION_LENGTH = "Iteration Length";
    public static final String DESCRIPTION = "Description";
    public static final String CREATED_AT = "Project Created At";
    public static final String UPDATED_AT = "Project Updated At";
    public static final String VELOCITY_AVERAGED = "Average Velocity";
    public static final String INITIAL_VELOCITY = "Initial Velocity";
    public static final String START_DATE = "Start Date";
    public static final String CURRENT_ITERATION_NUMBER = "Current Iteration";

    public PivotalTrackerV5ProjectSource() {
        setFeedName("Projects");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.PIVOTAL_V5_PROJECT;
    }

    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(ID, new AnalysisDimension());
        fieldBuilder.addField(NAME, new AnalysisDimension());
        fieldBuilder.addField(ITERATION_LENGTH, new AnalysisDimension());
        fieldBuilder.addField(DESCRIPTION, new AnalysisDimension());
        fieldBuilder.addField(CURRENT_ITERATION_NUMBER, new AnalysisDimension());
        fieldBuilder.addField(CREATED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(UPDATED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(START_DATE, new AnalysisDateDimension());
        fieldBuilder.addField(INITIAL_VELOCITY, new AnalysisMeasure());
        fieldBuilder.addField(VELOCITY_AVERAGED, new AnalysisMeasure());
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        HttpClient httpClient = new HttpClient();
        List<Map> projects = runRequestForList("projects", (PivotalTrackerV5CompositeSource) parentDefinition, httpClient);
        for (Map project : projects) {
            IRow row = dataSet.createRow();
            row.addValue(keys.get(ID), getJSONValue(project, "id"));
            row.addValue(keys.get(NAME), getJSONValue(project, "name"));
            row.addValue(keys.get(ITERATION_LENGTH), getJSONValue(project, "iteration_length"));
            row.addValue(keys.get(CURRENT_ITERATION_NUMBER), getJSONValue(project, "current_iteration_number"));
            row.addValue(keys.get(DESCRIPTION), getJSONValue(project, "description"));
            row.addValue(keys.get(CREATED_AT), getDate(project, "created_at"));
            row.addValue(keys.get(UPDATED_AT), getDate(project, "updated_at"));
            row.addValue(keys.get(START_DATE), getDate(project, "start_date"));
            row.addValue(keys.get(INITIAL_VELOCITY), getDate(project, "initial_velocity"));
            row.addValue(keys.get(VELOCITY_AVERAGED), getDate(project, "velocity_averaged_over"));
        }
        return dataSet;
    }
}
