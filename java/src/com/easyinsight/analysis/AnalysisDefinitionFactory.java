package com.easyinsight.analysis;

import com.easyinsight.analysis.definitions.WSHeatMap;
import com.easyinsight.analysis.definitions.WSTimeline;
import com.easyinsight.analysis.gauge.GaugeDefinitionState;

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
            if (Double.isNaN(wsChart.getElevationAngle())) {
                wsChart.setElevationAngle(0);
            }
            if (Double.isNaN(wsChart.getRotationAngle())) {
                wsChart.setRotationAngle(0);
            }
            chartDefinition.setElevationAngle(wsChart.getElevationAngle());
            chartDefinition.setRotationAngle(wsChart.getRotationAngle());
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
            gaugeDefinition.setGaugeType(wsGauge.getGaugeType());
            gaugeDefinition.setMaxValue(wsGauge.getMaxValue());
            gaugeDefinition.setGaugeDefinitionID(wsGauge.getGaugeDefinitionID());
            analysisDefinitionState = gaugeDefinition;
        } else if (wsAnalysisDefinition.getDataFeedType().equals(AnalysisTypes.TREEMAP)) {
            WSTreeMapDefinition wsTree = (WSTreeMapDefinition) wsAnalysisDefinition;
            TreeMapDefinitionState treeMapDefinitionState = new TreeMapDefinitionState();
            treeMapDefinitionState.setDefinitionID(wsTree.getTreeMapDefinitionID());
            treeMapDefinitionState.setColorScheme(wsTree.getColorScheme());
            analysisDefinitionState = treeMapDefinitionState;
        } else if (wsAnalysisDefinition.getDataFeedType().equals(AnalysisTypes.TREE)) {
            WSTreeDefinition wsTree = (WSTreeDefinition) wsAnalysisDefinition;
            TreeDefinitionState treeDefinitionState = new TreeDefinitionState();
            treeDefinitionState.setTreeDefinitionID(wsTree.getTreeDefinitionID());
            analysisDefinitionState = treeDefinitionState;
        } else if (wsAnalysisDefinition.getDataFeedType().equals(AnalysisTypes.TIMELINE)) {
            WSTimeline wsTimeline = (WSTimeline) wsAnalysisDefinition;
            TimelineDefinitionState timelineDefinitionState = new TimelineDefinitionState();
            timelineDefinitionState.setDefinitionID(wsTimeline.getTimelineID());
            timelineDefinitionState.setFilter(wsTimeline.getSequence());
            timelineDefinitionState.setContainedReport(AnalysisDefinitionFactory.fromWSDefinition(wsTimeline.getReport()));
            analysisDefinitionState = timelineDefinitionState;
        } else if (wsAnalysisDefinition.getDataFeedType().equals(AnalysisTypes.HEATMAP)) {
            WSHeatMap heatMap = (WSHeatMap) wsAnalysisDefinition;
            HeatMapDefinitionState heatMapDefinitionState = new HeatMapDefinitionState();
            heatMapDefinitionState.setLatitude(heatMap.getLatitude());
            heatMapDefinitionState.setLongitude(heatMap.getLongitude());
            heatMapDefinitionState.setMapType(heatMap.getMapType());
            heatMapDefinitionState.setZoomLevel(heatMap.getZoomLevel());
            heatMapDefinitionState.setHeatMapDefinitionID(heatMap.getHeatMapID());
            heatMapDefinitionState.setMinLat(heatMap.getMinLat());
            heatMapDefinitionState.setMinLong(heatMap.getMinLong());
            heatMapDefinitionState.setMaxLat(heatMap.getMaxLat());
            heatMapDefinitionState.setMaxLong(heatMap.getMaxLong());
            analysisDefinitionState = heatMapDefinitionState;
        } else {
            throw new RuntimeException("Unknown data feed type " + wsAnalysisDefinition.getDataFeedType());
        }
        analysisDefinitionState.setId(wsAnalysisDefinition.getReportStateID());
        AnalysisDefinition analysisDefinition = new AnalysisDefinition();
        analysisDefinition.setProperties(wsAnalysisDefinition.createProperties());
        analysisDefinition.setUrlKey(wsAnalysisDefinition.getUrlKey());
        analysisDefinition.setAnalysisDefinitionState(analysisDefinitionState);
        analysisDefinition.setDescription(wsAnalysisDefinition.getDescription());
        analysisDefinition.setAuthorName(wsAnalysisDefinition.getAuthorName());
        analysisDefinition.setAccountVisible(wsAnalysisDefinition.isAccountVisible());
        analysisDefinition.setDateCreated(wsAnalysisDefinition.getDateCreated());
        analysisDefinition.setDateUpdated(wsAnalysisDefinition.getDateUpdated());
        analysisDefinition.setReportType(wsAnalysisDefinition.getReportType());
        analysisDefinition.setReportStructure(wsAnalysisDefinition.createStructure());
        analysisDefinition.setDataScrubs(wsAnalysisDefinition.getDataScrubs());
        analysisDefinition.setAddedItems(wsAnalysisDefinition.getAddedItems());
        analysisDefinition.setFilterDefinitions(FilterDefinitionConverter.fromFilters(wsAnalysisDefinition.getFilterDefinitions()));
        analysisDefinition.setAnalysisID(wsAnalysisDefinition.getAnalysisID());
        analysisDefinition.setTitle(wsAnalysisDefinition.getName());
        analysisDefinition.setAnalysisPolicy(wsAnalysisDefinition.getPolicy());
        analysisDefinition.setDataFeedID(wsAnalysisDefinition.getDataFeedID());
        analysisDefinition.setTags(wsAnalysisDefinition.getTagCloud());
        analysisDefinition.setRootDefinition(wsAnalysisDefinition.isRootDefinition());
        analysisDefinition.setPubliclyVisible(wsAnalysisDefinition.isPubliclyVisible());
        analysisDefinition.setMarketplaceVisible(wsAnalysisDefinition.isMarketplaceVisible());
        analysisDefinition.setTemporaryReport(wsAnalysisDefinition.isTemporaryReport());
        analysisDefinition.setSolutionVisible(wsAnalysisDefinition.isSolutionVisible());
        analysisDefinition.setVisibleAtFeedLevel(wsAnalysisDefinition.isVisibleAtFeedLevel());
        return analysisDefinition;
    }
}
