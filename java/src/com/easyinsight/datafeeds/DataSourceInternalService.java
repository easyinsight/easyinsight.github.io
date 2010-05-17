package com.easyinsight.datafeeds;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.DataSourceInfo;
import com.easyinsight.database.EIConnection;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.DataStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * User: jamesboe
 * Date: Apr 2, 2010
 * Time: 12:12:44 PM
 */
public class DataSourceInternalService {

    private FeedStorage feedStorage = new FeedStorage();

    public void updateComposites(FeedDefinition feedDefinition, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT COMPOSITE_FEED.data_feed_id FROM COMPOSITE_NODE, COMPOSITE_FEED WHERE COMPOSITE_NODE.DATA_FEED_ID = ? AND " +
                "COMPOSITE_NODE.composite_feed_id = COMPOSITE_FEED.composite_feed_id");
        stmt.setLong(1, feedDefinition.getDataFeedID());
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            long parentDataSourceID = rs.getLong(1);
            CompositeFeedDefinition compositeFeedDefinition = (CompositeFeedDefinition) feedStorage.getFeedDefinitionData(parentDataSourceID, conn);
            compositeFeedDefinition.populateFields(conn);
            feedStorage.updateDataFeedConfiguration(compositeFeedDefinition, conn);
        }
    }

    public void updateFeedDefinition(FeedDefinition feedDefinition, EIConnection conn) throws Exception {
        updateFeedDefinition(feedDefinition, conn, false);
    }

    public void updateFeedDefinition(FeedDefinition feedDefinition, EIConnection conn, boolean systemUpdate) throws Exception {
        DataStorage metadata = null;
        try {
            feedDefinition.beforeSave(conn);
            FeedDefinition existingFeed = feedStorage.getFeedDefinitionData(feedDefinition.getDataFeedID(), conn, false);
            if (feedDefinition.getDataSourceType() == DataSourceInfo.STORED_PUSH || feedDefinition.getDataSourceType() == DataSourceInfo.STORED_PULL) {
                List<AnalysisItem> existingFields = existingFeed.getFields();
                if (systemUpdate) {
                    metadata = DataStorage.writeConnection(feedDefinition, conn, 0, systemUpdate);
                } else {
                    metadata = DataStorage.writeConnection(feedDefinition, conn, SecurityUtil.getAccountID(), systemUpdate);
                }
                feedStorage.updateDataFeedConfiguration(feedDefinition, conn);
                int version = metadata.migrate(existingFields, feedDefinition.getFields());
                feedStorage.updateVersion(feedDefinition, version, conn);
                metadata.commit();
            } else {
                feedStorage.updateDataFeedConfiguration(feedDefinition, conn);
            }

            updateComposites(feedDefinition, conn);

        } catch (Exception e) {
            if (metadata != null) {
                metadata.rollback();
            }
            throw e;
        } finally {
            if (metadata != null) {
                metadata.closeConnection();
            }
        }
    }
}
