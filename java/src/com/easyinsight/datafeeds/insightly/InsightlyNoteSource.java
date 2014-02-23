package com.easyinsight.datafeeds.insightly;

import com.easyinsight.analysis.*;
import com.easyinsight.core.*;
import com.easyinsight.core.StringValue;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 10/22/12
 * Time: 7:24 PM
 */
public class InsightlyNoteSource extends InsightlyBaseSource {

    public static final String NOTE_ID = "Note ID";
    public static final String TITLE = "Note Title";
    public static final String BODY = "Note Body";
    public static final String OWNER = "Note Owner";

    public static final String DATE_CREATED = "Note Date Created";
    public static final String DATE_UPDATED = "Note Date Updated";



    public InsightlyNoteSource() {
        setFeedName("Notes");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(NOTE_ID, TITLE, BODY, OWNER, DATE_CREATED, DATE_UPDATED);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {

        List<AnalysisItem> fields = new ArrayList<AnalysisItem>();
        fields.add(new AnalysisDimension(keys.get(NOTE_ID)));
        fields.add(new AnalysisDimension(keys.get(TITLE)));
        AnalysisText body = new AnalysisText(keys.get(BODY));
        body.setHtml(true);
        fields.add(body);
        fields.add(new AnalysisDimension(keys.get(OWNER)));
        fields.add(new AnalysisDateDimension(keys.get(DATE_CREATED), true, AnalysisDateDimension.DAY_LEVEL));
        fields.add(new AnalysisDateDimension(keys.get(DATE_UPDATED), true, AnalysisDateDimension.DAY_LEVEL));
        return fields;
    }

    private Value getValue(Map map, String param) {
        Object obj = map.get(param);
        if (obj == null) {
            return new EmptyValue();
        } else {
            return new StringValue(obj.toString());
        }
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DataSet dataSet = new DataSet();
            InsightlyCompositeSource insightlyCompositeSource = (InsightlyCompositeSource) parentDefinition;
            HttpClient httpClient = getHttpClient(insightlyCompositeSource.getInsightlyApiKey(), "x");
            List userList = runJSONRequest("users", insightlyCompositeSource, httpClient);
            Map<String, String> userMap = new HashMap<String, String>();
            for (Object userObject : userList) {
                Map user = (Map) userObject;
                if (user.get("USER_ID") != null) {
                    String userID = user.get("USER_ID").toString();
                    Object firstNameObj = user.get("FIRST_NAME");
                    Object lastNameObj = user.get("LAST_NAME");
                    String name;
                    if (firstNameObj == null && lastNameObj == null) {
                        name = null;
                    } else if (firstNameObj != null && lastNameObj == null) {
                        name = firstNameObj.toString();
                    } else if (firstNameObj == null && lastNameObj != null) {
                        name = lastNameObj.toString();
                    } else {
                        name = firstNameObj.toString() + " " + lastNameObj.toString();
                    }
                    if (name != null) {
                        userMap.put(userID, name);
                    }

                }
            }
            List contactList = runJSONRequest("notes", insightlyCompositeSource, httpClient);
            for (Object contactObj : contactList) {
                IRow row = dataSet.createRow();
                Map contactMap = (Map) contactObj;
                row.addValue(keys.get(NOTE_ID), contactMap.get("NOTE_ID").toString());
                String owner = getValue(contactMap, "OWNER_USER_ID").toString();
                owner = userMap.get(owner);
                if (owner != null) {
                    row.addValue(keys.get(OWNER), owner);
                }
                row.addValue(keys.get(TITLE), getValue(contactMap, "TITLE"));
                row.addValue(keys.get(BODY), getValue(contactMap, "BODY"));

                row.addValue(keys.get(DATE_CREATED), new DateValue(sdf.parse(contactMap.get("DATE_CREATED_UTC").toString())));
                row.addValue(keys.get(DATE_UPDATED), new DateValue(sdf.parse(contactMap.get("DATE_UPDATED_UTC").toString())));
            }
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INSIGHTLY_NOTES;
    }
}
