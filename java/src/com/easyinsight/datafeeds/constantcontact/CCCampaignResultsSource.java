package com.easyinsight.datafeeds.constantcontact;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

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
    public static final String LINK_ID = "Link ID";


    public CCCampaignResultsSource() {
        setFeedName("Campaign Results");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(CAMPAIGN_ID, CONTACT_ID, SENT_COUNT, CLICK_COUNT, OPEN_COUNT,
                BOUNCE_COUNT, FORWARD_COUNT, OPT_OUT_COUNT, SPAM_REPORT_COUNT, EVENT_DATE, OPEN_RATE, CLICK_RATE, FORWARD_RATE, LINK_ID);
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
        Key linkKey = keys.get(LINK_ID);
        if (linkKey == null) {
            linkKey = new NamedKey(LINK_ID);
        }
        items.add(new AnalysisDimension(linkKey));

        AnalysisCalculation openRate = new AnalysisCalculation();
        openRate.setKey(keys.get(OPEN_RATE));
        openRate.setCalculationString("([Open Count] / [Sent Count]) * 100");
        openRate.setFormattingType(FormattingConfiguration.PERCENTAGE);
        items.add(openRate);

        AnalysisCalculation clickRate = new AnalysisCalculation();
        clickRate.setKey(keys.get(CLICK_RATE));
        clickRate.setCalculationString("([Click Count] / [Sent Count]) * 100");
        clickRate.setFormattingType(FormattingConfiguration.PERCENTAGE);
        items.add(clickRate);

        AnalysisCalculation forwardRate = new AnalysisCalculation();
        forwardRate.setKey(keys.get(FORWARD_RATE));
        forwardRate.setCalculationString("([Forward Count] / [Sent Count]) * 100");
        forwardRate.setFormattingType(FormattingConfiguration.PERCENTAGE);
        items.add(forwardRate);

        return items;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.CONSTANT_CONTACT_CAMPAIGN_RESULTS;
    }

    @Override
    protected boolean clearsData(FeedDefinition parentSource) {
        return false;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, final IDataStorage dataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {

        try {
            final ConstantContactCompositeSource ccSource = (ConstantContactCompositeSource) parentDefinition;

            String lastRefreshString = null;
            if (lastRefreshDate != null) {
                Calendar cal = Calendar.getInstance();
                lastRefreshString = DATE_FORMAT.format(cal.getTime());
            }

            List<Campaign> campaigns = ccSource.getOrCreateCampaignCache().getOrCreateCampaigns(ccSource);

            HttpClient client = new HttpClient();
            for (int i = 0; i < campaigns.size(); i++) {
                Campaign campaign = campaigns.get(i);
                campaign.setCamapignNumber(i);
                if (!"SENT".equals(campaign.getStatus())) {
                    continue;
                }

                DataSet dataSet = new DataSet();

                try {
                    {
                        Map result;
                        if (lastRefreshString == null) {
                            result = query("https://api.constantcontact.com/v2/emailmarketing/campaigns/" + campaign.getId() + "/tracking/clicks?api_key=" + ConstantContactCompositeSource.KEY, ccSource, client);
                        } else {
                            result = query("https://api.constantcontact.com/v2/emailmarketing/campaigns/" + campaign.getId() + "/tracking/clicks?api_key=" + ConstantContactCompositeSource.KEY + "&created_since=" + lastRefreshString, ccSource, client);
                        }
                        Map meta = (Map) result.get("meta");
                        String nextLink = null;
                        if (meta != null) {
                            Map pagination = (Map) meta.get("pagination");
                            if (pagination != null) {
                                Object nextLinkObject = pagination.get("next_link");
                                if (nextLinkObject != null) {
                                    nextLink = "https://api.constantcontact.com" + nextLinkObject.toString() + "&api_key=" + ConstantContactCompositeSource.KEY;
                                }
                            }
                        }

                        boolean hasMoreData;
                        do {
                            List results = (List) result.get("results");
                            hasMoreData = false;
                            for (Object obj : results) {
                                Map node = (Map) obj;
                                String contactID = node.get("contact_id").toString();
                                String clickDateString = node.get("click_date").toString();
                                String linkID = node.get("link_id").toString();
                                IRow row = dataSet.createRow();

                                row.addValue(CONTACT_ID, contactID);
                                row.addValue(CAMPAIGN_ID, campaign.getId());
                                row.addValue(CLICK_COUNT, 1);
                                row.addValue(LINK_ID, linkID);
                                Date clickDate = DATE_FORMAT.parse(clickDateString);
                                row.addValue(EVENT_DATE, new DateValue(clickDate));
                            }
                            if (nextLink != null) {
                                result = query(nextLink, ccSource, client);
                                meta = (Map) result.get("meta");
                                nextLink = null;
                                if (meta != null) {
                                    Map pagination = (Map) meta.get("pagination");
                                    if (pagination != null) {
                                        Object nextLinkObject = pagination.get("next_link");
                                        if (nextLinkObject != null) {
                                            nextLink = "https://api.constantcontact.com" + nextLinkObject.toString() + "&api_key=" + ConstantContactCompositeSource.KEY;
                                        }
                                    }
                                }
                                hasMoreData = true;
                            }
                        } while (hasMoreData);
                    }

                    {
                        Map result;
                        if (lastRefreshString == null) {
                            result = query("https://api.constantcontact.com/v2/emailmarketing/campaigns/" + campaign.getId() + "/tracking/opens?api_key=" + ConstantContactCompositeSource.KEY, ccSource, client);
                        } else {
                            result = query("https://api.constantcontact.com/v2/emailmarketing/campaigns/" + campaign.getId() + "/tracking/opens?api_key=" + ConstantContactCompositeSource.KEY + "&created_since=" + lastRefreshString, ccSource, client);
                        }
                        Map meta = (Map) result.get("meta");
                        String nextLink = null;
                        if (meta != null) {
                            Map pagination = (Map) meta.get("pagination");
                            if (pagination != null) {
                                Object nextLinkObject = pagination.get("next_link");
                                if (nextLinkObject != null) {
                                    nextLink = "https://api.constantcontact.com" + nextLinkObject.toString() + "&api_key=" + ConstantContactCompositeSource.KEY;
                                }
                            }
                        }

                        boolean hasMoreData;
                        do {
                            List results = (List) result.get("results");
                            hasMoreData = false;
                            for (Object obj : results) {
                                Map node = (Map) obj;
                                String contactID = node.get("contact_id").toString();
                                String clickDateString = node.get("open_date").toString();
                                IRow row = dataSet.createRow();

                                row.addValue(CONTACT_ID, contactID);
                                row.addValue(CAMPAIGN_ID, campaign.getId());
                                row.addValue(OPEN_COUNT, 1);
                                Date clickDate = DATE_FORMAT.parse(clickDateString);
                                row.addValue(EVENT_DATE, new DateValue(clickDate));
                            }
                            if (nextLink != null) {
                                result = query(nextLink, ccSource, client);
                                meta = (Map) result.get("meta");
                                nextLink = null;
                                if (meta != null) {
                                    Map pagination = (Map) meta.get("pagination");
                                    if (pagination != null) {
                                        Object nextLinkObject = pagination.get("next_link");
                                        if (nextLinkObject != null) {
                                            nextLink = "https://api.constantcontact.com" + nextLinkObject.toString() + "&api_key=" + ConstantContactCompositeSource.KEY;
                                        }
                                    }
                                }
                                hasMoreData = true;
                            }
                        } while (hasMoreData);
                    }

                    {
                        Map result;
                        if (lastRefreshString == null) {
                            result = query("https://api.constantcontact.com/v2/emailmarketing/campaigns/" + campaign.getId() + "/tracking/sends?api_key=" + ConstantContactCompositeSource.KEY, ccSource, client);
                        } else {
                            result = query("https://api.constantcontact.com/v2/emailmarketing/campaigns/" + campaign.getId() + "/tracking/sends?api_key=" + ConstantContactCompositeSource.KEY + "&created_since=" + lastRefreshString, ccSource, client);
                        }
                        Map meta = (Map) result.get("meta");
                        String nextLink = null;
                        if (meta != null) {
                            Map pagination = (Map) meta.get("pagination");
                            if (pagination != null) {
                                Object nextLinkObject = pagination.get("next_link");
                                if (nextLinkObject != null) {
                                    nextLink = "https://api.constantcontact.com" + nextLinkObject.toString() + "&api_key=" + ConstantContactCompositeSource.KEY;
                                }
                            }
                        }

                        boolean hasMoreData;
                        do {
                            List results = (List) result.get("results");
                            hasMoreData = false;
                            for (Object obj : results) {
                                Map node = (Map) obj;
                                String contactID = node.get("contact_id").toString();
                                String clickDateString = node.get("send_date").toString();
                                IRow row = dataSet.createRow();

                                row.addValue(CONTACT_ID, contactID);
                                row.addValue(CAMPAIGN_ID, campaign.getId());
                                row.addValue(SENT_COUNT, 1);
                                Date clickDate = DATE_FORMAT.parse(clickDateString);
                                row.addValue(EVENT_DATE, new DateValue(clickDate));
                            }
                            if (nextLink != null) {
                                result = query(nextLink, ccSource, client);
                                meta = (Map) result.get("meta");
                                nextLink = null;
                                if (meta != null) {
                                    Map pagination = (Map) meta.get("pagination");
                                    if (pagination != null) {
                                        Object nextLinkObject = pagination.get("next_link");
                                        if (nextLinkObject != null) {
                                            nextLink = "https://api.constantcontact.com" + nextLinkObject.toString() + "&api_key=" + ConstantContactCompositeSource.KEY;
                                        }
                                    }
                                }
                                hasMoreData = true;
                            }
                        } while (hasMoreData);
                    }
                    {
                        Map result;
                        if (lastRefreshString == null) {
                            result = query("https://api.constantcontact.com/v2/emailmarketing/campaigns/" + campaign.getId() + "/tracking/bounces?api_key=" + ConstantContactCompositeSource.KEY, ccSource, client);
                        } else {
                            result = query("https://api.constantcontact.com/v2/emailmarketing/campaigns/" + campaign.getId() + "/tracking/bounces?api_key=" + ConstantContactCompositeSource.KEY + "&created_since=" + lastRefreshString, ccSource, client);
                        }

                        Map meta = (Map) result.get("meta");
                        String nextLink = null;
                        if (meta != null) {
                            Map pagination = (Map) meta.get("pagination");
                            if (pagination != null) {
                                Object nextLinkObject = pagination.get("next_link");
                                if (nextLinkObject != null) {
                                    nextLink = "https://api.constantcontact.com" + nextLinkObject.toString() + "&api_key=" + ConstantContactCompositeSource.KEY;
                                }
                            }
                        }

                        boolean hasMoreData;
                        do {
                            List results = (List) result.get("results");
                            hasMoreData = false;
                            for (Object obj : results) {
                                Map node = (Map) obj;
                                String contactID = node.get("contact_id").toString();
                                String clickDateString = node.get("bounce_date").toString();
                                IRow row = dataSet.createRow();

                                row.addValue(CONTACT_ID, contactID);
                                row.addValue(CAMPAIGN_ID, campaign.getId());
                                row.addValue(BOUNCE_COUNT, 1);
                                Date clickDate = DATE_FORMAT.parse(clickDateString);
                                row.addValue(EVENT_DATE, new DateValue(clickDate));
                            }
                            if (nextLink != null) {
                                result = query(nextLink, ccSource, client);
                                meta = (Map) result.get("meta");
                                nextLink = null;
                                if (meta != null) {
                                    Map pagination = (Map) meta.get("pagination");
                                    if (pagination != null) {
                                        Object nextLinkObject = pagination.get("next_link");
                                        if (nextLinkObject != null) {
                                            nextLink = "https://api.constantcontact.com" + nextLinkObject.toString() + "&api_key=" + ConstantContactCompositeSource.KEY;
                                        }
                                    }
                                }
                                hasMoreData = true;
                            }
                        } while (hasMoreData);
                    }
                    {
                        Map result;
                        if (lastRefreshString == null) {
                            result = query("https://api.constantcontact.com/v2/emailmarketing/campaigns/" + campaign.getId() + "/tracking/unsubscribes?api_key=" + ConstantContactCompositeSource.KEY, ccSource, client);
                        } else {
                            result = query("https://api.constantcontact.com/v2/emailmarketing/campaigns/" + campaign.getId() + "/tracking/unsubscribes?api_key=" + ConstantContactCompositeSource.KEY + "&created_since=" + lastRefreshString, ccSource, client);
                        }
                        Map meta = (Map) result.get("meta");
                        String nextLink = null;
                        if (meta != null) {
                            Map pagination = (Map) meta.get("pagination");
                            if (pagination != null) {
                                Object nextLinkObject = pagination.get("next_link");
                                if (nextLinkObject != null) {
                                    nextLink = "https://api.constantcontact.com" + nextLinkObject.toString() + "&api_key=" + ConstantContactCompositeSource.KEY;
                                }
                            }
                        }

                        boolean hasMoreData;
                        do {
                            List results = (List) result.get("results");
                            hasMoreData = false;
                            for (Object obj : results) {
                                Map node = (Map) obj;
                                String contactID = node.get("contact_id").toString();
                                String clickDateString = node.get("unsubscribe_date").toString();
                                IRow row = dataSet.createRow();

                                row.addValue(CONTACT_ID, contactID);
                                row.addValue(CAMPAIGN_ID, campaign.getId());
                                row.addValue(OPT_OUT_COUNT, 1);
                                Date clickDate = DATE_FORMAT.parse(clickDateString);
                                row.addValue(EVENT_DATE, new DateValue(clickDate));
                            }
                            if (nextLink != null) {
                                result = query(nextLink, ccSource, client);
                                meta = (Map) result.get("meta");
                                nextLink = null;
                                if (meta != null) {
                                    Map pagination = (Map) meta.get("pagination");
                                    if (pagination != null) {
                                        Object nextLinkObject = pagination.get("next_link");
                                        if (nextLinkObject != null) {
                                            nextLink = "https://api.constantcontact.com" + nextLinkObject.toString() + "&api_key=" + ConstantContactCompositeSource.KEY;
                                        }
                                    }
                                }
                                hasMoreData = true;
                            }
                        } while (hasMoreData);
                    }
                    {
                        Map result;
                        if (lastRefreshString == null) {
                            result = query("https://api.constantcontact.com/v2/emailmarketing/campaigns/" + campaign.getId() + "/tracking/forwards?api_key=" + ConstantContactCompositeSource.KEY, ccSource, client);
                        } else {
                            result = query("https://api.constantcontact.com/v2/emailmarketing/campaigns/" + campaign.getId() + "/tracking/forwards?api_key=" + ConstantContactCompositeSource.KEY + "&created_since=" + lastRefreshString, ccSource, client);
                        }
                        Map meta = (Map) result.get("meta");
                        String nextLink = null;
                        if (meta != null) {
                            Map pagination = (Map) meta.get("pagination");
                            if (pagination != null) {
                                Object nextLinkObject = pagination.get("next_link");
                                if (nextLinkObject != null) {
                                    nextLink = "https://api.constantcontact.com" + nextLinkObject.toString() + "&api_key=" + ConstantContactCompositeSource.KEY;
                                }
                            }
                        }

                        boolean hasMoreData;
                        do {
                            List results = (List) result.get("results");
                            hasMoreData = false;
                            for (Object obj : results) {
                                Map node = (Map) obj;
                                String contactID = node.get("contact_id").toString();
                                String clickDateString = node.get("forward_date").toString();
                                IRow row = dataSet.createRow();

                                row.addValue(CONTACT_ID, contactID);
                                row.addValue(CAMPAIGN_ID, campaign.getId());
                                row.addValue(FORWARD_COUNT, 1);
                                Date clickDate = DATE_FORMAT.parse(clickDateString);
                                row.addValue(EVENT_DATE, new DateValue(clickDate));
                            }
                            if (nextLink != null) {
                                result = query(nextLink, ccSource, client);
                                meta = (Map) result.get("meta");
                                nextLink = null;
                                if (meta != null) {
                                    Map pagination = (Map) meta.get("pagination");
                                    if (pagination != null) {
                                        Object nextLinkObject = pagination.get("next_link");
                                        if (nextLinkObject != null) {
                                            nextLink = "https://api.constantcontact.com" + nextLinkObject.toString() + "&api_key=" + ConstantContactCompositeSource.KEY;
                                        }
                                    }
                                }
                                hasMoreData = true;
                            }
                        } while (hasMoreData);
                    }
                    dataStorage.insertData(dataSet);
                } catch (Exception e) {
                    //LogClass.error(e);
                    LogClass.info(e.getMessage());
                }
            }
            return null;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
