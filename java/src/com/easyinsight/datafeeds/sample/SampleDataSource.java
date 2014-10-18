package com.easyinsight.datafeeds.sample;

import com.easyinsight.analysis.DataSourceInfo;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportFault;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.HTMLConnectionFactory;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.users.Account;

import java.util.*;

/**
 * User: jamesboe
 * Date: 6/8/11
 * Time: 7:11 PM
 */
public class SampleDataSource extends CompositeServerDataSource {

    public SampleDataSource() {
        setFeedName("Sample Data");
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.PERSONAL;
    }

    @Override
    public String validateCredentials() {
        return null;
    }

    private List<OpportunityData> opportunities;

    String[] reps = { "Jim", "Amy", "Mark", "Lisa"};

    @Override
    public void configureFactory(HTMLConnectionFactory factory) {
    }

    public List<OpportunityData> getOrCreateOpportunities() {
        if (opportunities == null) {
            opportunities = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                String id = String.valueOf(i);
                Calendar cal = Calendar.getInstance();
                //cal.add(Calendar.YEAR, -1);
                cal.add(Calendar.DAY_OF_YEAR, - (int) (Math.random() * (365 * 3)));
                Date dateCreated = cal.getTime();
                int dealChance = (int) (Math.random() * 6);
                double dealSize;
                if (dealChance == 0 || dealChance == 1 || dealChance == 2) {
                    dealSize = Math.random() * 1000 + 2000;
                } else if (dealChance == 3 || dealChance == 4) {
                    dealSize = Math.random() * 3000 + 2000;
                } else {
                    dealSize = Math.random() * 6000 + 2000;
                }
                OpportunityData opportunityData = new OpportunityData(id, dateCreated, reps[((int) (Math.random() * reps.length))], dealSize, dealChance);
                opportunityData.generateHistory();
                opportunities.add(opportunityData);
                /*IRow row = dataSet.createRow();
                row.addValue(keys.get(OPPORTUNITY_ID), String.valueOf(i));
                row.addValue(keys.get(CUSTOMER), customerNames[i % customerNames.length]);
                row.addValue(keys.get(PRODUCT), productNames[(int) (Math.random() * 5)]);
                row.addValue(keys.get(QUANTITY), (int) (Math.random() * 3));

                row.addValue(keys.get(CREATION_DATE), cal.getTime());*/
            }
        }
        return opportunities;
    }

    @Override
    public ReportFault validateDataConnectivity() {
        return null;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.SAMPLE_COMPOSITE;
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> types = new HashSet<FeedType>();
        types.add(FeedType.SAMPLE_CUSTOMER);
        types.add(FeedType.SAMPLE_PRODUCT);
        types.add(FeedType.SAMPLE_SALES);
        types.add(FeedType.SALES_HISTORY);
        types.add(FeedType.SAMPLE_TASKS);
        types.add(FeedType.SAMPLE_LEADS);
        return types;
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return new ArrayList<ChildConnection>();
    }

    @Override
    protected Collection<ChildConnection> getLiveChildConnections() {
        return Arrays.asList(new ChildConnection(FeedType.SAMPLE_CUSTOMER, FeedType.SAMPLE_SALES, SampleCustomerDataSource.CUSTOMER, SampleSalesDataSource.CUSTOMER),
                new ChildConnection(FeedType.SAMPLE_PRODUCT, FeedType.SAMPLE_SALES, SampleProductDataSource.PRODUCT_NAME, SampleSalesDataSource.PRODUCT),
                new ChildConnection(FeedType.SAMPLE_SALES, FeedType.SALES_HISTORY, SampleSalesDataSource.OPPORTUNITY_ID, SampleSalesHistorySource.OPPORTUNITY_ID));
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.COMPOSITE_PULL;
    }
}
