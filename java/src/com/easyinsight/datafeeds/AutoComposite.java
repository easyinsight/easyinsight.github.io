package com.easyinsight.datafeeds;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.*;
import com.easyinsight.cache.MemCachedManager;
import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.core.DerivedKey;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.core.NamedKey;
import com.easyinsight.dashboard.*;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.Roles;
import com.easyinsight.security.SecurityUtil;
import org.hibernate.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * User: jamesboe
 * Date: 9/22/14
 * Time: 9:40 AM
 */
public class AutoComposite {

    private long dataSourceID;
    private EIConnection conn;

    private Map<Long, Dashboard> dataSourceToDashboardMap = new HashMap<>();
    private Map<Long, Map<String, AnalysisItem>> dataSourceToFieldMap = new HashMap<>();
    private List<InsightDescriptor> allReports;

    private long timeMeasureTagID = 0;
    private CompositeFeedDefinition parent;

    public AutoComposite(long dataSourceID, EIConnection conn) {
        this.dataSourceID = dataSourceID;
        this.conn = conn;
    }

    public static final String KPIS = "kpis";
    //public static final String DIAGRAM = "diagram";
    public static final String TREND_CHART = "trendchart";
    public static final String COOL_CHART = "coolchart";

    private void determineTimeMeasure() throws SQLException {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT ACCOUNT_TAG_ID FROM ACCOUNT_TAG WHERE TAG_NAME = ? AND ACCOUNT_ID = ?");
        queryStmt.setString(1, "timemeasure");
        queryStmt.setLong(2, SecurityUtil.getAccountID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            timeMeasureTagID = rs.getLong(1);
        }
        queryStmt.close();
    }

    private long createTimeComparisonChart() throws SQLException {
        MultiFieldFilterDefinition multiFieldFilterDefinition = new MultiFieldFilterDefinition();
        multiFieldFilterDefinition.setAvailableHandles(new ArrayList<>());
        multiFieldFilterDefinition.setFieldOrdering(new ArrayList<>());
        multiFieldFilterDefinition.setSelectedItems(new ArrayList<>());
        multiFieldFilterDefinition.setAll(true);
        multiFieldFilterDefinition.setAlphaSort(true);
        multiFieldFilterDefinition.setToggleEnabled(true);
        List<WeNeedToReplaceHibernateTag> tags = new ArrayList<>();
        WeNeedToReplaceHibernateTag tag = new WeNeedToReplaceHibernateTag();
        tag.setTagID(timeMeasureTagID);
        tags.add(tag);
        multiFieldFilterDefinition.setAvailableTags(tags);
        WSLineChartDefinition lineChartDefinition = new WSLineChartDefinition();
        DerivedAnalysisDateDimension comparison = new DerivedAnalysisDateDimension();
        comparison.setConcrete(false);
        comparison.setApplyBeforeAggregation(true);
        comparison.setDerivationCode("nowdate()");
        comparison.setKey(new NamedKey("Date"));
        lineChartDefinition.setAddedItems(Arrays.asList(comparison));
        lineChartDefinition.setMultiMeasure(true);
        lineChartDefinition.setXaxis(comparison);
        lineChartDefinition.setBaseDate("Date");
        lineChartDefinition.setReportType(WSAnalysisDefinition.LINE);
        List<FilterDefinition> filters = new ArrayList<>();
        filters.add(multiFieldFilterDefinition);
        RollingFilterDefinition trendFilter = new RollingFilterDefinition();
        trendFilter.setShowOnReportView(false);
        trendFilter.setChildToParentLabel("Date");
        trendFilter.setField(comparison);
        trendFilter.setInterval(MaterializedRollingFilterDefinition.ALL);
        filters.add(trendFilter);
        lineChartDefinition.setFilterDefinitions(filters);
        lineChartDefinition.setDataFeedID(dataSourceID);
        lineChartDefinition.setName("Line Chart");
        return new AnalysisService().saveReportWithConn(lineChartDefinition, conn).getAnalysisID();
    }

    private long createYTDComparisonChart() throws SQLException {
        MultiFieldFilterDefinition multiFieldFilterDefinition = new MultiFieldFilterDefinition();
        multiFieldFilterDefinition.setAvailableHandles(new ArrayList<>());
        multiFieldFilterDefinition.setFieldOrdering(new ArrayList<>());
        multiFieldFilterDefinition.setSelectedItems(new ArrayList<>());
        multiFieldFilterDefinition.setAll(true);
        multiFieldFilterDefinition.setAlphaSort(true);
        multiFieldFilterDefinition.setToggleEnabled(true);
        List<WeNeedToReplaceHibernateTag> tags = new ArrayList<>();
        WeNeedToReplaceHibernateTag tag = new WeNeedToReplaceHibernateTag();
        tag.setTagID(timeMeasureTagID);
        tags.add(tag);
        multiFieldFilterDefinition.setAvailableTags(tags);
        WSYTDDefinition lineChartDefinition = new WSYTDDefinition();
        DerivedAnalysisDateDimension comparison = new DerivedAnalysisDateDimension();
        comparison.setConcrete(false);
        comparison.setApplyBeforeAggregation(true);
        comparison.setDerivationCode("nowdate()");
        comparison.setKey(new NamedKey("Date"));
        lineChartDefinition.setAddedItems(Arrays.asList(comparison));
        lineChartDefinition.setTimeDimension(comparison);
        lineChartDefinition.setBaseDate("Date");
        lineChartDefinition.setReportType(WSAnalysisDefinition.YTD);
        List<FilterDefinition> filters = new ArrayList<>();
        filters.add(multiFieldFilterDefinition);
        RollingFilterDefinition trendFilter = new RollingFilterDefinition();
        trendFilter.setShowOnReportView(false);
        trendFilter.setChildToParentLabel("Date");
        trendFilter.setField(comparison);
        trendFilter.setInterval(MaterializedRollingFilterDefinition.ALL);
        filters.add(trendFilter);
        lineChartDefinition.setFilterDefinitions(filters);
        lineChartDefinition.setDataFeedID(dataSourceID);
        lineChartDefinition.setName("Month over Month");
        return new AnalysisService().saveReportWithConn(lineChartDefinition, conn).getAnalysisID();
    }

