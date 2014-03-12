package com.easyinsight.datafeeds.redbooth;

import com.easyinsight.analysis.DataSourceInfo;
import com.easyinsight.config.ConfigLoader;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.HTMLConnectionFactory;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import org.apache.amber.oauth2.client.OAuthClient;
import org.apache.amber.oauth2.client.URLConnectionClient;
import org.apache.amber.oauth2.client.request.OAuthClientRequest;
import org.apache.amber.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.amber.oauth2.common.message.types.GrantType;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: 2/19/14
 * Time: 7:09 PM
 */
public class RedboothCompositeSource extends CompositeServerDataSource {

    public static final String CLIENT_KEY = "WsdMahDURnUmmQcIYbzrhTS9gQrBr9fxiqKo70nn";
    public static final String CLIENT_SECRET = "k9pj7xlslkCft2t8PQdJGCft3ylgk5iF5HU20gpO";

    private String accessToken;
    private String refreshToken;

    public RedboothCompositeSource() {
        setFeedName("Redbooth");
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
    public int getDataSourceType() {
        return DataSourceInfo.COMPOSITE_PULL;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.REDBOOTH_COMPOSITE;
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> types = new HashSet<FeedType>();
        types.add(FeedType.REDBOOTH_ORGANIZATION);
        types.add(FeedType.REDBOOTH_PROJECT);
        types.add(FeedType.REDBOOTH_TASK_LIST);
        types.add(FeedType.REDBOOTH_TASK);
        types.add(FeedType.REDBOOTH_COMMENT);
        return types;
    }

    @Override
    protected Collection<ChildConnection> getLiveChildConnections() {
        return Arrays.asList(new ChildConnection(FeedType.REDBOOTH_PROJECT, FeedType.REDBOOTH_TASK_LIST, RedboothProjectSource.ID, RedboothTaskListSource.PROJECT_ID),
                new ChildConnection(FeedType.REDBOOTH_TASK_LIST, FeedType.REDBOOTH_TASK, RedboothTaskListSource.ID, RedboothTaskSource.TASK_LIST_ID),
                new ChildConnection(FeedType.REDBOOTH_TASK, FeedType.REDBOOTH_COMMENT, RedboothTaskSource.ID, RedboothCommentSource.TASK_ID));
    }


    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return Arrays.asList(new ChildConnection(FeedType.REDBOOTH_PROJECT, FeedType.REDBOOTH_TASK_LIST, RedboothProjectSource.ID, RedboothTaskListSource.PROJECT_ID),
                new ChildConnection(FeedType.REDBOOTH_TASK_LIST, FeedType.REDBOOTH_TASK, RedboothTaskListSource.ID, RedboothTaskSource.TASK_LIST_ID),
                new ChildConnection(FeedType.REDBOOTH_TASK, FeedType.REDBOOTH_COMMENT, RedboothTaskSource.ID, RedboothCommentSource.TASK_ID));
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement deleteStatement = conn.prepareStatement("delete from redbooth where data_source_id = ?");
        deleteStatement.setLong(1, this.getDataFeedID());
        deleteStatement.execute();
        PreparedStatement statement = conn.prepareStatement("insert into redbooth (data_source_id, access_token, refresh_token) VALUES (?, ?, ?)");
        statement.setLong(1, getDataFeedID());
        statement.setString(2, accessToken);
        statement.setString(3, refreshToken);
        statement.execute();
        statement.close();
        deleteStatement.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement statement = conn.prepareStatement("select access_token, refresh_token from redbooth where data_source_id = ?");
        statement.setLong(1, getDataFeedID());
        ResultSet rs = statement.executeQuery();
        if(rs.next()) {
            setAccessToken(rs.getString(1));
            setRefreshToken(rs.getString(2));
        }
        statement.close();
    }

    @Override
    public void exchangeTokens(EIConnection conn, HttpServletRequest httpRequest, String externalPin) throws Exception {
        try {
            if (httpRequest != null) {
                String code = httpRequest.getParameter("code");
                if (code != null) {
                    HttpClient httpClient = new HttpClient();
                    PostMethod postMethod = new PostMethod("https://redbooth.com/oauth/token");
                    postMethod.setParameter("client_id", RedboothCompositeSource.CLIENT_KEY);
                    postMethod.setParameter("grant_type", "authorization_code");
                    postMethod.setParameter("client_secret", RedboothCompositeSource.CLIENT_SECRET);
                    postMethod.setParameter("code", code);
                    postMethod.setParameter("redirect_uri", "https://www.easy-insight.com/app/oauth");
                    httpClient.executeMethod(postMethod);
                    String responseBody = new String(postMethod.getResponseBody());
                    JSONObject jsonObject = new JSONObject(responseBody);
                    accessToken = jsonObject.get("access_token").toString();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void configureFactory(HTMLConnectionFactory factory) {
        factory.type(HTMLConnectionFactory.TYPE_OAUTH);
    }
}
