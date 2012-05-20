package com.easyinsight.datafeeds.batchbook;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.kpi.KPI;
import com.easyinsight.kpi.KPIUtil;
import com.easyinsight.users.Account;
import nu.xom.ParsingException;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: 1/17/11
 * Time: 10:11 PM
 */
public class BatchbookCompositeSource extends CompositeServerDataSource {

    private String url;
    private String bbApiKey;

    public BatchbookCompositeSource() {
        setFeedName("Batchbook");
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBbApiKey() {
        return bbApiKey;
    }

    public void setBbApiKey(String bbApiKey) {
        this.bbApiKey = bbApiKey;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.BATCHBOOK_COMPOSITE;
    }

    @Override
    public String validateCredentials() {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials(bbApiKey, "");
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        String string = getUrl() + "/service/users.xml";
        HttpMethod restMethod = new GetMethod(string);
        restMethod.setRequestHeader("Accept", "application/xml");
        restMethod.setRequestHeader("Content-Type", "application/xml");
        try {
            client.executeMethod(restMethod);
            return null;
        } catch (Exception e) {
            String statusLine = restMethod.getStatusLine().toString();
            if (statusLine.indexOf("401") != -1) {
                return "Your API key was invalid.";
            } else {
                return e.getMessage();
            }
        }
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> feedTypes = new HashSet<FeedType>();
        feedTypes.add(FeedType.BATCHBOOK_DEALS);
        feedTypes.add(FeedType.BATCHBOOK_COMPANIES);
        feedTypes.add(FeedType.BATCHBOOK_PEOPLE);
        feedTypes.add(FeedType.BATCHBOOK_COMMUNICATIONS);
        feedTypes.add(FeedType.BATCHBOOK_USERS);
        feedTypes.add(FeedType.BATCHBOOK_TODOS);
        feedTypes.add(FeedType.BATCHBOOK_COMMUNICATION_PARTIES);
        return feedTypes;
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return new ArrayList<ChildConnection>();
    }

    public String getUrl() {
        String batchbookURL = ((url.startsWith("http://") || url.startsWith("https://")) ? "" : "http://") + url;
        if(batchbookURL.endsWith("/")) {
            batchbookURL = batchbookURL.substring(0, batchbookURL.length() - 1);
        }
        if(!(batchbookURL.endsWith(".batchbook.com")))
            batchbookURL = batchbookURL + ".batchbook.com";
        batchbookURL = batchbookURL.trim();
        return batchbookURL;
    }

    @Override
    protected void refreshDone() {
        super.refreshDone();
        batchbookCommunicationsCache = null;
        superTags = null;
        superTagMap.clear();
        superTagID = 0;
    }

    protected void sortSources(List<IServerDataSourceDefinition> children) {
        Collections.sort(children, new Comparator<IServerDataSourceDefinition>() {

            public int compare(IServerDataSourceDefinition feedDefinition, IServerDataSourceDefinition feedDefinition1) {
                if (feedDefinition.getFeedType().getType() == FeedType.BATCHBOOK_SUPER_TAG.getType()) {
                    return 1;
                }
                return 0;
            }
        });
    }

    private BatchbookCommunicationsCache batchbookCommunicationsCache;

    private Map<String, List<String>> superTags;

    private Map<String, List<Map<String, String>>> superTagMap = new HashMap<String, List<Map<String,String>>>();

    private int superTagID = 0;

    public int getSuperTagID() {
        return superTagID++;
    }

    public Map<String, List<Map<String, String>>> getSuperTagMap() {
        return superTagMap;
    }

    public Map<String, List<String>> getOrCreateSuperTags() throws ParsingException {
        if (superTags == null) {
            superTags = new BatchbookSuperTagRetrieval().getSuperTags(this);
        }
        return superTags;
    }

    public BatchbookCommunicationsCache getOrCreateCache() {
        if (batchbookCommunicationsCache == null) {
            batchbookCommunicationsCache = new BatchbookCommunicationsCache();
            batchbookCommunicationsCache.populate(this);
        }
        return batchbookCommunicationsCache;
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.COMPOSITE_PULL;
    }

    @Override
    public int getVersion() {
        return 2;
    }

    @Override
    public List<DataSourceMigration> getMigrations() {
        return Arrays.asList((DataSourceMigration) new BatchbookComposite1To2(this));
    }

    @Override
    protected Collection<ChildConnection> getLiveChildConnections() {
        return Arrays.asList(new ChildConnection(FeedType.BATCHBOOK_DEALS, FeedType.BATCHBOOK_COMPANIES, BatchbookDealSource.DEAL_WITH_ID, BatchbookCompanySource.COMPANY_ID),
            new ChildConnection(FeedType.BATCHBOOK_COMPANIES, FeedType.BATCHBOOK_PEOPLE, BatchbookCompanySource.COMPANY_ID, BatchbookPeopleSource.COMPANY_ID),
                new ChildConnection(FeedType.BATCHBOOK_COMMUNICATIONS, FeedType.BATCHBOOK_COMMUNICATION_PARTIES, BatchbookCommunicationsSource.COMMUNICATION_ID, BatchbookCommunicationsPartySource.COMMUNICATION_ID),
                new ChildConnection(FeedType.BATCHBOOK_PEOPLE, FeedType.BATCHBOOK_COMMUNICATION_PARTIES, BatchbookPeopleSource.PERSON_ID, BatchbookCommunicationsPartySource.CONTACT_ID));
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM BATCHBOOK WHERE DATA_SOURCE_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO BATCHBOOK (DATA_SOURCE_ID, URL, API_KEY) VALUES (?, ?, ?)");
        insertStmt.setLong(1, getDataFeedID());
        insertStmt.setString(2, url);
        insertStmt.setString(3, bbApiKey);
        insertStmt.execute();
    }

    protected List<CompositeFeedConnection> getAdditionalConnections() throws SQLException {
        Map<Long, FeedDefinition> map = new HashMap<Long, FeedDefinition>();
        for (CompositeFeedNode child : getCompositeFeedNodes()) {
            FeedDefinition childDef = new FeedStorage().getFeedDefinitionData(child.getDataFeedID());
            map.put(child.getDataFeedID(), childDef);
        }
        List<CompositeFeedConnection> connections = new ArrayList<CompositeFeedConnection>();
        long dealID = 0;
        long companyID = 0;
        long partyID = 0;
        for (CompositeFeedNode node : getCompositeFeedNodes()) {
            if (node.getDataSourceType() == FeedType.BATCHBOOK_DEALS.getType()) {
                dealID = node.getDataFeedID();
            } else if (node.getDataSourceType() == FeedType.BATCHBOOK_COMPANIES.getType()) {
                companyID = node.getDataFeedID();
            } else if (node.getDataSourceType() == FeedType.BATCHBOOK_PEOPLE.getType()) {
                partyID = node.getDataFeedID();
            }
        }
        for (CompositeFeedNode node : getCompositeFeedNodes()) {
            if (node.getDataSourceType() == FeedType.BATCHBOOK_SUPER_TAG.getType()) {
                {
                    FeedDefinition sourceDef = map.get(dealID);
                    FeedDefinition targetDef = map.get(node.getDataFeedID());
                    Key sourceKey = sourceDef.getField("Deal " + node.getDataSourceName());
                    Key targetKey = targetDef.getField(node.getDataSourceName() + " Record ID");
                    connections.add(new CompositeFeedConnection(dealID, node.getDataFeedID(), sourceKey, targetKey));
                }
                {
                    FeedDefinition sourceDef = map.get(companyID);
                    FeedDefinition targetDef = map.get(node.getDataFeedID());
                    Key sourceKey = sourceDef.getField("Company " + node.getDataSourceName());
                    Key targetKey = targetDef.getField(node.getDataSourceName() + " Record ID");
                    connections.add(new CompositeFeedConnection(companyID, node.getDataFeedID(), sourceKey, targetKey));
                }
                {
                    FeedDefinition sourceDef = map.get(partyID);
                    FeedDefinition targetDef = map.get(node.getDataFeedID());
                    Key sourceKey = sourceDef.getField("Party " + node.getDataSourceName());
                    Key targetKey = targetDef.getField(node.getDataSourceName() + " Record ID");
                    connections.add(new CompositeFeedConnection(partyID, node.getDataFeedID(), sourceKey, targetKey));
                }
            }
        }
        return connections;
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT URL, API_KEY FROM BATCHBOOK WHERE DATA_SOURCE_ID = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        rs.next();
        setUrl(rs.getString(1));
        setBbApiKey(rs.getString(2));
    }

    @Override
    protected List<IServerDataSourceDefinition> obtainChildDataSources(EIConnection conn) throws Exception {
        List<IServerDataSourceDefinition> defaultChildren = super.obtainChildDataSources(conn);
        Map<String, List<String>> superTags = new BatchbookSuperTagRetrieval().getSuperTags(this);
        for (IServerDataSourceDefinition existing : defaultChildren) {
            superTags.remove(existing.getFeedName());
        }
        for (String remainingTag : superTags.keySet()) {
            BatchbookSuperTagSource source = new BatchbookSuperTagSource();
            source.setFeedName(remainingTag);
            newDefinition(source, conn, "", getUploadPolicy());
            CompositeFeedNode node = new CompositeFeedNode();
            node.setDataFeedID(source.getDataFeedID());
            getCompositeFeedNodes().add(node);
            defaultChildren.add(source);
        }
        return defaultChildren;
    }

    @Override
    public FeedDefinition clone(Connection conn) throws CloneNotSupportedException, SQLException {
        BatchbookCompositeSource batchbookCompositeSource = (BatchbookCompositeSource) super.clone(conn);
        batchbookCompositeSource.setUrl(null);
        batchbookCompositeSource.setBbApiKey(null);
        return batchbookCompositeSource;
    }

    @Override
    public List<KPI> createKPIs() {
        List<KPI> kpis = new ArrayList<KPI>();
        kpis.add(KPIUtil.createKPIWithFilters("Pipeline Value", "credit_card.png", (AnalysisMeasure) findAnalysisItem(BatchbookDealSource.AMOUNT),
                Arrays.asList((FilterDefinition) new FilterValueDefinition(findAnalysisItem(BatchbookDealSource.STATUS), true, Arrays.asList((Object) "pending"))), KPI.GOOD, 90));
        kpis.add(KPIUtil.createKPIForDateFilter("Deals Created in the Last 90 Days", "document.png", (AnalysisMeasure) findAnalysisItem(BatchbookDealSource.DEAL_COUNT),
                (AnalysisDimension) findAnalysisItem(BatchbookDealSource.DEAL_CREATED_AT), MaterializedRollingFilterDefinition.QUARTER, new ArrayList<FilterDefinition>(), KPI.GOOD, 90));
        kpis.add(KPIUtil.createKPIForDateFilter("Contacts Created in the Last 90 Days", "user.png", (AnalysisMeasure) findAnalysisItem(BatchbookPeopleSource.PERSON_COUNT),
                (AnalysisDimension) findAnalysisItem(BatchbookPeopleSource.PERSON_CREATED_AT), MaterializedRollingFilterDefinition.QUARTER, new ArrayList<FilterDefinition>(), KPI.GOOD, 90));
        kpis.add(KPIUtil.createKPIWithFilters("Open Todo Items", "inbox.png", (AnalysisMeasure) findAnalysisItem(BatchbookTodoSource.TODO_COUNT),
                Arrays.asList((FilterDefinition) new FilterValueDefinition(findAnalysisItem(BatchbookTodoSource.COMPLETE), true, Arrays.asList((Object) "false"))), KPI.GOOD, 90));
        kpis.add(KPIUtil.createKPIForDateFilter("Deals Won in the Last 90 Days", "symbol_dollar.png", (AnalysisMeasure) findAnalysisItem(BatchbookDealSource.AMOUNT),
                (AnalysisDimension) findAnalysisItem(BatchbookDealSource.DEAL_UPDATED_AT), MaterializedRollingFilterDefinition.QUARTER,
                Arrays.asList((FilterDefinition) new FilterValueDefinition(findAnalysisItem(BatchbookDealSource.STATUS), true, Arrays.asList((Object) "won"))), KPI.GOOD, 90));
        return kpis;
    }
}