    private List<DashboardStackItem> parentDashboards = new ArrayList<>();

    private DashboardStackItem createOverview() throws SQLException, CloneNotSupportedException {
        DashboardStack overviewPlaceHolder = new DashboardStack();
        overviewPlaceHolder.setLabel("Overview");
        overviewPlaceHolder.setCount(1);
        DashboardStackItem overviewPlacerHolderItem = new DashboardStackItem();
        overviewPlacerHolderItem.setDashboardElement(overviewPlaceHolder);

        List<DashboardStackItem> overviewItems = new ArrayList<>();
        overviewPlaceHolder.setGridItems(overviewItems);

        DashboardGrid overview = new DashboardGrid();
        DashboardStackItem overviewItem = new DashboardStackItem();
        overviewItem.setDashboardElement(overview);
        overviewItems.add(overviewItem);
        overview.setRows(3);
        overview.setColumns(1);

        List<DashboardGridItem> overviewGridItems = new ArrayList<>();

        WSTextDefinition kpiTime = new WSTextDefinition();
        kpiTime.setReportType(WSAnalysisDefinition.TEXT);
        kpiTime.setName("KPI Time");
        kpiTime.setText("The KPIs below show values for the current quarter to date ('''{[Now]}''') as compared to their values in the last quarter to date ('''{[Previous]}''').");
        kpiTime.setDataFeedID(parent.getDataFeedID());
        {
            RollingFilterDefinition previousFilter = new RollingFilterDefinition();
            previousFilter.setFilterName("Previous");
            RollingFilterDefinition newFilter = new RollingFilterDefinition();
            newFilter.setFilterName("Now");
            DerivedAnalysisDateDimension comparison = new DerivedAnalysisDateDimension();
            comparison.setConcrete(false);
            comparison.setApplyBeforeAggregation(true);
            comparison.setDerivationCode("nowdate()");
            comparison.setKey(new NamedKey("Date"));
            previousFilter.setField(comparison);
            newFilter.setField(comparison);
            previousFilter.setInterval(MaterializedRollingFilterDefinition.LAST_QUARTER_TO_NOW);
            previousFilter.setShowOnReportView(false);
            newFilter.setInterval(MaterializedRollingFilterDefinition.QUARTER_TO_NOW);
            newFilter.setShowOnReportView(false);
            kpiTime.setAddedItems(Arrays.asList(comparison));
            kpiTime.setFilterDefinitions(Arrays.asList(newFilter, previousFilter));
        }
        kpiTime.setColumns(new ArrayList<>());
        long kpiTimeID = new AnalysisService().saveReportWithConn(kpiTime, conn).getAnalysisID();
        InsightDescriptor kpiTimeDescriptor = new InsightDescriptor();
        kpiTimeDescriptor.setId(kpiTimeID);
        DashboardReport kpiTimeReport = new DashboardReport();
        kpiTimeReport.setReport(kpiTimeDescriptor);
        DashboardGridItem kpiTimeGridItem = new DashboardGridItem();
        kpiTimeGridItem.setDashboardElement(kpiTimeReport);
        overviewGridItems.add(kpiTimeGridItem);

        DashboardGrid gaugesGrid = new DashboardGrid();

        List<WSTrendDefinition> gauges = allReports.stream().filter(report -> report.getReportType() == WSAnalysisDefinition.TREND).
                map(report -> (WSTrendDefinition) new AnalysisStorage().getAnalysisDefinition(report.getId(), conn)).collect(Collectors.toList());

        Map<AnalysisItem, Long> kpiMap = new HashMap<>();
        int kpiCtr = 0;
        boolean remainingKPIs;
        do {
            remainingKPIs = false;
            for (WSTrendDefinition trendReport : gauges) {
                if (trendReport.getMeasures().size() > kpiCtr) {
                    kpiMap.put(trendReport.getMeasures().get(kpiCtr), trendReport.getDataFeedID());
                }
                if (kpiMap.size() == 4) {
                    remainingKPIs = false;
                    break;
                }
                if (trendReport.getMeasures().size() > (kpiCtr + 1)) {
                    remainingKPIs = true;
                }
            }
            kpiCtr++;
        } while (remainingKPIs);

        int ctr = 0;
        List<DashboardGridItem> gaugeGridItems = new ArrayList<>();
        for (Map.Entry<AnalysisItem, Long> entry : kpiMap.entrySet()) {
            AnalysisItem clone = entry.getKey().clone();
            WSTrendDefinition newTrendDefinition = new WSTrendDefinition();
            RollingFilterDefinition previousFilter = new RollingFilterDefinition();
            previousFilter.setFilterName("Previous");
            RollingFilterDefinition newFilter = new RollingFilterDefinition();
            newFilter.setFilterName("Now");
            DerivedAnalysisDateDimension comparison = new DerivedAnalysisDateDimension();
            comparison.setConcrete(false);
            comparison.setApplyBeforeAggregation(true);
            comparison.setDerivationCode("nowdate()");
            comparison.setKey(new NamedKey("Date"));
            newTrendDefinition.setAddedItems(Arrays.asList(comparison));
            newTrendDefinition.setNowDate("Now");
            newTrendDefinition.setPreviousDate("Previous");
            previousFilter.setField(comparison);
            newFilter.setField(comparison);
            previousFilter.setInterval(MaterializedRollingFilterDefinition.LAST_QUARTER_TO_NOW);
            previousFilter.setShowOnReportView(false);
            newFilter.setInterval(MaterializedRollingFilterDefinition.QUARTER_TO_NOW);
            newFilter.setShowOnReportView(false);
            newTrendDefinition.setDataFeedID(entry.getValue());
            newTrendDefinition.setMeasures(Arrays.asList(clone));
            newTrendDefinition.setName(entry.getKey().toDisplay());
            newTrendDefinition.setReportType(WSTrendDefinition.TREND);
            newTrendDefinition.setFilterDefinitions(Arrays.asList(previousFilter, newFilter));
            newTrendDefinition.setMajorFontSize(24);
            newTrendDefinition.setMinorFontSize(12);
            WSAnalysisDefinition savedTrend = new AnalysisService().saveReportWithConn(newTrendDefinition, conn);
            InsightDescriptor kpi = new InsightDescriptor();
            kpi.setId(savedTrend.getAnalysisID());
            DashboardReport dashboardReport = new DashboardReport();
            dashboardReport.setShowLabel(true);
            dashboardReport.setReport(kpi);
            DashboardGridItem dashboardGridItem = new DashboardGridItem();
            dashboardGridItem.setDashboardElement(dashboardReport);
            dashboardGridItem.setColumnIndex(ctr++);
            dashboardGridItem.setRowIndex(0);
            gaugeGridItems.add(dashboardGridItem);
        }

        gaugesGrid.setColumns(gaugeGridItems.size());
        gaugesGrid.setRows(1);



        gaugesGrid.setGridItems(gaugeGridItems);

        DashboardGridItem gaugesGridGridItem = new DashboardGridItem();
        gaugesGridGridItem.setColumnIndex(0);
        gaugesGridGridItem.setRowIndex(1);
        gaugesGridGridItem.setDashboardElement(gaugesGrid);
        overviewGridItems.add(gaugesGridGridItem);

        /*WSDiagramDefinition diagram = new WSDiagramDefinition();
        diagram.setDataFeedID(dataSourceID);
        diagram.setName("Combined Diagram");
        diagram.setFilterDefinitions(new ArrayList<>());
        diagram.setReportType(WSAnalysisDefinition.DIAGRAM);
        // find diagram specific to data source
        List<WSDiagramDefinition> diagrams = (List<WSDiagramDefinition>) fullReportsTagged(DIAGRAM);

        List<AnalysisItem> newItems = new ArrayList<>();
        List<DiagramLink> diagramLinks = new ArrayList<>();
        int xMax = 0;
        for (int i = 0; i < diagrams.size(); i++) {
            WSDiagramDefinition sourceDiagram = diagrams.get(i);
            Map<String, AnalysisItem> map = new HashMap<>();
            parent.getFields().stream().filter(item -> item.getKey() instanceof DerivedKey).forEach(item -> {
                DerivedKey derivedKey = (DerivedKey) item.getKey();
                if (derivedKey.getFeedID() == sourceDiagram.getDataFeedID()) {
                    map.put(item.toOriginalDisplayName(), item);
                }
            });

            int groupXMax = 0;
            List<AnalysisItem> measures = sourceDiagram.getMeasures();
            Map<AnalysisItem, AnalysisItem> sourceToMaster = new HashMap<>();
            for (AnalysisItem measure : measures) {
                // find the field used in the diagram in the composite source
                AnalysisItem item = map.get(measure.toDisplay());
                AnalysisItem clone = item.clone();
                newItems.add(clone);
                DiagramReportFieldExtension diagramReportFieldExtension = (DiagramReportFieldExtension) measure.getReportFieldExtension();
                int x = diagramReportFieldExtension.getX();
                int y = diagramReportFieldExtension.getY();
                DiagramReportFieldExtension newExtension = new DiagramReportFieldExtension();
                newExtension.setX(x + xMax);
                newExtension.setY(y);
                clone.setReportFieldExtension(newExtension);
                groupXMax = Math.max(x, groupXMax);
                sourceToMaster.put(measure, clone);
            }

            for (DiagramLink link : sourceDiagram.getLinks()) {
                DiagramLink masterLink = new DiagramLink();
                masterLink.setLabel(link.getLabel());
                masterLink.setStartItem(sourceToMaster.get(link.getStartItem()));
                masterLink.setEndItem(sourceToMaster.get(link.getEndItem()));
                diagramLinks.add(masterLink);
            }
            xMax = groupXMax + 180;
        }
        diagram.setMeasures(newItems);
        diagram.setLinks(diagramLinks);

        long diagramID = new AnalysisService().saveReportWithConn(diagram, conn).getAnalysisID();

        DashboardGridItem diagramGridItem = new DashboardGridItem();
        diagramGridItem.setColumnIndex(0);
        diagramGridItem.setRowIndex(1);
        DashboardReport diagramDashboardReport = new DashboardReport();
        diagramDashboardReport.setShowLabel(true);
        InsightDescriptor diagramDescriptor = new InsightDescriptor();
        diagramDescriptor.setId(diagramID);
        diagramDashboardReport.setReport(diagramDescriptor);
        diagramGridItem.setDashboardElement(diagramDashboardReport);

        overviewGridItems.add(diagramGridItem);*/

        DashboardGridItem coolChartGridItem = new DashboardGridItem();
        coolChartGridItem.setColumnIndex(0);
        coolChartGridItem.setRowIndex(2);

        DashboardGrid coolChartGrid = new DashboardGrid();
        coolChartGrid.setGridItems(new ArrayList<>());

        coolChartGridItem.setDashboardElement(coolChartGrid);

        overviewGridItems.add(coolChartGridItem);

        List<InsightDescriptor> coolCharts = new ArrayList<>();
        for (CompositeFeedNode node : parent.getCompositeFeedNodes()) {
            Dashboard dashboard1 = dataSourceToDashboardMap.get(node.getDataFeedID());
            if (dashboard1 != null) {
                DashboardElement container = dashboard1.getRootElement().findElementByLabel("Overview");
                if (container == null) {
                    container = dashboard1.getRootElement().findElementByLabel("What's Happening");
                }
                if (container != null) {
                    System.out.println("found container on " + dashboard1.getName());
                    Set<Long> containedReports = container.containedReports();
                    System.out.println("\tcontained reports = " + containedReports);
                    List<InsightDescriptor> charts = allReports.stream().filter(report -> containedReports.contains(report.getId())).filter(report -> report.getReportType() == WSAnalysisDefinition.COLUMN ||
                            report.getReportType() == WSAnalysisDefinition.STACKED_COLUMN ||
                            report.getReportType() == WSAnalysisDefinition.STACKED_BAR ||
                            report.getReportType() == WSAnalysisDefinition.BAR ||
                            report.getReportType() == WSAnalysisDefinition.LINE ||
                            report.getReportType() == WSAnalysisDefinition.AREA).collect(Collectors.toList());
                    if (charts.size() > 0) {
                        coolCharts.add(charts.get(0));
                    }
                } else {
                    System.out.println("couldn't find container on " + dashboard1.getName());
                }
            }
        }
        if (coolCharts.size() > 0) {
            int currentCapacity = coolChartGrid.getGridItems().size();
            int newMaxSize = currentCapacity + coolCharts.size();
            int newColumns = 2;
            int newRows = (int) Math.ceil((double) newMaxSize / 2);
            coolChartGrid.setColumns(newColumns);
            coolChartGrid.setRows(newRows);
            for (InsightDescriptor gauge : coolCharts) {
                DashboardGridItem dashboardGridItem = createReportInGrid(gauge, 0, 0);
                coolChartGrid.getGridItems().add(dashboardGridItem);
            }
            int totalCtr = 0;
            for (int rowCtr = 0; rowCtr < newRows; rowCtr++) {
                for (int colCtr = 0; colCtr < newColumns; colCtr++) {
                    DashboardGridItem gridItem;
                    if (totalCtr < coolChartGrid.getGridItems().size()) {
                        gridItem = coolChartGrid.getGridItems().get(totalCtr++);
                    } else {
                        DashboardText ph = new DashboardText();
                        ph.setText("");
                        gridItem = new DashboardGridItem();
                        gridItem.setDashboardElement(ph);
                        coolChartGrid.getGridItems().add(gridItem);
                    }
                    gridItem.setColumnIndex(colCtr);
                    gridItem.setRowIndex(rowCtr);
                }
            }
        }

        overview.setGridItems(overviewGridItems);
        overview.setRows(overviewGridItems.size());
        return overviewPlacerHolderItem;
    }

