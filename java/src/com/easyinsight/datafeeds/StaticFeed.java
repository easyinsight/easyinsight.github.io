package com.easyinsight.datafeeds;

import com.easyinsight.core.EmptyValue;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.etl.LookupPair;
import com.easyinsight.etl.LookupTable;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.logging.LogClass;
import com.easyinsight.analysis.*;
import com.easyinsight.core.Value;

import java.util.*;
import java.sql.SQLException;
import java.io.Serializable;

/**
 * User: James Boe
 * Date: Apr 26, 2008
 * Time: 8:07:57 PM
 */
public class StaticFeed extends Feed implements Serializable {

    //private Map<Key, ColumnSegment> cache = new WeakHashMap<Key, ColumnSegment>();

    public FeedType getDataFeedType() {
        return FeedType.STATIC;
    }

    /*public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata, EIConnection conn) throws ReportException {
        if (analysisItem.getLookupTableID() != null && analysisItem.getLookupTableID() > 0) {
            AnalysisItemResultMetadata analysisItemResultMetadata = analysisItem.createResultMetadata();
            Map<Value, Value> lookupMap = new HashMap<Value, Value>();
            LookupTable lookupTable = new FeedService().getLookupTable(analysisItem.getLookupTableID());
            AnalysisDimensionResultMetadata sourceMetadata = (AnalysisDimensionResultMetadata) getMetadata(lookupTable.getSourceField(), insightRequestMetadata, conn);
            for (LookupPair lookupPair : lookupTable.getLookupPairs()) {
                lookupMap.put(lookupPair.getSourceValue(), lookupPair.getTargetValue());
            }
            for (Value value : sourceMetadata.getValues()) {
                Value targetValue = lookupMap.get(value);
                if (targetValue == null) {
                    targetValue = new EmptyValue();
                }
                analysisItemResultMetadata.addValue(analysisItem, targetValue, insightRequestMetadata);
            }
            return analysisItemResultMetadata;
        }
        // this would change to return the data from our contained analysis definition
        AnalysisItemResultMetadata metadata = analysisItem.createResultMetadata();
        AnalysisItem queryItem;
        if (analysisItem.hasType(AnalysisItemTypes.HIERARCHY)) {
            AnalysisHierarchyItem analysisHierarchyItem = (AnalysisHierarchyItem) analysisItem;
            queryItem = analysisHierarchyItem.getHierarchyLevel().getAnalysisItem();
        } else if (analysisItem.hasType(AnalysisItemTypes.STEP)) {
            AnalysisStep step = (AnalysisStep) analysisItem;
            queryItem = step.getEndDate();
        } else {
            queryItem = analysisItem;
        }
        List<AnalysisItem> queryList = Arrays.asList( queryItem);
        DataStorage source = DataStorage.readConnection(getFields(), getFeedID());
        DataSet dataSet;
        try {
            dataSet = source.retrieveData(queryList, null, null, null);
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            source.closeConnection();
        }
        for (IRow row : dataSet.getRows()) {
            Value value = row.getValue(queryItem.createAggregateKey());
            metadata.addValue(analysisItem, value, insightRequestMetadata);
        }
        return metadata;
    }*/

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
        if (analysisItems.size() == 0) {
            return new DataSet();
        }
        DataSet dataSet;
        DataStorage source = DataStorage.readConnection(getFields(), getFeedID());
        try {
            dataSet = source.retrieveData(analysisItems, filters, null, insightRequestMetadata);
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            source.closeConnection();
        }
        /*if (!adminMode) {
            dataSet = new DerivedDataSourcePipeline().setup(getAnalysisDefinition(), this, insightRequestMetadata).toDataSet(dataSet);
        }*/
        return dataSet;
    }
}
