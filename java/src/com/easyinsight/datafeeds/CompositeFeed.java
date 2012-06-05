package com.easyinsight.datafeeds;


import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Key;
import com.easyinsight.core.DerivedKey;

import com.easyinsight.analysis.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.easyinsight.logging.LogClass;
import com.easyinsight.pipeline.*;
import org.antlr.runtime.RecognitionException;
import org.apache.jcs.JCS;
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

    private JCS resultCache = null;

    private JCS getCache(String cacheName) {

        try {
            return JCS.getInstance(cacheName);
        } catch (Exception e) {
            LogClass.error(e);
        }
        return null;
    }

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
        List<IJoin> connections;

        if (insightRequestMetadata.getJoinOverrides() != null && insightRequestMetadata.getJoinOverrides().size() > 0) {
            connections = new ArrayList<IJoin>();
            for (JoinOverride joinOverride : insightRequestMetadata.getJoinOverrides()) {
                if (joinOverride.getDataSourceID() == getFeedID()) {
                    if (joinOverride.getMarmotScript() != null && !"".equals(joinOverride.getMarmotScript())) {
                        try {
                            List<IJoin> newJoins = new ReportCalculation(joinOverride.getMarmotScript()).applyJoinCalculation();
                            for (IJoin newJoin : newJoins) {
                                //CompositeFeedCompositeConnection connection = (CompositeFeedCompositeConnection) newJoin;
                                newJoin.reconcile(compositeFeedNodes, getFields());
                            }
                            connections.addAll(newJoins);
                        } catch (RecognitionException e) {
                            throw new ReportException(new GenericReportFault(e.getMessage()));
                        }
                    } else {
                        connections.add(new CompositeFeedConnection(((DerivedKey) joinOverride.getSourceItem().getKey()).getFeedID(),
                            ((DerivedKey) joinOverride.getTargetItem().getKey()).getFeedID(), joinOverride.getSourceItem(),
                            joinOverride.getTargetItem(), joinOverride.getSourceName(), joinOverride.getTargetName(), joinOverride.isSourceOuterJoin(),
                            joinOverride.isTargetOuterJoin(), joinOverride.isSourceJoinOriginal(), joinOverride.isTargetJoinOriginal()));
                    }
                }
            }
        } else {
            connections = new ArrayList<IJoin>();
            connections.addAll(this.connections);
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
                    if (filterDefinition.isSingleSource() && filterDefinition.getField() != null) {
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
            Map<Key, Key> keyMap = new HashMap<Key, Key>();
            for (AnalysisItem analysisItem : getFields()) {
                Key key = analysisItem.getKey();
                if (key instanceof DerivedKey) {
                    DerivedKey derivedKey = (DerivedKey) key;
                    keyMap.put(derivedKey.getParentKey(), derivedKey);
                }
            }

            for (AnalysisItem analysisItem : itemSet) {
                Key replacement = keyMap.get(analysisItem.getKey());
                if (replacement != null) {
                    analysisItem.setKey(replacement);
                }
            }

            Iterator<AnalysisItem> analysisItemIterator = itemSet.iterator();
            while (analysisItemIterator.hasNext()) {
                AnalysisItem analysisItem = analysisItemIterator.next();
                for (QueryStateNode queryStateNode : queryNodeMap.values()) {
                    if (queryStateNode.handles(analysisItem.getKey())) {
                        analysisItemIterator.remove();
                        neededNodes.put(queryStateNode.feedID, queryStateNode);
                        queryStateNode.addItem(analysisItem);
                    }
                }
            }
        }
        /*if (itemSet.size() > 0) {
            throw new InvalidFieldsException(itemSet);
        }*/

        for (IJoin connection : connections) {
            QueryStateNode source = queryNodeMap.get(connection.getSourceFeedID());
            QueryStateNode target = queryNodeMap.get(connection.getTargetFeedID());
            Edge edge = new Edge(connection);
            graph.addEdge(source, target, edge);
        }

        if (neededNodes.size() == 1) {
            QueryStateNode queryStateNode = neededNodes.values().iterator().next();
            DataSet dataSet = queryStateNode.produceDataSet(insightRequestMetadata);
            if (insightRequestMetadata.isLookupTableAggregate()) {
                CompositeReportFinishPipeline pipeline = new CompositeReportFinishPipeline();
                WSListDefinition analysisDefinition = new WSListDefinition();
                Set<AnalysisItem> items = new HashSet<AnalysisItem>();
                for (AnalysisItem analysisItem : analysisItems) {
                    if (insightRequestMetadata.getReportItems().contains(analysisItem)) {
                        items.add(analysisItem);
                    }
                }
                analysisDefinition.setColumns(new ArrayList<AnalysisItem>(analysisItems));
                pipeline.setup(analysisDefinition, this, insightRequestMetadata, items);
                return pipeline.toDataSet(dataSet);
            } else {
                return dataSet;
            }
        }

        if (neededNodes.size() == 0) {
            return new DataSet();
        }

        // determine which keys are matched to which fields as we proceed here

        try {
            Iterator<QueryStateNode> neededNodeIter = new HashMap<Long, QueryStateNode>(neededNodes).values().iterator();
            QueryStateNode firstNode = neededNodeIter.next();
            while (neededNodeIter.hasNext()) {
                QueryStateNode nextNode = neededNodeIter.next();
                List<Edge> neededEdges = DijkstraShortestPath.findPathBetween(graph, firstNode, nextNode);
                if (neededEdges == null || neededEdges.get(0) == null) {
                    boolean stillOkay = false;
                    for (IJoin join : connections) {
                        if (join.isPostJoin()) {
                            stillOkay = true;
                        }
                    }
                    if (!stillOkay) {
                        throw new ReportException(new GenericReportFault("We weren't able to find a way to join data across the specified fields. Please adjust the report to try again."));    
                    }
                }
                for (Edge edge : neededEdges) {
                    QueryStateNode precedingNode = graph.getEdgeSource(edge);
                    QueryStateNode followingNode = graph.getEdgeTarget(edge);
                    //System.out.println("identified edge from " + precedingNode.dataSourceName + " to " + followingNode.dataSourceName);
                    neededNodes.put(precedingNode.feedID, precedingNode);
                    neededNodes.put(followingNode.feedID, followingNode);
                }
            }
        } catch (ReportException e) {
            DataSet dataSet = new DataSet();
            for (QueryStateNode queryStateNode : neededNodes.values()) {
                DataSet childSet = queryStateNode.produceDataSet(insightRequestMetadata);
                for (IRow row : childSet.getRows()) {
                    dataSet.addRow(row);
                }
            }
            return dataSet;
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
                    //System.out.println("edge between " + queryStateNode.dataSourceName + " and " + targetNode.dataSourceName);
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
        Map<Long, Collection<IJoin>> conns = new HashMap<Long, Collection<IJoin>>();
        Map<IJoin, FilterDefinition> filterMap = new HashMap<IJoin, FilterDefinition>();
        List<IJoin> postJoins = new ArrayList<IJoin>();
        for (IJoin connection : connections) {
            if (neededNodes.containsKey(connection.getSourceFeedID()) && neededNodes.containsKey(connection.getTargetFeedID())) {
                if (connection.isPostJoin()) {
                    postJoins.add(connection);
                } else {
                    Collection<IJoin> sourceConnList = conns.get(connection.getSourceFeedID());
                    if (sourceConnList == null) {
                        sourceConnList = new ArrayList<IJoin>();
                        conns.put(connection.getSourceFeedID(), sourceConnList);
                    }
                    sourceConnList.add(connection);
                    Collection<IJoin> targetConnList = conns.get(connection.getTargetFeedID());
                    if (targetConnList == null) {
                        targetConnList = new ArrayList<IJoin>();
                        conns.put(connection.getTargetFeedID(), targetConnList);
                    }
                    targetConnList.add(connection);
                    Edge edge = new Edge(connection);
                    QueryStateNode source = queryNodeMap.get(connection.getSourceFeedID());
                    if (firstVertex == null) {
                        firstVertex = source;
                    }

                    QueryStateNode target = queryNodeMap.get(connection.getTargetFeedID());
                    //System.out.println("** adding edge of " + source.dataSourceName + " to " + target.dataSourceName);
                    reducedGraph.addEdge(source, target, edge);
                }
            }
        }

        // actually connecting the data sets...

        DataSet dataSet = null;

        List<String> auditStrings = new ArrayList<String>();

        Map<Long, QueryData> map = new HashMap<Long, QueryData>();

        for (QueryStateNode queryStateNode : neededNodes.values()) {
            map.put(queryStateNode.feedID, queryStateNode.queryData);
        }

        int operations = 0;

        if (insightRequestMetadata.isTraverseAllJoins()) {
            Set<Edge> edges = reducedGraph.edgeSet();
            for (Edge last : edges) {
                QueryStateNode sourceNode = reducedGraph.getEdgeSource(last);
                QueryData sourceQueryData = map.get(sourceNode.feedID);
                QueryStateNode targetNode = reducedGraph.getEdgeTarget(last);
                if (sourceQueryData.dataSet == null) {
                    Collection<IJoin> myConnections = conns.get(sourceNode.feedID);
                    for (IJoin myConn : myConnections) {
                        FilterDefinition filter = filterMap.get(myConn);
                        if (filter != null) {
                            if (insightRequestMetadata.isOptimized() || myConn.isOptimized()) {
                                sourceNode.addFilter(filter);
                            }
                        }
                    }
                    sourceQueryData.dataSet = sourceNode.produceDataSet(insightRequestMetadata);
                    /*auditBuilder.append("<p>" + sourceNode.dataSourceName + " original data set</p>");
                    auditBuilder.append(ExportService.dataSetToHTMLTable(sourceQueryData.neededItems, sourceQueryData.dataSet, conn, insightRequestMetadata));*/
                    if (insightRequestMetadata.isOptimized()) {
                        for (IJoin myConn : myConnections) {
                            FilterDefinition filter = filterMap.get(myConn);
                            if (filter == null && myConn instanceof CompositeFeedConnection) {
                                QueryStateNode joinTargetNode;
                                if (sourceNode.feedID == myConn.getSourceFeedID()) {
                                    joinTargetNode = queryNodeMap.get(myConn.getTargetFeedID());
                                } else {
                                    joinTargetNode = queryNodeMap.get(myConn.getSourceFeedID());
                                }
                                //System.out.println("defining filter between " + sourceNode.dataSourceName + " and " + targetNode.dataSourceName);
                                FilterDefinition joinFilter = createJoinFilter(sourceNode, sourceQueryData.dataSet, joinTargetNode, (CompositeFeedConnection) myConn);
                                filterMap.put(myConn, joinFilter);
                            }
                        }
                    }
                }
                //System.out.println("joining " + sourceNode.dataSourceName + " and " + targetNode.dataSourceName);

                QueryData targetQueryData = map.get(targetNode.feedID);
                boolean swapped = false;
                if (last.connection.getSourceFeedID() != sourceNode.feedID) {
                    swapped = true;
                    QueryStateNode swap = sourceNode;
                    sourceNode = targetNode;
                    targetNode = swap;
                    QueryData swapData = sourceQueryData;
                    sourceQueryData = targetQueryData;
                    targetQueryData = swapData;
                }
                FilterDefinition joinFilter = null;
                if (insightRequestMetadata.isOptimized() && last.connection instanceof CompositeFeedConnection) {
                    joinFilter = createJoinFilter(sourceNode, sourceQueryData.dataSet, targetNode, (CompositeFeedConnection) last.connection);
                }
                if (!swapped) {
                    if (targetQueryData.dataSet == null) {
                        if (insightRequestMetadata.isOptimized() || last.connection.isOptimized()) {
                            targetNode.addFilter(joinFilter);
                        }
                        targetQueryData.dataSet = targetNode.produceDataSet(insightRequestMetadata);
                    }
                } else {
                    if (sourceQueryData.dataSet == null) {
                        if (insightRequestMetadata.isOptimized() || last.connection.isOptimized()) {
                            sourceNode.addFilter(joinFilter);
                        }
                        sourceQueryData.dataSet = sourceNode.produceDataSet(insightRequestMetadata);
                    }
                }
                System.out.println("joining " + sourceNode.dataSourceName + " to " + targetNode.dataSourceName);
                MergeAudit mergeAudit;
                if (last.connection.isTargetJoinOnOriginal()) {
                    mergeAudit = last.connection.merge(sourceQueryData.dataSet, targetNode.originalDataSet,
                        sourceQueryData.neededItems, targetQueryData.neededItems,
                        sourceNode.dataSourceName, targetNode.dataSourceName, conn, sourceNode.feedID, targetNode.feedID, operations);
                } else {
                    mergeAudit = last.connection.merge(sourceQueryData.dataSet, targetQueryData.dataSet,
                        sourceQueryData.neededItems, targetQueryData.neededItems,
                        sourceNode.dataSourceName, targetNode.dataSourceName, conn, sourceNode.feedID, targetNode.feedID, operations);
                }
                dataSet = mergeAudit.getDataSet();
                operations = mergeAudit.getOperations();

                auditStrings.addAll(mergeAudit.getMergeStrings());
                sourceQueryData.dataSet = dataSet;
                sourceQueryData.neededItems.addAll(targetQueryData.neededItems);
                sourceQueryData.ids.addAll(targetQueryData.ids);
                /*auditBuilder.append("<h1>" + "After joining " + sourceNode.dataSourceName + " to " + targetNode.dataSourceName + "</h1>");
                auditBuilder.append(ExportService.dataSetToHTMLTable(sourceQueryData.neededItems, dataSet, conn, insightRequestMetadata));*/
                for (Long id : sourceQueryData.ids) {
                    map.put(id, sourceQueryData);
                }
            }
        } else {

            ClosestFirstIterator<QueryStateNode, Edge> iter = new ClosestFirstIterator<QueryStateNode, Edge>(reducedGraph, firstVertex);
            while (iter.hasNext()) {
                QueryStateNode sourceNode = iter.next();
                QueryData sourceQueryData = map.get(sourceNode.feedID);
                if (sourceQueryData.dataSet == null) {
                    Collection<IJoin> myConnections = conns.get(sourceNode.feedID);
                    for (IJoin myConn : myConnections) {
                        FilterDefinition filter = filterMap.get(myConn);
                        if (filter != null) {
                            if (insightRequestMetadata.isOptimized()) {
                                System.out.println("adding filter for " + sourceNode.dataSourceName);
                                sourceNode.addFilter(filter);
                            }
                        }
                    }
                    sourceQueryData.dataSet = sourceNode.produceDataSet(insightRequestMetadata);
                    if (insightRequestMetadata.isOptimized()) {
                        for (IJoin myConn : myConnections) {
                            FilterDefinition filter = filterMap.get(myConn);
                            if (filter == null && myConn instanceof CompositeFeedConnection) {
                                QueryStateNode targetNode;
                                if (sourceNode.feedID == myConn.getSourceFeedID()) {
                                    targetNode = queryNodeMap.get(myConn.getTargetFeedID());
                                } else {
                                    targetNode = queryNodeMap.get(myConn.getSourceFeedID());
                                }
                                System.out.println("defining filter between " + sourceNode.dataSourceName + " and " + targetNode.dataSourceName);
                                FilterDefinition joinFilter = createJoinFilter(sourceNode, sourceQueryData.dataSet, targetNode, (CompositeFeedConnection) myConn);
                                filterMap.put(myConn, joinFilter);
                            }
                        }
                    }
                }
                Edge last = iter.getSpanningTreeEdge(sourceNode);
                if (last != null) {

                    QueryStateNode targetNode;
                    if (reducedGraph.getEdgeSource(last) == sourceNode) {
                        targetNode = reducedGraph.getEdgeTarget(last);
                    } else {
                        targetNode = reducedGraph.getEdgeSource(last);
                    }

                    //System.out.println("joining " + sourceNode.dataSourceName + " and " + targetNode.dataSourceName);

                    QueryData targetQueryData = map.get(targetNode.feedID);
                    boolean swapped = false;
                    if (last.connection.getSourceFeedID() != sourceNode.feedID) {
                        System.out.println("Swapping " + sourceNode.dataSourceName + " and " + targetNode.dataSourceName);
                        swapped = true;
                        QueryStateNode swap = sourceNode;
                        sourceNode = targetNode;
                        targetNode = swap;
                        QueryData swapData = sourceQueryData;
                        sourceQueryData = targetQueryData;
                        targetQueryData = swapData;
                    }
                    FilterDefinition joinFilter = null;
                    if (insightRequestMetadata.isOptimized() && last.connection instanceof CompositeFeedConnection) {
                        joinFilter = createJoinFilter(sourceNode, sourceQueryData.dataSet, targetNode, (CompositeFeedConnection) last.connection);
                    }
                    if (!swapped) {
                        if (targetQueryData.dataSet == null) {
                            if (insightRequestMetadata.isOptimized()) {
                                targetNode.addFilter(joinFilter);
                            }
                            targetQueryData.dataSet = targetNode.produceDataSet(insightRequestMetadata);
                        }
                    } else {
                        if (sourceQueryData.dataSet == null) {
                            if (insightRequestMetadata.isOptimized()) {
                                sourceNode.addFilter(joinFilter);
                            }
                            sourceQueryData.dataSet = sourceNode.produceDataSet(insightRequestMetadata);
                        }
                    }

                    System.out.println("joining " + sourceNode.dataSourceName + " to " + targetNode.dataSourceName + " with " + sourceQueryData.dataSet.getRows().size() + " and " + targetQueryData.dataSet.getRows().size());
                    MergeAudit mergeAudit = last.connection.merge(sourceQueryData.dataSet, targetQueryData.dataSet,
                            sourceQueryData.neededItems, targetQueryData.neededItems,
                            sourceNode.dataSourceName, targetNode.dataSourceName, conn, sourceNode.feedID, targetNode.feedID, operations);
                    operations = mergeAudit.getOperations();
                    dataSet = mergeAudit.getDataSet();
                    auditStrings.addAll(mergeAudit.getMergeStrings());
                    sourceQueryData.dataSet = dataSet;
                    sourceQueryData.neededItems.addAll(targetQueryData.neededItems);
                    sourceQueryData.ids.addAll(targetQueryData.ids);
                    for (Long id : sourceQueryData.ids) {
                        map.put(id, sourceQueryData);
                    }
                }

            }
        }

        for (IJoin postJoin : postJoins) {
            QueryStateNode queryStateNode = queryNodeMap.get(postJoin.getTargetFeedID());
            DataSet targetSet = queryStateNode.produceDataSet(insightRequestMetadata);
            dataSet = postJoin.merge(dataSet, targetSet, null, queryStateNode.neededItems, null, queryStateNode.dataSourceName, conn, 0, queryStateNode.feedID, operations).getDataSet();
        }

        dataSet.setAudits(auditStrings);
        /*try {
            new SendGridEmail().sendEmail("jboe@easy-insight.com", "Data Source Audit", auditBuilder.toString(), "jboe@easy-insight.com", true, "Audit Test");
        } catch (MessagingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }*/
        if (!insightRequestMetadata.isOptimized() && !insightRequestMetadata.isTraverseAllJoins()) {
            if (insightRequestMetadata.isLookupTableAggregate()) {
                CompositeReportFinishPipeline pipeline = new CompositeReportFinishPipeline();
                WSListDefinition analysisDefinition = new WSListDefinition();
                Set<AnalysisItem> items = new HashSet<AnalysisItem>();
                for (AnalysisItem analysisItem : analysisItems) {
                    if (insightRequestMetadata.getReportItems().contains(analysisItem)) {
                        items.add(analysisItem);
                    }
                }
                analysisDefinition.setColumns(new ArrayList<AnalysisItem>(analysisItems));
                pipeline.setup(analysisDefinition, this, insightRequestMetadata, items);
                return pipeline.toDataSet(dataSet);
            } else {
                CompositeReportPipeline pipeline = new CompositeReportPipeline();
                WSListDefinition analysisDefinition = new WSListDefinition();
                analysisDefinition.setColumns(new ArrayList<AnalysisItem>(analysisItems));
                pipeline.setup(analysisDefinition, this, insightRequestMetadata);
                return pipeline.toDataSet(dataSet);
            }
        } else {
            return dataSet;
        }
    }

    private FilterDefinition createJoinFilter(QueryStateNode sourceNode, DataSet dataSet, QueryStateNode targetNode, CompositeFeedConnection connection) {
        FilterValueDefinition filterValueDefinition = new FilterValueDefinition();
        if (connection.getTargetJoin() == null) {
            if (connection.getTargetItem().hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                return null;
            }
            filterValueDefinition.setField(connection.getTargetItem());
        } else {
            AnalysisItem target = findFieldForKey(targetNode.neededItems, connection.getTargetJoin(), targetNode.feedID);
            if (target == null) {
                return null;
            }
            if (target.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                return null;
            }
            filterValueDefinition.setField(target);
        }
        filterValueDefinition.setInclusive(true);
        if (connection.getSourceJoin() == null) {
            filterValueDefinition.setFilteredValues(obtainValues(dataSet, findFieldForItem(sourceNode.neededItems, connection.getSourceItem())));
        } else {
            filterValueDefinition.setFilteredValues(obtainValues(dataSet, findFieldForKey(sourceNode.neededItems, connection.getSourceJoin(), sourceNode.feedID)));
        }
        return filterValueDefinition;
    }

    private AnalysisItem findFieldForItem(Collection<AnalysisItem> analysisItems, AnalysisItem matchItem) {
        for (AnalysisItem item : analysisItems) {
            if (item.getKey().toKeyString().equals(matchItem.getKey().toKeyString())) {
                return item;
            }
        }
        return null;
    }

    private List<Object> obtainValues(DataSet dataSet, AnalysisItem analysisItem) {
        List<Object> objects = new ArrayList<Object>();
        for (IRow row : dataSet.getRows()) {
            Value value = row.getValue(analysisItem.createAggregateKey());
            objects.add(value);
        }
        return objects;
    }

    private AnalysisItem findFieldForKey(Collection<AnalysisItem> analysisItems, Key key, long sourceID) {

        for (AnalysisItem analysisItem : analysisItems) {
            if (matches(analysisItem, key, sourceID)) {
                return analysisItem;
            }
        }
        for (AnalysisItem analysisItem : analysisItems) {
            if (matches(analysisItem, key)) {
                return analysisItem;
            }
        }
        return null;
    }

    private boolean matches(AnalysisItem source, Key targetKey, long sourceID) {
        Key key = source.getKey();
        if (key.toKeyString().equals(targetKey.toKeyString())) {
            if (key instanceof DerivedKey) {
                DerivedKey derivedKey = (DerivedKey) key;
                if (derivedKey.getFeedID() == sourceID) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean matches(AnalysisItem source, Key targetKey) {
        Key key = source.getKey();
        return (key.toKeyString().equals(targetKey.toKeyString()));
    }

    private class QueryData {
        private DataSet dataSet;
        private Set<AnalysisItem> neededItems = new HashSet<AnalysisItem>();
        private Set<Long> ids = new HashSet<Long>();

        private QueryData(long id) {
            ids.add(id);
        }
    }

    protected boolean alwaysPassThrough(AnalysisItem analysisItem) {
        return false;
    }

    private class QueryStateNode {
        private long feedID;
        private QueryData queryData;
        private Set<AnalysisItem> neededItems = new HashSet<AnalysisItem>();
        private List<AnalysisItem> allAnalysisItems = new ArrayList<AnalysisItem>();
        private Collection<FilterDefinition> filters = new ArrayList<FilterDefinition>();
        private Collection<AnalysisItem> allFeedItems;
        private Collection<AnalysisItem> joinItems = new HashSet<AnalysisItem>();
        private String dataSourceName;
        private EIConnection conn;
        private DataSet originalDataSet;

        private QueryStateNode(long feedID, EIConnection conn) {
            this.feedID = feedID;
            queryData = new QueryData(feedID);
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
            List<AnalysisItem> items = analysisItem.getAnalysisItems(new ArrayList<AnalysisItem>(allFeedItems), Arrays.asList(analysisItem), false, true, CleanupComponent.AGGREGATE_CALCULATIONS, new HashSet<AnalysisItem>(), new AnalysisItemRetrievalStructure());
            for (AnalysisItem item : items) {
                addItem(item);
                joinItems.add(item);
            }
        }

        public void addItem(AnalysisItem analysisItem) {
            if (analysisItem.hasType(AnalysisItemTypes.CALCULATION)) {
                AnalysisCalculation analysisCalculation = (AnalysisCalculation) analysisItem;
                if (analysisCalculation.isCachedCalculation()) {
                    neededItems.add(analysisItem);
                }
            } else if (!analysisItem.isDerived()) {
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

        public DataSet produceDataSet(InsightRequestMetadata insightRequestMetadata) throws ReportException {

            Feed feed = FeedRegistry.instance().getFeed(feedID, conn);

            DataSet dataSet = feed.getAggregateDataSet(neededItems, filters, insightRequestMetadata, allAnalysisItems, false, conn);

            Pipeline pipeline;
            if (!insightRequestMetadata.isTraverseAllJoins() && getDataSource().getFeedType().getType() == FeedType.BASECAMP_MASTER.getType()) {
                pipeline = new CompositeReportPipeline();
            } else {
                pipeline = new AltCompositeReportPipeline(joinItems);
            }
            /*WSListDefinition analysisDefinition = new WSListDefinition();
            analysisDefinition.setColumns(new ArrayList<AnalysisItem>(queryData.neededItems));*/
            pipeline.setup(queryData.neededItems, feed.getFields(), insightRequestMetadata);
            originalDataSet = pipeline.toDataSet(dataSet);
            return originalDataSet;
        }

        public void addFilter(FilterDefinition filterDefinition) {
            filters.add(filterDefinition);
        }
    }

    private static class Edge {
        IJoin connection;

        private Edge(IJoin connection) {
            this.connection = connection;
        }
    }

    public List<CompositeFeedNode> getCompositeFeedNodes() {
        return compositeFeedNodes;
    }

    public List<CompositeFeedConnection> getConnections() {
        return connections;
    }

    @Override
    public DataSourceInfo createSourceInfo(EIConnection conn) throws SQLException {
        PreparedStatement typeStmt = conn.prepareStatement("SELECT FEED_TYPE FROM DATA_FEED WHERE DATA_FEED_ID = ?");
        Set<Long> validChildren = new HashSet<Long>();
        for (CompositeFeedNode node : compositeFeedNodes) {
            typeStmt.setLong(1, node.getDataFeedID());
            ResultSet rs = typeStmt.executeQuery();
            rs.next();
            int type = rs.getInt(1);
            if (type == FeedType.HIGHRISE_COMPOSITE.getType() || type == FeedType.QUICKBASE_COMPOSITE.getType() ||
                    type == FeedType.BASECAMP_MASTER.getType() || type == FeedType.HARVEST_COMPOSITE.getType() ||
                    type == FeedType.ZENDESK_COMPOSITE.getType() || type == FeedType.BATCHBOOK_COMPOSITE.getType()) {
                validChildren.add(node.getDataFeedID());
            }
        }
        DataSourceInfo dataSourceInfo;
        if (validChildren.size() == 1) {
            long child = validChildren.iterator().next();
            Feed childFeed = FeedRegistry.instance().getFeed(child, conn);
            dataSourceInfo = childFeed.createSourceInfo(conn);
        } else {
            dataSourceInfo = super.createSourceInfo(conn);
        }
        typeStmt.close();
        return dataSourceInfo;
    }
}
