package com.easyinsight.datafeeds.zendesk;

import com.easyinsight.analysis.*;
import com.easyinsight.core.*;
import com.easyinsight.core.StringValue;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.storage.IWhere;
import com.easyinsight.storage.StringWhere;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
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
public class ZendeskCommentSource extends ZendeskBaseSource {

    public static final String COMMENT_TICKET_ID = "Comment Ticket ID";
    public static final String AUTHOR = "Comment Author";
    public static final String COMMENT_BODY = "Comment Body";
    public static final String COMMENT_CREATED_AT = "Comment Created At";
    public static final String COUNT = "Comment Count";

    public ZendeskCommentSource() {
        setFeedName("Comments");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.ZENDESK_COMMENTS;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return new ArrayList<String>(Arrays.asList(COMMENT_TICKET_ID, COUNT, AUTHOR, COMMENT_BODY, COMMENT_CREATED_AT));
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(COMMENT_TICKET_ID), true));
        items.add(new AnalysisDimension(keys.get(AUTHOR), true));
        items.add(new AnalysisDateDimension(keys.get(COMMENT_CREATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        items.add(new AnalysisText(keys.get(COMMENT_BODY)));
        items.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        return items;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {

            ZendeskCompositeSource zendeskCompositeSource = (ZendeskCompositeSource) parentDefinition;
            /*if(!zendeskCompositeSource.isLoadComments())
                return new DataSet();*/
            HttpClient httpClient = getHttpClient(zendeskCompositeSource);
            ZendeskUserCache zendeskUserCache = zendeskCompositeSource.getOrCreateUserCache(httpClient);
            return getAllTickets(keys, zendeskCompositeSource, zendeskUserCache, IDataStorage, lastRefreshDate);

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

    private DataSet getAllTickets(Map<String, Key> keys, ZendeskCompositeSource zendeskCompositeSource, ZendeskUserCache userCache, IDataStorage dataStorage, Date lastRefresh) throws Exception {

        Key noteKey = zendeskCompositeSource.getField(COMMENT_TICKET_ID).toBaseKey();

        List<Comment> comments = zendeskCompositeSource.getComments();
        Map<String, List<Comment>> map = new HashMap<String, List<Comment>>();
        for (Comment comment : comments) {
            List<Comment> commentList = map.get(comment.getCommentTicketID());
            if (commentList == null) {
                commentList = new ArrayList<Comment>();
                map.put(comment.getCommentTicketID(), commentList);
            }
            commentList.add(comment);
        }

        DataSet dataSet = new DataSet();

        for (Map.Entry<String, List<Comment>> entry : map.entrySet()) {
            String commentID = entry.getKey();
            List<Comment> commentList = entry.getValue();

            for (Comment comment : commentList) {
                IRow row = dataSet.createRow();
                row.addValue(COMMENT_TICKET_ID, comment.getCommentTicketID());
                row.addValue(COMMENT_BODY, comment.getCommentBody());
                row.addValue(COMMENT_CREATED_AT, new DateValue(comment.getCreatedAt()));
                row.addValue(AUTHOR, comment.getCommentAuthor());
                row.addValue(COUNT, 1);
            }

            if (lastRefresh != null) {
                StringWhere userWhere = new StringWhere(noteKey, commentID);
                dataStorage.updateData(dataSet, Arrays.asList((IWhere) userWhere));
                dataSet = new DataSet();
            }


        }

        if (lastRefresh == null) {
            dataStorage.insertData(dataSet);
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

    protected Value queryDate(Node node, String target) throws ParseException {
        String value = queryField(node, target);
        if (value != null && !"".equals(value)) {
            try {
                return new DateValue(javax.xml.bind.DatatypeConverter.parseDateTime(value).getTime());
            } catch (Exception e) {
                LogClass.error(e);
                return new EmptyValue();
            }
        }
        return new EmptyValue();
    }

    @Override
    protected String getUpdateKeyName() {
        return COMMENT_TICKET_ID;
    }
}