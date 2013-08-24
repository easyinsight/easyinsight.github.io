package com.easyinsight.datafeeds.composite;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DerivedKey;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.dataset.DataSet;

import java.util.*;

/**
 * User: jamesboe
 * Date: 3/5/11
 * Time: 6:31 PM
 */
public class FederatedFeed extends Feed {

    private List<FederationSource> sources;

    public FederatedFeed(List<FederationSource> sources) {
        this.sources = sources;
    }

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
        try {
            DataSet dataSet = new DataSet();
            for (FederationSource source : sources) {
                Feed feed = FeedRegistry.instance().getFeed(source.getDataSourceID(), conn);

                Map<AnalysisItem, List<AnalysisItem>> map = new HashMap<AnalysisItem, List<AnalysisItem>>();
                Set<AnalysisItem> childAnalysisItems = new HashSet<AnalysisItem>();
                for (AnalysisItem analysisItem : analysisItems) {
                    boolean matched = false;
                    if (analysisItem.getKey() instanceof DerivedKey) {
                        DerivedKey derivedKey = (DerivedKey) analysisItem.getKey();
                        Key parentKey = derivedKey.getParentKey();
                        for (AnalysisItem field : feed.getDataSource().getFields()) {
                            if (field.getKey().toKeyString().equals(parentKey.toKeyString()) && field.isKeyColumn() == analysisItem.isKeyColumn()) {
                                matched = true;
                                childAnalysisItems.add(field);
                                List<AnalysisItem> items = map.get(field);
                                if (items == null) {
                                    items = new ArrayList<AnalysisItem>();
                                    map.put(field, items);
                                }
                                items.add(analysisItem);
                                break;
                            }
                        }
                    }
                    if (!matched) {
                        for (FieldMapping fieldMapping : source.getFieldMappings()) {
                            if (fieldMapping.getFederatedKey().equals(analysisItem.toOriginalDisplayName())) {
                                for (AnalysisItem field : feed.getDataSource().getFields()) {
                                    if (field.toOriginalDisplayName().equals(fieldMapping.getSourceKey()) && field.isKeyColumn() == analysisItem.isKeyColumn()) {
                                        matched = true;
                                        childAnalysisItems.add(field);
                                        List<AnalysisItem> items = map.get(field);
                                        if (items == null) {
                                            items = new ArrayList<AnalysisItem>();
                                            map.put(field, items);
                                        }
                                        items.add(analysisItem);
                                    }
                                }
                            }
                        }
                    }
                    if (!matched) {
                        for (AnalysisItem field : feed.getDataSource().getFields()) {
                            if (field.toOriginalDisplayName().equals(analysisItem.toOriginalDisplayName()) && field.isKeyColumn() == analysisItem.isKeyColumn()) {
                                childAnalysisItems.add(field);
                                List<AnalysisItem> items = map.get(field);
                                if (items == null) {
                                    items = new ArrayList<AnalysisItem>();
                                    map.put(field, items);
                                }
                                items.add(analysisItem);
                            }
                        }
                    }
                }
                Map<FilterDefinition, AnalysisItem> backMap = new HashMap<FilterDefinition, AnalysisItem>();
                for (FilterDefinition filter : filters) {
                    boolean matched = false;
                    if (filter.getField() != null) {
                        if (filter.getField().getKey() instanceof DerivedKey) {
                            DerivedKey derivedKey = (DerivedKey) filter.getField().getKey();
                            Key parentKey = derivedKey.getParentKey();
                            for (AnalysisItem field : feed.getDataSource().getFields()) {
                                if (field.getKey().toKeyString().equals(parentKey.toKeyString()) && field.isKeyColumn() == filter.getField().isKeyColumn()) {
                                    matched = true;
                                    backMap.put(filter, filter.getField());
                                    filter.setField(field);
                                }
                            }
                        }
                    }
                    for (FieldMapping fieldMapping : source.getFieldMappings()) {
                        if (fieldMapping.getFederatedKey().equals(filter.getField().toDisplay())) {
                            for (AnalysisItem field : feed.getDataSource().getFields()) {
                                if (field.toDisplay().equals(fieldMapping.getSourceKey())) {
                                    matched = true;
                                    backMap.put(filter, filter.getField());
                                    filter.setField(field);
                                }
                            }
                        }
                    }
                    if (!matched) {
                        for (AnalysisItem field : feed.getDataSource().getFields()) {
                            if (field.toDisplay().equals(filter.getField().toDisplay())) {
                                backMap.put(filter, filter.getField());
                                filter.setField(field);
                            }
                        }
                    }
                }
                DataSet childSet = feed.getAggregateDataSet(childAnalysisItems, filters, insightRequestMetadata, allAnalysisItems, adminMode, conn);

                for (FilterDefinition filter : filters) {
                    AnalysisItem field = backMap.get(filter);
                    if (field != null) {
                        filter.setField(field);
                    }
                }

                for (IRow row : childSet.getRows()) {
                    IRow newRow = dataSet.createRow();
                    for (Map.Entry<AnalysisItem, List<AnalysisItem>> entry : map.entrySet()) {
                        Value value = row.getValue(entry.getKey());
                        for (AnalysisItem valItem : entry.getValue()) {
                            newRow.addValue(valItem.createAggregateKey(), value);
                        }

                    }
                }
            }
            return dataSet;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
