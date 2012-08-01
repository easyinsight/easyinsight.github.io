package com.easyinsight.datafeeds.constantcontact;

import com.easyinsight.analysis.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.UserMessageException;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.kpi.KPI;
import com.easyinsight.kpi.KPIUtil;
import com.easyinsight.users.Account;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;

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

    private String tokenKey;
    private String tokenSecret;
    private String pin;
    private String ccUserName;
    private boolean briefMode;

    public ConstantContactCompositeSource() {
        setFeedName("Constant Contact");
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
    public void exchangeTokens(EIConnection conn, HttpServletRequest request, String externalPin) throws Exception {
        try {
            if (externalPin != null) {
                pin = externalPin;
            }
            if (pin != null && !"".equals(pin)) {
                OAuthConsumer consumer = (OAuthConsumer) request.getSession().getAttribute("oauthConsumer");
                OAuthProvider provider = (OAuthProvider) request.getSession().getAttribute("oauthProvider");
                provider.retrieveAccessToken(consumer, pin.trim());
                tokenKey = consumer.getToken();
                tokenSecret = consumer.getTokenSecret();
                pin = null;
            }
        } catch (OAuthCommunicationException oe) {
            throw new UserMessageException(oe, "The specified verifier token was rejected. Please try to authorize access again.");
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
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO CONSTANT_CONTACT (TOKEN_KEY, TOKEN_SECRET_KEY, DATA_SOURCE_ID, USERNAME, BRIEF_MODE) VALUES (?, ?, ?, ?, ?)");
        insertStmt.setString(1, tokenKey);
        insertStmt.setString(2, tokenSecret);
        insertStmt.setLong(3, getDataFeedID());
        insertStmt.setString(4, ccUserName);
        insertStmt.setBoolean(5, briefMode);
        insertStmt.execute();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT TOKEN_KEY, TOKEN_SECRET_KEY, USERNAME, BRIEF_MODE FROM CONSTANT_CONTACT WHERE DATA_SOURCE_ID = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            tokenKey = rs.getString(1);
            tokenSecret = rs.getString(2);
            ccUserName = rs.getString(3);
            briefMode = rs.getBoolean(4);
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
                    CCContactSource.CONTACT_ID, CCCampaignResultsSource.CONTACT_ID));
    }
}
