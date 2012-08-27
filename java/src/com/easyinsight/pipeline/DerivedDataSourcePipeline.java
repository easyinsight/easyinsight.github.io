package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;

import java.util.*;

/**
 * User: James Boe
 * Date: May 19, 2009
 * Time: 9:38:23 AM
 */
public class DerivedDataSourcePipeline extends Pipeline {
    protected List<IComponent> generatePipelineCommands(Set<AnalysisItem> allNeededAnalysisItems, Set<AnalysisItem> reportItems, Collection<FilterDefinition> filters, WSAnalysisDefinition report, List<AnalysisItem> allItems, InsightRequestMetadata insightRequestMetadata) {
        List<IComponent> components = new ArrayList<IComponent>();
        if (report.getFilterDefinitions() != null) {
            for (FilterDefinition filterDefinition : report.getFilterDefinitions()) {
                components.addAll(filterDefinition.createComponents(Pipeline.BEFORE, new DefaultFilterProcessor(), null, false));
            }
        }
        if (report.getFilterDefinitions() != null) {
            for (FilterDefinition filterDefinition : report.getFilterDefinitions()) {
                components.addAll(filterDefinition.createComponents(Pipeline.AFTER, new DefaultFilterProcessor(), null, false));
            }
        }
        return components;
    }
}
