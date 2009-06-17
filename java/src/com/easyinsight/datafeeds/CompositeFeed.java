package com.easyinsight.datafeeds;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Key;
import com.easyinsight.core.DerivedKey;
import com.easyinsight.logging.LogClass;
import com.easyinsight.analysis.*;
import com.easyinsight.pipeline.DerivedDataSourcePipeline;

import java.util.*;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.graph.SimpleGraph;

/**
 * User: James Boe
 * Date: Jan 20, 2008
 * Time: 12:30:05 PM
 */
public class CompositeFeed extends Feed {

    private List<CompositeFeedNode> compositeFeedNodes;
    private List<CompositeFeedConnection> connections;

    public CompositeFeed() { }

    public CompositeFeed(List<CompositeFeedNode> compositeFeedNodes, List<CompositeFeedConnection> connections) {
        this.compositeFeedNodes = compositeFeedNodes;
        this.connections = connections;
    }

    public FeedType getDataFeedType() {
        return FeedType.COMPOSITE;
    }

    public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata) {
        DerivedKey derivedKey = (DerivedKey) analysisItem.getKey();
        for (CompositeFeedNode compositeFeedNode : getCompositeFeedNodes()) {
            if (compositeFeedNode.getDataFeedID() == derivedKey.getFeedID()) {
                return FeedRegistry.instance().getFeed(compositeFeedNode.getDataFeedID()).getMetadata(analysisItem, insightRequestMetadata);
            }
        }
        return null;
    }

    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, Collection<Key> additionalNeededKeys) {
        try {
            return getDataSet(analysisItems, insightRequestMetadata);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public DataSet getDetails(Collection<FilterDefinition> filters) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private DataSet getDataSet(Set<AnalysisItem> analysisItems, InsightRequestMetadata insightRequestMetadata) {

        Map<Long, QueryStateNode> queryNodeMap = new HashMap<Long, QueryStateNode>();

        UndirectedGraph<QueryStateNode, Edge> graph = new SimpleGraph<QueryStateNode, Edge>(Edge.class);

        // convert the nodes and edges into the graph with addVertex() and addEdge()

        for (CompositeFeedNode node : compositeFeedNodes) {
            QueryStateNode queryStateNode = new QueryStateNode(node.getDataFeedID());
            queryNodeMap.put(node.getDataFeedID(), queryStateNode);
            graph.addVertex(queryStateNode);
        }

        for (CompositeFeedConnection connection : connections) {
            QueryStateNode source = queryNodeMap.get(connection.getSourceFeedID());
            QueryStateNode target = queryNodeMap.get(connection.getTargetFeedID());
            Edge edge = new Edge(connection);
            graph.addEdge(source, target, edge);
        }

        // determine the path through the graph, create a new graph

        Map<Long, QueryStateNode> neededNodes = new HashMap<Long, QueryStateNode>();

        Iterator<QueryStateNode> graphIterator = new BreadthFirstIterator<QueryStateNode, Edge>(graph);
        while (graphIterator.hasNext()) {
            QueryStateNode queryStateNode = graphIterator.next();
            for (AnalysisItem analysisItem : analysisItems) {
                if (queryStateNode.handles(analysisItem.getKey())) {
                    neededNodes.put(queryStateNode.feedID, queryStateNode);
                    queryStateNode.addItem(analysisItem);
                }
            }
        }

        if (neededNodes.size() == 1) {
            QueryStateNode queryStateNode = neededNodes.values().iterator().next();
            queryStateNode.produceDataSet(insightRequestMetadata);
            return queryStateNode.myDataSet;
        }

        // determine which keys are matched to which fields as we proceed here

        Iterator<QueryStateNode> neededNodeIter = new HashMap<Long, QueryStateNode>(neededNodes).values().iterator();
        QueryStateNode firstNode = neededNodeIter.next();
        while (neededNodeIter.hasNext()) {
            QueryStateNode nextNode = neededNodeIter.next();
            List<Edge> neededEdges = DijkstraShortestPath.findPathBetween(graph, firstNode, nextNode);
            for (Edge edge : neededEdges) {
                QueryStateNode precedingNode = graph.getEdgeSource(edge);
                QueryStateNode followingNode = graph.getEdgeTarget(edge);
                neededNodes.put(precedingNode.feedID, precedingNode);
                neededNodes.put(followingNode.feedID, followingNode);
            }
        }


        UndirectedGraph<QueryStateNode, Edge> reducedGraph = new SimpleGraph<QueryStateNode, Edge>(Edge.class);

        // defining the joins...

        for (QueryStateNode queryStateNode : neededNodes.values()) {
            reducedGraph.addVertex(queryStateNode);
            Set<Edge> localEdges = graph.edgesOf(queryStateNode);
            for (Edge localEdge : localEdges) {
                QueryStateNode targetNode;
                boolean queryStateIsSource;
                if (queryStateNode == graph.getEdgeSource(localEdge)) {
                    targetNode = graph.getEdgeTarget(localEdge);
                    queryStateIsSource = true;
                } else {
                    targetNode = graph.getEdgeSource(localEdge);
                    queryStateIsSource = false;
                }
                QueryStateNode exists = neededNodes.get(targetNode.feedID);
                if (exists != null) {
                    if (queryStateIsSource) {
                        queryStateNode.addKey(localEdge.connection.getSourceJoin());
                        targetNode.addKey(localEdge.connection.getTargetJoin());
                    } else {
                        queryStateNode.addKey(localEdge.connection.getTargetJoin());
                        targetNode.addKey(localEdge.connection.getSourceJoin());
                    }
                }
            }
        }

        // actually connecting the data sets...

        DataSet dataSet = null;

        Set<Edge> edgeSet = new HashSet<Edge>();

        for (QueryStateNode queryStateNode : neededNodes.values()) {
            queryStateNode.produceDataSet(insightRequestMetadata);
            Set<Edge> allEdges = graph.edgesOf(queryStateNode);
            for (Edge edge : allEdges) {
                if (neededNodes.get(graph.getEdgeSource(edge).feedID) != null &&
                        neededNodes.get(graph.getEdgeTarget(edge).feedID) != null) {
                    edgeSet.add(edge);
                }
            }
        }

        for (Edge edge : edgeSet) {
            QueryStateNode sourceNode = neededNodes.get(edge.connection.getSourceFeedID());
            QueryStateNode targetNode = neededNodes.get(edge.connection.getTargetFeedID());
            dataSet = sourceNode.myDataSet.merge(targetNode.myDataSet, edge.connection.getSourceJoin(), edge.connection.getTargetJoin());
            sourceNode.myDataSet = dataSet;
            targetNode.myDataSet = dataSet;
        }

        return dataSet;
    }

    private static class QueryStateNode {
        private long feedID;
        private Set<Key> neededKeys = new HashSet<Key>();
        private Set<AnalysisItem> neededItems = new HashSet<AnalysisItem>();
        private List<AnalysisItem> allAnalysisItems = new ArrayList<AnalysisItem>();
        private Collection<FilterDefinition> filters = new ArrayList<FilterDefinition>();
        private Set<Key> additionalKeys = new HashSet<Key>();
        private DataSet myDataSet;

        private QueryStateNode(long feedID) {
            this.feedID = feedID;
        }

        public boolean handles(Key key) {
            DerivedKey derivedKey = (DerivedKey) key;
            return (feedID == derivedKey.getFeedID());
        }

        public void addItem(AnalysisItem analysisItem) {
            neededItems.add(analysisItem);
            neededKeys.add(analysisItem.getKey());
        }

        public void addKey(Key key) {
            if (!neededKeys.contains(key)) {
                additionalKeys.add(key);
            }
        }

        /*public void addKey(Key key) {
            neededKeys.add(key);
        }*/

        public void produceDataSet(InsightRequestMetadata insightRequestMetadata) {
            Feed feed = FeedRegistry.instance().getFeed(feedID);
            Map<Key, Key> columnSet = new LinkedHashMap<Key, Key>();
            for (Key key : neededKeys) {
                if (key instanceof DerivedKey) {
                    DerivedKey derivedKey = (DerivedKey) key;
                    columnSet.put(derivedKey.getParentKey(), derivedKey);
                } else {
                    columnSet.put(key, key);
                }
            }
            DataSet dataSet = feed.getAggregateDataSet(neededItems, filters, insightRequestMetadata, allAnalysisItems, false, additionalKeys);
            for (Map.Entry<Key, Key> entry : columnSet.entrySet()) {
                if (entry.getValue() != null) {
                    dataSet.replaceKey(entry.getKey(), entry.getValue());
                }
            }
            myDataSet = dataSet;
        }
    }

    private static class Edge {
        CompositeFeedConnection connection;

        private Edge(CompositeFeedConnection connection) {
            this.connection = connection;
        }
    }

    public List<CompositeFeedNode> getCompositeFeedNodes() {
        return compositeFeedNodes;
    }

    public List<CompositeFeedConnection> getConnections() {
        return connections;
    }
}
