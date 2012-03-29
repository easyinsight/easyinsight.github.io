package com.easyinsight.datafeeds.basecampnext;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 3/26/12
 * Time: 11:06 AM
 */
public class BasecampNextProjectSource extends BasecampNextBaseSource {

    public static final String PROJECT_ID = "Project ID";
    public static final String PROJECT_NAME = "Project Name";
    public static final String DESCRIPTION = "Project Description";
    public static final String UPDATED_AT = "Project Updated At";
    public static final String URL = "Project URL";

    public BasecampNextProjectSource() {
        setFeedName("Projects");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.BASECAMP_NEXT_PROJECTS;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(PROJECT_ID, PROJECT_NAME, UPDATED_AT, URL, DESCRIPTION);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(PROJECT_ID), PROJECT_ID));
        analysisitems.add(new AnalysisDimension(keys.get(PROJECT_NAME), PROJECT_NAME));
        analysisitems.add(new AnalysisDimension(keys.get(DESCRIPTION), DESCRIPTION));
        analysisitems.add(new AnalysisDimension(keys.get(URL), URL));
        analysisitems.add(new AnalysisDateDimension(keys.get(UPDATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
            DataSet dataSet = new DataSet();
            JSONArray jsonArray = runJSONRequest("projects.json", (BasecampNextCompositeSource) parentDefinition);
            for (int i = 0; i < jsonArray.length(); i++) {
                IRow row = dataSet.createRow();
                JSONObject projectObject = jsonArray.getJSONObject(i);
                row.addValue(keys.get(PROJECT_ID), String.valueOf(projectObject.getInt("id")));
                row.addValue(keys.get(PROJECT_NAME), projectObject.getString("name"));
                row.addValue(keys.get(DESCRIPTION), projectObject.getString("description"));
                row.addValue(keys.get(URL), projectObject.getString("url"));
                row.addValue(keys.get(UPDATED_AT), format.parseDateTime(projectObject.getString("updated_at")).toDate());
            }
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
