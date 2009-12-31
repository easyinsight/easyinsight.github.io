package com.easyinsight.datafeeds;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.DerivedKey;
import com.easyinsight.core.Key;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Oct 30, 2009
 * Time: 4:00:43 PM
 */
public class DataSourceMigration {

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

    public void migrate(Map<String, Key> keys) {
        
    }

    public int fromVersion() {
        return 2;
    }

    public int toVersion() {
        return 3;
    }
}
