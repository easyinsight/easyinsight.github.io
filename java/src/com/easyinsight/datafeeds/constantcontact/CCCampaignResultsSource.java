package com.easyinsight.datafeeds.constantcontact;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import nu.xom.*;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;
import java.util.concurrent.*;

/**
 * User: jamesboe
 * Date: Nov 7, 2010
 * Time: 10:57:24 AM
 */
public class CCCampaignResultsSource extends ConstantContactBaseSource {

    public static final String CAMPAIGN_ID = "Source Campaign ID";
    public static final String CONTACT_ID = "Source Contact ID";
    public static final String SENT_COUNT = "Sent Count";
    public static final String OPEN_COUNT = "Open Count";
    public static final String CLICK_COUNT = "Click Count";
    public static final String BOUNCE_COUNT = "Bounce Count";
    public static final String FORWARD_COUNT = "Forward Count";
    public static final String OPT_OUT_COUNT = "Opt Out Count";
    public static final String SPAM_REPORT_COUNT = "Spam Report Count";
    public static final String EVENT_DATE = "Event Date";
    public static final String OPEN_RATE = "Open Rate";
    public static final String CLICK_RATE = "Click Rate";
    public static final String FORWARD_RATE = "Forward Rate";


    public CCCampaignResultsSource() {
        setFeedName("Campaign Results");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(CAMPAIGN_ID, CONTACT_ID, SENT_COUNT, CLICK_COUNT, OPEN_COUNT,
                BOUNCE_COUNT, FORWARD_COUNT, OPT_OUT_COUNT, SPAM_REPORT_COUNT, EVENT_DATE, OPEN_RATE, CLICK_RATE, FORWARD_RATE);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(CONTACT_ID), true));
        items.add(new AnalysisDimension(keys.get(CAMPAIGN_ID), true));
        items.add(new AnalysisDateDimension(keys.get(EVENT_DATE), true, AnalysisDateDimension.DAY_LEVEL));
        items.add(new AnalysisMeasure(keys.get(SENT_COUNT), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(OPEN_COUNT), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(CLICK_COUNT), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(BOUNCE_COUNT), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(FORWARD_COUNT), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(OPT_OUT_COUNT), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(SPAM_REPORT_COUNT), AggregationTypes.SUM));

        AnalysisCalculation openRate = new AnalysisCalculation();
        openRate.setKey(keys.get(OPEN_RATE));
        openRate.setCalculationString("([Open Count] / [Sent Count]) * 100");
        FormattingConfiguration openConfiguration = openRate.getFormattingConfiguration();
        openConfiguration.setFormattingType(FormattingConfiguration.PERCENTAGE);
        openRate.setFormattingConfiguration(openConfiguration);
        items.add(openRate);

        AnalysisCalculation clickRate = new AnalysisCalculation();
        clickRate.setKey(keys.get(CLICK_RATE));
        clickRate.setCalculationString("([Click Count] / [Sent Count]) * 100");
        FormattingConfiguration clickConfiguration = clickRate.getFormattingConfiguration();
        clickConfiguration.setFormattingType(FormattingConfiguration.PERCENTAGE);
        clickRate.setFormattingConfiguration(clickConfiguration);
        items.add(clickRate);

        AnalysisCalculation forwardRate = new AnalysisCalculation();
        forwardRate.setKey(keys.get(FORWARD_RATE));
        forwardRate.setCalculationString("([Forward Count] / [Sent Count]) * 100");
        FormattingConfiguration forwardConfiguration = forwardRate.getFormattingConfiguration();
        forwardConfiguration.setFormattingType(FormattingConfiguration.PERCENTAGE);
        forwardRate.setFormattingConfiguration(forwardConfiguration);
        items.add(forwardRate);

        return items;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.CONSTANT_CONTACT_CAMPAIGN_RESULTS;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, final IDataStorage dataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
        ThreadPoolExecutor tpe = new ThreadPoolExecutor(15, 15, 5000, TimeUnit.MINUTES, queue);
        try {
            final ConstantContactCompositeSource ccSource = (ConstantContactCompositeSource) parentDefinition;

            List<Campaign> campaigns = ccSource.getOrCreateCampaignCache().getOrCreateCampaigns(ccSource);

            for (int i = 0; i < campaigns.size(); i++) {
                Campaign campaign = campaigns.get(i);
                campaign.setCamapignNumber(i);
            }

            final CountDownLatch latch = new CountDownLatch(campaigns.size());

            DefaultHttpClient initClient = new DefaultHttpClient();

            ClientConnectionManager mgr = initClient.getConnectionManager();
            HttpParams params = initClient.getParams();
            ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(params, mgr.getSchemeRegistry());
            manager.setMaxTotal(10);
            manager.setDefaultMaxPerRoute(10);

            final DefaultHttpClient client = new DefaultHttpClient(manager, params);

            for (final Campaign campaign : campaigns) {
                tpe.execute(new Runnable() {

                    public void run() {
                        DataSet dataSet = new DataSet();
                        Date date = campaign.getDate();
                        long time = date.getTime();
                        long delta = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 90;
                        if (time < delta) {
                        } else {
                            try {
                                String eventsURL = "https://api.constantcontact.com/ws/customers/" + ccSource.getCcUserName() + "/campaigns/" + campaign.getId() + "/events/?pageSize=200";

                                Document eventsDoc = query(eventsURL, ccSource.getTokenKey(), ccSource.getTokenSecret(), ccSource, client);
                                Nodes eventNodes = eventsDoc.query("/service/workspace/collection");
                                for (int j = 0; j < eventNodes.size(); j++) {
                                    Element eventElement = (Element) eventNodes.get(j);
                                    Attribute attribute = eventElement.getAttribute("href");

                                    String string = "https://api.constantcontact.com" + attribute.getValue();
                                    string = string.substring(0, 45) + ccSource.getCcUserName() + string.substring(string.indexOf("/campaigns"));
                                    Document eventDetailDoc = query(string, ccSource.getTokenKey(), ccSource.getTokenSecret(), ccSource, client);
                                    boolean hasMoreEvents;
                                    do {
                                        hasMoreEvents = false;
                                        Nodes sendNodes = eventDetailDoc.query("/feed/entry/content/SentEvent");
                                        for (int k = 0; k < sendNodes.size(); k++) {
                                            Node sendNode = sendNodes.get(k);

                                            IRow row = dataSet.createRow();
                                            String contactID = sendNode.query("Contact/EmailAddress/text()").get(0).getValue();
                                            row.addValue(CONTACT_ID, contactID);
                                            row.addValue(CAMPAIGN_ID, campaign.getId());
                                            row.addValue(SENT_COUNT, 1);
                                            row.addValue(EVENT_DATE, sendNode.query("EventTime/text()").get(0).getValue());
                                        }

                                        Nodes bounceNodes = eventDetailDoc.query("/feed/entry/content/BounceEvent");
                                        for (int k = 0; k < bounceNodes.size(); k++) {
                                            Node sendNode = bounceNodes.get(k);

                                            IRow row = dataSet.createRow();
                                            String contactID = sendNode.query("Contact/EmailAddress/text()").get(0).getValue();
                                            row.addValue(CONTACT_ID, contactID);
                                            row.addValue(CAMPAIGN_ID, campaign.getId());
                                            row.addValue(BOUNCE_COUNT, 1);
                                            row.addValue(EVENT_DATE, sendNode.query("EventTime/text()").get(0).getValue());
                                        }

                                        Nodes openNodes = eventDetailDoc.query("/feed/entry/content/OpenEvent");
                                        for (int k = 0; k < openNodes.size(); k++) {
                                            Node sendNode = openNodes.get(k);
                                            String contactID = sendNode.query("Contact/EmailAddress/text()").get(0).getValue();
                                            IRow row = dataSet.createRow();
                                            row.addValue(CONTACT_ID, contactID);
                                            row.addValue(CAMPAIGN_ID, campaign.getId());
                                            row.addValue(OPEN_COUNT, 1);
                                            row.addValue(EVENT_DATE, sendNode.query("EventTime/text()").get(0).getValue());
                                        }

                                        Nodes clickNodes = eventDetailDoc.query("/feed/entry/content/ClickEvent");
                                        for (int k = 0; k < clickNodes.size(); k++) {
                                            Node sendNode = clickNodes.get(k);
                                            String contactID = sendNode.query("Contact/EmailAddress/text()").get(0).getValue();
                                            IRow row = dataSet.createRow();
                                            row.addValue(CONTACT_ID, contactID);
                                            row.addValue(CAMPAIGN_ID, campaign.getId());
                                            row.addValue(CLICK_COUNT, 1);
                                            row.addValue(EVENT_DATE, sendNode.query("EventTime/text()").get(0).getValue());
                                        }

                                        Nodes forwardNodes = eventDetailDoc.query("/feed/entry/content/ForwardEvent");
                                        for (int k = 0; k < forwardNodes.size(); k++) {
                                            Node sendNode = forwardNodes.get(k);
                                            String contactID = sendNode.query("Contact/EmailAddress/text()").get(0).getValue();
                                            IRow row = dataSet.createRow();
                                            row.addValue(CONTACT_ID, contactID);
                                            row.addValue(CAMPAIGN_ID, campaign.getId());
                                            row.addValue(FORWARD_COUNT, 1);
                                            row.addValue(EVENT_DATE, sendNode.query("EventTime/text()").get(0).getValue());
                                        }

                                        Nodes optOutNodes = eventDetailDoc.query("/feed/entry/content/OptOutEvent");
                                        for (int k = 0; k < optOutNodes.size(); k++) {
                                            Node sendNode = optOutNodes.get(k);

                                            IRow row = dataSet.createRow();
                                            String contactID = sendNode.query("Contact/EmailAddress/text()").get(0).getValue();
                                            row.addValue(CONTACT_ID, contactID);
                                            row.addValue(CAMPAIGN_ID, campaign.getId());
                                            row.addValue(OPT_OUT_COUNT, 1);
                                            row.addValue(EVENT_DATE, sendNode.query("EventTime/text()").get(0).getValue());
                                        }

                                        Nodes links = eventDetailDoc.query("/feed/link");
                                        for (int k = 0; k < links.size(); k++) {
                                            Element link = (Element) links.get(k);
                                            Attribute relAttribute = link.getAttribute("rel");
                                            if (relAttribute != null && "next".equals(relAttribute.getValue())) {
                                                String linkURL = link.getAttribute("href").getValue();
                                                hasMoreEvents = true;
                                                String linkURLString = "https://api.constantcontact.com" + linkURL;
                                                linkURLString = linkURLString.substring(0, 45) + ccSource.getCcUserName() + linkURLString.substring(linkURLString.indexOf("/campaigns"));

                                                eventDetailDoc = query(linkURLString, ccSource.getTokenKey(), ccSource.getTokenSecret(), ccSource, client);
                                                break;
                                            }
                                        }
                                    } while (hasMoreEvents);
                                }
                                dataStorage.insertData(dataSet);
                            } catch (Exception e) {
                                //LogClass.error(e);
                                LogClass.info(e.getMessage());
                            }

                        }
                        System.out.println("finished " + campaign.getCamapignNumber());
                        latch.countDown();
                    }
                });
            }

            latch.await();
            return null;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            tpe.shutdown();
        }
    }

    public static void main(String[] args) {
        System.out.println(new Date(1336608000000L));
    }
}
