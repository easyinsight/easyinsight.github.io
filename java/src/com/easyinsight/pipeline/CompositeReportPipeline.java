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
        /*for (AnalysisItem analysisItem : allNeededAnalysisItems) {
            if (analysisItem.getLookupTableID() != null && analysisItem.getLookupTableID() > 0) {
                LookupTable lookupTable = new FeedService().getLookupTable(analysisItem.getLookupTableID());
                if (lookupTable.getSourceField().hasType(AnalysisItemTypes.LISTING)) {
                    AnalysisList analysisList = (AnalysisList) lookupTable.getSourceField();
                    if (analysisList.isMultipleTransform()) components.add(new TagTransformComponent(analysisList));
                } else if (lookupTable.getSourceField().hasType(AnalysisItemTypes.DERIVED_DIMENSION)) {
                    Set<AnalysisItem> analysisItems = new HashSet<AnalysisItem>();
                    analysisItems.add(lookupTable.getSourceField());
                    components.addAll(new CalcGraph().doFunGraphStuff(analysisItems, allItems, reportItems, true));
                }
                components.add(new LookupTableComponent(lookupTable));
            }
        }

        for (AnalysisItem tag : items(AnalysisItemTypes.LISTING, allNeededAnalysisItems)) {
            AnalysisList analysisList = (AnalysisList) tag;
            if (analysisList.isMultipleTransform()) components.add(new TagTransformComponent(analysisList));
        }
        for (AnalysisItem range : items(AnalysisItemTypes.RANGE_DIMENSION, allNeededAnalysisItems)) {
            components.add(new RangeComponent((AnalysisRangeDimension) range));
        }
        components.addAll(new CalcGraph().doFunGraphStuff(allNeededAnalysisItems, allItems, reportItems, true));*/
        components.add(new NormalizationComponent());
        components.add(new AggregationComponent());

        //components.add(new TypeTransformComponent());

        return components;
    }
}
