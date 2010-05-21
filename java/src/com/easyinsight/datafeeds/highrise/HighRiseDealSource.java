package com.easyinsight.datafeeds.highrise;

import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceMigration;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.users.Credentials;
import com.easyinsight.users.Token;
import com.easyinsight.users.TokenStorage;
import nu.xom.*;
import org.jetbrains.annotations.NotNull;
import org.apache.commons.httpclient.HttpClient;

import java.util.*;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;

/**
 * User: jamesboe
 * Date: Sep 2, 2009
 * Time: 11:50:50 PM
 */
public class HighRiseDealSource extends HighRiseBaseSource {

    public static final String DEAL_NAME = "Deal Name";
    public static final String COMPANY_ID = "Company ID";
    public static final String PRICE = "Price";
    public static final String DURATION = "Duration";
    public static final String PRICE_TYPE = "Price Type";
    public static final String DEAL_OWNER = "Deal Owner";
    public static final String CATEGORY = "Category";
    public static final String STATUS = "Status";
    public static final String CREATED_AT = "Created At";
    public static final String STATUS_CHANGED_ON = "Status Changed On";
    public static final String COUNT = "Count";
    public static final String TOTAL_DEAL_VALUE = "Total Deal Value";

    public HighRiseDealSource() {
        setFeedName("Deals");
    }

