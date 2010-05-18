package com.easyinsight.datafeeds;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.DerivedKey;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.userupload.UploadPolicy;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: Oct 30, 2009
 * Time: 4:00:43 PM
 */
public abstract class DataSourceMigration {

    private FeedDefinition dataSource;

    public DataSourceMigration(FeedDefinition dataSource) {
        this.dataSource = dataSource;
    }

    protected void migrateAnalysisItem(String key, AnalysisItem toAnalysisItem) {
        AnalysisItem matchedItem = null;
        for (AnalysisItem analysisItem : dataSource.getFields()) {
            if (analysisItem.getKey().toKeyString().equals(key)) {
                matchedItem = analysisItem;
            }
        }
        if (matchedItem != null) {
            toAnalysisItem.setAnalysisItemID(matchedItem.getAnalysisItemID());
            dataSource.getFields().remove(matchedItem);
            dataSource.getFields().add(toAnalysisItem);
        }
    }

    protected void addConnection(ChildConnection childConnection, Connection conn) throws SQLException {
        FeedStorage feedStorage = new FeedStorage();
        Map<FeedType, IServerDataSourceDefinition> childMap = new HashMap<FeedType, IServerDataSourceDefinition>();
        CompositeServerDataSource compositeServerDataSource = (CompositeServerDataSource) dataSource;
        for (CompositeFeedNode compositeFeedNode : compositeServerDataSource.getCompositeFeedNodes()) {
            FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(compositeFeedNode.getDataFeedID(), conn);
            childMap.put(feedDefinition.getFeedType(), (IServerDataSourceDefinition) feedDefinition);
        }
        IServerDataSourceDefinition sourceDef = childMap.get(childConnection.getSourceFeedType());
        IServerDataSourceDefinition targetDef = childMap.get(childConnection.getTargetFeedType());
        Key sourceKey = sourceDef.getField(childConnection.getSourceKey());
        Key targetKey = targetDef.getField(childConnection.getTargetKey());
        CompositeFeedConnection connection = new CompositeFeedConnection(sourceDef.getDataFeedID(), targetDef.getDataFeedID(),
                sourceKey, targetKey);
        compositeServerDataSource.getConnections().add(connection);
    }

    protected void addChildDataSource(FeedDefinition childDataSource, EIConnection conn) throws SQLException {
        CompositeServerDataSource compositeServerDataSource = (CompositeServerDataSource) dataSource;
        UploadPolicy uploadPolicy = dataSource.getUploadPolicy();
        compositeServerDataSource.newDefinition((IServerDataSourceDefinition) childDataSource, conn, null, "", uploadPolicy);        
        CompositeFeedNode compositeFeedNode = new CompositeFeedNode(childDataSource.getDataFeedID(), 50, 50);
        compositeServerDataSource.getCompositeFeedNodes().add(compositeFeedNode);
        compositeServerDataSource.populateFields(conn);
    }

    protected AnalysisItem findAnalysisItem(String displayName) {
        AnalysisItem matchedItem = null;
        for (AnalysisItem analysisItem : dataSource.getFields()) {
            if (analysisItem.toDisplay().equals(displayName)) {
                matchedItem = analysisItem;
            }
        }
        return matchedItem;
    }

    protected void migrateAnalysisItemByDisplay(String displayName, AnalysisItem toAnalysisItem) {
        AnalysisItem matchedItem = null;
        for (AnalysisItem analysisItem : dataSource.getFields()) {
            if (analysisItem.toDisplay().equals(displayName)) {
                matchedItem = analysisItem;
            }
        }
        if (matchedItem != null) {
            toAnalysisItem.setAnalysisItemID(matchedItem.getAnalysisItemID());
            dataSource.getFields().remove(matchedItem);
            dataSource.getFields().add(toAnalysisItem);
        }
    }

    protected void addAnalysisItem(AnalysisItem analysisItem) {
        dataSource.getFields().add(analysisItem);
    }

    public abstract void migrate(Map<String, Key> keys, EIConnection conn) throws SQLException;

    public abstract int fromVersion();

    public abstract int toVersion();
}
