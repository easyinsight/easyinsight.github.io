package com.easyinsight.datafeeds;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.pipeline.AltCompositeReportPipeline;
import com.easyinsight.pipeline.CompositeReportPipeline;
import com.easyinsight.pipeline.NamedPipeline;
import com.easyinsight.pipeline.Pipeline;

import java.util.*;

/**
* User: jamesboe
* Date: 6/27/12
* Time: 12:23 PM
*/
class QueryStateNode {
    public long feedID;
    public QueryData queryData;
    public Set<AnalysisItem> neededItems = new HashSet<AnalysisItem>();
    public List<AnalysisItem> allAnalysisItems = new ArrayList<AnalysisItem>();
    public Collection<FilterDefinition> filters = new ArrayList<FilterDefinition>();
    public Collection<AnalysisItem> allFeedItems;
    public List<AnalysisItem> parentItems = new ArrayList<AnalysisItem>();
    public Collection<AnalysisItem> joinItems = new HashSet<AnalysisItem>();
    public String dataSourceName;
    public EIConnection conn;
    public DataSet originalDataSet;
    public Feed feed;
    private String pipelineName;
    private Map<String, AnalysisItem> map = new HashMap<String, AnalysisItem>();

    QueryStateNode(long feedID, Feed feed, EIConnection conn, List<AnalysisItem> parentItems, InsightRequestMetadata insightRequestMetadata) {
        this.feedID = feedID;
        queryData = new QueryData(feedID);
        this.feed = feed;
        this.conn = conn;
        dataSourceName = feed.getName();
        allFeedItems = feed.getFields();
        this.parentItems = parentItems;
        NamedPipeline pipeline = (NamedPipeline) insightRequestMetadata.findPipeline(feed.getName());
        if (pipeline != null) {
            pipelineName = pipeline.getName();
            List<AnalysisItem> analysisItems = insightRequestMetadata.getFieldsForPipeline(pipeline.getName());
            if (analysisItems != null) {
                for (AnalysisItem analysisItem : analysisItems) {
                    map.put(analysisItem.toDisplay(), analysisItem);
                }
            }
        }
    }

    public boolean handles(AnalysisItem analysisItem) {
        return analysisItem.getKey().hasDataSource(feedID) || map.get(analysisItem.toDisplay()) != null;
    }

    public void addJoinItem(AnalysisItem analysisItem) {
        for (AnalysisItem field : parentItems) {
            if (analysisItem.toDisplay().equals(field.toDisplay())) {
                analysisItem = field;
                break;
            }
        }
        List<AnalysisItem> items = analysisItem.getAnalysisItems(new ArrayList<AnalysisItem>(allFeedItems), Arrays.asList(analysisItem), false, true, new HashSet<AnalysisItem>(), new AnalysisItemRetrievalStructure(null));
        for (AnalysisItem item : items) {
            addItem(item);
            joinItems.add(item);
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
                    addJoinItem(analysisItem);
                }
            }
        }
    }

    public DataSet produceDataSet(InsightRequestMetadata insightRequestMetadata) throws ReportException {

        DataSet dataSet = feed.getAggregateDataSet(neededItems, filters, insightRequestMetadata, allAnalysisItems, false, conn);

        Pipeline pipeline;
        if (!insightRequestMetadata.isTraverseAllJoins() && feed.getDataSource().getFeedType().getType() == FeedType.BASECAMP_MASTER.getType()) {
            pipeline = new CompositeReportPipeline();
        } else {
            pipeline = new AltCompositeReportPipeline(joinItems);
        }
        pipeline.setup(queryData.neededItems, feed.getFields(), insightRequestMetadata);
        originalDataSet = pipeline.toDataSet(dataSet);
        return originalDataSet;
    }

    public void addFilter(FilterDefinition filterDefinition) {
        filters.add(filterDefinition);
    }
}
