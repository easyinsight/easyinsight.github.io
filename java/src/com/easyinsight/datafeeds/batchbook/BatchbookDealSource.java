package com.easyinsight.datafeeds.batchbook;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
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
 * Time: 10:12 PM
 */
public class BatchbookDealSource extends BatchbookBaseSource {

    public static final String DEAL_ID = "Deal ID";
    public static final String DEAL_TITLE = "Deal Title";
    public static final String DEAL_DESCRIPTION = "Deal Description";
    public static final String STATUS = "Deal Status";
    public static final String ASSIGNED_TO = "Deal Assigned To";
    public static final String AMOUNT = "Deal Amount";
    public static final String TAGS = "Deal Tags";
    public static final String DEAL_CREATED_AT = "Deal Created At";
    public static final String DEAL_UPDATED_AT = "Deal Updated At";
    public static final String DEAL_COUNT = "Deal Count";
    public static final String DEAL_WITH_ID = "Deal With ID";

    public BatchbookDealSource() {
        setFeedName("Deals");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.BATCHBOOK_DEALS;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(DEAL_ID, DEAL_TITLE, STATUS, ASSIGNED_TO, AMOUNT, TAGS, DEAL_CREATED_AT, DEAL_UPDATED_AT, DEAL_COUNT, DEAL_WITH_ID,
                DEAL_DESCRIPTION);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(DEAL_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(DEAL_TITLE), true));
        analysisItems.add(new AnalysisDimension(keys.get(DEAL_DESCRIPTION), true));
        analysisItems.add(new AnalysisDimension(keys.get(STATUS), true));
        analysisItems.add(new AnalysisDimension(keys.get(ASSIGNED_TO), true));
        AnalysisDimension dealWith = new AnalysisDimension(keys.get(DEAL_WITH_ID), true);
        dealWith.setHidden(true);
        analysisItems.add(dealWith);
        analysisItems.add(new AnalysisList(keys.get(TAGS), true, ","));
        analysisItems.add(new AnalysisMeasure(keys.get(AMOUNT), AMOUNT, AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY));
        analysisItems.add(new AnalysisMeasure(keys.get(DEAL_COUNT), AggregationTypes.SUM));
        analysisItems.add(new AnalysisDateDimension(keys.get(DEAL_CREATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDateDimension(keys.get(DEAL_UPDATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        return analysisItems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        BatchbookCompositeSource batchbookCompositeSource = (BatchbookCompositeSource) parentDefinition;
        HttpClient httpClient = getHttpClient(batchbookCompositeSource.getBbApiKey(), "");
        try {
            Document deals = runRestRequest("/service/deals.xml?limit=5000", httpClient, new Builder(), batchbookCompositeSource.getUrl(), parentDefinition);
            Nodes dealNodes = deals.query("/deals/deal");
            for (int i = 0; i < dealNodes.size(); i++) {
                Node dealNode = dealNodes.get(i);
                IRow row = dataSet.createRow();
                row.addValue(keys.get(DEAL_ID), queryField(dealNode, "id/text()"));
                row.addValue(keys.get(DEAL_TITLE), queryField(dealNode, "title/text()"));
                row.addValue(keys.get(DEAL_DESCRIPTION), queryField(dealNode, "description/text()"));
                row.addValue(keys.get(DEAL_WITH_ID), queryField(dealNode, "deal_with_id/text()"));
                row.addValue(keys.get(ASSIGNED_TO), queryField(dealNode, "assigned_to/text()"));
                row.addValue(keys.get(STATUS), queryField(dealNode, "status/text()"));
                row.addValue(keys.get(AMOUNT), queryField(dealNode, "amount/text()"));
                row.addValue(keys.get(DEAL_COUNT), 1);
                row.addValue(keys.get(DEAL_CREATED_AT), dateFormat.parse(queryField(dealNode, "created_at/text()")));
                row.addValue(keys.get(DEAL_UPDATED_AT), dateFormat.parse(queryField(dealNode, "updated_at/text()")));
                Nodes tagNodes = dealNode.query("tags/tag/name/text()");
                StringBuilder tagBuilder = new StringBuilder();
                for (int j = 0; j < tagNodes.size(); j++) {
                    String tag = tagNodes.get(j).getValue();
                    tagBuilder.append(tag).append(",");
                }
                if (tagNodes.size() > 0) {
                    tagBuilder.deleteCharAt(tagBuilder.length() - 1);
                }
                row.addValue(keys.get(TAGS), tagBuilder.toString());
            }
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dataSet;
    }
}
