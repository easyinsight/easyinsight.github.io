package com.easyinsight.pipeline;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.InsightRequestMetadata;
import com.easyinsight.analysis.WSAnalysisDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * User: jamesboe
 * Date: Feb 9, 2010
 * Time: 11:01:57 AM
 */
public class CompositeReportFinishPipeline extends Pipeline {
    @Override
    protected List<IComponent> generatePipelineCommands(Set<AnalysisItem> allNeededAnalysisItems, Set<AnalysisItem> reportItems, Collection<FilterDefinition> filters, WSAnalysisDefinition report, List<AnalysisItem> allItems, InsightRequestMetadata insightRequestMetadata) {
        List<IComponent> components = new ArrayList<IComponent>();
        components.add(new CleanupComponent(Pipeline.BEFORE, true));
        components.add(new NormalizationComponent());
        components.add(new AggregationComponent(AggregationComponent.OTHER));

        return components;
    }
}
