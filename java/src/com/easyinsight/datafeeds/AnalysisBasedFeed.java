package com.easyinsight.datafeeds;

import com.easyinsight.core.Key;
import com.easyinsight.core.ReportKey;
import com.easyinsight.core.Value;
import com.easyinsight.core.XMLMetadata;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.*;
import com.easyinsight.analysis.AnalysisItem;

import java.util.*;

/**
 * User: James Boe
 * Date: May 5, 2008
 * Time: 2:45:56 PM
 */
public class AnalysisBasedFeed extends Feed {

    private WSAnalysisDefinition analysisDefinition;
    //private Map<String, String> parameters;

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

    public Key originalField(Key key, AnalysisItem originalItem) {
        if (key instanceof ReportKey) {
            ReportKey reportKey = (ReportKey) key;
            Key parentKey = reportKey.getParentKey();
            if (parentKey instanceof ReportKey) {
                ReportKey parentReportKey = (ReportKey) parentKey;
                AnalysisBasedFeed cachedFeed = new AnalysisBasedFeed();
                cachedFeed.setAnalysisDefinition(new AnalysisStorage().getAnalysisDefinition(parentReportKey.getReportID()));
                return cachedFeed.originalField(parentReportKey, originalItem);
            } else {
                return FeedRegistry.instance().getFeed(analysisDefinition.getDataFeedID()).originalField(parentKey, originalItem);
            }
        }
        return null;
    }

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
        WSAnalysisDefinition analysisDefinition = getAnalysisDefinition();
        //analysisDefinition.updateFromParameters(parameters);
        InsightRequestMetadata localInsightRequestMetadata = new InsightRequestMetadata();
        localInsightRequestMetadata.setDepth(insightRequestMetadata.getDepth() + 1);
        if (localInsightRequestMetadata.getDepth() >= 10) {
            throw new RuntimeException("Server error in processing fields.");
        }
        localInsightRequestMetadata.setTargetCurrency(insightRequestMetadata.getTargetCurrency());
        localInsightRequestMetadata.setUtcOffset(insightRequestMetadata.getUtcOffset());
        localInsightRequestMetadata.setIp(insightRequestMetadata.getIp());
        localInsightRequestMetadata.setNow(insightRequestMetadata.getNow());
        localInsightRequestMetadata.setLogReport(insightRequestMetadata.isLogReport());
        List<FilterDefinition> reportFilters = new ArrayList<FilterDefinition>(analysisDefinition.getFilterDefinitions());
        if (insightRequestMetadata.getFilters() != null) {
            reportFilters.addAll(insightRequestMetadata.getFilters());
        }
        List<AnalysisItem> fields = new ArrayList<AnalysisItem>(analysisDefinition.createStructure().values());
        Map<Key, List<Key>> map = new HashMap<Key, List<Key>>();
        Map<String, List<AnalysisItem>> fieldsGroupedByOriginalDisplayName = new HashMap<String, List<AnalysisItem>>();
        Map<Long, List<AnalysisItem>> fieldsGroupedByOriginalFieldID = new HashMap<Long, List<AnalysisItem>>();
        for (AnalysisItem analysisItem : analysisItems) {
            System.out.println("item " + analysisItem.toDisplay() + " based on " + analysisItem.getBasedOnReportField() + " with original " + analysisItem.toOriginalDisplayName());
            if (analysisItem.getBasedOnReportField() != null && analysisItem.getBasedOnReportField() > 0) {
                List<AnalysisItem> items = fieldsGroupedByOriginalFieldID.get(analysisItem.getBasedOnReportField());
                if (items == null) {
                    items = new ArrayList<AnalysisItem>();
                    fieldsGroupedByOriginalFieldID.put(analysisItem.getBasedOnReportField(), items);
                }
                items.add(analysisItem);
            } else {
                List<AnalysisItem> items = fieldsGroupedByOriginalDisplayName.get(analysisItem.getOriginalDisplayName());
                if (items == null) {
                    items = new ArrayList<AnalysisItem>();
                    fieldsGroupedByOriginalDisplayName.put(analysisItem.getOriginalDisplayName(), items);
                }
                items.add(analysisItem);
            }
        }
        Map<FilterDefinition, AnalysisItem> filterMap = new HashMap<FilterDefinition, AnalysisItem>();

