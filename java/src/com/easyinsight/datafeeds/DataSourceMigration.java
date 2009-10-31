package com.easyinsight.datafeeds;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.Key;

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

    protected void addAnalysisItem(AnalysisItem analysisItem) {
        dataSource.getFields().add(analysisItem);
    }

    public void migrate(Map<String, Key> keys) {
        
    }

    public int fromVersion() {
        return 1;
    }

    public int toVersion() {
        return 2;
    }
}
