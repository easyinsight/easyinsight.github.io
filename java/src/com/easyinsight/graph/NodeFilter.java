package com.easyinsight.graph;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * User: James Boe
 * Date: Jun 16, 2008
 * Time: 12:02:11 PM
 */
public abstract class NodeFilter<E> {
    public abstract boolean accept(E nodeData);

    public List<GraphNode> getNeededGraphNodes() {
        return null;
    }

    public void postAccept() {
        List<GraphNode> graphNodes = getNeededGraphNodes();
        Set<GraphNode> fullSet = new HashSet<GraphNode>();
        for (GraphNode graphNode : graphNodes) {
            List<GraphNode> path = findShortestPath(fullSet, graphNode);
            fullSet.addAll(path);
        }
        // add first node of graphNodes to the set
        // find the shortest path from the contents of the set to the next node (N2) in graphNodes
        // add all contents of the shortest path to the set
        // find the shortest path from the contents of the set to the next node (N3) in graphnodes
        // add all contents of the shortest path to the set

        // okay, so after that algorithm is done...
        // we still have to actually perform the real thing
        // requesting the data sets is easy enough...
        // 
    }

    private List<GraphNode> findShortestPath(Set<GraphNode> fullSet, GraphNode targetNode) {
        for (GraphNode sourceNode : fullSet) {
            
        }
        return null;  //To change body of created methods use File | Settings | File Templates.
    }
}
