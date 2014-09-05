package com.easyinsight.datafeeds.zendesk;

import com.easyinsight.analysis.*;
import com.easyinsight.core.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.freshdesk.TicketAnalysis;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.storage.IWhere;
import com.easyinsight.storage.StringWhere;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * User: jamesboe
 * Date: 3/21/11
 * Time: 6:39 PM
 */
public class ZendeskTicketSource extends ZendeskBaseSource {

    public static final int CUSTOM_FIELD_TICKET = 1;

    public static final String ASSIGNED_AT = "Assigned At";
    public static final String ASSIGNEE = "Assignee";
    public static final String BASE_SCORE = "Base Score";
    public static final String CREATED_AT = "Ticket Created At";
    public static final String TAGS = "Ticket Tags";
    public static final String DESCRIPTION = "Description";
    public static final String DUE_DATE = "Due Date";
    public static final String GROUP_ID = "Group";
    public static final String INITIALLY_ASSIGNED_AT = "Initially Assigned At";
    public static final String ORGANIZATION_ID = "Organization ID";
    public static final String PRIORITY = "Priority";
    public static final String REQUESTER = "Requester";
    public static final String RESOLUTION_TIME = "Resolved At";
    public static final String SOLVED_AT = "Solved At";
    public static final String STATUS = "Request status";
    public static final String STATUS_UPDATED_AT = "Status Updated At";
    public static final String SUBMITTER = "Submitter";
    public static final String SUBJECT = "Subject";
    public static final String TICKET_TYPE = "Request Type";
    public static final String TICKET_ID = "Ticket ID";
    public static final String UPDATED_AT = "Ticket Updated At";
    public static final String VIA = "Ticket Submitted Via";
    public static final String SCORE = "Score";
    public static final String COUNT = "Ticket Count";

    public static final String CURRENT = "Ticket With";
    public static final String AGENT_HANDLES = "Agent Touches";
    public static final String CUSTOMER_HANDLES = "Customer Touches";
    public static final String TIME_UNASSIGNED = "Time Unassigned";
    public static final String TIME_WITH_AGENT = "Time With Agent";
    public static final String TIME_WITH_CUSTOMER = "Time With Customer";

