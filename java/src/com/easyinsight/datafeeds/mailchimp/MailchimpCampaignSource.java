package com.easyinsight.datafeeds.mailchimp;

import com.csvreader.CsvReader;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
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
public class MailchimpCampaignSource extends ServerDataSourceDefinition {

    public static final String CAMPAIGN_ID = "Campaign ID";
    public static final String NAME = "Campaign Name"; // title
    public static final String LIST_ID = "Campaign List ID"; // list_id
    public static final String EMAILS_SENT = "Emails Sent";
    public static final String SEND_TIME = "Campaign Sent At";

    /*
    {"pvmaanen1@gmail.com":
    [{"action":"open","timestamp":"2014-10-15 16:08:11","url":null,"ip":"172.56.7.105"},
    {"action":"open","timestamp":"2014-10-15 16:09:07","url":null,"ip":"172.56.7.105"}]}
     */

    public MailchimpCampaignSource() {
        setFeedName("Campaigns");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.MAILCHIMP_CAMPAIGN;
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {

        fieldBuilder.addField(CAMPAIGN_ID, new AnalysisDimension());
        fieldBuilder.addField(NAME, new AnalysisDimension());
        fieldBuilder.addField(LIST_ID, new AnalysisDimension());
        fieldBuilder.addField(EMAILS_SENT, new AnalysisMeasure());
        fieldBuilder.addField(SEND_TIME, new AnalysisDateDimension());

    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            MailchimpCompositeSource mailchimpCompositeSource = (MailchimpCompositeSource) parentDefinition;
            String apiKey = mailchimpCompositeSource.getMailchimpApiKey();
            String dataCenter = apiKey.split("-")[1];
            int offset = 0;
            int count;
            List<String> campaignIDs = new ArrayList<>();
            DataSet dataSet = new DataSet();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            do {
                count = 0;
                PostMethod postMethod = new PostMethod("https://"+dataCenter+".api.mailchimp.com/2.0/campaigns/list.json");
                net.minidev.json.JSONObject jo = new net.minidev.json.JSONObject();
                jo.put("apikey", mailchimpCompositeSource.getMailchimpApiKey());
                jo.put("start", offset / 1000);
                jo.put("limit", 1000);
                StringRequestEntity entity = new StringRequestEntity(jo.toString(), "application/json", "UTF-8");
                postMethod.setRequestEntity(entity);
                HttpClient httpClient = new HttpClient();
                httpClient.executeMethod(postMethod);
                Map m = (Map) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(postMethod.getResponseBodyAsStream());
                List<Map> data = (List<Map>) m.get("data");
                for (Map d : data) {
                    count++;
                    String id = d.get("id").toString();
                    campaignIDs.add(id);
                    IRow row = dataSet.createRow();
                    row.addValue(keys.get(CAMPAIGN_ID), id);
                    row.addValue(keys.get(NAME), getJSONValue(d, "title"));
                    row.addValue(keys.get(LIST_ID), getJSONValue(d, "list_id"));
                    row.addValue(keys.get(EMAILS_SENT), getJSONValue(d, "emails_sent"));
                    Object sendTimeObj = d.get("send_time");
                    if (sendTimeObj != null) {
                        Date date = sdf.parse(sendTimeObj.toString());
                        row.addValue(keys.get(SEND_TIME), date);
                    }
                }
                offset += count;
            } while (count == 1000);
            mailchimpCompositeSource.setCampaignIDs(campaignIDs);
            return dataSet;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
