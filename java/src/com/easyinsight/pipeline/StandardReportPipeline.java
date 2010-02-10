package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSPlotChartDefinition;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

/**
 * User: James Boe
 * Date: May 19, 2009
 * Time: 9:36:30 AM
 */
public class StandardReportPipeline extends Pipeline {
    protected List<IComponent> generatePipelineCommands(Set<AnalysisItem> allNeededAnalysisItems, Set<AnalysisItem> reportItems, Collection<FilterDefinition> filters, WSAnalysisDefinition report) {
        List<IComponent> components = new ArrayList<IComponent>();
        
        components.add(new DataScrubComponent());
        components.add(new TagTransformComponent());

        for (AnalysisItem range : items(AnalysisItemTypes.RANGE_DIMENSION, allNeededAnalysisItems)) {
            components.add(new RangeComponent((AnalysisRangeDimension) range));
        }
        //components.add(new VirtualDimensionComponent());
        components.add(new TypeTransformComponent());

        // for each filtered measure, apply an additional filtering component

        // 

        // then join them back together

        components.add(new FilterComponent(true));
        components.add(new FilterPipelineCleanupComponent());

        components.add(new MeasureFilterComponent());
        components.add(new MeasureFilterPipelineCleanupComponent());

        for (AnalysisItem calc : items(AnalysisItemTypes.CALCULATION, reportItems)) {
            AnalysisCalculation calculation = (AnalysisCalculation) calc;
            if (calculation.isApplyBeforeAggregation()) {
                components.add(new CalculationComponent(calculation));
            }
        }

        for (AnalysisItem step : items(AnalysisItemTypes.STEP, allNeededAnalysisItems)) {
            components.add(new StepCorrelationComponent((AnalysisStep) step));
        }
        boolean temporalAdded = false;
        if (filters != null) {
            for (FilterDefinition filterDefinition : filters) {
                if (filterDefinition instanceof LastValueFilter) {
                    components.add(new LastValueComponent((LastValueFilter) filterDefinition));
                }
            }
        }

        //if (!temporalAdded) {
            //components.add(new BroadAggregationComponent());
        components.add(new AggregationComponent());

        // TODO: if a calculation is based on second calculation, populate results with the first calculation first
        // directed graph required? or just list

        for(AnalysisItem calc : items(AnalysisItemTypes.CALCULATION, reportItems)) {
            AnalysisCalculation calculation = (AnalysisCalculation) calc;
            if (!calculation.isApplyBeforeAggregation()) {
                components.add(new CalculationComponent(calculation));
            }
        }

        for (AnalysisItem temporal : items(AnalysisItemTypes.TEMPORAL_MEASURE, reportItems)) {
            temporalAdded = true;
            components.add(new TemporalComponent((TemporalAnalysisMeasure) temporal));
        }

        components.add(new LinkDecorationComponent());
        components.add(new FilterComponent(false));
        components.add(new LimitsComponent());
        components.addAll(report.createComponents());        
        components.add(new SortComponent());
        return components;
    }
}
