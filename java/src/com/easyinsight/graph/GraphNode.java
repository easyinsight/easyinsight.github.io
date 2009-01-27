package com.easyinsight.graph;

import java.util.List;

/**
 * User: James Boe
 * Date: Jun 16, 2008
 * Time: 11:43:10 AM
 */
public class GraphNode<E> {
    private E nodeData;
    private List<GraphConnection> connections;

    public void filter(NodeFilter<E> nodeFilter) {
        boolean needed = nodeFilter.accept(nodeData);
        if (needed) {
            // add the node to our list of needed nodes
        }
    }



    // post filter, find the needed joins between those nodes

    // that should become a new graph

    // and then from that, we can apply a visitor to retrieve the data
    // okay, so...we not only want to filter, we want to indicate which fields are needed in the node
    // then we can produce the data set from each node

}
