package com.easyinsight.datafeeds.constantcontact;

import com.easyinsight.analysis.*;
import com.easyinsight.config.ConfigLoader;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.IServerDataSourceDefinition;
import com.easyinsight.datafeeds.UserMessageException;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.kpi.KPI;
import com.easyinsight.kpi.KPIUtil;
import com.easyinsight.users.Account;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import org.apache.amber.oauth2.client.OAuthClient;
import org.apache.amber.oauth2.client.URLConnectionClient;
import org.apache.amber.oauth2.client.request.OAuthClientRequest;
import org.apache.amber.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.amber.oauth2.common.message.types.GrantType;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: Nov 7, 2010
 * Time: 10:53:52 AM
 */
public class ConstantContactCompositeSource extends CompositeServerDataSource {

    public static final String CONSUMER_KEY = "cec7e39c-25fc-43e6-a423-bf02de492d87";
    public static final String CONSUMER_SECRET = "ee72ddd074804402966863aad91b9687";

    public static final String KEY = "hzt8g9gd27c7fbge3qyscwku";
    public static final String SECRET_KEY = "TURfzTrsh57a4pBgqgknPmVh";

    private String tokenKey;
    private String tokenSecret;
    private String pin;
    private String ccUserName;
    private boolean briefMode;

    private String accessToken;
    private String refreshToken;

    private Set<String> eventIDs;

    public Set<String> getEventIDs() {
        return eventIDs;
    }

    public void setEventIDs(Set<String> eventIDs) {
        this.eventIDs = eventIDs;
    }