    @NotNull
    protected List<String> getKeys() {
        return Arrays.asList(DEAL_NAME, COMPANY_ID, PRICE, DURATION, PRICE_TYPE, DEAL_OWNER,
                CATEGORY, STATUS, CREATED_AT, COUNT, TOTAL_DEAL_VALUE, STATUS_CHANGED_ON);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, com.easyinsight.users.Credentials credentials, Connection conn) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(DEAL_NAME), true));
        analysisItems.add(new AnalysisDimension(keys.get(DEAL_OWNER), true));
        analysisItems.add(new AnalysisDimension(keys.get(COMPANY_ID), true));
        AnalysisMeasure priceMeasure = new AnalysisMeasure(PRICE, AggregationTypes.SUM);
        FormattingConfiguration formattingConfiguration = new FormattingConfiguration();
        formattingConfiguration.setFormattingType(FormattingConfiguration.CURRENCY);
        priceMeasure.setFormattingConfiguration(formattingConfiguration);
        analysisItems.add(priceMeasure);
        AnalysisMeasure dealValueMeasure = new AnalysisMeasure(TOTAL_DEAL_VALUE, AggregationTypes.SUM);
        FormattingConfiguration dealFormattingConfiguration = new FormattingConfiguration();
        dealFormattingConfiguration.setFormattingType(FormattingConfiguration.CURRENCY);
        dealValueMeasure.setFormattingConfiguration(dealFormattingConfiguration);
        analysisItems.add(dealValueMeasure);
        analysisItems.add(new AnalysisDimension(keys.get(DURATION), true));
        analysisItems.add(new AnalysisDimension(keys.get(PRICE_TYPE), true));
        analysisItems.add(new AnalysisDimension(keys.get(CATEGORY), true));
        analysisItems.add(new AnalysisDimension(keys.get(STATUS), true));
        analysisItems.add(new AnalysisDateDimension(keys.get(STATUS_CHANGED_ON), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDateDimension(keys.get(CREATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        return analysisItems;
    }



    private String retrieveCategoryInfo(HttpClient client, Builder builder, Map<String, String> categoryCache, String categoryID, String url) throws HighRiseLoginException, ParsingException {
        try {
            String contactName = null;
            if(categoryID != null) {
                contactName = categoryCache.get(categoryID);
                if(contactName == null) {
                    Document contactInfo = runRestRequest("/deal_categories/" + categoryID + ".xml", client, builder, url, false);
                    Nodes dealNodes = contactInfo.query("/deal-category");
                    if (dealNodes.size() > 0) {
                        Node deal = dealNodes.get(0);
                        contactName = queryField(deal, "name/text()");
                    }

                    categoryCache.put(categoryID, contactName);
                }

            }
            return contactName;
        } catch (HighRiseLoginException e) {
            return "";
        }
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HIGHRISE_DEAL;
    }

    public DataSet getDataSet(Credentials credentials, Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn) {
        HighRiseCompositeSource highRiseCompositeSource = (HighRiseCompositeSource) parentDefinition;
        String url = highRiseCompositeSource.getUrl();

        DateFormat deadlineFormat = new SimpleDateFormat(XMLDATEFORMAT);

        DataSet ds = new DataSet();
        Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.HIGHRISE_TOKEN, parentDefinition.getDataFeedID(), false, conn);
        if (token == null) {
            token = new Token();
            token.setTokenValue(credentials.getUserName());
            token.setTokenType(TokenStorage.HIGHRISE_TOKEN);
            token.setUserID(SecurityUtil.getUserID());
            new TokenStorage().saveToken(token, parentDefinition.getDataFeedID(), conn);
        } else if (token != null && credentials != null && credentials.getUserName() != null && !"".equals(credentials.getUserName()) &&
                !credentials.getUserName().equals(token.getTokenValue())) {
            token.setTokenValue(credentials.getUserName());
            new TokenStorage().saveToken(token, parentDefinition.getDataFeedID(), conn);
        }
        HttpClient client = getHttpClient(token.getTokenValue(), "");
        Builder builder = new Builder();
        Map<String, String> peopleCache = new HashMap<String, String>();
        Map<String, String> categoryCache = new HashMap<String, String>();
        try {
            EIPageInfo info = new EIPageInfo();
            info.currentPage = 1;
            //do {
                Document deals = runRestRequest("/deals.xml", client, builder, url, true);
                loadingProgress(0, 1, "Synchronizing with deals...", true);
                Nodes dealNodes = deals.query("/deals/deal");
                for(int i = 0;i < dealNodes.size();i++) {
                    try {
                        IRow row = ds.createRow();
                        Node currDeal = dealNodes.get(i);
                        String dealName = queryField(currDeal, "name/text()");
                        row.addValue(DEAL_NAME, dealName);
                        String price = queryField(currDeal, "price/text()");
                        row.addValue(PRICE, price);
                        String status = queryField(currDeal, "status/text()");
                        row.addValue(STATUS, status);
                        String priceType = queryField(currDeal, "price-type/text()");
                        row.addValue(PRICE_TYPE, priceType);
                        double totalDealValue;
                        if ("fixed".equals(priceType)) {
                            try {
                                totalDealValue = Double.parseDouble(price);
                            } catch (Exception e) {
                                totalDealValue = 0;
                            }
                        } else {
                            String durationString = queryField(currDeal, "duration/text()");
                            int duration;
                            try {
                                duration = Integer.parseInt(durationString);
                            } catch (Exception e) {
                                duration = 0;
                            }
                            try {
                                totalDealValue = Double.parseDouble(price) * duration;
                            } catch (Exception e) {
                                totalDealValue = 0;
                            }
                            row.addValue(DURATION, new NumericValue(duration));

                        }
                        row.addValue(TOTAL_DEAL_VALUE, new NumericValue(totalDealValue));

                        String personID = queryField(currDeal, "person-id/text()");
                        row.addValue(DEAL_OWNER, retrieveContactInfo(client, builder, peopleCache, personID, url));
                        String categoryID = queryField(currDeal, "category-id/text()");
                        row.addValue(CATEGORY, retrieveCategoryInfo(client, builder, categoryCache, categoryID, url));
                        row.addValue(COUNT, new NumericValue(1));

                        String partyID = queryField(currDeal, "party-id/text()");
                        row.addValue(COMPANY_ID, partyID);
                        String createdAt = queryField(currDeal, "created-at/text()");
                        row.addValue(CREATED_AT, new DateValue(deadlineFormat.parse(createdAt)));
                        String statusChangedOn = queryField(currDeal, "status-changed-on/text()");
                        if (statusChangedOn != null) {
                            row.addValue(STATUS_CHANGED_ON, new DateValue(deadlineFormat.parse(statusChangedOn)));
                        }
                    } catch (Exception e) {
                        LogClass.error(e);
                    }
                }
            //} while(info.currentPage++ < info.MaxPages);

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return ds;
    }

    @Override
    public int getVersion() {
        return 3;
    }

    @Override
    public List<DataSourceMigration> getMigrations() {
        return Arrays.asList(new HighRise1To2(this), new HighRiseDeal2To3(this));
    }
}
