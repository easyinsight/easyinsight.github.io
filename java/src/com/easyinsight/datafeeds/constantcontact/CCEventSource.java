package com.easyinsight.datafeeds.constantcontact;

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
 * Date: Nov 7, 2010
 * Time: 10:56:52 AM
 */
public class CCEventSource extends ConstantContactBaseSource {

    public static final String EVENT_ID = "Event ID";
    public static final String EVENT_CREATED = "Event Created At";
    public static final String EVENT_END_DATE = "Event End Date";
    public static final String EVENT_DESCRIPTION = "Event Description";
    public static final String EVENT_LOCATION = "Event Location";
    public static final String EVENT_REGISTRATION_URL = "Event Registration URL";
    public static final String EVENT_STATUS = "Event Status";
    public static final String EVENT_START_DATE = "Event Start Date";
    public static final String EVENT_TITLE = "Event Title";
    public static final String EVENT_REGISTERED_COUNT = "Event Registered Count";
    public static final String EVENT_TYPE = "Event Type";
    public static final String EVENT_COUNT = "Event Count";

    public CCEventSource() {
        setFeedName("Event");
    }

    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(EVENT_ID, new AnalysisDimension());
        fieldBuilder.addField(EVENT_DESCRIPTION, new AnalysisDimension());
        fieldBuilder.addField(EVENT_LOCATION, new AnalysisDimension());
        fieldBuilder.addField(EVENT_REGISTRATION_URL, new AnalysisDimension());
        fieldBuilder.addField(EVENT_STATUS, new AnalysisDimension());
        fieldBuilder.addField(EVENT_TITLE, new AnalysisDimension());
        fieldBuilder.addField(EVENT_TYPE, new AnalysisDimension());
        fieldBuilder.addField(EVENT_CREATED, new AnalysisDateDimension());
        fieldBuilder.addField(EVENT_END_DATE, new AnalysisDateDimension());
        fieldBuilder.addField(EVENT_START_DATE, new AnalysisDateDimension());
        fieldBuilder.addField(EVENT_COUNT, new AnalysisMeasure());
        fieldBuilder.addField(EVENT_REGISTERED_COUNT, new AnalysisMeasure());
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.CC_EVENT;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            Set<String> ids = new HashSet<String>();
            DataSet dataSet = new DataSet();
            ConstantContactCompositeSource ccSource = (ConstantContactCompositeSource) parentDefinition;
            HttpClient client = new HttpClient();
            Map result = query("https://api.constantcontact.com/v2/eventspot/events?limit=500&api_key=" + ConstantContactCompositeSource.KEY, ccSource, client);
            Map meta = (Map) result.get("meta");
            String nextLink = null;
            if (meta != null) {
                Map pagination = (Map) meta.get("pagination");
                if (pagination != null) {
                    Object nextLinkObject = pagination.get("next_link");
                    if (nextLinkObject != null) {
                        nextLink = "https://api.constantcontact.com" + nextLinkObject.toString() + "&api_key=" + ConstantContactCompositeSource.KEY;
                    }
                }
            }

            boolean hasMoreData;
            do {
                List results = (List) result.get("results");
                hasMoreData = false;
                for (Object obj : results) {
                    Map node = (Map) obj;
                    IRow row = dataSet.createRow();
                    row.addValue(keys.get(EVENT_ID), node.get("id").toString());
                    ids.add(node.get("id").toString());
                    row.addValue(keys.get(EVENT_TITLE), node.get("title").toString());
                    row.addValue(keys.get(EVENT_DESCRIPTION), node.get("description").toString());
                    row.addValue(keys.get(EVENT_LOCATION), node.get("location").toString());
                    row.addValue(keys.get(EVENT_REGISTRATION_URL), queryField(node, "registration_url"));
                    row.addValue(keys.get(EVENT_STATUS), queryField(node, "status"));
                    row.addValue(keys.get(EVENT_START_DATE), queryDate(node, "start_date"));
                    row.addValue(keys.get(EVENT_END_DATE), queryDate(node, "end_date"));
                    row.addValue(keys.get(EVENT_CREATED), queryDate(node, "created_date"));
                    row.addValue(keys.get(EVENT_REGISTERED_COUNT), queryField(node, "registration_count"));
                    row.addValue(keys.get(EVENT_COUNT), 1);
                }
                if (nextLink != null) {
                    result = query(nextLink, ccSource, client);
                    meta = (Map) result.get("meta");
                    nextLink = null;
                    if (meta != null) {
                        Map pagination = (Map) meta.get("pagination");
                        if (pagination != null) {
                            Object nextLinkObject = pagination.get("next_link");
                            if (nextLinkObject != null) {
                                nextLink = "https://api.constantcontact.com" + nextLinkObject.toString() + "&api_key=" + ConstantContactCompositeSource.KEY;
                            }
                        }
                    }
                    hasMoreData = true;
                }
            } while (hasMoreData);
            ccSource.setEventIDs(ids);
            return dataSet;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
