package com.easyinsight.analysis;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.etl.LookupTable;
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



    public AnalysisItemResultMetadata getAnalysisItemMetadata(long feedID, AnalysisItem analysisItem, int utfOffset, long reportID, long dashboardID) {
        if (reportID > 0) {
            SecurityUtil.authorizeInsight(reportID);
        } else if (dashboardID > 0) {
            SecurityUtil.authorizeDashboard(dashboardID);
        } else {
            SecurityUtil.authorizeFeedAccess(feedID);
        }
        EIConnection conn = Database.instance().getConnection();
        try {
            if (analysisItem == null) {
                LogClass.error("Received null analysis item from feed " + feedID);
                return null;
            }
            Feed feed = feedRegistry.getFeed(feedID, conn);
            InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
            insightRequestMetadata.setUtcOffset(utfOffset);
            timeshift(Arrays.asList(analysisItem), new ArrayList<FilterDefinition>(), feed, insightRequestMetadata);
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
            feedMetadata.setExchangeSave(feed.isExchangeSave());
            feedMetadata.setUrlKey(feed.getUrlKey());
            feedMetadata.setDataSourceInfo(feed.getDataSourceInfo());
            feedMetadata.getDataSourceInfo().setLastDataTime(feed.createSourceInfo(conn).getLastDataTime());
            feedMetadata.setDataSourceAdmin(SecurityUtil.getRole(SecurityUtil.getUserID(false), feedID) == Roles.OWNER);
            feedMetadata.setCustomJoinsAllowed(feed.getDataSource().customJoinsAllowed());
            List<LookupTable> lookupTables = new ArrayList<LookupTable>();
            for (AnalysisItem field : feedItems) {
                if (field.getLookupTableID() != null && field.getLookupTableID() > 0) {
                    lookupTables.add(new FeedService().getLookupTable(field.getLookupTableID(), conn));
                }
            }
            feedMetadata.setLookupTables(lookupTables);
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

    private static DataSet getDataSet(Feed feed, Set<AnalysisItem> validQueryItems, Collection<FilterDefinition> filters,
                               InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> fields, EIConnection conn) {
        DataSet dataSet = null;
        boolean valid = false;
        while (!valid) {
            try {
                dataSet = feed.getAggregateDataSet(validQueryItems, filters, insightRequestMetadata, fields, false, conn);
                valid = true;
            } catch (InvalidFieldsException ife) {
                throw ife;
                //validQueryItems.removeAll(ife.getAnalysisItems());
            }
        }
        return dataSet;
    }

    public EmbeddedResults getEmbeddedResults(long reportID, long dataSourceID, List<FilterDefinition> customFilters,
                                              InsightRequestMetadata insightRequestMetadata, List<FilterDefinition> drillThroughFilters) {
        SecurityUtil.authorizeInsight(reportID);

        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            Feed feed = feedRegistry.getFeed(dataSourceID, conn);

            WSAnalysisDefinition analysisDefinition = new AnalysisStorage().getAnalysisDefinition(reportID, conn);
            if (analysisDefinition == null) {
                return null;
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
            insightRequestMetadata.setJoinOverrides(analysisDefinition.getJoinOverrides());
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
            timeshift(validQueryItems, filters, feed, insightRequestMetadata);
            DataSet dataSet = getDataSet(feed, validQueryItems, filters, insightRequestMetadata, feed.getFields(), conn);
            Pipeline pipeline = new StandardReportPipeline();
            pipeline.setup(analysisDefinition, feed, insightRequestMetadata);
            DataResults listDataResults = pipeline.toList(dataSet);
            results = listDataResults.toEmbeddedResults();
            results.setDefinition(analysisDefinition);


            if (dataSet.getLastTime() == null) {
                dataSet.setLastTime(new Date());
            }
            DataSourceInfo dataSourceInfo = feed.getDataSourceInfo();
            dataSourceInfo.setLastDataTime(feed.createSourceInfo(conn).getLastDataTime());
            results.setDataSourceInfo(dataSourceInfo);
            BenchmarkManager.recordBenchmark("DataService:List", System.currentTimeMillis() - startTime);
            conn.commit();
            return results;
        } catch (ReportException re) {
            EmbeddedDataResults results = new EmbeddedDataResults();
            results.setReportFault(re.getReportFault());
            return results;
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public DataSet listDataSet(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) {
        SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            if (insightRequestMetadata == null) {
                insightRequestMetadata = new InsightRequestMetadata();
            }
            insightRequestMetadata.setJoinOverrides(analysisDefinition.getJoinOverrides());
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
            timeshift(validQueryItems, filters, feed, insightRequestMetadata);
            DataSet dataSet = getDataSet(feed, validQueryItems, filters, insightRequestMetadata, feed.getFields(), conn);
            Pipeline pipeline = new StandardReportPipeline();
            pipeline.setup(analysisDefinition, feed, insightRequestMetadata);
            dataSet = pipeline.toDataSet(dataSet);
            conn.commit();
            return dataSet;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public static DataSet listDataSet(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata, EIConnection conn) {

        if (insightRequestMetadata == null) {
            insightRequestMetadata = new InsightRequestMetadata();
        }
        insightRequestMetadata.setJoinOverrides(analysisDefinition.getJoinOverrides());
        Feed feed = FeedRegistry.instance().getFeed(analysisDefinition.getDataFeedID(), conn);
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
        timeshift(validQueryItems, filters, feed, insightRequestMetadata);
        DataSet dataSet = getDataSet(feed, validQueryItems, filters, insightRequestMetadata, feed.getFields(), conn);
        Pipeline pipeline = new StandardReportPipeline();
        pipeline.setup(analysisDefinition, feed, insightRequestMetadata);
        return pipeline.toDataSet(dataSet);
    }

    public static DataResults list(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata,
                            EIConnection conn) {
        try {
            long startTime = System.currentTimeMillis();
            Feed feed = FeedRegistry.instance().getFeed(analysisDefinition.getDataFeedID(), conn);
            if (insightRequestMetadata == null) {
                insightRequestMetadata = new InsightRequestMetadata();
            }
            insightRequestMetadata.setJoinOverrides(analysisDefinition.getJoinOverrides());
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
            timeshift(validQueryItems, filters, feed, insightRequestMetadata);
            DataSet dataSet = getDataSet(feed, validQueryItems, filters, insightRequestMetadata, feed.getFields(), conn);
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
        }
    }

    private static void timeshift(Collection<AnalysisItem> items, Collection<FilterDefinition> filters, Feed dataSource, InsightRequestMetadata insightRequestMetadata) {
        for (AnalysisItem item : items) {
            if (item.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                AnalysisDateDimension dateDim = (AnalysisDateDimension) item;
                boolean dateTime = dataSource.getDataSource().checkDateTime(item.toOriginalDisplayName());
                //if (!insightRequestMetadata.isSuppressShifts()) {
                    dateDim.setTimeshift(dateTime);
                //}
            }
        }
        for (AnalysisItem item : items) {
            item.timeshift(dataSource, filters);
        }
        for (FilterDefinition filter : filters) {
            filter.timeshift(dataSource, filters);
        }
    }

    public DataResults list(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) {
        SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
        EIConnection conn = Database.instance().getConnection();
        try {
            long startTime = System.currentTimeMillis();
            Feed feed = FeedRegistry.instance().getFeed(analysisDefinition.getDataFeedID(), conn);
            if (insightRequestMetadata == null) {
                insightRequestMetadata = new InsightRequestMetadata();
            }
            insightRequestMetadata.setJoinOverrides(analysisDefinition.getJoinOverrides());
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
            timeshift(validQueryItems, filters, feed, insightRequestMetadata);
            DataSet dataSet = getDataSet(feed, validQueryItems, filters, insightRequestMetadata, feed.getFields(), conn);
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
            /*if (insightRequestMetadata.isReportEditor()) {
                new AdminService().logAction(new ActionReportLog(SecurityUtil.getUserID(), ActionLog.EDIT, analysisDefinition.getAnalysisID()), conn);
            }*/
            return results;
        } catch (ReportException dae) {
            ListDataResults embeddedDataResults = new ListDataResults();
            embeddedDataResults.setReportFault(dae.getReportFault());            
            return embeddedDataResults;
        } catch (Throwable e) {
            LogClass.error(e.getMessage() + " on running report " + analysisDefinition.getAnalysisID(), e);
            ListDataResults embeddedDataResults = new ListDataResults();
            embeddedDataResults.setReportFault(new ServerError(e.getMessage()));            
            return embeddedDataResults;
        } finally {
            Database.closeConnection(conn);
        }
    }


}
