package com.easyinsight.datafeeds;

import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.*;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.pipeline.DerivedDataSourcePipeline;

import java.util.*;

/**
 * User: James Boe
 * Date: May 5, 2008
 * Time: 2:45:56 PM
 */
public class AnalysisBasedFeed extends Feed {

    private WSAnalysisDefinition analysisDefinition;

    public FeedType getDataFeedType() {
        return FeedType.ANALYSIS_BASED;
    }

    public WSAnalysisDefinition getAnalysisDefinition() {
        return analysisDefinition;
    }

    public void setAnalysisDefinition(WSAnalysisDefinition analysisDefinition) {
        this.analysisDefinition = analysisDefinition;
    }

    /*public List<AnalysisItem> getFields() {
        WSAnalysisDefinition analysisDefinition = getAnalysisDefinition();
        return new ArrayList<AnalysisItem>(analysisDefinition.getAllAnalysisItems());
    }*/

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
        WSAnalysisDefinition analysisDefinition = getAnalysisDefinition();
        List<FilterDefinition> reportFilters = new ArrayList<FilterDefinition>(analysisDefinition.getFilterDefinitions());
        List<AnalysisItem> fields = new ArrayList<AnalysisItem>(analysisDefinition.createStructure().values());
        Map<Key, Key> map = new HashMap<Key, Key>();
        Map<String, AnalysisItem> map1 = new HashMap<String, AnalysisItem>();
        for (AnalysisItem analysisItem : analysisItems) {
            map1.put(analysisItem.getOriginalDisplayName(), analysisItem);
        }
        Map<FilterDefinition, AnalysisItem> filterMap = new HashMap<FilterDefinition, AnalysisItem>();
        for (FilterDefinition filter : filters) {
            if (filter.getField() != null) {
                for (AnalysisItem analysisItem : fields) {
                    if (analysisItem.toDisplay().equals(filter.getField().getOriginalDisplayName())) {
                        filterMap.put(filter, filter.getField());
                        filter.setField(analysisItem);
                        break;
                    }
                }
            }
            analysisDefinition.getFilterDefinitions().add(filter);
        }
        for (AnalysisItem analysisItem : fields) {
            AnalysisItem mapped = map1.get(analysisItem.toDisplay());
            if (mapped != null) {
                map.put(analysisItem.createAggregateKey(), mapped.createAggregateKey());
            }
        }
        DataSet dataSet = DataService.listDataSet(analysisDefinition, insightRequestMetadata, conn);
        // map this data set back into the original one
        for (Map.Entry<Key, Key> entry : map.entrySet()) {
            dataSet.replaceKey(entry.getKey(), entry.getValue());
        }
        analysisDefinition.setFilterDefinitions(reportFilters);
        for (Map.Entry<FilterDefinition, AnalysisItem> entry : filterMap.entrySet()) {
            entry.getKey().setField(entry.getValue());
        }
        return dataSet;
    }

    /*
   @Override
   public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
       WSAnalysisDefinition analysisDefinition = getAnalysisDefinition();
       Map<Key, Key> map = new HashMap<Key, Key>();
       for (AnalysisItem analysisItem : analysisItems) {
           if (analysisItem.getKey() instanceof ReportKey) {
               ReportKey reportKey = (ReportKey) analysisItem.getKey();
               Key existing = analysisItem.createAggregateKey();
               analysisItem.setKey(reportKey.getParentKey());
               analysisItem.clearCachedKey();
               map.put(analysisItem.createAggregateKey(), existing);
           }
       }
       DataSet dataSet = DataService.listDataSet(analysisDefinition, insightRequestMetadata, conn);
       // map this data set back into the original one
       for (Map.Entry<Key, Key> entry : map.entrySet()) {
           for (AnalysisItem analysisItem : analysisItems) {
               if (map.containsKey(analysisItem.createAggregateKey())) {
                   analysisItem.clearCachedKey();
                   analysisItem.setKey(map.get(analysisItem.createAggregateKey()));
               }
           }
           dataSet.replaceKey(entry.getKey(), entry.getValue());
       }
       for (Map.Entry<Key, Key> entry : map.entrySet()) {
           for (IRow row : dataSet.getRows()) {
               row.removeValue(entry.getKey());
           }
       }
       return dataSet;
   }
    */
}
