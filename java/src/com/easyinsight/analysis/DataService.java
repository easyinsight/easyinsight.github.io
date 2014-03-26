package com.easyinsight.analysis;

import com.easyinsight.analysis.definitions.*;
import com.easyinsight.benchmark.BenchmarkManager;
import com.easyinsight.calculations.FunctionException;
import com.easyinsight.calculations.FunctionFactory;
import com.easyinsight.core.*;
import com.easyinsight.dashboard.Dashboard;
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
import com.easyinsight.tag.Tag;
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

    public List<AnalysisItemSelection> possibleFields(IFieldChoiceFilter filter, @Nullable WSAnalysisDefinition reportEditorReport, @Nullable Dashboard dashboardEditorDashboard,
                                                      @Nullable FilterSet filterSet) {
        WSAnalysisDefinition report = null;
        EIConnection conn = Database.instance().getConnection();
        long dashboardID = 0;
        try {
            long dataSourceID;
            if (reportEditorReport != null) {
                report = reportEditorReport;
                dataSourceID = reportEditorReport.getDataFeedID();
            } else if (dashboardEditorDashboard != null) {
                dataSourceID = dashboardEditorDashboard.getDataSourceID();
            } else if (filterSet != null) {
                dataSourceID = filterSet.getDataSourceID();
            } else {



                    PreparedStatement stmt = conn.prepareStatement("SELECT ANALYSIS_ID FROM analysis_to_filter_join WHERE FILTER_ID = ?");
                    stmt.setLong(1, filter.getFilterID());
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        report = new AnalysisStorage().getAnalysisDefinition(rs.getLong(1), conn);
                        dataSourceID = report.getDataFeedID();
                    } else {
                        PreparedStatement dashboardStmt = conn.prepareStatement("SELECT DATA_SOURCE_ID, dashboard.DASHBOARD_ID FROM dashboard_to_filter, dashboard WHERE " +
                                "dashboard_to_filter.filter_id = ? AND dashboard_to_filter.dashboard_id = dashboard.dashboard_id");
                        dashboardStmt.setLong(1, filter.getFilterID());
                        ResultSet dashboardRS = dashboardStmt.executeQuery();
                        if (dashboardRS.next()) {
                            dataSourceID = dashboardRS.getLong(1);
                            dashboardID = dashboardRS.getLong(2);
                        } else {
                            return new ArrayList<AnalysisItemSelection>();
                            //throw new RuntimeException();
                        }
                    }


                if (report != null) {
                    SecurityUtil.authorizeInsight(report.getAnalysisID());
                } else if (dashboardID > 0) {
                    SecurityUtil.authorizeDashboard(dashboardID);
                }
            }

            FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(dataSourceID, conn);
            Map<Long, AnalysisItem> map = new HashMap<Long, AnalysisItem>();
            Map<String, AnalysisItem> mapByName = new HashMap<String, AnalysisItem>();
            final Map<AnalysisItem, Integer> positions = new HashMap<AnalysisItem, Integer>();
            List<AnalysisItem> allFields = dataSource.allFields(conn);
            for (AnalysisItem field : allFields) {
                map.put(field.getAnalysisItemID(), field);
                mapByName.put(field.toDisplay(), field);
            }

            if (report != null) {
                if (report.getAddedItems() != null) {
                    for (AnalysisItem item : report.getAddedItems()) {
                        mapByName.put(item.toDisplay(), item);
                        if (item.getAnalysisItemID() != 0) {
                            map.put(item.getAnalysisItemID(), item);
                        }
                    }
                }
            }
            Map<AnalysisItem, AnalysisItemHandle> selectedMap = new HashMap<AnalysisItem, AnalysisItemHandle>();


            Set<AnalysisItem> set = new HashSet<AnalysisItem>();
            int i = 0;
            if (!filter.excludeReportFields() && report != null && report instanceof WSListDefinition) {
                WSListDefinition list = (WSListDefinition) report;
                set.addAll(list.getColumns());
                for (AnalysisItem item : list.getColumns()) {
                    positions.put(item, i++);
                }
            }
            for (AnalysisItemHandle field : filter.getAvailableHandles()) {
                AnalysisItem item = map.get(field.getAnalysisItemID());
                if (item != null) {
                    set.add(item);
                    positions.put(item, i++);
                } else {
                    item = mapByName.get(field.getName());
                    if (item != null) {
                        set.add(item);
                        positions.put(item, i++);
                    }
                }
            }

            List<WeNeedToReplaceHibernateTag> tags = filter.getAvailableTags();

            PreparedStatement customFieldQueryStmt = conn.prepareStatement("SELECT custom_flag_to_tag.custom_flag FROM custom_flag_to_tag WHERE tag_id = ? AND data_source_id = ?");

            for (WeNeedToReplaceHibernateTag tag : tags) {
                customFieldQueryStmt.setLong(1, tag.getTagID());
                customFieldQueryStmt.setLong(2, dataSourceID);
                ResultSet rs = customFieldQueryStmt.executeQuery();
                while (rs.next()) {
                    int customFlag = rs.getInt(1);
                    for (AnalysisItem analysisItem : allFields) {
                        if (analysisItem.getCustomFlag() == customFlag) {
                            positions.put(analysisItem, i++);
                            set.add(analysisItem);
                        }
                    }
                }
            }
            customFieldQueryStmt.close();

            PreparedStatement queryStmt = conn.prepareStatement("SELECT field_to_tag.display_name FROM field_to_tag WHERE account_tag_id = ? AND field_to_tag.data_source_id = ?");

                for (WeNeedToReplaceHibernateTag tag : tags) {
                    queryStmt.setLong(1, tag.getTagID());
                    queryStmt.setLong(2, dataSourceID);
                    ResultSet rs = queryStmt.executeQuery();
                    while (rs.next()) {
                        String fieldName = rs.getString(1);
                        AnalysisItem analysisItem = mapByName.get(fieldName);
                        //if (report == null || report.accepts(analysisItem)) {
                        if (analysisItem != null) {
                            positions.put(analysisItem, i++);
                            set.add(analysisItem);
                        }
                            /*PreparedStatement extStmt = conn.prepareStatement("SELECT report_field_extension_id FROM analysis_item_to_report_field_extension WHERE analysis_item_id = ? and " +
                                    "extension_type = ?");
                            extStmt.setLong(1, fieldID);
                            extStmt.setInt(2, report.extensionType());
                            ResultSet extRS = extStmt.executeQuery();
                            if (extRS.next()) {
                                long extID = extRS.getLong(1);
                                Session session = Database.instance().createSession(conn);
                                try {
                                    ReportFieldExtension ext = (ReportFieldExtension) session.createQuery("from ReportFieldExtension where reportFieldExtensionID = ?").setLong(0, extID).list().get(0);
                                    ext.afterLoad();
                                    analysisItem.setReportFieldExtension(ext);
                                } finally {
                                    session.close();
                                }
                            }
                            extStmt.close();*/
                    }
                    //}
                }


                queryStmt.close();




            i = 0;
            for (AnalysisItemHandle handle : filter.selectedItems()) {
                AnalysisItem item = mapByName.get(handle.getName());
                if (item != null) {
                    positions.put(item, i++);
                    selectedMap.put(item, handle);
                }
            }


            List<AnalysisItemSelection> items = new ArrayList<AnalysisItemSelection>();

            for (AnalysisItem item : set) {
                AnalysisItemSelection selection = new AnalysisItemSelection();
                selection.setAnalysisItem(item);
                AnalysisItemHandle handle = selectedMap.get(item);
                if (handle != null) {
                    selection.setSelected(handle.isSelected());
                }
                items.add(selection);
            }

            final Map<String, Integer> fieldOrderingMap = new HashMap<String, Integer>();
            if (filter.getFieldOrdering() != null && filter.getFieldOrdering().size() > 0) {
                int j = 0;
                for (AnalysisItemHandle handle : filter.getFieldOrdering()) {
                    fieldOrderingMap.put(handle.getName(), j++);
                }
            }

            Collections.sort(items, new Comparator<AnalysisItemSelection>() {

                public int compare(AnalysisItemSelection analysisItem, AnalysisItemSelection analysisItem1) {
                    if (fieldOrderingMap.isEmpty()) {
                        Integer p1 = positions.get(analysisItem.getAnalysisItem());
                        Integer p2 = positions.get(analysisItem1.getAnalysisItem());
                        return p1.compareTo(p2);
                    } else {
                        Integer p1 = fieldOrderingMap.get(analysisItem.getAnalysisItem().toDisplay());
                        Integer p2 = fieldOrderingMap.get(analysisItem1.getAnalysisItem().toDisplay());
                        if (p1 == null && p2 != null) {
                            return 1;
                        }
                        if (p2 == null && p1 != null) {
                            return -1;
                        }
                        if (p1 == null && p2 == null) {
                            p1 = positions.get(analysisItem.getAnalysisItem());
                            p2 = positions.get(analysisItem1.getAnalysisItem());
                            return p1.compareTo(p2);
                        }
                        return p1.compareTo(p2);
                    }

                }
            });
            return items;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public AnalysisItemResultMetadata getAnalysisItemMetadata(long feedID, AnalysisItem analysisItem, int utfOffset, long reportID, long dashboardID) {
        return getAnalysisItemMetadata(feedID, analysisItem, utfOffset, reportID, dashboardID, null);
    }

    public AnalysisItemResultMetadata getAnalysisItemMetadata(long feedID, AnalysisItem analysisItem, int utfOffset, long reportID, long dashboardID,
                                                              @Nullable WSAnalysisDefinition report) {
        return getAnalysisItemMetadata(feedID, analysisItem, utfOffset, reportID, dashboardID, report, null, null, null);
    }

    public AnalysisItemResultMetadata getAnalysisItemMetadata(long feedID, AnalysisItem analysisItem, int utfOffset, long reportID, long dashboardID,
                                                              @Nullable WSAnalysisDefinition report, List<FilterDefinition> additionalFilters, FilterDefinition requester) {
        return getAnalysisItemMetadata(feedID, analysisItem, utfOffset, reportID, dashboardID, report, additionalFilters, requester, null);
    }

    public AnalysisItemResultMetadata getAnalysisItemMetadataForFilter(long filterID, List<FilterDefinition> filters, int utcOffset) {
        EIConnection conn = Database.instance().getConnection();
        try {
            Long reportID = null;
            Long dashboardID = null;
            Long dashboardElementID = null;
            FilterDefinition filter;
            Session session = Database.instance().createSession(conn);
            try {
                filter = (FilterDefinition) session.createQuery("from FilterDefinition where filterID = ?").setLong(0, filterID).list().get(0);
                filter.afterLoad();
            } finally {
                session.close();
            }
            AnalysisItem analysisItem = filter.getField();
            PreparedStatement ps = conn.prepareStatement("SELECT ANALYSIS_ID FROM analysis_to_filter_join WHERE filter_id = ?");
            ps.setLong(1, filterID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                reportID = rs.getLong(1);
            } else {
                PreparedStatement dashboardPS = conn.prepareStatement("SELECT dashboard_id FROM dashboard_to_filter WHERE filter_id = ?");
                dashboardPS.setLong(1, filterID);
                ResultSet dashboardRS = dashboardPS.executeQuery();
                if (dashboardRS.next()) {
                    dashboardID = dashboardRS.getLong(1);
                } else {
                    PreparedStatement dashboardElementPS = conn.prepareStatement("SELECT dashboard_element.dashboard_element_id FROM " +
                            "dashboard_element_to_filter, dashboard_element WHERE filter_id = ?");
                    dashboardElementPS.setLong(1, filterID);
                    ResultSet dashboardElementRS = dashboardElementPS.executeQuery();
                    while (dashboardElementRS.next()) {
                        dashboardElementID = dashboardElementRS.getLong(1);
                    }
                    dashboardElementPS.close();
                }
                dashboardPS.close();
            }
            ps.close();
            WSAnalysisDefinition report = null;
            long dataSourceID;
            if (reportID != null) {
                SecurityUtil.authorizeInsight(reportID);
                report = new AnalysisStorage().getAnalysisDefinition(reportID);
                dataSourceID = report.getDataFeedID();
            } else if (dashboardID != null) {
                SecurityUtil.authorizeDashboard(dashboardID);
                PreparedStatement stmt = conn.prepareStatement("SELECT data_source_id FROM dashboard WHERE dashboard_id = ?");
                stmt.setLong(1, dashboardID);
                ResultSet dashboardRS = stmt.executeQuery();
                dashboardRS.next();
                dataSourceID = dashboardRS.getLong(1);
                stmt.close();
            } else if (dashboardElementID != null) {
                PreparedStatement rootStmt = conn.prepareStatement("SELECT DASHBOARD.DASHBOARD_ID, DATA_SOURCE_ID FROM DASHBOARD, DASHBOARD_TO_DASHBOARD_ELEMENT WHERE DASHBOARD_ELEMENT_ID = ? AND " +
                        "DASHBOARD_TO_DASHBOARD_ELEMENT.DASHBOARD_ID = DASHBOARD.DASHBOARD_ID");
                PreparedStatement findParentInGridStmt = conn.prepareStatement("SELECT DASHBOARD_GRID.DASHBOARD_ELEMENT_ID  FROM " +
                        "DASHBOARD_GRID, DASHBOARD_GRID_ITEM WHERE DASHBOARD_GRID_ITEM.DASHBOARD_ELEMENT_ID = ? AND DASHBOARD_GRID_ITEM.DASHBOARD_GRID_ID = DASHBOARD_GRID.DASHBOARD_GRID_ID");
                PreparedStatement findParentInStackStmt = conn.prepareStatement("SELECT DASHBOARD_STACK.DASHBOARD_ELEMENT_ID  FROM " +
                        "DASHBOARD_STACK, DASHBOARD_STACK_ITEM WHERE DASHBOARD_STACK_ITEM.DASHBOARD_ELEMENT_ID = ? AND DASHBOARD_STACK_ITEM.DASHBOARD_STACK_ID = DASHBOARD_STACK.DASHBOARD_STACK_ID");
                Blah blah = findDashboard(dashboardElementID, rootStmt, findParentInGridStmt, findParentInStackStmt);
                if (blah == null) {
                    throw new RuntimeException();
                }
                dataSourceID = blah.dataSourceID;
                SecurityUtil.authorizeDashboard(blah.dashboardID);
                rootStmt.close();
                findParentInGridStmt.close();
                findParentInStackStmt.close();
            } else {
                throw new RuntimeException();
            }

            return getMetadata(dataSourceID, analysisItem, utcOffset, report, filters, filter, null, conn);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    private Blah findDashboard(long dashboardElementID, PreparedStatement rootStmt, PreparedStatement findParentInGridStmt, PreparedStatement findParentInStackStmt) throws SQLException {
        rootStmt.setLong(1, dashboardElementID);
        ResultSet rootRS = rootStmt.executeQuery();
        if (rootRS.next()) {
            return new Blah(rootRS.getLong(1), rootRS.getLong(2));
        }
        findParentInGridStmt.setLong(1, dashboardElementID);
        ResultSet gridRS = findParentInGridStmt.executeQuery();
        if (gridRS.next()) {
            return findDashboard(gridRS.getLong(1), rootStmt, findParentInGridStmt, findParentInStackStmt);
        }
        findParentInStackStmt.setLong(1, dashboardElementID);
        ResultSet stackRS = findParentInStackStmt.executeQuery();
        if (stackRS.next()) {
            return findDashboard(stackRS.getLong(1), rootStmt, findParentInGridStmt, findParentInStackStmt);
        }
        return null;
    }

    private static class Blah {
        long dashboardID;
        long dataSourceID;

        private Blah(long dashboardID, long dataSourceID) {
            this.dashboardID = dashboardID;
            this.dataSourceID = dataSourceID;
        }
    }

    public AnalysisItemResultMetadata getAnalysisItemMetadata(long feedID, AnalysisItem analysisItem, int utfOffset, long reportID, long dashboardID,
                                                              @Nullable WSAnalysisDefinition report, List<FilterDefinition> additionalFilters, FilterDefinition requester,
                                                              @Nullable Dashboard dashboard) {
        boolean success;
        try {
            success = UserThreadMutex.mutex().acquire(SecurityUtil.getUserID(false));
        } catch (ReportException e) {
            AnalysisItemResultMetadata metadata = new AnalysisItemResultMetadata();
            metadata.setReportFault(e.getReportFault());
            return metadata;
        }
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
            return getMetadata(feedID, analysisItem, utfOffset, report, additionalFilters, requester, dashboard, conn);
        } catch (ReportException re) {
            AnalysisItemResultMetadata metadata = new AnalysisItemResultMetadata();
            metadata.setReportFault(re.getReportFault());
            return metadata;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            if (success) {
                UserThreadMutex.mutex().release(SecurityUtil.getUserID(false));
            }
            Database.closeConnection(conn);
        }
    }

    private AnalysisItemResultMetadata getMetadata(long feedID, AnalysisItem analysisItem, int utfOffset, WSAnalysisDefinition report, List<FilterDefinition> additionalFilters, FilterDefinition requester, Dashboard dashboard, EIConnection conn) {
        Feed feed = feedRegistry.getFeed(feedID, conn);
        InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
        insightRequestMetadata.setUtcOffset(utfOffset);
        if (report != null) {
            insightRequestMetadata.setJoinOverrides(report.getJoinOverrides());
            insightRequestMetadata.setTraverseAllJoins(report.isFullJoins());
            insightRequestMetadata.setAddonReports(report.getAddonReports());
            insightRequestMetadata.setAggregateQuery(false);
            insightRequestMetadata.getDistinctFieldMap().put(analysisItem, true);
            insightRequestMetadata.setAdditionalAnalysisItems(report.getFieldsForDrillthrough());

            if (requester != null && requester.getFieldChoiceFilterLabel() != null && !"".equals(requester.getFieldChoiceFilterLabel())) {
                String label = requester.getFieldChoiceFilterLabel();
                for (FilterDefinition testFilter : report.getFilterDefinitions()) {
                    if (label.equals(testFilter.getFilterName()) && testFilter.type() == FilterDefinition.ANALYSIS_ITEM) {
                        AnalysisItemFilterDefinition analysisItemFilterDefinition = (AnalysisItemFilterDefinition) testFilter;
                        analysisItem = analysisItemFilterDefinition.getTargetItem();
                    }
                }
            }
        } else if (dashboard != null) {
            if (requester != null && requester.getFieldChoiceFilterLabel() != null && !"".equals(requester.getFieldChoiceFilterLabel())) {
                String label = requester.getFieldChoiceFilterLabel();
                for (FilterDefinition testFilter : dashboard.getFilters()) {
                    if (label.equals(testFilter.getFilterName()) && testFilter.type() == FilterDefinition.ANALYSIS_ITEM) {
                        AnalysisItemFilterDefinition analysisItemFilterDefinition = (AnalysisItemFilterDefinition) testFilter;
                        analysisItem = analysisItemFilterDefinition.getTargetItem();
                    }
                }
            }
        }
        timeshift(Arrays.asList(analysisItem), new ArrayList<FilterDefinition>(), feed, insightRequestMetadata);
        return feed.getMetadata(analysisItem, insightRequestMetadata, conn, report, additionalFilters, requester);
    }

    public List<FeedNode> multiAddonFields(WSAnalysisDefinition report) {
        EIConnection conn = Database.instance().getConnection();
        try {
            List<FeedNode> nodes = new ArrayList<FeedNode>();
            for (AddonReport addonReport : report.getAddonReports()) {
                nodes.add(addonFields(addonReport, conn));
            }
            return nodes;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public FeedNode addonFields(AddonReport addonReport) {
        EIConnection conn = Database.instance().getConnection();
        try {
            return addonFields(addonReport, conn);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    private FeedNode addonFields(AddonReport addonReport, EIConnection conn) throws CloneNotSupportedException {
        Map<Long, AnalysisItem> replacementMap = new HashMap<Long, AnalysisItem>();
        List<AnalysisItem> fields = new ArrayList<AnalysisItem>();
        WSAnalysisDefinition report = new AnalysisStorage().getAnalysisDefinition(addonReport.getReportID(), conn);
        Map<String, AnalysisItem> structure = report.createStructure();
        for (AnalysisItem item : structure.values()) {
            AnalysisItem clone;
            if (item.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                AnalysisDateDimension baseDate = (AnalysisDateDimension) item;
                AnalysisDateDimension date = new AnalysisDateDimension();
                date.setDateLevel(baseDate.getDateLevel());
                date.setOutputDateFormat(baseDate.getOutputDateFormat());
                date.setDateOnlyField(baseDate.isDateOnlyField() || baseDate.hasType(AnalysisItemTypes.DERIVED_DATE));
                clone = date;
            } else if (item.hasType(AnalysisItemTypes.MEASURE)) {
                AnalysisMeasure baseMeasure = (AnalysisMeasure) item;
                AnalysisMeasure measure = new AnalysisMeasure();
                measure.setFormattingType(item.getFormattingType());
                if (report.isPersistedCache()) {
                    measure.setAggregation(AggregationTypes.SUM);
                } else {
                    measure.setAggregation(baseMeasure.getAggregation());
                }
                measure.setPrecision(baseMeasure.getPrecision());
                measure.setMinPrecision(baseMeasure.getMinPrecision());
                clone = measure;
            } else {
                clone = new AnalysisDimension();
            }
            //clone.setParentItemID(item.getAnalysisItemID());
            clone.setOriginalDisplayName(item.toDisplay());
            clone.setDisplayName(report.getName() + " - " + item.toDisplay());
            clone.setUnqualifiedDisplayName(item.toUnqualifiedDisplay());
            clone.setBasedOnReportField(item.getAnalysisItemID());
            ReportKey reportKey = new ReportKey();
            reportKey.setParentKey(item.getKey());
            reportKey.setReportID(addonReport.getReportID());
            clone.setKey(reportKey);
            replacementMap.put(item.getAnalysisItemID(), clone);
            fields.add(clone);
        }
        ReplacementMap replacements = ReplacementMap.fromMap(replacementMap);
        for (AnalysisItem clone : fields) {
            clone.updateIDs(replacements);
        }
        FolderNode folderNode = new FolderNode();
        folderNode.setAddonReportDescriptor(new InsightDescriptor(report.getAnalysisID(), report.getName(), report.getDataFeedID(), report.getReportType(), report.getUrlKey(), 0, false));
        folderNode.setAddonReportID(addonReport.getReportID());
        FeedFolder feedFolder = new FeedFolder();
        feedFolder.setName(report.getName());
        folderNode.setFolder(feedFolder);
        for (AnalysisItem analysisItem : fields) {
            folderNode.getChildren().add(analysisItem.toFeedNode());
        }
        return folderNode;
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
            feedMetadata.setForceOuterJoin(feed.getDataSource().isDefaultToFullJoins());
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
            feedMetadata.setDefaultManualRun(feed.getDataSource().isManualReportRun());
            feedMetadata.setOptimized(feed.getDataSource().isDefaultToOptimized());

            //List<FieldRule> rules = FieldRule.load(conn, feedID);

            /*for (AnalysisItem analysisItem : feedItems) {
                for (FieldRule rule : rules) {
                    if (rule.matches(analysisItem)) {
                        rule.update(analysisItem);
                    }
                }
            }*/
            feedMetadata.setTagDefault(feed.getDefaultTag());
            feedMetadata.setTags(feed.getTags());
            PreparedStatement ps = conn.prepareStatement("SELECT DEFAULT_MAX_RECORDS FROM ACCOUNT WHERE ACCOUNT_ID = ?");
            ps.setLong(1, SecurityUtil.getAccountID());
            ResultSet rs = ps.executeQuery();
            rs.next();
            int defaultMaxRecords = rs.getInt(1);
            ps.close();
            feedMetadata.setDefaultMaxRecords(defaultMaxRecords);
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
            //feedMetadata.setDataSourceFields(feed.getDataSource().getFields());
            WSListDefinition tempList = new WSListDefinition();
            tempList.setDataFeedID(feedID);
            tempList.setColumns(new ArrayList<AnalysisItem>());
            feedMetadata.setSuggestions(new AnalysisService().generatePossibleIntentions(tempList, conn, new InsightRequestMetadata()));
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

    public EmbeddedTrendDataResults getEmbeddedTrendDataResults(long reportID, long dataSourceID, List<FilterDefinition> customFilters, InsightRequestMetadata insightRequestMetadata) {
        boolean success = UserThreadMutex.mutex().acquire(SecurityUtil.getUserID(false));
        EIConnection conn = Database.instance().getConnection();
        try {
            long start = System.currentTimeMillis();
            SecurityUtil.authorizeInsight(reportID);
            LogClass.info(SecurityUtil.getUserID(false) + " retrieving " + reportID);
            WSKPIDefinition analysisDefinition = (WSKPIDefinition) new AnalysisStorage().getAnalysisDefinition(reportID, conn);
            CacheKey cacheKey = null;
            if (analysisDefinition.isCacheable()) {
                List<String> filters = new ArrayList<String>();
                XMLMetadata xmlMetadata = new XMLMetadata();
                xmlMetadata.setConn(conn);
                for (FilterDefinition filter : customFilters) {
                    filters.add(filter.toXML(xmlMetadata).toXML());
                }
                cacheKey = new CacheKey(reportID, filters);
                EmbeddedResults embeddedResults = ReportCache.instance().getResults(dataSourceID, cacheKey, analysisDefinition.getCacheMinutes());
                if (embeddedResults != null) {
                    LogClass.debug("*** Returning from cache");
                    return (EmbeddedTrendDataResults) embeddedResults;
                }
            }
            analysisDefinition.setFilterDefinitions(customFilters);
            TrendResult trendResult = createTrendOutcomes(analysisDefinition, insightRequestMetadata, conn);
            //trendOutcomes = targetOutcomes;
            EmbeddedTrendDataResults trendDataResults = new EmbeddedTrendDataResults();
            trendDataResults.setTrendOutcomes(trendResult.trendOutcomes);
            trendDataResults.setNowString(trendResult.nowString);
            trendDataResults.setPreviousString(trendResult.previousString);
            trendDataResults.setDataSourceInfo(trendResult.dataSourceInfo);

            trendDataResults.setDefinition(analysisDefinition);
            if (cacheKey != null) {
                ReportCache.instance().storeReport(dataSourceID, cacheKey, trendDataResults, analysisDefinition.getCacheMinutes());
            }
            reportViewBenchmark(analysisDefinition, System.currentTimeMillis() - start - insightRequestMetadata.getDatabaseTime(), insightRequestMetadata.getDatabaseTime(), conn);
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
            if (success) {
                UserThreadMutex.mutex().release(SecurityUtil.getUserID(false));
            }
            Database.closeConnection(conn);
        }
    }

    public EmbeddedCrosstabDataResults getEmbeddedCrosstabResults(long reportID, long dataSourceID, List<FilterDefinition> customFilters, InsightRequestMetadata insightRequestMetadata,
                                                                  List<FilterDefinition> drillthroughFilters) {
        boolean success = UserThreadMutex.mutex().acquire(SecurityUtil.getUserID(false));
        EIConnection conn = Database.instance().getConnection();
        try {
            long start = System.currentTimeMillis();
            SecurityUtil.authorizeInsight(reportID);
            LogClass.info(SecurityUtil.getUserID(false) + " retrieving " + reportID);
            WSCrosstabDefinition crosstabReport = (WSCrosstabDefinition) new AnalysisStorage().getAnalysisDefinition(reportID, conn);
            ReportRetrieval reportRetrieval = ReportRetrieval.reportView(insightRequestMetadata, crosstabReport, conn, customFilters, drillthroughFilters);
            Crosstab crosstab = new Crosstab();
            crosstab.crosstab(crosstabReport, reportRetrieval.getPipeline().toDataSet(reportRetrieval.getDataSet()));
            CrosstabValue[][] values = crosstab.toTable(crosstabReport, insightRequestMetadata, conn);

            List<CrosstabMapWrapper> resultData = new ArrayList<CrosstabMapWrapper>();
            int rowOffset = crosstabReport.getMeasures().size() > 1 ? 3 : 2;
            for (int j = 0; j < (crosstab.getRowSections().size() + crosstabReport.getColumns().size()) + rowOffset; j++) {
                Map<String, CrosstabValue> resultMap = new HashMap<String, CrosstabValue>();
                if (crosstabReport.isExcludeZero()) {
                    CrosstabValue summaryValue = values[j][((crosstab.getColumnSections().size() * crosstabReport.getMeasures().size()) + crosstabReport.getRows().size())];
                    if (summaryValue != null && summaryValue.getValue() != null && summaryValue.getValue().toDouble() != null && summaryValue.getValue().toDouble() == 0) {
                        continue;
                    }
                }
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
            reportViewBenchmark(crosstabReport, System.currentTimeMillis() - start - insightRequestMetadata.getDatabaseTime(), insightRequestMetadata.getDatabaseTime(), conn);
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
            if (success) {
                UserThreadMutex.mutex().release(SecurityUtil.getUserID(false));
            }
            Database.closeConnection(conn);
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
                PreparedStatement dlsStmt = conn.prepareStatement("SELECT user_dls_to_filter.FILTER_ID FROM user_dls_to_filter, user_dls, dls WHERE " +
                        "user_dls_to_filter.user_dls_id = user_dls.user_dls_id AND user_dls.dls_id = dls.dls_id AND dls.data_source_id = ? AND " +
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
                PreparedStatement dlsStmt = conn.prepareStatement("SELECT user_dls_to_filter.FILTER_ID FROM user_dls_to_filter, user_dls, dls, composite_node, composite_feed WHERE " +
                        "user_dls_to_filter.user_dls_id = user_dls.user_dls_id AND user_dls.dls_id = dls.dls_id AND " +
                        "user_dls.user_id = ? AND composite_node.data_feed_id = ? AND composite_node.composite_feed_id = composite_feed.composite_feed_id AND " +
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
                PreparedStatement dlsStmt = conn.prepareStatement("SELECT user_dls_to_filter.FILTER_ID FROM user_dls_to_filter, user_dls, dls, composite_node, composite_feed WHERE " +
                        "user_dls_to_filter.user_dls_id = user_dls.user_dls_id AND user_dls.dls_id = dls.dls_id AND " +
                        "user_dls.user_id = ? AND composite_node.data_feed_id = dls.data_source_id AND composite_node.composite_feed_id = composite_feed.composite_feed_id AND " +
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

    private String cacheEmbeddedReportResults(long reportID, EmbeddedDataResults dataResults) {
        String uid = reportID + String.valueOf(System.currentTimeMillis());
        dataResults.setUid(uid);
        /*MemcachedClient client = MemCachedManager.instance();
        client.add(uid, 0, dataResults);*/
        simpleEmbeddedCache.put(uid, dataResults);
        return uid;
    }

    private EmbeddedDataResults truncateEmbeddedResults(EmbeddedDataResults dataResults, int limit) throws CloneNotSupportedException {
        EmbeddedDataResults copyResults = (EmbeddedDataResults) dataResults.clone();
        ListRow[] truncatedRows = new ListRow[limit];
        System.arraycopy(dataResults.getRows(), 0, truncatedRows, 0, limit);
        copyResults.setRows(truncatedRows);
        return copyResults;
    }

    public EmbeddedDataResults moreEmbeddedResults(long reportID, long dataSourceID, List<FilterDefinition> customFilters,
                                                   InsightRequestMetadata insightRequestMetadata, @Nullable List<FilterDefinition> drillThroughFilters, String uid) {
        /*MemcachedClient client = MemCachedManager.instance();
        EmbeddedDataResults results = (EmbeddedDataResults) client.get(uid);*/
        EmbeddedDataResults results = simpleEmbeddedCache.get(uid);
        if (results == null) {
            return (EmbeddedDataResults) getEmbeddedResults(reportID, dataSourceID, customFilters, insightRequestMetadata, drillThroughFilters, true);
        } else {
            results.getAdditionalProperties().put("cappedResults", null);
            return results;
        }
    }

    public EmbeddedResults getEmbeddedResults(long reportID, long dataSourceID, List<FilterDefinition> customFilters,
                                              InsightRequestMetadata insightRequestMetadata, @Nullable List<FilterDefinition> drillThroughFilters) {
        return getEmbeddedResults(reportID, dataSourceID, customFilters, insightRequestMetadata, drillThroughFilters, false);
    }

    public EmbeddedResults getEmbeddedResults(long reportID, long dataSourceID, List<FilterDefinition> customFilters,
                                              InsightRequestMetadata insightRequestMetadata, @Nullable List<FilterDefinition> drillThroughFilters, boolean ignoreCache) {
        boolean success = UserThreadMutex.mutex().acquire(SecurityUtil.getUserID(false));
        EIConnection conn = Database.instance().getConnection();
        try {
            long startTime = System.currentTimeMillis();
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
                EmbeddedResults embeddedResults = ReportCache.instance().getResults(dataSourceID, cacheKey, analysisDefinition.getCacheMinutes());
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
                ReportCache.instance().storeReport(dataSourceID, cacheKey, results, analysisDefinition.getCacheMinutes());
            }
            boolean tooManyResults = false;
            if (results instanceof EmbeddedDataResults) {
                EmbeddedDataResults listDataResults = (EmbeddedDataResults) results;
                if (!ignoreCache && analysisDefinition.getGeneralSizeLimit() > 0 && (listDataResults.getRows().length > analysisDefinition.getGeneralSizeLimit())) {
                    tooManyResults = true;
                }
            }
            if (tooManyResults) {
                cacheEmbeddedReportResults(analysisDefinition.getAnalysisID(), (EmbeddedDataResults) results);
                results = truncateEmbeddedResults((EmbeddedDataResults) results, analysisDefinition.getGeneralSizeLimit());
                results.getAdditionalProperties().put("cappedResults", ((EmbeddedDataResults) results).getUid());
            }
            conn.commit();
            long elapsed = System.currentTimeMillis() - startTime;
            long processingTime = elapsed - insightRequestMetadata.getDatabaseTime();
            reportViewBenchmark(analysisDefinition, processingTime, insightRequestMetadata.getDatabaseTime(), conn);
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
            if (success) {
                UserThreadMutex.mutex().release(SecurityUtil.getUserID(false));
            }
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
        DataSet dataSet = reportRetrieval.getPipeline().toDataSet(reportRetrieval.getDataSet());
        if (analysisDefinition.isLogReport()) {
            dataSet.setReportLog(reportRetrieval.getPipeline().toLogString());
        }
        dataSet.setPipelineData(reportRetrieval.getPipeline().getPipelineData());
        return dataSet;
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

    private static void timeshift(Collection<AnalysisItem> items, Collection<FilterDefinition> filters, Feed dataSource, InsightRequestMetadata insightRequestMetadata) {
        for (AnalysisItem item : items) {
            if (item.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                AnalysisDateDimension dateDim = (AnalysisDateDimension) item;
                boolean dateTime = !dateDim.isDateOnlyField() && dataSource.getDataSource().checkDateTime(item.toOriginalDisplayName(), item.getKey());
                if (insightRequestMetadata.isLogReport()) {
                    System.out.println("Setting " + dateDim.toDisplay() + " to timeshift of " + dateTime);
                }
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
        TrendResult trendResult = createTrendOutcomes(analysisDefinition, insightRequestMetadata, conn);
        TrendDataResults trendDataResults = new TrendDataResults();
        trendDataResults.setTrendOutcomes(trendResult.trendOutcomes);
        trendDataResults.setNowString(trendResult.nowString);
        trendDataResults.setPreviousString(trendResult.previousString);
        trendDataResults.setSuggestions(new AnalysisService().generatePossibleIntentions(analysisDefinition, conn, insightRequestMetadata));
        trendDataResults.setDataSourceInfo(trendResult.dataSourceInfo);
        return trendDataResults;
    }

    public EmbeddedTreeDataResults getEmbeddedTreeResults(long reportID, long dataSourceID, List<FilterDefinition> customFilters, InsightRequestMetadata insightRequestMetadata,
                                                          List<FilterDefinition> drillthroughFilters) {
        boolean success = UserThreadMutex.mutex().acquire(SecurityUtil.getUserID(false));
        // get the core data
        EIConnection conn = Database.instance().getConnection();
        try {
            long start = System.currentTimeMillis();
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
            reportViewBenchmark(report, System.currentTimeMillis() - start - insightRequestMetadata.getDatabaseTime(), insightRequestMetadata.getDatabaseTime(), conn);
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
            if (success) {
                UserThreadMutex.mutex().release(SecurityUtil.getUserID(false));
            }
            Database.closeConnection(conn);
        }
    }

    public EmbeddedCompareYearsDataResults getEmbeddedCompareYearsResults(long reportID, long dataSourceID, List<FilterDefinition> customFilters, InsightRequestMetadata insightRequestMetadata,
                                                                          List<FilterDefinition> drillthroughFilters) {
        boolean success = UserThreadMutex.mutex().acquire(SecurityUtil.getUserID(false));
        // get the core data
        EIConnection conn = Database.instance().getConnection();
        try {
            long start = System.currentTimeMillis();
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
            reportViewBenchmark(wsytdDefinition, System.currentTimeMillis() - start - insightRequestMetadata.getDatabaseTime(), insightRequestMetadata.getDatabaseTime(), conn);
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
            if (success) {
                UserThreadMutex.mutex().release(SecurityUtil.getUserID(false));
            }
            Database.closeConnection(conn);
        }
    }

    public CompareYearsDataResults getCompareYearsResults(WSAnalysisDefinition report, InsightRequestMetadata insightRequestMetadata) {
        boolean success = UserThreadMutex.mutex().acquire(SecurityUtil.getUserID(false));
        // get the core data
        EIConnection conn = Database.instance().getConnection();
        try {
            long start = System.currentTimeMillis();
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
            if (success) {
                UserThreadMutex.mutex().release(SecurityUtil.getUserID(false));
            }
            Database.closeConnection(conn);
        }
    }

    public EmbeddedYTDDataResults getEmbeddedYTDResults(long reportID, long dataSourceID, List<FilterDefinition> customFilters, InsightRequestMetadata insightRequestMetadata,
                                                        List<FilterDefinition> drillthroughFilters) {
        boolean success = UserThreadMutex.mutex().acquire(SecurityUtil.getUserID(false));
        // get the core data
        EIConnection conn = Database.instance().getConnection();
        try {
            long start = System.currentTimeMillis();
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
            reportViewBenchmark(wsytdDefinition, System.currentTimeMillis() - start - insightRequestMetadata.getDatabaseTime(), insightRequestMetadata.getDatabaseTime(), conn);
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
            if (success) {
                UserThreadMutex.mutex().release(SecurityUtil.getUserID(false));
            }
            Database.closeConnection(conn);
        }
    }

    public YTDDataResults getYTDResults(WSAnalysisDefinition report, InsightRequestMetadata insightRequestMetadata) {
        boolean success = UserThreadMutex.mutex().acquire(SecurityUtil.getUserID(false));
        // get the core data
        EIConnection conn = Database.instance().getConnection();
        try {
            long start = System.currentTimeMillis();
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
            if (!insightRequestMetadata.isNoLogging()) {
                reportEditorBenchmark(wsytdDefinition, System.currentTimeMillis() - insightRequestMetadata.getDatabaseTime() - start, insightRequestMetadata.getDatabaseTime(), conn);
            }
            return ytdDataResults;
        } catch (ReportException dae) {
            YTDDataResults embeddedDataResults = new YTDDataResults();
            embeddedDataResults.setReportFault(dae.getReportFault());
            return embeddedDataResults;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            if (success) {
                UserThreadMutex.mutex().release(SecurityUtil.getUserID(false));
            }
            Database.closeConnection(conn);
        }
    }

    private static TrendResult createTrendOutcomes(WSKPIDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata, EIConnection conn) throws SQLException {
        SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
        LogClass.info(SecurityUtil.getUserID(false) + " retrieving " + analysisDefinition.getAnalysisID());
        Map<String, List<AnalysisMeasure>> trendMap = new HashMap<String, List<AnalysisMeasure>>();
        Map<String, AnalysisDateDimension> dateMap = new HashMap<String, AnalysisDateDimension>();
        String nowDateFilterName = analysisDefinition.getNowDate();
        String previousDateFilterName = analysisDefinition.getPreviousDate();
        FilterDefinition nowFilter = null;
        FilterDefinition previousFilter = null;
        if (nowDateFilterName != null && previousDateFilterName != null && !"".equals(nowDateFilterName) && !"".equals(previousDateFilterName)) {
            for (FilterDefinition filter : analysisDefinition.getFilterDefinitions()) {
                if (nowDateFilterName.equals(filter.getFilterName())) {
                    nowFilter = filter;
                } else if (previousDateFilterName.equals(filter.getFilterName())) {
                    previousFilter = filter;
                }
            }
        }
        if (previousFilter instanceof RollingFilterDefinition) {
            RollingFilterDefinition rf = (RollingFilterDefinition) previousFilter;
            if (rf.getInterval() == MaterializedRollingFilterDefinition.ALL) {
                previousFilter = null;
            }
        }

        List<FieldRule> rules = FieldRule.load(conn, analysisDefinition.getDataFeedID());

        Map<String, List<Tag>> fieldMap = new HashMap<String, List<Tag>>();
        Feed feed = FeedRegistry.instance().getFeed(analysisDefinition.getDataFeedID(), conn);
        for (AnalysisItem field : feed.getFields()) {
            if (field.getTags() != null) {
                fieldMap.put(field.toOriginalDisplayName(), field.getTags());
            }
        }
        for (AnalysisItem analysisItem : analysisDefinition.getMeasures()) {
            analysisItem.setTags(fieldMap.get(analysisItem.toOriginalDisplayName()));
        }

        for (AnalysisItem analysisItem : analysisDefinition.getMeasures()) {
            for (FieldRule rule : rules) {
                if (rule.matches(analysisItem)) {
                    rule.update(analysisItem, analysisDefinition, insightRequestMetadata);
                }
            }
        }

        List<AnalysisItem> allItems = new ArrayList<AnalysisItem>(feed.getFields());
        if (analysisDefinition.getAddedItems() != null) {
            allItems.addAll(analysisDefinition.getAddedItems());
        }

        for (AnalysisItem analysisItem : analysisDefinition.getMeasures()) {

            if (nowFilter != null) {
                AnalysisDateDimension date = null;
                if (analysisItem.getReportFieldExtension() != null && analysisItem.getReportFieldExtension() instanceof TrendReportFieldExtension) {
                    TrendReportFieldExtension trendReportFieldExtension = (TrendReportFieldExtension) analysisItem.getReportFieldExtension();
                    if (trendReportFieldExtension.getDate() != null) {
                        date = (AnalysisDateDimension) trendReportFieldExtension.getDate();
                    }
                }
                if (date == null) {
                    if (analysisItem.getDefaultDate() != null) {
                        for (AnalysisItem item : allItems) {
                            if (item.hasType(AnalysisItemTypes.DATE_DIMENSION) && (item.toDisplay().equals(analysisItem.getDefaultDate()) || item.toOriginalDisplayName().equals(analysisItem.getDefaultDate()))) {
                                date = (AnalysisDateDimension) item;
                                break;
                            }
                        }
                    }
                }
                if (date == null) {
                    for (AnalysisItem item : allItems) {
                        if (item.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                            date = (AnalysisDateDimension) item;
                            break;
                        }
                    }
                }
                if (date == null) {
                    throw new ReportException(new GenericReportFault("You need to choose a date for " + analysisItem.toDisplay() + " for trend analysis to work."));
                }
                dateMap.put(date.toDisplay(), date);
                List<AnalysisMeasure> measures = trendMap.get(date.toDisplay());
                if (measures == null) {
                    measures = new ArrayList<AnalysisMeasure>();
                    trendMap.put(date.toDisplay(), measures);
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
        }
        List<TrendOutcome> trendOutcomes = new ArrayList<TrendOutcome>();
        DataSourceInfo dataSourceInfo = null;
        String nowString = null;
        String previousString = null;
        for (Map.Entry<String, List<AnalysisMeasure>> entry : trendMap.entrySet()) {
            String key = entry.getKey();
            List<AnalysisMeasure> measures = entry.getValue();
            WSListDefinition tempReport = new WSListDefinition();
            List<AnalysisItem> columns = new ArrayList<AnalysisItem>();
            columns.addAll(measures);
            if (analysisDefinition.getGroupings() != null) {
                columns.addAll(analysisDefinition.getGroupings());
            }
            if (nowFilter != null) {
                List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
                for (FilterDefinition filter : analysisDefinition.getFilterDefinitions()) {
                    if (filter != nowFilter && filter != previousFilter) {
                        filters.add(filter);
                    }
                }
                FilterDefinition nowFilterClone;
                try {
                    nowFilterClone = nowFilter.clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
                nowFilterClone.setField(dateMap.get(key));
                filters.add(nowFilterClone);
                tempReport.setFilterDefinitions(filters);
            } else {
                tempReport.setFilterDefinitions(analysisDefinition.getFilterDefinitions());
            }
            tempReport.setColumns(columns);
            tempReport.setDataFeedID(analysisDefinition.getDataFeedID());
            tempReport.setAddedItems(analysisDefinition.getAddedItems());
            tempReport.setMarmotScript(analysisDefinition.getMarmotScript());
            tempReport.setBaseDate(analysisDefinition.getBaseDate());
            tempReport.setReportRunMarmotScript(analysisDefinition.getReportRunMarmotScript());
            tempReport.setAddonReports(analysisDefinition.getAddonReports());
            tempReport.setJoinOverrides(analysisDefinition.getJoinOverrides());
            InsightRequestMetadata metadata = new InsightRequestMetadata();
            metadata.setUtcOffset(insightRequestMetadata.getUtcOffset());
            ReportRetrieval reportRetrievalNow = ReportRetrieval.reportEditor(metadata, tempReport, conn);
            dataSourceInfo = reportRetrievalNow.getDataSourceInfo();
            DataSet nowSet = reportRetrievalNow.getPipeline().toDataSet(reportRetrievalNow.getDataSet());
            int limit = 0;
            if (analysisDefinition instanceof WSTrendGridDefinition) {
                WSTrendGridDefinition trendGridDefinition = (WSTrendGridDefinition) analysisDefinition;
                limit = trendGridDefinition.getMaxRecords();
            }
            List<Value> aggregationValues = null;
            if (limit > 0 && analysisDefinition.getGroupings().size() > 0) {
                final Map<Value, Value> ranks = new HashMap<Value, Value>();
                for (IRow row : nowSet.getRows()) {
                    Value value = row.getValue(analysisDefinition.getGroupings().get(0).createAggregateKey());
                    Value measureValue = row.getValue(analysisDefinition.getMeasures().get(0).createAggregateKey());
                    ranks.put(value, measureValue);
                }

                aggregationValues = new ArrayList<Value>(ranks.keySet());
                if (aggregationValues.size() > limit) {
                    Collections.sort(aggregationValues, new Comparator<Value>() {

                        public int compare(Value value, Value value1) {
                            return ranks.get(value1).toDouble().compareTo(ranks.get(value).toDouble());
                        }
                    });

                    Aggregation other = new AggregationFactory((AnalysisMeasure) analysisDefinition.getMeasures().get(0), false).getAggregation();

                    aggregationValues = aggregationValues.subList(0, limit);
                    Iterator<IRow> iter = nowSet.getRows().iterator();
                    while (iter.hasNext()) {
                        IRow row = iter.next();
                        Value value = row.getValue(analysisDefinition.getGroupings().get(0).createAggregateKey());
                        if (!aggregationValues.contains(value)) {
                            iter.remove();
                            other.addValue(row.getValue(analysisDefinition.getMeasures().get(0)));
                        }
                    }
                    IRow otherRow = nowSet.createRow();
                    otherRow.addValue(analysisDefinition.getGroupings().get(0).createAggregateKey(), "Other");
                    otherRow.addValue(analysisDefinition.getMeasures().get(0).createAggregateKey(), other.getValue());
                }
            }
            DataSet pastSet;
            if (previousFilter != null) {

                List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
                for (FilterDefinition filter : analysisDefinition.getFilterDefinitions()) {
                    if (filter != nowFilter && filter != previousFilter) {
                        filters.add(filter);
                    }
                }
                FilterDefinition previousFilterClone;
                try {
                    previousFilterClone = previousFilter.clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
                previousFilterClone.setField(dateMap.get(key));
                filters.add(previousFilterClone);
                tempReport.setFilterDefinitions(filters);
                metadata = new InsightRequestMetadata();
                metadata.setUtcOffset(insightRequestMetadata.getUtcOffset());
                reportRetrievalNow = ReportRetrieval.reportEditor(metadata, tempReport, conn);
                dataSourceInfo = reportRetrievalNow.getDataSourceInfo();
                pastSet = reportRetrievalNow.getPipeline().toDataSet(reportRetrievalNow.getDataSet());

                if (limit > 0 && analysisDefinition.getGroupings().size() > 0) {
                    Aggregation other = new AggregationFactory((AnalysisMeasure) analysisDefinition.getMeasures().get(0), false).getAggregation();
                    Iterator<IRow> iter = pastSet.getRows().iterator();
                    while (iter.hasNext()) {
                        IRow row = iter.next();
                        Value value = row.getValue(analysisDefinition.getGroupings().get(0).createAggregateKey());
                        if (!aggregationValues.contains(value)) {
                            iter.remove();
                            other.addValue(row.getValue(analysisDefinition.getMeasures().get(0)));
                        }
                    }
                    IRow otherRow = pastSet.createRow();
                    otherRow.addValue(analysisDefinition.getGroupings().get(0).createAggregateKey(), "Other");
                    otherRow.addValue(analysisDefinition.getMeasures().get(0).createAggregateKey(), other.getValue());
                }

                nowString = nowFilter.asString(insightRequestMetadata);
                previousString = previousFilter.asString(insightRequestMetadata);
            } else if (nowFilter != null) {
                pastSet = new DataSet();
            } else {
                pastSet = nowSet;
            }
            trendOutcomes.addAll(new Trend().calculateTrends(measures, analysisDefinition.getGroupings(), nowSet, pastSet));
        }
        return new TrendResult(new ArrayList<TrendOutcome>(trendOutcomes), dataSourceInfo, nowString, previousString);
    }

    private static class TrendResult {
        private List<TrendOutcome> trendOutcomes;
        private DataSourceInfo dataSourceInfo;
        private String nowString;
        private String previousString;

        private TrendResult(List<TrendOutcome> trendOutcomes, DataSourceInfo dataSourceInfo, String nowString, String previousString) {
            this.trendOutcomes = trendOutcomes;
            this.dataSourceInfo = dataSourceInfo;
            this.nowString = nowString;
            this.previousString = previousString;
        }
    }

    public TrendDataResults getTrendDataResults(WSKPIDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) {
        boolean success = UserThreadMutex.mutex().acquire(SecurityUtil.getUserID(false));
        EIConnection conn = Database.instance().getConnection();
        try {
            long start = System.currentTimeMillis();
            TrendResult trendResult = createTrendOutcomes(analysisDefinition, insightRequestMetadata, conn);
            TrendDataResults trendDataResults = new TrendDataResults();
            trendDataResults.setTrendOutcomes(trendResult.trendOutcomes);
            trendDataResults.setSuggestions(new AnalysisService().generatePossibleIntentions(analysisDefinition, conn, insightRequestMetadata));
            trendDataResults.setDataSourceInfo(trendResult.dataSourceInfo);
            trendDataResults.setNowString(trendResult.nowString);
            trendDataResults.setPreviousString(trendResult.previousString);
            if (!insightRequestMetadata.isNoLogging()) {
                reportEditorBenchmark(analysisDefinition, System.currentTimeMillis() - insightRequestMetadata.getDatabaseTime() - start, insightRequestMetadata.getDatabaseTime(), conn);
            }
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
            if (success) {
                UserThreadMutex.mutex().release(SecurityUtil.getUserID(false));
            }
            Database.closeConnection(conn);
        }
    }

    public TreeDataResults getTreeDataResults(WSTreeDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) {
        boolean success = UserThreadMutex.mutex().acquire(SecurityUtil.getUserID(false));
        EIConnection conn = Database.instance().getConnection();
        try {
            long start = System.currentTimeMillis();
            SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
            LogClass.info(SecurityUtil.getUserID(false) + " retrieving " + analysisDefinition.getAnalysisID());
            ReportRetrieval reportRetrieval = ReportRetrieval.reportEditor(insightRequestMetadata, analysisDefinition, conn);

            DataSet dataSet = reportRetrieval.getPipeline().toDataSet(reportRetrieval.getDataSet());
            //DataSet dataSet = listDataSet(analysisDefinition, insightRequestMetadata, conn);
            TreeData treeData = new TreeData(analysisDefinition, (AnalysisHierarchyItem) analysisDefinition.getHierarchy(), null, dataSet);
            for (IRow row : dataSet.getRows()) {
                treeData.addRow(row);
            }
            List<TreeRow> rows = treeData.toTreeRows(reportRetrieval.getPipeline().getPipelineData());
            TreeDataResults crossTabDataResults = new TreeDataResults();
            crossTabDataResults.setTreeRows(rows);
            crossTabDataResults.setSuggestions(new AnalysisService().generatePossibleIntentions(analysisDefinition, conn, insightRequestMetadata));
            crossTabDataResults.setDataSourceInfo(reportRetrieval.getDataSourceInfo());
            if (!insightRequestMetadata.isNoLogging()) {
                reportEditorBenchmark(analysisDefinition, System.currentTimeMillis() - insightRequestMetadata.getDatabaseTime() - start, insightRequestMetadata.getDatabaseTime(), conn);
            }
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
            if (success) {
                UserThreadMutex.mutex().release(SecurityUtil.getUserID(false));
            }
            Database.closeConnection(conn);
        }
    }

    public CrossTabDataResults getCrosstabDataResults(WSCrosstabDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) {
        boolean success = UserThreadMutex.mutex().acquire(SecurityUtil.getUserID(false));
        EIConnection conn = Database.instance().getConnection();
        try {
            long start = System.currentTimeMillis();
            SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
            LogClass.info(SecurityUtil.getUserID(false) + " retrieving " + analysisDefinition.getAnalysisID());
            ReportRetrieval reportRetrieval = ReportRetrieval.reportEditor(insightRequestMetadata, analysisDefinition, conn);
            Crosstab crosstab = new Crosstab();


            crosstab.crosstab(analysisDefinition, reportRetrieval.getPipeline().toDataSet(reportRetrieval.getDataSet()));
            CrosstabValue[][] values = crosstab.toTable(analysisDefinition, insightRequestMetadata, conn);

            List<CrosstabMapWrapper> resultData = new ArrayList<CrosstabMapWrapper>();
            int rowOffset = analysisDefinition.getMeasures().size() > 1 ? 3 : 2;
            for (int j = 0; j < (crosstab.getRowSections().size() + analysisDefinition.getColumns().size()) + rowOffset; j++) {
                if (analysisDefinition.isExcludeZero()) {
                    CrosstabValue summaryValue = values[j][((crosstab.getColumnSections().size() * analysisDefinition.getMeasures().size()) + analysisDefinition.getRows().size())];
                    if (summaryValue != null && summaryValue.getValue() != null && summaryValue.getValue().toDouble() != null && summaryValue.getValue().toDouble() == 0) {
                        continue;
                    }
                }
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
            crossTabDataResults.setSuggestions(new AnalysisService().generatePossibleIntentions(analysisDefinition, conn, insightRequestMetadata));
            crossTabDataResults.setDataSourceInfo(reportRetrieval.getDataSourceInfo());
            if (!insightRequestMetadata.isNoLogging()) {
                reportEditorBenchmark(analysisDefinition, System.currentTimeMillis() - insightRequestMetadata.getDatabaseTime() - start, insightRequestMetadata.getDatabaseTime(), conn);
            }
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
            if (success) {
                UserThreadMutex.mutex().release(SecurityUtil.getUserID(false));
            }
            Database.closeConnection(conn);
        }
    }



    private Map<String, DataResults> simpleCache = new WeakHashMap<String, DataResults>();
    private Map<String, EmbeddedDataResults> simpleEmbeddedCache = new WeakHashMap<String, EmbeddedDataResults>();

    private String cacheReportResults(long reportID, DataResults dataResults) {
        String uid = reportID + String.valueOf(System.currentTimeMillis());
        dataResults.setUid(uid);
        simpleCache.put(uid, dataResults);
        /*MemcachedClient client = MemCachedManager.instance();
        client.add(uid, 0, dataResults);*/
        return uid;
    }

    private DataResults truncateResults(DataResults dataResults, int limit) throws CloneNotSupportedException {
        ListDataResults listDataResults = (ListDataResults) dataResults;
        ListDataResults copyResults = (ListDataResults) listDataResults.clone();
        ListRow[] truncatedRows = new ListRow[limit];
        System.arraycopy(listDataResults.getRows(), 0, truncatedRows, 0, limit);
        copyResults.setRows(truncatedRows);
        return copyResults;
    }

    public DataResults moreResults(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata, String uid) {
        //MemcachedClient client = MemCachedManager.instance();
        //DataResults results = (DataResults) client.get(uid);
        DataResults results = simpleCache.get(uid);
        if (results == null) {
            return list(analysisDefinition, insightRequestMetadata, true);
        } else {
            results.getAdditionalProperties().put("cappedResults", null);
            return results;
        }
    }

    public DataResults list(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) {
        return list(analysisDefinition, insightRequestMetadata, false);
    }

    public DataResults list(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata, boolean ignoreCache) {
        boolean success;
        try {
            success = UserThreadMutex.mutex().acquire(SecurityUtil.getUserID(false));
        } catch (ReportException e) {
            ListDataResults embeddedDataResults = new ListDataResults();
            embeddedDataResults.setReportFault(new ServerError(e.getMessage()));
            return embeddedDataResults;
        }
        EIConnection conn = Database.instance().getConnection();
        try {
            long startTime = System.currentTimeMillis();
            SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
            LogClass.info(SecurityUtil.getUserID(false) + " retrieving " + analysisDefinition.getAnalysisID());
            ReportRetrieval reportRetrieval = ReportRetrieval.reportEditor(insightRequestMetadata, analysisDefinition, conn);
            List<ReportAuditEvent> events = new ArrayList<ReportAuditEvent>();
            events.addAll(reportRetrieval.getDataSet().getAudits());
            DataResults results = reportRetrieval.getPipeline().toList(reportRetrieval.getDataSet(), conn, reportRetrieval.aliases);
            boolean tooManyResults = false;
            if (results instanceof ListDataResults) {
                ListDataResults listDataResults = (ListDataResults) results;
                if (!ignoreCache && analysisDefinition.getGeneralSizeLimit() > 0 && (listDataResults.getRows().length > analysisDefinition.getGeneralSizeLimit())) {
                    tooManyResults = true;
                }
            }
            List<IntentionSuggestion> suggestions = insightRequestMetadata.generateSuggestions();
            if (analysisDefinition.isLogReport()) {
                results.setReportLog(reportRetrieval.getPipeline().toLogString());
            }
            for (ReportAuditEvent auditEvent : events) {
                if (ReportAuditEvent.WARNING.equals(auditEvent.getAuditType())) {
                    insightRequestMetadata.getWarnings().add(auditEvent.getEventLabel());
                }
            }
            results.setDataSourceInfo(reportRetrieval.getDataSourceInfo());
            suggestions.addAll(new AnalysisService().generatePossibleIntentions(analysisDefinition, conn, insightRequestMetadata));
            if (tooManyResults) {
                cacheReportResults(analysisDefinition.getAnalysisID(), results);
                results = truncateResults(results, analysisDefinition.getGeneralSizeLimit());
                results.getAdditionalProperties().put("cappedResults", results.getUid());
            }
            results.setSuggestions(suggestions);
            long elapsed = System.currentTimeMillis() - startTime;
            long processingTime = elapsed - insightRequestMetadata.getDatabaseTime();
            results.setProcessingTime(processingTime);
            results.setDatabaseTime(insightRequestMetadata.getDatabaseTime());
            results.setFieldEvents(insightRequestMetadata.getFieldAudits());
            results.setFilterEvents(insightRequestMetadata.getFilterAudits());
            if (insightRequestMetadata.isLogReport()) {
                results.setAuditMessages(events);
            }
            if (!insightRequestMetadata.isNoLogging()) {
                reportEditorBenchmark(analysisDefinition, processingTime, insightRequestMetadata.getDatabaseTime(), conn);
            }
            results.setReport(analysisDefinition);

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
            if (success) {
                UserThreadMutex.mutex().release(SecurityUtil.getUserID(false));
            }
            Database.closeConnection(conn);
        }
    }

    private void reportEditorBenchmark(WSAnalysisDefinition analysisDefinition, long processingTime, long databaseTime, EIConnection conn) {
        if (analysisDefinition.getAnalysisID() == 0) {
            BenchmarkManager.recordBenchmarkForDataSource("ReportEditorProcessingTime", processingTime, SecurityUtil.getUserID(false), analysisDefinition.getDataFeedID(), conn);
            BenchmarkManager.recordBenchmarkForDataSource("ReportEditorDatabaseTime", databaseTime, SecurityUtil.getUserID(false), analysisDefinition.getDataFeedID(), conn);
        } else {
            BenchmarkManager.recordBenchmarkForReport("ReportEditorProcessingTime", processingTime, SecurityUtil.getUserID(false), analysisDefinition.getAnalysisID(), conn);
            BenchmarkManager.recordBenchmarkForReport("ReportEditorDatabaseTime", databaseTime, SecurityUtil.getUserID(false), analysisDefinition.getAnalysisID(), conn);
        }
    }

    private void reportViewBenchmark(WSAnalysisDefinition analysisDefinition, long processingTime, long databaseTime, EIConnection conn) {
        if (analysisDefinition.getAnalysisID() == 0) {
            BenchmarkManager.recordBenchmarkForDataSource("ReportViewProcessingTime", processingTime, SecurityUtil.getUserID(false), analysisDefinition.getDataFeedID(), conn);
            BenchmarkManager.recordBenchmarkForDataSource("ReportViewDatabaseTime", databaseTime, SecurityUtil.getUserID(false), analysisDefinition.getDataFeedID(), conn);
        } else {
            BenchmarkManager.recordBenchmarkForReport("ReportViewProcessingTime", processingTime, SecurityUtil.getUserID(false), analysisDefinition.getAnalysisID(), conn);
            BenchmarkManager.recordBenchmarkForReport("ReportViewDatabaseTime", databaseTime, SecurityUtil.getUserID(false), analysisDefinition.getAnalysisID(), conn);
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

            if (analysisDefinition.isPassThroughFilters()) {
                Map<Long, FilterDefinition> map = new HashMap<Long, FilterDefinition>();
                for (FilterDefinition filter : analysisDefinition.getFilterDefinitions()) {
                    map.put(filter.getFilterID(), filter);
                }
                List<FilterDefinition> toSet = new ArrayList<FilterDefinition>();
                List<FilterDefinition> toPass = new ArrayList<FilterDefinition>();
                if (customFilters != null) {
                    for (FilterDefinition filter : customFilters) {
                        FilterDefinition inMap = map.get(filter.getFilterID());
                        if (inMap != null) {
                            toSet.add(filter);
                        } else {
                            toPass.add(filter);
                        }
                    }
                }
                insightRequestMetadata.setFilters(toPass);
                analysisDefinition.setFilterDefinitions(toSet);
            } else if (customFilters != null) {
                analysisDefinition.setFilterDefinitions(customFilters);
            }
            if (drillThroughFilters != null) {
                analysisDefinition.applyFilters(drillThroughFilters);
            }
            ReportRetrieval reportRetrieval = new ReportRetrieval(insightRequestMetadata, analysisDefinition, conn).toPipeline();
            // TODO: get this code out of EI entirely
            boolean acsType = ((reportRetrieval.getFeed().getFeedType().getType() == FeedType.DEFAULT.getType() || reportRetrieval.getFeed().getFeedType().getType() == FeedType.STATIC.getType()) && reportRetrieval.getFeed().getName().contains("Survey")) ||
                    "ACS2".equals(reportRetrieval.getFeed().getName()) || "Therapy Works".equals(reportRetrieval.getFeed().getName()) || "ACS3".equals(reportRetrieval.getFeed().getName());
            if (acsType) {
                String personaName = SecurityUtil.getPersonaName();
                if ("Therapist".equals(personaName) || "Director".equals(personaName) || "CEO".equals(personaName) || "MLDirector".equals(personaName)) {
                    acsType = false;
                }
            }
            analysisDefinition.setRowsEditable(acsType);
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

            insightRequestMetadata.setAddonReports(analysisDefinition.getAddonReports());
            insightRequestMetadata.setNoDataOnNoJoin(analysisDefinition.isNoDataOnNoJoin());
            insightRequestMetadata.setLogReport(analysisDefinition.isLogReport());

            if (analysisDefinition.getBaseDate() != null && !"".equals(analysisDefinition.getBaseDate())) {
                for (AnalysisItem item : analysisDefinition.getAddedItems()) {
                    if (item.toDisplay().equals(analysisDefinition.getBaseDate())) {
                        insightRequestMetadata.setBaseDate((AnalysisDateDimension) item);
                    }
                }
            }

            if (insightRequestMetadata.getHierarchyOverrides() != null) {
                for (AnalysisItemOverride hierarchyOverride : insightRequestMetadata.getHierarchyOverrides()) {
                    hierarchyOverride.apply(analysisDefinition.getAllAnalysisItems());
                }
            }

            for (FilterDefinition filterDefinition : analysisDefinition.getFilterDefinitions()) {
                if (filterDefinition.isDrillthrough() && filterDefinition.getField() != null) {
                    Key key = filterDefinition.getField().getKey();
                    if (key instanceof ReportKey) {
                        ReportKey reportKey = (ReportKey) key;
                        boolean valid = false;
                        for (AddonReport addonReport : analysisDefinition.getAddonReports()) {
                            if (addonReport.getReportID() == reportKey.getReportID()) {
                                valid = true;
                                break;
                            }
                        }
                        if (!valid) {
                            insightRequestMetadata.getSuppressedFilters().add(filterDefinition);
                        }
                    }
                }
            }

            List<FilterDefinition> dlsFilters = addDLSFilters(analysisDefinition.getDataFeedID(), conn);
            analysisDefinition.getFilterDefinitions().addAll(dlsFilters);

            analysisDefinition.applyStyling(conn, feed.getFeedType().getType());

            Set<AnalysisItem> fieldsReplaced = new HashSet<AnalysisItem>();
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
                    if (analysisItemFilterDefinition.isEnabled()) {

                        Map<String, AnalysisItem> structure = analysisDefinition.createStructure();
                        Map<String, AnalysisItem> structureCopy = new HashMap<String, AnalysisItem>(structure);
                        for (Map.Entry<String, AnalysisItem> entry : structureCopy.entrySet()) {
                            if (entry.getValue() instanceof AnalysisHierarchyItem) {
                                continue;
                            }
                            if (entry.getValue().toDisplay().equals(filter.getField().toDisplay())) {
                                if (!fieldsReplaced.contains(entry.getValue())) {
                                    if (insightRequestMetadata.getBaseDate() != null && insightRequestMetadata.getBaseDate().qualifiedName().equals(filter.getField().qualifiedName())) {
                                        insightRequestMetadata.setBaseDate((AnalysisDateDimension) analysisItemFilterDefinition.getTargetItem());
                                    }
                                    structure.put(entry.getKey(), analysisItemFilterDefinition.getTargetItem());
                                    fieldsReplaced.add(entry.getValue());
                                }
                            }
                        }
                        analysisDefinition.populateFromReportStructure(structure);
                        for (AnalysisItem item : analysisDefinition.createStructure().values()) {
                            if (item.toDisplay().equals(filter.getField().toDisplay()) && item instanceof AnalysisHierarchyItem) {
                                AnalysisHierarchyItem analysisHierarchyItem = (AnalysisHierarchyItem) item;
                                HierarchyLevel targetLevel = null;
                                for (HierarchyLevel level : analysisHierarchyItem.getHierarchyLevels()) {
                                    if (level.getAnalysisItem().toDisplay().equals(analysisItemFilterDefinition.getTargetItem().toDisplay())) {
                                        targetLevel = level;
                                    }
                                }
                                analysisHierarchyItem.setHierarchyLevel(targetLevel);
                            }
                        }
                    }
                } else if (filter instanceof MultiFieldFilterDefinition) {
                    MultiFieldFilterDefinition multiFieldFilterDefinition = (MultiFieldFilterDefinition) filter;
                    if (multiFieldFilterDefinition.isEnabled()) {
                        analysisDefinition.multiField(multiFieldFilterDefinition);
                    }
                }
            }

            for (FilterDefinition filter : new ArrayList<FilterDefinition>(analysisDefinition.getFilterDefinitions())) {
                String obj = filter.getParentChildLabel();
                if (obj != null) {
                    Iterator<FilterDefinition> iter = analysisDefinition.getFilterDefinitions().iterator();
                    while (iter.hasNext()) {
                        FilterDefinition existingFilter = iter.next();
                        String child = existingFilter.getChildToParentLabel();
                        if (child != null && child.equals(obj)) {
                            iter.remove();
                            filter.setField(existingFilter.getField());
                        }
                    }
                }
            }

            analysisDefinition.tweakReport(aliases);

            List<AnalysisItem> allFields = new ArrayList<AnalysisItem>(feed.getFields());

            Iterator<AnalysisItem> iter = allFields.iterator();
            while (iter.hasNext()) {
                AnalysisItem item = iter.next();
                if (item.getOrigin() != null) {
                    if (item.getOrigin().getReport() == analysisDefinition.getAnalysisID()) {
                        iter.remove();
                    }
                }
            }

            allFields.addAll(analysisDefinition.allAddedItems(insightRequestMetadata));



            KeyDisplayMapper mapper = KeyDisplayMapper.create(allFields);
            Map<String, List<AnalysisItem>> keyMap = mapper.getKeyMap();
            Map<String, List<AnalysisItem>> displayMap = mapper.getDisplayMap();
            Map<String, List<AnalysisItem>> unqualifiedDisplayMap = mapper.getUnqualifiedDisplayMap();


            if (analysisDefinition.getMarmotScript() != null) {
                StringTokenizer toker = new StringTokenizer(analysisDefinition.getMarmotScript(), "\r\n");
                while (toker.hasMoreTokens()) {
                    String line = toker.nextToken();
                    if (!FunctionFactory.functionRunsOnReportLoad(line)) {
                        try {
                            new ReportCalculation(line).apply(analysisDefinition, allFields, keyMap, displayMap, unqualifiedDisplayMap, feed, conn, dlsFilters, insightRequestMetadata);
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
            }
            if (feed.getDataSource().getMarmotScript() != null) {
                StringTokenizer toker = new StringTokenizer(feed.getDataSource().getMarmotScript(), "\r\n");
                while (toker.hasMoreTokens()) {
                    String line = toker.nextToken();
                    try {
                        new ReportCalculation(line).apply(analysisDefinition, allFields, keyMap, displayMap, unqualifiedDisplayMap, feed, conn, dlsFilters, insightRequestMetadata);
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

            Set<AnalysisItem> items = analysisDefinition.getAllAnalysisItems();

            List<FieldRule> rules = FieldRule.load(conn, analysisDefinition.getDataFeedID());

            Map<String, List<Tag>> fieldMap = new HashMap<String, List<Tag>>();
            for (AnalysisItem field : feed.getFields()) {
                if (field.getTags() != null) {
                    fieldMap.put(field.toOriginalDisplayName(), field.getTags());
                }
            }

            if (analysisDefinition.getAnalysisID() > 0 && analysisDefinition.isDataDiscoveryEnabled()) {
                for (AnalysisItem analysisItem : items) {
                    if (analysisItem.hasType(AnalysisItemTypes.HIERARCHY)) {
                        AnalysisHierarchyItem hierarchyItem = (AnalysisHierarchyItem) analysisItem;
                        if (hierarchyItem.getHierarchyLevels().indexOf(hierarchyItem.getHierarchyLevel()) == (hierarchyItem.getHierarchyLevels().size() - 1)) {

                        } else {
                            DrillThrough drillThrough = new DrillThrough();
                            AnalysisItemHandle handle = new AnalysisItemHandle();
                            handle.setName(hierarchyItem.getHierarchyLevel().getAnalysisItem().toDisplay());
                            drillThrough.setPassThroughField(handle);
                            drillThrough.setReportID(analysisDefinition.getAnalysisID());
                            drillThrough.setDefaultLink(true);
                            drillThrough.setLabel("hierarchy");
                            drillThrough.setShowDrillThroughFilters(false);
                            analysisItem.setLinks(Arrays.asList((Link) drillThrough));
                        }
                    } else if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                        AnalysisDateDimension date = (AnalysisDateDimension) analysisItem;
                        if (date.getDateLevel() < AnalysisDateDimension.DAY_LEVEL) {
                            DrillThrough drillThrough = new DrillThrough();
                            drillThrough.setReportID(analysisDefinition.getAnalysisID());
                            drillThrough.setFilterRowGroupings(true);
                            drillThrough.setAddAllFilters(true);
                            drillThrough.setDefaultLink(true);
                            drillThrough.setLabel("date");
                            drillThrough.setShowDrillThroughFilters(false);
                            analysisItem.setLinks(Arrays.asList((Link) drillThrough));
                        }
                    }
                }
            }

            for (AnalysisItem analysisItem : items) {
                analysisItem.setTags(fieldMap.get(analysisItem.toOriginalDisplayName()));
                if (analysisItem.hasType(AnalysisItemTypes.HIERARCHY)) {
                    AnalysisHierarchyItem hierarchyItem = (AnalysisHierarchyItem) analysisItem;
                    hierarchyItem.getHierarchyLevel().getAnalysisItem().setTags(fieldMap.get(hierarchyItem.getHierarchyLevel().getAnalysisItem().toOriginalDisplayName()));
                }
            }

            for (AnalysisItem analysisItem : items) {
                for (FieldRule rule : rules) {
                    if (rule.matches(analysisItem)) {
                        rule.update(analysisItem, analysisDefinition, insightRequestMetadata);
                    }
                    if (analysisItem.hasType(AnalysisItemTypes.HIERARCHY)) {
                        AnalysisHierarchyItem hierarchyItem = (AnalysisHierarchyItem) analysisItem;
                        if (rule.matches(hierarchyItem.getHierarchyLevel().getAnalysisItem())) {
                            rule.update(hierarchyItem, analysisDefinition, insightRequestMetadata);
                        }
                    }
                }
            }

            analysisDefinition.argh();

            feed.getDataSource().decorateLinks(new ArrayList<AnalysisItem>(items));

            AnalysisItemRetrievalStructure structure = new AnalysisItemRetrievalStructure(null);
            structure.setReport(analysisDefinition);

            structure.setConn(conn);
            structure.setInsightRequestMetadata(insightRequestMetadata);

            insightRequestMetadata.setAllItems(allFields);
            insightRequestMetadata.setStructure(structure);

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
                            analysisItem.setFormattingType(field.getFormattingType());
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

            /*try {
                boolean fieldLookupEnabled = feed.getDataSource().isFieldLookupEnabled();

                if (fieldLookupEnabled) {
                    Map<String, AnalysisItem> map = new HashMap<String, AnalysisItem>();
                    for (AnalysisItem field : feed.getFields()) {
                        map.put(field.toDisplay(), field);
                    }
                    for (AnalysisItem analysisItem : analysisItems) {
                        if (analysisItem.isConcrete()) {
                            AnalysisItem dataSourceField = map.get(analysisItem.toOriginalDisplayName());
                            if (dataSourceField != null) {
                                if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION) && dataSourceField.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                                    AnalysisDateDimension dateDimension = (AnalysisDateDimension) analysisItem;
                                    AnalysisDateDimension parentDimension = (AnalysisDateDimension) dataSourceField;
                                    dateDimension.setDateOnlyField(parentDimension.isDateOnlyField());
                                } else if (analysisItem.hasType(AnalysisItemTypes.CALCULATION) && dataSourceField.hasType(AnalysisItemTypes.CALCULATION)) {
                                    AnalysisCalculation analysisCalculation = (AnalysisCalculation) analysisItem;
                                    AnalysisCalculation dataSourceCalculation = (AnalysisCalculation) dataSourceField;
                                    analysisCalculation.setApplyBeforeAggregation(dataSourceCalculation.isApplyBeforeAggregation());
                                    analysisCalculation.setCalculationString(dataSourceCalculation.getCalculationString());
                                } else if (analysisItem.hasType(AnalysisItemTypes.DERIVED_DIMENSION) && dataSourceField.hasType(AnalysisItemTypes.DERIVED_DIMENSION)) {
                                    DerivedAnalysisDimension analysisCalculation = (DerivedAnalysisDimension) analysisItem;
                                    DerivedAnalysisDimension dataSourceCalculation = (DerivedAnalysisDimension) dataSourceField;
                                    analysisCalculation.setApplyBeforeAggregation(dataSourceCalculation.isApplyBeforeAggregation());
                                    analysisCalculation.setDerivationCode(dataSourceCalculation.getDerivationCode());
                                } else if (analysisItem.hasType(AnalysisItemTypes.DERIVED_DATE) && dataSourceField.hasType(AnalysisItemTypes.DERIVED_DATE)) {
                                    DerivedAnalysisDateDimension analysisCalculation = (DerivedAnalysisDateDimension) analysisItem;
                                    DerivedAnalysisDateDimension dataSourceCalculation = (DerivedAnalysisDateDimension) dataSourceField;
                                    analysisCalculation.setApplyBeforeAggregation(dataSourceCalculation.isApplyBeforeAggregation());
                                    analysisCalculation.setDerivationCode(dataSourceCalculation.getDerivationCode());
                                }
                            }
                        }
                    }
                    for (AnalysisItem analysisItem : analysisItems) {
                        if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                            AnalysisDateDimension dateDimension = (AnalysisDateDimension) analysisItem;
                            feed.originalField(dateDimension.getKey(), dateDimension);
                        }
                    }
                }
            } catch (Exception e) {
                LogClass.error(e);
            }*/

            Set<AnalysisItem> validQueryItems = new HashSet<AnalysisItem>();

            for (AnalysisItem analysisItem : analysisItems) {
                validQueryItems.add(analysisItem);
                if (analysisItem.getFilters() != null) {
                    analysisItem.populateNamedFilters(analysisDefinition.getFilterDefinitions());
                    for (FilterDefinition filterDefinition : analysisItem.getFilters()) {
                        filterDefinition.applyCalculationsBeforeRun(analysisDefinition, allFields, keyMap, displayMap, feed, conn, dlsFilters, insightRequestMetadata);
                    }
                }
            }


            boolean aggregateQuery = analysisDefinition.isAggregateQueryIfPossible();

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
            } else if ("�".equals(symbol)) {
                currency = "EUR";
            } else if ("�".equals(symbol) || "?".equals(symbol)) {
                currency = "GBP";
            }
            insightRequestMetadata.setTargetCurrency(currency);
            insightRequestMetadata.setAggregateQuery(aggregateQuery);
            insightRequestMetadata.setReportItems(analysisDefinition.getAllAnalysisItems());
            Collection<FilterDefinition> filters = analysisDefinition.retrieveFilterDefinitions();

            timeshift(validQueryItems, filters, feed, insightRequestMetadata);
            for (FilterDefinition filterDefinition : analysisDefinition.getFilterDefinitions()) {
                filterDefinition.applyCalculationsBeforeRun(analysisDefinition, allFields, keyMap, displayMap, feed, conn, dlsFilters, insightRequestMetadata);
            }
            dataSet = retrieveDataSet(feed, validQueryItems, filters, insightRequestMetadata, feed.getFields(), conn);
            pipeline = new StandardReportPipeline(insightRequestMetadata.getIntermediatePipelines());
            pipeline.setup(analysisDefinition, feed, insightRequestMetadata, allFields, conn);
            dataSourceInfo = feed.createSourceInfo(conn);
            if (dataSet.getLastTime() == null) {
                dataSet.setLastTime(new Date());
            }
            return this;
        }
    }
}
