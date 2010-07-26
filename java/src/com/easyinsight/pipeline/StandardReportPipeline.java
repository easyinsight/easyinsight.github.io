package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.datafeeds.FeedService;
import com.easyinsight.etl.LookupTable;

import java.util.*;

/**
 * User: James Boe
 * Date: May 19, 2009
 * Time: 9:36:30 AM
 */
public class StandardReportPipeline extends Pipeline {

    private static class PointPopulation {
        private AnalysisZipCode analysisZipCode;
        private AnalysisLongitude analysisLongitude;
        private AnalysisLatitude analysisLatitude;
    }

    protected List<IComponent> generatePipelineCommands(Set<AnalysisItem> allNeededAnalysisItems, Set<AnalysisItem> reportItems, Collection<FilterDefinition> filters, WSAnalysisDefinition report, Map<Key, Integer> refMap) {

        // current problematic scenarios
        // a measure filter on a calculation
        // a calculation on a calculation
        // a tag filter?
        //

        // there's still a fundamental challenge here...

        // ordering of the pipeline operations

        List<IComponent> components = new ArrayList<IComponent>();

        Map<String, PointPopulation> map = new HashMap<String, PointPopulation>();
        for (AnalysisItem coordinate : items(AnalysisItemTypes.LONGITUDE, allNeededAnalysisItems)) {
            AnalysisCoordinate analysisCoordinate = (AnalysisCoordinate) coordinate;
            if (analysisCoordinate.getAnalysisZipCode() != null) {
                PointPopulation pointPopulation = map.get(analysisCoordinate.getAnalysisZipCode().toDisplay());
                if (pointPopulation == null) {
                    pointPopulation = new PointPopulation();
                    pointPopulation.analysisZipCode = analysisCoordinate.getAnalysisZipCode();
                    map.put(analysisCoordinate.getAnalysisZipCode().toDisplay(), pointPopulation);
                }
                pointPopulation.analysisLongitude = (AnalysisLongitude) analysisCoordinate;
            }
        }
        for (AnalysisItem coordinate : items(AnalysisItemTypes.LATITUDE, allNeededAnalysisItems)) {
            AnalysisCoordinate analysisCoordinate = (AnalysisCoordinate) coordinate;
            if (analysisCoordinate.getAnalysisZipCode() != null) {
                PointPopulation pointPopulation = map.get(analysisCoordinate.getAnalysisZipCode().toDisplay());
                if (pointPopulation == null) {
                    pointPopulation = new PointPopulation();
                    pointPopulation.analysisZipCode = analysisCoordinate.getAnalysisZipCode();
                    map.put(analysisCoordinate.getAnalysisZipCode().toDisplay(), pointPopulation);
                }
                pointPopulation.analysisLatitude = (AnalysisLatitude) analysisCoordinate;
            }
        }

        for (PointPopulation pointPopulation : map.values()) {
            components.add(new CoordinateComponent(pointPopulation.analysisZipCode, pointPopulation.analysisLatitude, pointPopulation.analysisLongitude));
        }

        for (AnalysisItem analysisItem : items(AnalysisItemTypes.LONGITUDE, allNeededAnalysisItems)) {
            components.add(new CoordinatePrecisionComponent((AnalysisCoordinate) analysisItem));
        }

        for (AnalysisItem analysisItem : items(AnalysisItemTypes.LATITUDE, allNeededAnalysisItems)) {
            components.add(new CoordinatePrecisionComponent((AnalysisCoordinate) analysisItem));
        }

        for (AnalysisItem analysisItem : allNeededAnalysisItems) {
            if (analysisItem.getLookupTableID() != null && analysisItem.getLookupTableID() > 0) {
                LookupTable lookupTable = new FeedService().getLookupTable(analysisItem.getLookupTableID());
                components.add(new LookupTableComponent(lookupTable, refMap.get(lookupTable.getSourceField().createAggregateKey())));
            }
        }

        components.add(new DataScrubComponent());
        for (AnalysisItem tag : items(AnalysisItemTypes.LISTING, allNeededAnalysisItems)) {
            AnalysisList analysisList = (AnalysisList) tag;
            if (analysisList.isMultipleTransform()) components.add(new TagTransformComponent(analysisList));
        }        

        for (AnalysisItem calc : items(AnalysisItemTypes.DERIVED_DIMENSION, allNeededAnalysisItems)) {
            DerivedAnalysisDimension calculation = (DerivedAnalysisDimension) calc;
            components.add(new DerivedGroupingComponent(calculation));
            components.add(new DerivedDimensionCleanupComponent(calculation));
        }

        for (AnalysisItem range : items(AnalysisItemTypes.RANGE_DIMENSION, allNeededAnalysisItems)) {
            components.add(new RangeComponent((AnalysisRangeDimension) range));
        }
        components.add(new TypeTransformComponent());

        for (AnalysisItem calc : items(AnalysisItemTypes.CALCULATION, allNeededAnalysisItems)) {
            AnalysisCalculation calculation = (AnalysisCalculation) calc;
            if (calculation.isApplyBeforeAggregation()) {
                components.add(new CalculationComponent(calculation));
                components.add(new CalculationCleanupComponent(calculation));
            }
        }

        components.add(new FilterComponent(true));
        components.add(new FilterPipelineCleanupComponent(true));

        components.add(new MeasureFilterComponent());
        components.add(new MeasureFilterPipelineCleanupComponent());

        if (report.getFilterDefinitions() != null) {
            for (FilterDefinition filterDefinition : report.getFilterDefinitions()) {
                components.addAll(filterDefinition.createComponents());       
            }
        }



        for (AnalysisItem step : items(AnalysisItemTypes.STEP, allNeededAnalysisItems)) {
            components.add(new StepCorrelationComponent((AnalysisStep) step));
            components.add(new StepTransformComponent((AnalysisStep) step));
        }
        if (filters != null) {
            for (FilterDefinition filterDefinition : filters) {
                if (filterDefinition instanceof LastValueFilter) {
                    components.add(new LastValueComponent((LastValueFilter) filterDefinition));
                }
            }
        }
        components.add(new AggregationComponent());

        // TODO: if a calculation is based on second calculation, populate results with the first calculation first
        // directed graph required? or just list

        for(AnalysisItem calc : items(AnalysisItemTypes.CALCULATION, allNeededAnalysisItems)) {
            AnalysisCalculation calculation = (AnalysisCalculation) calc;
            if (!calculation.isApplyBeforeAggregation()) {
                components.add(new CalculationComponent(calculation));
                components.add(new CalculationCleanupComponent(calculation));
            }
        }

        components.add(new AggregationComponent(AggregationTypes.RANK));

        components.add(new LinkDecorationComponent());
        components.add(new FilterComponent(false));
        components.add(new LimitsComponent());
        components.addAll(report.createComponents());        
        components.add(new SortComponent());
        return components;
    }
}
