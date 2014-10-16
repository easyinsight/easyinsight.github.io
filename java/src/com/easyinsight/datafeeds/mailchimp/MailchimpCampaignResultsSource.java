package com.easyinsight.datafeeds.mailchimp;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import net.minidev.json.parser.JSONParser;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 10/15/14
 * Time: 2:23 PM
 */
public class MailchimpCampaignResultsSource extends ServerDataSourceDefinition {

    public static final String EMAIL = "Campaign Email";
    public static final String EVENT_ID = "Campaign Result ID";
    public static final String TIMESTAMP = "Campaign Timestamp";
    public static final String ACTION = "Campaign Action";
    public static final String COUNT = "Campaign Action Count";
    public static final String CAMPAIGN_ID = "Campaign Result Campaign ID";

    public MailchimpCampaignResultsSource() {
        setFeedName("Campaign Results");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.MAILCHIMP_CAMPAIGN_RESULTS;
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(EMAIL, new AnalysisDimension());
        fieldBuilder.addField(CAMPAIGN_ID, new AnalysisDimension());
        fieldBuilder.addField(ACTION, new AnalysisDimension());
        fieldBuilder.addField(TIMESTAMP, new AnalysisDateDimension());
        fieldBuilder.addField(COUNT, new AnalysisMeasure());
    }

    @Override
    protected boolean clearsData(FeedDefinition parentSource) {
        return false;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            MailchimpCompositeSource mailchimpCompositeSource = (MailchimpCompositeSource) parentDefinition;
            String apiKey = mailchimpCompositeSource.getMailchimpApiKey();
            String dataCenter = apiKey.split("-")[1];
            List<String> campaignIDs = mailchimpCompositeSource.getCampaignIDs();


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            int i = 0;
            for (String id : campaignIDs)
            {
                loadingProgress(0, 0, "Retrieving results for campaign " + i + " of " + campaignIDs.size() + "...", callDataID);
                String urlString = "https://"+dataCenter+".api.mailchimp.com/export/1.0/campaignSubscriberActivity/?apikey="+apiKey+"&id=" + id;
                if (lastRefreshDate != null) {
                    String since = sdf.format(lastRefreshDate);
                    urlString += "&since="+ URLEncoder.encode(since, "UTF-8");
                }
                URL url = new URL(urlString);


                InputStream is = url.openStream();

                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                in.readLine();

                String line;
                DataSet dataSet = new DataSet();

                while ((line = in.readLine()) != null) {
                    ByteArrayInputStream bais = new ByteArrayInputStream(line.getBytes());
                    Map entry = (Map) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(bais);
                    for (Object oe : entry.entrySet()) {

                        Map.Entry e = (Map.Entry) oe;
                        String email = (String) e.getKey();
                        List<Map> events = (List<Map>) e.getValue();
                        for (Map event : events) {
                            String action = event.get("action").toString();
                            String timestamp = event.get("timestamp").toString();
                            Date date = sdf.parse(timestamp);

                            IRow row = dataSet.createRow();
                            row.addValue(keys.get(EMAIL), email);
                            row.addValue(keys.get(ACTION), action);
                            row.addValue(keys.get(TIMESTAMP), date);
                            row.addValue(keys.get(CAMPAIGN_ID), id);
                            row.addValue(keys.get(COUNT), 1);

                        }
                    }
                }
                IDataStorage.insertData(dataSet);
            }

        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return null;
    }
}
