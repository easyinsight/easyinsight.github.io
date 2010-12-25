package com.easyinsight.datafeeds.freshbooks;

import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceMigration;
import com.easyinsight.datafeeds.FeedDefinition;

import java.util.Map;

/**
 * User: jamesboe
 * Date: 12/22/10
 * Time: 3:54 PM
 */
public class FreshbooksComposite1To2 extends DataSourceMigration {

    public FreshbooksComposite1To2(FeedDefinition dataSource) {
        super(dataSource);
    }

    @Override
    public void migrate(Map<String, Key> keys, EIConnection conn) throws Exception {
        FreshbooksInvoiceLineSource commentsSource = new FreshbooksInvoiceLineSource();
        addChildDataSource(commentsSource, conn);
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
