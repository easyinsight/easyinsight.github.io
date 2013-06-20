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
public class InfusionsoftProductSource extends InfusionsoftTableSource {

    public static final String PRODUCT_ID = "Id";
    public static final String PRODUCT_NAME = "ProductName";
    public static final String PRODUCT_PRICE = "ProductPrice";
    public static final String PRODUCT_SKU = "Sku";


    public InfusionsoftProductSource() {
        setFeedName("Products");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_PRODUCTS;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(PRODUCT_ID, PRODUCT_NAME, PRODUCT_PRICE, PRODUCT_SKU);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(PRODUCT_ID), "Product ID"));
        analysisitems.add(new AnalysisDimension(keys.get(PRODUCT_NAME), "Product Name"));
        analysisitems.add(new AnalysisDimension(keys.get(PRODUCT_SKU), "Product SKU"));
        analysisitems.add(new AnalysisMeasure(keys.get(PRODUCT_PRICE), "Product Price", AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY));
        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            return query("Product", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