    private DashboardStackItem createTrends(long lineChartID, long ytdID) throws SQLException, CloneNotSupportedException {

        DashboardStack trendStack = new DashboardStack();
        trendStack.setLabel("What's Happened");

        MultiFlatDateFilter trendFilter = new MultiFlatDateFilter();
        trendFilter.setIncludeRelative(true);
        trendFilter.setLevel(AnalysisDateDimension.QUARTER_OF_YEAR_LEVEL);
        trendFilter.setUnitsBack(50);
        trendFilter.setUnitsForward(2);
        List<DateLevelWrapper> levels = new DataService().getMultiDateOptions(trendFilter);
        int startLevel = 0;
        int endLevel = 0;
        for (DateLevelWrapper level : levels) {
            if ("First Quarter of Year".equals(level.getDisplay())) {
                startLevel = level.getDateLevel();
            } else if ("This Quarter".equals(level.getDisplay())) {
                endLevel = level.getDateLevel();
            }
        }
        List<DateLevelWrapper> endLevels = new ArrayList<>();
        for (DateLevelWrapper wrapper : levels) {
            if (wrapper.getDateLevel() >= startLevel && wrapper.getDateLevel() <= endLevel) {
                endLevels.add(wrapper);
            }
        }
        trendFilter.setLevels(endLevels);
        DerivedAnalysisDateDimension trendDate = new DerivedAnalysisDateDimension();
        trendDate.setDerivationCode("nowdate()");
        trendDate.setKey(new NamedKey("Date"));
        trendFilter.setField(trendDate);
        trendFilter.setParentChildLabel("Date");
        trendStack.setFilters(Arrays.asList(trendFilter));

        List<DashboardStackItem> trendStackItems = new ArrayList<>();


        // find any report of type trend chart...

        List<WSColumnChartDefinition> fullTrendReports = allReports.stream().filter(report -> report.getReportType() == WSAnalysisDefinition.COLUMN).
                map(report -> (WSColumnChartDefinition) new AnalysisStorage().getAnalysisDefinition(report.getId(), conn)).
                filter(WSColumnChartDefinition::seemsLikeDashboardTrendReport).
                collect(Collectors.toList());

        List<WSColumnChartDefinition> qoqTrends = new ArrayList<>();
        List<WSColumnChartDefinition> momTrends = new ArrayList<>();

        for (WSColumnChartDefinition chart : fullTrendReports) {
            AnalysisDateDimension qoqDate = (AnalysisDateDimension) chart.getXaxis();
            qoqDate.setDateLevel(AnalysisDateDimension.QUARTER_OF_YEAR_LEVEL);
            WSColumnChartDefinition qoqCopy = (WSColumnChartDefinition) copyReport(chart, chart.getName() + " - QoQ");

            //new AnalysisService().saveReportWithConn(qoqCopy, conn);
            qoqTrends.add(qoqCopy);

            AnalysisDateDimension momDate = (AnalysisDateDimension) chart.getXaxis();
            momDate.setDateLevel(AnalysisDateDimension.MONTH_LEVEL);
            WSColumnChartDefinition momCopy = (WSColumnChartDefinition) copyReport(chart, chart.getName() + " - MoM");
            //new AnalysisService().saveReportWithConn(momCopy, conn);
            momTrends.add(momCopy);
        }

        {
            DashboardGrid qoqTrendGrid = new DashboardGrid();
            qoqTrendGrid.setGridItems(new ArrayList<>());
            qoqTrendGrid.setLabel("QoQ Trends");

            List<InsightDescriptor> trendReports = qoqTrends.stream().
                    map(report -> new InsightDescriptor(report.getAnalysisID(), report.getName(), report.getDataFeedID(), report.getReportType(), report.getUrlKey(), Roles.OWNER, true)).
                    collect(Collectors.toList());

            if (trendReports.size() > 8) {
                trendReports = trendReports.subList(0, 8);
            }

            if (trendReports.size() > 0) {
                int currentCapacity = qoqTrendGrid.getGridItems().size();
                int newMaxSize = currentCapacity + trendReports.size();
                int newColumns = 2;
                int newRows = (int) Math.ceil((double) newMaxSize / 2);
                qoqTrendGrid.setColumns(newColumns);
                qoqTrendGrid.setRows(newRows);
                for (InsightDescriptor gauge : trendReports) {
                    DashboardGridItem dashboardGridItem = createReportInGrid(gauge, 0, 0);
                    qoqTrendGrid.getGridItems().add(dashboardGridItem);
                }
                int totalCtr = 0;
                for (int rowCtr = 0; rowCtr < newRows; rowCtr++) {
                    for (int colCtr = 0; colCtr < newColumns; colCtr++) {
                        DashboardGridItem gridItem;
                        if (totalCtr < qoqTrendGrid.getGridItems().size()) {
                            gridItem = qoqTrendGrid.getGridItems().get(totalCtr++);
                        } else {
                            DashboardText ph = new DashboardText();
                            ph.setText("");
                            gridItem = new DashboardGridItem();
                            gridItem.setDashboardElement(ph);
                            qoqTrendGrid.getGridItems().add(gridItem);
                        }
                        gridItem.setColumnIndex(colCtr);
                        gridItem.setRowIndex(rowCtr);
                    }
                }
            }

            DashboardStackItem trendStackItem = new DashboardStackItem();
            trendStackItem.setDashboardElement(qoqTrendGrid);
            trendStackItems.add(trendStackItem);
        }

        {
            DashboardGrid momTrendGrid = new DashboardGrid();
            momTrendGrid.setGridItems(new ArrayList<>());
            momTrendGrid.setLabel("MoM Trends");

            List<InsightDescriptor> trendReports = momTrends.stream().
                    map(report -> new InsightDescriptor(report.getAnalysisID(), report.getName(), report.getDataFeedID(), report.getReportType(), report.getUrlKey(), Roles.OWNER, true)).
                    collect(Collectors.toList());

            if (trendReports.size() > 8) {
                trendReports = trendReports.subList(0, 8);
            }

            if (trendReports.size() > 0) {
                int currentCapacity = momTrendGrid.getGridItems().size();
                int newMaxSize = currentCapacity + trendReports.size();
                int newColumns = 2;
                int newRows = (int) Math.ceil((double) newMaxSize / 2);
                momTrendGrid.setColumns(newColumns);
                momTrendGrid.setRows(newRows);
                for (InsightDescriptor gauge : trendReports) {
                    DashboardGridItem dashboardGridItem = createReportInGrid(gauge, 0, 0);
                    momTrendGrid.getGridItems().add(dashboardGridItem);
                }
                int totalCtr = 0;
                for (int rowCtr = 0; rowCtr < newRows; rowCtr++) {
                    for (int colCtr = 0; colCtr < newColumns; colCtr++) {
                        DashboardGridItem gridItem;
                        if (totalCtr < momTrendGrid.getGridItems().size()) {
                            gridItem = momTrendGrid.getGridItems().get(totalCtr++);
                        } else {
                            DashboardText ph = new DashboardText();
                            ph.setText("");
                            gridItem = new DashboardGridItem();
                            gridItem.setDashboardElement(ph);
                            momTrendGrid.getGridItems().add(gridItem);
                        }
                        gridItem.setColumnIndex(colCtr);
                        gridItem.setRowIndex(rowCtr);
                    }
                }
            }

            DashboardStackItem trendStackItem = new DashboardStackItem();
            trendStackItem.setDashboardElement(momTrendGrid);
            trendStackItems.add(trendStackItem);
        }

        {
            if (lineChartID > 0) {
                DashboardReport lineChartReport = new DashboardReport();
                InsightDescriptor lineChartDescriptor = new InsightDescriptor();
                lineChartDescriptor.setId(lineChartID);
                lineChartReport.setShowLabel(true);
                lineChartReport.setReport(lineChartDescriptor);

                DashboardStackItem lineChartReportStackItem = new DashboardStackItem();
                lineChartReportStackItem.setDashboardElement(lineChartReport);
                trendStackItems.add(lineChartReportStackItem);
            }
        }
        {
            if (ytdID > 0) {
                DashboardReport ytdReport = new DashboardReport();
                InsightDescriptor ytdDescriptor = new InsightDescriptor();
                ytdDescriptor.setId(ytdID);
                ytdReport.setShowLabel(true);
                ytdReport.setReport(ytdDescriptor);

                DashboardStackItem ytdReportStackItem = new DashboardStackItem();
                ytdReportStackItem.setDashboardElement(ytdReport);
                trendStackItems.add(ytdReportStackItem);
            }
        }

        trendStack.setGridItems(trendStackItems);

        trendStack.setCount(trendStackItems.size());

        // create trend charts, month over month, quarter over quarter, year over year


        DashboardStackItem t = new DashboardStackItem();
        t.setDashboardElement(trendStack);
        return t;
    }

