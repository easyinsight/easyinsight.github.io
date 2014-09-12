package com.easyinsight.datafeeds.redbooth;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
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
public class RedboothProjectSource extends RedboothBaseSource {
    public static final String ID = "ID";
    public static final String ORGANIZATION_ID = "Project Organization ID";
    public static final String NAME = "Name";
    public static final String CREATED_AT = "Created At";
    public static final String UPDATED_AT = "Updated At";
    public static final String ARCHIVED = "Archived";
    public static final String URL = "URL";

    public RedboothProjectSource() {
        setFeedName("Projects");
    }

    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(ID, new AnalysisDimension());
        fieldBuilder.addField(ORGANIZATION_ID, new AnalysisDimension());
        fieldBuilder.addField(NAME, new AnalysisDimension());
        fieldBuilder.addField(ARCHIVED, new AnalysisDimension());
        fieldBuilder.addField(CREATED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(UPDATED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(URL, new AnalysisDimension());
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.REDBOOTH_PROJECT;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        RedboothCompositeSource redboothCompositeSource = (RedboothCompositeSource) parentDefinition;
        DataSet dataSet = new DataSet();
        HttpClient httpClient = getHttpClient(redboothCompositeSource);
        List<Map> organizations = (List<Map>) queryList("/api/3/projects?per_page=1000", redboothCompositeSource, httpClient);
        Set<String> projectIDs = new HashSet<String>();
        for (Map org : organizations) {
            IRow row = dataSet.createRow();
            String name = getJSONValue(org, "name");
            if ("Personal project".equals(name)) {
                continue;
            }
            String archived = getJSONValue(org, "archived");
            Value createdAt = getDateFromLong(org, "created_at");
            Value updatedAt = getDateFromLong(org, "updated_at");
            String id = getJSONValue(org, "id");
            projectIDs.add(id);
            row.addValue(keys.get(ID), id);
            row.addValue(keys.get(ORGANIZATION_ID), getJSONValue(org, "organization_id"));
            row.addValue(keys.get(CREATED_AT), createdAt);
            row.addValue(keys.get(UPDATED_AT), updatedAt);
            row.addValue(keys.get(ARCHIVED), archived);
            row.addValue(keys.get(NAME), getJSONValue(org, "name"));
            String url = "https://redbooth.com/a/#!/projects/" + id + "/tasks";
            row.addValue(keys.get(URL), url);
        }
        redboothCompositeSource.setValidProjects(projectIDs);
        return dataSet;
    }
}
