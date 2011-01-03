package com.easyinsight.datafeeds.linkedin;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.kpi.KPI;
import com.easyinsight.kpi.KPIUtil;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.users.Account;
import flex.messaging.FlexContext;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: May 6, 2010
 * Time: 8:34:00 PM
 */
public class LinkedInDataSource extends ServerDataSourceDefinition {
    
    public static final String NAME = "Name";
    public static final String HEADLINE = "Headline";
    public static final String CURRENT_TITLE = "Current Title";
    public static final String CURRENT_COMPANY = "Current Company";
    public static final String START_DATE = "Current Start Date";
    public static final String INDUSTRY = "Industry";
    public static final String PUBLIC_PROFILE_URL = "Public Profile URL";
    public static final String COUNT = "Count";
    public static final String NUMBER_CONNECTIONS = "Number of Connections";

    private String tokenKey;
    private String tokenSecret;

    private String myName;
    
    public LinkedInDataSource() {
        setFeedName("LinkedIn");
    }

    @Override
    public String validateCredentials() {
        return null;
    }

    @NotNull
    @Override
    protected List<String> getKeys() {
        return Arrays.asList(NAME, HEADLINE, INDUSTRY, PUBLIC_PROFILE_URL, COUNT, NUMBER_CONNECTIONS, CURRENT_TITLE,
                CURRENT_COMPANY, START_DATE);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Connection conn) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        URLLink nameLink = new URLLink();
        nameLink.setUrl("[Public Profile URL]");
        nameLink.setLabel("View in LinkedIn");
        AnalysisDimension nameDimension = new AnalysisDimension(keys.get(NAME), true);
        nameDimension.setLinks(Arrays.asList((Link) nameLink));
        items.add(nameDimension);
        items.add(new AnalysisDimension(keys.get(HEADLINE), true));
        items.add(new AnalysisDimension(keys.get(INDUSTRY), true));
        items.add(new AnalysisDimension(keys.get(PUBLIC_PROFILE_URL), true));
        items.add(new AnalysisDimension(keys.get(CURRENT_TITLE), true));
        items.add(new AnalysisDimension(keys.get(CURRENT_COMPANY), true));
        items.add(new AnalysisDateDimension(keys.get(START_DATE), true, AnalysisDateDimension.MONTH_LEVEL));
        items.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(NUMBER_CONNECTIONS), AggregationTypes.SUM));
        return items;
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.STORED_PULL;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.LINKEDIN;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.PERSONAL;
    }

    @Override
    public void exchangeTokens(EIConnection conn) throws Exception {
        try {
            if (pin != null && !"".equals(pin) && tokenKey == null && tokenSecret == null) {
                OAuthConsumer consumer = (OAuthConsumer) FlexContext.getHttpRequest().getSession().getAttribute("oauthConsumer");
                OAuthProvider provider = (OAuthProvider) FlexContext.getHttpRequest().getSession().getAttribute("oauthProvider");
                provider.retrieveAccessToken(consumer, pin.trim());
                tokenKey = consumer.getToken();
                tokenSecret = consumer.getTokenSecret();
                pin = null;
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM LINKEDIN_DATA_SOURCE WHERE FEED_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO LINKEDIN_DATA_SOURCE (TOKEN_KEY, TOKEN_SECRET_KEY, FEED_ID) VALUES (?, ?, ?)");
        insertStmt.setString(1, tokenKey);
        insertStmt.setString(2, tokenSecret);
        insertStmt.setLong(3, getDataFeedID());
        insertStmt.execute();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT TOKEN_KEY, TOKEN_SECRET_KEY FROM LINKEDIN_DATA_SOURCE WHERE FEED_ID = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            tokenKey = rs.getString(1);
            tokenSecret = rs.getString(2);
        }
    }

    public static final String CONSUMER_KEY = "pMAaMYgowzMITTDFzMoaIbHsCni3iBZKzz3bEvUYoIHlaSAEv78XoOsmpch9YkLq";
    public static final String CONSUMER_SECRET = "leKpqRVV3M8CMup_x6dY8THBiKT-T4PXSs3cpSVXp0kaMS4AiZYW830yRvH6JU2O";

    private String pin;

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID) {
        DataSet dataSet = new DataSet();
        try {
            Builder builder = new Builder();
            OAuthConsumer consumer = new DefaultOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
            consumer.setTokenWithSecret(tokenKey, tokenSecret);

            URL profileURL = new URL("http://api.linkedin.com/v1/people/~:(id,first-name,last-name,headline,industry,public-profile-url,num-connections,positions)");
            HttpURLConnection profileRequest = (HttpURLConnection) profileURL.openConnection();
            consumer.sign(profileRequest);
            profileRequest.connect();

            Document profileDoc = builder.build(profileRequest.getInputStream());
            Nodes profile = profileDoc.query("/person");
            for (int i = 0; i < profile.size(); i++) {
                Node person = profile.get(i);
                fromPerson(keys, dataSet, person);
            }

            // create an HTTP request to a protected resource
            URL url = new URL("http://api.linkedin.com/v1/people/~/connections:(id,first-name,last-name,headline,industry,public-profile-url,num-connections,positions)");
            HttpURLConnection request = (HttpURLConnection) url.openConnection();

            // sign the request
            consumer.sign(request);

            // send the request
            request.connect();


            Document connectionDoc = builder.build(request.getInputStream());
            Nodes people = connectionDoc.query("/connections/person");
            for (int i = 0; i < people.size(); i++) {
                Node person = people.get(i);
                fromPerson(keys, dataSet, person);
            }
        } catch (IOException ioe) {
            throw new ReportException(new DataSourceConnectivityReportFault("You need to reauthorize Easy Insight to access your LinkedIn data.", this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dataSet;
    }

    private void fromPerson(Map<String, Key> keys, DataSet dataSet, Node person) {
        IRow row = dataSet.createRow();
        Nodes positions = person.query("positions/position");
        String currentTitle = null;
        String currentEmployer = null;
        Date startDate = null;
        if (positions.size() > 0) {
            Node positionNode = positions.get(0);
            currentTitle = queryField(positionNode, "title/text()");
            currentEmployer = queryField(positionNode, "company/name/text()");
            Nodes startDateNodes = positionNode.query("start-date");
            if (startDateNodes.size() > 0) {
                Node startDateNode = startDateNodes.get(0);
                String yearString = queryField(startDateNode, "year/text()");
                String monthString = queryField(startDateNode, "month/text()");
                if (yearString != null && monthString != null) {
                    int year = Integer.parseInt(yearString);
                    int month = Integer.parseInt(monthString);
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.MONTH, month);
                    startDate = cal.getTime();
                }
            }
        }
        String firstName = queryField(person, "first-name/text()");
        String lastName = queryField(person, "last-name/text()");
        String headline = queryField(person, "headline/text()");
        String industry = queryField(person, "industry/text()");
        String publicProfileURL = queryField(person, "public-profile-url/text()");
        String numConnectionsString = queryField(person, "num-connections/text()");

        int numConnections = 0;
        if (numConnectionsString != null) numConnections = Integer.parseInt(numConnectionsString);
        row.addValue(keys.get(NAME), firstName + " " + lastName);
        row.addValue(keys.get(HEADLINE), headline);
        row.addValue(keys.get(INDUSTRY), industry);
        row.addValue(keys.get(CURRENT_COMPANY), currentEmployer);
        row.addValue(keys.get(CURRENT_TITLE), currentTitle);
        row.addValue(keys.get(START_DATE), startDate);
        row.addValue(keys.get(PUBLIC_PROFILE_URL), publicProfileURL);
        row.addValue(keys.get(COUNT), 1);
        row.addValue(keys.get(NUMBER_CONNECTIONS), numConnections);
    }

    protected static String queryField(Node n, String xpath) {
        Nodes results = n.query(xpath);
        if(results.size() > 0)
            return results.get(0).getValue();
        else
            return null;
    }

    @Override
    public List<KPI> createKPIs() {
        List<KPI> kpis = Arrays.asList(KPIUtil.createKPIWithFilters("Total Number of Connections, Including You", "user.png", (AnalysisMeasure) findAnalysisItem(LinkedInDataSource.COUNT),
                new ArrayList<FilterDefinition>(), KPI.GOOD, 1),
                KPIUtil.createKPIWithFilters("Network Size", "user.png", (AnalysisMeasure) findAnalysisItem(LinkedInDataSource.NUMBER_CONNECTIONS),
                new ArrayList<FilterDefinition>(), KPI.GOOD, 1));
        FilterValueDefinition filter = new FilterValueDefinition(findAnalysisItem(LinkedInDataSource.NAME), true, Arrays.asList((Object) myName));
        return kpis;
    }
}
