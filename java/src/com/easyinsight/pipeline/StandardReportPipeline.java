package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;

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
    protected List<IComponent> generatePipelineCommands(Set<AnalysisItem> allNeededAnalysisItems, Set<AnalysisItem> reportItems, Collection<FilterDefinition> filters) {
        List<IComponent> components = new ArrayList<IComponent>();
        components.add(new DataScrubComponent());
        components.add(new TagTransformComponent());
        components.add(new RangeComponent());
        components.add(new VirtualDimensionComponent());
        components.add(new TypeTransformComponent());
        components.add(new FilterComponent(true));
        components.add(new FilterPipelineCleanupComponent());
        for (AnalysisItem step : items(AnalysisItemTypes.STEP, allNeededAnalysisItems)) {
            components.add(new StepCorrelationComponent((AnalysisStep) step));
        }
        //boolean temporalAdded = false;
        if (filters != null) {
            for (FilterDefinition filterDefinition : filters) {
                if (filterDefinition instanceof LastValueFilter) {
                    components.add(new LastValueComponent((LastValueFilter) filterDefinition));
                }
            }
        }
        /*for (AnalysisItem temporal : items(AnalysisItemTypes.TEMPORAL_MEASURE, reportItems)) {
            temporalAdded = true;
            components.add(new TemporalComponent((TemporalAnalysisMeasure) temporal));
        }
        if (!temporalAdded) {*/
            components.add(new AggregationComponent());
        //}
        components.add(new FilterComponent(false));
        components.add(new LimitsComponent());
        return components;
    }
}
