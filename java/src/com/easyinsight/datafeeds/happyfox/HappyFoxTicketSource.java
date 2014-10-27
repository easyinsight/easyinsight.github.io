package com.easyinsight.datafeeds.happyfox;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.freshdesk.TicketAnalysis;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.storage.StringWhere;
import org.apache.commons.httpclient.HttpClient;

import java.net.URLEncoder;
import java.sql.Connection;
import java.time.ZoneId;
import java.util.*;

/**
 * User: jamesboe
 * Date: 8/15/14
 * Time: 11:27 AM
 */
public class HappyFoxTicketSource extends HappyFoxBaseSource {

    public static final String TICKET_ID = "Ticket ID";
    public static final String TICKET_COUNT = "Ticket Count";
    public static final String CREATED_AT = "Ticket Created At";
    public static final String UPDATED_AT = "Ticket Updated At";
    public static final String LAST_REPLY_AT = "Last Reply At";
    public static final String ASSIGNED_TO = "Assigned To";
    public static final String DUE_DATE = "Due Date";
    public static final String STATUS = "Status";
    public static final String PRIORITY = "Priority";
    public static final String CATEGORY = "Category";
    public static final String TIME_SPENT = "Time Spent";
    public static final String SUBJECT = "Subject";
    public static final String TICKET_CREATED_BY = "Ticket Created By";
    public static final String TICKET_CREATED_BY_ID = "Ticket Created By ID";
    public static final String RESOLVED_AT = "Ticket Resolved At";
    public static final String AGENT_TOUCHES = "Agent Touches";
    public static final String CUSTOMER_TOUCHES = "Customer Touches";
    public static final String AGENT_TIME = "Agent Time";
    public static final String CUSTOMER_TIME = "Customer Time";
    public static final String TICKET_WITH = "Ticket With";

    public HappyFoxTicketSource() {
        setFeedName("Tickets");
    }

