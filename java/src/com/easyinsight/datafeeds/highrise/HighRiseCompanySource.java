package com.easyinsight.datafeeds.highrise;

import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceMigration;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.users.Token;
import com.easyinsight.users.TokenStorage;
import nu.xom.*;
import org.jetbrains.annotations.NotNull;
import org.apache.commons.httpclient.HttpClient;

import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.Connection;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Key;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.DateValue;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.analysis.*;

/**
 * User: jamesboe
 * Date: Sep 2, 2009
 * Time: 11:50:45 PM
 */
public class HighRiseCompanySource extends HighRiseBaseSource {

    public static final String COMPANY_NAME = "Company Name";
    public static final String COMPANY_ID = "Company ID";
    public static final String TAGS = "Tags";
    public static final String OWNER = "Account Owner";
    public static final String CREATED_AT = "Created At";
    public static final String UPDATED_AT = "Company Updated At";
    public static final String COUNT = "Count";

    public static final String ZIP_CODE = "Company Zip Code";
    public static final String BACKGROUND = "Company Background";

    public HighRiseCompanySource() {
        setFeedName("Company");
    }

    @NotNull
    protected List<String> getKeys() {
        return Arrays.asList(COMPANY_NAME, COMPANY_ID, TAGS, OWNER, CREATED_AT, COUNT, ZIP_CODE, BACKGROUND, UPDATED_AT);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Connection conn) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(COMPANY_NAME), true));
        analysisItems.add(new AnalysisZipCode(keys.get(ZIP_CODE), true));
        analysisItems.add(new AnalysisDimension(keys.get(BACKGROUND), true));
        analysisItems.add(new AnalysisDimension(keys.get(COMPANY_ID), true));
        analysisItems.add(new AnalysisList(keys.get(TAGS), false, ","));
        analysisItems.add(new AnalysisDimension(keys.get(OWNER), true));
        analysisItems.add(new AnalysisDateDimension(keys.get(CREATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDateDimension(keys.get(UPDATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        return analysisItems;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HIGHRISE_COMPANY;
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID) {
        HighRiseCompositeSource highRiseCompositeSource = (HighRiseCompositeSource) parentDefinition;
        String url = highRiseCompositeSource.getUrl();

        DateFormat deadlineFormat = new SimpleDateFormat(XMLDATEFORMAT);

        DataSet ds = new DataSet();
        Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.HIGHRISE_TOKEN, parentDefinition.getDataFeedID(), false, conn);
        HttpClient client = getHttpClient(token.getTokenValue(), "");
        boolean writeDuring = dataStorage != null && !parentDefinition.isAdjustDates();
        Builder builder = new Builder();
        try {
            HighriseCache highriseCache = highRiseCompositeSource.getOrCreateCache(client);
            int offset = 0;
            int companyCount;
            do {
                companyCount = 0;
                Document companies;
                if (offset == 0) {
                    companies = runRestRequest("/companies.xml?", client, builder, url, true, false, parentDefinition);
                } else {
                    companies = runRestRequest("/companies.xml?n=" + offset, client, builder, url, true, false, parentDefinition);
                }
                Nodes companyNodes = companies.query("/companies/company");
                loadingProgress(0, 1, "Synchronizing with companies...", callDataID);
                for (int i = 0; i < companyNodes.size(); i++) {
                    IRow row = ds.createRow();
                    Node companyNode = companyNodes.get(i);
                    String name = queryField(companyNode, "name/text()");
                    row.addValue(COMPANY_NAME, name);

                    String id = queryField(companyNode, "id/text()");
                    row.addValue(COMPANY_ID, id);
                    String background = queryField(companyNode, "background/text()");
                    row.addValue(BACKGROUND, background);

                    Nodes contactDataNodes = companyNode.query("contact-data/addresses/address");
                    if (contactDataNodes.size() > 0) {
                        Node contactDataNode = contactDataNodes.get(0);
                        String zip = queryField(contactDataNode, "zip/text()");
                        row.addValue(ZIP_CODE, zip);
                    }
                    Date createdAt = deadlineFormat.parse(queryField(companyNode, "created-at/text()"));
                    row.addValue(CREATED_AT, new DateValue(createdAt));
                    String updatedAtString = queryField(companyNode, "updated-at/text()");
                    if (updatedAtString != null) {
                        row.addValue(UPDATED_AT, new DateValue(deadlineFormat.parse(updatedAtString)));
                    }
                    row.addValue(COUNT, new NumericValue(1));
                    String personId = queryField(companyNode, "owner-id/text()");
                    String responsiblePartyName = highriseCache.getUserName(personId);
                    row.addValue(OWNER, responsiblePartyName);

                    Nodes tagNodes = companyNode.query("tags/tag/name/text()");
                    if (tagNodes.size() > 0) {
                        StringBuilder tagBuilder = new StringBuilder();
                        for (int j = 0; j < tagNodes.size(); j++) {
                            String tag = tagNodes.get(j).getValue();
                            tagBuilder.append(tag).append(",");
                        }
                        String tagString = tagBuilder.substring(0, tagBuilder.length() - 1);
                        row.addValue(TAGS, tagString);
                    }
                    companyCount++;
                }
                if (writeDuring) {
                    dataStorage.insertData(ds);
                    ds = new DataSet();
                }
                offset += 500;
            } while (companyCount == 500);
        } catch (ReportException re) {
            throw re;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (dataStorage == null) {
            return ds;
        } else {
            return null;
        }
    }

    @Override
    public int getVersion() {
        return 3;
    }

    @Override
    public List<DataSourceMigration> getMigrations() {
        return Arrays.asList(new HighRiseCompany1To2(this), new HighRiseCompany2To3(this));
    }
}
