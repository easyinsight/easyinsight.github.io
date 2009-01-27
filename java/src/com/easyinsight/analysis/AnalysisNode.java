package com.easyinsight.analysis;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultDirectedGraph;
import com.easyinsight.AnalysisItem;
import com.easyinsight.scrubbing.DataScrub;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * User: James Boe
 * Date: Oct 14, 2008
 * Time: 1:22:45 PM
 */
public class AnalysisNode {

    private static interface IOperation {
        List<IOperation> getParentItems();
    }    

    public static void main(String[] args) {
        List<AnalysisItem> allFeedItems = new ArrayList<AnalysisItem>();

        DirectedGraph<AnalysisItem, DefaultEdge> graph = new DefaultDirectedGraph<AnalysisItem, DefaultEdge>(DefaultEdge.class);
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();

        Set<AnalysisItem> allNeededAnalysisItems = new HashSet<AnalysisItem>(analysisItems);

        for (AnalysisItem analysisItem : analysisItems) {
            Set<AnalysisItem> neededAnalysisItems = getNeededItems(analysisItem, allFeedItems);
            allNeededAnalysisItems.addAll(neededAnalysisItems);
        }

        for (AnalysisItem analysisItem : allNeededAnalysisItems) {
            graph.addVertex(analysisItem);
        }

        Set<AnalysisItem> set = new HashSet<AnalysisItem>(allNeededAnalysisItems);

        List<List<AnalysisItem>> tiers = new ArrayList<List<AnalysisItem>>();

        List<AnalysisItem> tier = new ArrayList<AnalysisItem>();
        tiers.add(tier);
        for (AnalysisItem analysisItem : allNeededAnalysisItems) {
            if (analysisItem.isDerived()) {
                List<AnalysisItem> parentItems = analysisItem.getParentItems(allFeedItems);
                for (AnalysisItem parentItem : parentItems) {
                    graph.addEdge(parentItem, analysisItem);
                }
            } else {
                tier.add(analysisItem);
            }
        }

        while (!set.isEmpty()) {
            List<AnalysisItem> nextTier = new ArrayList<AnalysisItem>();
            tiers.add(nextTier);
            for (AnalysisItem analysisItem : tier) {
                Set<DefaultEdge> edgeSet = graph.edgesOf(analysisItem);
                for (DefaultEdge edge : edgeSet) {
                    AnalysisItem child = graph.getEdgeTarget(edge);
                    if (set.contains(child)) {
                        set.remove(child);
                        nextTier.add(child);
                    }
                }
            }
        }

        System.out.println(tiers);
    }

    private static Set<AnalysisItem> getNeededItems(AnalysisItem analysisItem, List<AnalysisItem> allAnalysisItems) {
        Set<AnalysisItem> analysisItems = new HashSet<AnalysisItem>();
        analysisItems.add(analysisItem);
        if (analysisItem.isDerived()) {
            List<AnalysisItem> parentItems = analysisItem.getParentItems(allAnalysisItems);
            for (AnalysisItem parentItem : parentItems) {
                analysisItems.addAll(getNeededItems(parentItem, allAnalysisItems));
            }
        }
        return analysisItems;
    }
}
