package com.easyinsight.analysis;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.benchmark.BenchmarkManager;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.Roles;
import com.easyinsight.pipeline.Pipeline;
import com.easyinsight.pipeline.StandardReportPipeline;

import java.util.*;


/**
 * User: jboe
 * Date: Dec 22, 2007
 * Time: 1:16:37 PM
 */

public class DataService {



    private FeedRegistry feedRegistry = FeedRegistry.instance();



    public AnalysisItemResultMetadata getAnalysisItemMetadata(long feedID, AnalysisItem analysisItem, int utfOffset) {
        SecurityUtil.authorizeFeedAccess(feedID);
        EIConnection conn = Database.instance().getConnection();
        try {
            if (analysisItem == null) {
                LogClass.error("Received null analysis item from feed " + feedID);
                return null;
            }
            Feed feed = feedRegistry.getFeed(feedID, conn);
            InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
            insightRequestMetadata.setUtcOffset(utfOffset);
            return feed.getMetadata(analysisItem, insightRequestMetadata, conn);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public FeedMetadata getFeedMetadata(long feedID) {
        SecurityUtil.authorizeFeedAccess(feedID);
        EIConnection conn = Database.instance().getConnection();
        try {
            Feed feed = feedRegistry.getFeed(feedID, conn);
            Collection<AnalysisItem> feedItems = feed.getFields();
            // need to apply renames from the com.easyinsight.analysis definition here?
            List<AnalysisItem> sortedList = new ArrayList<AnalysisItem>(feedItems);
            Collections.sort(sortedList, new Comparator<AnalysisItem>() {

                public int compare(AnalysisItem o1, AnalysisItem o2) {
                    return o1.toDisplay().compareTo(o2.toDisplay());
                }
            });
            AnalysisItem[] feedItemArray = new AnalysisItem[sortedList.size()];
            sortedList.toArray(feedItemArray);
            FeedMetadata feedMetadata = new FeedMetadata();
            feedMetadata.setFilterExampleMessage(feed.getFilterExampleMessage());
            feedMetadata.setDataSourceName(feed.getName());
            feedMetadata.setFields(feedItemArray);
            feedMetadata.setFieldHierarchy(feed.getFieldHierarchy());
            feedMetadata.setIntrinsicFilters(feed.getIntrinsicFilters(conn));
            feedMetadata.setDataFeedID(feedID);
            feedMetadata.setVersion(feed.getVersion());
            feedMetadata.setOriginSolution(feed.getOriginSolution());
            feedMetadata.setUrlKey(feed.getUrlKey());
            feedMetadata.setDataSourceInfo(feed.getDataSourceInfo());
            feedMetadata.getDataSourceInfo().setLastDataTime(feed.createSourceInfo(conn).getLastDataTime());
            feedMetadata.setDataSourceAdmin(SecurityUtil.getRole(SecurityUtil.getUserID(false), feedID) == Roles.OWNER);
            return feedMetadata;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    private Set<Long> validate(WSAnalysisDefinition analysisDefinition, Feed feed) {
        Set<Long> ids = new HashSet<Long>();
        Set<Long> invalidIDs = new HashSet<Long>();
        for (AnalysisItem analysisItem : feed.getFields()) {
            ids.add(analysisItem.getKey().getKeyID());
        }
        Set<AnalysisItem> items = analysisDefinition.getAllAnalysisItems();
        for (AnalysisItem analysisItem : items) {
            if (analysisItem.getKey().getKeyID() != 0 && !ids.contains(analysisItem.getKey().getKeyID())) {
                invalidIDs.add(analysisItem.getKey().getKeyID());
            }
        }
        if (analysisDefinition.retrieveFilterDefinitions() != null) {
            for (FilterDefinition filterDefinition : analysisDefinition.retrieveFilterDefinitions()) {
                if (filterDefinition.getField().getKey().getKeyID() != 0 && !ids.contains(filterDefinition.getField().getKey().getKeyID())) {
                    invalidIDs.add(filterDefinition.getField().getKey().getKeyID());
                }
            }
        }
        return invalidIDs;
    }

    public EmbeddedResults getEmbeddedResults(long reportID, long dataSourceID, List<FilterDefinition> customFilters,
                                              InsightRequestMetadata insightRequestMetadata, List<FilterDefinition> drillThroughFilters) {
        SecurityUtil.authorizeInsight(reportID);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            Feed feed = feedRegistry.getFeed(dataSourceID, conn);

            WSAnalysisDefinition analysisDefinition = new AnalysisService().openAnalysisDefinition(reportID);
            if (analysisDefinition == null) {
                return null;
            }
            boolean dataSourceAccessible;
            try {
                SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
                dataSourceAccessible = true;
            } catch (Exception e) {
                dataSourceAccessible = false;
            }
            if (customFilters != null) {
                analysisDefinition.setFilterDefinitions(customFilters);
            }
            if (drillThroughFilters != null) {
                analysisDefinition.applyFilters(drillThroughFilters);
            }

            long startTime = System.currentTimeMillis();
            EmbeddedResults results;
            /*Set<Long> ids = validate(analysisDefinition, feed);
            if (ids.size() > 0) {
                throw new RuntimeException("Report is no longer valid.");
            } else {*/

            if (insightRequestMetadata.getHierarchyOverrides() != null) {
                for (AnalysisItemOverride hierarchyOverride : insightRequestMetadata.getHierarchyOverrides()) {
                    hierarchyOverride.apply(analysisDefinition.getAllAnalysisItems());
                }
            }
            List<AnalysisItem> allFields = new ArrayList<AnalysisItem>(feed.getFields());
            if (analysisDefinition.getAddedItems() != null) {
                allFields.addAll(analysisDefinition.getAddedItems());
            }
            Set<AnalysisItem> analysisItems = analysisDefinition.getColumnItems(allFields);
            Set<AnalysisItem> validQueryItems = new HashSet<AnalysisItem>();
            for (AnalysisItem analysisItem : analysisItems) {
                if (!analysisItem.isDerived() && (analysisItem.getLookupTableID() == null || analysisItem.getLookupTableID() == 0)) {
                    validQueryItems.add(analysisItem);
                }
            }
            boolean aggregateQuery = true;
            Set<AnalysisItem> items = analysisDefinition.getAllAnalysisItems();
            items.remove(null);
            for (AnalysisItem analysisItem : items) {
                if (analysisItem.blocksDBAggregation()) {
                    aggregateQuery = false;
                }
            }
            insightRequestMetadata.setAggregateQuery(aggregateQuery);
            Collection<FilterDefinition> filters = analysisDefinition.retrieveFilterDefinitions();
            DataSet dataSet = feed.getAggregateDataSet(validQueryItems, filters, insightRequestMetadata, feed.getFields(), false, conn);
            Pipeline pipeline = new StandardReportPipeline();
            pipeline.setup(analysisDefinition, feed, insightRequestMetadata);
            DataResults listDataResults = pipeline.toList(dataSet);
            results = listDataResults.toEmbeddedResults();
            results.setDataSourceAccessible(dataSourceAccessible);
            results.setDefinition(analysisDefinition);

            ReportMetrics reportMetrics = new AnalysisStorage().getRating(reportID);
            results.setRatingsAverage(reportMetrics.getAverage());
            results.setRatingsCount(reportMetrics.getCount());
            results.setMyRating(reportMetrics.getMyRating());
            if (dataSet.getLastTime() == null) {
                dataSet.setLastTime(new Date());
            }
            DataSourceInfo dataSourceInfo = feed.getDataSourceInfo();
            dataSourceInfo.setLastDataTime(feed.createSourceInfo(conn).getLastDataTime());
            results.setDataSourceInfo(dataSourceInfo);
            results.setAttribution(feed.getAttribution());
            //}
            BenchmarkManager.recordBenchmark("DataService:List", System.currentTimeMillis() - startTime);
            return results;
        } catch (ReportException re) {
            EmbeddedDataResults results = new EmbeddedDataResults();
            results.setReportFault(re.getReportFault());
            return results;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public DataSet listDataSet(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) {
        SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
        EIConnection conn = Database.instance().getConnection();
        try {
            Feed feed = feedRegistry.getFeed(analysisDefinition.getDataFeedID(), conn);
            List<AnalysisItem> allFields = new ArrayList<AnalysisItem>(feed.getFields());
            if (analysisDefinition.getAddedItems() != null) {
                allFields.addAll(analysisDefinition.getAddedItems());
            }
            Set<AnalysisItem> analysisItems = analysisDefinition.getColumnItems(allFields);
            Set<AnalysisItem> validQueryItems = new HashSet<AnalysisItem>();
            for (AnalysisItem analysisItem : analysisItems) {
                if (!analysisItem.isDerived() && (analysisItem.getLookupTableID() == null || analysisItem.getLookupTableID() == 0)) {
                    validQueryItems.add(analysisItem);
                }
            }
            boolean aggregateQuery = true;
            Set<AnalysisItem> items = analysisDefinition.getAllAnalysisItems();
            items.remove(null);
            for (AnalysisItem analysisItem : items) {
                if (analysisItem.blocksDBAggregation()) {
                    aggregateQuery = false;
                }
            }
            insightRequestMetadata.setAggregateQuery(aggregateQuery);
            Collection<FilterDefinition> filters = analysisDefinition.retrieveFilterDefinitions();
            DataSet dataSet = feed.getAggregateDataSet(validQueryItems, filters, insightRequestMetadata, feed.getFields(), false, conn);
            Pipeline pipeline = new StandardReportPipeline();
            pipeline.setup(analysisDefinition, feed, insightRequestMetadata);
            return pipeline.toDataSet(dataSet);
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public DataResults list(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) {
        SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
        EIConnection conn = Database.instance().getConnection();
        try {
            long startTime = System.currentTimeMillis();
            Feed feed = feedRegistry.getFeed(analysisDefinition.getDataFeedID(), conn);
            if (insightRequestMetadata == null) {
                insightRequestMetadata = new InsightRequestMetadata();
            }
            DataResults results;
            
            List<AnalysisItem> allFields = new ArrayList<AnalysisItem>(feed.getFields());
            if (analysisDefinition.getAddedItems() != null) {
                allFields.addAll(analysisDefinition.getAddedItems());
            }
            Set<AnalysisItem> analysisItems = analysisDefinition.getColumnItems(allFields);
            Set<AnalysisItem> validQueryItems = new HashSet<AnalysisItem>();
            for (AnalysisItem analysisItem : analysisItems) {
                if (!analysisItem.isDerived() && (analysisItem.getLookupTableID() == null || analysisItem.getLookupTableID() == 0)) {
                    validQueryItems.add(analysisItem);
                }
            }
            boolean aggregateQuery = true;
            Set<AnalysisItem> items = analysisDefinition.getAllAnalysisItems();
            items.remove(null);
            for (AnalysisItem analysisItem : items) {
                if (analysisItem.blocksDBAggregation()) {
                    aggregateQuery = false;
                }
            }
            insightRequestMetadata.setAggregateQuery(aggregateQuery);
            Collection<FilterDefinition> filters = analysisDefinition.retrieveFilterDefinitions();
            DataSet dataSet = feed.getAggregateDataSet(validQueryItems, filters, insightRequestMetadata, feed.getFields(), false, conn);
            List<String> auditMessages = dataSet.getAudits();
            auditMessages.add("At raw data source level, had " + dataSet.getRows().size() + " rows of data");
            //results = dataSet.toList(analysisDefinition, feed.getFields(), insightRequestMetadata);
            Pipeline pipeline = new StandardReportPipeline();
            pipeline.setup(analysisDefinition, feed, insightRequestMetadata);
            results = pipeline.toList(dataSet);
            DataSourceInfo dataSourceInfo = feed.getDataSourceInfo();
            if (dataSet.getLastTime() == null) {
                dataSet.setLastTime(new Date());
            }
            dataSourceInfo.setLastDataTime(feed.createSourceInfo(conn).getLastDataTime());
            //dataSourceInfo.setLastDataTime(dataSet.getLastTime());
            results.setDataSourceInfo(dataSourceInfo);
            results.setAuditMessages(auditMessages);
            // }
            BenchmarkManager.recordBenchmark("DataService:List", System.currentTimeMillis() - startTime);
            return results;
        } catch (ReportException dae) {
            ListDataResults embeddedDataResults = new ListDataResults();
            embeddedDataResults.setReportFault(dae.getReportFault());            
            return embeddedDataResults;
        } catch (Throwable e) {
            LogClass.error(e);
            ListDataResults embeddedDataResults = new ListDataResults();
            embeddedDataResults.setReportFault(new ServerError(e.getMessage()));            
            return embeddedDataResults;
        } finally {
            Database.closeConnection(conn);
        }
    }
}
