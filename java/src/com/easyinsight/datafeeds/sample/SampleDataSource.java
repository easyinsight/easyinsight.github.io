package com.easyinsight.datafeeds.sample;

import com.easyinsight.analysis.ReportFault;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
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
        return types;
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return new ArrayList<ChildConnection>();
    }

    @Override
    protected Collection<ChildConnection> getLiveChildConnections() {
        return Arrays.asList(new ChildConnection(FeedType.SAMPLE_CUSTOMER, FeedType.SAMPLE_SALES, SampleCustomerDataSource.CUSTOMER, SampleSalesDataSource.CUSTOMER),
                new ChildConnection(FeedType.SAMPLE_PRODUCT, FeedType.SAMPLE_SALES, SampleProductDataSource.PRODUCT_NAME, SampleSalesDataSource.PRODUCT));
    }
}
