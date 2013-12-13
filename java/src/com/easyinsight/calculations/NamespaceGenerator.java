package com.easyinsight.calculations;

import com.easyinsight.analysis.AddonReport;
import com.easyinsight.analysis.UniqueKey;
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


    public Map<String, UniqueKey> generate(long dataSourceID, @Nullable List<AddonReport> addonReports) {

        try {
            Map<String, UniqueKey> map;
            Feed feed = FeedRegistry.instance().getFeed(dataSourceID);
            if (feed.getDataSource() instanceof CompositeFeedDefinition) {
                Blah blah = new Blah();
                blah.visit((CompositeFeedDefinition) feed.getDataSource());
                map = blah.map;
            } else {
                map = new HashMap<String, UniqueKey>();
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

    private class Blah extends CompositeFeedNodeVisitor {

        private Map<String, UniqueKey> map = new HashMap<String, UniqueKey>();

        @Override
        protected void accept(CompositeFeedNode compositeFeedNode) throws SQLException {
            map.put(compositeFeedNode.getDataSourceName(), new UniqueKey(compositeFeedNode.getDataFeedID(), UniqueKey.DERIVED));
        }
    }
}
