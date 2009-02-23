package com.easyinsight.datafeeds;

import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.alg.DijkstraShortestPath;

import java.util.*;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Key;
import com.easyinsight.core.DerivedKey;
import com.easyinsight.core.NamedKey;
import com.easyinsight.analysis.IRow;

/**
 * User: James Boe
 * Date: Sep 27, 2008
 * Time: 9:55:26 AM
 */
public class GraphScratch {

    private static class TestCompositeFeedNode extends CompositeFeedNode {
        private List<Key> keys;

        private TestCompositeFeedNode(long dataFeedID, List<Key> keys) {
            super(dataFeedID);
            this.keys = keys;
        }

        public List<Key> getKeys() {
            return keys;
        }
    }

    public List<CompositeFeedConnection> initialDefine(List<CompositeFeedNode> nodes, List<Long> newFeeds) {
        List<CompositeFeedConnection> allNewEdges = new ArrayList<CompositeFeedConnection>();
        JoinDiscovery joinDiscovery = new JoinDiscovery();
        for (Long newFeedID : newFeeds) {
            for (CompositeFeedNode node : nodes) {
                List<CompositeFeedConnection> potentialJoins = joinDiscovery.findPotentialJoins(node.getDataFeedID(), newFeedID);
                allNewEdges.addAll(potentialJoins);
            }
        }
        return allNewEdges;
    }

    public static void main(String[] args) {

        Key customerDimID = new DerivedKey(new NamedKey("Customer"), 1);
        Key salesFK = new DerivedKey(new NamedKey("Sales Manager"), 1);
        CompositeFeedNode customerDim = new TestCompositeFeedNode(1, Arrays.asList(customerDimID, salesFK));
        Key productDimID = new DerivedKey(new NamedKey("Product"), 2);
        CompositeFeedNode productDim = new TestCompositeFeedNode(2, Arrays.asList(productDimID));        
        Key customerFK = new DerivedKey(new NamedKey("Customer"), 3);
        Key productFK = new DerivedKey(new NamedKey("Product"), 3);
        Key orderFK = new DerivedKey(new NamedKey("OrderID"), 3);
        CompositeFeedNode order1 = new TestCompositeFeedNode(3, Arrays.asList(customerFK, productFK, orderFK));
        Key customer2FK = new DerivedKey(new NamedKey("Customer"), 4);
        Key product2FK = new DerivedKey(new NamedKey("Product"), 4);
        Key order2FK = new DerivedKey(new NamedKey("OrderID"), 4);
        CompositeFeedNode order2 = new TestCompositeFeedNode(4, Arrays.asList(customer2FK, product2FK, order2FK));
        Key salesID = new DerivedKey(new NamedKey("Sales Manager"), 5);
        Key managerName = new DerivedKey(new NamedKey("Manager Name"), 5);
        CompositeFeedNode salesNode = new TestCompositeFeedNode(5, Arrays.asList(salesID, managerName));
        CompositeFeedConnection order1ToCustomer = new CompositeFeedConnection(order1.getDataFeedID(), customerDim.getDataFeedID(), customerFK, customerDimID);
        CompositeFeedConnection order2ToCustomer = new CompositeFeedConnection(order2.getDataFeedID(), customerDim.getDataFeedID(), customer2FK, customerDimID);
        CompositeFeedConnection order1ToProduct = new CompositeFeedConnection(order1.getDataFeedID(), productDim.getDataFeedID(), productFK, productDimID);
        CompositeFeedConnection order2ToProduct = new CompositeFeedConnection(order2.getDataFeedID(), productDim.getDataFeedID(), product2FK, productDimID);
        CompositeFeedConnection order1ToOrder2 = new CompositeFeedConnection(order1.getDataFeedID(), order2.getDataFeedID(), orderFK, order2FK);
        CompositeFeedConnection customerToSales = new CompositeFeedConnection(customerDim.getDataFeedID(), salesNode.getDataFeedID(), salesFK, salesID);

        List<CompositeFeedNode> nodes = Arrays.asList(customerDim, productDim, order1, order2, salesNode);
        List<CompositeFeedConnection> edges = Arrays.asList(order1ToCustomer, order2ToCustomer, order1ToProduct, order2ToProduct, order1ToOrder2, customerToSales);
        Map<Long, QueryStateNode> queryNodeMap = new HashMap<Long, QueryStateNode>();
        List<Key> keys = Arrays.asList(managerName, orderFK);
        
        UndirectedGraph<QueryStateNode, Edge> graph = new SimpleGraph<QueryStateNode, Edge>(Edge.class);

        // convert the nodes and edges into the graph with addVertex() and addEdge()

        for (CompositeFeedNode node : nodes) {
            QueryStateNode queryStateNode = new QueryStateNode(node.getDataFeedID(), getPossibleKeys(node));
            queryNodeMap.put(node.getDataFeedID(), queryStateNode);
            graph.addVertex(queryStateNode);
        }

        for (CompositeFeedConnection connection : edges) {
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
            for (Key key : keys) {
                if (queryStateNode.handles(key)) {
                    neededNodes.put(queryStateNode.feedID, queryStateNode);
                    queryStateNode.addKey(key);
                }
            }
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
            queryStateNode.produceDataSet();
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

        System.out.println(dataSet);
    }

    private static Set<Key> getPossibleKeys(CompositeFeedNode compositeFeedNode) {
        TestCompositeFeedNode testNode = (TestCompositeFeedNode) compositeFeedNode;
        return new HashSet<Key>(testNode.getKeys());
    }

    private static class QueryStateNode {
        private long feedID;
        private Set<Key> possibleKeys;
        private Set<Key> neededKeys = new HashSet<Key>();
        private DataSet myDataSet;

        private QueryStateNode(long feedID, Set<Key> possibleKeys) {
            this.feedID = feedID;
            this.possibleKeys = possibleKeys;
        }

        public boolean handles(Key key) {
            return possibleKeys.contains(key);
        }

        public void addKey(Key key) {
            neededKeys.add(key);
        }

        public void produceDataSet() {
            DataSet dataSet = new DataSet();
            IRow row = dataSet.createRow();
            for (Key key : neededKeys) {
                if ("Customer".equals(key.toKeyString())) {
                    row.addValue(key, "1");
                } else if ("Sales Manager".equals(key.toKeyString())) {
                    row.addValue(key, "5");
                } else if ("OrderID".equals(key.toKeyString())) {
                    row.addValue(key, "XYZ");
                } else if ("Manager Name".equals(key.toKeyString())) {
                    row.addValue(key, "Bob");
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
}
