package com.easyinsight.datafeeds.infusionsoft;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 4/24/13
 * Time: 11:30 AM
 */
public class InfusionsoftSubscriptionSource extends InfusionsoftTableSource {

    public static final String SUBSCRIPTION_ID = "Id";
    public static final String PRODUCT_ID = "ProductId";
    public static final String CYCLE = "Cycle";
    public static final String FREQUENCY = "Frequency";
    public static final String PRE_AUTHORIZE_AMOUNT = "PreAuthorizeAmount";
    public static final String PRO_RATE = "Prorate";
    public static final String ACTIVE = "Active";
    public static final String PLAN_PRICE = "PlanPrice";

    public InfusionsoftSubscriptionSource() {
        setFeedName("Subscription Plans");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_SUBSCRIPTIONS;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(SUBSCRIPTION_ID, PRODUCT_ID, CYCLE, FREQUENCY, PRE_AUTHORIZE_AMOUNT, PRO_RATE, ACTIVE, PLAN_PRICE);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(SUBSCRIPTION_ID), "Subscription ID"));
        analysisitems.add(new AnalysisDimension(keys.get(PRODUCT_ID), "Subscripton Product ID"));
        analysisitems.add(new AnalysisDimension(keys.get(CYCLE), "Subscription Cycle"));
        analysisitems.add(new AnalysisDimension(keys.get(ACTIVE), "Subscription Active"));
        analysisitems.add(new AnalysisDimension(keys.get(PRO_RATE), "Subscription Prorated"));
        analysisitems.add(new AnalysisMeasure(keys.get(PLAN_PRICE), "Subscription Price", AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY));
        analysisitems.add(new AnalysisMeasure(keys.get(FREQUENCY), "Subscription Frequency", AggregationTypes.SUM));
        analysisitems.add(new AnalysisMeasure(keys.get(PRE_AUTHORIZE_AMOUNT), "Subscription Preauthorize Amount", AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY));
        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            return query("SubscriptionPlan", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
