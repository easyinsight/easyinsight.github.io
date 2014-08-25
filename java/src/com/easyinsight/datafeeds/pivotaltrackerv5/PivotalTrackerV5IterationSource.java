package com.easyinsight.datafeeds.pivotaltrackerv5;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
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
public class PivotalTrackerV5IterationSource extends PivotalTrackerV5BaseSource {

    public static final String PROJECT_ID = "Project ID";
    public static final String ID = "ID";
    public static final String NUMBER = "Number";
    public static final String TEAM_STRENGTH = "Team Strength";
    public static final String START = "Start Date";
    public static final String FINISH = "Finish Date";
    public static final String CURRENT_STATE = "Iteration State";

    public static final int PAGE_SIZE = 15;

    public PivotalTrackerV5IterationSource() {
        setFeedName("Iterations");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.PIVOTAL_V5_ITERATION;
    }

    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(PROJECT_ID, new AnalysisDimension());
        fieldBuilder.addField(ID, new AnalysisDimension());
        fieldBuilder.addField(TEAM_STRENGTH, new AnalysisDimension());
        fieldBuilder.addField(NUMBER, new AnalysisDimension());
        fieldBuilder.addField(START, new AnalysisDateDimension());
        fieldBuilder.addField(FINISH, new AnalysisDateDimension());
        fieldBuilder.addField(CURRENT_STATE, new AnalysisDimension());
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        HttpClient httpClient = new HttpClient();
        Map<String, String> iterationToStoryMap = new HashMap<String, String>();
        Map<String, String> userMap = new HashMap<String, String>();
        Map<String, String> iterationToStateMap = new HashMap<String, String>();
        List<Map> projects = runRequestForList("projects", (PivotalTrackerV5CompositeSource) parentDefinition, httpClient);
        for (Map project : projects) {
            String projectID = getJSONValue(project, "id");
            try {
                List<Map> memberships = runRequestForList("/projects/" + projectID + "/memberships", (PivotalTrackerV5CompositeSource) parentDefinition, httpClient);
                for (Map membership : memberships) {
                    Map person = (Map) membership.get("person");
                    String name = person.get("name").toString();
                    String id = person.get("id").toString();
                    userMap.put(id, name);
                }
            } catch (Exception e) {
                System.out.println("!");
                // they may not have access to membership information for this project
            }
            {
                int page = 0;
                List<Map> iterations;
                do {
                    iterations = runRequestForList("/projects/" + projectID + "/iterations?scope=done&limit=" + PAGE_SIZE + "&offset=" + (page * PAGE_SIZE), (PivotalTrackerV5CompositeSource) parentDefinition, httpClient);
                    for (Map story : iterations) {
                        String id = projectID + "-" + getJSONValue(story, "number");
                        parseIterations(keys, dataSet, iterationToStoryMap, projectID, story, "done");
                        iterationToStateMap.put(id, "Done");
                    }
                    page++;
                } while (iterations.size() > 0);
            }
            {
                int page = 0;
                List<Map> iterations;
                do {
                    iterations = runRequestForList("/projects/" + projectID + "/iterations?scope=current&limit=" + PAGE_SIZE + "&offset=" + (page * PAGE_SIZE), (PivotalTrackerV5CompositeSource) parentDefinition, httpClient);
                    for (Map story : iterations) {
                        String id = projectID + "-" + getJSONValue(story, "number");
                        parseIterations(keys, dataSet, iterationToStoryMap, projectID, story, "current");
                        iterationToStateMap.put(id, "Current");
                    }
                    page++;
                } while (iterations.size() > 0);
            }
            {
                int page = 0;
                List<Map> iterations;
                do {
                    iterations = runRequestForList("/projects/" + projectID + "/iterations?scope=backlog&limit=" + PAGE_SIZE + "&offset=" + (page * PAGE_SIZE), (PivotalTrackerV5CompositeSource) parentDefinition, httpClient);
                    for (Map story : iterations) {
                        String id = projectID + "-" + getJSONValue(story, "number");
                        parseIterations(keys, dataSet, iterationToStoryMap, projectID, story, "backlog");
                        iterationToStateMap.put(id, "Backlog");
                    }
                    page++;
                } while (iterations.size() > 0);
            }
        }
        ((PivotalTrackerV5CompositeSource) parentDefinition).setIterationToStoryMap(iterationToStoryMap);
        ((PivotalTrackerV5CompositeSource) parentDefinition).setUserMap(userMap);
        ((PivotalTrackerV5CompositeSource) parentDefinition).setIterationToStateMap(iterationToStateMap);
        return dataSet;
    }

    private void parseIterations(Map<String, Key> keys, DataSet dataSet, Map<String, String> iterationToStoryMap, String projectID, Map story, String scope) {
        List<Map> labels = (List<Map>) story.get("stories");
        String id = projectID + "-" + getJSONValue(story, "number");
        for (Map label : labels) {
            String labelID = label.get("id").toString();
            iterationToStoryMap.put(labelID, id);
        }
        IRow row = dataSet.createRow();

        row.addValue(keys.get(ID), id);
        row.addValue(keys.get(PROJECT_ID), projectID);
        row.addValue(keys.get(NUMBER), getJSONValue(story, "number"));
        row.addValue(keys.get(TEAM_STRENGTH), getJSONValue(story, "team_strength"));
        row.addValue(keys.get(START), getDate(story, "start"));
        row.addValue(keys.get(FINISH), getDate(story, "finish"));
        row.addValue(keys.get(CURRENT_STATE), scope);
    }
}