        for (FilterDefinition filter : filters) {
            if (filter.getField() != null) {
                if (filter.getField().getBasedOnReportField() != null && filter.getField().getBasedOnReportField() > 0) {
                    for (AnalysisItem analysisItem : fields) {
                        if (analysisItem.getAnalysisItemID() == filter.getField().getBasedOnReportField()) {
                            filterMap.put(filter, filter.getField());
                            filter.setField(analysisItem);
                            break;
                        }
                    }
                } else {
                    for (AnalysisItem analysisItem : fields) {
                        if (analysisItem.toDisplay().equals(filter.getField().getOriginalDisplayName())) {
                            filterMap.put(filter, filter.getField());
                            filter.setField(analysisItem);
                            break;
                        }
                    }
                }
                if (filter.getFilterName() != null) {
                    Boolean override = insightRequestMetadata.getFilterOverrideMap().get(filter.getFilterName());
                    if (override != null && override) {
                        Iterator<FilterDefinition> iter = analysisDefinition.getFilterDefinitions().iterator();
                        while (iter.hasNext()) {
                            FilterDefinition existing = iter.next();
                            if (filter.getFilterName() != null && !"".equals(filter.getFilterName()) && existing.getFilterName() != null && !"".equals(existing.getFilterName()) &&
                                    filter.getFilterName().equals(existing.getFilterName())) {
                                iter.remove();
                            }
                        }
                    }
                }
            }

            analysisDefinition.getFilterDefinitions().add(filter);
        }
        for (AnalysisItem analysisItem : fields) {
            List<AnalysisItem> items = fieldsGroupedByOriginalFieldID.get(analysisItem.getAnalysisItemID());
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
            items = fieldsGroupedByOriginalDisplayName.get(analysisItem.toDisplay());
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
            List<String> sorted = new ArrayList<String>();
            for (AnalysisItem analysisItem : analysisItems) {
                sorted.add(analysisItem.getKey().toKeyString());
            }
            Collections.sort(sorted);
            filterStrings.addAll(sorted);
            cacheKey = new CacheKey(analysisDefinition.getAnalysisID(), filterStrings);
            DataSet embeddedResults = ReportCache.instance().getAddonResults(analysisDefinition.getDataFeedID(), cacheKey, analysisDefinition.getCacheMinutes());
            if (embeddedResults != null) {
                try {
                    if (insightRequestMetadata.isLogReport()) {
                        embeddedResults.getAudits().add(
                                new ReportAuditEvent(ReportAuditEvent.QUERY, getName() + " retrieved " + embeddedResults.getRows().size() + " rows from cache."));
                    }
                    return embeddedResults.clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            } else {

            }
        }
        if (insightRequestMetadata.getFilters() != null) {
            analysisDefinition.getFilterDefinitions().addAll(insightRequestMetadata.getFilters());

        }
        DataSet dataSet = DataService.listDataSet(analysisDefinition, localInsightRequestMetadata, conn);
        // map this data set back into the original one
        for (Map.Entry<Key, List<Key>> entry : map.entrySet()) {
            for (Key key : entry.getValue()) {
                dataSet.replaceKey(entry.getKey(), key);
            }
        }
        for (IRow row : dataSet.getRows()) {
            for (AnalysisItem item : fields) {
                if (item.hasType(AnalysisItemTypes.MEASURE)) {
                    Value value = row.getValue(item.createAggregateKey(true));
                    Value existing = row.getValue(item.createAggregateKey(false));
                    if (existing.type() == Value.EMPTY) {
                        row.addValue(item.createAggregateKey(false), value);
                    }
                }
            }
        }
        analysisDefinition.setFilterDefinitions(reportFilters);
        for (Map.Entry<FilterDefinition, AnalysisItem> entry : filterMap.entrySet()) {
            entry.getKey().setField(entry.getValue());
        }
        List<String> addonWarnings = localInsightRequestMetadata.getWarnings();
        List<ReportAuditEvent> events = dataSet.getAudits();

        if (cacheKey != null) {
            ReportCache.instance().storeAddonReport(analysisDefinition.getDataFeedID(), cacheKey, dataSet, analysisDefinition.getCacheMinutes());
        }
        return dataSet;
    }
}
