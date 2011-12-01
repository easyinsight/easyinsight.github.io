package com.easyinsight.calculations;

import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;
import com.easyinsight.datafeeds.*;
import com.easyinsight.security.SecurityUtil;

import java.util.Date;
import java.util.List;

/**
 * User: jamesboe
 * Date: 11/20/11
 * Time: 2:59 PM
 */
public class InvalidateData extends Function {
    public Value evaluate() {
        calculationMetadata.getConnection().setAutoCommit(false);
        try {
            long dataSourceID = calculationMetadata.getDashboard().getDataSourceID();
            FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(dataSourceID, calculationMetadata.getConnection());
            if (dataSource.getFeedType().getType() == FeedType.COMPOSITE.getType()) {
                CompositeFeedDefinition compositeFeedDefinition = (CompositeFeedDefinition) dataSource;
                List<CompositeFeedNode> nodes = compositeFeedDefinition.getCompositeFeedNodes();
                for (CompositeFeedNode node : nodes) {
                    FeedDefinition child = new FeedStorage().getFeedDefinitionData(node.getDataFeedID(), calculationMetadata.getConnection());
                    blah(child, dataSource);
                }
            } else {
                blah(dataSource, null);
            }
            calculationMetadata.getConnection().commit();
        } catch (Exception e) {
            if (!calculationMetadata.getConnection().getAutoCommit()) {
                calculationMetadata.getConnection().rollback();
            }
            throw new RuntimeException(e);
        } finally {
            calculationMetadata.getConnection().setAutoCommit(true);
        }
        return new EmptyValue();
    }

    private void blah(FeedDefinition dataSource, FeedDefinition parent) throws Exception {
        if (dataSource instanceof IServerDataSourceDefinition) {
            IServerDataSourceDefinition dataSourceDefinition = (IServerDataSourceDefinition) dataSource;
            if (dataSource.getLastRefreshStart() == null || dataSourceDefinition.hasNewData(dataSource.getLastRefreshStart(), parent, calculationMetadata.getConnection())) {
                Date now = new Date();
                boolean changed = dataSourceDefinition.refreshData(SecurityUtil.getAccountID(), new Date(), calculationMetadata.getConnection(), null, null, dataSource.getLastRefreshStart());
                dataSource.setVisible(true);
                dataSource.setLastRefreshStart(now);
                if (changed) {
                    new DataSourceInternalService().updateFeedDefinition(dataSource, calculationMetadata.getConnection(), true, true);
                } else {
                    new FeedStorage().updateDataFeedConfiguration(dataSource, calculationMetadata.getConnection());
                }
            }
        }
    }

    public int getParameterCount() {
        return 0;
    }
}
