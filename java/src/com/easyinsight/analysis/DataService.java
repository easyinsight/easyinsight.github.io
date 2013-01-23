package com.easyinsight.analysis;

import com.easyinsight.analysis.definitions.*;
import com.easyinsight.calculations.FunctionException;
import com.easyinsight.core.Key;
import com.easyinsight.core.XMLMetadata;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.etl.LookupTable;
import com.easyinsight.export.TreeData;
import com.easyinsight.intention.IntentionSuggestion;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.Roles;
import com.easyinsight.pipeline.StandardReportPipeline;
import flex.messaging.FlexContext;
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
        return getAnalysisItemMetadata(feedID, analysisItem, utfOffset, reportID, dashboardID, null);
    }

    public AnalysisItemResultMetadata getAnalysisItemMetadata(long feedID, AnalysisItem analysisItem, int utfOffset, long reportID, long dashboardID,
                                                              @Nullable WSAnalysisDefinition report) {
        return getAnalysisItemMetadata(feedID, analysisItem, utfOffset, reportID, dashboardID, report, null, null);
    }

    public AnalysisItemResultMetadata getAnalysisItemMetadata(long feedID, AnalysisItem analysisItem, int utfOffset, long reportID, long dashboardID,
                                                              @Nullable WSAnalysisDefinition report, List<FilterDefinition> additionalFilters, FilterDefinition requester) {
        EIConnection conn = Database.instance().getConnection();
        try {
            if (reportID > 0) {
                SecurityUtil.authorizeInsight(reportID);
            } else if (dashboardID > 0) {
                SecurityUtil.authorizeDashboard(dashboardID);
            } else {
                SecurityUtil.authorizeFeedAccess(feedID);
            }
            if (analysisItem == null) {
                LogClass.error("Received null analysis item from feed " + feedID);
                return null;
            }
            Feed feed = feedRegistry.getFeed(feedID, conn);
            InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
            insightRequestMetadata.setUtcOffset(utfOffset);
            if (report != null) {
                insightRequestMetadata.setJoinOverrides(report.getJoinOverrides());
                insightRequestMetadata.setTraverseAllJoins(report.isFullJoins());

                if (requester.getFieldChoiceFilterLabel() != null && !"".equals(requester.getFieldChoiceFilterLabel())) {
                    String label = requester.getFieldChoiceFilterLabel();
                    for (FilterDefinition testFilter : report.getFilterDefinitions()) {
                        if (label.equals(testFilter.getFilterName()) && testFilter.type() == FilterDefinition.ANALYSIS_ITEM) {
                            AnalysisItemFilterDefinition analysisItemFilterDefinition = (AnalysisItemFilterDefinition) testFilter;
                            analysisItem = analysisItemFilterDefinition.getTargetItem();
                        }
                    }
                }
            }
            timeshift(Arrays.asList(analysisItem), new ArrayList<FilterDefinition>(), feed);
            return feed.getMetadata(analysisItem, insightRequestMetadata, conn, report, additionalFilters, requester);
        } catch (ReportException re) {
            AnalysisItemResultMetadata metadata = new AnalysisItemResultMetadata();
            metadata.setReportFault(re.getReportFault());
            return metadata;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public FeedMetadata getFeedMetadata(long feedID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            SecurityUtil.authorizeFeedAccess(feedID);
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
                    LookupTable lookupTable = new FeedService().getLookupTable(field.getLookupTableID(), conn);
                    if (lookupTable != null) {
                        if (lookupTable.getDataSourceID() != feedID) {
                            AnalysisItem sourceItem = lookupTable.getSourceField();

                            for (AnalysisItem dataSourceField : feedMetadata.getFields()) {
                                Key key = dataSourceField.getKey();
                                if (key.matchesOrContains(sourceItem.getKey())) {
                                    lookupTable.setSourceField(dataSourceField);
                                    break;
                                }
                            }
                        }
                        lookupTables.add(lookupTable);
                    }
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
            SecurityUtil.authorizeInsight(reportID);
            LogClass.info(SecurityUtil.getUserID(false) + " retrieving " + reportID);
            WSKPIDefinition analysisDefinition = (WSKPIDefinition) new AnalysisStorage().getAnalysisDefinition(reportID);
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
            Map<String, List<AnalysisMeasure>> trendMap = new HashMap<String, List<AnalysisMeasure>>();

            for (AnalysisItem analysisItem : analysisDefinition.getMeasures()) {
                if (analysisItem.getReportFieldExtension() != null && reportFilter != null && analysisItem.getReportFieldExtension() instanceof TrendReportFieldExtension) {
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
                        List<AnalysisMeasure> measures = trendMap.get(dateDimension.qualifiedName());
                        if (measures == null) {
                            measures = new ArrayList<AnalysisMeasure>();
                            trendMap.put(dateDimension.qualifiedName(), measures);
                        }
                        measures.add((AnalysisMeasure) analysisItem);
                    } else {
                        List<AnalysisMeasure> measures = trendMap.get("");
                        if (measures == null) {
                            measures = new ArrayList<AnalysisMeasure>();
                            trendMap.put("", measures);
                        }
                        measures.add((AnalysisMeasure) analysisItem);
                    }
                } else {
                    List<AnalysisMeasure> measures = trendMap.get("");
                    if (measures == null) {
                        measures = new ArrayList<AnalysisMeasure>();
                        trendMap.put("", measures);
                    }
                    measures.add((AnalysisMeasure) analysisItem);
                }
            }
            List<TrendOutcome> trendOutcomes = new ArrayList<TrendOutcome>();
            DataSourceInfo dataSourceInfo = null;
            for (Map.Entry<String, List<AnalysisMeasure>> entry : trendMap.entrySet()) {
                String key = entry.getKey();
                List<AnalysisMeasure> measures = entry.getValue();
                WSListDefinition tempReport = new WSListDefinition();
                List<AnalysisItem> columns = new ArrayList<AnalysisItem>();
                columns.addAll(measures);
                if (analysisDefinition.getGroupings() != null) {
                    columns.addAll(analysisDefinition.getGroupings());
                }
                tempReport.setFilterDefinitions(analysisDefinition.getFilterDefinitions());
                tempReport.setColumns(columns);
                tempReport.setDataFeedID(analysisDefinition.getDataFeedID());
                tempReport.setAddedItems(analysisDefinition.getAddedItems());
                tempReport.setMarmotScript(analysisDefinition.getMarmotScript());
                tempReport.setReportRunMarmotScript(analysisDefinition.getReportRunMarmotScript());
                tempReport.setJoinOverrides(analysisDefinition.getJoinOverrides());
                InsightRequestMetadata metadata = new InsightRequestMetadata();
                metadata.setUtcOffset(insightRequestMetadata.getUtcOffset());
                ReportRetrieval reportRetrievalNow = ReportRetrieval.reportView(metadata, tempReport, conn, customFilters, new ArrayList<FilterDefinition>());
                dataSourceInfo = reportRetrievalNow.getDataSourceInfo();
                DataSet nowSet = reportRetrievalNow.getPipeline().toDataSet(reportRetrievalNow.getDataSet());
                DataSet pastSet;
                if ("".equals(key)) {
                    pastSet = nowSet;
                } else {
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DAY_OF_YEAR, -analysisDefinition.getDayWindow());
                    metadata = new InsightRequestMetadata();
                    metadata.setUtcOffset(insightRequestMetadata.getUtcOffset());
                    metadata.setNow(cal.getTime());
                    ReportRetrieval reportRetrievalPast = ReportRetrieval.reportView(metadata, tempReport, conn, customFilters, new ArrayList<FilterDefinition>());
                    pastSet = reportRetrievalPast.getPipeline().toDataSet(reportRetrievalPast.getDataSet());
                }
                trendOutcomes.addAll(new Trend().calculateTrends(measures, analysisDefinition.getGroupings(), nowSet, pastSet));
            }
            List<TrendOutcome> targetOutcomes = new ArrayList<TrendOutcome>();
            for (AnalysisItem analysisItem : analysisDefinition.getMeasures()) {
                for (TrendOutcome trendOutcome : trendOutcomes) {
                    if (trendOutcome.getMeasure().equals(analysisItem)) {
                        targetOutcomes.add(trendOutcome);
                        break;
                    }
                }
            }
            trendOutcomes = targetOutcomes;
            EmbeddedTrendDataResults trendDataResults = new EmbeddedTrendDataResults();
            trendDataResults.setTrendOutcomes(trendOutcomes);
            trendDataResults.setDataSourceInfo(dataSourceInfo);
            trendDataResults.setDefinition(analysisDefinition);
            return trendDataResults;
        } catch (com.easyinsight.security.SecurityException se) {
            EmbeddedTrendDataResults results = new EmbeddedTrendDataResults();
            results.setReportFault(new ServerError("You don't have access to this report. Check with your administrator about altering access privileges."));
            return results;
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

    public EmbeddedCrosstabDataResults getEmbeddedCrosstabResults(long reportID, long dataSourceID, List<FilterDefinition> customFilters, InsightRequestMetadata insightRequestMetadata,
                                                                  List<FilterDefinition> drillthroughFilters) {
        EIConnection conn = Database.instance().getConnection();
        try {
            SecurityUtil.authorizeInsight(reportID);
            LogClass.info(SecurityUtil.getUserID(false) + " retrieving " + reportID);
            WSCrosstabDefinition crosstabReport = (WSCrosstabDefinition) new AnalysisStorage().getAnalysisDefinition(reportID);
            ReportRetrieval reportRetrieval = ReportRetrieval.reportView(insightRequestMetadata, crosstabReport, conn, customFilters, drillthroughFilters);
            Crosstab crosstab = new Crosstab();
            crosstab.crosstab(crosstabReport, reportRetrieval.getPipeline().toDataSet(reportRetrieval.getDataSet()));
            CrosstabValue[][] values = crosstab.toTable(crosstabReport, insightRequestMetadata, conn);

            List<CrosstabMapWrapper> resultData = new ArrayList<CrosstabMapWrapper>();
            int rowOffset = crosstabReport.getMeasures().size() > 1 ? 3 : 2;
            for (int j = 0; j < (crosstab.getRowSections().size() + crosstabReport.getColumns().size()) + rowOffset; j++) {
                Map<String, CrosstabValue> resultMap = new HashMap<String, CrosstabValue>();
                for (int i = 0; i < ((crosstab.getColumnSections().size() * crosstabReport.getMeasures().size()) + crosstabReport.getRows().size() + 1); i++) {
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
            crossTabDataResults.setColumnCount((crosstab.getColumnSections().size() * crosstabReport.getMeasures().size()) + crosstabReport.getRows().size() + 1);
            crossTabDataResults.setDataSourceInfo(reportRetrieval.getDataSourceInfo());
            return crossTabDataResults;
        } catch (com.easyinsight.security.SecurityException se) {
            EmbeddedCrosstabDataResults results = new EmbeddedCrosstabDataResults();
            results.setReportFault(new ServerError("You don't have access to this report. Check with your administrator about altering access privileges."));
            return results;
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
            LogClass.info(SecurityUtil.getUserID(false) + " retrieving " + reportID);
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
        DataResults results = reportRetrieval.getPipeline().toList(reportRetrieval.getDataSet(), conn, reportRetrieval.aliases);
        analysisDefinition.untweakReport(null);
        EmbeddedResults embeddedResults = results.toEmbeddedResults();
        embeddedResults.setDataSourceInfo(reportRetrieval.getDataSourceInfo());
        embeddedResults.setDefinition(analysisDefinition);
        return embeddedResults;
    }

    public static List<FilterDefinition> addDLSFilters(long dataSourceID, EIConnection conn) throws SQLException {
        if (SecurityUtil.getUserID(false) != 0) {

            List<FilterDefinition> dlsFilters = new ArrayList<FilterDefinition>();
            {
                PreparedStatement dlsStmt = conn.prepareStatement("SELECT user_dls_to_filter.FILTER_ID FROM user_dls_to_filter, user_dls, dls where " +
                                    "user_dls_to_filter.user_dls_id = user_dls.user_dls_id and user_dls.dls_id = dls.dls_id and dls.data_source_id = ? and " +
                                    "user_dls.user_id = ?");
                dlsStmt.setLong(1, dataSourceID);
                dlsStmt.setLong(2, SecurityUtil.getUserID());
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
            }

            {
                PreparedStatement dlsStmt = conn.prepareStatement("SELECT user_dls_to_filter.FILTER_ID FROM user_dls_to_filter, user_dls, dls, composite_node, composite_feed where " +
                        "user_dls_to_filter.user_dls_id = user_dls.user_dls_id and user_dls.dls_id = dls.dls_id and " +
                        "user_dls.user_id = ? and composite_node.data_feed_id = ? and composite_node.composite_feed_id = composite_feed.composite_feed_id and " +
                        "composite_feed.data_feed_id = dls.data_source_id");
                dlsStmt.setLong(1, SecurityUtil.getUserID());
                dlsStmt.setLong(2, dataSourceID);
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
            }
            {
                PreparedStatement dlsStmt = conn.prepareStatement("SELECT user_dls_to_filter.FILTER_ID FROM user_dls_to_filter, user_dls, dls, composite_node, composite_feed where " +
                        "user_dls_to_filter.user_dls_id = user_dls.user_dls_id and user_dls.dls_id = dls.dls_id and " +
                        "user_dls.user_id = ? and composite_node.data_feed_id = dls.data_source_id and composite_node.composite_feed_id = composite_feed.composite_feed_id and " +
                        "composite_feed.data_feed_id = ?");
                dlsStmt.setLong(1, SecurityUtil.getUserID());
                dlsStmt.setLong(2, dataSourceID);
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
            }
            return dlsFilters;
        }
        return new ArrayList<FilterDefinition>();
    }

    public EmbeddedResults getEmbeddedResults(long reportID, long dataSourceID, List<FilterDefinition> customFilters,
                                              InsightRequestMetadata insightRequestMetadata, @Nullable List<FilterDefinition> drillThroughFilters) {
        try {
            UserThreadMutex.mutex().acquire(SecurityUtil.getUserID(false));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        EIConnection conn = Database.instance().getConnection();
        try {
            SecurityUtil.authorizeInsight(reportID);
            LogClass.info(SecurityUtil.getUserID(false) + " retrieving " + reportID);

            conn.setAutoCommit(false);
            WSAnalysisDefinition analysisDefinition = new AnalysisStorage().getAnalysisDefinition(reportID, conn);
            CacheKey cacheKey = null;
            if (analysisDefinition.isCacheable()) {
                List<String> filters = new ArrayList<String>();
                XMLMetadata xmlMetadata = new XMLMetadata();
                xmlMetadata.setConn(conn);
                for (FilterDefinition filter : customFilters) {
                    filters.add(filter.toXML(xmlMetadata).toXML());
                }
                cacheKey = new CacheKey(reportID, filters);
                EmbeddedResults embeddedResults = ReportCache.instance().getResults(dataSourceID, cacheKey);
                if (embeddedResults != null) {
                    LogClass.debug("*** Returning from cache");
                    return embeddedResults;
                }
            }
            if (analysisDefinition == null) {
                return null;
            }
            analysisDefinition.setDataFeedID(dataSourceID);
            EmbeddedResults results = getEmbeddedResultsForReport(analysisDefinition, customFilters, insightRequestMetadata, drillThroughFilters, conn);
            if (cacheKey != null) {
                ReportCache.instance().storeReport(dataSourceID, cacheKey, results);
            }
            conn.commit();
            return results;
        } catch (com.easyinsight.security.SecurityException se) {
            EmbeddedDataResults results = new EmbeddedDataResults();
            results.setReportFault(new ServerError("You don't have access to this report. Check with your administrator about altering access privileges."));
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
            UserThreadMutex.mutex().release(SecurityUtil.getUserID(false));
        }
    }

    public static DataSet listDataSet(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata, EIConnection conn) {
        try {
            UserThreadMutex.mutex().acquire(SecurityUtil.getUserID(false));
        } catch (InterruptedException e) {
            return null;
        }
        try {
            ReportRetrieval reportRetrieval;
            try {
                reportRetrieval = ReportRetrieval.reportEditor(insightRequestMetadata, analysisDefinition, conn);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            DataSet dataSet = reportRetrieval.getPipeline().toDataSet(reportRetrieval.getDataSet());
            if (analysisDefinition.isLogReport()) {
                dataSet.setReportLog(reportRetrieval.getPipeline().toLogString());
            }
            dataSet.setPipelineData(reportRetrieval.getPipeline().getPipelineData());
            return dataSet;
        } finally {
            UserThreadMutex.mutex().release(SecurityUtil.getUserID(false));
        }
    }

    public static ExtendedDataSet extendedListDataSet(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata, EIConnection conn) {
        ReportRetrieval reportRetrieval;
        try {
            reportRetrieval = ReportRetrieval.reportEditor(insightRequestMetadata, analysisDefinition, conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new ExtendedDataSet(reportRetrieval.getPipeline().toDataSet(reportRetrieval.getDataSet()),
                reportRetrieval.getPipeline().getPipelineData(), reportRetrieval.getPipeline().getPipelineData().getAllRequestedItems());
    }

    public static DataResults list(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata,
                                   EIConnection conn) {
        try {
            ReportRetrieval reportRetrieval = ReportRetrieval.reportEditor(insightRequestMetadata, analysisDefinition, conn);
            DataResults results = reportRetrieval.getPipeline().toList(reportRetrieval.getDataSet(), conn, reportRetrieval.aliases);
            results.setDataSourceInfo(reportRetrieval.getDataSourceInfo());
            return results;
        } catch (ReportException dae) {
            throw dae;
            /*ListDataResults embeddedDataResults = new ListDataResults();
            embeddedDataResults.setReportFault(dae.getReportFault());
            return embeddedDataResults;*/
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
                boolean dateTime = !dateDim.isDateOnlyField() && dataSource.getDataSource().checkDateTime(item.toOriginalDisplayName(), item.getKey());
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
        Map<String, List<AnalysisMeasure>> trendMap = new HashMap<String, List<AnalysisMeasure>>();

        for (AnalysisItem analysisItem : analysisDefinition.getMeasures()) {
            if (analysisItem.getReportFieldExtension() != null && reportFilter != null && analysisItem.getReportFieldExtension() instanceof TrendReportFieldExtension) {
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
                    List<AnalysisMeasure> measures = trendMap.get(dateDimension.qualifiedName());
                    if (measures == null) {
                        measures = new ArrayList<AnalysisMeasure>();
                        trendMap.put(dateDimension.qualifiedName(), measures);
                    }
                    measures.add((AnalysisMeasure) analysisItem);
                } else {
                    List<AnalysisMeasure> measures = trendMap.get("");
                    if (measures == null) {
                        measures = new ArrayList<AnalysisMeasure>();
                        trendMap.put("", measures);
                    }
                    measures.add((AnalysisMeasure) analysisItem);
                }
            } else {
                List<AnalysisMeasure> measures = trendMap.get("");
                if (measures == null) {
                    measures = new ArrayList<AnalysisMeasure>();
                    trendMap.put("", measures);
                }
                measures.add((AnalysisMeasure) analysisItem);
            }
        }
        List<TrendOutcome> trendOutcomes = new ArrayList<TrendOutcome>();
        DataSourceInfo dataSourceInfo = null;
        for (Map.Entry<String, List<AnalysisMeasure>> entry : trendMap.entrySet()) {
            String key = entry.getKey();
            List<AnalysisMeasure> measures = entry.getValue();
            WSListDefinition tempReport = new WSListDefinition();
            List<AnalysisItem> columns = new ArrayList<AnalysisItem>();
            columns.addAll(measures);
            if (analysisDefinition.getGroupings() != null) {
                columns.addAll(analysisDefinition.getGroupings());
            }
            tempReport.setFilterDefinitions(analysisDefinition.getFilterDefinitions());
            tempReport.setColumns(columns);
            tempReport.setDataFeedID(analysisDefinition.getDataFeedID());
            tempReport.setAddedItems(analysisDefinition.getAddedItems());
            tempReport.setMarmotScript(analysisDefinition.getMarmotScript());
            tempReport.setReportRunMarmotScript(analysisDefinition.getReportRunMarmotScript());
            tempReport.setJoinOverrides(analysisDefinition.getJoinOverrides());
            InsightRequestMetadata metadata = new InsightRequestMetadata();
            metadata.setUtcOffset(insightRequestMetadata.getUtcOffset());
            ReportRetrieval reportRetrievalNow = ReportRetrieval.reportEditor(metadata, tempReport, conn);
            dataSourceInfo = reportRetrievalNow.getDataSourceInfo();
            DataSet nowSet = reportRetrievalNow.getPipeline().toDataSet(reportRetrievalNow.getDataSet());
            DataSet pastSet;
            if ("".equals(key)) {
                pastSet = nowSet;
            } else {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, -analysisDefinition.getDayWindow());
                metadata = new InsightRequestMetadata();
                metadata.setUtcOffset(insightRequestMetadata.getUtcOffset());
                metadata.setNow(cal.getTime());
                ReportRetrieval reportRetrievalPast = ReportRetrieval.reportEditor(metadata, tempReport, conn);
                pastSet = reportRetrievalPast.getPipeline().toDataSet(reportRetrievalPast.getDataSet());
            }
            trendOutcomes.addAll(new Trend().calculateTrends(measures, analysisDefinition.getGroupings(), nowSet, pastSet));
        }
        List<TrendOutcome> targetOutcomes = new ArrayList<TrendOutcome>();
        for (AnalysisItem analysisItem : analysisDefinition.getMeasures()) {
            for (TrendOutcome trendOutcome : trendOutcomes) {
                if (trendOutcome.getMeasure().equals(analysisItem)) {
                    targetOutcomes.add(trendOutcome);
                    break;
                }
            }
        }
        trendOutcomes = targetOutcomes;
        TrendDataResults trendDataResults = new TrendDataResults();
        trendDataResults.setTrendOutcomes(trendOutcomes);
        trendDataResults.setSuggestions(new AnalysisService().generatePossibleIntentions(analysisDefinition, conn));
        trendDataResults.setDataSourceInfo(dataSourceInfo);
        return trendDataResults;
    }

    public EmbeddedTreeDataResults getEmbeddedTreeResults(long reportID, long dataSourceID, List<FilterDefinition> customFilters, InsightRequestMetadata insightRequestMetadata,
                                                          List<FilterDefinition> drillthroughFilters) {
        try {
            UserThreadMutex.mutex().acquire(SecurityUtil.getUserID(false));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // get the core data
        EIConnection conn = Database.instance().getConnection();
        try {
            SecurityUtil.authorizeInsight(reportID);
            LogClass.info(SecurityUtil.getUserID(false) + " retrieving " + reportID);
            WSTreeDefinition report = (WSTreeDefinition) new AnalysisStorage().getAnalysisDefinition(reportID, conn);
            ReportRetrieval reportRetrievalNow = ReportRetrieval.reportView(insightRequestMetadata, report, conn, customFilters, drillthroughFilters);

            DataSet dataSet = reportRetrievalNow.getPipeline().toDataSet(reportRetrievalNow.getDataSet());
            TreeData treeData = new TreeData(report, (AnalysisHierarchyItem) report.getHierarchy(), null, dataSet);
            for (IRow row : dataSet.getRows()) {
                treeData.addRow(row);
            }
            List<TreeRow> rows = treeData.toTreeRows(reportRetrievalNow.getPipeline().getPipelineData());
            EmbeddedTreeDataResults crossTabDataResults = new EmbeddedTreeDataResults();
            crossTabDataResults.setTreeRows(rows);
            crossTabDataResults.setDataSourceInfo(reportRetrievalNow.getDataSourceInfo());
            crossTabDataResults.setDefinition(report);
            return crossTabDataResults;
        } catch (com.easyinsight.security.SecurityException se) {
            EmbeddedTreeDataResults results = new EmbeddedTreeDataResults();
            results.setReportFault(new ServerError("You don't have access to this report. Check with your administrator about altering access privileges."));
            return results;
        } catch (ReportException dae) {
            EmbeddedTreeDataResults embeddedDataResults = new EmbeddedTreeDataResults();
            embeddedDataResults.setReportFault(dae.getReportFault());
            return embeddedDataResults;
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
            UserThreadMutex.mutex().release(SecurityUtil.getUserID(false));
        }
    }

    public EmbeddedCompareYearsDataResults getEmbeddedCompareYearsResults(long reportID, long dataSourceID, List<FilterDefinition> customFilters, InsightRequestMetadata insightRequestMetadata,
                                                        List<FilterDefinition> drillthroughFilters) {
        try {
            UserThreadMutex.mutex().acquire(SecurityUtil.getUserID(false));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // get the core data
        EIConnection conn = Database.instance().getConnection();
        try {
            SecurityUtil.authorizeInsight(reportID);
            WSCompareYearsDefinition wsytdDefinition = (WSCompareYearsDefinition) new AnalysisStorage().getAnalysisDefinition(reportID, conn);

            ReportRetrieval reportRetrievalNow = ReportRetrieval.reportView(insightRequestMetadata, wsytdDefinition, conn, customFilters, drillthroughFilters);
            DataSet nowSet = reportRetrievalNow.getPipeline().toDataSet(reportRetrievalNow.getDataSet());
            YearStuff ytdStuff = YTDUtil.getYearStuff(wsytdDefinition, nowSet, reportRetrievalNow.getPipeline().getPipelineData(),
                    reportRetrievalNow.getPipeline().getPipelineData().getAllRequestedItems());
            Map<String, Object> additionalProperties = new HashMap<String, Object>();
            additionalProperties.put("headers", ytdStuff.getHeaders());
            EmbeddedCompareYearsDataResults ytdDataResults = new EmbeddedCompareYearsDataResults();
            ytdDataResults.setDataSourceInfo(reportRetrievalNow.getDataSourceInfo());
            ytdDataResults.setAdditionalProperties(additionalProperties);
            ytdDataResults.setDataSet(ytdStuff.getRows());
            ytdDataResults.setDefinition(wsytdDefinition);
            return ytdDataResults;
        } catch (com.easyinsight.security.SecurityException se) {
            EmbeddedCompareYearsDataResults results = new EmbeddedCompareYearsDataResults();
            results.setReportFault(new ServerError("You don't have access to this report. Check with your administrator about altering access privileges."));
            return results;
        } catch (ReportException dae) {
            EmbeddedCompareYearsDataResults embeddedDataResults = new EmbeddedCompareYearsDataResults();
            embeddedDataResults.setReportFault(dae.getReportFault());
            return embeddedDataResults;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
            UserThreadMutex.mutex().release(SecurityUtil.getUserID(false));
        }
    }

    public CompareYearsDataResults getCompareYearsResults(WSAnalysisDefinition report, InsightRequestMetadata insightRequestMetadata) {
        try {
            UserThreadMutex.mutex().acquire(SecurityUtil.getUserID(false));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // get the core data
        EIConnection conn = Database.instance().getConnection();
        try {
            WSCompareYearsDefinition wsytdDefinition = (WSCompareYearsDefinition) report;

            ReportRetrieval reportRetrievalNow = ReportRetrieval.reportEditor(insightRequestMetadata, report, conn);
            DataSet nowSet = reportRetrievalNow.getPipeline().toDataSet(reportRetrievalNow.getDataSet());
            YearStuff ytdStuff = YTDUtil.getYearStuff(wsytdDefinition, nowSet, reportRetrievalNow.getPipeline().getPipelineData(),
                    reportRetrievalNow.getPipeline().getPipelineData().getAllRequestedItems());
            Map<String, Object> additionalProperties = new HashMap<String, Object>();
            additionalProperties.put("headers", ytdStuff.getHeaders());
            CompareYearsDataResults ytdDataResults = new CompareYearsDataResults();
            ytdDataResults.setDataSourceInfo(reportRetrievalNow.getDataSourceInfo());
            ytdDataResults.setAdditionalProperties(additionalProperties);
            ytdDataResults.setDataSet(ytdStuff.getRows());
            return ytdDataResults;
        } catch (ReportException dae) {
            CompareYearsDataResults embeddedDataResults = new CompareYearsDataResults();
            embeddedDataResults.setReportFault(dae.getReportFault());
            return embeddedDataResults;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
            UserThreadMutex.mutex().release(SecurityUtil.getUserID(false));
        }
    }

    public EmbeddedYTDDataResults getEmbeddedYTDResults(long reportID, long dataSourceID, List<FilterDefinition> customFilters, InsightRequestMetadata insightRequestMetadata,
                                                        List<FilterDefinition> drillthroughFilters) {
        try {
            UserThreadMutex.mutex().acquire(SecurityUtil.getUserID(false));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // get the core data
        EIConnection conn = Database.instance().getConnection();
        try {
            SecurityUtil.authorizeInsight(reportID);
            WSYTDDefinition wsytdDefinition = (WSYTDDefinition) new AnalysisStorage().getAnalysisDefinition(reportID, conn);
            if (wsytdDefinition.getTimeDimension() instanceof AnalysisDateDimension) {
                AnalysisDateDimension date = (AnalysisDateDimension) wsytdDefinition.getTimeDimension();
                date.setDateLevel(AnalysisDateDimension.MONTH_FLAT);
            }

            ReportRetrieval reportRetrievalNow = ReportRetrieval.reportView(insightRequestMetadata, wsytdDefinition, conn, customFilters, drillthroughFilters);
            DataSet nowSet = reportRetrievalNow.getPipeline().toDataSet(reportRetrievalNow.getDataSet());
            YTDStuff ytdStuff = YTDUtil.getYTDStuff(wsytdDefinition, nowSet, insightRequestMetadata, conn, reportRetrievalNow.getPipeline().getPipelineData(),
                    reportRetrievalNow.getPipeline().getPipelineData().getAllRequestedItems());
            Map<String, Object> additionalProperties = new HashMap<String, Object>();
            additionalProperties.put("timeIntervals", ytdStuff.getIntervals());
            EmbeddedYTDDataResults ytdDataResults = new EmbeddedYTDDataResults();
            ytdDataResults.setDataSourceInfo(reportRetrievalNow.getDataSourceInfo());
            ytdDataResults.setAdditionalProperties(additionalProperties);
            ytdDataResults.setDataSet(ytdStuff.values);
            ytdDataResults.setDefinition(wsytdDefinition);
            return ytdDataResults;
        } catch (com.easyinsight.security.SecurityException se) {
            EmbeddedYTDDataResults results = new EmbeddedYTDDataResults();
            results.setReportFault(new ServerError("You don't have access to this report. Check with your administrator about altering access privileges."));
            return results;
        } catch (ReportException dae) {
            EmbeddedYTDDataResults embeddedDataResults = new EmbeddedYTDDataResults();
            embeddedDataResults.setReportFault(dae.getReportFault());
            return embeddedDataResults;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
            UserThreadMutex.mutex().release(SecurityUtil.getUserID(false));
        }
    }

    public YTDDataResults getYTDResults(WSAnalysisDefinition report, InsightRequestMetadata insightRequestMetadata) {
        try {
            UserThreadMutex.mutex().acquire(SecurityUtil.getUserID(false));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // get the core data
        EIConnection conn = Database.instance().getConnection();
        try {
            WSYTDDefinition wsytdDefinition = (WSYTDDefinition) report;
            if (wsytdDefinition.getTimeDimension() instanceof AnalysisDateDimension) {
                AnalysisDateDimension date = (AnalysisDateDimension) wsytdDefinition.getTimeDimension();
                date.setDateLevel(AnalysisDateDimension.MONTH_FLAT);
            }

            ReportRetrieval reportRetrievalNow = ReportRetrieval.reportEditor(insightRequestMetadata, report, conn);
            DataSet nowSet = reportRetrievalNow.getPipeline().toDataSet(reportRetrievalNow.getDataSet());
            YTDStuff ytdStuff = YTDUtil.getYTDStuff(wsytdDefinition, nowSet, insightRequestMetadata, conn, reportRetrievalNow.getPipeline().getPipelineData(),
                    reportRetrievalNow.getPipeline().getPipelineData().getAllRequestedItems());
            Map<String, Object> additionalProperties = new HashMap<String, Object>();
            additionalProperties.put("timeIntervals", ytdStuff.getIntervals());
            YTDDataResults ytdDataResults = new YTDDataResults();
            ytdDataResults.setDataSourceInfo(reportRetrievalNow.getDataSourceInfo());
            ytdDataResults.setAdditionalProperties(additionalProperties);
            ytdDataResults.setDataSet(ytdStuff.values);
            return ytdDataResults;
        } catch (ReportException dae) {
            YTDDataResults embeddedDataResults = new YTDDataResults();
            embeddedDataResults.setReportFault(dae.getReportFault());
            return embeddedDataResults;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
            UserThreadMutex.mutex().release(SecurityUtil.getUserID(false));
        }
    }

    public TrendDataResults getTrendDataResults(WSKPIDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) {
        try {
            UserThreadMutex.mutex().acquire(SecurityUtil.getUserID(false));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        EIConnection conn = Database.instance().getConnection();
        try {
            SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
            LogClass.info(SecurityUtil.getUserID(false) + " retrieving " + analysisDefinition.getAnalysisID());
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
            Map<String, List<AnalysisMeasure>> trendMap = new HashMap<String, List<AnalysisMeasure>>();

            for (AnalysisItem analysisItem : analysisDefinition.getMeasures()) {
                if (analysisItem.getReportFieldExtension() != null && reportFilter != null && analysisItem.getReportFieldExtension() instanceof TrendReportFieldExtension) {
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
                        List<AnalysisMeasure> measures = trendMap.get(dateDimension.qualifiedName());
                        if (measures == null) {
                            measures = new ArrayList<AnalysisMeasure>();
                            trendMap.put(dateDimension.qualifiedName(), measures);
                        }
                        measures.add((AnalysisMeasure) analysisItem);
                    } else {
                        List<AnalysisMeasure> measures = trendMap.get("");
                        if (measures == null) {
                            measures = new ArrayList<AnalysisMeasure>();
                            trendMap.put("", measures);
                        }
                        measures.add((AnalysisMeasure) analysisItem);
                    }
                } else {
                    List<AnalysisMeasure> measures = trendMap.get("");
                    if (measures == null) {
                        measures = new ArrayList<AnalysisMeasure>();
                        trendMap.put("", measures);
                    }
                    measures.add((AnalysisMeasure) analysisItem);
                }
            }
            List<TrendOutcome> trendOutcomes = new ArrayList<TrendOutcome>();
            DataSourceInfo dataSourceInfo = null;
            for (Map.Entry<String, List<AnalysisMeasure>> entry : trendMap.entrySet()) {
                String key = entry.getKey();
                List<AnalysisMeasure> measures = entry.getValue();
                WSListDefinition tempReport = new WSListDefinition();
                List<AnalysisItem> columns = new ArrayList<AnalysisItem>();
                columns.addAll(measures);
                if (analysisDefinition.getGroupings() != null) {
                    columns.addAll(analysisDefinition.getGroupings());
                }
                tempReport.setFilterDefinitions(analysisDefinition.getFilterDefinitions());
                tempReport.setColumns(columns);
                tempReport.setDataFeedID(analysisDefinition.getDataFeedID());
                tempReport.setAddedItems(analysisDefinition.getAddedItems());
                tempReport.setMarmotScript(analysisDefinition.getMarmotScript());
                tempReport.setReportRunMarmotScript(analysisDefinition.getReportRunMarmotScript());
                tempReport.setJoinOverrides(analysisDefinition.getJoinOverrides());
                InsightRequestMetadata metadata = new InsightRequestMetadata();
                metadata.setUtcOffset(insightRequestMetadata.getUtcOffset());
                ReportRetrieval reportRetrievalNow = ReportRetrieval.reportEditor(metadata, tempReport, conn);
                dataSourceInfo = reportRetrievalNow.getDataSourceInfo();
                DataSet nowSet = reportRetrievalNow.getPipeline().toDataSet(reportRetrievalNow.getDataSet());
                DataSet pastSet;
                if ("".equals(key)) {
                    pastSet = nowSet;
                } else {
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DAY_OF_YEAR, -analysisDefinition.getDayWindow());
                    metadata = new InsightRequestMetadata();
                    metadata.setUtcOffset(insightRequestMetadata.getUtcOffset());
                    metadata.setNow(cal.getTime());
                    ReportRetrieval reportRetrievalPast = ReportRetrieval.reportEditor(metadata, tempReport, conn);
                    pastSet = reportRetrievalPast.getPipeline().toDataSet(reportRetrievalPast.getDataSet());
                }
                trendOutcomes.addAll(new Trend().calculateTrends(measures, analysisDefinition.getGroupings(), nowSet, pastSet));
            }
            List<TrendOutcome> targetOutcomes = new ArrayList<TrendOutcome>();
            for (AnalysisItem analysisItem : analysisDefinition.getMeasures()) {
                for (TrendOutcome trendOutcome : trendOutcomes) {
                    if (trendOutcome.getMeasure().equals(analysisItem)) {
                        targetOutcomes.add(trendOutcome);
                        break;
                    }
                }
            }
            trendOutcomes = targetOutcomes;
            TrendDataResults trendDataResults = new TrendDataResults();
            trendDataResults.setTrendOutcomes(trendOutcomes);
            trendDataResults.setSuggestions(new AnalysisService().generatePossibleIntentions(analysisDefinition, conn));
            trendDataResults.setDataSourceInfo(dataSourceInfo);
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
            UserThreadMutex.mutex().release(SecurityUtil.getUserID(false));
        }
    }

    public TreeDataResults getTreeDataResults(WSTreeDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) {
        try {
            UserThreadMutex.mutex().acquire(SecurityUtil.getUserID(false));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        EIConnection conn = Database.instance().getConnection();
        try {
            SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
            LogClass.info(SecurityUtil.getUserID(false) + " retrieving " + analysisDefinition.getAnalysisID());
            ReportRetrieval reportRetrieval = ReportRetrieval.reportEditor(insightRequestMetadata, analysisDefinition, conn);

            DataSet dataSet = listDataSet(analysisDefinition, insightRequestMetadata, conn);
            TreeData treeData = new TreeData(analysisDefinition, (AnalysisHierarchyItem) analysisDefinition.getHierarchy(), null, dataSet);
            for (IRow row : dataSet.getRows()) {
                treeData.addRow(row);
            }
            List<TreeRow> rows = treeData.toTreeRows(reportRetrieval.getPipeline().getPipelineData());
            TreeDataResults crossTabDataResults = new TreeDataResults();
            crossTabDataResults.setTreeRows(rows);
            crossTabDataResults.setSuggestions(new AnalysisService().generatePossibleIntentions(analysisDefinition, conn));
            crossTabDataResults.setDataSourceInfo(reportRetrieval.getDataSourceInfo());
            return crossTabDataResults;
        } catch (ReportException dae) {
            TreeDataResults embeddedDataResults = new TreeDataResults();
            embeddedDataResults.setReportFault(dae.getReportFault());
            return embeddedDataResults;
        } catch (Throwable e) {
            LogClass.error(e.getMessage() + " on running report " + analysisDefinition.getAnalysisID(), e);
            TreeDataResults embeddedDataResults = new TreeDataResults();
            embeddedDataResults.setReportFault(new ServerError(e.getMessage()));
            return embeddedDataResults;
        } finally {
            Database.closeConnection(conn);
            UserThreadMutex.mutex().release(SecurityUtil.getUserID(false));
        }
    }

    public CrossTabDataResults getCrosstabDataResults(WSCrosstabDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) {
        try {
            UserThreadMutex.mutex().acquire(SecurityUtil.getUserID(false));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        EIConnection conn = Database.instance().getConnection();
        try {
            SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
            LogClass.info(SecurityUtil.getUserID(false) + " retrieving " + analysisDefinition.getAnalysisID());
            ReportRetrieval reportRetrieval = ReportRetrieval.reportEditor(insightRequestMetadata, analysisDefinition, conn);
            Crosstab crosstab = new Crosstab();


            crosstab.crosstab(analysisDefinition, reportRetrieval.getPipeline().toDataSet(reportRetrieval.getDataSet()));
            CrosstabValue[][] values = crosstab.toTable(analysisDefinition, insightRequestMetadata, conn);

            List<CrosstabMapWrapper> resultData = new ArrayList<CrosstabMapWrapper>();
            int rowOffset = analysisDefinition.getMeasures().size() > 1 ? 3 : 2;
            for (int j = 0; j < (crosstab.getRowSections().size() + analysisDefinition.getColumns().size()) + rowOffset; j++) {
                Map<String, CrosstabValue> resultMap = new HashMap<String, CrosstabValue>();
                for (int i = 0; i < ((crosstab.getColumnSections().size() * analysisDefinition.getMeasures().size()) + analysisDefinition.getRows().size() + 1); i++) {
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
            crossTabDataResults.setColumnCount((crosstab.getColumnSections().size() * analysisDefinition.getMeasures().size()) + analysisDefinition.getRows().size() + 1);
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
            UserThreadMutex.mutex().release(SecurityUtil.getUserID(false));
        }
    }

    public VerticalDataResults getVerticalDataResults(WSCombinedVerticalListDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) {
        try {
            UserThreadMutex.mutex().acquire(SecurityUtil.getUserID(false));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
            LogClass.info(SecurityUtil.getUserID(false) + " retrieving " + analysisDefinition.getAnalysisID());
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
        } finally {
            UserThreadMutex.mutex().release(SecurityUtil.getUserID(false));
        }
    }

    public DataResults list(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) {
        try {
            UserThreadMutex.mutex().acquire(SecurityUtil.getUserID(false));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        EIConnection conn = Database.instance().getConnection();
        try {
            if (FlexContext.getHttpRequest() != null) {
                String ip = FlexContext.getHttpRequest().getRemoteAddr();
                System.out.println(ip);
            }
            SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
            LogClass.info(SecurityUtil.getUserID(false) + " retrieving " + analysisDefinition.getAnalysisID());
            ReportRetrieval reportRetrieval = ReportRetrieval.reportEditor(insightRequestMetadata, analysisDefinition, conn);
            DataResults results = reportRetrieval.getPipeline().toList(reportRetrieval.getDataSet(), conn, reportRetrieval.aliases);
            List<IntentionSuggestion> suggestions = new ArrayList<IntentionSuggestion>();
            suggestions.addAll(insightRequestMetadata.getSuggestions());
            if (analysisDefinition.isLogReport()) {
                results.setReportLog(reportRetrieval.getPipeline().toLogString());
            }
            results.setDataSourceInfo(reportRetrieval.getDataSourceInfo());
            suggestions.addAll(new AnalysisService().generatePossibleIntentions(analysisDefinition, conn));
            results.setSuggestions(suggestions);
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
            UserThreadMutex.mutex().release(SecurityUtil.getUserID(false));
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
        private Map<AnalysisItem, AnalysisItem> aliases = new HashMap<AnalysisItem, AnalysisItem>();

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
            ReportRetrieval reportRetrieval = new ReportRetrieval(insightRequestMetadata, analysisDefinition, conn).toPipeline();
            analysisDefinition.setRowsEditable(((reportRetrieval.getFeed().getFeedType().getType() == FeedType.DEFAULT.getType() || reportRetrieval.getFeed().getFeedType().getType() == FeedType.STATIC.getType()) && reportRetrieval.getFeed().getName().contains("Survey")) ||
                "ACS2".equals(reportRetrieval.getFeed().getName()) || "Therapy Works".equals(reportRetrieval.getFeed().getName()));
            return reportRetrieval;
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
            if (analysisDefinition.getAdditionalGroupingItems() == null) {
                analysisDefinition.setAdditionalGroupingItems(new ArrayList<AnalysisItem>());
            }
            if (insightRequestMetadata == null) {
                insightRequestMetadata = new InsightRequestMetadata();
            }
            insightRequestMetadata.setJoinOverrides(analysisDefinition.getJoinOverrides());
            insightRequestMetadata.setOptimized(analysisDefinition.isOptimized());
            insightRequestMetadata.setTraverseAllJoins(analysisDefinition.isFullJoins());




            if (insightRequestMetadata.getHierarchyOverrides() != null) {
                for (AnalysisItemOverride hierarchyOverride : insightRequestMetadata.getHierarchyOverrides()) {
                    hierarchyOverride.apply(analysisDefinition.getAllAnalysisItems());
                }
            }

            List<FilterDefinition> dlsFilters = addDLSFilters(analysisDefinition.getDataFeedID(), conn);
            analysisDefinition.getFilterDefinitions().addAll(dlsFilters);

            for (FilterDefinition filter : analysisDefinition.getFilterDefinitions()) {
                if (filter.getFieldChoiceFilterLabel() != null && !"".equals(filter.getFieldChoiceFilterLabel())) {
                    String label = filter.getFieldChoiceFilterLabel();
                    for (FilterDefinition testFilter : analysisDefinition.getFilterDefinitions()) {
                        if (label.equals(testFilter.getFilterName()) && testFilter.type() == FilterDefinition.ANALYSIS_ITEM) {
                            AnalysisItemFilterDefinition analysisItemFilterDefinition = (AnalysisItemFilterDefinition) testFilter;
                            filter.setField(analysisItemFilterDefinition.getTargetItem());
                        }
                    }
                }
                if (filter instanceof AnalysisItemFilterDefinition) {
                    AnalysisItemFilterDefinition analysisItemFilterDefinition = (AnalysisItemFilterDefinition) filter;

                    Map<String, AnalysisItem> structure = analysisDefinition.createStructure();
                    Map<String, AnalysisItem> structureCopy = new HashMap<String, AnalysisItem>(structure);
                    for (Map.Entry<String, AnalysisItem> entry : structureCopy.entrySet()) {
                        if (entry.getValue().toDisplay().equals(filter.getField().toDisplay())) {
                            structure.put(entry.getKey(), analysisItemFilterDefinition.getTargetItem());
                        }
                    }
                    analysisDefinition.populateFromReportStructure(structure);
                }
            }
            analysisDefinition.tweakReport(aliases);

            // acquirent report header on embed
            // medecare zendesk
            // balance freshbooks
            // oeo report
            // activity report on highrise
            // acs stuff

            List<AnalysisItem> allFields = new ArrayList<AnalysisItem>(feed.getFields());
            if (analysisDefinition.getAddedItems() != null) {
                allFields.addAll(analysisDefinition.getAddedItems());
            }

            KeyDisplayMapper mapper = KeyDisplayMapper.create(allFields);
            Map<String, List<AnalysisItem>> keyMap = mapper.getKeyMap();
            Map<String, List<AnalysisItem>> displayMap = mapper.getDisplayMap();


            if (analysisDefinition.getMarmotScript() != null) {
                StringTokenizer toker = new StringTokenizer(analysisDefinition.getMarmotScript(), "\r\n");
                while (toker.hasMoreTokens()) {
                    String line = toker.nextToken();
                    try {
                        new ReportCalculation(line).apply(analysisDefinition, allFields, keyMap, displayMap, feed, conn, dlsFilters, insightRequestMetadata);
                    } catch (FunctionException fe) {
                        throw new ReportException(new AnalysisItemFault(fe.getMessage() + " in the calculation of " + line + ".", null));
                    } catch (ReportException re) {
                        throw re;
                    } catch (Exception e) {
                        if ("org.antlr.runtime.tree.CommonErrorNode cannot be cast to com.easyinsight.calculations.CalculationTreeNode".equals(e.getMessage())) {
                            throw new ReportException(new AnalysisItemFault("Syntax error in the calculation of " + line + ".", null));
                        }
                        LogClass.error("On calculating " + line, e);
                        String message;
                        if (e.getMessage() == null) {
                            message = "Internal error";
                        } else {
                            message = e.getMessage();
                        }
                        throw new ReportException(new AnalysisItemFault(message + " in calculating " + line, null));
                    }
                }
            }
            if (feed.getDataSource().getMarmotScript() != null) {
                StringTokenizer toker = new StringTokenizer(feed.getDataSource().getMarmotScript(), "\r\n");
                while (toker.hasMoreTokens()) {
                    String line = toker.nextToken();
                    try {
                        new ReportCalculation(line).apply(analysisDefinition, allFields, keyMap, displayMap, feed, conn, dlsFilters, insightRequestMetadata);
                    } catch (FunctionException fe) {
                        throw new ReportException(new AnalysisItemFault(fe.getMessage() + " in the calculation of " + line + ".", null));
                    } catch (ReportException re) {
                        throw re;
                    } catch (Exception e) {
                        if ("org.antlr.runtime.tree.CommonErrorNode cannot be cast to com.easyinsight.calculations.CalculationTreeNode".equals(e.getMessage())) {
                            throw new ReportException(new AnalysisItemFault("Syntax error in the calculation of " + line + ".", null));
                        }
                        LogClass.error("On calculating " + line, e);
                        String message;
                        if (e.getMessage() == null) {
                            message = "Internal error";
                        } else {
                            message = e.getMessage();
                        }
                        throw new ReportException(new AnalysisItemFault(message + " in calculating " + line, null));
                    }
                }
            }
            insightRequestMetadata.setFieldToUniqueMap(analysisDefinition.getFieldToUniqueMap());
            insightRequestMetadata.setUniqueIteMap(analysisDefinition.getUniqueIteMap());
            AnalysisItemRetrievalStructure structure = new AnalysisItemRetrievalStructure(null);
            structure.setReport(analysisDefinition);
            structure.setInsightRequestMetadata(insightRequestMetadata);
            Set<AnalysisItem> analysisItems = analysisDefinition.getColumnItems(allFields, structure, insightRequestMetadata);
            if (analysisDefinition.isDataSourceFields()) {
                Map<String, AnalysisItem> map = new HashMap<String, AnalysisItem>();
                for (AnalysisItem field : feed.getFields()) {
                    map.put(field.toDisplay(), field);
                }
                for (AnalysisItem analysisItem : analysisItems) {
                    if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                        AnalysisMeasure analysisMeasure = (AnalysisMeasure) analysisItem;
                        AnalysisItem field = map.get(analysisItem.toDisplay());
                        if (field != null && field.hasType(AnalysisItemTypes.MEASURE)) {
                            analysisItem.getFormattingConfiguration().setFormattingType(field.getFormattingConfiguration().getFormattingType());
                            AnalysisMeasure sourceMeasure = (AnalysisMeasure) field;
                            analysisMeasure.setPrecision(sourceMeasure.getPrecision());
                            analysisMeasure.setUnderline(sourceMeasure.isUnderline());
                            analysisMeasure.setMinPrecision(sourceMeasure.getMinPrecision());
                            if (analysisItem.hasType(AnalysisItemTypes.CALCULATION) && field.hasType(AnalysisItemTypes.CALCULATION)) {
                                AnalysisCalculation sourceCalculation = (AnalysisCalculation) field;
                                AnalysisCalculation targetCalculation = (AnalysisCalculation) analysisItem;
                                targetCalculation.setCalculationString(sourceCalculation.getCalculationString());
                            }
                        }
                    }
                }
            }

            Set<AnalysisItem> validQueryItems = new HashSet<AnalysisItem>();

            for (AnalysisItem analysisItem : analysisItems) {
                //if (!analysisItem.isDerived() && (analysisItem.getLookupTableID() == null || analysisItem.getLookupTableID() == 0)) {
                    validQueryItems.add(analysisItem);
                //}
                if (analysisItem.getFilters() != null) {
                    for (FilterDefinition filterDefinition : analysisItem.getFilters()) {
                        filterDefinition.applyCalculationsBeforeRun(analysisDefinition, allFields, keyMap, displayMap, feed, conn, dlsFilters, insightRequestMetadata);
                    }
                }
            }

            for (FilterDefinition filterDefinition : analysisDefinition.getFilterDefinitions()) {
                filterDefinition.applyCalculationsBeforeRun(analysisDefinition, allFields, keyMap, displayMap, feed, conn, dlsFilters, insightRequestMetadata);
            }

            boolean aggregateQuery = true;
            Set<AnalysisItem> items = analysisDefinition.getAllAnalysisItems();
            items.remove(null);
            for (AnalysisItem analysisItem : items) {
                if (analysisItem.blocksDBAggregation()) {
                    aggregateQuery = false;
                }
            }
            String symbol = "$";
            long accountID = SecurityUtil.getAccountID(false);
            if (accountID > 0) {
                PreparedStatement accountStmt = conn.prepareStatement("SELECT CURRENCY_SYMBOL FROM ACCOUNT WHERE ACCOUNT_ID = ?");
                accountStmt.setLong(1, accountID);
                ResultSet rs = accountStmt.executeQuery();
                rs.next();
                symbol = rs.getString(1);
                accountStmt.close();
            }
            String currency = "USD";
            if ("$".equals(symbol)) {
                currency = "USD";
            } else if ("Û".equals(symbol)) {
                currency = "EUR";
            } else if ("£".equals(symbol) || "?".equals(symbol)) {
                currency = "GBP";
            }
            insightRequestMetadata.setTargetCurrency(currency);
            insightRequestMetadata.setAggregateQuery(aggregateQuery);
            insightRequestMetadata.setLookupTableAggregate(analysisDefinition.isLookupTableOptimization());
            insightRequestMetadata.setReportItems(analysisDefinition.getAllAnalysisItems());
            Collection<FilterDefinition> filters = analysisDefinition.retrieveFilterDefinitions();
            timeshift(validQueryItems, filters, feed);
            dataSet = retrieveDataSet(feed, validQueryItems, filters, insightRequestMetadata, feed.getFields(), conn);
            pipeline = new StandardReportPipeline(insightRequestMetadata.getIntermediatePipelines());
            pipeline.setup(analysisDefinition, feed, insightRequestMetadata, allFields);
            dataSourceInfo = feed.createSourceInfo(conn);
            if (dataSet.getLastTime() == null) {
                dataSet.setLastTime(new Date());
            }
            return this;
        }
    }
}
