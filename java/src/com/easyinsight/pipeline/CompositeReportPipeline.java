package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.calculations.CalcGraph;
import com.easyinsight.core.Key;
import com.easyinsight.datafeeds.FeedService;
import com.easyinsight.etl.LookupTable;

import java.util.*;

/**
 * User: jamesboe
 * Date: Feb 9, 2010
 * Time: 11:01:57 AM
 */
public class CompositeReportPipeline extends Pipeline {
    @Override
    protected List<IComponent> generatePipelineCommands(Set<AnalysisItem> allNeededAnalysisItems, Set<AnalysisItem> reportItems, Collection<FilterDefinition> filters, WSAnalysisDefinition report, List<AnalysisItem> allItems) {
        List<IComponent> components = new ArrayList<IComponent>();
        components.add(new NormalizationComponent());
        components.add(new AggregationComponent());

        return components;
    }
}
