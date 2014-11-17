package com.easyinsight.datafeeds.infusionsoft;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 4/24/13
 * Time: 11:30 AM
 */
public class InfusionsoftProductToCategorySource extends InfusionsoftTableSource {

    public static final String PRODUCT_CATEGORY_ID = "ProductCategoryId";
    public static final String PRODUCT_ID = "ProductId";

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(PRODUCT_CATEGORY_ID, new AnalysisDimension("Product to Category Category ID"));
        fieldBuilder.addField(PRODUCT_ID, new AnalysisDimension("Product to Category Product ID"));
    }


    public InfusionsoftProductToCategorySource() {
        setFeedName("Product to Category");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_PRODUCT_TO_CATEGORY;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            return query("ProductCategoryAssign", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
