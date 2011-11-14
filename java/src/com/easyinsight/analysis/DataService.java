package com.easyinsight.analysis;

import com.easyinsight.analysis.definitions.WSCombinedVerticalListDefinition;
import com.easyinsight.analysis.definitions.WSKPIDefinition;
import com.easyinsight.analysis.definitions.WSVerticalListDefinition;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.etl.LookupTable;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.Roles;
import com.easyinsight.pipeline.StandardReportPipeline;
import org.hibernate.Session;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            timeshift(Arrays.asList(analysisItem), new ArrayList<FilterDefinition>(), feed);
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
            feedMetadata.setDataSourceInfo(feed.createSourceInfo(conn));
            feedMetadata.setDataSourceAdmin(SecurityUtil.getRole(SecurityUtil.getUserID(false), feedID) == Roles.OWNER);
            feedMetadata.setCustomJoinsAllowed(feed.getDataSource().customJoinsAllowed(conn));
            feedMetadata.setDataSourceType(feed.getDataSource().getFeedType().getType());
            List<LookupTable> lookupTables = new ArrayList<LookupTable>();
            for (AnalysisItem field : feedItems) {
                if (field.getLookupTableID() != null && field.getLookupTableID() > 0) {
                    lookupTables.add(new FeedService().getLookupTable(field.getLookupTableID(), conn));
                }
            }
            WSListDefinition tempList = new WSListDefinition();
            tempList.setDataFeedID(feedID);
            tempList.setColumns(new ArrayList<AnalysisItem>());
            feedMetadata.setSuggestions(new AnalysisService().generatePossibleIntentions(tempList, conn));
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

    private static DataSet retrieveDataSet(Feed feed, Set<AnalysisItem> validQueryItems, Collection<FilterDefinition> filters,
                                      InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> fields, EIConnection conn) {
        return feed.getAggregateDataSet(validQueryItems, filters, insightRequestMetadata, fields, false, conn);
    }

    public static List<DataSet> getEmbeddedVerticalDataSets(WSCombinedVerticalListDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata, EIConnection conn) {
        try {
            insightRequestMetadata.setOptimized(analysisDefinition.isOptimized());
            List<DataSet> list = new ArrayList<DataSet>();

            for (WSAnalysisDefinition analysis : analysisDefinition.getReports()) {
                List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
                filters.addAll(analysisDefinition.getFilterDefinitions());
                filters.addAll(analysis.getFilterDefinitions());
                analysis.setFilterDefinitions(filters);
                list.add(listDataSet(analysis, insightRequestMetadata, conn));
            }
            return list;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public EmbeddedTrendDataResults getEmbeddedTrendDataResults(long reportID, long dataSourceID, List<FilterDefinition> customFilters, InsightRequestMetadata insightRequestMetadata) {
        EIConnection conn = Database.instance().getConnection();
        try {
            WSKPIDefinition analysisDefinition = (WSKPIDefinition) new AnalysisStorage().getAnalysisDefinition(reportID);
            RollingFilterDefinition reportFilter = null;
            for (FilterDefinition customFilter : customFilters) {
                if (analysisDefinition.getFilterName().equals(customFilter.getFilterName())) {
                    reportFilter = (RollingFilterDefinition) customFilter;
                }
            }
            if (reportFilter == null) {
                for (FilterDefinition customFilter : customFilters) {
                    if (customFilter instanceof RollingFilterDefinition) {
                        if (analysisDefinition.getFilterName().equals(customFilter.getField().qualifiedName()) ||
                                analysisDefinition.getFilterName().equals(customFilter.getField().toDisplay())) {
                            reportFilter = (RollingFilterDefinition) customFilter;
                        }
                    }
                }
            }
            for (AnalysisItem analysisItem : analysisDefinition.getMeasures()) {
                if (analysisItem.getReportFieldExtension() != null) {
                    TrendReportFieldExtension trendReportFieldExtension = (TrendReportFieldExtension) analysisItem.getReportFieldExtension();
                    AnalysisDateDimension dateDimension = (AnalysisDateDimension) trendReportFieldExtension.getDate();
                    if (dateDimension != null) {
                        RollingFilterDefinition rollingFilterDefinition = new RollingFilterDefinition();
                        rollingFilterDefinition.setField(dateDimension);
                        rollingFilterDefinition.setInterval(reportFilter.getInterval());
                        if (reportFilter.getInterval() == MaterializedRollingFilterDefinition.CUSTOM) {
                            rollingFilterDefinition.setCustomBeforeOrAfter(reportFilter.getCustomBeforeOrAfter());
                            rollingFilterDefinition.setCustomIntervalAmount(reportFilter.getCustomIntervalAmount());
                            rollingFilterDefinition.setCustomIntervalType(reportFilter.getCustomIntervalType());
                        }
                        analysisItem.getFilters().add(rollingFilterDefinition);
                    }
                }
            }
            ReportRetrieval reportRetrievalNow = ReportRetrieval.reportView(insightRequestMetadata, analysisDefinition, conn, customFilters, null);
            DataSet nowSet = reportRetrievalNow.getPipeline().toDataSet(reportRetrievalNow.getDataSet());
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, -analysisDefinition.getDayWindow());
            insightRequestMetadata.setNow(cal.getTime());
            ReportRetrieval reportRetrievalPast = ReportRetrieval.reportView(insightRequestMetadata, analysisDefinition, conn, customFilters, null);
            DataSet pastSet = reportRetrievalPast.getPipeline().toDataSet(reportRetrievalPast.getDataSet());
            List<TrendOutcome> trendOutcomes = new Trend().calculateTrends(analysisDefinition, nowSet, pastSet);
            EmbeddedTrendDataResults trendDataResults = new EmbeddedTrendDataResults();
            trendDataResults.setTrendOutcomes(trendOutcomes);
            trendDataResults.setDataSourceInfo(reportRetrievalNow.getDataSourceInfo());
            trendDataResults.setDefinition(analysisDefinition);
            return trendDataResults;
        } catch (ReportException dae) {
            EmbeddedTrendDataResults embeddedDataResults = new EmbeddedTrendDataResults();
            embeddedDataResults.setReportFault(dae.getReportFault());
            return embeddedDataResults;
        } catch (Throwable e) {
            LogClass.error(e.getMessage() + " on running report " + reportID, e);
            EmbeddedTrendDataResults embeddedDataResults = new EmbeddedTrendDataResults();
            embeddedDataResults.setReportFault(new ServerError(e.getMessage()));
            return embeddedDataResults;
        } finally {
            Database.closeConnection(conn);
        }
    }

    public EmbeddedCrosstabDataResults getEmbeddedCrosstabResults(long reportID, long dataSourceID, List<FilterDefinition> customFilters, InsightRequestMetadata insightRequestMetadata) {
        SecurityUtil.authorizeInsight(reportID);
        EIConnection conn = Database.instance().getConnection();
        try {
            WSCrosstabDefinition crosstabReport = (WSCrosstabDefinition) new AnalysisStorage().getAnalysisDefinition(reportID);
            ReportRetrieval reportRetrieval = ReportRetrieval.reportView(insightRequestMetadata, crosstabReport, conn, customFilters, null);
            Crosstab crosstab = new Crosstab();
            crosstab.crosstab(crosstabReport, reportRetrieval.getPipeline().toDataSet(reportRetrieval.getDataSet()));
            CrosstabValue[][] values = crosstab.toTable(crosstabReport);

            List<CrosstabMapWrapper> resultData = new ArrayList<CrosstabMapWrapper>();
            for (int j = 0; j < (crosstab.getRowSections().size() + crosstabReport.getColumns().size()) + 2; j++) {
                Map<String, CrosstabValue> resultMap = new HashMap<String, CrosstabValue>();
                for (int i = 0; i < (crosstab.getColumnSections().size() + crosstabReport.getRows().size() + 1); i++) {
                    CrosstabValue crosstabValue = values[j][i];
                    if (crosstabValue == null) {

                    } else {
                        resultMap.put(String.valueOf(i), crosstabValue);
                    }
                }
                CrosstabMapWrapper crosstabMapWrapper = new CrosstabMapWrapper();
                crosstabMapWrapper.setMap(resultMap);
                resultData.add(crosstabMapWrapper);
            }
            EmbeddedCrosstabDataResults crossTabDataResults = new EmbeddedCrosstabDataResults();
            crossTabDataResults.setDataSet(resultData);
            crossTabDataResults.setDefinition(crosstabReport);
            crossTabDataResults.setColumnCount(crosstab.getColumnSections().size() + crosstabReport.getRows().size() + 1);
            crossTabDataResults.setDataSourceInfo(reportRetrieval.getDataSourceInfo());
            return crossTabDataResults;
        } catch (ReportException re) {
            EmbeddedCrosstabDataResults results = new EmbeddedCrosstabDataResults();
            results.setReportFault(re.getReportFault());
            return results;
        } catch (Exception e) {
            LogClass.error(e);
            EmbeddedCrosstabDataResults results = new EmbeddedCrosstabDataResults();
            results.setReportFault(new ServerError(e.getMessage()));
            return results;
        } finally {
            Database.closeConnection(conn);
        }
    }

    public EmbeddedVerticalResults getEmbeddedVerticalDataResults(long reportID, long dataSourceID, List<FilterDefinition> customFilters, InsightRequestMetadata insightRequestMetadata) {
        try {
            SecurityUtil.authorizeInsight(reportID);
            WSCombinedVerticalListDefinition analysisDefinition = (WSCombinedVerticalListDefinition) new AnalysisStorage().getAnalysisDefinition(reportID);
            insightRequestMetadata.setOptimized(analysisDefinition.isOptimized());
            List<EmbeddedResults> list = new ArrayList<EmbeddedResults>();

            analysisDefinition.setFilterDefinitions(customFilters);
            FilterDefinition[] labelFilters = new FilterDefinition[analysisDefinition.getReports().size()];
            int i = 0;
            for (WSAnalysisDefinition analysis : analysisDefinition.getReports()) {
                WSVerticalListDefinition verticalListDefinition = (WSVerticalListDefinition) analysis;
                List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
                filters.addAll(customFilters);
                filters.addAll(analysis.getFilterDefinitions());
                if (verticalListDefinition.getPatternName() != null && !"".equals(verticalListDefinition.getPatternName())) {
                    for (FilterDefinition filter : filters) {
                        System.out.println("checking " + verticalListDefinition.getPatternName() + " against " + filter.getFilterName());
                        if (verticalListDefinition.getPatternName().equals(filter.getFilterName())) {
                            labelFilters[i] = filter;
                        }
                    }
                }
                list.add(getEmbeddedResults(analysis.getAnalysisID(), dataSourceID, filters, insightRequestMetadata, null));
                i++;
            }
            EmbeddedVerticalResults verticalDataResults = new EmbeddedVerticalResults();
            verticalDataResults.setList(list);
            verticalDataResults.setReport(analysisDefinition);
            verticalDataResults.getAdditionalProperties().put("labelFilters", labelFilters);
            return verticalDataResults;
        } catch (ReportException re) {
            EmbeddedVerticalResults results = new EmbeddedVerticalResults();
            results.setReportFault(re.getReportFault());
            return results;
        } catch (Exception e) {
            LogClass.error(e);
            EmbeddedVerticalResults results = new EmbeddedVerticalResults();
            results.setReportFault(new ServerError(e.getMessage()));
            return results;
        }
    }

    private EmbeddedResults getEmbeddedResultsForReport(WSAnalysisDefinition analysisDefinition, List<FilterDefinition> customFilters,
                                                        InsightRequestMetadata insightRequestMetadata, List<FilterDefinition> drillThroughFilters, EIConnection conn) throws Exception {
        ReportRetrieval reportRetrieval = ReportRetrieval.reportView(insightRequestMetadata, analysisDefinition, conn, customFilters, drillThroughFilters);
        DataResults results = reportRetrieval.getPipeline().toList(reportRetrieval.getDataSet());
        EmbeddedResults embeddedResults = results.toEmbeddedResults();
        embeddedResults.setDataSourceInfo(reportRetrieval.getDataSourceInfo());
        embeddedResults.setDefinition(analysisDefinition);
        return embeddedResults;
    }

    public static List<FilterDefinition> addDLSFilters(long dataSourceID, EIConnection conn) throws SQLException {
        if (SecurityUtil.getUserID(false) != 0) {
            PreparedStatement dlsStmt = conn.prepareStatement("SELECT user_dls_to_filter.FILTER_ID FROM user_dls_to_filter, user_dls, dls where " +
                    "user_dls_to_filter.user_dls_id = user_dls.user_dls_id and user_dls.dls_id = dls.dls_id and dls.data_source_id = ? and " +
                    "user_dls.user_id = ?");
            dlsStmt.setLong(1, dataSourceID);
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
            dlsStmt.close();
            return dlsFilters;
        }
        return new ArrayList<FilterDefinition>();
    }

    public EmbeddedResults getEmbeddedResults(long reportID, long dataSourceID, List<FilterDefinition> customFilters,
                                              InsightRequestMetadata insightRequestMetadata, @Nullable List<FilterDefinition> drillThroughFilters) {
        SecurityUtil.authorizeInsight(reportID);

        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            WSAnalysisDefinition analysisDefinition = new AnalysisStorage().getAnalysisDefinition(reportID, conn);
            analysisDefinition.setDataFeedID(dataSourceID);
            if (analysisDefinition == null) {
                return null;
            }
            EmbeddedResults results = getEmbeddedResultsForReport(analysisDefinition, customFilters, insightRequestMetadata, drillThroughFilters, conn);
            conn.commit();
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

    public static DataSet listDataSet(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata, EIConnection conn) {
        ReportRetrieval reportRetrieval;
        try {
            reportRetrieval = ReportRetrieval.reportEditor(insightRequestMetadata, analysisDefinition, conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reportRetrieval.getPipeline().toDataSet(reportRetrieval.getDataSet());
    }

    public static DataResults list(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata,
                                   EIConnection conn) {
        try {
            ReportRetrieval reportRetrieval = ReportRetrieval.reportEditor(insightRequestMetadata, analysisDefinition, conn);
            DataResults results = reportRetrieval.getPipeline().toList(reportRetrieval.getDataSet());
            results.setDataSourceInfo(reportRetrieval.getDataSourceInfo());
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

    private static void timeshift(Collection<AnalysisItem> items, Collection<FilterDefinition> filters, Feed dataSource) {
        for (AnalysisItem item : items) {
            if (item.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                AnalysisDateDimension dateDim = (AnalysisDateDimension) item;
                boolean dateTime = dataSource.getDataSource().checkDateTime(item.toOriginalDisplayName(), item.getKey());
                dateDim.setTimeshift(dateTime);
            }
        }
        for (AnalysisItem item : items) {
            item.timeshift(dataSource, filters);
        }
        for (FilterDefinition filter : filters) {
            filter.timeshift(dataSource, filters);
        }
    }

    public static TrendDataResults getTrendDataResults(WSKPIDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata, EIConnection conn) throws SQLException {
        RollingFilterDefinition reportFilter = null;
        for (FilterDefinition customFilter : analysisDefinition.getFilterDefinitions()) {
            if (analysisDefinition.getFilterName().equals(customFilter.getFilterName())) {
                reportFilter = (RollingFilterDefinition) customFilter;
            }
        }
        if (reportFilter == null) {
            for (FilterDefinition customFilter : analysisDefinition.getFilterDefinitions()) {
                if (customFilter instanceof RollingFilterDefinition) {
                    if (analysisDefinition.getFilterName().equals(customFilter.getField().qualifiedName()) ||
                            analysisDefinition.getFilterName().equals(customFilter.getField().toDisplay())) {
                        reportFilter = (RollingFilterDefinition) customFilter;
                    }
                }
            }
        }
        for (AnalysisItem analysisItem : analysisDefinition.getMeasures()) {
            if (analysisItem.getReportFieldExtension() != null) {
                TrendReportFieldExtension trendReportFieldExtension = (TrendReportFieldExtension) analysisItem.getReportFieldExtension();
                if (trendReportFieldExtension.getDate() != null) {
                    AnalysisDateDimension dateDimension = (AnalysisDateDimension) trendReportFieldExtension.getDate();
                    RollingFilterDefinition rollingFilterDefinition = new RollingFilterDefinition();
                    rollingFilterDefinition.setField(dateDimension);
                    rollingFilterDefinition.setInterval(reportFilter.getInterval());
                    if (reportFilter.getInterval() == MaterializedRollingFilterDefinition.CUSTOM) {
                        rollingFilterDefinition.setCustomBeforeOrAfter(reportFilter.getCustomBeforeOrAfter());
                        rollingFilterDefinition.setCustomIntervalAmount(reportFilter.getCustomIntervalAmount());
                        rollingFilterDefinition.setCustomIntervalType(reportFilter.getCustomIntervalType());
                    }
                    analysisItem.getFilters().add(rollingFilterDefinition);
                }
            }
        }
        ReportRetrieval reportRetrievalNow = ReportRetrieval.reportEditor(insightRequestMetadata, analysisDefinition, conn);
        DataSet nowSet = reportRetrievalNow.getPipeline().toDataSet(reportRetrievalNow.getDataSet());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -analysisDefinition.getDayWindow());
        insightRequestMetadata.setNow(cal.getTime());
        ReportRetrieval reportRetrievalPast = ReportRetrieval.reportEditor(insightRequestMetadata, analysisDefinition, conn);
        DataSet pastSet = reportRetrievalPast.getPipeline().toDataSet(reportRetrievalPast.getDataSet());
        List<TrendOutcome> trendOutcomes = new Trend().calculateTrends(analysisDefinition, nowSet, pastSet);
        TrendDataResults trendDataResults = new TrendDataResults();
        trendDataResults.setTrendOutcomes(trendOutcomes);
        trendDataResults.setSuggestions(new AnalysisService().generatePossibleIntentions(analysisDefinition, conn));
        trendDataResults.setDataSourceInfo(reportRetrievalNow.getDataSourceInfo());
        return trendDataResults;
    }

    public TrendDataResults getTrendDataResults(WSKPIDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) {
        EIConnection conn = Database.instance().getConnection();
        try {
            RollingFilterDefinition reportFilter = null;
            for (FilterDefinition customFilter : analysisDefinition.getFilterDefinitions()) {
                if (analysisDefinition.getFilterName().equals(customFilter.getFilterName())) {
                    reportFilter = (RollingFilterDefinition) customFilter;
                }
            }
            if (reportFilter == null) {
                for (FilterDefinition customFilter : analysisDefinition.getFilterDefinitions()) {
                    if (customFilter instanceof RollingFilterDefinition) {
                        if (analysisDefinition.getFilterName().equals(customFilter.getField().qualifiedName()) ||
                                analysisDefinition.getFilterName().equals(customFilter.getField().toDisplay())) {
                            reportFilter = (RollingFilterDefinition) customFilter;
                        }
                    }
                }
            }
            for (AnalysisItem analysisItem : analysisDefinition.getMeasures()) {
                if (analysisItem.getReportFieldExtension() != null) {
                    TrendReportFieldExtension trendReportFieldExtension = (TrendReportFieldExtension) analysisItem.getReportFieldExtension();
                    if (trendReportFieldExtension.getDate() != null) {
                        AnalysisDateDimension dateDimension = (AnalysisDateDimension) trendReportFieldExtension.getDate();
                        RollingFilterDefinition rollingFilterDefinition = new RollingFilterDefinition();
                        rollingFilterDefinition.setField(dateDimension);
                        rollingFilterDefinition.setInterval(reportFilter.getInterval());
                        if (reportFilter.getInterval() == MaterializedRollingFilterDefinition.CUSTOM) {
                            rollingFilterDefinition.setCustomBeforeOrAfter(reportFilter.getCustomBeforeOrAfter());
                            rollingFilterDefinition.setCustomIntervalAmount(reportFilter.getCustomIntervalAmount());
                            rollingFilterDefinition.setCustomIntervalType(reportFilter.getCustomIntervalType());
                        }
                        analysisItem.getFilters().add(rollingFilterDefinition);
                    }
                }
            }
            ReportRetrieval reportRetrievalNow = ReportRetrieval.reportEditor(insightRequestMetadata, analysisDefinition, conn);
            DataSet nowSet = reportRetrievalNow.getPipeline().toDataSet(reportRetrievalNow.getDataSet());
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, -analysisDefinition.getDayWindow());
            insightRequestMetadata.setNow(cal.getTime());
            ReportRetrieval reportRetrievalPast = ReportRetrieval.reportEditor(insightRequestMetadata, analysisDefinition, conn);
            DataSet pastSet = reportRetrievalPast.getPipeline().toDataSet(reportRetrievalPast.getDataSet());
            List<TrendOutcome> trendOutcomes = new Trend().calculateTrends(analysisDefinition, nowSet, pastSet);
            TrendDataResults trendDataResults = new TrendDataResults();
            trendDataResults.setTrendOutcomes(trendOutcomes);
            trendDataResults.setSuggestions(new AnalysisService().generatePossibleIntentions(analysisDefinition, conn));
            trendDataResults.setDataSourceInfo(reportRetrievalNow.getDataSourceInfo());
            return trendDataResults;
        } catch (ReportException dae) {
            TrendDataResults embeddedDataResults = new TrendDataResults();
            embeddedDataResults.setReportFault(dae.getReportFault());
            return embeddedDataResults;
        } catch (Throwable e) {
            LogClass.error(e.getMessage() + " on running report " + analysisDefinition.getAnalysisID(), e);
            TrendDataResults embeddedDataResults = new TrendDataResults();
            embeddedDataResults.setReportFault(new ServerError(e.getMessage()));
            return embeddedDataResults;
        } finally {
            Database.closeConnection(conn);
        }
    }



    public CrossTabDataResults getCrosstabDataResults(WSCrosstabDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) {
        EIConnection conn = Database.instance().getConnection();
        try {
            ReportRetrieval reportRetrieval = ReportRetrieval.reportEditor(insightRequestMetadata, analysisDefinition, conn);
            Crosstab crosstab = new Crosstab();
            crosstab.crosstab(analysisDefinition, reportRetrieval.getPipeline().toDataSet(reportRetrieval.getDataSet()));
            CrosstabValue[][] values = crosstab.toTable(analysisDefinition);

            List<CrosstabMapWrapper> resultData = new ArrayList<CrosstabMapWrapper>();
            for (int j = 0; j < (crosstab.getRowSections().size() + analysisDefinition.getColumns().size()) + 2; j++) {
                Map<String, CrosstabValue> resultMap = new HashMap<String, CrosstabValue>();
                for (int i = 0; i < (crosstab.getColumnSections().size() + analysisDefinition.getRows().size() + 1); i++) {
                    CrosstabValue crosstabValue = values[j][i];
                    if (crosstabValue == null) {

                    } else {
                        resultMap.put(String.valueOf(i), crosstabValue);
                    }
                }
                CrosstabMapWrapper crosstabMapWrapper = new CrosstabMapWrapper();
                crosstabMapWrapper.setMap(resultMap);
                resultData.add(crosstabMapWrapper);
            }
            CrossTabDataResults crossTabDataResults = new CrossTabDataResults();
            crossTabDataResults.setDataSet(resultData);
            crossTabDataResults.setColumnCount(crosstab.getColumnSections().size() + analysisDefinition.getRows().size() + 1);
            crossTabDataResults.setSuggestions(new AnalysisService().generatePossibleIntentions(analysisDefinition, conn));
            crossTabDataResults.setDataSourceInfo(reportRetrieval.getDataSourceInfo());
            return crossTabDataResults;
        } catch (ReportException dae) {
            CrossTabDataResults embeddedDataResults = new CrossTabDataResults();
            embeddedDataResults.setReportFault(dae.getReportFault());
            return embeddedDataResults;
        } catch (Throwable e) {
            LogClass.error(e.getMessage() + " on running report " + analysisDefinition.getAnalysisID(), e);
            CrossTabDataResults embeddedDataResults = new CrossTabDataResults();
            embeddedDataResults.setReportFault(new ServerError(e.getMessage()));
            return embeddedDataResults;
        } finally {
            Database.closeConnection(conn);
        }
    }

    public VerticalDataResults getVerticalDataResults(WSCombinedVerticalListDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) {
        try {
            SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
            insightRequestMetadata.setOptimized(analysisDefinition.isOptimized());
            List<DataResults> list = new ArrayList<DataResults>();
            for (WSAnalysisDefinition analysis : analysisDefinition.getReports()) {
                List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
                filters.addAll(analysisDefinition.getFilterDefinitions());
                filters.addAll(analysis.getFilterDefinitions());
                analysis.setFilterDefinitions(filters);
                DataResults dataResults = list(analysis, insightRequestMetadata);
                list.add(dataResults);
            }
            VerticalDataResults verticalDataResults = new VerticalDataResults();
            verticalDataResults.setMap(list);
            return verticalDataResults;
        } catch (ReportException dae) {
            VerticalDataResults embeddedDataResults = new VerticalDataResults();
            embeddedDataResults.setReportFault(dae.getReportFault());
            return embeddedDataResults;
        } catch (Throwable e) {
            LogClass.error(e.getMessage() + " on running report " + analysisDefinition.getAnalysisID(), e);
            VerticalDataResults embeddedDataResults = new VerticalDataResults();
            embeddedDataResults.setReportFault(new ServerError(e.getMessage()));
            return embeddedDataResults;
        }
    }

    public DataResults list(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) {
        SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
        EIConnection conn = Database.instance().getConnection();
        try {
            ReportRetrieval reportRetrieval = ReportRetrieval.reportEditor(insightRequestMetadata, analysisDefinition, conn);
            DataResults results = reportRetrieval.getPipeline().toList(reportRetrieval.getDataSet());
            results.setDataSourceInfo(reportRetrieval.getDataSourceInfo());
            results.setSuggestions(new AnalysisService().generatePossibleIntentions(analysisDefinition, conn));
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

    private static class ReportRetrieval {

        private InsightRequestMetadata insightRequestMetadata;
        private WSAnalysisDefinition analysisDefinition;
        private EIConnection conn;
        private DataSet dataSet;
        private StandardReportPipeline pipeline;
        private DataSourceInfo dataSourceInfo;
        private Feed feed;

        private ReportRetrieval(InsightRequestMetadata insightRequestMetadata, WSAnalysisDefinition analysisDefinition, EIConnection conn) throws SQLException {
            this.insightRequestMetadata = insightRequestMetadata;
            this.analysisDefinition = analysisDefinition;
            this.conn = conn;

        }

        private static ReportRetrieval reportEditor(InsightRequestMetadata insightRequestMetadata, WSAnalysisDefinition analysisDefinition, EIConnection conn) throws SQLException {
            return new ReportRetrieval(insightRequestMetadata, analysisDefinition, conn).toPipeline();
        }

        private static ReportRetrieval reportView(InsightRequestMetadata insightRequestMetadata, WSAnalysisDefinition analysisDefinition, EIConnection conn,
                                                  @Nullable List<FilterDefinition> customFilters, @Nullable List<FilterDefinition> drillThroughFilters) throws SQLException {
            if (customFilters != null) {
                analysisDefinition.setFilterDefinitions(customFilters);
            }
            if (drillThroughFilters != null) {
                analysisDefinition.applyFilters(drillThroughFilters);
            }
            return new ReportRetrieval(insightRequestMetadata, analysisDefinition, conn).toPipeline();
        }

        public StandardReportPipeline getPipeline() {
            return pipeline;
        }

        public DataSet getDataSet() {
            return dataSet;
        }

        public Feed getFeed() {
            return feed;
        }

        public DataSourceInfo getDataSourceInfo() {
            return dataSourceInfo;
        }

        private ReportRetrieval toPipeline() throws SQLException {
            feed = FeedRegistry.instance().getFeed(analysisDefinition.getDataFeedID(), conn);
            if (insightRequestMetadata == null) {
                insightRequestMetadata = new InsightRequestMetadata();
            }
            insightRequestMetadata.setJoinOverrides(analysisDefinition.getJoinOverrides());
            insightRequestMetadata.setOptimized(analysisDefinition.isOptimized());
            insightRequestMetadata.setTraverseAllJoins(analysisDefinition.isFullJoins());

            /*
            if (insightRequestMetadata.getHierarchyOverrides() != null) {
                for (AnalysisItemOverride hierarchyOverride : insightRequestMetadata.getHierarchyOverrides()) {
                    hierarchyOverride.apply(analysisDefinition.getAllAnalysisItems());
                }
            }
             */

            List<FilterDefinition> dlsFilters = addDLSFilters(analysisDefinition.getDataFeedID(), conn);
            analysisDefinition.getFilterDefinitions().addAll(dlsFilters);

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
            if (analysisDefinition.getMarmotScript() != null) {
                StringTokenizer toker = new StringTokenizer(analysisDefinition.getMarmotScript(), "\r\n");
                while (toker.hasMoreTokens()) {
                    String line = toker.nextToken();
                    new ReportCalculation(line).apply(analysisDefinition, allFields, feed, conn, dlsFilters);
                }
            }
            if (feed.getDataSource().getMarmotScript() != null) {
                StringTokenizer toker = new StringTokenizer(feed.getDataSource().getMarmotScript(), "\r\n");
                while (toker.hasMoreTokens()) {
                    String line = toker.nextToken();
                    new ReportCalculation(line).apply(analysisDefinition, allFields, feed, conn, dlsFilters);
                }
            }
            //new ReportCalculation("replacefields(\"Procedures\", \"*PT/OT*\")").apply(analysisDefinition, allFields);
            //new ReportCalculation("equals([Group Name], \"Atlantic Orthopedics\", addfield(\"Charges\"))").apply(analysisDefinition, allFields, feed, conn, dlsFilters);
            /*new ReportCalculation("copyfields(\"2009 Procedures\", \"2009 !0#\", \"9*-PT/OT*\")").apply(analysisDefinition, allFields);
            new ReportCalculation("copyfields(\"2010 Procedures\", \"2010 !0#\", \"9*-PT/OT*\")").apply(analysisDefinition, allFields);
            new ReportCalculation("replacecalculation(\"Procedures\", \"9*-*PT/OT*\")").apply(analysisDefinition, allFields);*/
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
            timeshift(validQueryItems, filters, feed);
            dataSet = retrieveDataSet(feed, validQueryItems, filters, insightRequestMetadata, feed.getFields(), conn);
            pipeline = new StandardReportPipeline();
            pipeline.setup(analysisDefinition, feed, insightRequestMetadata, allFields);
            dataSourceInfo = feed.createSourceInfo(conn);
            if (dataSet.getLastTime() == null) {
                dataSet.setLastTime(new Date());
            }
            return this;
        }
    }
}
