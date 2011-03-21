package com.easyinsight.datafeeds.batchbook;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataStorage;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 1/17/11
 * Time: 10:13 PM
 */
public class BatchbookTodoSource extends BatchbookBaseSource {
    public static final String TODO_ID = "Todo ID";
    public static final String TODO_TITLE = "Todo Title";
    public static final String TODO_DESCRIPTION = "Todo Description";
    public static final String DUE_DATE = "Todo Due Date";
    public static final String CREATED_AT = "Todo Creation Date";
    public static final String UPDATED_AT = "Todo Update Date";
    public static final String FLAGGED = "Flagged";
    public static final String COMPLETE = "Completed";
    public static final String ASSIGNED_BY = "Todo Assigned By";
    public static final String ASSIGNED_TO = "Todo Assigned To";

    public static final String TAGS = "Todo Tags";
    public static final String TODO_COUNT = "Todo Count";

    public BatchbookTodoSource() {
        setFeedName("Todos");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.BATCHBOOK_TODOS;
    }

    @NotNull
    @Override
    protected List<String> getKeys() {
        return Arrays.asList(TODO_ID, TODO_TITLE, TODO_DESCRIPTION, DUE_DATE, CREATED_AT, UPDATED_AT, FLAGGED, COMPLETE, ASSIGNED_BY,
                ASSIGNED_TO, TAGS, TODO_COUNT);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(TODO_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(TODO_TITLE), true));
        analysisItems.add(new AnalysisDimension(keys.get(TODO_DESCRIPTION), true));
        analysisItems.add(new AnalysisDimension(keys.get(FLAGGED), true));
        analysisItems.add(new AnalysisDimension(keys.get(COMPLETE), true));
        analysisItems.add(new AnalysisDimension(keys.get(ASSIGNED_BY), true));
        analysisItems.add(new AnalysisDimension(keys.get(ASSIGNED_TO), true));
        analysisItems.add(new AnalysisList(keys.get(TAGS), true, ","));
        analysisItems.add(new AnalysisMeasure(keys.get(TODO_COUNT), AggregationTypes.SUM));
        analysisItems.add(new AnalysisDateDimension(keys.get(DUE_DATE), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDateDimension(keys.get(CREATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDateDimension(keys.get(UPDATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        return analysisItems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        DateFormat dueDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        BatchbookCompositeSource batchbookCompositeSource = (BatchbookCompositeSource) parentDefinition;
        HttpClient httpClient = getHttpClient(batchbookCompositeSource.getBbApiKey(), "");
        try {
            Document users = runRestRequest("/service/users.xml", httpClient, new Builder(), batchbookCompositeSource.getUrl(), parentDefinition);
            Nodes userNodes = users.query("/users/user");
            for (int i = 0; i < userNodes.size(); i++) {
                Node userNode = userNodes.get(i);
                String email = queryField(userNode, "email/text()");
                Document deals = runRestRequest("/service/todos.xml?assigned_to=" + email, httpClient, new Builder(), batchbookCompositeSource.getUrl(), parentDefinition);
                Nodes dealNodes = deals.query("/todos/todo");
                for (int j = 0; j < dealNodes.size(); j++) {
                    Node dealNode = dealNodes.get(j);
                    IRow row = dataSet.createRow();
                    row.addValue(keys.get(TODO_ID), queryField(dealNode, "id/text()"));
                    row.addValue(keys.get(TODO_TITLE), queryField(dealNode, "title/text()"));
                    row.addValue(keys.get(TODO_DESCRIPTION), queryField(dealNode, "description/text()"));
                    row.addValue(keys.get(FLAGGED), queryField(dealNode, "flagged/text()"));
                    row.addValue(keys.get(COMPLETE), queryField(dealNode, "complete/text()"));
                    row.addValue(keys.get(ASSIGNED_BY), queryField(dealNode, "assigned_by/text()"));
                    row.addValue(keys.get(ASSIGNED_TO), queryField(dealNode, "assigned_to/text()"));

                    row.addValue(keys.get(TODO_COUNT), 1);
                    row.addValue(keys.get(DUE_DATE), dueDateFormat.parse(queryField(dealNode, "due_date/text()")));
                    row.addValue(keys.get(CREATED_AT), dateFormat.parse(queryField(dealNode, "created_at/text()")));
                    row.addValue(keys.get(UPDATED_AT), dateFormat.parse(queryField(dealNode, "updated_at/text()")));

                    Nodes tagNodes = dealNode.query("tags/tag/name/text()");
                    StringBuilder tagBuilder = new StringBuilder();
                    for (int k = 0; k < tagNodes.size(); k++) {
                        String tag = tagNodes.get(k).getValue();
                        tagBuilder.append(tag).append(",");
                    }
                    if (tagNodes.size() > 0) {
                        tagBuilder.deleteCharAt(tagBuilder.length() - 1);
                    }
                    row.addValue(keys.get(TAGS), tagBuilder.toString());
                }
            }
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dataSet;
    }
}
