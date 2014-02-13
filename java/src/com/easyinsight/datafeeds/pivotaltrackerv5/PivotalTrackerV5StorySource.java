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
import java.util.*;

/**
 * User: jamesboe
 * Date: 2/6/14
 * Time: 4:48 PM
 */
public class PivotalTrackerV5StorySource extends PivotalTrackerV5BaseSource {

    public static final String ID = "ID";
    public static final String ITERATION_ID = "Iteration ID";
    public static final String PROJECT_ID = "Project ID";
    public static final String NAME = "Name";
    public static final String ACCEPTED_AT = "Accepted At";
    public static final String KIND = "Kind";
    public static final String URL = "URL";
    public static final String CURRENT_STATE = "Current State";
    public static final String STORY_TYPE = "Story Type";
    public static final String CREATED_AT = "Created At";
    public static final String UPDATED_AT = "Updated At";
    public static final String REQUESTED_BY = "Requested By";
    public static final String ESTIMATE = "Estimate";
    public static final String DEADLINE = "Deadline";
    public static final String OWNER = "Owner";
    public static final String COUNT = "Count";

    public static int PAGE_SIZE = 100;

    public PivotalTrackerV5StorySource() {
        setFeedName("Stories");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.PIVOTAL_V5_STORY;
    }

    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(ID, new AnalysisDimension());
        fieldBuilder.addField(PROJECT_ID, new AnalysisDimension());
        fieldBuilder.addField(ITERATION_ID, new AnalysisDimension());
        fieldBuilder.addField(NAME, new AnalysisDimension());
        fieldBuilder.addField(KIND, new AnalysisDimension());
        fieldBuilder.addField(URL, new AnalysisDimension());
        fieldBuilder.addField(CURRENT_STATE, new AnalysisDimension());
        fieldBuilder.addField(STORY_TYPE, new AnalysisDimension());
        fieldBuilder.addField(REQUESTED_BY, new AnalysisDimension());
        fieldBuilder.addField(OWNER, new AnalysisDimension());
        fieldBuilder.addField(ACCEPTED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(CREATED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(DEADLINE, new AnalysisDateDimension());
        fieldBuilder.addField(UPDATED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(ESTIMATE, new AnalysisMeasure());
        fieldBuilder.addField(COUNT, new AnalysisMeasure());
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        HttpClient httpClient = new HttpClient();
        PivotalTrackerV5CompositeSource p = (PivotalTrackerV5CompositeSource) parentDefinition;
        Map<String, List<String>> storyIDToLabelMap = new HashMap<String, List<String>>();
        List<Map> projects = runRequestForList("projects", p, httpClient);
        int page = 0;

        for (Map project : projects) {
            String projectID = getJSONValue(project, "id");
            List<Map> stories;
            do {
                stories = runRequestForList("/projects/" + projectID + "/stories?limit=" + PAGE_SIZE + "&offset=" + (page * PAGE_SIZE), p, httpClient);
                for (Map story : stories) {
                    String storyID = getJSONValue(story, "id");
                    List<Map> labels = (List<Map>) story.get("labels");
                    for (Map label : labels) {
                        String labelID = label.get("id").toString();
                        List<String> labelList = storyIDToLabelMap.get(storyID);
                        if (labelList == null) {
                            labelList = new ArrayList<String>();
                            storyIDToLabelMap.put(storyID, labelList);
                        }
                        labelList.add(labelID);
                    }
                    IRow row = dataSet.createRow();
                    row.addValue(keys.get(ID), storyID);
                    String iterationID = p.getIterationToStoryMap().get(storyID);
                    if (iterationID != null) {
                        row.addValue(keys.get(ITERATION_ID), iterationID);
                    }
                    row.addValue(keys.get(PROJECT_ID), projectID);
                    row.addValue(keys.get(NAME), getJSONValue(story, "name"));
                    row.addValue(keys.get(KIND), getJSONValue(story, "kind"));
                    row.addValue(keys.get(URL), getJSONValue(story, "url"));
                    row.addValue(keys.get(CURRENT_STATE), getJSONValue(story, "current_state"));
                    row.addValue(keys.get(REQUESTED_BY), p.getUser(getJSONValue(story, "requested_by_id")));
                    row.addValue(keys.get(ACCEPTED_AT), getDate(story, "accepted_at"));
                    row.addValue(keys.get(CREATED_AT), getDate(story, "created_at"));
                    row.addValue(keys.get(STORY_TYPE), getJSONValue(story, "url"));
                    row.addValue(keys.get(OWNER), p.getUser(getJSONValue(story, "owned_by_id")));
                    row.addValue(keys.get(DEADLINE), getDate(story, "deadline"));
                    row.addValue(keys.get(UPDATED_AT), getDate(story, "updated_at"));
                    row.addValue(keys.get(ESTIMATE), getJSONValue(story, "estimate"));
                    row.addValue(keys.get(COUNT), 1);
                }
                page = page + 1;
            } while (stories.size() > 0);
        }

        p.setStoryIDToLabelMap(storyIDToLabelMap);
        return dataSet;
    }
}
