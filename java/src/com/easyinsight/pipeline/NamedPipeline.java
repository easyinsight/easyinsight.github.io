package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.calculations.CalcGraph;
import com.easyinsight.datafeeds.FeedService;
import com.easyinsight.etl.LookupTable;

import java.util.*;

/**
 * User: jamesboe
 * Date: 7/12/12
 * Time: 5:17 PM
 */
public class NamedPipeline extends Pipeline {
    private String name;

    public NamedPipeline(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    protected List<IComponent> generatePipelineCommands(Set<AnalysisItem> allNeededAnalysisItems, Set<AnalysisItem> reportItems, Collection<FilterDefinition> filters, WSAnalysisDefinition report, List<AnalysisItem> allItems, InsightRequestMetadata insightRequestMetadata) {
        List<IComponent> components = new ArrayList<IComponent>();
        components.addAll(new CalcGraph().doFunGraphStuff(allNeededAnalysisItems, allItems, reportItems, name, getStructure()));
        // after this...
        // drop everything not relevant, that's what we're missing atm
        components.add(new ReworkedCleanupComponent(name));
        components.add(new AggregationComponent(AggregationComponent.OTHER));
        return components;
    }
}
