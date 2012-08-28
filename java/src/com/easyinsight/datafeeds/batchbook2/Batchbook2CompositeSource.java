package com.easyinsight.datafeeds.batchbook2;

import com.easyinsight.analysis.DataSourceInfo;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.users.Account;
import org.apache.commons.httpclient.HttpClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: 7/16/12
 * Time: 9:31 AM
 */
public class Batchbook2CompositeSource extends CompositeServerDataSource {

    private String url;
    private String token;

    public Batchbook2CompositeSource() {
        setFeedName("Batchbook");
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.COMPOSITE_PULL;
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

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String validateCredentials() {
        return null;
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM BATCHBOOK2 WHERE DATA_SOURCE_ID = ?");
        stmt.setLong(1, getDataFeedID());
        stmt.executeUpdate();
        PreparedStatement insert = conn.prepareStatement("INSERT INTO BATCHBOOK2 (URL, AUTH_TOKEN, DATA_SOURCE_ID) VALUES (?, ?, ?)");
        insert.setString(1, url);
        insert.setString(2, token);
        insert.setLong(3, getDataFeedID());
        insert.execute();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement query = conn.prepareStatement("SELECT URL, AUTH_TOKEN FROM BATCHBOOK2 WHERE DATA_SOURCE_ID = ?");
        query.setLong(1, getDataFeedID());
        ResultSet rs = query.executeQuery();
        if (rs.next()) {
            setUrl(rs.getString(1));
            setToken(rs.getString(2));
        }
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.BATCHBOOK2_COMPOSITE;
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> types = new HashSet<FeedType>();
        types.add(FeedType.BATCHBOOK2_COMPANIES);
        types.add(FeedType.BATCHBOOK2_PEOPLE);
        types.add(FeedType.BATCHBOOK2_PHONES);
        types.add(FeedType.BATCHBOOK2_WEBSITES);
        types.add(FeedType.BATCHBOOK2_ADDRESSES);
        types.add(FeedType.BATCHBOOK2_EMAILS);
        return types;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return new ArrayList<ChildConnection>();
    }

    @Override
    protected List<IServerDataSourceDefinition> childDataSources(EIConnection conn) throws Exception {
        List<IServerDataSourceDefinition> defaultChildren = super.childDataSources(conn);
        Map<String, Batchbook2CustomFieldInfo> infos = new Batchbook2CustomFieldCache().getCustomFieldSets(this);
        for (CompositeFeedNode existing : getCompositeFeedNodes()) {
            if (existing.getDataSourceType() == FeedType.BATCHBOOK2_CUSTOM.getType()) {
                FeedDefinition existingSource = new FeedStorage().getFeedDefinitionData(existing.getDataFeedID(), conn);
                Batchbook2CustomFieldSource batchbook2CustomFieldSource = (Batchbook2CustomFieldSource) existingSource;
                infos.remove(batchbook2CustomFieldSource.getCustomFieldID());
                //defaultChildren.add(batchbook2CustomFieldSource);
                break;
            }
        }
        for (Batchbook2CustomFieldInfo info : infos.values()) {
            Batchbook2CustomFieldSource source = new Batchbook2CustomFieldSource();
            source.setFeedName(info.getName());
            source.setCustomFieldID(info.getId());
            newDefinition(source, conn, "", getUploadPolicy());
            CompositeFeedNode node = new CompositeFeedNode();
            node.setDataFeedID(source.getDataFeedID());
            node.setDataSourceType(source.getFeedType().getType());
            getCompositeFeedNodes().add(node);
            defaultChildren.add(source);
        }
        return defaultChildren;
    }

    @Override
    protected Collection<ChildConnection> getLiveChildConnections() {
        return Arrays.asList(new ChildConnection(FeedType.BATCHBOOK2_COMPANIES, FeedType.BATCHBOOK2_PEOPLE, Batchbook2CompanySource.ID, Batchbook2PeopleSource.COMPANY_ID),
                new ChildConnection(FeedType.BATCHBOOK2_COMPANIES, FeedType.BATCHBOOK2_ADDRESSES, Batchbook2CompanySource.ID, Batchbook2AddressSource.COMPANY_ID),
                new ChildConnection(FeedType.BATCHBOOK2_COMPANIES, FeedType.BATCHBOOK2_EMAILS, Batchbook2CompanySource.ID, Batchbook2EmailSource.COMPANY_ID),
                new ChildConnection(FeedType.BATCHBOOK2_COMPANIES, FeedType.BATCHBOOK2_PHONES, Batchbook2CompanySource.ID, Batchbook2PhoneSource.COMPANY_ID),
                new ChildConnection(FeedType.BATCHBOOK2_COMPANIES, FeedType.BATCHBOOK2_WEBSITES, Batchbook2CompanySource.ID, Batchbook2WebsiteSource.COMPANY_ID),
                new ChildConnection(FeedType.BATCHBOOK2_PEOPLE, FeedType.BATCHBOOK2_ADDRESSES, Batchbook2PeopleSource.ID, Batchbook2AddressSource.ID),
                new ChildConnection(FeedType.BATCHBOOK2_PEOPLE, FeedType.BATCHBOOK2_EMAILS, Batchbook2PeopleSource.ID, Batchbook2EmailSource.ID),
                new ChildConnection(FeedType.BATCHBOOK2_PEOPLE, FeedType.BATCHBOOK2_PHONES, Batchbook2PeopleSource.ID, Batchbook2PhoneSource.ID),
                new ChildConnection(FeedType.BATCHBOOK2_PEOPLE, FeedType.BATCHBOOK2_WEBSITES, Batchbook2PeopleSource.ID, Batchbook2WebsiteSource.ID));
    }

    @Override
    protected List<CompositeFeedConnection> getAdditionalConnections() throws SQLException {
        List<CompositeFeedConnection> connections = new ArrayList<CompositeFeedConnection>();
        Map<Long, FeedDefinition> map = new HashMap<Long, FeedDefinition>();
        for (CompositeFeedNode child : getCompositeFeedNodes()) {
            FeedDefinition childDef = new FeedStorage().getFeedDefinitionData(child.getDataFeedID());
            map.put(child.getDataFeedID(), childDef);
        }
        long companyID = 0;
        long partyID = 0;
        for (CompositeFeedNode node : getCompositeFeedNodes()) {
            if (node.getDataSourceType() == FeedType.BATCHBOOK2_COMPANIES.getType()) {
                companyID = node.getDataFeedID();
            } else if (node.getDataSourceType() == FeedType.BATCHBOOK2_PEOPLE.getType()) {
                partyID = node.getDataFeedID();
            }
        }
        for (CompositeFeedNode node : getCompositeFeedNodes()) {
            if (node.getDataSourceType() == FeedType.BATCHBOOK2_CUSTOM.getType()) {
                {
                    FeedDefinition sourceDef = map.get(companyID);
                    FeedDefinition targetDef = map.get(node.getDataFeedID());
                    Key sourceKey = sourceDef.getField(Batchbook2CompanySource.ID);
                    Key targetKey = targetDef.getField(node.getDataSourceName() + " CompanyID");
                    connections.add(new CompositeFeedConnection(companyID, node.getDataFeedID(), sourceKey, targetKey));
                }
                {
                    FeedDefinition sourceDef = map.get(partyID);
                    FeedDefinition targetDef = map.get(node.getDataFeedID());
                    Key sourceKey = sourceDef.getField(Batchbook2PeopleSource.ID);
                    Key targetKey = targetDef.getField(node.getDataSourceName() + " PersonID");
                    connections.add(new CompositeFeedConnection(partyID, node.getDataFeedID(), sourceKey, targetKey));
                }
            }
        }
        return connections;
    }

    private transient BatchbookCache cache;

    public BatchbookCache getOrCreateCache(HttpClient httpClient) throws Exception {
        if (cache == null) {
            cache = new BatchbookCache();
            cache.populate(httpClient, this);
        }
        return cache;
    }

    @Override
    protected void refreshDone() {
        super.refreshDone();
        cache = null;
    }
}
