package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;

import java.util.*;

/**
 * User: jamesboe
 * Date: Feb 9, 2010
 * Time: 11:01:57 AM
 */
public class CompositeReportPipeline extends Pipeline {
    @Override
    protected List<IComponent> generatePipelineCommands(Set<AnalysisItem> allNeededAnalysisItems, Set<AnalysisItem> reportItems, Collection<FilterDefinition> filters, WSAnalysisDefinition report, List<AnalysisItem> allItems, InsightRequestMetadata insightRequestMetadata) {
        List<IComponent> components = new ArrayList<IComponent>();
        components.add(new NormalizationComponent());
        components.add(new AggregationComponent(AggregationComponent.OTHER));

        return components;
    }
}
