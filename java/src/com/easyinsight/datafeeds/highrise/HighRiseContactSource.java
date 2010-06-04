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
import com.easyinsight.users.Credentials;
import com.easyinsight.users.Token;
import com.easyinsight.users.TokenStorage;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: Mar 23, 2010
 * Time: 1:35:19 PM
 */
public class HighRiseContactSource extends HighRiseBaseSource {
    public static final String CONTACT_NAME = "Contact Name";
    public static final String CONTACT_ID = "Contact ID";
    public static final String COMPANY_ID = "Company ID";
    public static final String TAGS = "Tags";
    public static final String TITLE = "Title";
    public static final String OWNER = "Contact Owner";
    public static final String CREATED_AT = "Created At";
    public static final String COUNT = "Count";

    public HighRiseContactSource() {
        setFeedName("Contact");
    }

    @NotNull
    protected List<String> getKeys() {
        return Arrays.asList(CONTACT_NAME, COMPANY_ID, TAGS, OWNER, CREATED_AT, COUNT, TITLE, CONTACT_ID);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, com.easyinsight.users.Credentials credentials, Connection conn) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(CONTACT_NAME), true));
        analysisItems.add(new AnalysisDimension(keys.get(CONTACT_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(TITLE), true));
        analysisItems.add(new AnalysisDimension(keys.get(COMPANY_ID), true));
        analysisItems.add(new AnalysisList(keys.get(TAGS), false, ","));
        analysisItems.add(new AnalysisDimension(keys.get(OWNER), true));
        analysisItems.add(new AnalysisDateDimension(keys.get(CREATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        return analysisItems;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HIGHRISE_CONTACTS;
    }

    public DataSet getDataSet(Credentials credentials, Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn) {
        HighRiseCompositeSource highRiseCompositeSource = (HighRiseCompositeSource) parentDefinition;
        String url = highRiseCompositeSource.getUrl();

        DateFormat deadlineFormat = new SimpleDateFormat(XMLDATEFORMAT);

        DataSet ds = new DataSet();
        Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.HIGHRISE_TOKEN, parentDefinition.getDataFeedID(), false, conn);
        if (token == null) {
            token = new Token();
            token.setTokenValue(credentials.getUserName());
            token.setTokenType(TokenStorage.HIGHRISE_TOKEN);
            token.setUserID(SecurityUtil.getUserID());
            new TokenStorage().saveToken(token, parentDefinition.getDataFeedID(), conn);
        } else if (token != null && credentials != null && credentials.getUserName() != null && !"".equals(credentials.getUserName()) &&
                !credentials.getUserName().equals(token.getTokenValue())) {
            token.setTokenValue(credentials.getUserName());
            new TokenStorage().saveToken(token, parentDefinition.getDataFeedID(), conn);
        }
        HttpClient client = getHttpClient(token.getTokenValue(), "");
        Builder builder = new Builder();
        Map<String, String> peopleCache = new HashMap<String, String>();
        try {
            int offset = 0;
            int contactCount;
            Map<String, List<String>> tagMap = new HashMap<String, List<String>>();
            Document tagDoc = runRestRequest("/tags.xml", client, builder, url, true);
            Nodes tagNodes = tagDoc.query("/tags/tag");
            for (int i = 0; i < tagNodes.size(); i++) {
                Node tagNode = tagNodes.get(i);
                String tag = queryField(tagNode, "name/text()");
                String id = queryField(tagNode, "id/text()");
                Document ppl = runRestRequest("/tags/" + id + ".xml", client, builder, url, false);
                Nodes pplNodes = ppl.query("/people/person");
                for (int j = 0; j < pplNodes.size(); j++) {
                    Node person = pplNodes.get(j);
                    String personID = queryField(person, "id/text()");
                    List<String> tags = tagMap.get(personID);
                    if (tags == null) {
                        tags = new ArrayList<String>();
                        tagMap.put(personID, tags);
                    }
                    tags.add(tag);
                }
            }
            do {
                Document companies;
                if (offset == 0) {
                    companies = runRestRequest("/people.xml?", client, builder, url, true);
                } else {
                    companies = runRestRequest("/people.xml?n=" + offset, client, builder, url, true);
                }
                Nodes companyNodes = companies.query("/people/person");
                loadingProgress(0, 1, "Synchronizing with contacts...", true);
                contactCount = 0;
                for (int i = 0; i < companyNodes.size(); i++) {
                    IRow row = ds.createRow();
                    Node companyNode = companyNodes.get(i);
                    String firstName = queryField(companyNode, "first-name/text()");
                    String lastName = queryField(companyNode, "last-name/text()");
                    String name = firstName + " " + lastName;
                    String title = queryField(companyNode, "title/text()");

                    row.addValue(TITLE, title);

                    row.addValue(CONTACT_NAME, name);

                    String id = queryField(companyNode, "id/text()");
                    row.addValue(CONTACT_ID, id);
                    String companyID = queryField(companyNode, "company-id/text()");
                    row.addValue(COMPANY_ID, companyID);
                    Date createdAt = deadlineFormat.parse(queryField(companyNode, "created-at/text()"));
                    row.addValue(CREATED_AT, new DateValue(createdAt));
                    row.addValue(COUNT, new NumericValue(1));
                    String personId = queryField(companyNode, "owner-id/text()");
                    String responsiblePartyName = retrieveUserInfo(client, builder, peopleCache, personId, url);
                    row.addValue(OWNER, responsiblePartyName);

                    List<String> tagList = tagMap.get(id);
                    if (tagList != null) {
                        StringBuilder tagBuilder = new StringBuilder();
                        for (String tag : tagList) {
                            tagBuilder.append(tag).append(",");
                        }
                        String tagString = tagBuilder.substring(0, tagBuilder.length() - 1);
                        row.addValue(TAGS, tagString);
                    }
                    contactCount++;
                }
                offset += 500;
            } while(contactCount == 500);

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return ds;
    }
}
