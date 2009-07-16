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
public abstract class CompositeFeedNodeShallowVisitor {
    public void visit(CompositeFeedDefinition compositeFeed) throws SQLException {
        for (CompositeFeedNode compositeFeedNode : compositeFeed.getCompositeFeedNodes()) {
            accept(compositeFeedNode);
        }
    }

    protected abstract void accept(CompositeFeedNode compositeFeedNode) throws SQLException;
}