package com.easyinsight.datafeeds.redbooth;

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
 * Date: 2/19/14
 * Time: 7:09 PM
 */
public class RedboothCommentSource extends RedboothBaseSource {
    public static final String ID = "ID";
    public static final String BODY = "Body";
    public static final String BODY_HTML = "Body HTML";
    public static final String PROJECT_ID = "Project ID";
    public static final String HOURS = "Hours";
    public static final String TASK_ID = "Task ID";
    public static final String CONVERSATION_ID = "Conversation ID";
    public static final String USER_ID = "User ID";
    public static final String CREATED_AT = "Created At";

    public RedboothCommentSource() {
        setFeedName("Comments");
    }

    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(ID, new AnalysisDimension());
        fieldBuilder.addField(BODY, new AnalysisText());
        fieldBuilder.addField(BODY_HTML, new AnalysisText());
        fieldBuilder.addField(PROJECT_ID, new AnalysisDimension());
        fieldBuilder.addField(TASK_ID, new AnalysisDimension());
        fieldBuilder.addField(CONVERSATION_ID, new AnalysisDimension());
        fieldBuilder.addField(USER_ID, new AnalysisDimension());
        fieldBuilder.addField(CREATED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(HOURS, new AnalysisMeasure());
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.REDBOOTH_COMMENT;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        RedboothCompositeSource redboothCompositeSource = (RedboothCompositeSource) parentDefinition;
        DataSet dataSet = new DataSet();
        HttpClient httpClient = getHttpClient(redboothCompositeSource);
        Map base = (Map) queryList("/api/1/comments?count=0", redboothCompositeSource, httpClient);
        List<Map> references = (List<Map>) base.get("references");
        Map<String, String> users = new HashMap<String, String>();
        for (Map ref : references) {
            String type = ref.get("type").toString();
            if ("User".equals(type)) {
                users.put(ref.get("id").toString(), ref.get("first_name").toString() + " " + ref.get("last_name").toString());
            }
        }
        List<Map> organizations = (List<Map>) base.get("objects");
        Set<String> validIDs = redboothCompositeSource.getValidProjects();
        for (Map org : organizations) {
            String projectID = getJSONValue(org, "project_id");
            if (!validIDs.contains(projectID)) {
                continue;
            }
            IRow row = dataSet.createRow();
            row.addValue(keys.get(ID), getJSONValue(org, "id"));
            row.addValue(keys.get(BODY), getJSONValue(org, "body"));
            row.addValue(keys.get(BODY_HTML), getJSONValue(org, "body_html"));
            row.addValue(keys.get(CREATED_AT), getDate(org, "created_at"));
            row.addValue(keys.get(PROJECT_ID), projectID);
            row.addValue(keys.get(HOURS), getJSONValue(org, "hours"));
            String targetType = getJSONValue(org, "target_type");
            if ("Conversation".equals(targetType)) {
                row.addValue(keys.get(CONVERSATION_ID), getJSONValue(org, "target_id"));
            } else {
                row.addValue(keys.get(TASK_ID), getJSONValue(org, "target_id"));
            }
            String userID = getJSONValue(org, "user_id");
            if (userID != null) {
                row.addValue(USER_ID, users.get(userID));
            }
        }
        return dataSet;
    }
}
