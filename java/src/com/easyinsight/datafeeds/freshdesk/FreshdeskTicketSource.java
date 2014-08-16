package com.easyinsight.datafeeds.freshdesk;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 1/10/14
 * Time: 11:45 AM
 */
public class FreshdeskTicketSource extends FreshdeskBaseSource {

    public static final String ID = "ID";
    public static final String DISPLAY_ID = "Display ID";
    public static final String DESCRIPTION = "Description";
    public static final String REQUESTER_NAME ="Requester";
    public static final String DUE_BY = "Due By";
    public static final String STATUS = "Status";
    public static final String PRIORITY = "Priority";
    public static final String CREATED_AT = "Created At";
    public static final String RESOLVED_AT = "Resolved At";
    public static final String SOURCE_NAME = "Source Name";
    public static final String TRAINED = "Trained";
    public static final String TICKET_TYPE = "Ticket Type";
    public static final String URGENT = "Urgent";
    public static final String GROUP_ID = "Group ID";
    public static final String REQUESTER_STATUS = "Requester Status";
    public static final String DELETED = "Deleted";
    public static final String ESCALATED = "Escalated";
    public static final String UPDATED_AT = "Updated At";
    public static final String OWNER_ID = "Owner ID";
    public static final String RESPONDER_NAME = "Responder Name";
    public static final String SPAM = "Spam";
    public static final String COUNT = "Ticket Count";
    public static final String TICKET_URL = "Ticket URL";
    public static final String REOPEN_COUNT = "Times Reopened";



    public FreshdeskTicketSource() {
        setFeedName("Tickets");
    }

