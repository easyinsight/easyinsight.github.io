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
public class InfusionsoftOrderItemSource extends InfusionsoftTableSource {

    public static final String ORDER_ITEM_ID = "Id";
    public static final String ORDER_ID = "OrderId";
    public static final String PRODUCT_ID = "ProductId";
    public static final String SUBSCRIPTION_PLAN_ID = "SubscriptionPlanId";
    public static final String QUANTITY = "Qty";
    public static final String CPU = "CPU";
    public static final String PPU = "PPU";
    public static final String ITEM_TYPE = "ItemType";

    public InfusionsoftOrderItemSource() {
        setFeedName("Order Item");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_ORDER_ITEM;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ORDER_ITEM_ID, ORDER_ID, PRODUCT_ID, SUBSCRIPTION_PLAN_ID, QUANTITY, CPU, PPU, ITEM_TYPE);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(ORDER_ITEM_ID), "Order Item ID"));
        analysisitems.add(new AnalysisDimension(keys.get(ORDER_ID), "Order Item Order ID"));
        analysisitems.add(new AnalysisDimension(keys.get(PRODUCT_ID), "Order Item Product ID"));
        analysisitems.add(new AnalysisDimension(keys.get(SUBSCRIPTION_PLAN_ID), "Order Item Subscription Plan ID"));
        analysisitems.add(new AnalysisDimension(keys.get(ITEM_TYPE), "Order Item Type"));
        analysisitems.add(new AnalysisMeasure(keys.get(QUANTITY), "Order Item Quantity", AggregationTypes.SUM));
        analysisitems.add(new AnalysisMeasure(keys.get(CPU), "CPU", AggregationTypes.SUM));
        analysisitems.add(new AnalysisMeasure(keys.get(PPU), "PPU", AggregationTypes.SUM));
        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            return query("OrderItem", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
