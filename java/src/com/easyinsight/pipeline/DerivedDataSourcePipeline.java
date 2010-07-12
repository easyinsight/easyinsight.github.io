package com.easyinsight.pipeline;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.core.Key;

import java.util.*;

/**
 * User: James Boe
 * Date: May 19, 2009
 * Time: 9:38:23 AM
 */
public class DerivedDataSourcePipeline extends Pipeline {
    protected List<IComponent> generatePipelineCommands(Set<AnalysisItem> allNeededAnalysisItems, Set<AnalysisItem> reportItems, Collection<FilterDefinition> filters, WSAnalysisDefinition report, Map<Key, Integer> refMap) {
        List<IComponent> components = new ArrayList<IComponent>();
        components.add(new DataScrubComponent());
        components.add(new FilterComponent(true));
        return components;
    }
}