    private WSAnalysisDefinition copyReport(WSAnalysisDefinition source, String newName) throws SQLException, CloneNotSupportedException {
        AnalysisDefinition analysisDefinition = AnalysisDefinitionFactory.fromWSDefinition(source);
        FeedDefinition feedDefinition = new FeedStorage().getFeedDefinitionData(source.getDataFeedID(), conn);
        List<AnalysisItem> allFields = feedDefinition.allFields(conn);
        AnalysisDefinition.SaveMetadata metadata = analysisDefinition.clone(allFields, false);
        AnalysisDefinition clone = metadata.analysisDefinition;
        AnalysisDefinition.updateFromMetadata(null, metadata.replacementMap, clone, allFields, metadata.added);
        clone.setAuthorName(SecurityUtil.getUserName());
        clone.setTitle(newName);
        List<UserToAnalysisBinding> bindings = new ArrayList<UserToAnalysisBinding>();
        bindings.add(new UserToAnalysisBinding(SecurityUtil.getUserID(), UserPermission.OWNER));
        clone.setUserBindings(bindings);
        Session session = Database.instance().createSession(conn);
        new AnalysisStorage().saveAnalysis(clone, session);
        session.flush();
        session.close();
        return clone.createBlazeDefinition();
    }

    private void createDataSourceStack() throws SQLException, CloneNotSupportedException {
        for (CompositeFeedNode child : parent.getCompositeFeedNodes()) {
            DashboardStackItem item = createDataSourceStackForChild(child);
            if (item != null) {
                parentDashboards.add(item);
            }
        }
    }

