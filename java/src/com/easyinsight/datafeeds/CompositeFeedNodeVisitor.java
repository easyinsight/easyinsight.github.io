package com.easyinsight.datafeeds;


import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.graph.SimpleGraph;

/**
 * User: James Boe
* Date: Jul 3, 2008
* Time: 5:22:14 PM
*/
public abstract class CompositeFeedNodeVisitor {
    public void visit(CompositeFeedDefinition compositeFeed) throws SQLException {
        UndirectedGraph<CompositeFeedNode, CompositeFeedConnection> graph = new SimpleGraph<CompositeFeedNode,
                CompositeFeedConnection>(CompositeFeedConnection.class);
        Map<Long, CompositeFeedNode> nodes = new HashMap<Long, CompositeFeedNode>();
        for (CompositeFeedNode node : compositeFeed.getCompositeFeedNodes()) {
            nodes.put(node.getDataFeedID(), node);
            graph.addVertex(node);
        }
        for (CompositeFeedConnection connection : compositeFeed.getConnections()) {
            graph.addEdge(nodes.get(connection.getSourceFeedID()), nodes.get(connection.getTargetFeedID()), connection);
        }
        BreadthFirstIterator<CompositeFeedNode, CompositeFeedConnection> iter = new BreadthFirstIterator<CompositeFeedNode,
                CompositeFeedConnection>(graph);
        while (iter.hasNext()) {
            CompositeFeedNode compositeFeedNode = iter.next();
            accept(compositeFeedNode);
        }
    }

    protected abstract void accept(CompositeFeedNode compositeFeedNode) throws SQLException;
}
