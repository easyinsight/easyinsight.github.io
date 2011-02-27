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
public class BatchbookCommunicationsSource extends BatchbookBaseSource {
    public static final String COMMUNICATION_ID = "Communication ID";
    public static final String SUBJECT = "Subject";
    public static final String BODY = "Body";
    public static final String DATE = "Communication Date";
    public static final String TYPE = "Communication Type";

    public static final String TAGS = "Communication Tags";
    public static final String COMMUNICATION_COUNT = "Communication Count";

    public BatchbookCommunicationsSource() {
        setFeedName("Communications");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.BATCHBOOK_COMMUNICATIONS;
    }

    @NotNull
    @Override
    protected List<String> getKeys() {
        return Arrays.asList(COMMUNICATION_ID, SUBJECT, BODY, DATE, TYPE, TAGS, COMMUNICATION_COUNT);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Connection conn) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(COMMUNICATION_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(SUBJECT), true));
        analysisItems.add(new AnalysisDimension(keys.get(BODY), true));
        analysisItems.add(new AnalysisDimension(keys.get(TYPE), true));
        analysisItems.add(new AnalysisList(keys.get(TAGS), true, ","));
        analysisItems.add(new AnalysisMeasure(keys.get(COMMUNICATION_COUNT), AggregationTypes.SUM));
        analysisItems.add(new AnalysisDateDimension(keys.get(DATE), true, AnalysisDateDimension.DAY_LEVEL));
        return analysisItems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        BatchbookCompositeSource batchbookCompositeSource = (BatchbookCompositeSource) parentDefinition;
        HttpClient httpClient = getHttpClient(batchbookCompositeSource.getBbApiKey(), "");
        try {
            Document deals = runRestRequest("/service/communications.xml?limit=5000", httpClient, new Builder(), batchbookCompositeSource.getUrl(), parentDefinition);
            Nodes dealNodes = deals.query("/communications/communication");
            for (int i = 0; i < dealNodes.size(); i++) {
                Node dealNode = dealNodes.get(i);
                IRow row = dataSet.createRow();
                row.addValue(keys.get(COMMUNICATION_ID), queryField(dealNode, "id/text()"));
                row.addValue(keys.get(SUBJECT), queryField(dealNode, "subject/text()"));
                row.addValue(keys.get(BODY), queryField(dealNode, "body/text()"));
                row.addValue(keys.get(TYPE), queryField(dealNode, "ctype/text()"));

                row.addValue(keys.get(COMMUNICATION_COUNT), 1);
                row.addValue(keys.get(DATE), dateFormat.parse(queryField(dealNode, "date/text()")));

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
