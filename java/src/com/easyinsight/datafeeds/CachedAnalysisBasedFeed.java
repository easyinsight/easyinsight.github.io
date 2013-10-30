package com.easyinsight.datafeeds;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.core.XMLMetadata;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
 * User: James Boe
 * Date: May 5, 2008
 * Time: 2:45:56 PM
 */
public class CachedAnalysisBasedFeed extends Feed {

    private WSAnalysisDefinition analysisDefinition;
    private Collection<FilterDefinition> reportFilters;
    //private Map<String, String> parameters;

    public FeedType getDataFeedType() {
        return FeedType.ANALYSIS_BASED;
    }

    public Collection<FilterDefinition> getReportFilters() {
        return reportFilters;
    }

    public void setReportFilters(Collection<FilterDefinition> reportFilters) {
        this.reportFilters = reportFilters;
    }

    public WSAnalysisDefinition getAnalysisDefinition() {
        return analysisDefinition;
    }

    public void setAnalysisDefinition(WSAnalysisDefinition analysisDefinition) {
        this.analysisDefinition = analysisDefinition;
    }

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {

        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT data_source_id FROM cached_addon_report_source WHERE report_id = ?");
            stmt.setLong(1, analysisDefinition.getAnalysisID());
            ResultSet rs = stmt.executeQuery();
            rs.next();
            long dataSourceID = rs.getLong(1);
            stmt.close();
            Feed feed = FeedRegistry.instance().getFeed(dataSourceID);

            Map<AnalysisItem, List<AnalysisItem>> map = new HashMap<AnalysisItem, List<AnalysisItem>>();
            Map<String, List<AnalysisItem>> fieldsGroupedByOriginalDisplayName = new HashMap<String, List<AnalysisItem>>();
            Map<Long, List<AnalysisItem>> fieldsGroupedByOriginalFieldID = new HashMap<Long, List<AnalysisItem>>();
            for (AnalysisItem analysisItem : analysisItems) {
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



            Map<FilterDefinition, AnalysisItem> filterBackMap = new HashMap<FilterDefinition, AnalysisItem>();
            for (FilterDefinition filter : filters) {
                if (filter.getField() != null) {
                    if (filter.getField().getBasedOnReportField() != null && filter.getField().getBasedOnReportField() > 0) {
                        for (AnalysisItem analysisItem : feed.getFields()) {
                            if (analysisItem.getBasedOnReportField() != null && analysisItem.getBasedOnReportField().equals(filter.getField().getBasedOnReportField())) {
                                filterBackMap.put(filter, filter.getField());
                                filter.setField(analysisItem);
                                break;
                            }
                        }
                    } else {
                        for (AnalysisItem analysisItem : feed.getFields()) {
                            if (analysisItem.toOriginalDisplayName().equals(filter.getField().getOriginalDisplayName())) {
                                filterBackMap.put(filter, filter.getField());
                                filter.setField(analysisItem);
                                break;
                            }
                        }
                    }

                }
            }
            List<FilterDefinition> includeFilters = new ArrayList<FilterDefinition>();
            includeFilters.addAll(filters);
            if (analysisDefinition.isCacheFilters()) {
                for (FilterDefinition filter : analysisDefinition.getFilterDefinitions()) {
                    if (filter.getField() != null) {
                        for (FilterDefinition reportFilter : reportFilters) {
                            if (reportFilter.getField() != null && reportFilter.getField().equals(filter.getField()) &&
                                    reportFilter.getClass().getName().equals(filter.getClass().getName()) &&
                                    reportFilter.isNotCondition() == filter.isNotCondition()) {
                                System.out.println("Including filter on " + reportFilter.getField().toDisplay());
                                includeFilters.add(filter);
                            }
                        }
                    }
                }
            }

            for (AnalysisItem analysisItem : feed.getFields()) {
                List<AnalysisItem> items = fieldsGroupedByOriginalFieldID.get(analysisItem.getBasedOnReportField());
                if (items != null) {
                    for (AnalysisItem mapped : items) {
                        List<AnalysisItem> keys = map.get(analysisItem);
                        if (keys == null) {
                            keys = new ArrayList<AnalysisItem>();
                            map.put(analysisItem, keys);
                        }
                        keys.add(mapped);
                    }
                }
                items = fieldsGroupedByOriginalDisplayName.get(analysisItem.toOriginalDisplayName());
                if (items != null) {
                    for (AnalysisItem mapped : items) {
                        List<AnalysisItem> keys = map.get(analysisItem);
                        if (keys == null) {
                            keys = new ArrayList<AnalysisItem>();
                            map.put(analysisItem, keys);
                        }
                        keys.add(mapped);
                    }
                }
            }

            Set<AnalysisItem> items = new HashSet<AnalysisItem>(map.keySet());
            DataSet reportSet = feed.getAggregateDataSet(items, includeFilters, insightRequestMetadata, allAnalysisItems, adminMode, conn);
            DataSet dataSet = new DataSet();
            for (IRow reportRow : reportSet.getRows()) {
                IRow row = dataSet.createRow();
                for (Map.Entry<AnalysisItem, List<AnalysisItem>> entry : map.entrySet()) {
                    Value value = reportRow.getValue(entry.getKey().createAggregateKey());
                    for (AnalysisItem item : entry.getValue()) {
                        row.addValue(item.createAggregateKey(), value);
                    }
                }
            }
            for (Map.Entry<FilterDefinition, AnalysisItem> entry : filterBackMap.entrySet()) {
                entry.getKey().setField(entry.getValue());
            }
            return dataSet;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
