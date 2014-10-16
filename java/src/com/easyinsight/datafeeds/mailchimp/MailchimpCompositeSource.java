package com.easyinsight.datafeeds.mailchimp;

import com.easyinsight.analysis.DataSourceInfo;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * User: jamesboe
 * Date: 10/14/14
 * Time: 4:01 PM
 */
public class MailchimpCompositeSource extends CompositeServerDataSource {

    private String mailchimpApiKey;

    public MailchimpCompositeSource() {
        setFeedName("MailChimp");
    }

    public void configureFactory(HTMLConnectionFactory factory) {
        factory.addField("MailChimp API Key:", "mailchimpApiKey");
        factory.type(HTMLConnectionFactory.TYPE_BASIC_AUTH);
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.COMPOSITE_PULL;
    }

    public String getMailchimpApiKey() {
        return mailchimpApiKey;
    }

    public void setMailchimpApiKey(String mailchimpApiKey) {
        this.mailchimpApiKey = mailchimpApiKey;
    }

    private transient List<String> campaignIDs;

    public List<String> getCampaignIDs() {
        return campaignIDs;
    }

    public void setCampaignIDs(List<String> campaignIDs) {
        this.campaignIDs = campaignIDs;
    }

    protected List<IServerDataSourceDefinition> sortSources(List<IServerDataSourceDefinition> children) {
        List<IServerDataSourceDefinition> end = new ArrayList<>();
        Set<Integer> set = new HashSet<>();
        children.stream().filter(s -> s.getFeedType().getType() == FeedType.MAILCHIMP_CAMPAIGN.getType()).forEach(s -> {
            set.add(s.getFeedType().getType());
            end.add(s);
        });
        end.addAll(children.stream().filter(s -> !set.contains(s.getFeedType().getType())).collect(Collectors.toList()));
        return end;
    }

    @Override
    protected List<IServerDataSourceDefinition> childDataSources(EIConnection conn) throws Exception {
        List<IServerDataSourceDefinition> defaultChildren = super.childDataSources(conn);
        String dataCenter = mailchimpApiKey.split("-")[1];
        PostMethod postMethod = new PostMethod("https://"+dataCenter+".api.mailchimp.com/2.0/lists/list.json");
        HttpClient httpClient = new HttpClient();
        JSONObject jo = new JSONObject();
        jo.put("apikey", mailchimpApiKey);
        StringRequestEntity entity = new StringRequestEntity(jo.toString(), "application/json", "UTF-8");
        postMethod.setRequestEntity(entity);
        httpClient.executeMethod(postMethod);
        Map map = (Map) new net.minidev.json.parser.JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(postMethod.getResponseBodyAsStream());
        List<Map> data = (List<Map>) map.get("data");
        Map<String, String> ids = new HashMap<>();
        for (Map m : data) {
            String id = m.get("id").toString();
            String name = m.get("name").toString();
            ids.put(id, name);
        }
        for (CompositeFeedNode existing : getCompositeFeedNodes()) {
            if (existing.getDataSourceType() == FeedType.MAILCHIMP_LIST.getType()) {
                FeedDefinition existingSource = new FeedStorage().getFeedDefinitionData(existing.getDataFeedID(), conn);
                MailchimpListResultSource listSource = (MailchimpListResultSource) existingSource;
                ids.remove(listSource.getListID());
            }
        }
        for (Map.Entry<String, String> entry : ids.entrySet()) {
            MailchimpListResultSource source = new MailchimpListResultSource();
            source.setFeedName(entry.getValue());
            source.setListID(entry.getKey());
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
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM MAILCHIMP WHERE DATA_SOURCE_ID = ?");
        deleteStmt.setLong(1, getDataFeedID());
        deleteStmt.executeUpdate();
        deleteStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO MAILCHIMP (DATA_SOURCE_ID, MAILCHIMP_API_KEY) VALUES (?, ?)");
        insertStmt.setLong(1, getDataFeedID());
        insertStmt.setString(2, mailchimpApiKey);
        insertStmt.execute();
        insertStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT MAILCHIMP_API_KEY FROM MAILCHIMP WHERE DATA_SOURCE_ID = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            setMailchimpApiKey(rs.getString(1));
        }
        queryStmt.close();
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> types = new HashSet<>();
        types.add(FeedType.MAILCHIMP_CAMPAIGN_RESULTS);
        types.add(FeedType.MAILCHIMP_CAMPAIGN);
        return types;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.MAILCHIMP_COMPOSITE;
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return new ArrayList<>();
    }
}
