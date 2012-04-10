package com.easyinsight.datafeeds.basecampnext;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import org.jetbrains.annotations.NotNull;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 4/9/12
 * Time: 8:57 AM
 */
public class BasecampNextPeopleSource extends BasecampNextBaseSource {

    public static final String PERSON_ID = "Person ID";
    public static final String PERSON_NAME = "Person Name";
    public static final String PERSON_EMAIL = "Person Description";
    public static final String PERSON_UPDATED_AT = "Person Updated At";
    public static final String URL = "Person URL";

    public BasecampNextPeopleSource() {
        setFeedName("People");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.BASECAMP_NEXT_PEOPLE;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(PERSON_ID, PERSON_NAME, PERSON_UPDATED_AT, URL, PERSON_EMAIL);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(PERSON_ID), PERSON_ID));
        analysisitems.add(new AnalysisDimension(keys.get(PERSON_NAME), PERSON_NAME));
        analysisitems.add(new AnalysisDimension(keys.get(PERSON_EMAIL), PERSON_EMAIL));
        analysisitems.add(new AnalysisDimension(keys.get(URL), URL));
        analysisitems.add(new AnalysisDateDimension(keys.get(PERSON_UPDATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
            DataSet dataSet = new DataSet();
            JSONArray jsonArray = runJSONRequest("people.json", (BasecampNextCompositeSource) parentDefinition);
            for (int i = 0; i < jsonArray.length(); i++) {
                IRow row = dataSet.createRow();
                JSONObject projectObject = jsonArray.getJSONObject(i);
                row.addValue(keys.get(PERSON_ID), String.valueOf(projectObject.getInt("id")));
                row.addValue(keys.get(PERSON_NAME), projectObject.getString("name"));
                row.addValue(keys.get(PERSON_EMAIL), projectObject.getString("email_address"));
                row.addValue(keys.get(URL), projectObject.getString("url"));
                row.addValue(keys.get(PERSON_UPDATED_AT), format.parseDateTime(projectObject.getString("updated_at")).toDate());
            }
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
