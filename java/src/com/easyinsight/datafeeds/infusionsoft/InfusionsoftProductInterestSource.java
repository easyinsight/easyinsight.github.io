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
public class InfusionsoftProductInterestSource extends InfusionsoftTableSource {

    public static final String PRODUCT_INTEREST_ID = "Id";
    public static final String OBJECT_ID = "ObjectId";
    public static final String OBJECT_TYPE = "ObjType";
    public static final String PRODUCT_ID = "ProductId";
    public static final String QTY = "Qty";
    public static final String DISCOUNT_PERCENT = "DiscountPercent";


    public InfusionsoftProductInterestSource() {
        setFeedName("Product Interest");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_PRODUCT_INTEREST;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(PRODUCT_INTEREST_ID, PRODUCT_ID, OBJECT_ID, OBJECT_TYPE, QTY, DISCOUNT_PERCENT);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(PRODUCT_ID), "Product Interest Product ID"));
        analysisitems.add(new AnalysisDimension(keys.get(PRODUCT_INTEREST_ID), "Product Interest ID"));
        analysisitems.add(new AnalysisDimension(keys.get(OBJECT_ID), "Object ID"));
        analysisitems.add(new AnalysisDimension(keys.get(OBJECT_TYPE), "Object Type"));
        analysisitems.add(new AnalysisMeasure(keys.get(QTY), "Quantity", AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY));
        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            return query("ProductInterest", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
