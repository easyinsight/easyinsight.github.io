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
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 3/29/12
 * Time: 2:30 PM
 */
public class BasecampNextCalendarSource extends BasecampNextBaseSource {

    public static final String CALENDAR_ID = "Calendar ID";
    public static final String CALENDAR_NAME = "Calendar Name";
    public static final String UPDATED_AT = "Calendar Updated At";
    public static final String URL = "Calendar URL";
    public static final String CALENDAR_EVENT_ID = "Calendar Event ID";
    public static final String CALENDAR_EVENT_SUMMARY = "Calendar Event Summary";
    public static final String CALENDAR_EVENT_DESCRIPTION = "Calendar Event Description";
    public static final String CALENDAR_EVENT_CREATED_AT = "Calendar Event Created At";
    public static final String CALENDAR_EVENT_UPDATED_AT = "Calendar Event Updated At";
    public static final String CALENDAR_EVENT_ALL_DAY = "Calendar Event All Day";
    public static final String CALENDAR_EVENT_STARTS_AT = "Calendar Event Starts At";
    public static final String CALENDAR_EVENT_ENDS_AT = "Calendar Event Ends At";
    public static final String CALENDAR_EVENT_URL = "Calendar Event URL";
    public static final String CALENDAR_EVENT_COUNT = "Calendar Event Count";
    public static final String CALENDAR_EVENT_PROJECT_ID = "Calendar Event Project ID";

