package com.easyinsight.datafeeds.infusionsoft;

import com.easyinsight.analysis.*;
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
public class InfusionsoftProductCategorySource extends InfusionsoftTableSource {

    public static final String CATEGORY_NAME = "CategoryDisplayName";
    public static final String CATEGORY_ID = "Id";
    public static final String CATEGORY_PARENT_NAME = "Parent Category Name";
    public static final String CATEGORY_PARENT_ID = "ParentId";

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(CATEGORY_NAME, new AnalysisDimension("Product Category Name"));
        fieldBuilder.addField(CATEGORY_ID, new AnalysisDimension("Product Category ID"));
        fieldBuilder.addField(CATEGORY_PARENT_NAME, new AnalysisDimension("Product Category Parent Name"));
        fieldBuilder.addField(CATEGORY_PARENT_ID, new AnalysisDimension("Product Category Parent ID"));
    }


    public InfusionsoftProductCategorySource() {
        setFeedName("Product Category");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_PRODUCT_CATEGORY;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            DataSet dataSet = query("ProductCategory", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition, Arrays.asList(CATEGORY_PARENT_NAME));
            Map<String, String> map = new HashMap<>();
            for (IRow row : dataSet.getRows()) {
                String categoryID = row.getValue(keys.get(CATEGORY_ID)).toString();
                String categoryName = row.getValue(keys.get(CATEGORY_NAME)).toString();
                map.put(categoryID, categoryName);
            }
            for (IRow row : dataSet.getRows()) {
                String parentID = row.getValue(keys.get(CATEGORY_PARENT_ID)).toString();
                String categoryName = map.get(parentID);
                if (categoryName != null) {
                    row.addValue(keys.get(CATEGORY_PARENT_NAME), categoryName);
                }
            }
            return dataSet;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