    private DashboardStackItem createDataSourceStackForChild(CompositeFeedNode child) throws SQLException, CloneNotSupportedException {
        Map<String, AnalysisItem> map = dataSourceToFieldMap.get(child.getDataFeedID());
        // find the dashboard used for the child

        // if it's custom data...

        PreparedStatement ps = conn.prepareStatement("SELECT DASHBOARD.DASHBOARD_ID FROM DASHBOARD, dashboard_install_info WHERE dashboard.DATA_SOURCE_ID = ? AND " +
                "DASHBOARD.DASHBOARD_ID = dashboard_install_info.result_dashboard_id AND dashboard_install_info.dashboard_install_info_id IS NOT NULL");
        ps.setLong(1, child.getDataFeedID());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            long dashboardID = rs.getLong(1);
            Dashboard dashboard1 = new DashboardService().getDashboard(dashboardID);

            DashboardStack childStack = (DashboardStack) dashboard1.getRootElement();

            List<FilterDefinition> childFilters = new ArrayList<>();
            for (FilterDefinition filter : dashboard1.getFilters()) {
                AnalysisItem field = filter.getField();
                FilterDefinition clonedFilter = filter.clone();
                AnalysisItem masterItem = map.get(field.toDisplay());
                clonedFilter.setField(masterItem);
                childFilters.add(clonedFilter);
            }

            DashboardStack masterStack = new DashboardStack();
            masterStack.setFilterBorderStyle("none");
            masterStack.setFilterBorderColor(0xFFFFFF);
            masterStack.setFilters(childFilters);
            masterStack.setLabel(child.getDataSourceName());

            List<DashboardStackItem> masterStackChildren = new ArrayList<>();

            for (DashboardStackItem item : childStack.getGridItems()) {
                if ("Overview".equals(item.getDashboardElement().getLabel())) {
                    DashboardStackItem clone = item.clone();
                    masterStackChildren.add(clone);
                } else if ("Analyze".equals(item.getDashboardElement().getLabel())) {
                    if (item.getDashboardElement() instanceof DashboardStack) {
                        DashboardStack analyzeStack = (DashboardStack) item.getDashboardElement();
                        for (DashboardStackItem analyzeItem : analyzeStack.getGridItems()) {
                            DashboardStackItem clone = analyzeItem.clone();
                            masterStackChildren.add(clone);
                        }
                    }
                }
            }

            masterStack.setCount(masterStackChildren.size());
            masterStack.setGridItems(masterStackChildren);
            DashboardStackItem blah = new DashboardStackItem();
            blah.setDashboardElement(masterStack);
            return blah;
        }
        return null;
    }

    /*private DashboardStackItem createTablesStack() throws SQLException {
        return null;
    }*/

    public static final int CREATE_COMPOSITE = 1;
    public static final int CREATE_DASHBOARD_ON_COMPOSITE = 2;
    public static final int ADD_TO_COMPOSITE = 3;
    public static final int NADA = 4;

    public static int autoDashboardable(EIConnection conn) throws SQLException {
        List<DataSourceDescriptor> dataSources = new FeedStorage().getDataSources(SecurityUtil.getUserID(), SecurityUtil.getAccountID(), conn, true);

            // if data sources

            int dataSourceTypes = 0;
            Set<Integer> types = new HashSet<>();
            Set<Long> existingIDs = new HashSet<>();
            List<DataSourceDescriptor> compositeSources = new ArrayList<>();
            DataSourceDescriptor autoDataSourceDescriptor = null;
            for (DataSourceDescriptor dataSource : dataSources) {
                if (new DataSourceTypeRegistry().billingInfoForType(FeedType.valueOf(dataSource.getDataSourceType())) == ConnectionBillingType.SMALL_BIZ && !types.contains(dataSource.getDataSourceType())) {
                    existingIDs.add(dataSource.getId());
                    types.add(dataSource.getDataSourceType());
                    dataSourceTypes++;
                } else if (dataSource.getDataSourceType() == FeedType.COMPOSITE.getType()) {
                    compositeSources.add(dataSource);
                    if (dataSource.isAutoCombined()) {
                        autoDataSourceDescriptor = dataSource;
                    }
                }
            }

            if (types.size() < 2) {

                // only one connection, ignore

                return NADA;

            } else if (autoDataSourceDescriptor != null) {

                // data source is created, do we need to add any child sources to it?

                PreparedStatement ps = conn.prepareStatement("SELECT COMPOSITE_NODE.DATA_FEED_ID FROM COMPOSITE_FEED, COMPOSITE_NODE WHERE " +
                        "COMPOSITE_NODE.COMPOSITE_FEED_ID = COMPOSITE_FEED.COMPOSITE_FEED_ID AND COMPOSITE_FEED.DATA_FEED_ID = ?");
                ps.setLong(1, autoDataSourceDescriptor.getId());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    long existingID = rs.getLong(1);
                    existingIDs.remove(existingID);
                }

                if (existingIDs.size() > 0) {

                    // we need to add some child sources

                    return ADD_TO_COMPOSITE;

                } else {
                    PreparedStatement dashboardStmt = conn.prepareStatement("SELECT DASHBOARD.DASHBOARD_ID FROM DASHBOARD WHERE " +
                            "DASHBOARD.DATA_SOURCE_ID = ? AND DASHBOARD.AUTO_DASHBOARD = ?");
                    dashboardStmt.setLong(1, autoDataSourceDescriptor.getId());
                    dashboardStmt.setBoolean(2, true);
                    ResultSet dashboardRS = dashboardStmt.executeQuery();
                    if (dashboardRS.next()) {
                        long dashboardID = dashboardRS.getLong(1);

                        // dashboard exists!

                        return NADA;


                    } else {

                        return CREATE_DASHBOARD_ON_COMPOSITE;

                    }
                }

                // if not, do we need to create a composite dashboard on it?

            } else if (compositeSources.size() > 0) {

                // a non-auto composite source has been created, ignore

                return NADA;

            } else {

                // create composite source?

                return CREATE_COMPOSITE;

            }






    }

    public String doSomething() throws SQLException, CloneNotSupportedException {

        parent = (CompositeFeedDefinition) new FeedStorage().getFeedDefinitionData(dataSourceID, conn);

        for (CompositeFeedNode child : parent.getCompositeFeedNodes()) {
            Map<String, AnalysisItem> map = new HashMap<>();
            parent.getFields().stream().filter(item -> item.getKey() instanceof DerivedKey).forEach(item -> {
                DerivedKey derivedKey = (DerivedKey) item.getKey();
                if (derivedKey.getFeedID() == child.getDataFeedID()) {
                    map.put(item.toOriginalDisplayName(), item);
                }
            });
            dataSourceToFieldMap.put(child.getDataFeedID(), map);

            PreparedStatement ps = conn.prepareStatement("SELECT DASHBOARD.DASHBOARD_ID FROM DASHBOARD, dashboard_install_info WHERE dashboard.DATA_SOURCE_ID = ? AND " +
                    "DASHBOARD.DASHBOARD_ID = dashboard_install_info.result_dashboard_id AND dashboard_install_info.dashboard_install_info_id IS NOT NULL");
            ps.setLong(1, child.getDataFeedID());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                long dashboardID = rs.getLong(1);
                Dashboard dashboard1 = new DashboardService().getDashboard(dashboardID);
                dataSourceToDashboardMap.put(child.getDataFeedID(), dashboard1);
            }
        }

        allReports = new AnalysisService().getInsightDescriptorsForDataSource(dataSourceID);

        determineTimeMeasure();

        long lineChartID;
        long ytdID;
        if (timeMeasureTagID > 0) {
            lineChartID = createTimeComparisonChart();
            ytdID = createYTDComparisonChart();
        } else {
            lineChartID = 0;
            ytdID = 0;
        }

        Dashboard dashboard = new Dashboard();
        dashboard.setAutoCombined(true);
        dashboard.setFolder(1);
        dashboard.setFillStackHeaders(true);
        dashboard.setDescription("");
        dashboard.setAbsoluteSizing(true);

        DashboardStack primaryStack = new DashboardStack();
        primaryStack.setGridItems(parentDashboards);

        parentDashboards.add(createOverview());

        parentDashboards.add(createTrends(lineChartID, ytdID));

        createDataSourceStack();

        primaryStack.setCount(parentDashboards.size());

        dashboard.setRootElement(primaryStack);

        dashboard.setName("Master Dashboard");

        dashboard.setDataSourceID(dataSourceID);

        Dashboard saved = new DashboardService().saveDashboardWithConn(dashboard, conn);

        MemCachedManager.delete("dashboard" + dashboard.getId());

        return saved.getUrlKey();
    }

    public String attach(CompositeFeedNode newNode) throws Exception {

        parent = (CompositeFeedDefinition) new FeedStorage().getFeedDefinitionData(dataSourceID, conn);

        PreparedStatement ps = conn.prepareStatement("SELECT DASHBOARD_ID FROM DASHBOARD WHERE DATA_SOURCE_ID = ? AND AUTO_DASHBOARD = ?");
        ps.setLong(1, dataSourceID);
        ps.setBoolean(2, true);
        ResultSet rs = ps.executeQuery();
        rs.next();
        long dashboardID = rs.getLong(1);
        ps.close();
        Dashboard dashboard = new DashboardStorage().getDashboard(dashboardID, conn);
        DashboardStack primaryStack = (DashboardStack) dashboard.getRootElement();
        DashboardStack overview = (DashboardStack) primaryStack.findElementByLabel("Overview");
        DashboardGrid parentOverViewGrid = (DashboardGrid) overview.getGridItems().get(0).getDashboardElement();
        DashboardGrid overviewGrid = (DashboardGrid) parentOverViewGrid.getGridItems().get(0).getDashboardElement();

        if (overviewGrid != null)
        {
            if (overviewGrid.getGridItems().size() < 4) {

                List<WSAnalysisDefinition> gauges = allReports.stream().filter(report -> report.getReportType() == WSAnalysisDefinition.TREND).
                        map(report -> new AnalysisStorage().getAnalysisDefinition(report.getId(), conn)).collect(Collectors.toList());


                if (gauges.size() > 0) {
                    WSTrendDefinition firstKPIReport = (WSTrendDefinition) gauges.get(0);
                    InsightDescriptor kpi = createKPIReport(newNode, firstKPIReport);
                    DashboardReport dashboardReport = new DashboardReport();
                    dashboardReport.setShowLabel(true);
                    dashboardReport.setReport(kpi);
                    DashboardGridItem dashboardGridItem = new DashboardGridItem();
                    dashboardGridItem.setDashboardElement(dashboardReport);
                    dashboardGridItem.setColumnIndex(overviewGrid.getGridItems().size());
                    dashboardGridItem.setRowIndex(0);
                    overviewGrid.getGridItems().add(dashboardGridItem);
                    overviewGrid.setColumns(overviewGrid.getGridItems().size());
                }
            }
        }

        List<InsightDescriptor> coolCharts = new ArrayList<>();
        for (CompositeFeedNode node : parent.getCompositeFeedNodes()) {
            Dashboard dashboard1 = dataSourceToDashboardMap.get(node.getDataFeedID());
            if (dashboard1 != null) {
                DashboardElement container = dashboard1.getRootElement().findElementByLabel("Overview");
                if (container == null) {
                    container = dashboard1.getRootElement().findElementByLabel("What's Happening");
                }
                if (container != null) {
                    Set<Long> containedReports = container.containedReports();
                    List<InsightDescriptor> charts = allReports.stream().filter(report -> containedReports.contains(report.getDataFeedID())).filter(report -> report.getReportType() == WSAnalysisDefinition.COLUMN ||
                            report.getReportType() == WSAnalysisDefinition.STACKED_COLUMN ||
                            report.getReportType() == WSAnalysisDefinition.STACKED_BAR ||
                            report.getReportType() == WSAnalysisDefinition.BAR ||
                            report.getReportType() == WSAnalysisDefinition.LINE ||
                            report.getReportType() == WSAnalysisDefinition.AREA).collect(Collectors.toList());
                    if (charts.size() > 0) {
                        coolCharts.add(charts.get(0));
                    }
                }
            }
        }

        DashboardGrid coolChartGrid = (DashboardGrid) overviewGrid.getGridItems().get(1).getDashboardElement();
        if (coolCharts.size() > 0) {
            int currentCapacity = coolChartGrid.getGridItems().size();
            int newMaxSize = currentCapacity + coolCharts.size();
            int newColumns = 2;
            int newRows = (int) Math.ceil((double) newMaxSize / 2);
            coolChartGrid.setColumns(newColumns);
            coolChartGrid.setRows(newRows);
            for (InsightDescriptor gauge : coolCharts) {
                DashboardGridItem dashboardGridItem = createReportInGrid(gauge, 0, 0);
                coolChartGrid.getGridItems().add(dashboardGridItem);
            }
            int totalCtr = 0;
            for (int rowCtr = 0; rowCtr < newRows; rowCtr++) {
                for (int colCtr = 0; colCtr < newColumns; colCtr++) {
                    DashboardGridItem gridItem;
                    if (totalCtr < coolChartGrid.getGridItems().size()) {
                        gridItem = coolChartGrid.getGridItems().get(totalCtr++);
                    } else {
                        DashboardText ph = new DashboardText();
                        ph.setText("");
                        gridItem = new DashboardGridItem();
                        gridItem.setDashboardElement(ph);
                        coolChartGrid.getGridItems().add(gridItem);
                    }
                    gridItem.setColumnIndex(colCtr);
                    gridItem.setRowIndex(rowCtr);
                }
            }
        }

        DashboardStack trends = (DashboardStack) primaryStack.findElementByLabel("What's Happened");
        DashboardGrid trendGrid = (DashboardGrid) trends.getGridItems().get(0).getDashboardElement();
        List<InsightDescriptor> trendReports = allReports.stream().filter(report -> report.getDataFeedID() == newNode.getDataFeedID()).
                filter(report -> report.getReportType() == WSAnalysisDefinition.COLUMN).
                map(report -> (WSColumnChartDefinition) new AnalysisStorage().getAnalysisDefinition(report.getId(), conn)).
                filter(WSColumnChartDefinition::seemsLikeDashboardTrendReport).
                map(report -> new InsightDescriptor(report.getAnalysisID(), report.getName(), report.getDataFeedID(), report.getReportType(), report.getUrlKey(), Roles.OWNER, true)).
                collect(Collectors.toList());
        if (trendReports.size() > 0) {
            int currentCapacity = trendGrid.getGridItems().size();
            int newMaxSize = currentCapacity + trendReports.size();
            int newColumns = 2;
            int newRows = (int) Math.ceil((double) newMaxSize / 2);
            trendGrid.setColumns(newColumns);
            trendGrid.setRows(newRows);
            for (InsightDescriptor gauge : trendReports) {
                DashboardGridItem dashboardGridItem = createReportInGrid(gauge, 0, 0);
                trendGrid.getGridItems().add(dashboardGridItem);
            }
            int totalCtr = 0;
            for (int rowCtr = 0; rowCtr < newRows; rowCtr++) {
                for (int colCtr = 0; colCtr < newColumns; colCtr++) {
                    DashboardGridItem gridItem;
                    if (totalCtr < trendGrid.getGridItems().size()) {
                        gridItem = trendGrid.getGridItems().get(totalCtr++);
                    } else {
                        DashboardText ph = new DashboardText();
                        ph.setText("");
                        gridItem = new DashboardGridItem();
                        gridItem.setDashboardElement(ph);
                        trendGrid.getGridItems().add(gridItem);
                    }
                    gridItem.setColumnIndex(colCtr);
                    gridItem.setRowIndex(rowCtr);
                }
            }
        }
        // modify the gauges
        // modify the diagram
        // modify the trends
        // add the dashboard section
        DashboardStackItem childDashboard = createDataSourceStackForChild(newNode);
        if (childDashboard != null) {
            primaryStack.getGridItems().add(childDashboard);
        }
        primaryStack.setCount(primaryStack.getGridItems().size());
        new DashboardService().saveDashboardWithConn(dashboard, conn);

        return dashboard.getUrlKey();
    }

    protected InsightDescriptor createKPIReport(CompositeFeedNode newNode, WSTrendDefinition firstKPIReport) throws CloneNotSupportedException {
        AnalysisItem analysisItem = firstKPIReport.getMeasures().get(0);
        AnalysisItem clone = analysisItem.clone();
        WSTrendDefinition newTrendDefinition = new WSTrendDefinition();
        newTrendDefinition.setDataFeedID(newNode.getDataFeedID());
        newTrendDefinition.setMeasures(Arrays.asList(clone));
        newTrendDefinition.setName(analysisItem.toDisplay());
        newTrendDefinition.setReportType(WSTrendDefinition.TREND);
        newTrendDefinition.setFilterDefinitions(new ArrayList<>());
        newTrendDefinition.setMajorFontSize(24);
        newTrendDefinition.setMinorFontSize(12);
        WSAnalysisDefinition savedTrend = new AnalysisService().saveReportWithConn(newTrendDefinition, conn);
        InsightDescriptor kpi = new InsightDescriptor();
        kpi.setId(savedTrend.getAnalysisID());
        return kpi;
    }

    protected DashboardGridItem createReportInGrid(InsightDescriptor gauge, int rowIndex, int colIndex) {
        DashboardGridItem dashboardGridItem = new DashboardGridItem();
        DashboardReport dashboardReport = new DashboardReport();
        dashboardReport.setShowLabel(true);
        dashboardReport.setReport(gauge);
        dashboardGridItem.setDashboardElement(dashboardReport);
        dashboardGridItem.setColumnIndex(colIndex);
        dashboardGridItem.setRowIndex(rowIndex);
        return dashboardGridItem;
    }
}
