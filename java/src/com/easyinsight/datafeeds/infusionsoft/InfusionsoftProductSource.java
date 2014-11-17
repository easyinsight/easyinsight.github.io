package com.easyinsight.datafeeds.infusionsoft;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
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
    public static final String CITY_TAXABLE = "CityTaxable";
    public static final String COUNTRY_TAXABLE = "CountryTaxable";
    public static final String DESCRIPTION = "Description";
    public static final String INVENTORY_LIMIT = "InventoryLimit";
    public static final String IS_PACKAGE = "IsPackage";
    public static final String NEEDS_DIGITAL_DELIVERY = "NeedsDigitalDelivery";
    public static final String SHIPPABLE = "Shippable";
    public static final String SHORT_DESCRIPTION = "ShortDescription";
    public static final String STATE_TAXABLE = "StateTaxable";
    public static final String TAXABLE = "Taxable";
    public static final String WEIGHT = "Weight";
    public static final String STATUS = "Status";

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(PRODUCT_ID, new AnalysisDimension("Product ID"));
        fieldBuilder.addField(PRODUCT_NAME, new AnalysisDimension("Product Name"));
        fieldBuilder.addField(PRODUCT_SKU, new AnalysisDimension("Product SKU"));
        fieldBuilder.addField(PRODUCT_PRICE, new AnalysisMeasure("Product Price", AggregationTypes.SUM, FormattingConfiguration.CURRENCY));
        fieldBuilder.addField(CITY_TAXABLE, new AnalysisDimension("City Taxable"));
        fieldBuilder.addField(COUNTRY_TAXABLE, new AnalysisDimension("Country Taxable"));
        fieldBuilder.addField(STATE_TAXABLE, new AnalysisDimension("State Taxable"));
        fieldBuilder.addField(TAXABLE, new AnalysisDimension("Taxable"));
        fieldBuilder.addField(DESCRIPTION, new AnalysisDimension("Product Description"));
        fieldBuilder.addField(SHORT_DESCRIPTION, new AnalysisDimension("Product Short Description"));
        fieldBuilder.addField(NEEDS_DIGITAL_DELIVERY, new AnalysisDimension("Product Needs Digital Delivery"));
        fieldBuilder.addField(SHIPPABLE, new AnalysisDimension("Product Shippable"));
        fieldBuilder.addField(IS_PACKAGE, new AnalysisDimension("Product is Package"));
        fieldBuilder.addField(WEIGHT, new AnalysisDimension("Product Weight"));
        fieldBuilder.addField(INVENTORY_LIMIT, new AnalysisMeasure("Inventory Limit"));
        fieldBuilder.addField(STATUS, new AnalysisDimension("Product Status"));
    }


    public InfusionsoftProductSource() {
        setFeedName("Products");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_PRODUCTS;
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
