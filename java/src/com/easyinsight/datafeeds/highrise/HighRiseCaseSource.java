package com.easyinsight.datafeeds.highrise;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Key;
import com.easyinsight.core.NumericValue;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.users.Token;
import com.easyinsight.users.TokenStorage;
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
 * Date: Sep 2, 2009
 * Time: 11:50:45 PM
 */
public class HighRiseCaseSource extends HighRiseBaseSource {

    public static final String CASE_NAME = "Case Name";
    public static final String CASE_ID = "Case ID";
    public static final String OWNER = "Case Account Owner";
    public static final String CREATED_AT = "Case Created At";
    public static final String UPDATED_AT = "Case Updated At";
    public static final String CLOSED_AT = "Case Closed At";
    public static final String COUNT = "Case Count";
    public static final String AUTHOR = "Case Author";
    public static final String TAGS = "Case Tags";

    public HighRiseCaseSource() {
        setFeedName("Cases");
    }

    @NotNull
    protected List<String> getKeys() {
        return Arrays.asList(CASE_NAME, CASE_ID, OWNER, CREATED_AT, COUNT, AUTHOR, CLOSED_AT, TAGS, UPDATED_AT);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Connection conn) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();        
        analysisItems.add(new AnalysisDimension(keys.get(CASE_NAME), true));
        analysisItems.add(new AnalysisList(keys.get(CASE_ID), false, ","));
        analysisItems.add(new AnalysisDimension(keys.get(OWNER), true));
        analysisItems.add(new AnalysisDimension(keys.get(AUTHOR), true));
        analysisItems.add(new AnalysisList(keys.get(TAGS), false, ","));
        analysisItems.add(new AnalysisDateDimension(keys.get(CREATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDateDimension(keys.get(UPDATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDateDimension(keys.get(CLOSED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        return analysisItems;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HIGHRISE_CASES;
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn) {
        HighRiseCompositeSource highRiseCompositeSource = (HighRiseCompositeSource) parentDefinition;
        String url = highRiseCompositeSource.getUrl();

        DateFormat deadlineFormat = new SimpleDateFormat(XMLDATEFORMAT);

        DataSet ds = new DataSet();
        Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.HIGHRISE_TOKEN, parentDefinition.getDataFeedID(), false, conn);
        HttpClient client = getHttpClient(token.getTokenValue(), "");
        Builder builder = new Builder();
        Map<String, String> peopleCache = new HashMap<String, String>();
        try {
           /* EIPageInfo info = new EIPageInfo();
            info.currentPage = 1;
            do {*/
                loadingProgress(0, 1, "Synchronizing with cases...", true);
                Document companies = runRestRequest("/kases/open.xml", client, builder, url, true, false, parentDefinition);
                Nodes companyNodes = companies.query("/kases/kase");
                for (int i = 0; i < companyNodes.size(); i++) {
                    IRow row = ds.createRow();
                    Node companyNode = companyNodes.get(i);
                    String name = queryField(companyNode, "name/text()");
                    row.addValue(CASE_NAME, name);

                    String id = queryField(companyNode, "id/text()");
                    row.addValue(CASE_ID, id);
                    Date createdAt = deadlineFormat.parse(queryField(companyNode, "created-at/text()"));
                    row.addValue(CREATED_AT, new DateValue(createdAt));
                    String closedAtString = queryField(companyNode, "created-at/text()");
                    if (closedAtString != null) {
                        Date closedAt = deadlineFormat.parse(closedAtString);
                        row.addValue(CLOSED_AT, new DateValue(closedAt));
                    }
                    String updatedAtString = queryField(companyNode, "updated-at/text()");
                    if (updatedAtString != null) {
                        Date updatedAt = deadlineFormat.parse(updatedAtString);
                        row.addValue(UPDATED_AT, new DateValue(updatedAt));
                    }
                    row.addValue(COUNT, new NumericValue(1));
                    String personId = queryField(companyNode, "owner-id/text()");
                    row.addValue(OWNER, retrieveUserInfo(client, builder, peopleCache, personId, url, parentDefinition));
                    String authorID = queryField(companyNode, "author-id/text()");
                    row.addValue(AUTHOR, retrieveUserInfo(client, builder, peopleCache, authorID, url, parentDefinition));

                    Document tags = runRestRequest("/kases/"+id+"/tags.xml", client, builder, url, true, false, parentDefinition);
                    Nodes tagNodes = tags.query("/tags/tag");
                    StringBuilder tagBuilder = new StringBuilder();
                    for (int j = 0; j < tagNodes.size(); j++) {
                        Node tagNode = tagNodes.get(j);
                        String tagName = queryField(tagNode, "name/text()");
                        tagBuilder.append(tagName).append(",");
                    }
                    if (tagBuilder.length() > 0) {
                        String tagString = tagBuilder.substring(0, tagBuilder.length() - 1);
                        row.addValue(TAGS, tagString);
                    }
                }
            //} while(info.currentPage++ < info.MaxPages);

        } catch (ReportException re) {
            throw re;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (parentDefinition.isAdjustDates()) {
            ds = adjustDates(ds);
        }
        return ds;
    }
}
