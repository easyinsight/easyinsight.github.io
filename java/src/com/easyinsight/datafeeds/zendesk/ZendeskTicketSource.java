package com.easyinsight.datafeeds.zendesk;

import com.easyinsight.analysis.*;
import com.easyinsight.core.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.storage.IWhere;
import com.easyinsight.storage.StringWhere;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.net.URLEncoder;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 3/21/11
 * Time: 6:39 PM
 */
public class ZendeskTicketSource extends ZendeskBaseSource {

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

    public ZendeskTicketSource() {
        setFeedName("Tickets");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.ZENDESK_TICKET;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        List<String> baseKeys = new ArrayList<String>(Arrays.asList(ASSIGNED_AT, ASSIGNEE, BASE_SCORE, CREATED_AT, TAGS, DESCRIPTION,
                DUE_DATE, GROUP_ID, INITIALLY_ASSIGNED_AT, ORGANIZATION_ID, PRIORITY, REQUESTER, RESOLUTION_TIME,
                SOLVED_AT, STATUS, STATUS_UPDATED_AT, SUBMITTER, SUBJECT, TICKET_TYPE, UPDATED_AT, SCORE, VIA, COUNT, TICKET_ID));
        try {
            ZendeskCompositeSource zendeskCompositeSource = (ZendeskCompositeSource) parentDefinition;
            /*Document fields = runRestRequest(zendeskCompositeSource, getHttpClient(zendeskCompositeSource.getZdUserName(),
                    zendeskCompositeSource.getZdPassword()), "/ticket_fields.xml", new Builder());
            Nodes recordNodes = fields.query("/records/record");
            for (int i = 0; i < recordNodes.size(); i++) {
                Node recordNode = recordNodes.get(i);
                String title = queryField(recordNode, "title/text()");
                if (!baseKeys.contains(title)) {
                    baseKeys.add("zd" + queryField(recordNode, "id/text()"));
                }
            }*/
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return baseKeys;
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

        try {
            ZendeskCompositeSource zendeskCompositeSource = (ZendeskCompositeSource) parentDefinition;
            Document fields = runRestRequest(zendeskCompositeSource, getHttpClient(zendeskCompositeSource.getZdUserName(),
                    zendeskCompositeSource.getZdPassword()), "/ticket_fields.xml", new Builder());
            Nodes recordNodes = fields.query("/records/record");
            for (int i = 0; i < recordNodes.size(); i++) {
                Node recordNode = recordNodes.get(i);
                String title = queryField(recordNode, "title/text()");
                String id = queryField(recordNode, "id/text()");
                Key customKey = keys.get("zd" + id);
                if (customKey == null) {
                    customKey = new NamedKey("zd" + id);
                }
                String type = queryField(recordNode, "type/text()");
                if ("FieldText".equals(type) || "DropDownField".equals(type) || "CheckboxField1".equals(type)) {
                    items.add(new AnalysisDimension(customKey, title));
                } else if ("MultiLineField".equals(type)) {
                    items.add(new AnalysisText(customKey, title));
                } else if ("NumericField".equals(type) || "DecimalField".equals(type)) {
                    items.add(new AnalysisMeasure(customKey, title, AggregationTypes.SUM));
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return items;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            if (lastRefreshDate == null) {
                return getAllTickets(keys, (ZendeskCompositeSource) parentDefinition);
            } else {
                getUpdatedTickets(keys, (ZendeskCompositeSource) parentDefinition, lastRefreshDate, IDataStorage);
                return null;
            }
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private String getUserName(String userID, Map<String, String> userCache, ZendeskCompositeSource zendeskCompositeSource, HttpClient httpClient) throws InterruptedException {
        String userName = userCache.get(userID);
        if (userName == null) {
            Document userDoc = runRestRequest(zendeskCompositeSource, httpClient, "/users/" + userID + ".xml", new Builder());
            Nodes nameNodes = userDoc.query("/user/name/text()");
            if (nameNodes.size() > 0) {
                userName = nameNodes.get(0).getValue();
            } else {
                userName = "";
            }
            userCache.put(userID, userName);
        }
        return userName;
    }

    @Override
    protected boolean clearsData(FeedDefinition parentSource) {
        return false;
    }

    private void getUpdatedTickets(Map<String, Key> keys, ZendeskCompositeSource zendeskCompositeSource, Date lastUpdateDate, IDataStorage IDataStorage) throws Exception {

        HttpClient httpClient = getHttpClient(zendeskCompositeSource.getZdUserName(), zendeskCompositeSource.getZdPassword());
        Builder builder = new Builder();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
        DateFormat updateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(lastUpdateDate);
        cal.add(Calendar.DAY_OF_YEAR, -1);
        String updateDate = updateFormat.format(cal.getTime());
        Map<String, String> userCache = new HashMap<String, String>();
        Key noteKey = zendeskCompositeSource.getField(TICKET_ID).toBaseKey();
        boolean moreData;
        int page = 1;
        do {
            String path = "/search.xml?query=" + URLEncoder.encode("\"type:ticket updated>"+updateDate+"\"", "UTF-8");
            if (page > 1) {
                path += "&page=" + page;
            }
            Document doc = runRestRequest(zendeskCompositeSource, httpClient, path, builder);
            Nodes ticketNodes = doc.query("/records/record");
            moreData = ticketNodes.size() == 15;
            for (int i = 0; i < ticketNodes.size(); i++) {
                DataSet dataSet = new DataSet();
                IRow row = dataSet.createRow();
                Node ticketNode = ticketNodes.get(i);
                String id = parseTicket(keys, zendeskCompositeSource, httpClient, dateFormat, userCache, row, ticketNode);
                if (id != null) {
                    StringWhere userWhere = new StringWhere(noteKey, id);
                    IDataStorage.updateData(dataSet, Arrays.asList((IWhere) userWhere));
                }
            }
            page++;
        } while (moreData);
    }

    private DataSet getAllTickets(Map<String, Key> keys, ZendeskCompositeSource zendeskCompositeSource) throws InterruptedException, ParseException {
        DataSet dataSet = new DataSet();
        HttpClient httpClient = getHttpClient(zendeskCompositeSource.getZdUserName(), zendeskCompositeSource.getZdPassword());
        Builder builder = new Builder();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
        Map<String, String> userCache = new HashMap<String, String>();
        boolean moreData;
        int page = 1;
        do {
            String path = "/search.xml?query=type:ticket";
            if (page > 1) {
                path += "&page=" + page;
            }

            Document doc = runRestRequest(zendeskCompositeSource, httpClient, path, builder);
            Nodes ticketNodes = doc.query("/records/record");
            moreData = ticketNodes.size() == 15;
            for (int i = 0; i < ticketNodes.size(); i++) {
                IRow row = dataSet.createRow();
                Node ticketNode = ticketNodes.get(i);
                parseTicket(keys, zendeskCompositeSource, httpClient, dateFormat, userCache, row, ticketNode);
            }
            page++;
        } while (moreData);
        return dataSet;
    }

    private String parseTicket(Map<String, Key> keys, ZendeskCompositeSource zendeskCompositeSource, HttpClient httpClient, DateFormat dateFormat, Map<String, String> userCache, IRow row, Node ticketNode) throws ParseException, InterruptedException {
        try {
            row.addValue(keys.get(ASSIGNED_AT), queryDate(ticketNode, "assigned-at/text()"));
            row.addValue(keys.get(ASSIGNEE), queryUser(ticketNode, "assignee-id/text()", userCache, zendeskCompositeSource, httpClient));
            row.addValue(keys.get(BASE_SCORE), queryField(ticketNode, "base-score/text()"));
            row.addValue(keys.get(SCORE), queryField(ticketNode, "score/text()"));
            row.addValue(keys.get(COUNT), 1);
            row.addValue(keys.get(CREATED_AT), queryDate(ticketNode, "created-at/text()"));
            row.addValue(keys.get(DESCRIPTION), queryField(ticketNode, "description/text()"));
            row.addValue(keys.get(DUE_DATE), queryDate(ticketNode, "due-date/text()"));
            //row.addValue(keys.get(RESOLUTION_TIME), queryDate(ticketNode, "resolution-time/text()"));
            row.addValue(keys.get(SOLVED_AT), queryDate(ticketNode, "solved-at/text()"));
            row.addValue(keys.get(UPDATED_AT), queryDate(ticketNode, "updated-at/text()"));
            row.addValue(keys.get(GROUP_ID), queryField(ticketNode, "group-id/text()"));
            row.addValue(keys.get(SUBJECT), queryField(ticketNode, "subject/text()"));
            row.addValue(keys.get(ORGANIZATION_ID), queryField(ticketNode, "organization-id/text()"));
            String id = queryField(ticketNode, "nice-id/text()");
            row.addValue(keys.get(TICKET_ID), id);
            row.addValue(keys.get(REQUESTER), queryUser(ticketNode, "requester-id/text()", userCache, zendeskCompositeSource, httpClient));
            row.addValue(keys.get(SUBMITTER), queryUser(ticketNode, "submitter-id/text()", userCache, zendeskCompositeSource, httpClient));
            String tags = queryField(ticketNode, "current-tags/text()");
            if (tags != null) {
                String[] tagElements = tags.split(" ");
                StringBuilder stringBuilder = new StringBuilder();
                for (String tag : tagElements) {
                    stringBuilder.append(tag).append(",");
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                row.addValue(keys.get(TAGS), stringBuilder.toString());
            }

            Nodes customFieldEntries = ticketNode.query("ticket-field-entries/ticket-field-entry");
            for (int i = 0; i < customFieldEntries.size(); i++) {
                Node customFieldEntry = customFieldEntries.get(i);
                String fieldID = queryField(customFieldEntry, "ticket-field-id/text()");
                String value = queryField(customFieldEntry, "value/text()");
                Key key = keys.get("zd" + fieldID);
                row.addValue(key, value);
            }

            int statusID = Integer.parseInt(queryField(ticketNode, "status-id/text()"));

            if (statusID == 0) {
                row.addValue(keys.get(STATUS), "New");
            } else if (statusID == 1) {
                row.addValue(keys.get(STATUS), "Open");
            } else if (statusID == 2) {
                row.addValue(keys.get(STATUS), "Pending");
            } else if (statusID == 3) {
                row.addValue(keys.get(STATUS), "Solved");
            } else if (statusID == 4) {
                row.addValue(keys.get(STATUS), "Closed");
            }

            int ticketTypeID = Integer.parseInt(queryField(ticketNode, "ticket-type-id/text()"));

            if (ticketTypeID == 0) {
                row.addValue(keys.get(TICKET_TYPE), "( No Type Set )");
            } else if (ticketTypeID == 1) {
                row.addValue(keys.get(TICKET_TYPE), "Question");
            } else if (ticketTypeID == 2) {
                row.addValue(keys.get(TICKET_TYPE), "Incident");
            } else if (ticketTypeID == 3) {
                row.addValue(keys.get(TICKET_TYPE), "Problem");
            } else if (ticketTypeID == 4) {
                row.addValue(keys.get(TICKET_TYPE), "Task");
            }

            int priorityID = Integer.parseInt(queryField(ticketNode, "priority-id/text()"));

            if (priorityID == 0) {
                row.addValue(keys.get(PRIORITY), "( No Priority Set )");
            } else if (priorityID == 1) {
                row.addValue(keys.get(PRIORITY), "Low");
            } else if (priorityID == 2) {
                row.addValue(keys.get(PRIORITY), "Normal");
            } else if (priorityID == 3) {
                row.addValue(keys.get(PRIORITY), "High");
            } else if (priorityID == 4) {
                row.addValue(keys.get(PRIORITY), "Urgent");
            }

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

    protected Value queryUser(Node node, String target, Map<String, String> userCache, ZendeskCompositeSource zendeskCompositeSource, HttpClient client) throws InterruptedException {
        String value = queryField(node, target);
        if (value != null && !"".equals(value)) {
            try {
                return new StringValue(getUserName(value, userCache, zendeskCompositeSource, client));
            } catch (Exception e) {
                return new EmptyValue();
            }
        }
        return new EmptyValue();
    }

    protected Value queryDate(Node node, String target) throws ParseException {
        String value = queryField(node, target);
        if (value != null && !"".equals(value)) {
            try {
                System.out.println(value);
                return new DateValue(javax.xml.bind.DatatypeConverter.parseDateTime(value).getTime());
            } catch (Exception e) {
                LogClass.error(e);
                return new EmptyValue();
            }
        }
        return new EmptyValue();
    }

    private DataSet getTicketsChangedSince() {
        return null;
    }
}
