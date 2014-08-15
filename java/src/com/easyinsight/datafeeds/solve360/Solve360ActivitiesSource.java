package com.easyinsight.datafeeds.solve360;

import com.easyinsight.analysis.*;
import com.easyinsight.api.v3.MeasureFormattingType;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.storage.IWhere;
import com.easyinsight.storage.StringWhere;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 11/1/11
 * Time: 4:26 PM
 */
public class Solve360ActivitiesSource extends Solve360BaseSource {

    public static final String ID = "Activity ID";
    private static final String TYPE = "Activity Type";
    private static final String ASSIGNED_TO = "Assigned To";
    private static final String COMMENTS = "Comments";
    private static final String DURATION = "Duration";
    private static final String LOCATION = "Location";
    private static final String DATE_OCCURRED = "Date Occurred";
    private static final String TIME_START = "Start Time";
    private static final String TIME_END = "End Time";
    private static final String EVENT_TYPE = "Event Type";
    private static final String REMIND_STATUS = "Reminder Status";
    private static final String PRIORITY = "Priority";
    private static final String TITLE = "Title";
    private static final String COMPLETED = "Completed";
    private static final String REPEAT_INTERVAL = "Repeat Interval";
    public static final String PARENT_COMPANY = "Parent Company";
    public static final String PARENT_CONTACT = "Parent Contact";
    private static final String DETAILS = "Details";
    private static final String COUNT = "Activity Count";

