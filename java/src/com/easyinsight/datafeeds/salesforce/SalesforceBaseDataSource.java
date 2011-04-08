package com.easyinsight.datafeeds.salesforce;

import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.datafeeds.constantcontact.ConstantContactCompositeSource;
import com.easyinsight.kpi.KPI;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.users.Credentials;
import com.easyinsight.users.Account;
import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.users.TokenStorage;
import com.sforce.soap.partner.*;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.signature.HmacSha1MessageSigner;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.BindingProvider;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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

    public static final String OPPORTUNITY = "Opportunity";
    public static final String ACCOUNT = "Account";
    public static final String LEAD = "Lead";
    public static final String CAMPAIGN = "Campaign";
    public static final String CONTACT = "Contact";
    public static final String CASE = "Case";

    @Transient
    private String tokenKey;
    @Transient
    private String tokenSecret;
    @Transient
    private String pin;

    @Transient
    protected SessionHeader sessionHeader;

    @Transient
    protected Soap service;

    @Override
    public Map<String, Key> newDataSourceFields(FeedDefinition parentDefinition) {
        try {
            Map<String, Key> keys = new HashMap<String, Key>();
            /*if(service == null || sessionHeader == null)
                login(credentials);
            keys.putAll(getKeysForObject(OPPORTUNITY));
            keys.putAll(getKeysForObject(ACCOUNT));
            keys.putAll(getKeysForObject(LEAD));
            keys.putAll(getKeysForObject(CAMPAIGN));
            keys.putAll(getKeysForObject(CONTACT));
            keys.putAll(getKeysForObject(CASE));*/
            return keys;
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
        //throw new UnsupportedOperationException();
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
                URL url = new URL("https://na5.salesforce.com/services/data/v20.0/query/?q=SELECT+name+from+Account");
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                consumer.sign(httpConn);
                httpConn.connect();
                byte[] b = new byte[1024];
                String out = "";
                while(httpConn.getInputStream().read(b) > 0) {
                    String s = new String(b);
                    out = out + s;
                }

                System.out.println(out);

            }
        } catch (OAuthCommunicationException oe) {
            throw new UserMessageException(oe, "The specified verifier token was rejected. Please try to authorize access again.");
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

    public boolean isConfigured() {
        return true;
    }

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        return new SalesforceFeed();
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.SALESFORCE;
    }

    private Map<String, Key> getKeysForObject(String s) throws InvalidSObjectFault, UnexpectedErrorFault {
        Map<String, Key> keys = new HashMap<String, Key>();
        DescribeSObjectResultType response = service.describeSObject(sessionHeader, s);
        for(FieldType f : response.getFields()) {
            if(!"REFERENCE".equals(f.getType().name()))
                keys.put(s + "." + f.getName(), new NamedKey(s + "." + f.getName()));
        }
        return keys;
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        try {
            /*if(sessionHeader == null)
                login(credentials);*/

            List<AnalysisItem> items = new LinkedList<AnalysisItem>();
            /*List<AnalysisItem> opportunityItems = getAnalysisItemsForObject(OPPORTUNITY, keys);
            List<AnalysisItem> accountItems = getAnalysisItemsForObject(ACCOUNT, keys);
            List<AnalysisItem> leadItems = getAnalysisItemsForObject(LEAD, keys);
            List<AnalysisItem> campaignItems = getAnalysisItemsForObject(CAMPAIGN, keys);
            List<AnalysisItem> caseItems = getAnalysisItemsForObject(CASE, keys);
            List<AnalysisItem> contactItems = getAnalysisItemsForObject(CONTACT, keys);
            defineFolder("Opportunities").getChildItems().addAll(opportunityItems);
            defineFolder("Accounts").getChildItems().addAll(accountItems);
            defineFolder("Leads").getChildItems().addAll(leadItems);
            defineFolder("Campaigns").getChildItems().addAll(campaignItems);
            defineFolder("Cases").getChildItems().addAll(caseItems);
            defineFolder("Contacts").getChildItems().addAll(contactItems);
            items.addAll(opportunityItems);
            items.addAll(accountItems);
            items.addAll(leadItems);
            items.addAll(campaignItems);
            items.addAll(caseItems);
            items.addAll(contactItems);*/
            return items;
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
        //throw new UnsupportedOperationException();
    }

    public int getDataSourceType() {
        return DataSourceInfo.LIVE;
    }

    private List<AnalysisItem> getAnalysisItemsForObject(String s, Map<String, Key> keys) throws InvalidSObjectFault, UnexpectedErrorFault {
        List<AnalysisItem> items = new LinkedList<AnalysisItem>();
        DescribeSObjectResultType response = service.describeSObject(sessionHeader, s);
        for(FieldType f : response.getFields()) {
            String fieldName = s + "." + f.getName();
            String friendlyName = f.getName();
            if (friendlyName.endsWith("__c")) {
                friendlyName = friendlyName.substring(0, friendlyName.length() - 3);
            }
            if("BOOLEAN".equals(f.getType().name()) ||
                    "STRING".equals(f.getType().name()) ||
                    "TEXTAREA".equals(f.getType().name()) ||
                    "PHONE".equals(f.getType().name()) ||
                    "URL".equals(f.getType().name()) ||
                    "PICKLIST".equals(f.getType().name())) {
                items.add(new AnalysisDimension(keys.get(fieldName), friendlyName));
            }
            else if("DOUBLE".equals(f.getType().name()) || "INT".equals(f.getType().name())) {
                items.add(new AnalysisMeasure(keys.get(fieldName), friendlyName, AggregationTypes.SUM));
            } else if ("CURRENCY".equals(f.getType().name())) {
                AnalysisMeasure analysisMeasure = new AnalysisMeasure(keys.get(fieldName), friendlyName, AggregationTypes.SUM);
                FormattingConfiguration formattingConfiguration = new FormattingConfiguration();
                formattingConfiguration.setFormattingType(FormattingConfiguration.CURRENCY);
                analysisMeasure.setFormattingConfiguration(formattingConfiguration);
                items.add(analysisMeasure);
            } else if ("PERCENT".equals(f.getType().name())) {
                AnalysisMeasure analysisMeasure = new AnalysisMeasure(keys.get(fieldName), friendlyName, AggregationTypes.AVERAGE);
                FormattingConfiguration formattingConfiguration = new FormattingConfiguration();
                formattingConfiguration.setFormattingType(FormattingConfiguration.PERCENTAGE);
                analysisMeasure.setFormattingConfiguration(formattingConfiguration);
                items.add(analysisMeasure);
            } else if ("DATE".equals(f.getType().name())) {
                items.add(new AnalysisDateDimension(keys.get(fieldName), friendlyName, AnalysisDateDimension.DAY_LEVEL));
            } else if ("DATETIME".equals(f.getType().name())) {
                AnalysisDateDimension dateDimension = new AnalysisDateDimension(keys.get(fieldName), friendlyName, AnalysisDateDimension.DAY_LEVEL);
                dateDimension.setCustomDateFormat("yyyy-MM-dd'T'HH:mm:SS.sss'Z'");
                items.add(dateDimension);
            }
        }
        return items;
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) {
        return new DataSet();
    }

    @Override
    public String validateCredentials() {
        /*try {
            if (sessionHeader == null) login(credentials);
            return null;
        } catch (Exception e) {
            return e.getMessage();
        }*/
        throw new UnsupportedOperationException();
    }

    private void login(Credentials c) throws InvalidIdFault, UnexpectedErrorFault, LoginFault {
        if(service == null) {
            SforceService sf = new SforceService();
            service = sf.getSoap();
        }

        LoginResultType result = service.login(c.getUserName(), c.getPassword());
        ((BindingProvider) service).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, result.getServerUrl());
        sessionHeader = new SessionHeader();
        sessionHeader.setSessionId(result.getSessionId());
    }

    @NotNull
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return new ArrayList<String>();
    }

    @Override
    public List<KPI> createKPIs() {
        List<KPI> kpis = new ArrayList<KPI>();
        
        return kpis;
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
                throw new ReportException(new DataSourceConnectivityReportFault("You need to reauthorize Easy Insight to access your Constant Contact data.", parentSource));
            } else {
                throw e;
            }
        }
    }
}
