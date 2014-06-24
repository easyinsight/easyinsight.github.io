package com.easyinsight.datafeeds.sample;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 6/8/11
 * Time: 10:02 PM
 */
public class SampleCustomerDataSource extends ServerDataSourceDefinition {
    public static final String CUSTOMER = "Customer";
    public static final String INDUSTRY = "Industry";
    public static final String POSTAL_CODE = "Postal Code";
    public static final String STATE = "State";

    private static final String[] customerNames = { "Bross Design", "Crestone Engineering", "Uncompahgre Systems",
            "Sneffels Technology", "Sunlight Architecture", "Pyramid Research", "Little Bear Consulting", "Torreys Inc" };

    private static final String[] industries = { "Design", "Engineering", "Consulting" };

    private static final String[] postalCodes = { "80424", "81252", "81235", "81432", "81122", "81624", "81101", "80435"};

    public SampleCustomerDataSource() {
        setFeedName("Customer");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.SAMPLE_CUSTOMER;
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(CUSTOMER, new AnalysisDimension());
        fieldBuilder.addField(INDUSTRY, new AnalysisDimension());
        fieldBuilder.addField(POSTAL_CODE, new AnalysisDimension());
        fieldBuilder.addField(STATE, new AnalysisDimension());
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        try {
            for (int i = 0; i < customerNames.length; i++) {
                IRow row = dataSet.createRow();
                row.addValue(keys.get(CUSTOMER), customerNames[i]);
                row.addValue(keys.get(INDUSTRY), industries[i % industries.length]);
                row.addValue(keys.get(POSTAL_CODE), postalCodes[i % postalCodes.length]);
                row.addValue(keys.get(STATE), "CO");
            }
            IDataStorage.insertData(dataSet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
