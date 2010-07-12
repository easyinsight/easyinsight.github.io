package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;

import java.util.*;

/**
 * User: jamesboe
 * Date: Feb 9, 2010
 * Time: 11:01:57 AM
 */
public class CompositeReportPipeline extends Pipeline {
    @Override
    protected List<IComponent> generatePipelineCommands(Set<AnalysisItem> allNeededAnalysisItems, Set<AnalysisItem> reportItems, Collection<FilterDefinition> filters, WSAnalysisDefinition report, Map<Key, Integer> refMap) {
        List<IComponent> components = new ArrayList<IComponent>();
        for (AnalysisItem range : items(AnalysisItemTypes.RANGE_DIMENSION, allNeededAnalysisItems)) {
            components.add(new RangeComponent((AnalysisRangeDimension) range));
        }
        components.add(new AggregationComponent());
        return components;
    }
}
