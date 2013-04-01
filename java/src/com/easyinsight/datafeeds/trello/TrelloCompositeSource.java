package com.easyinsight.datafeeds.trello;

import com.easyinsight.analysis.DataSourceInfo;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.UserMessageException;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
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
 * Date: 3/12/13
 * Time: 8:32 PM
 */
public class TrelloCompositeSource extends CompositeServerDataSource {

    public static final String KEY = "5d049b71083c27518166c0f0a598d067";
    public static final String SECRET_KEY = "12816dd2c4a1d3d5136ddc80d5b09bf18a15dc539667a4181b8bcb3bbbb3008a";

    private String tokenKey;
    private String tokenSecret;
    private String pin;

    public TrelloCompositeSource() {
        setFeedName("Trello");
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> types = new HashSet<FeedType>();
        types.add(FeedType.TRELLO_BOARD);
        types.add(FeedType.TRELLO_CARD);
        types.add(FeedType.TRELLO_LIST);
        types.add(FeedType.TRELLO_CARD_HISTORY);
        return types;
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
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM TRELLO_COMPOSITE_SOURCE WHERE DATA_SOURCE_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO TRELLO_COMPOSITE_SOURCE (DATA_SOURCE_ID, TOKEN_KEY, TOKEN_SECRET_KEY) VALUES (?, ?, ?)");
        insertStmt.setLong(1, getDataFeedID());
        insertStmt.setString(2, getTokenKey());
        insertStmt.setString(3, getTokenSecret());
        insertStmt.execute();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement loadStmt = conn.prepareStatement("SELECT TOKEN_KEY, TOKEN_SECRET_KEY FROM TRELLO_COMPOSITE_SOURCE WHERE DATA_SOURCE_ID = ?");
        loadStmt.setLong(1, getDataFeedID());
        ResultSet rs = loadStmt.executeQuery();
        if (rs.next()) {
            setTokenKey(rs.getString(1));
            setTokenSecret(rs.getString(2));
        }
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.TRELLO_COMPOSITE;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.COMPOSITE_PULL;
    }

    @Override
    public String validateCredentials() {
        return null;
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return new ArrayList<ChildConnection>();
    }

    @Override
    protected Collection<ChildConnection> getLiveChildConnections() {
        return Arrays.asList(new ChildConnection(FeedType.TRELLO_BOARD, FeedType.TRELLO_CARD, TrelloBoardSource.BOARD_ID, TrelloCardSource.CARD_BOARD_ID),
                new ChildConnection(FeedType.TRELLO_LIST, FeedType.TRELLO_CARD, TrelloListSource.LIST_ID, TrelloCardSource.CARD_LIST_ID),
                new ChildConnection(FeedType.TRELLO_CARD_HISTORY, FeedType.TRELLO_CARD, TrelloCardHistorySource.HISTORY_CARD_ID, TrelloCardSource.CARD_ID));
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
}
