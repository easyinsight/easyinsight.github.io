package com.easyinsight.datafeeds.redbooth;

import com.easyinsight.analysis.DataSourceConnectivityReportFault;
import com.easyinsight.analysis.DataSourceInfo;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.config.ConfigLoader;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.HTMLConnectionFactory;
import com.easyinsight.datafeeds.IServerDataSourceDefinition;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.logging.LogClass;
import org.apache.amber.oauth2.client.OAuthClient;
import org.apache.amber.oauth2.client.URLConnectionClient;
import org.apache.amber.oauth2.client.request.OAuthClientRequest;
import org.apache.amber.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.types.GrantType;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
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

    public static final String CLIENT_KEY = "d4ecf56065af12a506fb7b8acf924a1de6f3ef7ec472d2caacd3fba960187512";
    public static final String CLIENT_SECRET = "521d5dc1b25e223cad478934334d1bb81291cab0633f151f81ea516ab7429731";

    private String accessToken;
    private String refreshToken;
    private transient Set<String> taskIDs;

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

    public Set<String> getTaskIDs() {
        return taskIDs;
    }

    public void setTaskIDs(Set<String> taskIDs) {
        this.taskIDs = taskIDs;
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

    private Set<String> validProjects = new HashSet<String>();

    public Set<String> getValidProjects() {
        return validProjects;
    }

    public void setValidProjects(Set<String> validProjects) {
        this.validProjects = validProjects;
    }

    @Override
    protected void refreshDone() {
        super.refreshDone();
        validProjects = null;
    }

    protected List<IServerDataSourceDefinition> sortSources(List<IServerDataSourceDefinition> children) {
        List<IServerDataSourceDefinition> end = new ArrayList<IServerDataSourceDefinition>();
        Set<Integer> set = new HashSet<Integer>();
        for (IServerDataSourceDefinition s : children) {
            if (s.getFeedType().getType() == FeedType.REDBOOTH_PROJECT.getType()) {
                set.add(s.getFeedType().getType());
                end.add(s);
            }
        }
        for (IServerDataSourceDefinition s : children) {
            if (s.getFeedType().getType() == FeedType.REDBOOTH_TASK.getType()) {
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

    @Override
    protected Collection<ChildConnection> getLiveChildConnections() {
        return Arrays.asList(
                new ChildConnection(FeedType.REDBOOTH_ORGANIZATION, FeedType.REDBOOTH_PROJECT, RedboothOrganizationSource.ID, RedboothProjectSource.ORGANIZATION_ID),
                new ChildConnection(FeedType.REDBOOTH_PROJECT, FeedType.REDBOOTH_TASK_LIST, RedboothProjectSource.ID, RedboothTaskListSource.PROJECT_ID),
                new ChildConnection(FeedType.REDBOOTH_TASK_LIST, FeedType.REDBOOTH_TASK, RedboothTaskListSource.ID, RedboothTaskSource.TASK_LIST_ID),
                new ChildConnection(FeedType.REDBOOTH_TASK, FeedType.REDBOOTH_COMMENT, RedboothTaskSource.ID, RedboothCommentSource.TASK_ID));
    }


    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return Arrays.asList(
                new ChildConnection(FeedType.REDBOOTH_ORGANIZATION, FeedType.REDBOOTH_PROJECT, RedboothOrganizationSource.ID, RedboothProjectSource.ORGANIZATION_ID),
                new ChildConnection(FeedType.REDBOOTH_PROJECT, FeedType.REDBOOTH_TASK_LIST, RedboothProjectSource.ID, RedboothTaskListSource.PROJECT_ID),
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
    protected void beforeRefresh(Date lastRefreshTime) {
        super.beforeRefresh(lastRefreshTime);

        if (lastRefreshTime != null) {
            try {
                refreshTokenInfo();
            } catch (ReportException re) {
                throw re;
            } catch (Exception e) {
                throw new ReportException(new DataSourceConnectivityReportFault(e.getMessage(), this));
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(new Date(1403078398L));
        System.out.println(new Date(1403078398L * 1000));
    }

    public void refreshTokenInfo() throws OAuthSystemException, OAuthProblemException {
        try {
            HttpClient httpClient = new HttpClient();
            PostMethod postMethod = new PostMethod("https://redbooth.com/oauth2/token");
            postMethod.setParameter("client_id", RedboothCompositeSource.CLIENT_KEY);
            postMethod.setParameter("grant_type", "refresh_token");
            postMethod.setParameter("refresh_token", refreshToken);
            postMethod.setParameter("client_secret", RedboothCompositeSource.CLIENT_SECRET);
            postMethod.setParameter("redirect_uri", "https://www.easy-insight.com/app/oauth");
            httpClient.executeMethod(postMethod);
            String responseBody = new String(postMethod.getResponseBody());
            JSONObject jsonObject = new JSONObject(responseBody);
            accessToken = jsonObject.get("access_token").toString();
            refreshToken = jsonObject.get("refresh_token").toString();
        } catch (Exception e) {
            throw new ReportException(new DataSourceConnectivityReportFault("You need to reauthorize access to Redbooth.", this));
        }
    }


    @Override
    public void exchangeTokens(EIConnection conn, HttpServletRequest httpRequest, String externalPin) throws Exception {
        try {
            if (httpRequest != null) {
                String code = httpRequest.getParameter("code");
                if (code != null) {
                    HttpClient httpClient = new HttpClient();
                    PostMethod postMethod = new PostMethod("https://redbooth.com/oauth2/token");
                    postMethod.setParameter("client_id", RedboothCompositeSource.CLIENT_KEY);
                    postMethod.setParameter("grant_type", "authorization_code");
                    postMethod.setParameter("client_secret", RedboothCompositeSource.CLIENT_SECRET);
                    postMethod.setParameter("code", code);
                    postMethod.setParameter("redirect_uri", "https://www.easy-insight.com/app/oauth");
                    httpClient.executeMethod(postMethod);
                    String responseBody = new String(postMethod.getResponseBody());
                    JSONObject jsonObject = new JSONObject(responseBody);
                    accessToken = jsonObject.get("access_token").toString();
                    if (accessToken == null || "".equals(accessToken)) {
                        throw new RuntimeException();
                    }
                    refreshToken = jsonObject.get("refresh_token").toString();
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
