package com.easyinsight.datafeeds;


import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Key;
import com.easyinsight.core.DerivedKey;

import com.easyinsight.analysis.*;

import java.util.*;

import com.easyinsight.pipeline.AltCompositeReportPipeline;
import com.easyinsight.pipeline.CleanupComponent;
import com.easyinsight.pipeline.CompositeReportPipeline;
import com.easyinsight.pipeline.Pipeline;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.ClosestFirstIterator;

/**
 * User: James Boe
 * Date: Jan 20, 2008
 * Time: 12:30:05 PM
 */
public class CompositeFeed extends Feed {

    private List<CompositeFeedNode> compositeFeedNodes;
    private List<CompositeFeedConnection> connections;

    public CompositeFeed() {
    }

    public CompositeFeed(List<CompositeFeedNode> compositeFeedNodes, List<CompositeFeedConnection> connections) {
        this.compositeFeedNodes = compositeFeedNodes;
        this.connections = connections;
    }

    public List<Long> getDataSourceIDs() {
        List<Long> ids = super.getDataSourceIDs();
        for (CompositeFeedNode node : compositeFeedNodes) {
            ids.add(node.getDataFeedID());
        }
        return ids;
    }

    @Override
    public List<FilterDefinition> getIntrinsicFilters(EIConnection conn) {
        List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
        for (CompositeFeedNode child : compositeFeedNodes) {
            Feed childDataSource = FeedRegistry.instance().getFeed(child.getDataFeedID(), conn);
            //filters.addAll(childDataSource.getIntrinsicFilters());
            List<FilterDefinition> childFilters = childDataSource.getIntrinsicFilters(conn);
            for (FilterDefinition filterDefinition : childFilters) {
                for (AnalysisItem item : getFields()) {
                    Key key = item.getKey();
                    if (key.toKeyString().equals(filterDefinition.getField().getKey().toKeyString())) {
                        filterDefinition.getField().setKey(key);
                        filters.add(filterDefinition);
                    }
                }
            }
        }
        return filters;
    }

    private AnalysisItemResultMetadata findMetadataForComposite(DerivedKey derivedKey, AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata, EIConnection conn) throws ReportException {
        AnalysisItemResultMetadata metadata = null;
        for (CompositeFeedNode compositeFeedNode : getCompositeFeedNodes()) {
            if (compositeFeedNode.getDataFeedID() == derivedKey.getFeedID()) {
                metadata = FeedRegistry.instance().getFeed(compositeFeedNode.getDataFeedID(), conn).getMetadata(analysisItem, insightRequestMetadata, conn);
            }
        }
        if (metadata == null) {
            Key parentKey = derivedKey.getParentKey();
            if (parentKey instanceof DerivedKey) {
                DerivedKey parentDerivedKey = (DerivedKey) parentKey;
                metadata = findMetadataForComposite(parentDerivedKey, analysisItem, insightRequestMetadata, conn);
            }
        }
        return metadata;
    }

