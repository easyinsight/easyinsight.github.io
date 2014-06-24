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
 * Time: 10:03 PM
 */
public class SampleSalesDataSource extends ServerDataSourceDefinition {
    public static final String CUSTOMER = "Sales Customer";
    public static final String PRODUCT = "Sales Product";
    public static final String SALES_REP = "Sales Rep";
    public static final String DEAL_SIZE = "Deal Size";
    public static final String DATE_WON = "Date Won";
    public static final String QUANTITY = "Quantity";
    public static final String CREATION_DATE = "Date Created";
    public static final String STAGE = "Current Stage";
    public static final String OPPORTUNITY_ID = "Opportunity ID";
    public static final String OPPORTUNITY_COUNT = "Opportunity Count";

    public SampleSalesDataSource() {
        setFeedName("Sales");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.SAMPLE_SALES;
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(CUSTOMER, new AnalysisDimension());
        fieldBuilder.addField(PRODUCT, new AnalysisDimension());
        fieldBuilder.addField(STAGE, new AnalysisDimension());
        fieldBuilder.addField(SALES_REP, new AnalysisDimension());
        fieldBuilder.addField(DEAL_SIZE, new AnalysisMeasure());
        fieldBuilder.addField(QUANTITY, new AnalysisMeasure());
        fieldBuilder.addField(OPPORTUNITY_COUNT, new AnalysisMeasure());
        fieldBuilder.addField(CREATION_DATE, new AnalysisDateDimension());
        fieldBuilder.addField(DATE_WON, new AnalysisDateDimension());
        fieldBuilder.addField(OPPORTUNITY_ID, new AnalysisDimension());
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        try {
            SampleDataSource sampleDataSource = (SampleDataSource) parentDefinition;
            List<OpportunityData> opportunities = sampleDataSource.getOrCreateOpportunities();
            for (OpportunityData opportunityData : opportunities) {
                IRow row = dataSet.createRow();
                row.addValue(keys.get(OPPORTUNITY_ID), opportunityData.getId());
                row.addValue(keys.get(STAGE), opportunityData.getStage());
                row.addValue(keys.get(CREATION_DATE), opportunityData.getDateCreated());
                row.addValue(keys.get(DATE_WON), opportunityData.getDateWon());
                row.addValue(keys.get(SALES_REP), opportunityData.getSalesRep());
                row.addValue(keys.get(DEAL_SIZE), opportunityData.getDealSize());
                row.addValue(keys.get(PRODUCT), opportunityData.getProduct());
                row.addValue(keys.get(CUSTOMER), opportunityData.getCustomer());
                row.addValue(keys.get(QUANTITY), Math.random() * 10);
                row.addValue(keys.get(OPPORTUNITY_COUNT), 1);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dataSet;
    }
}