    public Solve360ActivitiesSource() {
        setFeedName("Activities");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.SOLVE360_ACTIVITIES;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ID, TYPE, COMMENTS, DURATION, LOCATION, DATE_OCCURRED, TIME_START, TIME_END, EVENT_TYPE, REMIND_STATUS, PRIORITY, TITLE, COMPLETED, REPEAT_INTERVAL, PARENT_COMPANY, PARENT_CONTACT, DETAILS);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(ID)));
        analysisItems.add(new AnalysisDimension(keys.get(TYPE)));
        analysisItems.add(new AnalysisDimension(keys.get(COMMENTS)));
        analysisItems.add(new AnalysisMeasure(keys.get(DURATION), DURATION, AggregationTypes.SUM, true, FormattingConfiguration.MILLISECONDS));
        analysisItems.add(new AnalysisDimension(keys.get(LOCATION)));
        analysisItems.add(new AnalysisDateDimension(keys.get(DATE_OCCURRED), true, AnalysisDateDimension.MINUTE_LEVEL));
        analysisItems.add(new AnalysisDateDimension(keys.get(TIME_START), true, AnalysisDateDimension.MINUTE_LEVEL));
        analysisItems.add(new AnalysisDateDimension(keys.get(TIME_END), true, AnalysisDateDimension.MINUTE_LEVEL));
        analysisItems.add(new AnalysisDimension(keys.get(EVENT_TYPE)));
        analysisItems.add(new AnalysisDimension(keys.get(REMIND_STATUS)));
        analysisItems.add(new AnalysisDimension(keys.get(PRIORITY)));
        analysisItems.add(new AnalysisDimension(keys.get(TITLE)));
        analysisItems.add(new AnalysisDimension(keys.get(COMPLETED)));
        analysisItems.add(new AnalysisDimension(keys.get(REPEAT_INTERVAL)));
        analysisItems.add(new AnalysisDimension(keys.get(PARENT_COMPANY)));
        analysisItems.add(new AnalysisDimension(keys.get(PARENT_CONTACT)));
        analysisItems.add(new AnalysisText(keys.get(DETAILS)));
        Key countKey = keys.get(COUNT);
        if (countKey == null) {
            countKey = new NamedKey(COUNT);
        }
        Key assignedToKey = keys.get(ASSIGNED_TO);
        if (assignedToKey == null) {
            assignedToKey = new NamedKey(ASSIGNED_TO);
        }
        analysisItems.add(new AnalysisDimension(assignedToKey));
        analysisItems.add(new AnalysisMeasure(countKey, AggregationTypes.SUM));
        return analysisItems;
    }

    @Override
    protected String getUpdateKeyName() {
        return ID;
    }

    @Override
    protected boolean clearsData(FeedDefinition parentSource) {
        return false;
    }

    // completed, responsible party

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage dataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        Solve360CompositeSource solve360CompositeSource = (Solve360CompositeSource) parentDefinition;
        HttpClient httpClient = getHttpClient(solve360CompositeSource.getUserEmail(), solve360CompositeSource.getAuthKey());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String startDate = "2011-01-01";
            if(lastRefreshDate != null) {
                startDate = df2.format(lastRefreshDate);
            }
            Document doc = runRestRequest("https://secure.solve360.com/report/activities/?start=" + startDate + "&end=" + df2.format(new Date()) + "&last=created&types=3,4,6,14,23,24,73,88", httpClient, new Builder(), solve360CompositeSource);            DataSet dataSet = new DataSet();
            Nodes oppNodes = doc.query("/response/activities/activity");
            for (int i = 0; i < oppNodes.size(); i++) {
                Node activityNode = oppNodes.get(i);
                System.out.println(activityNode.toXML());
                IRow row = dataSet.createRow();
                String id =  queryField(activityNode, "id/text()");
                row.addValue(keys.get(ID), id);
                String type = queryField(activityNode, "typeid/text()");
                if ("3".equals(type)) {
                    type = "Note";
                } else if ("4".equals(type)) {
                    type = "Event";
                } else if ("6".equals(type)) {
                    type = "Followup";
                } else if ("14".equals(type)) {
                    type = "Task";
                } else if ("23".equals(type)) {
                    type = "File";
                } else if ("24".equals(type)) {
                    type = "Photo";
                } else if ("32".equals(type)) {
                    type = "Opportunity";
                } else if ("61".equals(type)) {
                    type = "Event (non-linked)";
                } else if ("73".equals(type)) {
                    type = "Call Log";
                } else if ("88".equals(type)) {
                    type = "Scheduled Email";
                }
                row.addValue(keys.get(TYPE), type);
                row.addValue(keys.get(ASSIGNED_TO), queryField(activityNode, "fields/assignedto/@cn"));
                row.addValue(keys.get(COMMENTS), queryField(activityNode, "comments/text()"));
                String durationStr = queryField(activityNode, "fields/duration/text()");
                if(durationStr != null && !"?".equals(durationStr))
                    row.addValue(keys.get(DURATION), Integer.parseInt(durationStr) * 60 * 1000);
                row.addValue(keys.get(LOCATION), queryField(activityNode, "fields/location/text()"));

                row.addValue(keys.get(DETAILS), queryField(activityNode, "fields/details/text()"));
                String dateOccured = queryField(activityNode, "fields/dateoccured/text()");
                if(dateOccured != null) {
                    row.addValue(keys.get(DATE_OCCURRED), df.parse(dateOccured));
                }

                String parentType = queryField(activityNode, "parenttypeid/text()");
                if("40".equals(parentType)) {
                    row.addValue(keys.get(PARENT_COMPANY), queryField(activityNode, "parent/text()"));
                } else if("1".equals(parentType)) {
                    row.addValue(keys.get(PARENT_CONTACT), queryField(activityNode, "parent/text()"));
                }

                String startTime = queryField(activityNode, "fields/timestart/text()");
                if(startTime != null)
                    row.addValue(keys.get(TIME_START), df.parse(startTime));

                String endTime = queryField(activityNode, "fields/timeend/text()");
                if(endTime != null)
                    row.addValue(keys.get(TIME_END), df.parse(endTime));
                row.addValue(keys.get(EVENT_TYPE), queryField(activityNode, "fields/eventtype/text()"));
                row.addValue(keys.get(REMIND_STATUS), queryField(activityNode, "fields/remindstatus/text()"));
                row.addValue(keys.get(PRIORITY), queryField(activityNode, "fields/priority/text()"));
                row.addValue(keys.get(TITLE), queryField(activityNode, "fields/title/text()"));
                String completed = queryField(activityNode, "fields/completed/text()");
                if (completed == null || "".equals(completed) || "0".equals(completed)) {
                    completed = "Not Completed";
                } else {
                    completed = "Completed";
                }
                row.addValue(keys.get(COMPLETED), completed);
                row.addValue(keys.get(REPEAT_INTERVAL), queryField(activityNode, "fields/repeatinterval/text()"));
                row.addValue(keys.get(COUNT), 1);
                if(lastRefreshDate != null) {
                    StringWhere userWhere = new StringWhere(keys.get(ID), id);
                    dataStorage.updateData(dataSet, Arrays.asList((IWhere) userWhere));
                    dataSet = new DataSet();
                }
            }
            if(lastRefreshDate == null) {
                dataStorage.insertData(dataSet);
            }
            return null;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
