package com.easyinsight.datafeeds;

import com.easyinsight.core.Key;
import com.easyinsight.core.XMLMetadata;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.*;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.logging.LogClass;
import org.apache.jcs.access.exception.CacheException;

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
        Map<Key, List<Key>> map = new HashMap<Key, List<Key>>();
        Map<String, List<AnalysisItem>> map1 = new HashMap<String, List<AnalysisItem>>();
        for (AnalysisItem analysisItem : analysisItems) {
            List<AnalysisItem> items = map1.get(analysisItem.getOriginalDisplayName());
            if (items == null) {
                items = new ArrayList<AnalysisItem>();
                map1.put(analysisItem.getOriginalDisplayName(), items);
            }
            items.add(analysisItem);
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
            List<AnalysisItem> items = map1.get(analysisItem.toDisplay());
            if (items != null) {
                for (AnalysisItem mapped : items) {
                    List<Key> keys = map.get(analysisItem.createAggregateKey());
                    if (keys == null) {
                        keys = new ArrayList<Key>();
                        map.put(analysisItem.createAggregateKey(), keys);
                    }
                    keys.add(mapped.createAggregateKey());
                }
            }
        }
        CacheKey cacheKey = null;
        if (analysisDefinition.isCacheable()) {
            List<String> filterStrings = new ArrayList<String>();
            XMLMetadata xmlMetadata = new XMLMetadata();
            xmlMetadata.setConn(conn);
            for (FilterDefinition filter : filters) {
                filterStrings.add(filter.toXML(xmlMetadata).toXML());
            }
            cacheKey = new CacheKey(analysisDefinition.getAnalysisID(), filterStrings);
            DataSet embeddedResults = ReportCache.instance().getAddonResults(analysisDefinition.getDataFeedID(), cacheKey, analysisDefinition.getCacheMinutes());
            if (embeddedResults != null) {
                LogClass.debug("*** Returning from cache");
                return embeddedResults;
            }
        }
        DataSet dataSet = DataService.listDataSet(analysisDefinition, insightRequestMetadata, conn);
        // map this data set back into the original one
        for (Map.Entry<Key, List<Key>> entry : map.entrySet()) {
            for (Key key : entry.getValue()) {
                dataSet.replaceKey(entry.getKey(), key);
            }
        }
        analysisDefinition.setFilterDefinitions(reportFilters);
        for (Map.Entry<FilterDefinition, AnalysisItem> entry : filterMap.entrySet()) {
            entry.getKey().setField(entry.getValue());
        }
        if (cacheKey != null) {
            try {
                ReportCache.instance().storeAddonReport(analysisDefinition.getDataFeedID(), cacheKey, dataSet);
            } catch (CacheException e) {
                LogClass.debug(e.getMessage());
            }
        }
        return dataSet;
    }
}