    private transient Map<String, AnalysisItem> customFields;

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(TICKET_ID, new AnalysisDimension());
        fieldBuilder.addField(ASSIGNED_TO, new AnalysisDimension());
        fieldBuilder.addField(STATUS, new AnalysisDimension());
        fieldBuilder.addField(PRIORITY, new AnalysisDimension());
        fieldBuilder.addField(CATEGORY, new AnalysisDimension());
        fieldBuilder.addField(SUBJECT, new AnalysisDimension());
        fieldBuilder.addField(DUE_DATE, new AnalysisDateDimension());
        fieldBuilder.addField(LAST_REPLY_AT, new AnalysisDateDimension());
        fieldBuilder.addField(CREATED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(UPDATED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(RESOLVED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(TICKET_COUNT, new AnalysisMeasure());
        fieldBuilder.addField(TIME_SPENT, new AnalysisMeasure());
        fieldBuilder.addField(TICKET_CREATED_BY, new AnalysisDimension());
        fieldBuilder.addField(TICKET_CREATED_BY_ID, new AnalysisDimension());
        fieldBuilder.addField(TICKET_WITH, new AnalysisDimension());
        fieldBuilder.addField(AGENT_TOUCHES, new AnalysisMeasure());
        fieldBuilder.addField(CUSTOMER_TOUCHES, new AnalysisMeasure());
        fieldBuilder.addField(AGENT_TIME, new AnalysisMeasure(FormattingConfiguration.MILLISECONDS));
        fieldBuilder.addField(CUSTOMER_TIME, new AnalysisMeasure(FormattingConfiguration.MILLISECONDS));
        HappyFoxCompositeSource happyFoxCompositeSource = (HappyFoxCompositeSource) parentDefinition;
        HttpClient client = getHttpClient(happyFoxCompositeSource.getHfApiKey(), happyFoxCompositeSource.getAuthKey());
        List<Map> userCustomFields = runRestRequestForList("ticket_custom_fields/", client, happyFoxCompositeSource);
        customFields = new HashMap<>();
        for (Map userCustomField : userCustomFields) {
            String name = userCustomField.get("name").toString();
            String customFieldID = userCustomField.get("id").toString();
            String type = userCustomField.get("type").toString();
            if ("text".equals(type) || "multiple_choice".equals(type)) {
                AnalysisDimension customField = new AnalysisDimension(name);
                fieldBuilder.addField(customFieldID, customField);
                customFields.put(customFieldID, customField);
            }
        }
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            //DataSet dataSet = new DataSet();



            HappyFoxCompositeSource happyFoxCompositeSource = (HappyFoxCompositeSource) parentDefinition;
            HttpClient client = getHttpClient(happyFoxCompositeSource.getHfApiKey(), happyFoxCompositeSource.getAuthKey());

            int page = 1;
            if (lastRefreshDate == null) {
                // new refresh
            }
            int pageCount;
            do {
                Map response;
                if (page == 1) {
                    response = runRestRequestForMap("tickets/?size=50&show_updates=0", client, happyFoxCompositeSource);
                } else {
                    response = runRestRequestForMap("tickets/?size=50&page=" + page+"&show_updates=0", client, happyFoxCompositeSource);
                }

                Map pageInfo = (Map) response.get("page_info");
                pageCount = (int) pageInfo.get("page_count");
                loadingProgress(page, pageCount, "Retrieving page " + page + " of " + pageCount + " pages of tickets from HappyFox", callDataID);
                page++;
                List<Map> data = (List<Map>) response.get("data");
                Set<String> postUpdate = new HashSet<>();
                DataSet fullSet = new DataSet();
                Map<String, DataSet> map = new HashMap<>();
                Map<String, IRow> rowMap = new HashMap<>();

                for (Map ticket : data) {
                    String ticketID = getJSONValue(ticket, "id");
                    IRow row;

                    if (lastRefreshDate == null) {
                        row = fullSet.createRow();
                    } else {
                        DataSet dataSet = new DataSet();
                        row = dataSet.createRow();
                        map.put(ticketID, dataSet);
                    }


                    String categoryName = ((Map) ticket.get("category")).get("name").toString();
                    row.addValue(keys.get(CATEGORY), categoryName);
                    String status = ((Map) ticket.get("status")).get("name").toString();
                    row.addValue(keys.get(STATUS), status);

                    row.addValue(keys.get(TIME_SPENT), getJSONValue(ticket, "time_spent"));
                    row.addValue(keys.get(LAST_REPLY_AT), getDate(ticket, "last_user_reply_at"));
                    Value updatedAtValue = getDate(ticket, "last_updated_at");
                    Date updateDate = ((DateValue) updatedAtValue).getDate();

                    row.addValue(keys.get(UPDATED_AT), updatedAtValue);

                    row.addValue(keys.get(CREATED_AT), getDate(ticket, "created_at"));
                    row.addValue(keys.get(DUE_DATE), getDate(ticket, "due_date"));
                    row.addValue(keys.get(PRIORITY), ((Map) ticket.get("priority")).get("name").toString());
                    Map user = (Map) ticket.get("user");
                    if (user != null) {
                        row.addValue(keys.get(TICKET_CREATED_BY), getValue(user, "name"));
                        row.addValue(keys.get(TICKET_CREATED_BY_ID), getValue(user, "id"));
                    }
                    Map assignedToID = (Map) ticket.get("assigned_to");
                    if (assignedToID != null) {
                        String email = getJSONValue(assignedToID, "name");
                        row.addValue(keys.get(ASSIGNED_TO), email);
                    }


                    row.addValue(keys.get(TICKET_ID), ticketID);
                    //if (lastRefreshDate != null && lastRefreshDate.before(updateDate)) {
                        postUpdate.add(ticketID);
                    //}

                    row.addValue(keys.get(SUBJECT), getJSONValue(ticket, "subject"));
                    row.addValue(keys.get(TICKET_COUNT), 1);
                    List<Map> customFields = (List<Map>) ticket.get("custom_fields");
                    for (Map customField : customFields) {
                        String customFieldID = customField.get("id").toString();
                        Object val = customField.get("value");
                        if (val != null) {
                            String customFieldValue = val.toString();
                            if (this.customFields.containsKey(customFieldID)) {
                                row.addValue(keys.get(customFieldID), customFieldValue);
                            }
                        }
                    }

                    rowMap.put(ticketID, row);
                }

                if (postUpdate.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (String ticketID : postUpdate) {
                        sb.append(ticketID).append(",");
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    //Map details = runRestRequestForMap("tickets/?q=id:" + sb.toString(), client, happyFoxCompositeSource);
                    //List<Map> detailData = (List<Map>) details.get("data");
                    for (Map ticket : data) {
                        String ticketID = ticket.get("id").toString();
                        IRow row = rowMap.get(ticketID);
                        DateValue createdAt = (DateValue) row.getValue(keys.get(CREATED_AT));
                        TicketAnalysis ticketAnalysis = new TicketAnalysis(createdAt.getDate().toInstant().atZone(ZoneId.systemDefault()));
                        List<Map> updates = (List<Map>) ticket.get("updates");
                        boolean closed = false;
                        Value resolvedAt = null;
                        if (updates != null) {
                            for (Map update : updates) {
                                DateValue dv = (DateValue) getDate(update, "timestamp");
                                Date date = dv.getDate();
                                if (update.get("status_change") != null) {
                                    Map sc = (Map) update.get("status_change");
                                    String status = sc.get("new_name").toString();
                                    if ("Solved".equals(status) || "Closed".equals(status)) {
                                        ticketAnalysis.addResponsibility(TicketAnalysis.SOLVED, date);
                                        if (!closed) {
                                            resolvedAt = dv;
                                        }
                                        closed = true;
                                    } else if ("On Hold".equals(status)) {
                                        ticketAnalysis.addResponsibility(TicketAnalysis.CUSTOMER, date);
                                    } else {
                                        ticketAnalysis.addResponsibility(TicketAnalysis.AGENT, date);
                                        resolvedAt = new EmptyValue();
                                        closed = false;
                                    }
                                }
                            }
                            ticketAnalysis.calculate();
                            row.addValue(keys.get(AGENT_TIME), ticketAnalysis.getElapsedAgentTime());
                            row.addValue(keys.get(CUSTOMER_TIME), ticketAnalysis.getElapsedCustomerTime());
                            row.addValue(keys.get(AGENT_TOUCHES), ticketAnalysis.getAgentHandles());
                            row.addValue(keys.get(CUSTOMER_TOUCHES), ticketAnalysis.getCustomerHandles());
                            if (ticketAnalysis.getWaitState() == TicketAnalysis.AGENT) {
                                row.addValue(keys.get(TICKET_WITH), "On Agent");
                            } else if (ticketAnalysis.getWaitState() == TicketAnalysis.CUSTOMER) {
                                row.addValue(keys.get(TICKET_WITH), "On Customer");
                            } else if (ticketAnalysis.getWaitState() == TicketAnalysis.UNASSIGNED) {
                                row.addValue(keys.get(TICKET_WITH), "Unassigned");
                            } else if (ticketAnalysis.getWaitState() == TicketAnalysis.SOLVED) {
                                row.addValue(keys.get(TICKET_WITH), "Solved");
                            }
                            row.addValue(keys.get(RESOLVED_AT), resolvedAt);
                        }
                    }
                }

                IDataStorage.insertData(fullSet);
            } while (page <= pageCount);
            return null;
        } catch (ReportException e) {
            throw e;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HAPPYFOX_TICKET;
    }
}
