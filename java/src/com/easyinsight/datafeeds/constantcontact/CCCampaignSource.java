package com.easyinsight.datafeeds.constantcontact;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataStorage;
import nu.xom.*;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: Nov 7, 2010
 * Time: 10:56:37 AM
 */
public class CCCampaignSource extends ConstantContactBaseSource {

    public static final String CAMPAIGN_NAME = "Campaign Name";
    public static final String CAMPAIGN_ID = "Campaign ID";
    public static final String CAMPAIGN_STATUS = "Campaign Status";
    public static final String CAMPAIGN_DATE = "Campaign Date";
    public static final String CAMPAIGN_URL = "Campaign URL";
    public static final String CAMPAIGN_COUNT = "Campaign Count";

    public CCCampaignSource() {
        setFeedName("Campaigns");
    }

    @NotNull
    @Override
    protected List<String> getKeys() {
        return Arrays.asList(CAMPAIGN_NAME, CAMPAIGN_ID, CAMPAIGN_STATUS, CAMPAIGN_DATE, CAMPAIGN_COUNT,
                CAMPAIGN_URL);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Connection conn) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(CAMPAIGN_NAME), true));
        items.add(new AnalysisDimension(keys.get(CAMPAIGN_ID), true));
        items.add(new AnalysisDimension(keys.get(CAMPAIGN_STATUS), true));
        items.add(new AnalysisDimension(keys.get(CAMPAIGN_URL), true));
        items.add(new AnalysisDateDimension(keys.get(CAMPAIGN_DATE), true, AnalysisDateDimension.DAY_LEVEL));
        items.add(new AnalysisMeasure(keys.get(CAMPAIGN_COUNT), AggregationTypes.SUM));
        return items;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.CONSTANT_CONTACT_CAMPAIGN;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID) throws ReportException {
        try {
            ConstantContactCompositeSource ccSource = (ConstantContactCompositeSource) parentDefinition;
            DataSet dataSet = new DataSet();
            Document doc = query("https://api.constantcontact.com/ws/customers/" + ccSource.getCcUserName() + "/campaigns", ccSource.getTokenKey(), ccSource.getTokenSecret(), parentDefinition);
            boolean hasMoreData;
            do {
                hasMoreData = false;
                Nodes nodes = doc.query("/feed/entry");
                for (int i = 0; i < nodes.size(); i++) {

                    Node node = nodes.get(i);
                    Date date = null;
                    String dateString = queryField(node, "content/Campaign/Date/text()");
                    if (dateString != null) {
                        date = DATE_FORMAT.parse(dateString);
                        long time = date.getTime();
                        
                        long delta = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 90;
                        if (time < delta) {
                            continue;
                        }

                    }
                    IRow row = dataSet.createRow();
                    String idString = node.query("id/text()").get(0).getValue();
                    String id = idString.split("/")[7];
                    String name = node.query("content/Campaign/Name/text()").get(0).getValue();
                    String status = node.query("content/Campaign/Status/text()").get(0).getValue();
                    row.addValue(CAMPAIGN_ID, id);
                    row.addValue(CAMPAIGN_NAME, name);
                    row.addValue(CAMPAIGN_STATUS, status);
                    row.addValue(CAMPAIGN_DATE, new DateValue(date));
                    row.addValue(CAMPAIGN_STATUS, status);
                    row.addValue(CAMPAIGN_COUNT, 1);
                }
                Nodes links = doc.query("/feed/link");

                for (int i = 0; i < links.size(); i++) {
                    Element link = (Element) links.get(i);
                    Attribute attribute = link.getAttribute("rel");
                    if (attribute != null && "next".equals(attribute.getValue())) {
                        String linkURL = link.getAttribute("href").getValue();
                        hasMoreData = true;
                        doc = query("https://api.constantcontact.com" + linkURL, ccSource.getTokenKey(), ccSource.getTokenSecret(), parentDefinition);
                        break;
                    }
                }
            } while (hasMoreData);
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
