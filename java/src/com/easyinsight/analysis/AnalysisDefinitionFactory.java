package com.easyinsight.analysis;

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
            //treeMapDefinitionState.setTreemapDefinitionID(wsTree.getTreeMapDefinitionID());
            treeMapDefinitionState.setColorScheme(wsTree.getColorScheme());
            analysisDefinitionState = treeMapDefinitionState;
        } else if (wsAnalysisDefinition.getDataFeedType().equals(AnalysisTypes.TREE)) {
            WSTreeDefinition wsTree = (WSTreeDefinition) wsAnalysisDefinition;
            TreeDefinitionState treeDefinitionState = new TreeDefinitionState();
            treeDefinitionState.setTreeDefinitionID(wsTree.getTreeDefinitionID());
            analysisDefinitionState = treeDefinitionState;
        } else {
            throw new RuntimeException("Unknown data feed type " + wsAnalysisDefinition.getDataFeedType());
        }
        analysisDefinitionState.setId(wsAnalysisDefinition.getReportStateID());
        AnalysisDefinition analysisDefinition = new AnalysisDefinition();
        analysisDefinition.setAnalysisDefinitionState(analysisDefinitionState);
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
        analysisDefinition.setVisibleAtFeedLevel(wsAnalysisDefinition.isVisibleAtFeedLevel());
        return analysisDefinition;
    }
}
