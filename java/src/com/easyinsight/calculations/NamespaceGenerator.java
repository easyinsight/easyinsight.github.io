package com.easyinsight.calculations;

import com.easyinsight.analysis.AddonReport;
import com.easyinsight.analysis.UniqueKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.logging.LogClass;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 10/17/13
 * Time: 9:55 AM
 */
public class NamespaceGenerator {


    public Map<String, UniqueKey> generate(long dataSourceID, @Nullable List<AddonReport> addonReports, @Nullable EIConnection conn) {

        try {
            Map<String, UniqueKey> map;
            if (dataSourceID == 0) {
                map = new HashMap<String, UniqueKey>();
            } else {
                FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(dataSourceID, conn);
                if (dataSource instanceof CompositeFeedDefinition) {
                    Blah blah = new Blah();
                    blah.traverse((CompositeFeedDefinition) dataSource, conn);
                    map = blah.map;
                } else {
                    map = new HashMap<String, UniqueKey>();
                }
            }
            if (addonReports != null) {
                for (AddonReport addonReport : addonReports) {
                    map.put(addonReport.getReportName(), new UniqueKey(addonReport.getReportID(), UniqueKey.REPORT));
                }
            }
            return map;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private class Blah  {

        private Map<String, UniqueKey> map = new HashMap<String, UniqueKey>();


        protected void traverse(CompositeFeedDefinition compositeFeedDefinition, EIConnection conn) throws SQLException {
            for (CompositeFeedNode compositeFeedNode : compositeFeedDefinition.getCompositeFeedNodes()) {
                map.put(compositeFeedNode.getDataSourceName(), new UniqueKey(compositeFeedNode.getDataFeedID(), UniqueKey.DERIVED));
                FeedDefinition dataSource;
                if (conn == null) {
                    dataSource = new FeedStorage().getFeedDefinitionData(compositeFeedNode.getDataFeedID());
                } else {
                    dataSource = new FeedStorage().getFeedDefinitionData(compositeFeedNode.getDataFeedID(), conn);
                }
                if (dataSource instanceof CompositeFeedDefinition) {
                    traverse((CompositeFeedDefinition) dataSource, conn);
                }
            }
        }
    }
}
