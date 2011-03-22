package com.easyinsight.datafeeds.harvest;

import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.kpi.KPI;
import com.easyinsight.users.Account;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: 3/21/11
 * Time: 7:36 PM
 */
public class HarvestCompositeSource extends CompositeServerDataSource {

    public HarvestCompositeSource() {
        setFeedName("Harvest");
    }

    public String validateCredentials() {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HARVEST_COMPOSITE;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> types = new HashSet<FeedType>();
        types.add(FeedType.HARVEST_CLIENT);
        types.add(FeedType.HARVEST_PROJECT);
        types.add(FeedType.HARVEST_TIME);
        return types;
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        // TODO: implement
        return new ArrayList<ChildConnection>();
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        // TODO: implement, keep call to super()
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        // TODO: implement, keep call to super()
    }

    @Override
    public List<KPI> createKPIs() {
        // TODO: implement
        throw new UnsupportedOperationException();
    }
}
