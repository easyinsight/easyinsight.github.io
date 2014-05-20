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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: jamesboe
 * Date: Nov 7, 2010
 * Time: 10:56:52 AM
 */
public class CCEventRegistrantSource extends ConstantContactBaseSource {

    public static final String REGISTRANT_ATTENDANCE_STATUS = "Registrant Attendance Status";
    public static final String REGISTRANT_EVENT_ID = "Registrant Event ID";
    public static final String REGISTRANT_EMAIL = "Registrant Email";
    public static final String REGISTRANT_FIRST_NAME = "Registrant First Name";
    public static final String REGISTRANT_GUEST_COUNT = "Registrant Guest Count";
    public static final String REGISTRANT_ID = "Registrant ID";
    public static final String REGISTRANT_LAST_NAME = "Registrant Last Name";
    public static final String REGISTRANT_PAYMENT_STATUS = "Registrant Payment Status";
    public static final String REGISTRANT_REGISTRATION_DATE = "Registration Date";
    public static final String REGISTRANT_REGISTRATION_STATUS = "Registration Status";
    public static final String REGISTRANT_TICKET_ID = "Registrant Ticket ID";
    public static final String REGISTRANT_UPDATED_DATE = "Registrant Updated Date";

    public CCEventRegistrantSource() {
        setFeedName("Event Registrant");
    }

    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(REGISTRANT_ID, new AnalysisDimension());
        fieldBuilder.addField(REGISTRANT_ATTENDANCE_STATUS, new AnalysisDimension());
        fieldBuilder.addField(REGISTRANT_EMAIL, new AnalysisDimension());
        fieldBuilder.addField(REGISTRANT_PAYMENT_STATUS, new AnalysisDimension());
        fieldBuilder.addField(REGISTRANT_EVENT_ID, new AnalysisDimension());
        fieldBuilder.addField(REGISTRANT_FIRST_NAME, new AnalysisDimension());
        fieldBuilder.addField(REGISTRANT_LAST_NAME, new AnalysisDimension());
        fieldBuilder.addField(REGISTRANT_TICKET_ID, new AnalysisDimension());
        fieldBuilder.addField(REGISTRANT_REGISTRATION_DATE, new AnalysisDateDimension());
        fieldBuilder.addField(REGISTRANT_REGISTRATION_STATUS, new AnalysisDimension());
        fieldBuilder.addField(REGISTRANT_UPDATED_DATE, new AnalysisDateDimension());
        fieldBuilder.addField(REGISTRANT_GUEST_COUNT, new AnalysisMeasure());
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.CC_EVENT_REGISTRANTS;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            DataSet dataSet = new DataSet();
            ConstantContactCompositeSource ccSource = (ConstantContactCompositeSource) parentDefinition;
            HttpClient client = new HttpClient();
            Set<String> eventIDs = ccSource.getEventIDs();
            int count = 0;
            for (String eventID : eventIDs) {
                Map result = query("https://api.constantcontact.com/v2/eventspot/events/"+eventID+"/registrants?limit=500&api_key=" + ConstantContactCompositeSource.KEY, ccSource, client);
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
                        count++;
                        Map node = (Map) obj;
                        IRow row = dataSet.createRow();
                        row.addValue(keys.get(REGISTRANT_ATTENDANCE_STATUS), queryField(node, "attendance_status"));
                        row.addValue(keys.get(REGISTRANT_EMAIL), queryField(node, "email"));
                        row.addValue(keys.get(REGISTRANT_EVENT_ID), eventID);
                        row.addValue(keys.get(REGISTRANT_FIRST_NAME), queryField(node, "first_name"));
                        row.addValue(keys.get(REGISTRANT_LAST_NAME), queryField(node, "last_name"));
                        row.addValue(keys.get(REGISTRANT_GUEST_COUNT), queryField(node, "guest_count"));
                        row.addValue(keys.get(REGISTRANT_ID), queryField(node, "id"));
                        row.addValue(keys.get(REGISTRANT_PAYMENT_STATUS), queryField(node, "payment_status"));
                        row.addValue(keys.get(REGISTRANT_REGISTRATION_DATE), queryDate(node, "registration_date"));
                        row.addValue(keys.get(REGISTRANT_TICKET_ID), queryField(node, "ticket_id"));
                        row.addValue(keys.get(REGISTRANT_REGISTRATION_STATUS), queryField(node, "registration_status"));
                        row.addValue(keys.get(REGISTRANT_UPDATED_DATE), queryDate(node, "updated_date"));
                        if (count == 200) {
                            count = 0;
                            IDataStorage.insertData(dataSet);
                            dataSet = new DataSet();
                        }
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
            }
            IDataStorage.insertData(dataSet);
            return null;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
