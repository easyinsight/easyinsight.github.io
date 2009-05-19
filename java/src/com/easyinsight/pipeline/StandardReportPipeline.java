package com.easyinsight.pipeline;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.AnalysisStep;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: May 19, 2009
 * Time: 9:36:30 AM
 */
public class StandardReportPipeline extends Pipeline {
    protected List<IComponent> generatePipelineCommands(Set<AnalysisItem> allNeededAnalysisItems) {
        List<IComponent> components = new ArrayList<IComponent>();
        components.add(new DataScrubComponent());
        components.add(new TagTransformComponent());
        components.add(new RangeComponent());
        components.add(new VirtualDimensionComponent());
        components.add(new TypeTransformComponent());
        components.add(new FilterComponent(true));
        for (AnalysisItem step : items(AnalysisItemTypes.STEP, allNeededAnalysisItems)) {
            components.add(new StepCorrelationComponent((AnalysisStep) step));
        }
        components.add(new AggregationComponent());
        components.add(new FilterComponent(false));
        components.add(new LimitsComponent());
        return components;
    }
}
