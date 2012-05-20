package com.easyinsight.datafeeds.batchbook;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
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
public class BatchbookCompanySource extends BatchbookBaseSource {
    public static final String COMPANY_ID = "Company ID";
    public static final String NAME = "Company Name";

    public static final String TAGS = "Company Tags";
    public static final String COMPANY_CREATED_AT = "Company Created At";
    public static final String COMPANY_UPDATED_AT = "Company Updated At";
    public static final String COMPANY_COUNT = "Company Count";

    public BatchbookCompanySource() {
        setFeedName("Companies");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.BATCHBOOK_COMPANIES;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(COMPANY_ID, NAME, TAGS, COMPANY_CREATED_AT, COMPANY_UPDATED_AT, COMPANY_COUNT);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(COMPANY_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(NAME), true));
        analysisItems.add(new AnalysisList(keys.get(TAGS), true, ","));
        analysisItems.add(new AnalysisMeasure(keys.get(COMPANY_COUNT), AggregationTypes.SUM));
        analysisItems.add(new AnalysisDateDimension(keys.get(COMPANY_CREATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDateDimension(keys.get(COMPANY_UPDATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        BatchbookCompositeSource batchbookCompositeSource = (BatchbookCompositeSource) parentDefinition;
        try {
            Map<String, List<String>> superTags = batchbookCompositeSource.getOrCreateSuperTags();
            for (String superTag : superTags.keySet()) {
                String name = "Company " + superTag;
                Key key = keys.get(name);
                if (key == null) {
                    key = new NamedKey(name);
                }
                analysisItems.add(new AnalysisDimension(key));
            }
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return analysisItems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        BatchbookCompositeSource batchbookCompositeSource = (BatchbookCompositeSource) parentDefinition;
        HttpClient httpClient = getHttpClient(batchbookCompositeSource.getBbApiKey(), "");
        try {
            Document deals = runRestRequest("/service/companies.xml?limit=5000", httpClient, new Builder(), batchbookCompositeSource.getUrl(), parentDefinition);
            Nodes dealNodes = deals.query("/companies/company");
            for (int i = 0; i < dealNodes.size(); i++) {
                Node dealNode = dealNodes.get(i);
                IRow row = dataSet.createRow();
                row.addValue(keys.get(COMPANY_ID), queryField(dealNode, "id/text()"));
                row.addValue(keys.get(NAME), queryField(dealNode, "name/text()"));

                row.addValue(keys.get(COMPANY_COUNT), 1);
                row.addValue(keys.get(COMPANY_CREATED_AT), dateFormat.parse(queryField(dealNode, "created_at/text()")));
                row.addValue(keys.get(COMPANY_UPDATED_AT), dateFormat.parse(queryField(dealNode, "updated_at/text()")));

                Nodes tagNodes = dealNode.query("tags/tag");
                StringBuilder tagBuilder = new StringBuilder();
                for (int j = 0; j < tagNodes.size(); j++) {
                    Node tagNode = tagNodes.get(j);
                    String name = queryField(tagNode, "name/text()");
                    boolean superTag = Boolean.parseBoolean(queryField(tagNode, "supertag"));
                    if (superTag) {
                        Map<String, String> map = new HashMap<String, String>();
                        String id = String.valueOf(batchbookCompositeSource.getSuperTagID());
                        map.put("SuperTagRecordID", id);
                        row.addValue(keys.get("Company " + name), id);
                        List<String> fields = batchbookCompositeSource.getOrCreateSuperTags().get(name);
                        for (String field : fields) {
                            String fieldValue = queryField(tagNode, "fields/" + field.replaceAll(" ", "_").toLowerCase() + "/text()");
                            map.put(field, fieldValue);
                        }
                        List<Map<String, String>> list = batchbookCompositeSource.getSuperTagMap().get(name);
                        if (list == null) {
                            list = new ArrayList<Map<String, String>>();
                            batchbookCompositeSource.getSuperTagMap().put(name, list);
                        }
                        list.add(map);
                    } else {
                        tagBuilder.append(name).append(",");
                    }

                }
                if (tagBuilder.length() > 0) {
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
