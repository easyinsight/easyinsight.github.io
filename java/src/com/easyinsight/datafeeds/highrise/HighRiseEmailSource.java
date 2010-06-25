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
 * Date: Mar 23, 2010
 * Time: 1:35:19 PM
 */
public class HighRiseEmailSource extends HighRiseBaseSource {
    public static final String EMAIL_AUTHOR = "Email Author";
    public static final String EMAIL_CONTACT_ID = "Email Contact ID";
    public static final String SENT_AT = "Email Sent At";
    public static final String COUNT = "Email Count";

    public HighRiseEmailSource() {
        setFeedName("Email");
    }

    @NotNull
    protected List<String> getKeys() {
        return Arrays.asList(EMAIL_AUTHOR, EMAIL_CONTACT_ID, SENT_AT, COUNT);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Credentials credentials, Connection conn) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(EMAIL_AUTHOR), true));
        analysisItems.add(new AnalysisDimension(keys.get(EMAIL_CONTACT_ID), true));        
        analysisItems.add(new AnalysisDateDimension(keys.get(SENT_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        return analysisItems;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HIGHRISE_EMAILS;
    }

    public DataSet getDataSet(Credentials credentials, Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn) {
        HighRiseCompositeSource highRiseCompositeSource = (HighRiseCompositeSource) parentDefinition;

        String url = highRiseCompositeSource.getUrl();

        DateFormat deadlineFormat = new SimpleDateFormat(XMLDATEFORMAT);

        DataSet ds = new DataSet();
        if (!highRiseCompositeSource.isIncludeEmails()) {
            return ds;
        }
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
        try {
            HighriseCache highriseCache = highRiseCompositeSource.getOrCreateCache(client);
            int offset = 0;
            int contactCount;
            do {
                Document companies;
                if (offset == 0) {
                    companies = runRestRequest("/people.xml?", client, builder, url, true, false);
                } else {
                    companies = runRestRequest("/people.xml?n=" + offset, client, builder, url, true, false);
                }
                Nodes companyNodes = companies.query("/people/person");
                loadingProgress(0, 1, "Synchronizing with emails...", true);
                contactCount = 0;
                for (int i = 0; i < companyNodes.size(); i++) {
                    Node companyNode = companyNodes.get(i);
                    String id = queryField(companyNode, "id/text()");
                    String name = queryField(companyNode, "last-name/text()");
                    System.out.println(name);
                    Document emails = runRestRequest("/people/" + id + "/emails.xml", client, builder, url, false, false);
                    Nodes emailNodes = emails.query("/emails/email");
                    for (int j = 0; j < emailNodes.size(); j++) {
                        Node emailNode = emailNodes.get(j);
                        IRow row = ds.createRow();
                        String authorID = queryField(emailNode, "author-id");
                        String authorName = highriseCache.getUserName(authorID);
                        String createdAtString = queryField(emailNode, "created-at");
                        Date createdAt = deadlineFormat.parse(createdAtString);
                        row.addValue(EMAIL_AUTHOR, authorName);
                        row.addValue(EMAIL_CONTACT_ID, id);
                        row.addValue(SENT_AT, new DateValue(createdAt));
                        row.addValue(COUNT, 1);
                    }
                }
                offset += 500;
                if (dataStorage != null) {
                    dataStorage.insertData(ds);
                    ds = new DataSet();
                }
            } while(contactCount == 500);
            
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (dataStorage == null) {
            return ds;
        } else {
            return null;
        }
    }
}
