package com.easyinsight.pipeline;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.WSAnalysisDefinition;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collection;

/**
 * User: James Boe
 * Date: May 19, 2009
 * Time: 9:38:23 AM
 */
public class DerivedDataSourcePipeline extends Pipeline {
    protected List<IComponent> generatePipelineCommands(Set<AnalysisItem> allNeededAnalysisItems, Set<AnalysisItem> reportItems, Collection<FilterDefinition> filters, WSAnalysisDefinition report) {
        List<IComponent> components = new ArrayList<IComponent>();
        components.add(new DataScrubComponent());
        components.add(new FilterComponent(true));
        return components;
    }
}
