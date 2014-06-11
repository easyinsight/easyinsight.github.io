package com.easyinsight.calculations;

import com.easyinsight.analysis.AddonReport;
import com.easyinsight.analysis.UniqueKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
                if (conn != null) {

                    NamespaceTraversal blah = new NamespaceTraversal();
                    blah.traverse(dataSourceID, conn);
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
            //throw new RuntimeException(e);
            return new HashMap<String, UniqueKey>();
        }
    }

    private class NamespaceTraversal {

        private Map<String, UniqueKey> map = new HashMap<String, UniqueKey>();


        protected void traverse(long dataSourceID, EIConnection conn) throws SQLException {
            PreparedStatement ps = conn.prepareStatement("SELECT COMPOSITE_NODE.DATA_FEED_ID, DATA_FEED.feed_name FROM COMPOSITE_NODE, COMPOSITE_FEED, DATA_FEED " +
                    "WHERE COMPOSITE_FEED.data_feed_id = ? AND " +
                    "COMPOSITE_NODE.composite_feed_id = composite_feed.composite_feed_id AND " +
                    "COMPOSITE_NODE.data_feed_id = DATA_FEED.data_feed_id");
            ps.setLong(1, dataSourceID);
            ResultSet rs = ps.executeQuery();
            Set<Long> ids = new HashSet<Long>();
            while (rs.next()) {
                long id = rs.getLong(1);
                ids.add(id);
                String name = rs.getString(2);
                map.put(name, new UniqueKey(id, UniqueKey.DERIVED));
            }
            ps.close();
            for (Long id : ids) {
                traverse(id, conn);
            }
        }
    }
}