    public ConstantContactCompositeSource() {
        setFeedName("Constant Contact");
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean isBriefMode() {
        return briefMode;
    }

    public void setBriefMode(boolean briefMode) {
        this.briefMode = briefMode;
    }

    public String getCcUserName() {
        return ccUserName;
    }

    public void setCcUserName(String ccUserName) {
        this.ccUserName = ccUserName;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    private ContactListCache contactListCache;
    private CampaignCache campaignCache;

    protected List<IServerDataSourceDefinition> sortSources(List<IServerDataSourceDefinition> children) {
        List<IServerDataSourceDefinition> end = new ArrayList<IServerDataSourceDefinition>();
        Set<Integer> set = new HashSet<Integer>();
        for (IServerDataSourceDefinition s : children) {
            if (s.getFeedType().getType() == FeedType.CC_EVENT.getType()) {
                set.add(s.getFeedType().getType());
                end.add(s);
            }
        }
        for (IServerDataSourceDefinition s : children) {
            if (!set.contains(s.getFeedType().getType())) {
                end.add(s);
            }
        }
        return end;
    }

    public ContactListCache getOrCreateContactListCache() {
        if (contactListCache == null) {
            contactListCache = new ContactListCache();
        }
        return contactListCache;
    }

    public CampaignCache getOrCreateCampaignCache() {
        if (campaignCache == null) {
            campaignCache = new CampaignCache();
        }
        return campaignCache;
    }

    @Override
    protected void refreshDone() {
        super.refreshDone();
        contactListCache = null;
        campaignCache = null;
        eventIDs = null;
    }

    @Override
    public FeedDefinition clone(Connection conn) throws CloneNotSupportedException, SQLException {
        ConstantContactCompositeSource constantContactCompositeSource = (ConstantContactCompositeSource) super.clone(conn);
        constantContactCompositeSource.setCcUserName(null);
        constantContactCompositeSource.setTokenKey(null);
        constantContactCompositeSource.setTokenSecret(null);
        return constantContactCompositeSource;
    }

    @Override
    public void exchangeTokens(EIConnection conn, HttpServletRequest httpRequest, String externalPin) throws Exception {
        try {
            if (httpRequest != null) {
                String code = httpRequest.getParameter("code");
                if (code != null) {
                    OAuthClientRequest request;
                    if (ConfigLoader.instance().isProduction()) {
                        request = OAuthClientRequest.tokenLocation("https://oauth2.constantcontact.com/oauth2/oauth/token").
                                setGrantType(GrantType.AUTHORIZATION_CODE).setClientId(KEY).
                                setClientSecret(SECRET_KEY).
                                setRedirectURI("https://www.easy-insight.com/app/oauth").
                                setCode(code).buildBodyMessage();
                    } else {
                        request = OAuthClientRequest.tokenLocation("https://oauth2.constantcontact.com/oauth2/oauth/token").
                                setGrantType(GrantType.AUTHORIZATION_CODE).setClientId(KEY).
                                setClientSecret(SECRET_KEY).
                                setRedirectURI("https://www.easy-insight.com/app/oauth").
                                setCode(code).buildBodyMessage();
                    }
                    OAuthClient client = new OAuthClient(new URLConnectionClient());
                    OAuthJSONAccessTokenResponse response = client.accessToken(request);
                    accessToken = response.getAccessToken();
                    refreshToken = response.getRefreshToken();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM CONSTANT_CONTACT WHERE DATA_SOURCE_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        clearStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO CONSTANT_CONTACT (TOKEN_KEY, TOKEN_SECRET_KEY, DATA_SOURCE_ID, USERNAME, BRIEF_MODE, " +
                "REFRESH_TOKEN, ACCESS_TOKEN) VALUES (?, ?, ?, ?, ?, ?, ?)");
        insertStmt.setString(1, tokenKey);
        insertStmt.setString(2, tokenSecret);
        insertStmt.setLong(3, getDataFeedID());
        insertStmt.setString(4, ccUserName);
        insertStmt.setBoolean(5, briefMode);
        insertStmt.setString(6, accessToken);
        insertStmt.setString(7, refreshToken);
        insertStmt.execute();
        insertStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT TOKEN_KEY, TOKEN_SECRET_KEY, USERNAME, BRIEF_MODE, " +
                "REFRESH_TOKEN, ACCESS_TOKEN FROM CONSTANT_CONTACT WHERE DATA_SOURCE_ID = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            tokenKey = rs.getString(1);
            tokenSecret = rs.getString(2);
            ccUserName = rs.getString(3);
            briefMode = rs.getBoolean(4);
            accessToken = rs.getString(5);
            refreshToken = rs.getString(6);
        }
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.COMPOSITE_PULL;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.CONSTANT_CONTACT;
    }

    @Override
    public String validateCredentials() {
        return null;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> types = new HashSet<FeedType>();
        types.add(FeedType.CONSTANT_CONTACT_CONTACTS);
        types.add(FeedType.CONSTANT_CONTACT_CONTACT_LISTS);
        types.add(FeedType.CONSTANT_CONTACT_CONTACT_TO_CONTACT_LIST);
        types.add(FeedType.CONSTANT_CONTACT_CAMPAIGN);
        types.add(FeedType.CONSTANT_CONTACT_CAMPAIGN_RESULTS);
        types.add(FeedType.CONSTANT_CONTACT_LINKS);
        types.add(FeedType.CC_EVENT);
        types.add(FeedType.CC_EVENT_REGISTRANTS);
        return types;
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return new ArrayList<ChildConnection>();
    }

    @Override
    protected Collection<ChildConnection> getLiveChildConnections() {
        return Arrays.asList(new ChildConnection(FeedType.CONSTANT_CONTACT_CONTACTS, FeedType.CONSTANT_CONTACT_CONTACT_TO_CONTACT_LIST,
                CCContactSource.CONTACT_ID, CCContactToContactListSource.CONTACT_ID),
                new ChildConnection(FeedType.CONSTANT_CONTACT_CONTACT_TO_CONTACT_LIST, FeedType.CONSTANT_CONTACT_CONTACT_LISTS,
                    CCContactToContactListSource.CONTACT_LIST_ID, CCContactListSource.CONTACT_LIST_ID),
                new ChildConnection(FeedType.CONSTANT_CONTACT_CAMPAIGN, FeedType.CONSTANT_CONTACT_CAMPAIGN_RESULTS,
                    CCCampaignSource.CAMPAIGN_ID, CCCampaignResultsSource.CAMPAIGN_ID),
                new ChildConnection(FeedType.CONSTANT_CONTACT_CONTACTS, FeedType.CONSTANT_CONTACT_CAMPAIGN_RESULTS,
                    CCContactSource.CONTACT_ID, CCCampaignResultsSource.CONTACT_ID),
                new ChildConnection(FeedType.CONSTANT_CONTACT_CAMPAIGN_RESULTS, FeedType.CONSTANT_CONTACT_LINKS,
                        CCCampaignResultsSource.LINK_ID, CCCampaignLinkSource.LINK_ID),
                new ChildConnection(FeedType.CONSTANT_CONTACT_CONTACTS, FeedType.CC_EVENT_REGISTRANTS,
                        CCContactSource.CONTACT_EMAIL, CCEventRegistrantSource.REGISTRANT_EMAIL),
                new ChildConnection(FeedType.CC_EVENT, FeedType.CC_EVENT_REGISTRANTS,
                        CCEventSource.EVENT_ID, CCEventRegistrantSource.REGISTRANT_EVENT_ID));
    }
}
