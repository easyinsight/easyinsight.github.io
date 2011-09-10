package com.easyinsight.datafeeds.highrise;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.users.Token;
import com.easyinsight.users.TokenStorage;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: Sep 2, 2009
 * Time: 11:50:45 PM
 */
public class HighRiseCaseJoinSource extends HighRiseBaseSource {

    public static final String CASE_ID = "Join Case ID";
    public static final String COMPANY_ID = "Join Company ID";
    public static final String CONTACT_ID = "Join Contact ID";

    public HighRiseCaseJoinSource() {
        setFeedName("Case Join");
    }

    @NotNull
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(CASE_ID, COMPANY_ID, CONTACT_ID);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();        
        analysisItems.add(new AnalysisDimension(keys.get(CASE_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(COMPANY_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(CONTACT_ID), true));
        return analysisItems;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HIGHRISE_CASE_JOIN;
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) {
        HighRiseCompositeSource highRiseCompositeSource = (HighRiseCompositeSource) parentDefinition;
        String url = highRiseCompositeSource.getUrl();

        DataSet ds = new DataSet();
        Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.HIGHRISE_TOKEN, parentDefinition.getDataFeedID(), false, conn);
        HttpClient client = getHttpClient(token.getTokenValue(), "");
        Builder builder = new Builder();
        try {
                loadingProgress(0, 1, "Synchronizing with cases...", callDataID);
                Document companies = runRestRequest("/kases/open.xml", client, builder, url, true, true, parentDefinition);
                Nodes companyNodes = companies.query("/kases/kase");
                for (int i = 0; i < companyNodes.size(); i++) {

                    Node companyNode = companyNodes.get(i);
                    String id = queryField(companyNode, "id/text()");
                    Nodes parties = companyNode.query("parties/party");
                    for (int j = 0; j < parties.size(); j++) {
                        IRow row = ds.createRow();
                        row.addValue(CASE_ID, id);
                        Node partyNode = parties.get(j);
                        String partyID = queryField(partyNode, "id/text()");
                        String partyType = queryField(partyNode, "type/text()");
                        if ("Company".equals(partyType)) {
                            row.addValue(COMPANY_ID, partyID);
                        } else if ("Person".equals(partyType)) {
                            row.addValue(CONTACT_ID, partyID);
                        }
                    }
                }
                companies = runRestRequest("/kases/closed.xml", client, builder, url, true, false, parentDefinition);
                companyNodes = companies.query("/kases/kase");
                for (int i = 0; i < companyNodes.size(); i++) {
                    Node companyNode = companyNodes.get(i);
                    String id = queryField(companyNode, "id/text()");
                    Nodes parties = companyNode.query("parties/party");
                    for (int j = 0; j < parties.size(); j++) {
                        IRow row = ds.createRow();
                        row.addValue(CASE_ID, id);
                        Node partyNode = parties.get(j);
                        String partyID = queryField(partyNode, "id/text()");
                        String partyType = queryField(partyNode, "type/text()");
                        if ("Company".equals(partyType)) {
                            row.addValue(COMPANY_ID, partyID);
                        } else if ("Person".equals(partyType)) {
                            row.addValue(CONTACT_ID, partyID);
                        }
                    }
                }
        } catch (ReportException re) {
            throw re;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return ds;
    }
}
