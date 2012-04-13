package com.easyinsight.storage;

import com.easyinsight.analysis.*;
import com.easyinsight.calculations.CalcGraph;
import com.easyinsight.pipeline.IComponent;
import com.easyinsight.pipeline.Pipeline;
import com.easyinsight.pipeline.TypeTransformComponent;

import java.util.*;

/**
 * User: jamesboe
 * Date: 4/3/12
 * Time: 11:41 AM
 */
public class CacheCalculationPipeline extends Pipeline {
    
    private List<AnalysisCalculation> calculations;

    public CacheCalculationPipeline(List<AnalysisCalculation> calculations) {
        this.calculations = calculations;
        getStructure().setOnStorage(true);
    }

    @Override
    protected List<IComponent> generatePipelineCommands(Set<AnalysisItem> allNeededAnalysisItems, Set<AnalysisItem> reportItems, Collection<FilterDefinition> filters, WSAnalysisDefinition report, List<AnalysisItem> allItems, InsightRequestMetadata insightRequestMetadata) {
        List<IComponent> components = new ArrayList<IComponent>();
        //components.add(new TypeTransformComponent());
        components.addAll(new CalcGraph().doFunGraphStuff(new HashSet<AnalysisItem>(calculations), allItems, new HashSet<AnalysisItem>(calculations), true, getStructure()));
        return components;
    }
}