    public ZendeskTicketSource() {
        setFeedName("Tickets");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.ZENDESK_TICKET;
    }

    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ZZ");
    private final DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");



    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return new ArrayList<String>(Arrays.asList(ASSIGNED_AT, ASSIGNEE, BASE_SCORE, CREATED_AT, TAGS, DESCRIPTION,
                DUE_DATE, GROUP_ID, INITIALLY_ASSIGNED_AT, ORGANIZATION_ID, PRIORITY, REQUESTER, RESOLUTION_TIME,
                SOLVED_AT, STATUS, STATUS_UPDATED_AT, SUBMITTER, SUBJECT, TICKET_TYPE, UPDATED_AT, SCORE, VIA, COUNT, TICKET_ID));
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDateDimension(keys.get(ASSIGNED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        items.add(new AnalysisDateDimension(keys.get(CREATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        items.add(new AnalysisDateDimension(keys.get(DUE_DATE), true, AnalysisDateDimension.DAY_LEVEL));
        items.add(new AnalysisDateDimension(keys.get(INITIALLY_ASSIGNED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        items.add(new AnalysisDateDimension(keys.get(RESOLUTION_TIME), true, AnalysisDateDimension.DAY_LEVEL));
        items.add(new AnalysisDateDimension(keys.get(SOLVED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        items.add(new AnalysisDateDimension(keys.get(STATUS_UPDATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        items.add(new AnalysisDateDimension(keys.get(UPDATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        items.add(new AnalysisDimension(keys.get(ASSIGNEE), true));
        items.add(new AnalysisText(keys.get(DESCRIPTION)));
        items.add(new AnalysisList(keys.get(TAGS), true, ","));
        items.add(new AnalysisDimension(keys.get(GROUP_ID), true));
        items.add(new AnalysisDimension(keys.get(VIA), true));
        items.add(new AnalysisDimension(keys.get(ORGANIZATION_ID), true));
        items.add(new AnalysisDimension(keys.get(PRIORITY), true));
        items.add(new AnalysisDimension(keys.get(REQUESTER), true));
        items.add(new AnalysisDimension(keys.get(STATUS), true));
        items.add(new AnalysisDimension(keys.get(SUBMITTER), true));
        items.add(new AnalysisDimension(keys.get(SUBJECT), true));
        items.add(new AnalysisDimension(keys.get(TICKET_TYPE), true));
        items.add(new AnalysisDimension(keys.get(TICKET_ID), true));
        items.add(new AnalysisMeasure(keys.get(BASE_SCORE), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(SCORE), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        Key currentKey = keys.get(CURRENT);
        if (currentKey == null) {
            currentKey = new NamedKey(CURRENT);
        }
        items.add(new AnalysisDimension(currentKey));

        Key agentHandlesKey = keys.get(AGENT_HANDLES);
        if (agentHandlesKey == null) {
            agentHandlesKey = new NamedKey(AGENT_HANDLES);
        }
        items.add(new AnalysisMeasure(agentHandlesKey, AggregationTypes.SUM));

        Key customerHandlesKey = keys.get(CUSTOMER_HANDLES);
        if (customerHandlesKey == null) {
            customerHandlesKey = new NamedKey(CUSTOMER_HANDLES);
        }
        items.add(new AnalysisMeasure(customerHandlesKey, AggregationTypes.SUM));

        Key timeWithAgent = keys.get(TIME_WITH_AGENT);
        if (timeWithAgent == null) {
            timeWithAgent = new NamedKey(TIME_WITH_AGENT);
        }
        items.add(new AnalysisMeasure(timeWithAgent, TIME_WITH_AGENT, AggregationTypes.SUM, false, FormattingConfiguration.MILLISECONDS));

        Key timeWithCustomer = keys.get(TIME_WITH_CUSTOMER);
        if (timeWithCustomer == null) {
            timeWithCustomer = new NamedKey(TIME_WITH_CUSTOMER);
        }
        items.add(new AnalysisMeasure(timeWithCustomer, TIME_WITH_CUSTOMER, AggregationTypes.SUM, false, FormattingConfiguration.MILLISECONDS));

        try {
            ZendeskCompositeSource zendeskCompositeSource = (ZendeskCompositeSource) parentDefinition;
            String nextPage = zendeskCompositeSource.getUrl() + "/api/v2/ticket_fields.json";
            do {
                Map m = queryList(nextPage, zendeskCompositeSource, getHttpClient(zendeskCompositeSource));
                JSONArray recordNodes = (JSONArray) m.get("ticket_fields");
                if (recordNodes != null) {
                    for (int i = 0; i < recordNodes.size(); i++) {
                        JSONObject recordNode = (JSONObject) recordNodes.get(i);
                        String title = String.valueOf(recordNode.get("title"));
                        String id = String.valueOf(recordNode.get("id"));
                        Key customKey = keys.get("zd" + id);
                        if (customKey == null) {
                            customKey = new NamedKey("zd" + id);
                        }
                        String type = String.valueOf(recordNode.get("type"));
                        AnalysisItem item;
                        if ("FieldText".equals(type) || "DropDownField".equals(type) || "CheckboxField1".equals(type) || "FieldTagger".equals(type) || "tagger".equals(type) ||
                                "checkbox".equals(type) || "dropdown".equals(type) || "text".equals(type) || "regexp".equals(type)) {
                            item = new AnalysisDimension(customKey, title);
                        } else if ("MultiLineField".equals(type) || "FieldTextarea".equals(type) || "textarea".equals(type)) {
                            item = new AnalysisText(customKey, title);
                        } else if ("NumericField".equals(type) || "FieldDecimal".equals(type) || "FieldInteger".equals(type) || "FieldNumeric".equals(type) ||
                                "integer".equals(type) || "decimal".equals(type) || "numeric".equals(type)) {
                            item = new AnalysisMeasure(customKey, title, AggregationTypes.SUM);
                        } else if ("date".equals(type)) {
                            item = new AnalysisDateDimension(customKey, title, AnalysisDateDimension.DAY_LEVEL);
                        } else {
                            item = null;
                        }
                        if (item != null) {
                            item.setCustomFlag(CUSTOM_FIELD_TICKET);
                            items.add(item);
                        }
                    }
                    nextPage = (String) m.get("next_page");
                } else {
                    nextPage = null;
                }
            } while (nextPage != null);
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return items;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            ZendeskCompositeSource zendeskCompositeSource = (ZendeskCompositeSource) parentDefinition;
            HttpClient httpClient = getHttpClient(zendeskCompositeSource);
            ZendeskUserCache zendeskUserCache = zendeskCompositeSource.getOrCreateUserCache(httpClient);
            //if (lastRefreshDate == null) {
            return getAllTickets(keys, zendeskCompositeSource, zendeskUserCache, IDataStorage, lastRefreshDate);
            /*} else {
                getUpdatedTickets(keys, zendeskCompositeSource, lastRefreshDate, IDataStorage, zendeskUserCache);
                return null;
            }*/
        } catch (ReportException re) {
            LogClass.error(re);
            throw re;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private String getUserName(String userID, ZendeskUserCache zendeskUserCache) throws InterruptedException {
        ZendeskUser zendeskUser = zendeskUserCache.getUsers().get(userID);
        if (zendeskUser == null) {
            return "";
        } else {
            return zendeskUser.getName();
        }
    }

    @Override
    protected boolean clearsData(FeedDefinition parentSource) {
        return false;
    }

    private DataSet getAllTickets(Map<String, Key> keys, ZendeskCompositeSource zendeskCompositeSource, ZendeskUserCache userCache, IDataStorage dataStorage, Date lastStart) throws Exception {
        DataSet dataSet = new DataSet();
        HttpClient httpClient = getHttpClient(zendeskCompositeSource);
        Calendar cal = Calendar.getInstance();
        if (lastStart == null) {
            cal.add(Calendar.YEAR, -5);
        } else {
            cal.setTime(lastStart);
            cal.add(Calendar.DAY_OF_YEAR, -1);
        }
        Key noteKey = zendeskCompositeSource.getField(TICKET_ID).toBaseKey();
        DateFormat adf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date start = new Date(System.currentTimeMillis() - (1000 * 60 * 60));
        long time = cal.getTimeInMillis() / 1000;
        String nextPage = zendeskCompositeSource.getUrl() + "/api/v2/exports/tickets.json?start_time=" + time;
        List<Comment> commentList = new ArrayList<Comment>();

        Set<String> ticketIDs = new HashSet<>();
        Set<String> commentIDs = new HashSet<>();
        Set<String> auditIDs = new HashSet<>();
        int safeguard = 0;
        while (nextPage != null) {
            Map ticketObjects = queryList(nextPage, zendeskCompositeSource, httpClient);
            List results = (List) ticketObjects.get("results");
            if (results != null) {
                for (Object obj : results) {
                    Map map = (Map) obj;
                    IRow row = dataSet.createRow();
                    String id = parseTicket(keys, userCache, row, map);
                    for (Map.Entry<String, Key> entry : keys.entrySet()) {
                        if (entry.getKey().startsWith("zd")) {
                            int customFieldID = Integer.parseInt(entry.getKey().substring(2));


                            Object customFieldObject = map.get("field_" + customFieldID);
                            if (customFieldObject != null) {
                                row.addValue(entry.getKey(), customFieldObject.toString());
                            }
                        }
                    }

                    String status = queryField(map, "status");
                    if (!"Deleted".equals(status)) {
                        if (auditIDs.contains(id)) {

                        } else {
                            try {
                                Date createDate = ((DateValue) queryDate(map, "created_at")).getDate();
                                ZonedDateTime createDateTime = createDate.toInstant().atZone(ZoneId.systemDefault());
                                TicketAnalysis ticketAnalysis = new TicketAnalysis(createDateTime);
                                Map ticketDetail = queryList(zendeskCompositeSource.getUrl() + "/api/v2/tickets/" + id + "/audits.json", zendeskCompositeSource, httpClient);

                                List<Map> audits = (List<Map>) ticketDetail.get("audits");
                                if (audits != null) {
                                    for (Map audit : audits) {
                                        Date date = df2.parse(audit.get("created_at").toString());
                                        List<Map> events = (List<Map>) audit.get("events");
                                        for (Map event : events) {
                                            if (event.get("field_name") != null) {

                                                String fieldName = event.get("field_name").toString();
                                                if ("status".equals(fieldName)) {
                                                    String type = event.get("value").toString();
                                                    if ("new".equals(type) || "open".equals(type)) {
                                                        ticketAnalysis.addResponsibility(TicketAnalysis.AGENT, date);
                                                    } else if ("solved".equals(type)) {
                                                        ticketAnalysis.addResponsibility(TicketAnalysis.SOLVED, date);
                                                    } else {
                                                        ticketAnalysis.addResponsibility(TicketAnalysis.CUSTOMER, date);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                ticketAnalysis.calculate();
                                row.addValue(keys.get(CUSTOMER_HANDLES), ticketAnalysis.getCustomerHandles());
                                row.addValue(keys.get(AGENT_HANDLES), ticketAnalysis.getAgentHandles());
                                if (ticketAnalysis.getWaitState() == TicketAnalysis.AGENT) {
                                    row.addValue(keys.get(CURRENT), "On Agent");
                                } else if (ticketAnalysis.getWaitState() == TicketAnalysis.CUSTOMER) {
                                    row.addValue(keys.get(CURRENT), "On Customer");
                                } else if (ticketAnalysis.getWaitState() == TicketAnalysis.UNASSIGNED) {
                                    row.addValue(keys.get(CURRENT), "Unassigned");
                                } else if (ticketAnalysis.getWaitState() == TicketAnalysis.SOLVED) {
                                    row.addValue(keys.get(CURRENT), "Solved");
                                }
                                row.addValue(keys.get(TIME_WITH_CUSTOMER), ticketAnalysis.getElapsedCustomerTime());
                                row.addValue(keys.get(TIME_WITH_AGENT), ticketAnalysis.getElapsedAgentTime());
                            } catch (Exception e) {
                                LogClass.error(e);
                            }
                            auditIDs.add(id);
                        }
                    }
                    //}

                    if (zendeskCompositeSource.isHackMethod()) {
                        if (ticketIDs.contains(id)) {

                        } else {
                            try {
                                Map ticketDetail = queryList(zendeskCompositeSource.getUrl() + "/api/v2/tickets/" + id + ".json", zendeskCompositeSource, httpClient);

                                Map detailObject = (Map) ticketDetail.get("ticket");
                                if (detailObject != null) {
                                    List customFields = (List) detailObject.get("custom_fields");
                                    if (customFields != null) {
                                        for (Object cFieldObj : customFields) {
                                            Map customFieldMap = (Map) cFieldObj;
                                            String fieldID = customFieldMap.get("id").toString();
                                            Key key = keys.get("zd" + fieldID);
                                            if (row.getValueNoAdd(key).type() == Value.EMPTY || "".equals(row.getValueNoAdd(key).toString())) {
                                                Object fieldValueObject = customFieldMap.get("value");
                                                if (fieldValueObject != null) {

                                                    if (key != null) {
                                                        row.addValue(key, fieldValueObject.toString());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                LogClass.error(e);
                            }
                            ticketIDs.add(id);
                        }
                    }

                    if (zendeskCompositeSource.isLoadComments()) {
                        if (commentIDs.contains(id)) {

                        } else {
                            Map detail = queryList(zendeskCompositeSource.getUrl() + "/api/v2/tickets/" + id + "/comments.json", zendeskCompositeSource, httpClient);

                            List comments = (List) detail.get("comments");
                            if (comments != null) {
                                String firstComment = null;
                                for (Object commentMapObj : comments) {
                                    Map commentMap = (Map) commentMapObj;
                                    String commentID = commentMap.get("id").toString();
                                    String commentDescription = commentMap.get("body").toString();
                                    if (firstComment == null) {
                                        firstComment = commentDescription;
                                    }
                                    String author = queryUser(commentMap.get("author_id").toString(), userCache).toString();
                                    Date createdAt = adf.parse(commentMap.get("created_at").toString());
                                    commentList.add(new Comment(Long.parseLong(commentID), commentDescription, author, createdAt, id));
                                }

                                row.addValue(DESCRIPTION, firstComment);
                            }
                            commentIDs.add(id);
                        }
                    }
                    if (lastStart != null) {
                        StringWhere userWhere = new StringWhere(noteKey, id);
                        dataStorage.updateData(dataSet, Arrays.asList((IWhere) userWhere));
                        dataSet = new DataSet();
                    }
                }
            }
            if (ticketObjects.get("next_page") != null && !ticketObjects.get("next_page").toString().equals(nextPage)) {
                safeguard++;
                if (safeguard == 50) {
                    return null;
                }
                // https://asejur.zendesk.com/api/v2/exports/tickets.json?start_time=1404236653
                // https://asejur.zendesk.com/api/v2/exports/tickets.json?start_time=1404322404
                nextPage = ticketObjects.get("next_page").toString();
                long ms = Long.parseLong(nextPage.split("\\=")[1]) * 1000;
                Calendar c2 = Calendar.getInstance();
                c2.setTimeInMillis(ms);
                if (c2.getTime().after(start)) {
                    nextPage = null;
                } else {
                    Thread.sleep(60000);
                }
            } else {
                nextPage = null;
            }

            if (lastStart == null) {
                dataStorage.insertData(dataSet);
                dataSet = new DataSet();
            }
            zendeskCompositeSource.setComments(commentList);
        }

        return null;
    }

    protected Value queryUser(String value, ZendeskUserCache zendeskUserCache) throws InterruptedException {
        if (value != null && !"".equals(value)) {
            try {
                return new StringValue(getUserName(value, zendeskUserCache));
            } catch (Exception e) {
                return new EmptyValue();
            }
        }
        return new EmptyValue();
    }

    private String parseTicket(Map<String, Key> keys, ZendeskUserCache userCache, IRow row, Map ticketNode) throws ParseException, InterruptedException {
        try {
            row.addValue(keys.get(ASSIGNED_AT), queryDate(ticketNode, "assigned_at"));
            row.addValue(keys.get(INITIALLY_ASSIGNED_AT), queryDate(ticketNode, "initially_assigned_at"));
            row.addValue(keys.get(ASSIGNEE), queryField(ticketNode, "assignee_name"));
            row.addValue(keys.get(BASE_SCORE), queryField(ticketNode, "base_score"));
            row.addValue(keys.get(SCORE), queryField(ticketNode, "score"));
            row.addValue(keys.get(COUNT), 1);
            row.addValue(keys.get(CREATED_AT), queryDate(ticketNode, "created_at"));
            row.addValue(keys.get(DESCRIPTION), queryField(ticketNode, "description"));
            row.addValue(keys.get(DUE_DATE), queryDate(ticketNode, "due_date"));
            row.addValue(keys.get(STATUS), queryField(ticketNode, "status"));
            row.addValue(keys.get(TICKET_TYPE), queryField(ticketNode, "ticket_type"));
            row.addValue(keys.get(PRIORITY), queryField(ticketNode, "priority"));
            //row.addValue(keys.get(RESOLUTION_TIME), queryDate(ticketNode, "resolution_time"));
            row.addValue(keys.get(SOLVED_AT), queryDate(ticketNode, "solved_at"));
            row.addValue(keys.get(UPDATED_AT), queryDate(ticketNode, "updated_at"));
            row.addValue(keys.get(GROUP_ID), queryField(ticketNode, "group_id"));
            row.addValue(keys.get(SUBJECT), queryField(ticketNode, "subject"));
            row.addValue(keys.get(ORGANIZATION_ID), queryField(ticketNode, "organization_id"));
            String id = queryField(ticketNode, "id");
            row.addValue(keys.get(TICKET_ID), id);
            row.addValue(keys.get(REQUESTER), queryField(ticketNode, "req_name"));
            row.addValue(keys.get(SUBMITTER), queryField(ticketNode, "submitter_name"));
            row.addValue(keys.get(VIA), queryField(ticketNode, "via"));
            String tags = queryField(ticketNode, "current_tags");
            if (tags != null) {
                String[] tagElements = tags.split(" ");
                StringBuilder stringBuilder = new StringBuilder();
                for (String tag : tagElements) {
                    stringBuilder.append(tag).append(",");
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                row.addValue(keys.get(TAGS), stringBuilder.toString());
            }



            /*try {
                int viaID = Integer.parseInt(queryField(ticketNode, "via-id/text()"));

                if (viaID == 0) {
                    row.addValue(keys.get(VIA), "Web Form");
                } else if (viaID == 4) {
                    row.addValue(keys.get(VIA), "Mail");
                } else if (viaID == 5) {
                    row.addValue(keys.get(VIA), "Web Service API");
                } else if (viaID == 16) {
                    row.addValue(keys.get(VIA), "Get Satisfaction");
                } else if (viaID == 17) {
                    row.addValue(keys.get(VIA), "Dropbox");
                } else if (viaID == 19) {
                    row.addValue(keys.get(VIA), "Ticket merge");
                } else if (viaID == 21) {
                    row.addValue(keys.get(VIA), "Recovered from suspended tickets");
                } else if (viaID == 23) {
                    row.addValue(keys.get(VIA), "Twitter favorite");
                } else if (viaID == 24) {
                    row.addValue(keys.get(VIA), "Forum topic");
                } else if (viaID == 26) {
                    row.addValue(keys.get(VIA), "Twitter direct message");
                } else if (viaID == 27) {
                    row.addValue(keys.get(VIA), "Closed ticket");
                } else if (viaID == 29) {
                    row.addValue(keys.get(VIA), "Chat");
                } else if (viaID == 30) {
                    row.addValue(keys.get(VIA), "Twitter public message");
                }
            } catch (NumberFormatException e) {
                // ignore
            }*/

            return id;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            LogClass.error(e);
        }
        return null;
    }

    @Override
    protected String getUpdateKeyName() {
        return TICKET_ID;
    }

    protected Value queryUser(Map node, String target, ZendeskUserCache zendeskUserCache) throws InterruptedException {
        String value = queryField(node, target);
        if (value != null && !"".equals(value)) {
            try {
                return new StringValue(getUserName(value, zendeskUserCache));
            } catch (Exception e) {
                return new EmptyValue();
            }
        }
        return new EmptyValue();
    }

    protected Value queryDate(Map node, String target) throws ParseException {
        String value = queryField(node, target);
        if (value != null && !"".equals(value)) {
            try {
                return new DateValue(df.parse(value));
            } catch (Exception e) {
                LogClass.error(e);
                return new EmptyValue();
            }
        }
        return new EmptyValue();
    }

    protected Value queryOtherDate(Map node, String target) throws ParseException {
        String value = queryField(node, target);
        if (value != null && !"".equals(value)) {
            try {
                return new DateValue(df2.parse(value));
            } catch (Exception e) {
                LogClass.error(e);
                return new EmptyValue();
            }
        }
        return new EmptyValue();
    }
}
