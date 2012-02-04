package com.easyinsight.calculations;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.pipeline.*;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;

import java.util.*;

/**
 * User: jamesboe
 * Date: Jul 28, 2010
 * Time: 3:06:38 PM
 */
public class CalcGraph {

    public List<IComponent> doFunGraphStuff(Set<AnalysisItem> allNeededAnalysisItems, List<AnalysisItem> allItems, Set<AnalysisItem> reportItems,
                                            boolean rowLevel) {
        List<AnalysisItem> derivedItems = new ArrayList<AnalysisItem>();
        for (AnalysisItem item : allNeededAnalysisItems) {
            if (item.hasType(AnalysisItemTypes.CALCULATION)) {
                AnalysisCalculation calc = (AnalysisCalculation) item;
                if (calc.isApplyBeforeAggregation() == rowLevel) derivedItems.add(item);
            } else if (rowLevel && item.hasType(AnalysisItemTypes.DERIVED_DIMENSION)) {
                derivedItems.add(item);
            } else if (rowLevel && item.hasType(AnalysisItemTypes.DERIVED_DATE)) {
                derivedItems.add(item);
            } else if (!rowLevel && item.hasType(AnalysisItemTypes.REAGGREGATE_MEASURE)) {
                derivedItems.add(item);
            }
        }
        List<IComponent> components = new ArrayList<IComponent>();
        if (derivedItems.size() > 0) {
            DirectedGraph<AnalysisItem, DefaultEdge> graph = new DefaultDirectedGraph<AnalysisItem, DefaultEdge>(DefaultEdge.class);
            Map<Key, AnalysisItem> nodeMap = new HashMap<Key, AnalysisItem>();
            for (AnalysisItem item : derivedItems) {
                graph.addVertex(item);
                nodeMap.put(item.createAggregateKey(), item);
            }
            for (AnalysisItem analysisItem : nodeMap.values()) {
                List<AnalysisItem> requiredItems = analysisItem.getAnalysisItems(allItems, reportItems, false, true, CleanupComponent.AGGREGATE_CALCULATIONS, new HashSet<AnalysisItem>());
                for (AnalysisItem item : requiredItems) {
                    AnalysisItem requiredNode = nodeMap.get(item.createAggregateKey());
                    if (requiredNode != null && requiredNode != analysisItem) {
                        graph.addEdge(requiredNode, analysisItem);
                    }
                }
            }

            if (new CycleDetector<AnalysisItem, DefaultEdge>(graph).detectCycles()) {
                throw new RuntimeException("Cycle detected in calculated fields.");
            }

            TopologicalOrderIterator<AnalysisItem, DefaultEdge> iterator = new TopologicalOrderIterator<AnalysisItem, DefaultEdge>(graph);
            List<AnalysisItem> items = new ArrayList<AnalysisItem>();
            while (iterator.hasNext()) {
                items.add(iterator.next());
            }
            List<AnalysisItem> reaggregateItems = new ArrayList<AnalysisItem>();
            List<AnalysisItem> allOtherItems = new ArrayList<AnalysisItem>();
            for (AnalysisItem item : items) {
                if (item.hasType(AnalysisItemTypes.CALCULATION)) {
                    AnalysisCalculation analysisCalculation = (AnalysisCalculation) item;
                    if (analysisCalculation.getCalculationString().contains("aggregatefield")) {
                        reaggregateItems.add(analysisCalculation);
                    } else {
                        allOtherItems.add(analysisCalculation);
                    }
                } else {
                    allOtherItems.add(item);
                }
            }
            if (!reaggregateItems.isEmpty()) {
                for (AnalysisItem calcNode : reaggregateItems) {
                    if (calcNode.hasType(AnalysisItemTypes.CALCULATION)) {
                       components.add(new CalculationComponent((AnalysisCalculation) calcNode));
                    } else if (calcNode.hasType(AnalysisItemTypes.DERIVED_DIMENSION)) {
                        components.add(new DerivedGroupingComponent((DerivedAnalysisDimension) calcNode));
                    } else if (calcNode.hasType(AnalysisItemTypes.DERIVED_DATE)) {
                        components.add(new DerivedDateComponent((DerivedAnalysisDateDimension) calcNode));
                    } else if (calcNode.hasType(AnalysisItemTypes.REAGGREGATE_MEASURE)) {
                        components.add(new ReaggregateComponent((ReaggregateAnalysisMeasure) calcNode));
                    }
                }
                components.add(new AdjustPipelineComponent());
                components.add(new AggregationComponent());
            }
            for (AnalysisItem calcNode : allOtherItems) {
                if (calcNode.hasType(AnalysisItemTypes.CALCULATION)) {
                    components.add(new CalculationComponent((AnalysisCalculation) calcNode));
                } else if (calcNode.hasType(AnalysisItemTypes.DERIVED_DIMENSION)) {
                    components.add(new DerivedGroupingComponent((DerivedAnalysisDimension) calcNode));
                } else if (calcNode.hasType(AnalysisItemTypes.DERIVED_DATE)) {
                    components.add(new DerivedDateComponent((DerivedAnalysisDateDimension) calcNode));
                } else if (calcNode.hasType(AnalysisItemTypes.REAGGREGATE_MEASURE)) {
                    components.add(new ReaggregateComponent((ReaggregateAnalysisMeasure) calcNode));
                }
            }
            FieldFilterComponent fieldFilterComponent = new FieldFilterComponent();
            components.add(fieldFilterComponent);
            for (AnalysisItem calcNode : allOtherItems) {
                if (calcNode.getFilters() != null && calcNode.getFilters().size() > 0) {
                    for (FilterDefinition filterDefinition : calcNode.getFilters()) {
                        if (filterDefinition.getField() != null) {
                            fieldFilterComponent.addFilterPair(calcNode, filterDefinition);
                        } else {
                            components.addAll(filterDefinition.createComponents(false, new FieldFilterProcessor(calcNode), calcNode, true));
                        }
                    }
                }
            }
        }
        return components;
    }
}
