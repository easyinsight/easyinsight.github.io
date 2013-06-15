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

    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ZZ");

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
                if ("FieldText".equals(type) || "DropDownField".equals(type) || "CheckboxField1".equals(type) || "FieldTagger".equals(type)) {
                    items.add(new AnalysisDimension(customKey, title));
                } else if ("MultiLineField".equals(type) || "FieldTextarea".equals(type)) {
                    items.add(new AnalysisText(customKey, title));
                } else if ("NumericField".equals(type) || "FieldDecimal".equals(type) || "FieldInteger".equals(type) || "FieldNumeric".equals(type)) {
                    items.add(new AnalysisMeasure(customKey, title, AggregationTypes.SUM));
                }
            }
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
            HttpClient httpClient = getHttpClient(zendeskCompositeSource.getZdUserName(), zendeskCompositeSource.getZdPassword());
            ZendeskUserCache zendeskUserCache = zendeskCompositeSource.getOrCreateUserCache(httpClient);
            //if (lastRefreshDate == null) {
                return getAllTickets(keys, zendeskCompositeSource, zendeskUserCache, IDataStorage);
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
        return true;
    }

    /*private void getUpdatedTickets(Map<String, Key> keys, ZendeskCompositeSource zendeskCompositeSource, Date lastUpdateDate, IDataStorage IDataStorage,
                                   ZendeskUserCache zendeskUserCache) throws Exception {

        HttpClient httpClient = new HttpClient();
        Builder builder = new Builder();

        DateFormat updateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();
        cal.setTime(lastUpdateDate);
        cal.add(Calendar.DAY_OF_YEAR, -7);
        lastUpdateDate = cal.getTime();

        String updateDate = updateFormat.format(lastUpdateDate);
        Key noteKey = zendeskCompositeSource.getField(TICKET_ID).toBaseKey();
        int page = 1;
        int count = 0;
        int recordCount = 0;
        do {
            String path = "/search.xml?query=" + URLEncoder.encode("\"type:ticket updated>"+updateDate+"\"", "UTF-8");
            path += "&page=" + page;
            System.out.println(path);
            Document doc = runRestRequest(zendeskCompositeSource, httpClient, path, builder);
            Nodes ticketNodes = doc.query("/records/record");
            if (page == 1) {
                Nodes countNodes = doc.query("/records/@count");
                if (countNodes.size() == 1) {
                    count = Integer.parseInt(countNodes.get(0).getValue());
                } else {
                    count = 0;
                }
            }
            for (int i = 0; i < ticketNodes.size(); i++) {
                DataSet dataSet = new DataSet();
                IRow row = dataSet.createRow();
                Node ticketNode = ticketNodes.get(i);
                String id = parseTicket(keys, zendeskUserCache, row, ticketNode);
                if (id != null) {
                    StringWhere userWhere = new StringWhere(noteKey, id);
                    IDataStorage.updateData(dataSet, Arrays.asList((IWhere) userWhere));
                }
            }
            page++;
            recordCount += 15;
        } while (recordCount < count);
    }*/

    private DataSet getAllTickets(Map<String, Key> keys, ZendeskCompositeSource zendeskCompositeSource, ZendeskUserCache userCache, IDataStorage dataStorage) throws Exception {
        DataSet dataSet = new DataSet();
        DateFormat updateFormat = new SimpleDateFormat("yyyy-MM-dd");
        HttpClient httpClient = getHttpClient(zendeskCompositeSource.getZdUserName(), zendeskCompositeSource.getZdPassword());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        long time = cal.getTimeInMillis();
        time = 0;
        Map<String, IRow> ticketMap = new HashMap<String, IRow>();
        String updateDate = updateFormat.format(cal.getTime());
        String nextPage = zendeskCompositeSource.getUrl() + "/api/v2/exports/tickets.json?start_time=" + time;

        while (nextPage != null) {
            int count = 0;
            Map ticketObjects = queryList(nextPage, zendeskCompositeSource, httpClient);
            List results = (List) ticketObjects.get("results");
            if (results != null) {
                for (Object obj : results) {
                    count++;
                    Map map = (Map) obj;
                    IRow row = dataSet.createRow();
                    String id = parseTicket(keys, userCache, row, map);
                    ticketMap.put(id, row);
                }
            }
            if (ticketObjects.get("next_page") != null && !ticketObjects.get("next_page").toString().equals(nextPage) && count == 1000) {
                nextPage = ticketObjects.get("next_page").toString();
                Thread.sleep(60000);
            } else {
                nextPage = null;
            }
        }


        nextPage = zendeskCompositeSource.getUrl() + "/api/v2/search.json?query=" + "type:ticket%20updated>"+updateDate;
        while (nextPage != null) {
            Map ticketObjects = queryList(nextPage, zendeskCompositeSource, httpClient);
            List results = (List) ticketObjects.get("results");
            for (Object obj : results) {
                Map map = (Map) obj;
                String ticketID = map.get("id").toString();
                IRow row = ticketMap.get(ticketID);
                row.addValue(DESCRIPTION, map.get("description").toString());
                map.get("tags");
                if (map.get("custom_fields") != null) {
                    List customFields = (List) map.get("custom_fields");
                    for (Object customFieldObj : customFields) {
                        Map customFieldMap = (Map) customFieldObj;
                        String fieldID = customFieldMap.get("id").toString();
                        if (customFieldMap.get("value") != null) {
                            String value = customFieldMap.get("value").toString();
                            Key key = keys.get("zd" + fieldID);
                            row.addValue(key, value);
                        }
                    }
                }

                // parse custom fields and anything else that's missing
            }
            if (ticketObjects.get("next_page") != null) {
                nextPage = ticketObjects.get("next_page").toString();
            } else {
                nextPage = null;
            }
        }
        Set<String> ids = new HashSet<String>();
        for(Map.Entry<String, IRow> entry : ticketMap.entrySet()) {
            if(!"Deleted".equals(entry.getValue().getValue(keys.get(STATUS)).toString()))
                ids.add(entry.getKey());
        }
        zendeskCompositeSource.populateTicketIdList(ids);
        dataStorage.insertData(dataSet);
        return null;
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
                System.out.println(value);
                return new DateValue(df.parse(value));
            } catch (Exception e) {
                LogClass.error(e);
                return new EmptyValue();
            }
        }
        return new EmptyValue();
    }
}
