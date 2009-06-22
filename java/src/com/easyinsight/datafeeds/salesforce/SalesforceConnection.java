package com.easyinsight.datafeeds.salesforce;

import com.easyinsight.users.*;
import com.easyinsight.datafeeds.*;
import com.easyinsight.userupload.UserUploadService;
import com.easyinsight.userupload.CredentialsResponse;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.userupload.UserUploadInternalService;
import com.easyinsight.database.Database;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.dataset.ColumnSegmentFactory;
import com.easyinsight.dataset.PersistableDataSetForm;
import com.easyinsight.security.Roles;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.analysis.*;
import com.easyinsight.logging.LogClass;
import com.sforce.soap.enterprise.*;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.net.URL;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.lang.Error;

/**
 * User: James Boe
 * Date: Jan 26, 2008
 * Time: 2:28:24 PM
 */
public class SalesforceConnection {

    private Map<CredentialKey, Soap> portCache = new WeakHashMap<CredentialKey, Soap>();
    private static SalesforceConnection instance;
    private FeedStorage feedStorage = new FeedStorage();
    private UserService userStorage = new UserService();

    public SalesforceConnection() {
        instance = this;
    }

    public static SalesforceConnection instance() {
        return instance;
    }

    public Soap getPort(String userName, String password) throws InvalidIdFault, LoginFault {
        CredentialKey credentialKey = new CredentialKey(userName, password);
        Soap port = portCache.get(credentialKey);
        if (port == null) {
            port = createPort(userName, password);
            portCache.put(credentialKey, port);
        }
        return port;
    }

