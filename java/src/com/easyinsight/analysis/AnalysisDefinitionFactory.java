package com.easyinsight.analysis;

import com.easyinsight.analysis.AnalysisTypes;
import com.easyinsight.analysis.AnalysisItemFactory;

/**
 * User: James Boe
 * Date: Jan 10, 2008
 * Time: 8:08:08 PM
 */
public class AnalysisDefinitionFactory {

    public static AnalysisDefinition fromWSDefinition(WSAnalysisDefinition wsAnalysisDefinition) {
        AnalysisDefinition analysisDefinition;
        if (wsAnalysisDefinition.getDataFeedType().equals(AnalysisTypes.CROSSTAB)) {
            WSCrosstabDefinition wsCrosstabDefinition = (WSCrosstabDefinition) wsAnalysisDefinition;
            CrosstabDefinition crosstabDefinition = new CrosstabDefinition();
            crosstabDefinition.setColumns(AnalysisItemFactory.createCrosstabColumns(wsCrosstabDefinition.getColumns()));
            crosstabDefinition.setMeasures(AnalysisItemFactory.createCrosstabMeasures(wsCrosstabDefinition.getMeasures()));
            crosstabDefinition.setRows(AnalysisItemFactory.createCrosstabRows(wsCrosstabDefinition.getRows()));
            crosstabDefinition.setCrosstabDefinitionID(wsCrosstabDefinition.getCrosstabDefinitionID());
            analysisDefinition = crosstabDefinition;
        } else if (wsAnalysisDefinition.getDataFeedType().equals(AnalysisTypes.LIST)) {
            WSListDefinition wsListDefinition = (WSListDefinition) wsAnalysisDefinition;
            ListDefinition listDefinition = new ListDefinition();
            listDefinition.setListDefinitionID(wsListDefinition.getListDefinitionID());
            listDefinition.setColumns(wsListDefinition.getColumns());
            listDefinition.setShowRowNumbers(wsListDefinition.isShowLineNumbers());
            listDefinition.setListLimitsMetadata(wsListDefinition.getListLimitsMetadata());
            analysisDefinition = listDefinition;
        } else if (wsAnalysisDefinition.getDataFeedType().equals(AnalysisTypes.CHART)) {
            WSChartDefinition wsChart = (WSChartDefinition) wsAnalysisDefinition;
            ChartDefinition chartDefinition = new ChartDefinition();
            chartDefinition.setDimensions(AnalysisItemFactory.createChartDimensions(wsChart.getDimensions()));
            chartDefinition.setMeasures(AnalysisItemFactory.createChartMeasures(wsChart.getMeasures()));
            chartDefinition.setChartFamily(wsChart.getChartFamily());
            chartDefinition.setChartType(wsChart.getChartType());
            chartDefinition.setChartDefinitionID(wsChart.getChartDefinitionID());
            analysisDefinition = chartDefinition;
        } else if (wsAnalysisDefinition.getDataFeedType().equals(AnalysisTypes.MAP)) {
            WSMapDefinition wsMap = (WSMapDefinition) wsAnalysisDefinition;
            MapDefinition mapDefinition = new MapDefinition();
            mapDefinition.setDimensions(AnalysisItemFactory.createChartDimensions(wsMap.getDimensions()));
            mapDefinition.setMeasures(AnalysisItemFactory.createChartMeasures(wsMap.getMeasures()));
            mapDefinition.setMapType(wsMap.getMapType());
            mapDefinition.setMapDefinitionID(wsMap.getMapDefinitionID());
            analysisDefinition = mapDefinition;
        } else if (wsAnalysisDefinition.getDataFeedType().equals(AnalysisTypes.YAHOO_MAP)) {
            WSYahooMapDefinition wsMap = (WSYahooMapDefinition) wsAnalysisDefinition;
            YahooMapDefinition mapDefinition = new YahooMapDefinition();
            mapDefinition.setDimensions(AnalysisItemFactory.createChartDimensions(wsMap.getDimensions()));
            mapDefinition.setMeasures(AnalysisItemFactory.createChartMeasures(wsMap.getMeasures()));
            mapDefinition.setYahooMapDefinitionID(wsMap.getYahooMapDefinitionID());
            analysisDefinition = mapDefinition;
        } else {
            throw new RuntimeException("Unknown data feed type " + wsAnalysisDefinition.getDataFeedType());
        }
        analysisDefinition.setDataScrubs(wsAnalysisDefinition.getDataScrubs());
        analysisDefinition.setHierarchies(wsAnalysisDefinition.getHierarchies());
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
