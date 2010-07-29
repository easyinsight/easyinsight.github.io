package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisCalculation;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.DerivedAnalysisDimension;
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
            } else if (!rowLevel && item.hasType(AnalysisItemTypes.DERIVED_DIMENSION)) {
                derivedItems.add(item);
            }
        }
        List<IComponent> components = new ArrayList<IComponent>();
        if (derivedItems.size() > 0) {
            DirectedGraph<CalcNode, DefaultEdge> graph = new DefaultDirectedGraph<CalcNode, DefaultEdge>(DefaultEdge.class);
            Map<Key, CalcNode> nodeMap = new HashMap<Key, CalcNode>();
            for (AnalysisItem item : derivedItems) {
                CalcNode calcNode = new CalcNode(item);
                graph.addVertex(calcNode);
                nodeMap.put(item.createAggregateKey(), calcNode);
            }
            for (CalcNode calcNode : nodeMap.values()) {
                List<AnalysisItem> requiredItems = calcNode.analysisItem.getAnalysisItems(allItems, reportItems, false, true, false);
                for (AnalysisItem item : requiredItems) {
                    CalcNode requiredNode = nodeMap.get(item.createAggregateKey());
                    if (requiredNode != null && requiredNode.analysisItem != calcNode.analysisItem) {
                        graph.addEdge(requiredNode, calcNode);
                    }
                }
            }

            if (new CycleDetector<CalcNode, DefaultEdge>(graph).detectCycles()) {
                throw new RuntimeException("Cycle detected in calculated fields.");
            }

            TopologicalOrderIterator<CalcNode, DefaultEdge> iterator = new TopologicalOrderIterator<CalcNode, DefaultEdge>(graph);
            while (iterator.hasNext()) {
                CalcNode calcNode = iterator.next();
                if (calcNode.analysisItem.hasType(AnalysisItemTypes.CALCULATION)) {
                    components.add(new CalculationComponent((AnalysisCalculation) calcNode.analysisItem));
                    components.add(new CalculationCleanupComponent((AnalysisCalculation) calcNode.analysisItem));
                } else if (calcNode.analysisItem.hasType(AnalysisItemTypes.DERIVED_DIMENSION)) {
                    components.add(new DerivedGroupingComponent((DerivedAnalysisDimension) calcNode.analysisItem));
                    components.add(new DerivedDimensionCleanupComponent((DerivedAnalysisDimension) calcNode.analysisItem));
                }
            }
        }
        return components;
    }

    private class CalcNode {
        private AnalysisItem analysisItem;

        private CalcNode(AnalysisItem analysisItem) {
            this.analysisItem = analysisItem;
        }
    }
}
