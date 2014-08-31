package com.easyinsight.analysis;

import com.easyinsight.analysis.definitions.*;
import com.easyinsight.analysis.gauge.GaugeDefinitionState;
import com.easyinsight.core.InsightDescriptor;

import java.util.ArrayList;
import java.util.List;

/**
 * User: James Boe
 * Date: Jan 10, 2008
 * Time: 8:08:08 PM
 */
public class AnalysisDefinitionFactory {

    public static AnalysisDefinition fromWSDefinition(WSAnalysisDefinition wsAnalysisDefinition) {
        AnalysisDefinitionState analysisDefinitionState;
        if (wsAnalysisDefinition.getDataFeedType().equals(AnalysisTypes.CROSSTAB)) {
            WSCrosstabDefinition wsCrosstabDefinition = (WSCrosstabDefinition) wsAnalysisDefinition;
            CrosstabDefinitionState crosstabDefinition = new CrosstabDefinitionState();
            crosstabDefinition.setCrosstabDefinitionID(wsCrosstabDefinition.getCrosstabDefinitionID());
            analysisDefinitionState = crosstabDefinition;
        } else if (wsAnalysisDefinition.getDataFeedType().equals(AnalysisTypes.LIST)) {
            WSListDefinition wsListDefinition = (WSListDefinition) wsAnalysisDefinition;
            ListDefinitionState listDefinition = new ListDefinitionState();
            listDefinition.setShowRowNumbers(wsListDefinition.isShowLineNumbers());
            listDefinition.setListLimitsMetadata(wsListDefinition.getListLimitsMetadata());
            listDefinition.setSummarizeAll(wsListDefinition.isSummaryTotal());
            listDefinition.setDefinitionID(wsListDefinition.getListDefinitionID());
            analysisDefinitionState = listDefinition;
        } else if (wsAnalysisDefinition.getDataFeedType().equals(AnalysisTypes.CHART)) {
            WSChartDefinition wsChart = (WSChartDefinition) wsAnalysisDefinition;
            ChartDefinitionState chartDefinition = new ChartDefinitionState();
            chartDefinition.setChartFamily(wsChart.getChartFamily());
            chartDefinition.setChartType(wsChart.getChartType());
            chartDefinition.setLimitsMetadata(wsChart.getLimitsMetadata());
            chartDefinition.setDefinitionID(wsChart.getChartDefinitionID());
            analysisDefinitionState = chartDefinition;
        } else if (wsAnalysisDefinition.getDataFeedType().equals(AnalysisTypes.MAP)) {
            WSMapDefinition wsMap = (WSMapDefinition) wsAnalysisDefinition;
            MapDefinitionState mapDefinition = new MapDefinitionState();
            mapDefinition.setMapType(wsMap.getMapType());
            mapDefinition.setMapDefinitionID(wsMap.getMapDefinitionID());
            analysisDefinitionState = mapDefinition;
        } else if (wsAnalysisDefinition.getDataFeedType().equals(AnalysisTypes.GAUGE)) {
            WSGaugeDefinition wsGauge = (WSGaugeDefinition) wsAnalysisDefinition;
            GaugeDefinitionState gaugeDefinition = new GaugeDefinitionState();
            gaugeDefinition.setMaxValue(wsGauge.getMaxValue());
            gaugeDefinition.setGaugeDefinitionID(wsGauge.getGaugeDefinitionID());
            analysisDefinitionState = gaugeDefinition;
        } else if (wsAnalysisDefinition.getDataFeedType().equals(AnalysisTypes.TREEMAP)) {
            WSTreeMapDefinition wsTree = (WSTreeMapDefinition) wsAnalysisDefinition;
            TreeMapDefinitionState treeMapDefinitionState = new TreeMapDefinitionState();
            treeMapDefinitionState.setDefinitionID(wsTree.getTreeMapDefinitionID());
            analysisDefinitionState = treeMapDefinitionState;
        } else if (wsAnalysisDefinition.getDataFeedType().equals(AnalysisTypes.TREE)) {
            WSTreeDefinition wsTree = (WSTreeDefinition) wsAnalysisDefinition;
            TreeDefinitionState treeDefinitionState = new TreeDefinitionState();
            treeDefinitionState.setTreeDefinitionID(wsTree.getTreeDefinitionID());
            analysisDefinitionState = treeDefinitionState;
        } else if (wsAnalysisDefinition.getDataFeedType().equals(AnalysisTypes.SUMMARY)) {
            WSSummaryDefinition wsTree = (WSSummaryDefinition) wsAnalysisDefinition;
            SummaryDefinitionState treeDefinitionState = new SummaryDefinitionState();
            treeDefinitionState.setSummaryDefinitionID(wsTree.getSummaryDefinitionID());
            analysisDefinitionState = treeDefinitionState;
        } else if (wsAnalysisDefinition.getDataFeedType().equals(AnalysisTypes.FORM)) {
            WSForm wsForm = (WSForm) wsAnalysisDefinition;
            FormDefinitionState formDefinitionState = new FormDefinitionState();
            formDefinitionState.setFormID(wsForm.getFormID());
            analysisDefinitionState = formDefinitionState;
        } else if (wsAnalysisDefinition.getDataFeedType().equals(AnalysisTypes.HEATMAP)) {
            WSHeatMap heatMap = (WSHeatMap) wsAnalysisDefinition;
            HeatMapDefinitionState heatMapDefinitionState = new HeatMapDefinitionState();
            heatMapDefinitionState.setLatitude(heatMap.getLatitude());
            heatMapDefinitionState.setLongitude(heatMap.getLongitude());
            heatMapDefinitionState.setMapType(heatMap.getMapType());
            heatMapDefinitionState.setDisplayType(heatMap.getDisplayType());
            heatMapDefinitionState.setZoomLevel(heatMap.getZoomLevel());
            heatMapDefinitionState.setHeatMapDefinitionID(heatMap.getHeatMapID());
            heatMapDefinitionState.setMinLat(heatMap.getMinLat());
            heatMapDefinitionState.setMinLong(heatMap.getMinLong());
            heatMapDefinitionState.setMaxLat(heatMap.getMaxLat());
            heatMapDefinitionState.setMaxLong(heatMap.getMaxLong());
            analysisDefinitionState = heatMapDefinitionState;
        } else if (wsAnalysisDefinition.getDataFeedType().equals(AnalysisTypes.GANTT)) {
            WSGanttChartDefinition gantt = (WSGanttChartDefinition) wsAnalysisDefinition;
            GanttChartDefinitionState ganttChartDefinitionState = new GanttChartDefinitionState();
            ganttChartDefinitionState.setGanttDefinitionID(gantt.getGanttDefinitionID());
            analysisDefinitionState = ganttChartDefinitionState;
        } else if (wsAnalysisDefinition.getDataFeedType().equals(AnalysisTypes.VERTICAL_LIST)) {
            WSVerticalListDefinition verticalListDefinition = (WSVerticalListDefinition) wsAnalysisDefinition;
            VerticalListDefinitionState verticalListDefinitionState = new VerticalListDefinitionState();
            verticalListDefinitionState.setVerticalListID(verticalListDefinition.getVerticalListID());
            analysisDefinitionState = verticalListDefinitionState;
        } else if (wsAnalysisDefinition.getDataFeedType().equals(AnalysisTypes.YTD)) {
            WSYTDDefinition ytdReport = (WSYTDDefinition) wsAnalysisDefinition;
            YTDDefinitionState ytdDefinitionState = new YTDDefinitionState();
            ytdDefinitionState.setYtdID(ytdReport.getYtdID());
            analysisDefinitionState = ytdDefinitionState;
        } else if (wsAnalysisDefinition.getDataFeedType().equals(AnalysisTypes.COMPARE_YEARS)) {
            WSCompareYearsDefinition ytdReport = (WSCompareYearsDefinition) wsAnalysisDefinition;
            CompareYearsDefinitionState ytdDefinitionState = new CompareYearsDefinitionState();
            ytdDefinitionState.setYtdID(ytdReport.getYtdID());
            analysisDefinitionState = ytdDefinitionState;
        } else if (wsAnalysisDefinition.getDataFeedType().equals(AnalysisTypes.TREND)) {
            WSTrendDefinition wsTrendDefinition = (WSTrendDefinition) wsAnalysisDefinition;
            TrendDefinitionState trendDefinitionState = new TrendDefinitionState();
            trendDefinitionState.setTrendReportID(wsTrendDefinition.getTrendReportID());
            analysisDefinitionState = trendDefinitionState;
        } else if (wsAnalysisDefinition.getDataFeedType().equals(AnalysisTypes.TREND_GRID)) {
            WSTrendGridDefinition wsTrendDefinition = (WSTrendGridDefinition) wsAnalysisDefinition;
            TrendGridDefinitionState trendDefinitionState = new TrendGridDefinitionState();
            trendDefinitionState.setTrendReportID(wsTrendDefinition.getTrendReportID());
            trendDefinitionState.setSortDirection(wsTrendDefinition.isSortAscending());
            trendDefinitionState.setSortIndex(wsTrendDefinition.getSortIndex());
            analysisDefinitionState = trendDefinitionState;
        } else if (wsAnalysisDefinition.getDataFeedType().equals(AnalysisTypes.DIAGRAM)) {
            WSDiagramDefinition wsTrendDefinition = (WSDiagramDefinition) wsAnalysisDefinition;
            DiagramDefinitionState trendDefinitionState = new DiagramDefinitionState();
            trendDefinitionState.setDiagramReportID(wsTrendDefinition.getDiagramReportID());
            trendDefinitionState.setLinks(wsTrendDefinition.getLinks());
            analysisDefinitionState = trendDefinitionState;
        } else if (wsAnalysisDefinition.getDataFeedType().equals(AnalysisTypes.TOPO)) {
            WSMap wsTextDefinition = (WSMap) wsAnalysisDefinition;
            TopoMapDefinitionState textDefinitionState = new TopoMapDefinitionState();
            textDefinitionState.setTopoDefinitionID(wsTextDefinition.getMapID());
            analysisDefinitionState = textDefinitionState;
        } else if (wsAnalysisDefinition.getDataFeedType().equals(AnalysisTypes.TEXT)) {
            WSTextDefinition wsTextDefinition = (WSTextDefinition) wsAnalysisDefinition;
            TextDefinitionState textDefinitionState = new TextDefinitionState();
            textDefinitionState.setTextReportID(wsTextDefinition.getTextReportID());
            textDefinitionState.setReportText(wsTextDefinition.getText());
            analysisDefinitionState = textDefinitionState;
        } else if (wsAnalysisDefinition.getDataFeedType().equals(AnalysisTypes.MULTI_SUMMARY)) {
            WSMultiSummaryDefinition wsMultiSummaryDefinition = (WSMultiSummaryDefinition) wsAnalysisDefinition;
            MultiSummaryDefinitionState multiSummaryDefinitionState = new MultiSummaryDefinitionState();
            multiSummaryDefinitionState.setMultiSummaryID(wsMultiSummaryDefinition.getMultiSummaryID());
            List<ReportStub> stubs = new ArrayList<>();
            for (InsightDescriptor insightDescriptor : wsMultiSummaryDefinition.getReports()) {
                ReportStub reportStub = new ReportStub();
                reportStub.setReportID(insightDescriptor.getId());
                stubs.add(reportStub);
            }
            multiSummaryDefinitionState.setSummaryReports(stubs);
            analysisDefinitionState = multiSummaryDefinitionState;
        } else {
            throw new RuntimeException("Unknown data feed type " + wsAnalysisDefinition.getDataFeedType());
        }
        analysisDefinitionState.setId(wsAnalysisDefinition.getReportStateID());
        AnalysisDefinition analysisDefinition = new AnalysisDefinition();
        analysisDefinition.setProperties(wsAnalysisDefinition.createProperties());
        for (ReportProperty reportProperty : analysisDefinition.getProperties()) {
            reportProperty.cleanup();
        }
        List<ReportStub> reportStubs = new ArrayList<ReportStub>();
        if (wsAnalysisDefinition.getAddonReports() != null) {
            for (AddonReport addonReport : wsAnalysisDefinition.getAddonReports()) {
                ReportStub reportStub = new ReportStub();
                reportStub.setReportID(addonReport.getReportID());
                reportStubs.add(reportStub);
            }
        }
        analysisDefinition.setReportStubs(reportStubs);
        List<FilterSetStub> filterSetStubs = new ArrayList<FilterSetStub>();
        if (wsAnalysisDefinition.getFilterSets() != null) {
            for (FilterSetDescriptor filterSetDescriptor : wsAnalysisDefinition.getFilterSets()) {
                FilterSetStub stub = new FilterSetStub();
                stub.setFilterSetID(filterSetDescriptor.getId());
                filterSetStubs.add(stub);
            }
        }
        analysisDefinition.setFilterSets(filterSetStubs);
        analysisDefinition.setUrlKey(wsAnalysisDefinition.getUrlKey());
        analysisDefinition.setJoinOverrides(wsAnalysisDefinition.getJoinOverrides());
        analysisDefinition.setAnalysisDefinitionState(analysisDefinitionState);
        analysisDefinition.setRecommendedExchange(wsAnalysisDefinition.isRecommendedExchange());
        analysisDefinition.setAutoSetupDelivery(wsAnalysisDefinition.isAutoSetupDelivery());
        analysisDefinition.setDescription(wsAnalysisDefinition.getDescription());
        analysisDefinition.setAuthorName(wsAnalysisDefinition.getAuthorName());
        analysisDefinition.setMarmotScript(wsAnalysisDefinition.getMarmotScript());
        analysisDefinition.setReportRunMarmotScript(wsAnalysisDefinition.getReportRunMarmotScript());
        analysisDefinition.setAccountVisible(wsAnalysisDefinition.isAccountVisible());
        analysisDefinition.setFolder(wsAnalysisDefinition.getFolder());
        analysisDefinition.setDateCreated(wsAnalysisDefinition.getDateCreated());
        analysisDefinition.setPublicWithKey(wsAnalysisDefinition.isPublicWithKey());
        analysisDefinition.setDateUpdated(wsAnalysisDefinition.getDateUpdated());
        analysisDefinition.setReportType(wsAnalysisDefinition.getReportType());
        analysisDefinition.setReportStructure(wsAnalysisDefinition.createStructure());
        analysisDefinition.setAddedItems(wsAnalysisDefinition.getAddedItems());
        analysisDefinition.setFilterDefinitions(FilterDefinitionConverter.fromFilters(wsAnalysisDefinition.getFilterDefinitions()));
        analysisDefinition.setAnalysisID(wsAnalysisDefinition.getAnalysisID());
        analysisDefinition.setTitle(wsAnalysisDefinition.getName());
        analysisDefinition.setAnalysisPolicy(wsAnalysisDefinition.getPolicy());
        analysisDefinition.setDataFeedID(wsAnalysisDefinition.getDataFeedID());
        analysisDefinition.setPubliclyVisible(wsAnalysisDefinition.isPubliclyVisible());
        analysisDefinition.setMarketplaceVisible(wsAnalysisDefinition.isMarketplaceVisible());
        analysisDefinition.setTemporaryReport(wsAnalysisDefinition.isTemporaryReport());
        analysisDefinition.setSolutionVisible(wsAnalysisDefinition.isSolutionVisible());
        analysisDefinition.setDataSourceFieldReport(wsAnalysisDefinition.isDataSourceFieldReport());
        return analysisDefinition;
    }
}