    /*public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata, EIConnection conn) throws ReportException {
        if (analysisItem.getKey() instanceof DerivedKey) {
            DerivedKey derivedKey = (DerivedKey) analysisItem.getKey();
            return findMetadataForComposite(derivedKey, analysisItem, insightRequestMetadata, conn);
        } else {
            if (analysisItem.getLookupTableID() != null && analysisItem.getLookupTableID() > 0) {
                AnalysisItemResultMetadata analysisItemResultMetadata = analysisItem.createResultMetadata();
                Map<Value, Value> lookupMap = new HashMap<Value, Value>();
                LookupTable lookupTable = new FeedService().getLookupTable(analysisItem.getLookupTableID());
                AnalysisDimensionResultMetadata sourceMetadata = (AnalysisDimensionResultMetadata) getMetadata(lookupTable.getSourceField(), insightRequestMetadata, conn);
                for (LookupPair lookupPair : lookupTable.getLookupPairs()) {
                    lookupMap.put(lookupPair.getSourceValue(), lookupPair.getTargetValue());
                }
                for (Value value : sourceMetadata.getValues()) {
                    Value targetValue = lookupMap.get(value);
                    if (targetValue == null) {
                        targetValue = new EmptyValue();
                    }
                    analysisItemResultMetadata.addValue(analysisItem, targetValue, insightRequestMetadata);
                }
                return analysisItemResultMetadata;
            } else {
                AnalysisItemResultMetadata analysisItemResultMetadata = analysisItem.createResultMetadata();

            }
        }
        return null;
    }*/

    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
        try {
            return getDataSet(analysisItems, filters, insightRequestMetadata, conn);
        } catch (ReportException re) {
            throw re;
        } catch (InvalidFieldsException ife) {
            throw ife;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private DataSet getDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, EIConnection conn) throws ReportException {

        if (analysisItems.size() == 0) {
            return new DataSet();
        }

        List<CompositeFeedNode> compositeFeedNodes = this.compositeFeedNodes;
        List<CompositeFeedConnection> connections;

        if (insightRequestMetadata.getJoinOverrides() != null && insightRequestMetadata.getJoinOverrides().size() > 0) {
            connections = new ArrayList<CompositeFeedConnection>();
            for (JoinOverride joinOverride : insightRequestMetadata.getJoinOverrides()) {
                connections.add(new CompositeFeedConnection(((DerivedKey) joinOverride.getSourceItem().getKey()).getFeedID(),
                        ((DerivedKey) joinOverride.getTargetItem().getKey()).getFeedID(), joinOverride.getSourceItem(),
                        joinOverride.getTargetItem(), joinOverride.getSourceName(), joinOverride.getTargetName()));
            }
            insightRequestMetadata.setJoinOverrides(null);
        } else {
            connections = this.connections;
        }

        Map<Long, QueryStateNode> queryNodeMap = new HashMap<Long, QueryStateNode>();

        UndirectedGraph<QueryStateNode, Edge> graph = new SimpleGraph<QueryStateNode, Edge>(Edge.class);

        // convert the nodes and edges into the graph with addVertex() and addEdge()
        Map<Long, QueryStateNode> neededNodes = new HashMap<Long, QueryStateNode>();

        Set<AnalysisItem> itemSet = new HashSet<AnalysisItem>(analysisItems);
        Set<AnalysisItem> alwaysSet = new HashSet<AnalysisItem>();
        for (CompositeFeedNode node : compositeFeedNodes) {
            QueryStateNode queryStateNode = new QueryStateNode(node.getDataFeedID(), conn);
            queryNodeMap.put(node.getDataFeedID(), queryStateNode);
            graph.addVertex(queryStateNode);
            for (AnalysisItem analysisItem : analysisItems) {
                if (queryStateNode.handles(analysisItem.getKey())) {
                    itemSet.remove(analysisItem);
                    neededNodes.put(queryStateNode.feedID, queryStateNode);
                    queryStateNode.addItem(analysisItem);
                } else if (alwaysPassThrough(analysisItem)) {
                    alwaysSet.add(analysisItem);
                    queryStateNode.addItem(analysisItem);
                }
            }
            if (filters != null) {
                for (FilterDefinition filterDefinition : filters) {
                    if (filterDefinition.isSingleSource()) {
                        if (queryStateNode.handles(filterDefinition.getField().getKey())) {
                            queryStateNode.addFilter(filterDefinition);
                        } else if (alwaysPassThrough(filterDefinition.getField())) {
                            queryStateNode.addFilter(filterDefinition);
                        }
                    }
                }
            }
        }

        itemSet.removeAll(alwaysSet);

        if (itemSet.size() > 0) {
            throw new InvalidFieldsException(itemSet);
        }

        for (CompositeFeedConnection connection : connections) {
            QueryStateNode source = queryNodeMap.get(connection.getSourceFeedID());
            QueryStateNode target = queryNodeMap.get(connection.getTargetFeedID());
            Edge edge = new Edge(connection);
            graph.addEdge(source, target, edge);
        }

        if (neededNodes.size() == 1) {
            QueryStateNode queryStateNode = neededNodes.values().iterator().next();
            queryStateNode.produceDataSet(insightRequestMetadata);
            return queryStateNode.queryData.dataSet;
        }

        if (neededNodes.size() == 0) {
            return new DataSet();
        }

        // determine which keys are matched to which fields as we proceed here

        Iterator<QueryStateNode> neededNodeIter = new HashMap<Long, QueryStateNode>(neededNodes).values().iterator();
        QueryStateNode firstNode = neededNodeIter.next();
        while (neededNodeIter.hasNext()) {
            QueryStateNode nextNode = neededNodeIter.next();
            List<Edge> neededEdges = DijkstraShortestPath.findPathBetween(graph, firstNode, nextNode);
            if (neededEdges == null || neededEdges.get(0) == null) {
                throw new ReportException(new GenericReportFault("We weren't able to find a way to join data across the specified fields. Please adjust the report to try again."));
            }
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
                        for (Key join : localEdge.connection.getSourceJoins()) {
                            queryStateNode.addKey(join);
                        }
                        for (AnalysisItem sourceItem : localEdge.connection.getSourceItems()) {
                            queryStateNode.addJoinItem(sourceItem);
                        }
                        for (Key join : localEdge.connection.getTargetJoins()) {
                            targetNode.addKey(join);
                        }
                        for (AnalysisItem sourceItem : localEdge.connection.getTargetItems()) {
                            targetNode.addJoinItem(sourceItem);
                        }
                    } else {
                        for (Key join : localEdge.connection.getTargetJoins()) {
                            queryStateNode.addKey(join);
                        }
                        for (AnalysisItem sourceItem : localEdge.connection.getTargetItems()) {
                            queryStateNode.addJoinItem(sourceItem);
                        }
                        for (Key join : localEdge.connection.getSourceJoins()) {
                            targetNode.addKey(join);
                        }
                        for (AnalysisItem sourceItem : localEdge.connection.getSourceItems()) {
                            targetNode.addJoinItem(sourceItem);
                        }
                    }
                }
            }
        }

        QueryStateNode firstVertex = null;
        for (CompositeFeedConnection connection : connections) {
            if (neededNodes.containsKey(connection.getSourceFeedID()) && neededNodes.containsKey(connection.getTargetFeedID())) {
                Edge edge = new Edge(connection);
                QueryStateNode source = queryNodeMap.get(connection.getSourceFeedID());
                if (firstVertex == null) {
                    firstVertex = source;
                }
                QueryStateNode target = queryNodeMap.get(connection.getTargetFeedID());
                reducedGraph.addEdge(source, target, edge);
            }
        }

        // actually connecting the data sets...

        DataSet dataSet = null;

        List<String> auditStrings = new ArrayList<String>();
        ClosestFirstIterator<QueryStateNode, Edge> iter = new ClosestFirstIterator<QueryStateNode, Edge>(reducedGraph, firstVertex);

        while (iter.hasNext()) {
            QueryStateNode sourceNode = iter.next();
            if (sourceNode.queryData.dataSet == null) {
                sourceNode.produceDataSet(insightRequestMetadata);
            }
            Edge last = iter.getSpanningTreeEdge(sourceNode);
            if (last != null) {

                QueryStateNode targetNode;
                if (reducedGraph.getEdgeSource(last) == sourceNode) {
                    targetNode = reducedGraph.getEdgeTarget(last);
                } else {
                    targetNode = reducedGraph.getEdgeSource(last);
                }
                if (targetNode.queryData.dataSet == null) {
                    targetNode.produceDataSet(insightRequestMetadata);
                }
                if (last.connection.getSourceFeedID() != sourceNode.feedID) {
                    QueryStateNode swap = sourceNode;
                    sourceNode = targetNode;
                    targetNode = swap;
                }
                MergeAudit mergeAudit = last.connection.merge(sourceNode.queryData.dataSet, targetNode.queryData.dataSet,
                        sourceNode.queryData.neededItems, targetNode.queryData.neededItems,
                        sourceNode.dataSourceName, targetNode.dataSourceName, conn, sourceNode.feedID, targetNode.feedID);
                dataSet = mergeAudit.getDataSet();
                auditStrings.addAll(mergeAudit.getMergeStrings());
                sourceNode.queryData.dataSet = dataSet;
                sourceNode.queryData.neededItems.addAll(targetNode.queryData.neededItems);
                targetNode.queryData = sourceNode.queryData;
            }

        }
        dataSet.setAudits(auditStrings);

        return dataSet;
    }

    private class QueryData {
        private DataSet dataSet;
        private Set<AnalysisItem> neededItems = new HashSet<AnalysisItem>();
    }

    protected boolean alwaysPassThrough(AnalysisItem analysisItem) {
        return false;
    }

    private class QueryStateNode {
        private long feedID;
        private QueryData queryData = new QueryData();
        private Set<AnalysisItem> neededItems = new HashSet<AnalysisItem>();
        private List<AnalysisItem> allAnalysisItems = new ArrayList<AnalysisItem>();
        private Collection<FilterDefinition> filters = new ArrayList<FilterDefinition>();
        private Collection<AnalysisItem> allFeedItems;
        private Collection<AnalysisItem> joinItems = new HashSet<AnalysisItem>();
        private String dataSourceName;
        private EIConnection conn;

        private QueryStateNode(long feedID, EIConnection conn) {
            this.feedID = feedID;
            Feed feed = FeedRegistry.instance().getFeed(feedID, conn);
            this.conn = conn;
            dataSourceName = feed.getName();
            allFeedItems = feed.getFields();
        }

        public boolean handles(Key key) {
            return key.hasDataSource(feedID);
        }

        public void addJoinItem(AnalysisItem analysisItem) {
            for (AnalysisItem field : getFields()) {
                if (analysisItem.toDisplay().equals(field.toDisplay())) {
                    analysisItem = field;
                    break;
                }
            }
            List<AnalysisItem> items = analysisItem.getAnalysisItems(new ArrayList<AnalysisItem>(allFeedItems), Arrays.asList(analysisItem), false, true, false, CleanupComponent.AGGREGATE_CALCULATIONS);
            for (AnalysisItem item : items) {
                addItem(item);
                joinItems.add(item);
            }
        }

        public void addItem(AnalysisItem analysisItem) {
            if (!analysisItem.isDerived()) {
                neededItems.add(analysisItem);
            }
            queryData.neededItems.add(analysisItem);
        }

        public void addKey(Key key) {
            boolean alreadyHaveItem = false;
            for (AnalysisItem analysisItem : queryData.neededItems) {
                if (analysisItem.hasType(AnalysisItemTypes.DIMENSION) && analysisItem.getKey().toKeyString().equals(key.toKeyString())) {
                    alreadyHaveItem = true;
                }
            }
            if (!alreadyHaveItem) {
                for (AnalysisItem analysisItem : getFields()) {
                    if (analysisItem.hasType(AnalysisItemTypes.DIMENSION) && analysisItem.getKey().toBaseKey().getKeyID() == key.toBaseKey().getKeyID()) {
                        addJoinItem(analysisItem);
                    }
                }
            }
        }

        public void produceDataSet(InsightRequestMetadata insightRequestMetadata) throws ReportException {

            Feed feed = FeedRegistry.instance().getFeed(feedID, conn);

            DataSet dataSet = feed.getAggregateDataSet(neededItems, filters, insightRequestMetadata, allAnalysisItems, false, conn);

            Pipeline pipeline;
            if (getDataSource().getFeedType().getType() == FeedType.BASECAMP_MASTER.getType()) {
                pipeline = new CompositeReportPipeline();
            } else {
                pipeline = new AltCompositeReportPipeline(joinItems);
            }
            WSListDefinition analysisDefinition = new WSListDefinition();
            analysisDefinition.setColumns(new ArrayList<AnalysisItem>(queryData.neededItems));
            pipeline.setup(analysisDefinition, feed, insightRequestMetadata);
            queryData.dataSet = pipeline.toDataSet(dataSet);
        }

        public void addFilter(FilterDefinition filterDefinition) {
            filters.add(filterDefinition);
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
