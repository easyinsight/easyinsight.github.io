package com.easyinsight.datafeeds.linkedin;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 3/24/14
 * Time: 7:53 AM
 */
public class LinkedInPeopleSource extends ServerDataSourceDefinition {

    public static final String NAME = "Name";
    public static final String HEADLINE = "Headline";
    public static final String CURRENT_TITLE = "Current Title";
    public static final String CURRENT_COMPANY = "Current Company";
    public static final String START_DATE = "Current Start Date";
    public static final String INDUSTRY = "Industry";
    public static final String PUBLIC_PROFILE_URL = "Public Profile URL";
    public static final String COUNT = "Count";
    public static final String NUMBER_CONNECTIONS = "Number of Connections";

    public LinkedInPeopleSource() {
        setFeedName("People");
    }

    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(NAME, new AnalysisDimension());
        fieldBuilder.addField(HEADLINE, new AnalysisText());
        fieldBuilder.addField(CURRENT_TITLE, new AnalysisDimension());
        fieldBuilder.addField(CURRENT_COMPANY, new AnalysisDimension());
        fieldBuilder.addField(START_DATE, new AnalysisDateDimension());
        fieldBuilder.addField(INDUSTRY, new AnalysisDimension());
        fieldBuilder.addField(PUBLIC_PROFILE_URL, new AnalysisDimension());
        fieldBuilder.addField(COUNT, new AnalysisMeasure());
        fieldBuilder.addField(NUMBER_CONNECTIONS, new AnalysisMeasure());
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        try {
            LinkedInCompositeSource linkedInCompositeSource = (LinkedInCompositeSource) parentDefinition;
            Builder builder = new Builder();
            OAuthConsumer consumer = new DefaultOAuthConsumer(LinkedInCompositeSource.CONSUMER_KEY, LinkedInCompositeSource.CONSUMER_SECRET);
            consumer.setTokenWithSecret(linkedInCompositeSource.getTokenKey(), linkedInCompositeSource.getTokenSecret());

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
            int start = 0;
            int count;
            do {
                count = 0;
                String urlString = "http://api.linkedin.com/v1/people/~/connections:(id,first-name,last-name,headline,industry,public-profile-url,num-connections,positions)?start=" + start + "&count=50";
                System.out.println(urlString);
                URL url = new URL(urlString);
                HttpURLConnection request = (HttpURLConnection) url.openConnection();

                // sign the request
                consumer.sign(request);

                // send the request
                request.connect();


                Document connectionDoc = builder.build(request.getInputStream());
                Nodes people = connectionDoc.query("/connections/person");
                for (int i = 0; i < people.size(); i++) {
                    Node person = people.get(i);
                    start++;
                    count++;
                    fromPerson(keys, dataSet, person);
                }
            } while (count == 50);
        } catch (IOException ioe) {
            ioe.printStackTrace();
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
        if (results.size() > 0)
            return results.get(0).getValue();
        else
            return null;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.LINKEDIN_PEOPLE;
    }
}
