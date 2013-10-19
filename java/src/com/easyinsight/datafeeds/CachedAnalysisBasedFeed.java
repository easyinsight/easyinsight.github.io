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

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {

        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT data_source_id FROM cached_addon_report_source WHERE report_id = ?");
            stmt.setLong(1, analysisDefinition.getAnalysisID());
            ResultSet rs = stmt.executeQuery();
            rs.next();
            long dataSourceID = rs.getLong(1);
            Feed feed = FeedRegistry.instance().getFeed(dataSourceID);

            Map<AnalysisItem, Collection<AnalysisItem>> map = new HashMap<AnalysisItem, Collection<AnalysisItem>>();
            for (AnalysisItem item : analysisItems) {
                boolean found = false;
                for (AnalysisItem reportField : feed.getFields()) {
                    if (reportField.getBasedOnReportField().equals(item.getBasedOnReportField())) {
                        Collection<AnalysisItem> items = map.get(reportField);
                        if (items == null) {
                            items = new ArrayList<AnalysisItem>();
                            map.put(reportField, items);
                        }
                        items.add(item);
                        found = true;
                    }
                }

                if (!found) {
                    System.out.println("\tCould not find field " + item.toDisplay() + " with based on = " + item.getBasedOnReportField());
                }
            }

            Map<FilterDefinition, AnalysisItem> filterBackMap = new HashMap<FilterDefinition, AnalysisItem>();
            for (FilterDefinition filter : filters) {
                if (filter.getField() != null) {
                    for (AnalysisItem reportField : feed.getFields()) {
                        if (filter.getField().getBasedOnReportField().equals(reportField.getBasedOnReportField())) {
                            filterBackMap.put(filter, filter.getField());
                            filter.setField(reportField);
                            break;
                        }
                    }
                }
            }

            Set<AnalysisItem> items = new HashSet<AnalysisItem>(map.keySet());
            DataSet reportSet = feed.getAggregateDataSet(items, filters, insightRequestMetadata, allAnalysisItems, adminMode, conn);
            DataSet dataSet = new DataSet();
            for (IRow reportRow : reportSet.getRows()) {
                IRow row = dataSet.createRow();
                for (Map.Entry<AnalysisItem, Collection<AnalysisItem>> entry : map.entrySet()) {
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
