package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSHeatMap;
import com.easyinsight.calculations.CalcGraph;
import com.easyinsight.datafeeds.FeedService;
import com.easyinsight.etl.LookupTable;

import java.util.*;

/**
 * User: James Boe
 * Date: May 19, 2009
 * Time: 9:36:30 AM
 */
public class StandardReportPipeline extends Pipeline {

    protected List<IComponent> generatePipelineCommands(Set<AnalysisItem> allNeededAnalysisItems, Set<AnalysisItem> reportItems, Collection<FilterDefinition> filters, WSAnalysisDefinition report, List<AnalysisItem> allItems, InsightRequestMetadata insightRequestMetadata) {

        List<IComponent> components = new ArrayList<IComponent>();

        for (AnalysisItem analysisItem : allNeededAnalysisItems) {
            if (analysisItem.getLookupTableID() != null && analysisItem.getLookupTableID() > 0) {
                LookupTable lookupTable = new FeedService().getLookupTable(analysisItem.getLookupTableID());
                if (lookupTable != null && lookupTable.getSourceField() != null) {
                    if (lookupTable.getSourceField().hasType(AnalysisItemTypes.LISTING)) {
                        AnalysisList analysisList = (AnalysisList) lookupTable.getSourceField();
                        if (analysisList.isMultipleTransform()) components.add(new TagTransformComponent(analysisList));
                    } else if (lookupTable.getSourceField().hasType(AnalysisItemTypes.DERIVED_DIMENSION)) {
                        Set<AnalysisItem> analysisItems = new HashSet<AnalysisItem>();
                        analysisItems.add(lookupTable.getSourceField());
                        components.addAll(new CalcGraph().doFunGraphStuff(analysisItems, allItems, reportItems, Pipeline.BEFORE, getStructure()));
                    }
                    components.add(new LookupTableComponent(lookupTable));
                }
            }
        }

        /*Set<AnalysisItem> items = new HashSet<AnalysisItem>(reportItems);
        for (AnalysisItem item : allNeededAnalysisItems) {
            if (item.hasType(AnalysisItemTypes.CALCULATION) || item.hasType(AnalysisItemTypes.DERIVED_DIMENSION) ||
                    item.hasType(AnalysisItemTypes.DERIVED_DATE)) {
                items.addAll(item.getAnalysisItems(allItems, reportItems, false, false, items, getStructure()));
            }
        }*/


        for (AnalysisItem tag : items(AnalysisItemTypes.LISTING, reportItems)) {
            AnalysisList analysisList = (AnalysisList) tag;
            if (analysisList.isMultipleTransform()) components.add(new TagTransformComponent(analysisList));
        }

        components.addAll(new CalcGraph().doFunGraphStuff(allNeededAnalysisItems, allItems, reportItems, Pipeline.BEFORE, getStructure()));

        if (report instanceof WSHeatMap) {
            WSHeatMap heatMap = (WSHeatMap) report;
            if (heatMap.getZipCode() != null) {
                components.add(new CoordinateComponent(heatMap.getZipCode()));
            }
            if (heatMap.getLongitudeItem() != null) {
                components.add(new CoordinatePrecisionComponent(heatMap.getLongitudeItem()));
            }
            if (heatMap.getLatitudeItem() != null) {
                components.add(new CoordinatePrecisionComponent(heatMap.getLatitudeItem()));
            }
        }

        for (AnalysisItem range : items(AnalysisItemTypes.RANGE_DIMENSION, allNeededAnalysisItems)) {
            components.add(new RangeComponent((AnalysisRangeDimension) range));
        }
        components.add(new TypeTransformComponent());

        if (report.getFilterDefinitions() != null) {
            for (FilterDefinition filterDefinition : report.retrieveFilterDefinitions()) {
                if (!insightRequestMetadata.isLookupTableAggregate() || !filterDefinition.validForQuery()) {
                    components.addAll(filterDefinition.createComponents(true, new DefaultFilterProcessor(), null, false));
                }
            }
        }

        FieldFilterComponent fieldFilterComponent = new FieldFilterComponent();
        components.add(fieldFilterComponent);
        for (AnalysisItem analysisItem : allNeededAnalysisItems) {
            if (analysisItem.getFilters() != null) {
                if (analysisItem.hasType(AnalysisItemTypes.CALCULATION)) {
                    AnalysisCalculation analysisCalculation = (AnalysisCalculation) analysisItem;
                    if (!analysisCalculation.isApplyBeforeAggregation()) {
                        continue;
                    }
                }
                for (FilterDefinition filterDefinition : analysisItem.getFilters()) {
                    if (filterDefinition.getField() != null) {
                        fieldFilterComponent.addFilterPair(analysisItem, filterDefinition);
                    } else {
                        components.addAll(filterDefinition.createComponents(true, new FieldFilterProcessor(analysisItem), analysisItem, true));
                    }
                }
            }
        }

        for (AnalysisItem step : items(AnalysisItemTypes.STEP, allNeededAnalysisItems)) {
            components.add(new StepCorrelationComponent((AnalysisStep) step));
            components.add(new StepTransformComponent((AnalysisStep) step));
        }

        // done with row level operations, clean everything up

        boolean measureFilter = false;
        for (AnalysisItem analysisCalculation : items(AnalysisItemTypes.CALCULATION, allNeededAnalysisItems)) {
            if (analysisCalculation.getFilters() != null && analysisCalculation.getFilters().size() > 0) {
                measureFilter = false;
            }
        }

        components.add(new CleanupComponent(Pipeline.AFTER, measureFilter));
        components.add(new NormalizationComponent());
        components.add(new AggregationComponent(AggregationComponent.OTHER));

        List<IComponent> postAggCalculations = new CalcGraph().doFunGraphStuff(allNeededAnalysisItems, allItems, reportItems, Pipeline.AFTER, getStructure());
        components.addAll(postAggCalculations);

        // need another cleanup component here...
        components.add(new CleanupComponent(Pipeline.LAST, false));
        
        /*for (AnalysisItem analysisItem : reportItems) {
            if (analysisItem.getSortItem() != null) {
                components.add(new SortDecorationComponent(analysisItem));
            }
        }*/

        components.add(new AggregationComponent(AggregationComponent.FINAL, AggregationTypes.RANK));

        components.add(new LinkDecorationComponent());
        if (report.getFilterDefinitions() != null) {
            for (FilterDefinition filterDefinition : report.retrieveFilterDefinitions()) {
                components.addAll(filterDefinition.createComponents(false, new DefaultFilterProcessor(), null, false));
            }
        }
        components.add(new LimitsComponent());
        components.addAll(report.createComponents());
        components.add(new MarmotHerderComponent());
        components.add(new SortComponent());
        components.add(new DateHackComponent());
        return components;
    }
}
