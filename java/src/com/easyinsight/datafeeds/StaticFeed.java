package com.easyinsight.datafeeds;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.TableDefinitionMetadata;
import com.easyinsight.IRow;
import com.easyinsight.AnalysisItem;
import com.easyinsight.AnalysisItemTypes;
import com.easyinsight.logging.LogClass;
import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;

import java.util.*;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: Apr 26, 2008
 * Time: 8:07:57 PM
 */
public class StaticFeed extends Feed {


    //private Map<Key, ColumnSegment> cache = new WeakHashMap<Key, ColumnSegment>();

    private Map<Set<Key>, DataSet> dataSetCache = new WeakHashMap<Set<Key>, DataSet>();

    public FeedType getDataFeedType() {
        return FeedType.STATIC;
    }

    public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem) {
        // this would change to return the data from our contained analysis definition
        AnalysisItemResultMetadata metadata = analysisItem.createResultMetadata();
        Key key;
        if (analysisItem.hasType(AnalysisItemTypes.HIERARCHY)) {
            AnalysisHierarchyItem analysisHierarchyItem = (AnalysisHierarchyItem) analysisItem;
            key = analysisHierarchyItem.getHierarchyLevel().getAnalysisItem().getKey();
        } else {
            key = analysisItem.getKey();
        }
        Set<Key> keySet = new HashSet<Key>();
        keySet.add(key);
        DataSet dataSet = dataSetCache.get(keySet);
        if (dataSet == null) {
            TableDefinitionMetadata source = TableDefinitionMetadata.writeConnection(getFields(), getFeedID());
            try {
                dataSet = source.retrieveData(new ArrayList<Key>(keySet), null);
            } catch (SQLException e) {
                LogClass.error(e);
                throw new RuntimeException(e);
            } finally {
                source.closeConnection();
            }
        }
        for (IRow row : dataSet.getRows()) {
            Value value = row.getValue(key);
            metadata.addValue(analysisItem, value);
        }
        return metadata;
    }

    protected DataSet getUncachedDataSet(List<Key> columns, Integer maxRows, boolean adminMode, InsightRequestMetadata insightRequestMetadata) {

        if (columns.size() == 0) {
            throw new RuntimeException("Attempt made to retrieve a data set with no columns where ID = " + getFeedID());
        }

        if (!adminMode) {
            Set<Key> columnSet = new HashSet<Key>(columns);
            if (getAnalysisDefinition().getFilterDefinitions() != null) {
                for (FilterDefinition filterDefinition : getAnalysisDefinition().getFilterDefinitions()) {
                    columnSet.add(filterDefinition.getField().getKey());
                }
            }
            columns = new ArrayList<Key>(columnSet);
        }

        Set<Key> keySet = new HashSet<Key>();
        keySet.addAll(columns);
        DataSet dataSet = dataSetCache.get(keySet);
        if (dataSet == null) {
            TableDefinitionMetadata source = TableDefinitionMetadata.writeConnection(getFields(), getFeedID());
            try {
                dataSet = source.retrieveData(new ArrayList<Key>(keySet), null);
            } catch (SQLException e) {
                LogClass.error(e);
                throw new RuntimeException(e);
            } finally {
                source.closeConnection();
            }
            dataSetCache.put(keySet, dataSet);
        }

        if (!adminMode) {
            dataSet = dataSet.nextStep(getAnalysisDefinition(), getAnalysisDefinition().getAllAnalysisItems(), insightRequestMetadata);
        }
        return dataSet;
    }
}
