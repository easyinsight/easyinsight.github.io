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
public class BatchbookPeopleSource extends BatchbookBaseSource {
    public static final String PERSON_ID = "Person ID";
    public static final String PERSON_TITLE = "Person Title";
    public static final String FIRST_NAME = "First Name";
    public static final String LAST_NAME = "Last Name";
    public static final String NAME = "Name";
    public static final String COMPANY_ID = "Person Company ID";

    public static final String TAGS = "Person Tags";
    public static final String PERSON_CREATED_AT = "Person Created At";
    public static final String PERSON_UPDATED_AT = "Person Updated At";
    public static final String PERSON_COUNT = "Person Count";

    public BatchbookPeopleSource() {
        setFeedName("People");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.BATCHBOOK_PEOPLE;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(PERSON_ID, PERSON_TITLE, FIRST_NAME, LAST_NAME, NAME, TAGS, PERSON_CREATED_AT, PERSON_UPDATED_AT, PERSON_COUNT, COMPANY_ID);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(PERSON_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(PERSON_TITLE), true));
        analysisItems.add(new AnalysisDimension(keys.get(FIRST_NAME), true));
        analysisItems.add(new AnalysisDimension(keys.get(LAST_NAME), true));
        analysisItems.add(new AnalysisDimension(keys.get(NAME), true));
        AnalysisDimension dealWith = new AnalysisDimension(keys.get(COMPANY_ID), true);
        dealWith.setHidden(true);
        analysisItems.add(dealWith);
        analysisItems.add(new AnalysisList(keys.get(TAGS), true, ","));
        analysisItems.add(new AnalysisMeasure(keys.get(PERSON_COUNT), AggregationTypes.SUM));
        analysisItems.add(new AnalysisDateDimension(keys.get(PERSON_CREATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDateDimension(keys.get(PERSON_UPDATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        return analysisItems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        BatchbookCompositeSource batchbookCompositeSource = (BatchbookCompositeSource) parentDefinition;
        HttpClient httpClient = getHttpClient(batchbookCompositeSource.getBbApiKey(), "");
        try {
            Document deals = runRestRequest("/service/people.xml?limit=5000", httpClient, new Builder(), batchbookCompositeSource.getUrl(), parentDefinition);
            Nodes dealNodes = deals.query("/people/person");
            for (int i = 0; i < dealNodes.size(); i++) {
                Node dealNode = dealNodes.get(i);
                IRow row = dataSet.createRow();
                row.addValue(keys.get(PERSON_ID), queryField(dealNode, "id/text()"));
                row.addValue(keys.get(COMPANY_ID), queryField(dealNode, "company_id/text()"));
                row.addValue(keys.get(PERSON_TITLE), queryField(dealNode, "title/text()"));
                row.addValue(keys.get(FIRST_NAME), queryField(dealNode, "first_name/text()"));
                row.addValue(keys.get(LAST_NAME), queryField(dealNode, "last_name/text()"));
                row.addValue(keys.get(NAME), queryField(dealNode, "first_name/text()") + " " + queryField(dealNode, "last_name/text()"));

                row.addValue(keys.get(PERSON_COUNT), 1);
                row.addValue(keys.get(PERSON_CREATED_AT), dateFormat.parse(queryField(dealNode, "created_at/text()")));
                row.addValue(keys.get(PERSON_UPDATED_AT), dateFormat.parse(queryField(dealNode, "updated_at/text()")));

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
