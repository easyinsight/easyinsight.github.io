package com.easyinsight.datafeeds.salesforce;

import com.easyinsight.core.DerivedKey;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.datafeeds.constantcontact.ConstantContactCompositeSource;

import com.easyinsight.users.Account;
import com.easyinsight.analysis.*;

import net.smartam.leeloo.client.OAuthClient;
import net.smartam.leeloo.client.URLConnectionClient;
import net.smartam.leeloo.client.request.OAuthClientRequest;
import net.smartam.leeloo.client.response.OAuthJSONAccessTokenResponse;
import net.smartam.leeloo.common.exception.OAuthProblemException;
import net.smartam.leeloo.common.exception.OAuthSystemException;
import net.smartam.leeloo.common.message.types.GrantType;
import nu.xom.*;
import oauth.signpost.OAuthConsumer;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.signature.HmacSha1MessageSigner;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.sql.Connection;

/**
 * User: abaldwin
 * Date: Jul 8, 2009
 * Time: 10:10:17 AM
 */
public class SalesforceBaseDataSource extends CompositeServerDataSource {

    public static final String SALESFORCE_CONSUMER_KEY = "3MVG9VmVOCGHKYBQUAbz7d7kk6x2g29kEbyFhTBt7u..yutNvp7evoFyWTm2q4tZfWRdxekrK6fhhwf5BN4Tq";
    public static final String SALESFORCE_SECRET_KEY = "5028271817562655674";

    private String accessToken;