    private transient List<String> customFields;

    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(ID, new AnalysisDimension());
        fieldBuilder.addField(DISPLAY_ID, new AnalysisDimension());
        fieldBuilder.addField(TICKET_URL, new AnalysisDimension());
        fieldBuilder.addField(DESCRIPTION, new AnalysisText());
        fieldBuilder.addField(STATUS, new AnalysisDimension());
        fieldBuilder.addField(PRIORITY, new AnalysisDimension());
        fieldBuilder.addField(TRAINED, new AnalysisDimension());
        fieldBuilder.addField(TICKET_TYPE, new AnalysisDimension());
        fieldBuilder.addField(URGENT, new AnalysisDimension());
        fieldBuilder.addField(GROUP_ID, new AnalysisDimension());
        fieldBuilder.addField(REQUESTER_NAME, new AnalysisDimension());
        fieldBuilder.addField(REQUESTER_STATUS, new AnalysisDimension());
        fieldBuilder.addField(DELETED, new AnalysisDimension());
        fieldBuilder.addField(ESCALATED, new AnalysisDimension());
        fieldBuilder.addField(OWNER_ID, new AnalysisDimension());
        fieldBuilder.addField(SPAM, new AnalysisDimension());
        fieldBuilder.addField(RESPONDER_NAME, new AnalysisDimension());
        fieldBuilder.addField(DUE_BY, new AnalysisDateDimension());
        fieldBuilder.addField(CREATED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(UPDATED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(RESOLVED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(COUNT, new AnalysisMeasure());
        fieldBuilder.addField(REOPEN_COUNT, new AnalysisMeasure());

        customFields = new ArrayList<>();

        FreshdeskCompositeSource freshdeskCompositeSource = (FreshdeskCompositeSource) parentDefinition;
        HttpClient client = getHttpClient(freshdeskCompositeSource.getFreshdeskApiKey());
        List<Map> ticketFields = runRestRequestForListNoHelp("ticket_fields.json", client, freshdeskCompositeSource);
        for (Map ticketField : ticketFields) {
            Map ticketFieldData = (Map) ticketField.get("ticket_field");
            String fieldType = ticketFieldData.get("field_type").toString();
            String label = ticketFieldData.get("label").toString();
            String name = ticketFieldData.get("name").toString();
            if (fieldType.startsWith("custom")) {
                if ("custom_paragraph".equals(fieldType)) {
                    AnalysisText text = new AnalysisText();
                    text.setDisplayName(label);
                    fieldBuilder.addField(name, text);
                } else if ("custom_number".equals(fieldType)) {
                    fieldBuilder.addField(name, new AnalysisMeasure(label));
                } else {
                    fieldBuilder.addField(name, new AnalysisDimension(label));
                }
                customFields.add(name);
            }
        }
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        FreshdeskCompositeSource freshdeskCompositeSource = (FreshdeskCompositeSource) parentDefinition;
        DataSet dataSet = new DataSet();
        HttpClient client = getHttpClient(freshdeskCompositeSource.getFreshdeskApiKey());
        int ctr;
        int page = 1;
        List<String> ticketIDs = new ArrayList<>();
        Map<String, List<Map>> statusUpdates = new HashMap<>();
        Map<String, List<Map>> assignmentUpdates = new HashMap<>();
        Map<String, List<Map>> addedNotes = new HashMap<>();
        do {
            ctr = 0;
            List responseList;
            if (page == 1) {
                responseList = runRestRequestForList("tickets.json?filter_name=all_tickets", client, freshdeskCompositeSource);
            } else {
                responseList = runRestRequestForList("tickets.json?filter_name=all_tickets&page=" + page, client, freshdeskCompositeSource);
            }
            for (Object obj : responseList) {
                ctr++;
                Map map = (Map) obj;
                String id = map.get("id").toString();
                String displayID = getJSONValue(map, "display_id");
                List<Map> activities = runRestRequestForList("tickets/activities/" + displayID + ".json", client, freshdeskCompositeSource);


                IRow row = dataSet.createRow();
                Date updatedAt = createTicket(keys, map, id, row, freshdeskCompositeSource);
                //if (lastRefreshDate == null || lastRefreshDate.before(updatedAt)) {
                    ticketIDs.add(displayID);
                    List<Map> statusUpdateList = new LinkedList<>();
                    List<Map> assignmentUpdateList = new LinkedList<>();
                    List<Map> noteList = new LinkedList<>();
                    for (Map activity : activities) {
                        Map ticketActivity = (Map) activity.get("ticket_activity");
                        //IRow row = dataSet.createRow();
                        Object activityObj = ticketActivity.get("activity");
                        String activityBody;
                        if (activityObj instanceof String) {
                            activityBody = activityObj.toString();
                        } else if (activityObj instanceof List) {
                            List list = (List) activityObj;
                            activityBody = list.get(list.size() - 1).toString();
                        } else {
                            throw new RuntimeException();
                        }
                        if (activityBody.startsWith("updated status to ")) {
                            statusUpdateList.add(activity);
                        } else if (activityBody.startsWith("assigned to ")) {
                            assignmentUpdateList.add(activity);
                        } else if (activityBody.startsWith("Added a note")) {
                            noteList.add(activity);
                        }

                    }
                    statusUpdates.put(displayID, statusUpdateList);
                    assignmentUpdates.put(displayID, assignmentUpdateList);
                    addedNotes.put(displayID, noteList);

                boolean closed = false;
                int reopenCount = 0;
                System.out.println("new ticket");
                Value resolvedAt = null;
                for (Object activityObject : statusUpdateList) {
                    // calculate # of times issues was reopened
                    Map m = (Map) activityObject;
                    Map ticketActivityMap = (Map) m.get("ticket_activity");
                    System.out.println(m);
                    List<String> list = (List<String>) ticketActivityMap.get("activity");
                    for (String activityBody : list) {
                        System.out.println("\tbody = " + activityBody);
                        int index = activityBody.lastIndexOf(" ");
                        String status = activityBody.substring(index).trim();
                        System.out.println("\tstatus = " + status);
                        if ("Resolved".equals(status) || "Closed".equals(status)) {
                            System.out.println("Setting closed ");
                            if (!closed) {
                                resolvedAt = getDate(ticketActivityMap, "performed_time");
                            }
                            closed = true;
                        } else {
                            resolvedAt = new EmptyValue();
                            if (closed) {
                                System.out.println("Incrementing reopen count");
                                reopenCount++;
                            }
                            System.out.println("Marking unclosed");
                            closed = false;
                        }
                    }
                }
                row.addValue(keys.get(REOPEN_COUNT), reopenCount);
                row.addValue(keys.get(RESOLVED_AT), resolvedAt);
                //}
            }
            page++;
        } while (ctr == 30);
        freshdeskCompositeSource.setAssignmentUpdates(assignmentUpdates);
        freshdeskCompositeSource.setStatusUpdates(statusUpdates);
        freshdeskCompositeSource.setNotes(addedNotes);
        freshdeskCompositeSource.setTicketIDs(ticketIDs);
        return dataSet;
    }

    private Date createTicket(Map<String, Key> keys, Map map, String id, IRow row, FreshdeskCompositeSource freshdeskCompositeSource) {
        row.addValue(keys.get(ID), id);

        row.addValue(keys.get(DISPLAY_ID), getJSONValue(map, "display_id"));
        row.addValue(keys.get(DESCRIPTION), getJSONValue(map, "description"));
        row.addValue(keys.get(REQUESTER_NAME), getJSONValue(map, "requester_name"));
        row.addValue(keys.get(DUE_BY), getDate(map, "due_by"));
        row.addValue(keys.get(STATUS), getJSONValue(map, "status_name"));
        row.addValue(keys.get(PRIORITY), getJSONValue(map, "priority_name"));
        row.addValue(keys.get(CREATED_AT), getDate(map, "created_at"));
        DateValue updatedAtValue = (DateValue) getDate(map, "updated_at");
        Date updatedAt = updatedAtValue.getDate();
        row.addValue(keys.get(UPDATED_AT), updatedAtValue);
        row.addValue(keys.get(SOURCE_NAME), getJSONValue(map, "source_name"));
        row.addValue(keys.get(TRAINED), getJSONValue(map, "trained"));
        row.addValue(keys.get(TICKET_TYPE), getJSONValue(map, "ticket_type"));
        row.addValue(keys.get(GROUP_ID), getJSONValue(map, "group_id"));
        row.addValue(keys.get(REQUESTER_STATUS), getJSONValue(map, "requester_status_name"));
        row.addValue(keys.get(DELETED), getJSONValue(map, "deleted"));
        row.addValue(keys.get(OWNER_ID), getJSONValue(map, "owner_id"));
        row.addValue(keys.get(RESPONDER_NAME), getJSONValue(map, "responder_name"));
        row.addValue(keys.get(SPAM), getValue(map, "spam"));
        row.addValue(keys.get(TICKET_URL), freshdeskCompositeSource.getUrl() + "/helpdesk/tickets/" + getJSONValue(map, "display_id"));
        row.addValue(keys.get(COUNT), 1);
        Map customFieldMap = (Map) map.get("custom_field");
        if (customFieldMap != null) {
            for (String customField : customFields) {
                row.addValue(keys.get(customField), getJSONValue(customFieldMap, customField));
            }
        }
        return updatedAt;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHDESK_TICKET;
    }
}