    public SalesforceCreationResponse createFeed(String userName, String password, String securityToken) {

        long userID = SecurityUtil.getUserID();

        Soap port;
        try {
            port = getPort(userName, password + securityToken);
        } catch (InvalidIdFault invalidIdFault) {
            return new SalesforceCreationResponse(false, invalidIdFault.getFaultInfo().getExceptionMessage());
        } catch (LoginFault loginFault) {
            return new SalesforceCreationResponse(false, loginFault.getFaultInfo().getExceptionMessage());
        } catch (Error e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }

        try {

            //User user = userStorage.retrieveUser();
            //Account account = userStorage.getAccount(user.getAccount().getAccountID());

            long feedID = createCompositeFeed(port);
            new UserUploadInternalService().createUserFeedLink(userID, feedID, Roles.OWNER);

            FeedDescriptor feedDescriptor = new FeedDescriptor();
            feedDescriptor.setDataFeedID(feedID);
            return new SalesforceCreationResponse(feedDescriptor, true);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private long createCompositeFeed(Soap port) throws SQLException {
        /*SalesforceFeedDefinition feedDefinition = new SalesforceFeedDefinition();
        feedDefinition.setFeedName("Salesforce");
        // get the data...
        CompositeFeedNode accountsNode = createAccountsNode(port);

        feedDefinition.setCompositeFeedNode(accountsNode);
        feedDefinition.populateFields();
        long feedID = feedStorage.addFeedDefinitionData(feedDefinition);
        CompositeSizeVisitor visitor = new CompositeSizeVisitor();
        visitor.visit(accountsNode);
        feedDefinition.setSize(visitor.getSize());
        ListDefinition baseDefinition = new ListDefinition();
        baseDefinition.setDataFeedID(feedID);
        baseDefinition.setRootDefinition(true);
        baseDefinition.setUserBindings(Arrays.asList(new UserToAnalysisBinding(SecurityUtil.getUserID(), UserPermission.OWNER)));
        new AnalysisStorage().saveAnalysis(baseDefinition);
        feedDefinition.setAnalysisDefinitionID(baseDefinition.getAnalysisID());
        feedStorage.updateDataFeedConfiguration(feedDefinition);
        return feedID;*/
        throw new UnsupportedOperationException();
    }

    public CredentialsResponse refresh(SalesforceCredentials credentials, CompositeFeedNode compositeFeedNode) {
        /*final Soap port;
        try {
            port = getPort(credentials.getUserName(), credentials.getPassword() + credentials.getSecurityToken());
        } catch (InvalidIdFault invalidIdFault) {
            return new CredentialsResponse(false, invalidIdFault.getFaultInfo().getExceptionMessage());
        } catch (LoginFault loginFault) {
            return new CredentialsResponse(false, loginFault.getFaultInfo().getExceptionMessage());
        } catch (Error e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }

        try {
            CompositeFeedNodeVisitor visitor = new CompositeFeedNodeVisitor() {

                protected void accept(CompositeFeedNode compositeFeedNode) throws SQLException {
                    SalesforceSubFeedDefinition def = (SalesforceSubFeedDefinition) feedStorage.getFeedDefinitionData(compositeFeedNode.getDataFeedID());
                    def.refresh(port);
                }
            };
            visitor.visit(compositeFeedNode);
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return new CredentialsResponse(true);*/
        throw new UnsupportedOperationException();
    }

    private CompositeFeedNode createAccountsNode(Soap port) {
        /*DataSet dataSet = new SalesforceDataRetrieval().getDataSet(SalesforceDataRetrieval.ACCOUNTS, port);
        ColumnSegmentFactory columnSegmentFactory = new ColumnSegmentFactory();
        PersistableDataSetForm persistable = columnSegmentFactory.createPersistableForm(dataSet);
        SalesforceSubFeedDefinition accountsFeedDefinition = new SalesforceSubFeedDefinition(SalesforceDataRetrieval.ACCOUNTS);
        accountsFeedDefinition.setFields(columnSegmentFactory.getFields());
        accountsFeedDefinition.setFeedName("Salesforce Accounts");
        long accountsID = feedStorage.addFeedDefinitionData(accountsFeedDefinition);
        DataRetrievalManager.instance().storeData(accountsID, persistable);
        CompositeFeedNode accountsNode = new CompositeFeedNode();
        accountsNode.setDataFeedID(accountsID);
        CompositeFeedConnection opportunitiesConnection = new CompositeFeedConnection();
        CompositeFeedNode opportunitiesNode = createOpportunitiesNode(port);
        opportunitiesConnection.setSourceNode(opportunitiesNode);
        opportunitiesConnection.setSourceJoin(AccountsQueryNode.ACCOUNT_ID);
        opportunitiesConnection.setTargetJoin(OpportunitiesQueryNode.ACCOUNT_ID);
        accountsNode.setCompositeFeedConnections(Arrays.asList(opportunitiesConnection));
        return accountsNode;*/
        throw new UnsupportedOperationException();
    }

    private CompositeFeedNode createOpportunitiesNode(Soap port) {
        DataSet dataSet = new SalesforceDataRetrieval().getDataSet(SalesforceDataRetrieval.OPPORTUNITIES, port);
        ColumnSegmentFactory columnSegmentFactory = new ColumnSegmentFactory();
        PersistableDataSetForm persistable = columnSegmentFactory.createPersistableForm(dataSet);
        SalesforceSubFeedDefinition opportunitiesFeedDef = new SalesforceSubFeedDefinition(SalesforceDataRetrieval.OPPORTUNITIES);
        opportunitiesFeedDef.setFields(columnSegmentFactory.getFields());
        opportunitiesFeedDef.setFeedName("Salesforce Opportunities");
        long accountsID = feedStorage.addFeedDefinitionData(opportunitiesFeedDef);
        //DataRetrievalManager.instance().storeData(accountsID, persistable);
        CompositeFeedNode opportunities = new CompositeFeedNode();
        opportunities.setDataFeedID(accountsID);
        return opportunities;
    }

    public FeedDescriptor getLicenseEstablished(long userID) {
        try {
            Connection conn = Database.instance().getConnection();
            FeedDescriptor feedDescriptor = null;
            PreparedStatement linkedPS = null;
            try {
                linkedPS = conn.prepareStatement("SELECT DATA_FEED.DATA_FEED_ID FROM DATA_FEED, USER_TO_FEED " +
                        "WHERE USER_TO_FEED.DATA_FEED_ID = USER_TO_FEED.DATA_FEED_ID AND USER_ID = ? AND USER_ROLE = ? AND " +
                        "DATA_FEED.FEED_TYPE = ?");
                linkedPS.setLong(1, userID);
                linkedPS.setInt(2, Roles.OWNER);
                linkedPS.setInt(3, FeedType.SALESFORCE.getType());
                ResultSet rs = linkedPS.executeQuery();
                if (rs.next()) {
                    long feedID = rs.getLong(1);
                    feedDescriptor = new FeedDescriptor();
                    feedDescriptor.setDataFeedID(feedID);
                }
            } catch (SQLException e) {
                LogClass.error(e);
                throw new RuntimeException(e);
            } finally {
                try {
                    if (linkedPS != null) { linkedPS.close(); }
                } catch (SQLException e) {
                    LogClass.error(e);
                }
                Database.instance().closeConnection(conn);
            }

            return feedDescriptor;
        } catch (RuntimeException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private Soap createPort(String userName, String password) throws InvalidIdFault, LoginFault {
        try {
            URL wsdlLocation = getClass().getClassLoader().getResource("enterprise.wsdl");
            Soap port = new SforceService(wsdlLocation, new QName("urn:enterprise.soap.sforce.com", "SforceService")).getSoap();
            LoginResult loginResponse = port.login(userName, password);

            /*WSBindingProvider bindingProvider = ((WSBindingProvider) port);
            bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, loginResponse.getServerUrl());*/

            //Enable GZip compression
            Map<String, List<String>> httpHeaders = new HashMap<String, List<String>>();
            httpHeaders.put("Content-Encoding", Collections.singletonList("gzip"));
            httpHeaders.put("Accept-Encoding", Collections.singletonList("gzip"));
            /*Map<String, Object> reqContext = bindingProvider.getRequestContext();
            reqContext.put(MessageContext.HTTP_REQUEST_HEADERS, httpHeaders);*/

            SessionHeader sh = new SessionHeader();
            sh.setSessionId(loginResponse.getSessionId());
            JAXBContext jc;
            try {
                jc = JAXBContext.newInstance("com.sforce.soap.enterprise");
            } catch (JAXBException e) {
                LogClass.error(e);
                throw new WebServiceException(e);
            }

            //bindingProvider.setOutboundHeaders(Headers.create((JAXBRIContext) jc, sh));
            return port;
        } catch (LoginFault lf) {
            throw lf;
        } catch (InvalidIdFault iif) {
            throw iif;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public boolean testSFConnect(Credentials credentials) {
        boolean success = true;
        CredentialKey credentialKey = new CredentialKey(credentials.getUserName(), credentials.getPassword());
        if (!portCache.containsKey(credentialKey)) {
            try {
                Soap port = getPort(credentials.getUserName(), credentials.getPassword());
                portCache.put(credentialKey, port);
            } catch (Exception e) {
                LogClass.error(e);
                success = false;
            }
        }
        return success;
    }

    private static class CredentialKey {
        private String userName;
        private String password;

        private CredentialKey(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CredentialKey that = (CredentialKey) o;

            return password.equals(that.password) && userName.equals(that.userName);

        }

        public int hashCode() {
            int result;
            result = userName.hashCode();
            result = 31 * result + password.hashCode();
            return result;
        }
    }
}
