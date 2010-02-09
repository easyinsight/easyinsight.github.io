package com.easyinsight.datafeeds.highrise;

import com.easyinsight.storage.DataStorage;
import com.easyinsight.users.Credentials;
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
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Nodes;
import nu.xom.Node;

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
    public static final String COUNT = "Count";

    public HighRiseCompanySource() {
        setFeedName("Company");
    }

    @NotNull
    protected List<String> getKeys() {
        return Arrays.asList(COMPANY_NAME, COMPANY_ID, TAGS, OWNER, CREATED_AT, COUNT);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, com.easyinsight.users.Credentials credentials, Connection conn) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(COMPANY_NAME), true));
        analysisItems.add(new AnalysisDimension(keys.get(COMPANY_ID), true));
        analysisItems.add(new AnalysisList(keys.get(TAGS), false, ","));
        analysisItems.add(new AnalysisDimension(keys.get(OWNER), true));
        analysisItems.add(new AnalysisDateDimension(keys.get(CREATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        return analysisItems;
    }

    private String retrieveContactInfo(HttpClient client, Builder builder, Map<String, String> peopleCache, String contactId, String url) throws HighRiseLoginException {
        try {
            String contactName = null;
            if(contactId != null) {
                contactName = peopleCache.get(contactId);
                if(contactName == null) {
                    Document contactInfo = runRestRequest("/people/person/" + contactId, client, builder, url, null);
                    contactName = queryField(contactInfo, "/person/first-name/text()") + " " + queryField(contactInfo, "/person/last-name/text()");
                    peopleCache.put(contactId, contactName);
                }

            }
            return contactName;
        } catch (HighRiseLoginException e) {
            return "";
        }
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HIGHRISE_COMPANY;
    }

    public DataSet getDataSet(Credentials credentials, Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage) {
        HighRiseCompositeSource highRiseCompositeSource = (HighRiseCompositeSource) parentDefinition;
        String url = highRiseCompositeSource.getUrl();

        DateFormat deadlineFormat = new SimpleDateFormat(XMLDATEFORMAT);

        DataSet ds = new DataSet();
        HttpClient client = getHttpClient(credentials.getUserName(), credentials.getPassword());
        Builder builder = new Builder();
        Map<String, String> peopleCache = new HashMap<String, String>();
        try {
           /* EIPageInfo info = new EIPageInfo();
            info.currentPage = 1;
            do {*/
                Document companies = runRestRequest("/companies.xml", client, builder, url, null);
                Nodes companyNodes = companies.query("/companies/company");
                for (int i = 0; i < companyNodes.size(); i++) {
                    IRow row = ds.createRow();
                    Node companyNode = companyNodes.get(i);
                    String name = queryField(companyNode, "name/text()");
                    row.addValue(COMPANY_NAME, name);

                    String id = queryField(companyNode, "id/text()");
                    row.addValue(COMPANY_ID, id);
                    Date createdAt = deadlineFormat.parse(queryField(companyNode, "created-at/text()"));
                    row.addValue(CREATED_AT, new DateValue(createdAt));
                    row.addValue(COUNT, new NumericValue(1));
                    String personId = queryField(companyNode, "owner-id/text()");
                    String responsiblePartyName = retrieveContactInfo(client, builder, peopleCache, personId, url);
                    row.addValue(OWNER, responsiblePartyName);

                    Document tags = runRestRequest("/companies/"+id+"/tags.xml", client, builder, url, null);
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

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return ds;
    }
}