    private String instanceName;

    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM SALESFORCE_DEFINITION WHERE DATA_SOURCE_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        PreparedStatement saveStmt = conn.prepareStatement("INSERT INTO SALESFORCE_DEFINITION (DATA_SOURCE_ID, ACCESS_TOKEN, REFRESH_TOKEN, INSTANCE_NAME) VALUES (?, ?, ?, ?)");
        saveStmt.setLong(1, getDataFeedID());
        saveStmt.setString(2, accessToken);
        saveStmt.setString(3, refreshToken);
        saveStmt.setString(4, instanceName);
        saveStmt.execute();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement loadStmt = conn.prepareStatement("SELECT ACCESS_TOKEN, REFRESH_TOKEN, INSTANCE_NAME FROM SALESFORCE_DEFINITION WHERE DATA_SOURCE_ID = ?");
        loadStmt.setLong(1, getDataFeedID());
        ResultSet rs = loadStmt.executeQuery();
        if (rs.next()) {
            accessToken = rs.getString(1);
            refreshToken = rs.getString(2);
            instanceName = rs.getString(3);
        }
        loadStmt.close();
    }

    public void refreshTokenInfo() throws OAuthSystemException, OAuthProblemException {
        try {
            OAuthClientRequest.TokenRequestBuilder tokenRequestBuilder = OAuthClientRequest.tokenLocation("https://na1.salesforce.com/services/oauth2/token").
                    setGrantType(GrantType.REFRESH_TOKEN).setClientId(SALESFORCE_CONSUMER_KEY).
                    setClientSecret(SALESFORCE_SECRET_KEY).setRefreshToken(refreshToken).setRedirectURI("https://easy-insight.com/app/oauth");
            tokenRequestBuilder.setParameter("type", "refresh");
            OAuthClient client = new OAuthClient(new URLConnectionClient());
            OAuthClientRequest request = tokenRequestBuilder.buildBodyMessage();
            OAuthJSONAccessTokenResponse response = client.accessToken(request);
            accessToken = response.getAccessToken();
        } catch (Exception e) {
            throw new ReportException(new DataSourceConnectivityReportFault("You need to reauthorize access to Salesforce.", this));
        }
    }

    @Override
    public void exchangeTokens(EIConnection conn, HttpServletRequest httpRequest, String externalPin) throws Exception {
        try {
            if (httpRequest != null) {
                String code = httpRequest.getParameter("code");
                if (code != null) {
                    OAuthClientRequest request = OAuthClientRequest.tokenLocation("https://na1.salesforce.com/services/oauth2/token").
                                setGrantType(GrantType.AUTHORIZATION_CODE).setClientId(SalesforceBaseDataSource.SALESFORCE_CONSUMER_KEY).
                                setClientSecret(SalesforceBaseDataSource.SALESFORCE_SECRET_KEY).
                                setRedirectURI("https://www.easy-insight.com/app/oauth").
                                setCode(code).buildBodyMessage();
                    OAuthClient client = new OAuthClient(new URLConnectionClient());
                    OAuthJSONAccessTokenResponse response = client.accessToken(request);
                    accessToken = response.getAccessToken();
                    refreshToken = response.getRefreshToken();
                    instanceName = response.getParam("instance_url");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.PROFESSIONAL;
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        return new HashSet<FeedType>();
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return new ArrayList<ChildConnection>();
    }

    @Override
    public List<CompositeFeedConnection> obtainChildConnections() throws SQLException {
        return getConnections();
    }

    public boolean isConfigured() {
        return true;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.SALESFORCE;
    }

    public int getDataSourceType() {
        return DataSourceInfo.COMPOSITE_PULL;
    }

    @Override
    public String validateCredentials() {
        return null;
    }

    public SalesforceBaseDataSource() {
        setFeedName("Salesforce");
    }

    protected Document query(String queryString, String tokenKey, String tokenSecretKey, FeedDefinition parentSource) throws OAuthExpectationFailedException, OAuthMessageSignerException, OAuthCommunicationException, IOException, ParsingException {
        try {
            Builder builder = new Builder();
            OAuthConsumer consumer = new CommonsHttpOAuthConsumer(ConstantContactCompositeSource.CONSUMER_KEY, ConstantContactCompositeSource.CONSUMER_SECRET);
            consumer.setMessageSigner(new HmacSha1MessageSigner());
            consumer.setTokenWithSecret(tokenKey, tokenSecretKey);
            HttpGet httpRequest = new HttpGet(queryString);
            httpRequest.setHeader("Accept", "application/xml");
            httpRequest.setHeader("Content-Type", "application/xml");


            org.apache.http.client.HttpClient client = new DefaultHttpClient();
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            String string = client.execute(httpRequest, responseHandler);
            string = string.replace("xmlns=\"http://www.w3.org/2005/Atom\"", "");
            string = string.replace("xmlns=\"http://ws.constantcontact.com/ns/1.0/\"", "");
            string = string.replace("xmlns=\"http://www.w3.org/2007/app\"", "");
            //System.out.println(string);
            return builder.build(new ByteArrayInputStream(string.getBytes("UTF-8")));
        } catch (HttpResponseException e) {
            if ("Unauthorized".equals(e.getMessage())) {
                throw new ReportException(new DataSourceConnectivityReportFault("You need to reauthorize Easy Insight to access your Salesforce data.", parentSource));
            } else {
                throw e;
            }
        }
    }

    private static class AuthFailed extends Exception {

    }

    @Override
    protected List<IServerDataSourceDefinition> childDataSources(EIConnection conn) throws Exception {
        List<IServerDataSourceDefinition> defaultChildren = super.childDataSources(conn);
        Document doc;
        try {
            HttpGet httpRequest = new HttpGet(instanceName + "/services/data/v20.0/sobjects/");
            httpRequest.setHeader("Accept", "application/xml");
            httpRequest.setHeader("Content-Type", "application/xml");
            httpRequest.setHeader("Authorization", "OAuth " + accessToken);


            org.apache.http.client.HttpClient cc = new DefaultHttpClient();
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            String string = cc.execute(httpRequest, responseHandler);

            Builder builder = new Builder();

            doc = builder.build(new ByteArrayInputStream(string.getBytes()));
        } catch (HttpResponseException hre) {
            if ("Unauthorized".equals(hre.getMessage())) {
                refreshTokenInfo();
                HttpGet httpRequest = new HttpGet(instanceName + "/services/data/v20.0/sobjects/");
                httpRequest.setHeader("Accept", "application/xml");
                httpRequest.setHeader("Content-Type", "application/xml");
                httpRequest.setHeader("Authorization", "OAuth " + accessToken);


                org.apache.http.client.HttpClient cc = new DefaultHttpClient();
                ResponseHandler<String> responseHandler = new BasicResponseHandler();

                String string = cc.execute(httpRequest, responseHandler);

                Builder builder = new Builder();

                doc = builder.build(new ByteArrayInputStream(string.getBytes()));
            } else {
                throw hre;
            }
        }
        Nodes sobjectNodes = doc.query("/DescribeGlobal/sobjects");
        List<CompositeFeedConnection> connections = new ArrayList<CompositeFeedConnection>();
        List<SFConnection> connectionList = new ArrayList<SFConnection>();
        Map<String, SalesforceSObjectSource> map = new HashMap<String, SalesforceSObjectSource>();

        for (IServerDataSourceDefinition child : defaultChildren) {
            SalesforceSObjectSource source = (SalesforceSObjectSource) child;
            map.put(source.getSobjectName(), source);
        }

        for (int i = 0; i < sobjectNodes.size(); i++) {
            Node sobjectNode = sobjectNodes.get(i);
            String name = sobjectNode.query("name/text()").get(0).getValue();
            System.out.println(name);
            SalesforceSObjectSource existing = map.get(name);
            if (existing == null) {
                String searchableString = sobjectNode.query("searchable/text()").get(0).getValue();
                boolean searchable = Boolean.parseBoolean(searchableString);
                if (searchable || "UserRole".equals(name) || "OpportunityHistory".equals(name) || "OpportunityStage".equals(name)) {
                //if (searchable) {
                    SalesforceSObjectSource salesforceSObjectSource = new SalesforceSObjectSource();
                    salesforceSObjectSource.setSobjectName(name);
                    salesforceSObjectSource.setFeedName(name);
                    newDefinition(salesforceSObjectSource, conn, "", getUploadPolicy());
                    CompositeFeedNode node = new CompositeFeedNode();
                    node.setDataFeedID(salesforceSObjectSource.getDataFeedID());
                    getCompositeFeedNodes().add(node);
                    defaultChildren.add(salesforceSObjectSource);
                    map.put(name, salesforceSObjectSource);
                    connectionList.addAll(connections(name));
                }
            } else {
                map.put(name, existing);
                connectionList.addAll(connections(name));
            }
        }

        for (SFConnection connection : connectionList) {
            if (connection.source.equals(connection.target)) {
                continue;
            }
            SalesforceSObjectSource source = map.get(connection.source);
            SalesforceSObjectSource target = map.get(connection.target);
            if (source != null & target != null) {
                AnalysisItem sourceItem = source.findAnalysisItem(connection.sourceField);
                AnalysisItem targetItem = target.findAnalysisItem(connection.targetField);
                if (sourceItem == null) {
                    System.out.println("Could not find field " + connection.sourceField + " on " + connection.source);
                }
                if (targetItem == null) {
                    System.out.println("Could not find field " + connection.targetField + " on " + connection.target);
                }
                System.out.println("Connecting " + connection.source + " to " + connection.target);
                if (sourceItem != null && targetItem != null) {
                    connections.add(new CompositeFeedConnection(source.getDataFeedID(), target.getDataFeedID(), sourceItem, targetItem,
                            source.getFeedName(), target.getFeedName(), false, false, false, false));
                }
            }
        }
        //if (getConnections() == null || getConnections().size() == 0) {
            setConnections(connections);
        //}
        return defaultChildren;
    }

    private List<SFConnection> connections(String sobjectName) throws Exception {
        List<SFConnection> connections = new ArrayList<SFConnection>();
        HttpGet httpRequest = new HttpGet(instanceName + "/services/data/v20.0/sobjects/" + sobjectName + "/describe/");
        httpRequest.setHeader("Accept", "application/xml");
        httpRequest.setHeader("Content-Type", "application/xml");
        httpRequest.setHeader("Authorization", "OAuth " + accessToken);


        org.apache.http.client.HttpClient cc = new DefaultHttpClient();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();

        String string = cc.execute(httpRequest, responseHandler);
        Builder builder = new Builder();
        Document doc = builder.build(new ByteArrayInputStream(string.getBytes()));
        Nodes fieldsNodes = doc.query("/" + sobjectName + "/fields");
        String idField = null;
        for (int i = 0; i < fieldsNodes.size(); i++) {
            Node fieldNode = fieldsNodes.get(i);
            String type = fieldNode.query("type/text()").get(0).getValue();
            if ("id".equals(type)) {
                idField = fieldNode.query("name/text()").get(0).getValue();
            }
        }
        Nodes relNodes = doc.query("/" + sobjectName + "/childRelationships");
        for (int i = 0; i < relNodes.size(); i++) {
            Node relNode = relNodes.get(i);
            String targetSObject = relNode.query("childSObject/text()").get(0).getValue();
            String targetField = relNode.query("field/text()").get(0).getValue();
            //String relationship = relNode.query("relationshipName/text()").get(0).getValue();
            connections.add(new SFConnection(sobjectName, targetSObject, idField, targetField));
        }
        return connections;
    }

    @Override
    public void beforeSave(EIConnection conn) throws Exception {
        Map<Long, FeedDefinition> childMap = new HashMap<Long, FeedDefinition>();
        for (AnalysisItem analysisItem : getFields()) {
            Key key = analysisItem.getKey();
            //if (key.toBaseKey().indexed()) {
            if (key instanceof DerivedKey) {
                DerivedKey derivedKey = (DerivedKey) key;
                FeedDefinition child = childMap.get(derivedKey.getFeedID());
                if (child == null) {
                    child = new FeedStorage().getFeedDefinitionData(derivedKey.getFeedID(), conn);
                    childMap.put(derivedKey.getFeedID(), child);
                }
                for (AnalysisItem item : child.getFields()) {
                    if (item.getKey().getKeyID() == derivedKey.getParentKey().getKeyID()) {
                        NamedKey namedKey = (NamedKey) item.getKey().toBaseKey();
                        namedKey.setIndexed(key.toBaseKey().indexed());
                    }
                }
            }
            //}
        }
        for (FeedDefinition dataSource : childMap.values()) {
            new FeedStorage().updateDataFeedConfiguration(dataSource, conn);
        }
        super.beforeSave(conn);
    }

    private static class SFConnection {
        private String source;
        private String target;
        private String sourceField;
        private String targetField;

        private SFConnection(String source, String target, String sourceField, String targetField) {
            this.source = source;
            this.target = target;
            this.sourceField = sourceField;
            this.targetField = targetField;
        }
    }
}
