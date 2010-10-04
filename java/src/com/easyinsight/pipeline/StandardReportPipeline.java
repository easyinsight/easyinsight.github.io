package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSHeatMap;
import com.easyinsight.calculations.CalcGraph;
import com.easyinsight.core.Key;
import com.easyinsight.datafeeds.FeedService;
import com.easyinsight.etl.LookupTable;

import java.util.*;

/**
 * User: James Boe
 * Date: May 19, 2009
 * Time: 9:36:30 AM
 */
public class StandardReportPipeline extends Pipeline {

    protected List<IComponent> generatePipelineCommands(Set<AnalysisItem> allNeededAnalysisItems, Set<AnalysisItem> reportItems, Collection<FilterDefinition> filters, WSAnalysisDefinition report, Map<Key, Integer> refMap, List<AnalysisItem> allItems) {

        // current problematic scenarios
        // a measure filter on a calculation
        // a calculation on a calculation
        // a tag filter?
        //

        // there's still a fundamental challenge here...

        // ordering of the pipeline operations

        List<IComponent> components = new ArrayList<IComponent>();

        if (report instanceof WSHeatMap) {
            WSHeatMap heatMap = (WSHeatMap) report;
            if (heatMap.getZipCode() != null) {
                components.add(new CoordinateComponent(heatMap.getZipCode()));
            }
            if (heatMap.getLongitudeItem() != null) {
                components.add(new CoordinatePrecisionComponent(heatMap.getLongitudeItem()));
            }
            if (heatMap.getLatitudeItem() != null) {
                components.add(new CoordinatePrecisionComponent(heatMap.getLatitudeItem()));
            }
        }

        for (AnalysisItem analysisItem : allNeededAnalysisItems) {
            if (analysisItem.getLookupTableID() != null && analysisItem.getLookupTableID() > 0) {
                LookupTable lookupTable = new FeedService().getLookupTable(analysisItem.getLookupTableID());
                components.add(new LookupTableComponent(lookupTable, refMap.get(lookupTable.getTargetField().createAggregateKey())));
            }
        }

        components.add(new DataScrubComponent());
        for (AnalysisItem tag : items(AnalysisItemTypes.LISTING, allNeededAnalysisItems)) {
            AnalysisList analysisList = (AnalysisList) tag;
            if (analysisList.isMultipleTransform()) components.add(new TagTransformComponent(analysisList));
        }

        components.addAll(new CalcGraph().doFunGraphStuff(allNeededAnalysisItems, allItems, reportItems, true));

        for (AnalysisItem range : items(AnalysisItemTypes.RANGE_DIMENSION, allNeededAnalysisItems)) {
            components.add(new RangeComponent((AnalysisRangeDimension) range));
        }
        components.add(new TypeTransformComponent());

        if (report.getFilterDefinitions() != null) {
            for (FilterDefinition filterDefinition : report.retrieveFilterDefinitions()) {
                components.addAll(filterDefinition.createComponents(true, new DefaultFilterProcessor(), null));
            }
        }

        for (AnalysisItem analysisItem : allNeededAnalysisItems) {
            if (analysisItem.getFilters() != null) {
                for (FilterDefinition filterDefinition : analysisItem.getFilters()) {
                    components.addAll(filterDefinition.createComponents(true, new FieldFilterProcessor(analysisItem), analysisItem));
                }
            }
        }

        /*if (report.getFilterDefinitions() != null) {
            for (FilterDefinition filterDefinition : report.getFilterDefinitions()) {
                components.addAll(filterDefinition.createComponents(true, filterProcessor));
            }
        }*/

        for (AnalysisItem step : items(AnalysisItemTypes.STEP, allNeededAnalysisItems)) {
            components.add(new StepCorrelationComponent((AnalysisStep) step));
            components.add(new StepTransformComponent((AnalysisStep) step));
        }
        
        components.add(new AggregationComponent());

        components.addAll(new CalcGraph().doFunGraphStuff(allNeededAnalysisItems, allItems, reportItems, false));

        components.add(new AggregationComponent(AggregationTypes.RANK));

        components.add(new LinkDecorationComponent());
        if (report.getFilterDefinitions() != null) {
            for (FilterDefinition filterDefinition : report.retrieveFilterDefinitions()) {
                components.addAll(filterDefinition.createComponents(false, new DefaultFilterProcessor(), null));
            }
        }
        components.add(new LimitsComponent());
        components.addAll(report.createComponents());        
        components.add(new SortComponent());
        return components;
    }
}
