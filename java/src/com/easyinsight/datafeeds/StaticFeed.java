package com.easyinsight.datafeeds;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
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
        AnalysisItem queryItem;
        if (analysisItem.hasType(AnalysisItemTypes.HIERARCHY)) {
            AnalysisHierarchyItem analysisHierarchyItem = (AnalysisHierarchyItem) analysisItem;
            queryItem = analysisHierarchyItem.getHierarchyLevel().getAnalysisItem();
        } else {
            queryItem = analysisItem;
        }
        List<AnalysisItem> queryList = Arrays.asList( queryItem);
        DataStorage source = DataStorage.readConnection(getFields(), getFeedID());
        DataSet dataSet;
        try {
            dataSet = source.retrieveData(queryList, null, null, null, null);
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            source.closeConnection();
        }
        for (IRow row : dataSet.getRows()) {
            Value value = row.getValue(queryItem.getKey());
            metadata.addValue(analysisItem, value);
        }
        return metadata;
    }

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, Collection<Key> additionalNeededKeys) {
        if (analysisItems.size() == 0) {
            return new DataSet();
        }
        DataSet dataSet;
        DataStorage source = DataStorage.readConnection(getFields(), getFeedID());
        try {
            dataSet = source.retrieveData(analysisItems, filters, additionalNeededKeys, null, insightRequestMetadata);
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            source.closeConnection();
        }
        if (!adminMode) {
            dataSet = dataSet.nextStep(getAnalysisDefinition(), getAnalysisDefinition().getAllAnalysisItems(), insightRequestMetadata);
        }
        return dataSet;
    }

    protected DataSet getUncachedDataSet(List<Key> columns, Integer maxRows, boolean adminMode, InsightRequestMetadata insightRequestMetadata) {

        /*if (columns.size() == 0) {
            return new DataSet();
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
            DataStorage source = DataStorage.readConnection(getFields(), getFeedID());
            try {
                dataSet = source.retrieveData(new ArrayList<Key>(keySet), null, null);
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
        return dataSet;*/
        throw new UnsupportedOperationException();
    }
}
