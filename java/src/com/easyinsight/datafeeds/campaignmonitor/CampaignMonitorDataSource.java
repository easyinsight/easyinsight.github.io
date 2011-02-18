package com.easyinsight.datafeeds.campaignmonitor;

import com.easyinsight.analysis.DataSourceInfo;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.users.Account;
import nu.xom.*;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * User: jamesboe
 * Date: 12/27/10
 * Time: 12:12 PM
 */
public class CampaignMonitorDataSource extends CompositeServerDataSource {

    private String cmUserName;
    private String cmPassword;

    private String cmApiKey;
    private String url;

    public CampaignMonitorDataSource() {
        setFeedName("Campaign Monitor");
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.COMPOSITE_PULL;
    }

    public String getUrl() {
        String campaignMonitorURL = ((url.startsWith("http://") || url.startsWith("https://")) ? "" : "http://") + url;
        if(campaignMonitorURL.endsWith("/")) {
            campaignMonitorURL = campaignMonitorURL.substring(0, campaignMonitorURL.length() - 1);
        }
        if(!(campaignMonitorURL.endsWith(".createsend.com")))
            campaignMonitorURL = campaignMonitorURL + ".createsend.com";
        return campaignMonitorURL;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCmApiKey() {
        return cmApiKey;
    }

    public void setCmApiKey(String cmApiKey) {
        this.cmApiKey = cmApiKey;
    }

    public String getCmPassword() {
        return cmPassword;
    }

    public void setCmPassword(String cmPassword) {
        this.cmPassword = cmPassword;
    }

    public String getCmUserName() {
        return cmUserName;
    }

    public void setCmUserName(String cmUserName) {
        this.cmUserName = cmUserName;
    }

    @Override
    public String validateCredentials() {
        try {
            if (cmUserName != null && cmPassword != null) {
                HttpClient client = new HttpClient();
                client.getParams().setAuthenticationPreemptive(true);
                Credentials defaultcreds = new UsernamePasswordCredentials(cmUserName, cmPassword);
                client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
                String siteURL = URLEncoder.encode("http://"+url+".createsend.com/", "UTF-8");
                HttpMethod restMethod = new GetMethod("https://api.createsend.com/api/v3/apikey.xml?siteurl=" + siteURL);
                restMethod.setRequestHeader("Accept", "application/xml");
                restMethod.setRequestHeader("Content-Type", "application/xml");
                client.executeMethod(restMethod);
                Document doc = new Builder().build(restMethod.getResponseBodyAsStream());
                Nodes codes = doc.query("/Result/Code/text()");
                if (codes.size() > 0) {
                    Node code = codes.get(0);
                    String errorCode = code.getValue();
                    if ("401".equals(errorCode)) {
                        return "The username and password were rejected.";
                    }
                }
                doc.query("/Result/ApiKey/text()");
                cmUserName = null;
                cmPassword = null;
            }
            return null;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.CAMPAIGN_MONITOR_COMPOSITE;
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> types = new HashSet<FeedType>();
        types.add(FeedType.CAMPAIGN_MONITOR_CLIENTS);
        /*types.add(FeedType.CAMPAIGN_MONITOR_CAMPAIGNS);
        types.add(FeedType.CAMPAIGN_MONITOR_CAMPAIGN_RESULTS);
        types.add(FeedType.CAMPAIGN_MONITOR_LISTS);*/
        return types;
    }

    @Override
    public void exchangeTokens(EIConnection conn, HttpServletRequest request, String externalPin) throws Exception {
        if (cmUserName != null && cmPassword != null) {
            HttpClient client = new HttpClient();
            client.getParams().setAuthenticationPreemptive(true);
            Credentials defaultcreds = new UsernamePasswordCredentials(cmUserName, cmPassword);
            client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
            String siteURL = URLEncoder.encode("http://"+url+".createsend.com/", "UTF-8");
            HttpMethod restMethod = new GetMethod("https://api.createsend.com/api/v3/apikey.xml?siteurl=" + siteURL);
            restMethod.setRequestHeader("Accept", "application/xml");
            restMethod.setRequestHeader("Content-Type", "application/xml");
            client.executeMethod(restMethod);
            Document doc = new Builder().build(restMethod.getResponseBodyAsStream());
            cmApiKey = doc.query("/Result/ApiKey/text()").get(0).getValue();
            cmUserName = null;
            cmPassword = null;
        }
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT API_KEY, URL FROM CAMPAIGN_MONITOR WHERE data_source_id = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            cmApiKey = rs.getString(1);
            url = rs.getString(2);
        }
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM CAMPAIGN_MONITOR WHERE DATA_SOURCE_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO CAMPAIGN_MONITOR (API_KEY, URL, DATA_SOURCE_ID) VALUES (?, ?, ?)");
        insertStmt.setString(1, cmApiKey);
        insertStmt.setString(2, url);
        insertStmt.setLong(3, getDataFeedID());
        insertStmt.execute();
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return new ArrayList<ChildConnection>();
    }

    public static void main(String[] args) throws Exception {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials("897470a854682f2e2d9f27028a9b95bf", "");
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        HttpMethod restMethod = new GetMethod("http://api.createsend.com/api/v3/clients.xml");
        restMethod.setRequestHeader("Accept", "application/xml");
        restMethod.setRequestHeader("Content-Type", "application/xml");
        client.executeMethod(restMethod);
        System.out.println(restMethod.getResponseBodyAsString());
    }
}
