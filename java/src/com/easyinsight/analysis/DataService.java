package com.easyinsight.analysis;

import com.easyinsight.analysis.definitions.WSCombinedVerticalListDefinition;
import com.easyinsight.core.Value;
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
import org.hibernate.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
            PreparedStatement dlsStmt = conn.prepareStatement("SELECT user_dls_to_filter.FILTER_ID FROM user_dls_to_filter, user_dls, dls where " +
                    "user_dls_to_filter.user_dls_id = user_dls.user_dls_id and user_dls.dls_id = dls.dls_id and dls.data_source_id = ? and " +
                    "user_dls.user_id = ?");
            dlsStmt.setLong(1, feedID);
            dlsStmt.setLong(2, SecurityUtil.getUserID());
            List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
            ResultSet dlsRS = dlsStmt.executeQuery();
            while (dlsRS.next()) {
                long filterID = dlsRS.getLong(1);
                Session session = Database.instance().createSession(conn);
                try {
                    List results = session.createQuery("from FilterDefinition where filterID = ?").setLong(0, filterID).list();
                    if (results.size() > 0) {
                        FilterDefinition filter = (FilterDefinition) results.get(0);
                        filter.getField().afterLoad();
                        filter.afterLoad();
                        filters.add(filter);
                    }
                } finally {
                    session.close();
                }
            }
            timeshift(Arrays.asList(analysisItem), new ArrayList<FilterDefinition>(), feed, insightRequestMetadata);
            AnalysisItemResultMetadata metadata = feed.getMetadata(analysisItem, insightRequestMetadata, conn);
            if (metadata instanceof AnalysisDimensionResultMetadata) {
                AnalysisDimensionResultMetadata dMetadata = (AnalysisDimensionResultMetadata) metadata;
                for (FilterDefinition filter : filters) {
                    MaterializedFilterDefinition mFilter = filter.materialize(insightRequestMetadata);
                    Iterator<Value> valueIter = dMetadata.getValues().iterator();
                    while (valueIter.hasNext()) {
                        Value value = valueIter.next();
                        if (!mFilter.allows(value)) {
                            valueIter.remove();
                        }
                    }
                }
            }
            return metadata;
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
            feedMetadata.setCustomJoinsAllowed(feed.getDataSource().customJoinsAllowed(conn));
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

    public EmbeddedVerticalResults getEmbeddedVerticalDataResults(long reportID, long dataSourceID, List<FilterDefinition> customFilters, InsightRequestMetadata insightRequestMetadata) {
        try {
            long startTime = System.currentTimeMillis();
            System.out.println("evr request for " + reportID);
            SecurityUtil.authorizeInsight(reportID);
            WSCombinedVerticalListDefinition analysisDefinition = (WSCombinedVerticalListDefinition) new AnalysisStorage().getAnalysisDefinition(reportID);
            insightRequestMetadata.setOptimized(analysisDefinition.isOptimized());
            if (customFilters != null) {
                analysisDefinition.setFilterDefinitions(customFilters);
            }
            List<EmbeddedResults> list = new ArrayList<EmbeddedResults>();
            for (WSAnalysisDefinition analysis : analysisDefinition.getReports()) {
                list.add(getEmbeddedResults(analysis.getAnalysisID(), dataSourceID, analysisDefinition.getFilterDefinitions(), insightRequestMetadata, null));
            }
            System.out.println("overall time = " + (System.currentTimeMillis() - startTime) / 1000);
            EmbeddedVerticalResults verticalDataResults = new EmbeddedVerticalResults();
            verticalDataResults.setList(list);
            verticalDataResults.setReport(analysisDefinition);
            return verticalDataResults;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public EmbeddedResults getEmbeddedResults(long reportID, long dataSourceID, List<FilterDefinition> customFilters,
                                              InsightRequestMetadata insightRequestMetadata, List<FilterDefinition> drillThroughFilters) {
        System.out.println("request for " + reportID);
        SecurityUtil.authorizeInsight(reportID);

        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            Feed feed = feedRegistry.getFeed(dataSourceID, conn);

            WSAnalysisDefinition analysisDefinition = new AnalysisStorage().getAnalysisDefinition(reportID, conn);
            if (analysisDefinition == null) {
                return null;
            }

            insightRequestMetadata.setOptimized(analysisDefinition.isOptimized());

            if (customFilters != null) {
                analysisDefinition.setFilterDefinitions(customFilters);
            }
            if (drillThroughFilters != null) {
                analysisDefinition.applyFilters(drillThroughFilters);
            }

            if (SecurityUtil.getUserID(false) != 0) {
                PreparedStatement dlsStmt = conn.prepareStatement("SELECT user_dls_to_filter.FILTER_ID FROM user_dls_to_filter, user_dls, dls where " +
                        "user_dls_to_filter.user_dls_id = user_dls.user_dls_id and user_dls.dls_id = dls.dls_id and dls.data_source_id = ? and " +
                        "user_dls.user_id = ?");
                dlsStmt.setLong(1, analysisDefinition.getDataFeedID());
                dlsStmt.setLong(2, SecurityUtil.getUserID());
                List<FilterDefinition> dlsFilters = new ArrayList<FilterDefinition>();
                ResultSet dlsRS = dlsStmt.executeQuery();
                while (dlsRS.next()) {
                    long filterID = dlsRS.getLong(1);
                    Session session = Database.instance().createSession(conn);
                    try {
                        List results = session.createQuery("from FilterDefinition where filterID = ?").setLong(0, filterID).list();
                        if (results.size() > 0) {
                            FilterDefinition filter = (FilterDefinition) results.get(0);
                            filter.getField().afterLoad();
                            filter.afterLoad();
                            dlsFilters.add(filter);
                        }
                    } finally {
                        session.close();
                    }
                }
                analysisDefinition.getFilterDefinitions().addAll(dlsFilters);
            }



            for (FilterDefinition filter : analysisDefinition.getFilterDefinitions()) {
                if (filter instanceof AnalysisItemFilterDefinition) {
                    AnalysisItemFilterDefinition analysisItemFilterDefinition = (AnalysisItemFilterDefinition) filter;
                    Map<String, AnalysisItem> structure = analysisDefinition.createStructure();
                    Map<String, AnalysisItem> structureCopy = new HashMap<String, AnalysisItem>(structure);
                    for (Map.Entry<String, AnalysisItem> entry : structureCopy.entrySet()) {
                        if (entry.getValue().equals(filter.getField())) {
                            structure.put(entry.getKey(), analysisItemFilterDefinition.getTargetItem());
                        }
                    }
                    analysisDefinition.populateFromReportStructure(structure);
                }
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
            //BenchmarkManager.recordBenchmark("DataService:List", System.currentTimeMillis() - startTime);
            conn.commit();
            //System.out.println("Took " + (System.currentTimeMillis() - beginTime) / 1000 + " seconds to retrieve " + reportID);
            return results;
        } catch (ReportException re) {
            EmbeddedDataResults results = new EmbeddedDataResults();
            results.setReportFault(re.getReportFault());
            return results;
        } catch (Exception e) {
            LogClass.error(e);
            EmbeddedDataResults results = new EmbeddedDataResults();
            results.setReportFault(new ServerError(e.getMessage()));
            return results;
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

            PreparedStatement dlsStmt = conn.prepareStatement("SELECT user_dls_to_filter.FILTER_ID FROM user_dls_to_filter, user_dls, dls where " +
                    "user_dls_to_filter.user_dls_id = user_dls.user_dls_id and user_dls.dls_id = dls.dls_id and dls.data_source_id = ? and " +
                    "user_dls.user_id = ?");
            dlsStmt.setLong(1, analysisDefinition.getDataFeedID());
            dlsStmt.setLong(2, SecurityUtil.getUserID());
            List<FilterDefinition> dlsFilters = new ArrayList<FilterDefinition>();
            ResultSet dlsRS = dlsStmt.executeQuery();
            while (dlsRS.next()) {
                long filterID = dlsRS.getLong(1);
                Session session = Database.instance().createSession(conn);
                try {
                    List dlsResults = session.createQuery("from FilterDefinition where filterID = ?").setLong(0, filterID).list();
                    if (dlsResults.size() > 0) {
                        FilterDefinition filter = (FilterDefinition) dlsResults.get(0);
                        filter.getField().afterLoad();
                        filter.afterLoad();
                        dlsFilters.add(filter);
                    }
                } finally {
                    session.close();
                }
            }

            analysisDefinition.getFilterDefinitions().addAll(dlsFilters);

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

    public VerticalDataResults getVerticalDataResults(WSCombinedVerticalListDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) {
        SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
        insightRequestMetadata.setOptimized(analysisDefinition.isOptimized());
        List<DataResults> list = new ArrayList<DataResults>();
        for (WSAnalysisDefinition analysis : analysisDefinition.getReports()) {
            analysis.setFilterDefinitions(analysisDefinition.getFilterDefinitions());
            DataResults dataResults = list(analysis, insightRequestMetadata);
            list.add(dataResults);
        }
        VerticalDataResults verticalDataResults = new VerticalDataResults();
        verticalDataResults.setMap(list);
        return verticalDataResults;
    }

    public DataResults list(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) {
        SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
        EIConnection conn = Database.instance().getConnection();
        try {
            Feed feed = FeedRegistry.instance().getFeed(analysisDefinition.getDataFeedID(), conn);
            if (insightRequestMetadata == null) {
                insightRequestMetadata = new InsightRequestMetadata();
            }
            insightRequestMetadata.setJoinOverrides(analysisDefinition.getJoinOverrides());
            insightRequestMetadata.setOptimized(analysisDefinition.isOptimized());
            DataResults results;

            for (FilterDefinition filter : analysisDefinition.getFilterDefinitions()) {
                if (filter instanceof AnalysisItemFilterDefinition) {
                    AnalysisItemFilterDefinition analysisItemFilterDefinition = (AnalysisItemFilterDefinition) filter;
                    Map<String, AnalysisItem> structure = analysisDefinition.createStructure();
                    Map<String, AnalysisItem> structureCopy = new HashMap<String, AnalysisItem>(structure);
                    for (Map.Entry<String, AnalysisItem> entry : structureCopy.entrySet()) {
                        if (entry.getValue().equals(filter.getField())) {
                            structure.put(entry.getKey(), analysisItemFilterDefinition.getTargetItem());
                        }
                    }
                    analysisDefinition.populateFromReportStructure(structure);
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
            timeshift(validQueryItems, filters, feed, insightRequestMetadata);
            DataSet dataSet = getDataSet(feed, validQueryItems, filters, insightRequestMetadata, feed.getFields(), conn);
            Pipeline pipeline = new StandardReportPipeline();
            pipeline.setup(analysisDefinition, feed, insightRequestMetadata);
            results = pipeline.toList(dataSet);
            DataSourceInfo dataSourceInfo = feed.getDataSourceInfo();
            if (dataSet.getLastTime() == null) {
                dataSet.setLastTime(new Date());
            }
            dataSourceInfo.setLastDataTime(feed.createSourceInfo(conn).getLastDataTime());
            results.setDataSourceInfo(dataSourceInfo);
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
