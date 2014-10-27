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
public class MailchimpCampaignDetailSource extends ServerDataSourceDefinition {

    public static final String CAMPAIGN_ID = "Detail Campaign ID";
    public static final String DETAIL_DATE = "Detail Date";
    public static final String DETAIL_EVENT = "Detail Type";
    public static final String DETAIL_EMAIL = "Detail Email";
    public static final String DETAIL_COUNT = "Detail Count";

    public MailchimpCampaignDetailSource() {
        setFeedName("Campaign Details");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.MAILCHIMP_CAMPAIGN_DETAILS;
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(CAMPAIGN_ID, new AnalysisDimension());
        fieldBuilder.addField(DETAIL_EVENT, new AnalysisDimension());
        fieldBuilder.addField(DETAIL_EMAIL, new AnalysisDimension());
        fieldBuilder.addField(DETAIL_DATE, new AnalysisDateDimension());
        fieldBuilder.addField(DETAIL_COUNT, new AnalysisMeasure());
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            MailchimpCompositeSource mailchimpCompositeSource = (MailchimpCompositeSource) parentDefinition;
            String apiKey = mailchimpCompositeSource.getMailchimpApiKey();
            String dataCenter = apiKey.split("-")[1];


            List<String> campaignIDs = mailchimpCompositeSource.getCampaignIDs();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            for (String campaignID : campaignIDs) {
                {
                    try {
                        DataSet dataSet = new DataSet();
                        int offset = 0;
                        int count;
                        do {
                            count = 0;
                            PostMethod postMethod = new PostMethod("https://"+dataCenter+".api.mailchimp.com/2.0/reports/abuse.json");
                            net.minidev.json.JSONObject jo = new net.minidev.json.JSONObject();
                            jo.put("apikey", mailchimpCompositeSource.getMailchimpApiKey());
                            jo.put("cid", campaignID);
                            net.minidev.json.JSONObject opts = new net.minidev.json.JSONObject();
                            opts.put("start", offset / 100);
                            opts.put("limit", 100);
                            jo.put("opts", opts);
                            StringRequestEntity entity = new StringRequestEntity(jo.toString(), "application/json", "UTF-8");
                            postMethod.setRequestEntity(entity);
                            HttpClient httpClient = new HttpClient();
                            httpClient.executeMethod(postMethod);
                            Map m = (Map) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(postMethod.getResponseBodyAsStream());
                            System.out.println(m);
                            List<Map> data = (List<Map>) m.get("data");
                            for (Map d : data) {
                                count++;
                                Date date = sdf.parse(d.get("date").toString());
                                String email = d.get("member").toString();
                                IRow row = dataSet.createRow();
                                row.addValue(keys.get(CAMPAIGN_ID), campaignID);
                                row.addValue(keys.get(DETAIL_COUNT), 1);
                                row.addValue(keys.get(DETAIL_DATE), date);
                                row.addValue(keys.get(DETAIL_EMAIL), email);
                                row.addValue(keys.get(DETAIL_EVENT), "Abuse Report");
                            }
                            IDataStorage.insertData(dataSet);
                        } while (count == 100);
                    } catch (Exception e) {
                        LogClass.error(e);
                    }
                }
                {
                    try {
                        DataSet dataSet = new DataSet();
                        int offset = 0;
                        int count;
                        do {
                            count = 0;
                            PostMethod postMethod = new PostMethod("https://"+dataCenter+".api.mailchimp.com/2.0/reports/unsubscribes.json");
                            net.minidev.json.JSONObject jo = new net.minidev.json.JSONObject();
                            jo.put("apikey", mailchimpCompositeSource.getMailchimpApiKey());
                            jo.put("cid", campaignID);
                            net.minidev.json.JSONObject opts = new net.minidev.json.JSONObject();
                            opts.put("start", offset / 100);
                            opts.put("limit", 100);
                            jo.put("opts", opts);
                            StringRequestEntity entity = new StringRequestEntity(jo.toString(), "application/json", "UTF-8");
                            postMethod.setRequestEntity(entity);
                            HttpClient httpClient = new HttpClient();
                            httpClient.executeMethod(postMethod);
                            Map m = (Map) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(postMethod.getResponseBodyAsStream());
                            System.out.println(m);
                            List<Map> data = (List<Map>) m.get("data");
                            for (Map d : data) {
                                count++;
                                if (d.get("date") != null) {
                                    Date date = sdf.parse(d.get("date").toString());
                                    String email = d.get("member").toString();
                                    IRow row = dataSet.createRow();
                                    row.addValue(keys.get(CAMPAIGN_ID), campaignID);
                                    row.addValue(keys.get(DETAIL_COUNT), 1);
                                    row.addValue(keys.get(DETAIL_DATE), date);
                                    row.addValue(keys.get(DETAIL_EMAIL), email);
                                    row.addValue(keys.get(DETAIL_EVENT), "Unsubscribe");
                                }
                            }
                            IDataStorage.insertData(dataSet);
                        } while (count == 100);
                    } catch (Exception e) {
                        LogClass.error(e);
                    }
                }
                {
                    try {
                        DataSet dataSet = new DataSet();
                        int offset = 0;
                        int count;
                        do {
                            count = 0;
                            PostMethod postMethod = new PostMethod("https://"+dataCenter+".api.mailchimp.com/2.0/reports/bounce-messages.json");
                            net.minidev.json.JSONObject jo = new net.minidev.json.JSONObject();
                            jo.put("apikey", mailchimpCompositeSource.getMailchimpApiKey());
                            jo.put("cid", campaignID);
                            net.minidev.json.JSONObject opts = new net.minidev.json.JSONObject();
                            opts.put("start", offset / 100);
                            opts.put("limit", 100);
                            jo.put("opts", opts);
                            StringRequestEntity entity = new StringRequestEntity(jo.toString(), "application/json", "UTF-8");
                            postMethod.setRequestEntity(entity);
                            HttpClient httpClient = new HttpClient();
                            httpClient.executeMethod(postMethod);
                            Map m = (Map) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(postMethod.getResponseBodyAsStream());
                            System.out.println(m);
                            List<Map> data = (List<Map>) m.get("data");
                            for (Map d : data) {
                                if (d.get("date") != null) {
                                    count++;
                                    Date date = df.parse(d.get("date").toString());
                                    String email = d.get("member").toString();
                                    IRow row = dataSet.createRow();
                                    row.addValue(keys.get(CAMPAIGN_ID), campaignID);
                                    row.addValue(keys.get(DETAIL_COUNT), 1);
                                    row.addValue(keys.get(DETAIL_DATE), date);
                                    row.addValue(keys.get(DETAIL_EMAIL), email);
                                    row.addValue(keys.get(DETAIL_EVENT), "Bounce");
                                }
                            }
                            IDataStorage.insertData(dataSet);
                        } while (count == 100);
                    } catch (Exception e) {
                        LogClass.error(e);
                    }
                }
            }

            return null;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
