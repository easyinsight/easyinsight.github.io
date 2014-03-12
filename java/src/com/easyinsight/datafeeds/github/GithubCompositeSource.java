package com.easyinsight.datafeeds.github;

import com.easyinsight.analysis.DataSourceConnectivityReportFault;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.config.ConfigLoader;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.users.Account;
import org.apache.amber.oauth2.client.OAuthClient;
import org.apache.amber.oauth2.client.URLConnectionClient;
import org.apache.amber.oauth2.client.request.OAuthClientRequest;
import org.apache.amber.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.types.GrantType;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: 1/5/14
 * Time: 11:19 AM
 */
public class GithubCompositeSource extends CompositeServerDataSource {

    public static final String CLIENT_ID = "57a7af824803bc0fe694";
    public static final String CLIENT_SECRET = "c2371684decf87d6ae4486186b519b124b31eced";

    private String accessToken;
    private String refreshToken;

    public GithubCompositeSource() {
        setFeedName("GitHub");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.GITHUB_COMPOSITE;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    @Override
    public String validateCredentials() {
        return null;
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM GITHUB WHERE DATA_SOURCE_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        PreparedStatement saveStmt = conn.prepareStatement("INSERT INTO GITHUB (DATA_SOURCE_ID, ACCESS_TOKEN, REFRESH_TOKEN) VALUES (?, ?, ?)");
        saveStmt.setLong(1, getDataFeedID());
        saveStmt.setString(2, accessToken);
        saveStmt.setString(3, refreshToken);
        saveStmt.execute();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT ACCESS_TOKEN, REFRESH_TOKEN FROM GITHUB WHERE data_source_id = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            accessToken = rs.getString(1);
            refreshToken = rs.getString(2);
        }
    }

    public void refreshTokenInfo() throws OAuthSystemException, OAuthProblemException {
        try {
            OAuthClientRequest.TokenRequestBuilder tokenRequestBuilder = OAuthClientRequest.tokenLocation("https://github.com/login/oauth/access_token").
                    setGrantType(GrantType.REFRESH_TOKEN).setClientId(CLIENT_ID).
                    setClientSecret(CLIENT_SECRET).setRefreshToken(refreshToken).setRedirectURI("https://easy-insight.com/app/oauth");
            tokenRequestBuilder.setParameter("type", "refresh");
            OAuthClient client = new OAuthClient(new URLConnectionClient());
            OAuthClientRequest request = tokenRequestBuilder.buildBodyMessage();
            OAuthJSONAccessTokenResponse response = client.accessToken(request);
            accessToken = response.getAccessToken();
        } catch (Exception e) {
            throw new ReportException(new DataSourceConnectivityReportFault("You need to reauthorize access to GitHub.", this));
        }
    }

    @Override
    public void exchangeTokens(EIConnection conn, HttpServletRequest httpRequest, String externalPin) throws Exception {
        try {
            if (httpRequest != null) {
                String code = httpRequest.getParameter("code");
                if (code != null) {
                    HttpClient httpClient = new HttpClient();
                    PostMethod postMethod = new PostMethod("https://github.com/login/oauth/access_token");
                    postMethod.setParameter("client_id", GithubCompositeSource.CLIENT_ID);
                    postMethod.setParameter("client_secret", GithubCompositeSource.CLIENT_SECRET);
                    postMethod.setParameter("code", code);
                    postMethod.setParameter("redirect_uri", "https://www.easy-insight.com/app/oauth");
                    httpClient.executeMethod(postMethod);
                    String responseBody = new String(postMethod.getResponseBody());
                    accessToken = responseBody.split("\\&")[0].split("\\=")[1];
                    /*OAuthClientRequest request;
                    if (ConfigLoader.instance().isProduction()) {
                        OAuthClientRequest.TokenRequestBuilder tokenRequestBuilder = OAuthClientRequest.tokenLocation("https://github.com/login/oauth/access_token").
                                setGrantType(GrantType.AUTHORIZATION_CODE).setClientId(CLIENT_ID).
                                setClientSecret(CLIENT_SECRET).
                                setRedirectURI("https://www.easy-insight.com/app/oauth").
                                setCode(code);
                        tokenRequestBuilder.setParameter("type", "web_server");
                        request = tokenRequestBuilder.buildBodyMessage();
                    } else {
                        OAuthClientRequest.TokenRequestBuilder tokenRequestBuilder = OAuthClientRequest.tokenLocation("https://github.com/login/oauth/access_token").
                                setGrantType(GrantType.AUTHORIZATION_CODE).setClientId(CLIENT_ID).
                                setClientSecret(CLIENT_SECRET).
                                setRedirectURI("https://www.easy-insight.com/app/oauth").
                                setCode(code);
                        tokenRequestBuilder.setParameter("type", "web_server");
                        request = tokenRequestBuilder.buildBodyMessage();
                    }
                    OAuthClient client = new OAuthClient(new URLConnectionClient());
                    client.accessToken(request);
                    OAuthJSONAccessTokenResponse response = client.accessToken(request);
                    accessToken = response.getAccessToken();
                    refreshToken = response.getRefreshToken();*/
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    @Override
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> types = new HashSet<FeedType>();
        types.add(FeedType.GITHUB_REPOSITORY);
        return types;
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return new ArrayList<ChildConnection>();
    }
}
