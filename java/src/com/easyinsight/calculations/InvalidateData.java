package com.easyinsight.calculations;

import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;
import com.easyinsight.datafeeds.DataSourceInternalService;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.IServerDataSourceDefinition;
import com.easyinsight.security.SecurityUtil;

import java.util.Date;

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
            IServerDataSourceDefinition dataSourceDefinition = (IServerDataSourceDefinition) dataSource;
            Date now = new Date();
            boolean changed = dataSourceDefinition.refreshData(SecurityUtil.getAccountID(), new Date(), calculationMetadata.getConnection(), null, null, dataSource.getLastRefreshStart());
            dataSource.setVisible(true);
            dataSource.setLastRefreshStart(now);
            if (changed) {
                new DataSourceInternalService().updateFeedDefinition(dataSource, calculationMetadata.getConnection(), true, true);
            } else {
                new FeedStorage().updateDataFeedConfiguration(dataSource, calculationMetadata.getConnection());
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

    public int getParameterCount() {
        return 0;
    }
}
