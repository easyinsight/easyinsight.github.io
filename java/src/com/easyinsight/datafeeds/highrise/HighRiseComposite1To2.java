package com.easyinsight.datafeeds.highrise;

import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceMigration;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.composite.ChildConnection;

import java.sql.SQLException;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Mar 23, 2010
 * Time: 2:10:43 PM
 */
public class HighRiseComposite1To2 extends DataSourceMigration {
    public HighRiseComposite1To2(FeedDefinition dataSource) {
        super(dataSource);
    }

    @Override
    public void migrate(Map<String, Key> keys, EIConnection conn) throws SQLException {
        HighRiseContactSource contactSource = new HighRiseContactSource();
        addChildDataSource(contactSource, conn);
        addConnection(new ChildConnection(FeedType.HIGHRISE_COMPANY, FeedType.HIGHRISE_CONTACTS, HighRiseCompanySource.COMPANY_ID,
                HighRiseContactSource.COMPANY_ID), conn);
    }

    @Override
    public int fromVersion() {
        return 1;
    }

    @Override
    public int toVersion() {
        return 2;
    }
}
