package com.easyinsight.datafeeds;

import com.easyinsight.ReportQueryNodeKey;
import com.easyinsight.analysis.*;
import com.easyinsight.core.DerivedKey;
import com.easyinsight.core.Key;
import com.easyinsight.core.ReportKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.pipeline.AltCompositeReportPipeline;
import com.easyinsight.pipeline.CompositeReportPipeline;
import com.easyinsight.pipeline.NamedPipeline;
import com.easyinsight.pipeline.Pipeline;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
* User: jamesboe
* Date: 6/27/12
* Time: 12:23 PM
*/
class ReportQueryStateNode extends QueryStateNode {
    public long reportID;
    private WSAnalysisDefinition report;
    private String pipelineName;
    private Map<String, AnalysisItem> map = new HashMap<String, AnalysisItem>();
    private QueryNodeKey queryNodeKey;
    private long dataSourceID;
    private Collection<FilterDefinition> parentFilters;
    private Feed sourceFeed;

    ReportQueryStateNode(long reportID, EIConnection conn, List<AnalysisItem> parentItems, InsightRequestMetadata insightRequestMetadata, Collection<FilterDefinition> parentFilters) {
        this.reportID = reportID;
        this.parentFilters = parentFilters;
        queryNodeKey = new ReportQueryNodeKey(reportID);
        report = new AnalysisStorage().getAnalysisDefinition(reportID, conn);
        queryData = new QueryData(queryNodeKey);
        this.conn = conn;
        dataSourceName = report.getName();
        sourceFeed = FeedRegistry.instance().getFeed(report.getDataFeedID());
        allFeedItems = sourceFeed.getFields();
        this.parentItems = parentItems;
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT DATA_SOURCE_ID FROM cached_addon_report_source WHERE report_id = ?");
            stmt.setLong(1, reportID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                dataSourceID = rs.getLong(1);
            }
            stmt.close();
        } catch (Exception e) {
            LogClass.error(e);
        }

    }

    public QueryNodeKey queryNodeKey() {
        return queryNodeKey;
    }

    public boolean handles(AnalysisItem analysisItem) {
        return analysisItem.getKey().hasReport(reportID) || map.get(analysisItem.toDisplay()) != null;
    }

    public void addJoinItem(AnalysisItem analysisItem, int dateLevel) {
        AnalysisItem matchedItem = null;
        if (sourceFeed.getFeedType().getType() == FeedType.REDBOOTH_COMPOSITE.getType()) {
            for (AnalysisItem field : parentItems) {
                if (field.getKey() instanceof ReportKey) {
                    ReportKey derivedKey = (ReportKey) field.getKey();
                    long dataSourceID = derivedKey.getReportID();
                    if (dataSourceID == this.reportID) {
                        if (analysisItem.toDisplay().equals(field.toDisplay())) {
                            matchedItem = field;
                            break;
                        }
                    }
                }
            }
            if (matchedItem == null) {
                for (AnalysisItem field : parentItems) {
                    if (analysisItem.toDisplay().equals(field.toDisplay())) {
                        matchedItem = field;
                        break;
                    }
                }
            }
            if (matchedItem == null) {
                matchedItem = analysisItem;
            }
        } else {
            matchedItem = analysisItem;
            for (AnalysisItem field : parentItems) {
                if (analysisItem.toDisplay().equals(field.toDisplay())) {
                    matchedItem = field;
                    break;
                }
            }
        }
        List<AnalysisItem> items = matchedItem.getAnalysisItems(new ArrayList<AnalysisItem>(allFeedItems), Arrays.asList(matchedItem), false, true, new HashSet<AnalysisItem>(), new AnalysisItemRetrievalStructure(null));
        for (AnalysisItem item : items) {
            addItem(item);
            joinItems.add(new JoinMetadata(item, dateLevel));
        }
    }

    public void addItem(AnalysisItem analysisItem) {
        /*if (analysisItem.hasType(AnalysisItemTypes.CALCULATION)) {
            AnalysisCalculation analysisCalculation = (AnalysisCalculation) analysisItem;
            if (analysisCalculation.isCachedCalculation()) {
                neededItems.add(analysisItem);
            }
        } else if (!analysisItem.isDerived()) {*/
            neededItems.add(analysisItem);
        //}
        queryData.neededItems.add(analysisItem);
    }

    public void addKey(Key key) {
        boolean alreadyHaveItem = false;
        for (AnalysisItem analysisItem : queryData.neededItems) {
            if (analysisItem.hasType(AnalysisItemTypes.DIMENSION) && analysisItem.getKey().toKeyString().equals(key.toKeyString())) {
                alreadyHaveItem = true;
            }
        }
        if (!alreadyHaveItem) {
            for (AnalysisItem analysisItem : parentItems) {
                if (analysisItem.hasType(AnalysisItemTypes.DIMENSION) && analysisItem.getKey().toBaseKey().getKeyID() == key.toBaseKey().getKeyID()) {
                    addJoinItem(analysisItem, 0);
                }
            }
        }
    }

    public DataSet produceDataSet(InsightRequestMetadata insightRequestMetadata) throws ReportException {
        Feed feed;
        if (dataSourceID > 0) {
            CachedAnalysisBasedFeed cachedFeed = new CachedAnalysisBasedFeed();
            cachedFeed.setAnalysisDefinition(report);
            cachedFeed.setReportFilters(parentFilters);
            feed = cachedFeed;
        } else {
            AnalysisBasedFeed cachedFeed = new AnalysisBasedFeed();
            cachedFeed.setAnalysisDefinition(report);
            feed = cachedFeed;
        }
        feed.setFields(allAnalysisItems);
        DataSet dataSet = feed.getAggregateDataSet(neededItems, filters, insightRequestMetadata, allAnalysisItems, false, conn);

        Pipeline pipeline;

        pipeline = new AltCompositeReportPipeline(joinItems);

        pipeline.setup(queryData.neededItems, feed.getFields(), insightRequestMetadata);
        originalDataSet = pipeline.toDataSet(dataSet);
        return originalDataSet;
    }

    public void addFilter(FilterDefinition filterDefinition) {
        filters.add(filterDefinition);
    }
}
