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
    public static final String CUSTOMER_NAME = "CustomerName";
    public static final String INDUSTRY = "Industry";
    public static final String POSTAL_CODE = "Postal Code";
    public static final String STATE = "State";
    public static final String CUSTOMER_COUNT = "Customer Count";

    private static final String[] customerNames = { "Bross Design", "Crestone Engineering", "Uncompahgre Systems",
            "Sneffels Technology", "Sunlight Architecture", "Pyramid Research", "Little Bear Consulting", "Torreys Inc" };

    private static final String[] industries = { "Design", "Engineering", "Consulting" };

    private static final String[] postalCodes = { "80424", "81252", "81235", "81432", "81122", "81624", "81101", "80435"};

    private static final String[] states = { "AL", "AR", "AZ", "CA", "CO", "CT", "DE", "FL", "GA", "ID", "IL", "IN",
        "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC",
        "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VA", "WA", "WV", "WI", "WY"};

    private static final double[] stateOdds = { 1, 1.2, 1.3, 4.7, 1.5, 1.5, 1.5, 2.0, 1.9, 0.6, 1.7, 1.1,
            1.0, 0.8, 1.2, 1.3, 1.4, 1.2, 1.5, 1.4, 1.4, 1.1, 1.2, 0.7, 0.6, 0.8, 0.7, 1.3, 1.1, 3.7, 2.0,
            1.1, 1.9, 1.3, 1.4, 2.1, 0.7, 1.3, 1.1, 1.2, 3.5, 1.5, 1.9, 1.7, 1.6, 1.4, 0.5};

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
        fieldBuilder.addField(CUSTOMER_NAME, new AnalysisDimension());
        fieldBuilder.addField(INDUSTRY, new AnalysisDimension());
        fieldBuilder.addField(POSTAL_CODE, new AnalysisDimension());
        fieldBuilder.addField(STATE, new AnalysisDimension());
        fieldBuilder.addField(CUSTOMER_COUNT, new AnalysisMeasure());
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        List<String> s = new ArrayList<>();
        for (int i = 0; i < states.length; i++) {
            String state = states[i];
            double odds = stateOdds[i];
            int num = (int)(odds * 20);
            for (int j = 0; j < num; j++) {
                s.add(state);
            }
        }
        try {
            for (int i = 0; i < 5000; i++) {
                IRow row = dataSet.createRow();
                row.addValue(keys.get(CUSTOMER), String.valueOf(i));
                row.addValue(keys.get(CUSTOMER_NAME), customerNames[i % customerNames.length]);
                row.addValue(keys.get(INDUSTRY), industries[i % industries.length]);
                row.addValue(keys.get(POSTAL_CODE), postalCodes[i % postalCodes.length]);
                row.addValue(keys.get(STATE), s.get((int) (Math.random() * s.size())));
                row.addValue(keys.get(CUSTOMER_COUNT), 1);
            }
            IDataStorage.insertData(dataSet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