    public BasecampNextCalendarSource() {
        setFeedName("Calendar");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.BASECAMP_NEXT_CALENDAR;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(CALENDAR_ID, CALENDAR_NAME, UPDATED_AT, URL, CALENDAR_EVENT_ID, CALENDAR_EVENT_SUMMARY,
                CALENDAR_EVENT_DESCRIPTION, CALENDAR_EVENT_CREATED_AT, CALENDAR_EVENT_UPDATED_AT, CALENDAR_EVENT_ALL_DAY,
                CALENDAR_EVENT_STARTS_AT, CALENDAR_EVENT_ENDS_AT, CALENDAR_EVENT_URL, CALENDAR_EVENT_COUNT, CALENDAR_EVENT_PROJECT_ID);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(CALENDAR_ID), CALENDAR_ID));
        analysisitems.add(new AnalysisDimension(keys.get(CALENDAR_NAME), CALENDAR_NAME));
        analysisitems.add(new AnalysisDimension(keys.get(CALENDAR_EVENT_PROJECT_ID), CALENDAR_EVENT_PROJECT_ID));
        analysisitems.add(new AnalysisDimension(keys.get(URL), URL));
        analysisitems.add(new AnalysisDateDimension(keys.get(UPDATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisitems.add(new AnalysisDimension(keys.get(CALENDAR_EVENT_ID)));
        analysisitems.add(new AnalysisDimension(keys.get(CALENDAR_EVENT_SUMMARY)));
        analysisitems.add(new AnalysisDimension(keys.get(CALENDAR_EVENT_DESCRIPTION)));
        analysisitems.add(new AnalysisDimension(keys.get(CALENDAR_EVENT_ALL_DAY)));
        analysisitems.add(new AnalysisDimension(keys.get(CALENDAR_EVENT_URL)));
        analysisitems.add(new AnalysisDateDimension(keys.get(CALENDAR_EVENT_CREATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisitems.add(new AnalysisDateDimension(keys.get(CALENDAR_EVENT_UPDATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisitems.add(new AnalysisDateDimension(keys.get(CALENDAR_EVENT_STARTS_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisitems.add(new AnalysisDateDimension(keys.get(CALENDAR_EVENT_ENDS_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisitems.add(new AnalysisMeasure(keys.get(CALENDAR_EVENT_COUNT), AggregationTypes.SUM));
        return analysisitems;
    }

    private Date parseDate(String string) {
        if (string != null && !"null".equals(string) && !"".equals(string)) {
            try {
                return format.parseDateTime(string).toDate();
            } catch (Exception e) {
                try {
                    return otherFormat.parse(string);
                } catch (ParseException e1) {
                    e1.printStackTrace();
                    return null;
                }
            }
        }
        return null;
    }

    private static final DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
    public static final DateFormat otherFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
            DataSet dataSet = new DataSet();
            JSONArray jsonArray = runJSONRequest("calendars.json", (BasecampNextCompositeSource) parentDefinition);
            for (int i = 0; i < jsonArray.length(); i++) {
                
                JSONObject projectObject = jsonArray.getJSONObject(i);
                String calendarID = String.valueOf(projectObject.getInt("id"));
                String calendarName = projectObject.getString("name");
                String calendarURL = projectObject.getString("url");
                Date calendarUpdatedAt = format.parseDateTime(projectObject.getString("updated_at")).toDate();
                JSONArray eventArray = runJSONRequest("calendars/"+calendarID+"/calendar_events.json", (BasecampNextCompositeSource) parentDefinition);
                parseCalendarEvents(keys, dataSet, calendarID, calendarName, calendarURL, calendarUpdatedAt, eventArray, null);
                JSONArray pastEventArray = runJSONRequest("calendars/"+calendarID+"/calendar_events/past.json", (BasecampNextCompositeSource) parentDefinition);
                parseCalendarEvents(keys, dataSet, calendarID, calendarName, calendarURL, calendarUpdatedAt, pastEventArray, null);
            }
            JSONArray projectArray = runJSONRequest("projects.json", (BasecampNextCompositeSource) parentDefinition);
            for (int i = 0; i < projectArray.length(); i++) {
                JSONObject projectObject = projectArray.getJSONObject(i);
                String projectID = String.valueOf(projectObject.getInt("id"));

                try {
                    Object eventObj = runJSONRequest("projects/" + projectID + "/calendar_events.json", (BasecampNextCompositeSource) parentDefinition);
                    if (eventObj instanceof JSONArray) {
                        JSONArray eventArray = (JSONArray) eventObj;
                        parseCalendarEvents(keys, dataSet, null, null, null, null, eventArray, projectID);
                    }
                    Object pastEventObject = runJSONRequest("projects/"+projectID+"/calendar_events/past.json", (BasecampNextCompositeSource) parentDefinition);
                    if (pastEventObject instanceof JSONArray) {
                        JSONArray eventArray = (JSONArray) pastEventObject;
                        parseCalendarEvents(keys, dataSet, null, null, null, null, eventArray, projectID);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void parseCalendarEvents(Map<String, Key> keys, DataSet dataSet, String calendarID, String calendarName, String calendarURL, Date calendarUpdatedAt, JSONArray eventArray, String projectID) throws JSONException {
        for (int j = 0; j < eventArray.length(); j++) {
            JSONObject calendarEvent = eventArray.getJSONObject(j);
            String id = calendarEvent.getString("id");
            String summary = calendarEvent.getString("summary");
            String url = calendarEvent.getString("url");
            String description = calendarEvent.getString("description");
            Date createdAt = parseDate(calendarEvent.getString("created_at"));
            Date updatedAt = parseDate(calendarEvent.getString("updated_at"));
            Date startsAt = parseDate(calendarEvent.getString("starts_at"));
            Date endsAt = parseDate(calendarEvent.getString("ends_at"));
            IRow row = dataSet.createRow();
            row.addValue(keys.get(CALENDAR_ID), calendarID);
            row.addValue(keys.get(CALENDAR_NAME), calendarName);
            row.addValue(keys.get(UPDATED_AT), calendarUpdatedAt);
            row.addValue(keys.get(URL), calendarURL);
            row.addValue(keys.get(CALENDAR_EVENT_ID), id);
            row.addValue(keys.get(CALENDAR_EVENT_SUMMARY), summary);
            row.addValue(keys.get(CALENDAR_EVENT_DESCRIPTION), description);
            row.addValue(keys.get(CALENDAR_EVENT_CREATED_AT), createdAt);
            row.addValue(keys.get(CALENDAR_EVENT_UPDATED_AT), updatedAt);
            row.addValue(keys.get(CALENDAR_EVENT_STARTS_AT), startsAt);
            row.addValue(keys.get(CALENDAR_EVENT_ENDS_AT), endsAt);
            row.addValue(keys.get(CALENDAR_EVENT_PROJECT_ID), projectID);
            row.addValue(keys.get(CALENDAR_EVENT_URL), url);
            row.addValue(keys.get(CALENDAR_EVENT_COUNT), 1);
        }
    }
}