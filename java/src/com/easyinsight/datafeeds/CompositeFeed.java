package com.easyinsight.datafeeds;


import com.easyinsight.core.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;

import com.easyinsight.analysis.*;

import java.sql.SQLException;
import java.util.*;

import com.easyinsight.intention.IntentionSuggestion;
import com.easyinsight.pipeline.*;
import org.antlr.runtime.RecognitionException;
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
    private List<AddonReport> addonReports;

    // two distinct scenarios...
    // one is combining some # of data sources into a

    public CompositeFeed() {
    }

    public CompositeFeed(List<CompositeFeedNode> compositeFeedNodes, List<CompositeFeedConnection> connections, List<AddonReport> addonReports) {
        this.compositeFeedNodes = compositeFeedNodes;
        this.connections = connections;
        this.addonReports = addonReports;
    }

    public Key originalField(Key key, AnalysisItem originalItem) {
        if (key instanceof ReportKey) {
            ReportKey reportKey = (ReportKey) key;
            Key parentKey = reportKey.getParentKey();
            ReportKey parentReportKey = (ReportKey) parentKey;
            AnalysisBasedFeed cachedFeed = new AnalysisBasedFeed();
            cachedFeed.setAnalysisDefinition(new AnalysisStorage().getAnalysisDefinition(parentReportKey.getReportID()));
            return cachedFeed.originalField(parentKey, originalItem);
        } else if (key instanceof DerivedKey) {
            DerivedKey derivedKey = (DerivedKey) key;
            long dataSourceID = derivedKey.getFeedID();
            for (CompositeFeedNode node : compositeFeedNodes) {
                if (node.getDataFeedID() == dataSourceID) {
                    Feed feed = FeedRegistry.instance().getFeed(dataSourceID);
                    return feed.originalField(derivedKey.getParentKey(), originalItem);
                }
            }
        }
        return null;
    }

    public List<Long> getDataSourceIDs() {
        List<Long> ids = super.getDataSourceIDs();
        for (CompositeFeedNode node : compositeFeedNodes) {
            ids.add(node.getDataFeedID());
        }
        return ids;
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

        List<CompositeFeedNode> compositeFeedNodes = new ArrayList<CompositeFeedNode>(this.compositeFeedNodes);

        //
        List<AddonReport> addonReports = insightRequestMetadata.getAddonReports();
        if (addonReports != null) {
            for (AddonReport addonReport : addonReports) {
                CompositeFeedNode reportNode = new CompositeFeedNode();
                reportNode.setReportID(addonReport.getReportID());
                compositeFeedNodes.add(reportNode);
            }
        }
        for (AddonReport addonReport : this.addonReports) {
            CompositeFeedNode reportNode = new CompositeFeedNode();
            reportNode.setReportID(addonReport.getReportID());
            compositeFeedNodes.add(reportNode);
        }

        List<IJoin> connections = null;

        boolean joinsOverridden = false;

        if (insightRequestMetadata.getJoinOverrides() != null && insightRequestMetadata.getJoinOverrides().size() > 0) {
            connections = new ArrayList<IJoin>();
            for (JoinOverride joinOverride : insightRequestMetadata.getJoinOverrides()) {
                if (joinOverride.getDataSourceID() == getFeedID()) {
                    if (joinOverride.getMarmotScript() != null && !"".equals(joinOverride.getMarmotScript())) {
                        try {
                            List<IJoin> newJoins = new ReportCalculation(joinOverride.getMarmotScript()).applyJoinCalculation(new ArrayList<FilterDefinition>(filters), getFeedID());
                            for (IJoin newJoin : newJoins) {
                                newJoin.reconcile(compositeFeedNodes, getFields());
                            }
                            connections.addAll(newJoins);
                        } catch (RecognitionException e) {
                            throw new ReportException(new GenericReportFault(e.getMessage()));
                        }
                    } else {
                        if ((joinOverride.getSourceItem() != null && "com.easyinsight.core.Key".equals(joinOverride.getSourceItem().getKey().getClass().getName())) ||
                                (joinOverride.getTargetItem() != null && "com.easyinsight.core.Key".equals(joinOverride.getTargetItem().getKey().getClass().getName()))) {
                            System.out.println("Bypassing bad state join.");
                            continue;
                        }
                        if (joinOverride.getJoinType() == JoinOverride.NORMAL) {
                            CompositeFeedConnection compositeFeedConnection = new CompositeFeedConnection();
                            compositeFeedConnection.setSourceItem(joinOverride.getSourceItem());
                            compositeFeedConnection.setTargetItem(joinOverride.getTargetItem());
                            compositeFeedConnection.setSourceItems(Arrays.asList(joinOverride.getSourceItem()));
                            compositeFeedConnection.setTargetItems(Arrays.asList(joinOverride.getTargetItem()));
                            compositeFeedConnection.setSourceFeedName(joinOverride.getSourceName());
                            compositeFeedConnection.setTargetFeedName(joinOverride.getTargetName());
                            compositeFeedConnection.setSourceJoinOnOriginal(joinOverride.isSourceJoinOriginal());
                            compositeFeedConnection.setTargetJoinOnOriginal(joinOverride.isTargetJoinOriginal());
                            compositeFeedConnection.setSourceOuterJoin(joinOverride.isSourceOuterJoin());
                            compositeFeedConnection.setTargetOuterJoin(joinOverride.isTargetOuterJoin());
                            compositeFeedConnection.setForceOuterJoin(joinOverride.getForceOuterJoin());
                            compositeFeedConnection.setSourceCardinality(joinOverride.getSourceCardinality());
                            compositeFeedConnection.setTargetCardinality(joinOverride.getTargetCardinality());
                            if (joinOverride.getSourceItem().getKey() instanceof DerivedKey) {
                                compositeFeedConnection.setSourceFeedID(((DerivedKey) joinOverride.getSourceItem().getKey()).getFeedID());
                            } else {
                                compositeFeedConnection.setSourceReportID(((ReportKey) joinOverride.getSourceItem().getKey()).getReportID());
                            }
                            if (joinOverride.getTargetItem().getKey() instanceof DerivedKey) {
                                compositeFeedConnection.setTargetFeedID(((DerivedKey) joinOverride.getTargetItem().getKey()).getFeedID());
                            } else {
                                compositeFeedConnection.setTargetReportID(((ReportKey) joinOverride.getTargetItem().getKey()).getReportID());
                            }
                            connections.add(compositeFeedConnection);
                        } else if (joinOverride.getJoinType() == JoinOverride.COMPOSITE) {
                            CompositeFeedCompositeConnection compositeFeedCompositeConnection = new CompositeFeedCompositeConnection();
                            if (joinOverride.getSourceItems().get(0).getKey() instanceof DerivedKey) {
                                compositeFeedCompositeConnection.setSourceFeedID(((DerivedKey) joinOverride.getSourceItems().get(0).getKey()).getFeedID());
                            } else {
                                compositeFeedCompositeConnection.setSourceReportID(((ReportKey) joinOverride.getSourceItems().get(0).getKey()).getReportID());
                            }
                            if (joinOverride.getTargetItems().get(0).getKey() instanceof DerivedKey) {
                                compositeFeedCompositeConnection.setTargetFeedID(((DerivedKey) joinOverride.getTargetItems().get(0).getKey()).getFeedID());
                            } else {
                                compositeFeedCompositeConnection.setTargetReportID(((ReportKey) joinOverride.getTargetItems().get(0).getKey()).getReportID());
                            }
                            compositeFeedCompositeConnection.setSourceItems(joinOverride.getSourceItems());
                            compositeFeedCompositeConnection.setTargetItems(joinOverride.getTargetItems());
                            connections.add(compositeFeedCompositeConnection);
                        }
                    }
                }
            }
            if (connections.size() > 0) {
                joinsOverridden = true;
            }
        }
        if (!joinsOverridden) {
            connections = new ArrayList<IJoin>();
            for (CompositeFeedConnection connection : this.connections) {
                if (connection.getMarmotScript() != null && !"".equals(connection.getMarmotScript())) {
                    try {
                        List<IJoin> newJoins = new ReportCalculation(connection.getMarmotScript()).applyJoinCalculation(new ArrayList<FilterDefinition>(filters), getFeedID());
                        for (IJoin newJoin : newJoins) {
                            newJoin.reconcile(compositeFeedNodes, getFields());
                        }
                        connections.addAll(newJoins);
                    } catch (RecognitionException e) {
                        throw new ReportException(new GenericReportFault(e.getMessage()));
                    }
                } else {
                    connections.add(connection);
                }
            }
        }

        Map<QueryNodeKey, QueryStateNode> queryNodeMap = new HashMap<QueryNodeKey, QueryStateNode>();

        UndirectedGraph<QueryStateNode, Edge> graph = new SimpleGraph<QueryStateNode, Edge>(Edge.class);

        // convert the nodes and edges into the graph with addVertex() and addEdge()
        Map<QueryNodeKey, QueryStateNode> neededNodes = new HashMap<QueryNodeKey, QueryStateNode>();

        Set<AnalysisItem> itemSet = new HashSet<AnalysisItem>(analysisItems);
        Set<AnalysisItem> alwaysSet = new HashSet<AnalysisItem>();

        // exchange install or kpi...
        // identify that a report is required
        //

        // identify the presence of an ad hoc federated source

        // match each field with its appropriate ad hoc source

        for (CompositeFeedNode node : compositeFeedNodes) {
            QueryStateNode queryStateNode = node.createQueryStateNode(conn, getFields(), insightRequestMetadata, filters, this);
            QueryNodeKey queryNodeKey = node.createQueryNodeKey();
            queryNodeMap.put(queryNodeKey, queryStateNode);
            graph.addVertex(queryStateNode);
            for (AnalysisItem analysisItem : analysisItems) {
                if (insightRequestMetadata.getPostProcessJoins().contains(analysisItem)) {
                    itemSet.remove(analysisItem);
                    continue;
                }
                if (queryStateNode.handles(analysisItem)) {
                    itemSet.remove(analysisItem);
                    neededNodes.put(queryNodeKey, queryStateNode);
                    queryStateNode.addItem(analysisItem);
                } else if (alwaysPassThrough(analysisItem)) {
                    alwaysSet.add(analysisItem);
                    queryStateNode.addItem(analysisItem);
                }
            }
            if (filters != null) {
                for (FilterDefinition filterDefinition : filters) {
                    if (filterDefinition.isSingleSource() && filterDefinition.getField() != null) {
                        if (insightRequestMetadata.getPostProcessJoins().contains(filterDefinition.getField())) {
                            continue;
                        }
                        if (queryStateNode.handles(filterDefinition.getField())) {
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

            Iterator<AnalysisItem> analysisItemIterator = itemSet.iterator();
            while (analysisItemIterator.hasNext()) {
                AnalysisItem analysisItem = analysisItemIterator.next();
                for (QueryStateNode queryStateNode : queryNodeMap.values()) {

                    // here's where we need the federated data source to come into play

                    if (queryStateNode.handles(analysisItem)) {
                        analysisItemIterator.remove();
                        neededNodes.put(queryStateNode.queryNodeKey(), queryStateNode);
                        queryStateNode.addItem(analysisItem);
                    }
                }
            }
        }


        for (IJoin connection : connections) {
            try {
                QueryStateNode source = queryNodeMap.get(connection.sourceQueryNodeKey());
                QueryStateNode target = queryNodeMap.get(connection.targetQueryNodeKey());
                if (source == target) {

                }
                Edge edge = new Edge(connection);
                graph.addEdge(source, target, edge);
            } catch (NullPointerException e) {
                throw new ReportException(new GenericReportFault("We weren't able to build the underlying graph of connections to support this report. Check Configure Joins for any problems that may be present in that setup."));
            }
        }


        if (neededNodes.size() == 1) {
            QueryStateNode queryStateNode = neededNodes.values().iterator().next();
            DataSet dataSet = queryStateNode.produceDataSet(insightRequestMetadata);
            NamedPipeline pipeline = (NamedPipeline) insightRequestMetadata.findPipeline(getName());
            if (pipeline != null) {
                WSListDefinition analysisDefinition = new WSListDefinition();
                List<AnalysisItem> fields = new ArrayList<AnalysisItem>(analysisItems);
                analysisDefinition.setColumns(fields);
                pipeline.setup(analysisDefinition, insightRequestMetadata, conn);
                dataSet = pipeline.toDataSet(dataSet);
            }
            return dataSet;
        }

        if (neededNodes.size() == 0) {
            return new DataSet();
        }

        // determine which keys are matched to which fields as we proceed here
        String error = null;
        try {
            Iterator<QueryStateNode> neededNodeIter = new HashMap<QueryNodeKey, QueryStateNode>(neededNodes).values().iterator();
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
                        error = "We weren't able to find a way to join data between " + firstNode.dataSourceName + " and " + nextNode.dataSourceName + ". Please adjust the report to try again.";
                        throw new ReportException(new GenericReportFault("We weren't able to find a way to join data between " + firstNode.dataSourceName + " and " + nextNode.dataSourceName + ". Please adjust the report to try again."));
                    }
                }
                for (Edge edge : neededEdges) {
                    QueryStateNode precedingNode = graph.getEdgeSource(edge);
                    QueryStateNode followingNode = graph.getEdgeTarget(edge);
                    neededNodes.put(precedingNode.queryNodeKey(), precedingNode);
                    neededNodes.put(followingNode.queryNodeKey(), followingNode);
                }
            }
        } catch (ReportException e) {
            insightRequestMetadata.getSuggestions().add(new IntentionSuggestion("No Join in Data",
                    error,
                    IntentionSuggestion.SCOPE_REPORT, IntentionSuggestion.WARNING_JOIN_FAILURE, IntentionSuggestion.WARNING));
            if (insightRequestMetadata.isNoDataOnNoJoin()) {
                return new DataSet();
            }
            Map<QueryStateNode, DataSet> map = new HashMap<QueryStateNode, DataSet>();

            DataSet dataSet = new DataSet();
            boolean oneRowData = true;
            for (QueryStateNode queryStateNode : neededNodes.values()) {
                DataSet childSet = queryStateNode.produceDataSet(insightRequestMetadata);
                if (childSet.getRows().size() > 1) {
                    oneRowData = false;
                }
                map.put(queryStateNode, childSet);
            }
            if (oneRowData) {
                IRow baseRow = dataSet.createRow();
                for (DataSet childSet : map.values()) {
                    for (IRow row : childSet.getRows()) {
                        baseRow.addValues(row.getValues());
                    }
                }
            } else {
                for (DataSet childSet : map.values()) {
                    for (IRow row : childSet.getRows()) {
                        dataSet.addRow(row);
                    }
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
                QueryStateNode exists = neededNodes.get(targetNode.queryNodeKey());
                if (exists != null) {
                    //System.out.println("edge between " + queryStateNode.dataSourceName + " and " + targetNode.dataSourceName);
                    if (queryStateIsSource) {
                        for (Key join : localEdge.connection.getSourceJoins()) {
                            queryStateNode.addKey(join);
                        }
                        for (AnalysisItem sourceItem : localEdge.connection.getSourceItems()) {
                            queryStateNode.addJoinItem(sourceItem, localEdge.connection.dateLevelForJoin());
                        }
                        for (Key join : localEdge.connection.getTargetJoins()) {
                            targetNode.addKey(join);
                        }
                        for (AnalysisItem sourceItem : localEdge.connection.getTargetItems()) {
                            targetNode.addJoinItem(sourceItem, localEdge.connection.dateLevelForJoin());
                        }
                    } else {
                        for (Key join : localEdge.connection.getTargetJoins()) {
                            queryStateNode.addKey(join);
                        }
                        for (AnalysisItem sourceItem : localEdge.connection.getTargetItems()) {
                            queryStateNode.addJoinItem(sourceItem, localEdge.connection.dateLevelForJoin());
                        }
                        for (Key join : localEdge.connection.getSourceJoins()) {
                            targetNode.addKey(join);
                        }
                        for (AnalysisItem sourceItem : localEdge.connection.getSourceItems()) {
                            targetNode.addJoinItem(sourceItem, localEdge.connection.dateLevelForJoin());
                        }
                    }
                }
            }
        }

        QueryStateNode firstVertex = null;
        Map<QueryNodeKey, Collection<IJoin>> conns = new HashMap<QueryNodeKey, Collection<IJoin>>();
        Map<IJoin, FilterDefinition> filterMap = new HashMap<IJoin, FilterDefinition>();
        List<IJoin> postJoins = new ArrayList<IJoin>();
        for (IJoin connection : connections) {
            if (neededNodes.containsKey(connection.sourceQueryNodeKey()) && neededNodes.containsKey(connection.targetQueryNodeKey())) {
                if (connection.isPostJoin()) {
                    postJoins.add(connection);
                } else {
                    Collection<IJoin> sourceConnList = conns.get(connection.sourceQueryNodeKey());
                    if (sourceConnList == null) {
                        sourceConnList = new ArrayList<IJoin>();
                        conns.put(connection.sourceQueryNodeKey(), sourceConnList);
                    }
                    sourceConnList.add(connection);
                    Collection<IJoin> targetConnList = conns.get(connection.targetQueryNodeKey());
                    if (targetConnList == null) {
                        targetConnList = new ArrayList<IJoin>();
                        conns.put(connection.targetQueryNodeKey(), targetConnList);
                    }
                    targetConnList.add(connection);
                    Edge edge = new Edge(connection);

                    QueryStateNode source = queryNodeMap.get(connection.sourceQueryNodeKey());
                    if (firstVertex == null) {
                        firstVertex = source;
                    }

                    QueryStateNode target = queryNodeMap.get(connection.targetQueryNodeKey());
                    //System.out.println("** adding edge of " + source.dataSourceName + " to " + target.dataSourceName);
                    reducedGraph.addEdge(source, target, edge);

                    if (connection instanceof CompositeFeedConnection) {
                        CompositeFeedConnection connection1 = (CompositeFeedConnection) connection;
                        if (connection1.getSourceCardinality() == 1) {
                            AnalysisDimension rowIDDimension = new AnalysisDimension();
                            rowIDDimension.setKey(new NamedKey(connection1.getTargetFeedID() + "RowID"));
                            rowIDDimension.setRowIDField(true);
                            if (connection1.getTargetFeedID() != null) {
                                insightRequestMetadata.getUniqueIteMap().put(new UniqueKey(connection1.getTargetFeedID(), UniqueKey.DERIVED), rowIDDimension);
                            } else {
                                insightRequestMetadata.getUniqueIteMap().put(new UniqueKey(connection1.getTargetReportID(), UniqueKey.REPORT), rowIDDimension);
                            }
                            target.addItem(rowIDDimension);
                        }
                        if (connection1.getTargetCardinality() == 1) {
                            AnalysisDimension rowIDDimension = new AnalysisDimension();
                            rowIDDimension.setKey(new NamedKey(connection1.getSourceFeedID() + "RowID"));
                            rowIDDimension.setRowIDField(true);
                            if (connection1.getSourceFeedID() != null) {
                                insightRequestMetadata.getUniqueIteMap().put(new UniqueKey(connection1.getSourceFeedID(), UniqueKey.DERIVED), rowIDDimension);
                            } else {
                                insightRequestMetadata.getUniqueIteMap().put(new UniqueKey(connection1.getSourceReportID(), UniqueKey.REPORT), rowIDDimension);
                            }
                            source.addItem(rowIDDimension);
                        }
                    }
                }
            }
        }

        // actually connecting the data sets...

        DataSet dataSet = null;

        List<ReportAuditEvent> auditStrings = new ArrayList<ReportAuditEvent>();

        Map<QueryNodeKey, QueryData> map = new HashMap<QueryNodeKey, QueryData>();

        for (QueryStateNode queryStateNode : neededNodes.values()) {
            map.put(queryStateNode.queryNodeKey(), queryStateNode.queryData);
        }

        int operations = 0;

        if (insightRequestMetadata.isTraverseAllJoins()) {
            Set<Edge> edges = reducedGraph.edgeSet();
            for (Edge last : edges) {
                QueryStateNode sourceNode = reducedGraph.getEdgeSource(last);
                QueryData sourceQueryData = map.get(sourceNode.queryNodeKey());
                QueryStateNode targetNode = reducedGraph.getEdgeTarget(last);
                if (sourceQueryData.dataSet == null) {
                    Collection<IJoin> myConnections = conns.get(sourceNode.queryNodeKey());
                    for (IJoin myConn : myConnections) {
                        FilterDefinition filter = filterMap.get(myConn);
                        if (filter != null) {
                            if (insightRequestMetadata.isOptimized() || myConn.isOptimized()) {
                                sourceNode.addFilter(filter);
                            }
                        }
                    }
                    sourceQueryData.dataSet = sourceNode.produceDataSet(insightRequestMetadata);
                    if (insightRequestMetadata.isOptimized()) {
                        for (IJoin myConn : myConnections) {
                            FilterDefinition filter = filterMap.get(myConn);
                            if (filter == null && myConn instanceof CompositeFeedConnection) {
                                CompositeFeedConnection c = (CompositeFeedConnection) myConn;
                                if (c.getForceOuterJoin() == 0) {
                                    QueryStateNode joinTargetNode;
                                    if (sourceNode.queryNodeKey().equals(myConn.sourceQueryNodeKey())) {
                                        joinTargetNode = queryNodeMap.get(myConn.targetQueryNodeKey());
                                    } else {
                                        joinTargetNode = queryNodeMap.get(myConn.sourceQueryNodeKey());
                                    }
                                    FilterDefinition joinFilter = createJoinFilter(sourceNode, sourceQueryData.dataSet, joinTargetNode, (CompositeFeedConnection) myConn);
                                    filterMap.put(myConn, joinFilter);
                                }
                            }
                        }
                    }
                }

                QueryData targetQueryData = map.get(targetNode.queryNodeKey());
                boolean swapped = false;
                if (!last.connection.sourceQueryNodeKey().equals(sourceNode.queryNodeKey())) {
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
                    CompositeFeedConnection c = (CompositeFeedConnection) last.connection;
                    if (c.getForceOuterJoin() == 0) {
                        joinFilter = createJoinFilter(sourceNode, sourceQueryData.dataSet, targetNode, (CompositeFeedConnection) last.connection);
                    }
                }
                if (!swapped) {
                    if (targetQueryData.dataSet == null) {
                        if (joinFilter != null) {
                            targetNode.addFilter(joinFilter);
                        }
                        targetQueryData.dataSet = targetNode.produceDataSet(insightRequestMetadata);
                    }
                } else {
                    if (sourceQueryData.dataSet == null) {
                        if (joinFilter != null) {
                            sourceNode.addFilter(joinFilter);
                        }
                        sourceQueryData.dataSet = sourceNode.produceDataSet(insightRequestMetadata);
                    }
                }

                /*if (sourceQueryData.dataSet == targetQueryData.dataSet) {
                    dataSet = sourceQueryData.dataSet;
                } else {*/
                if (sourceQueryData.dataSet == targetQueryData.dataSet) {

                }
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
                if (mergeAudit.getAuditEvents() != null) {
                    auditStrings.addAll(mergeAudit.getAuditEvents());
                }
                dataSet = mergeAudit.getDataSet();
                operations = mergeAudit.getOperations();

                //auditStrings.addAll(mergeAudit.getMergeStrings());
                sourceQueryData.dataSet = dataSet;
                sourceQueryData.neededItems.addAll(targetQueryData.neededItems);
                sourceQueryData.ids.addAll(targetQueryData.ids);
                for (QueryNodeKey id : sourceQueryData.ids) {
                    map.put(id, sourceQueryData);
                }
            }
        } else {
            ClosestFirstIterator<QueryStateNode, Edge> iter = new ClosestFirstIterator<QueryStateNode, Edge>(reducedGraph, firstVertex);
            while (iter.hasNext()) {
                QueryStateNode sourceNode = iter.next();
                QueryData sourceQueryData = map.get(sourceNode.queryNodeKey());
                if (sourceQueryData.dataSet == null) {
                    Collection<IJoin> myConnections = conns.get(sourceNode.queryNodeKey());
                    for (IJoin myConn : myConnections) {
                        FilterDefinition filter = filterMap.get(myConn);
                        if (filter != null) {
                            if (insightRequestMetadata.isOptimized()) {
                                sourceNode.addFilter(filter);
                            }
                        }
                    }
                    sourceQueryData.dataSet = sourceNode.produceDataSet(insightRequestMetadata);
                    if (insightRequestMetadata.isOptimized()) {
                        for (IJoin myConn : myConnections) {
                            FilterDefinition filter = filterMap.get(myConn);
                            if (filter == null && myConn instanceof CompositeFeedConnection) {
                                CompositeFeedConnection c = (CompositeFeedConnection) myConn;
                                if (c.getForceOuterJoin() == 0) {
                                    QueryStateNode targetNode;
                                    if (sourceNode.queryNodeKey().equals(myConn.sourceQueryNodeKey())) {
                                        targetNode = queryNodeMap.get(myConn.targetQueryNodeKey());
                                    } else {
                                        targetNode = queryNodeMap.get(myConn.sourceQueryNodeKey());
                                    }
                                    FilterDefinition joinFilter = createJoinFilter(sourceNode, sourceQueryData.dataSet, targetNode, (CompositeFeedConnection) myConn);
                                    filterMap.put(myConn, joinFilter);
                                }
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

                    QueryData targetQueryData = map.get(targetNode.queryNodeKey());
                    boolean swapped = false;
                    if (!last.connection.sourceQueryNodeKey().equals(sourceNode.queryNodeKey())) {
                        swapped = true;
                        QueryStateNode swap = sourceNode;
                        sourceNode = targetNode;
                        targetNode = swap;
                        QueryData swapData = sourceQueryData;
                        sourceQueryData = targetQueryData;
                        targetQueryData = swapData;
                    }
                    FilterValueDefinition joinFilter = null;
                    String string = null;
                    if (insightRequestMetadata.isOptimized() && last.connection instanceof CompositeFeedConnection) {
                        joinFilter = createJoinFilter(sourceNode, sourceQueryData.dataSet, targetNode, (CompositeFeedConnection) last.connection);
                        if (joinFilter != null) {
                            string = "Joining " + sourceNode.dataSourceName + " to " + targetNode.dataSourceName + " with filter containing " + joinFilter.getFilteredValues().size() + " values.";
                        }
                    }
                    if (string == null) {
                        string = "Joining " + sourceNode.dataSourceName + " to " + targetNode.dataSourceName + ".";
                    }

                    auditStrings.add(new ReportAuditEvent(ReportAuditEvent.JOIN_FILTER, string));

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
                    if (sourceQueryData.dataSet == targetQueryData.dataSet) {

                    }

                    MergeAudit mergeAudit = last.connection.merge(sourceQueryData.dataSet, targetQueryData.dataSet,
                            sourceQueryData.neededItems, targetQueryData.neededItems,
                            sourceNode.dataSourceName, targetNode.dataSourceName, conn, sourceNode.feedID, targetNode.feedID, operations);
                    if (mergeAudit.getAuditEvents() != null) {
                        auditStrings.addAll(mergeAudit.getAuditEvents());
                    }
                    operations = mergeAudit.getOperations();
                    dataSet = mergeAudit.getDataSet();
                    //auditStrings.addAll(mergeAudit.getMergeStrings());
                    sourceQueryData.dataSet = dataSet;
                    sourceQueryData.neededItems.addAll(targetQueryData.neededItems);
                    sourceQueryData.ids.addAll(targetQueryData.ids);
                    for (QueryNodeKey id : sourceQueryData.ids) {
                        map.put(id, sourceQueryData);
                    }

                }

            }
        }

        for (IJoin postJoin : postJoins) {
            QueryStateNode queryStateNode = queryNodeMap.get(postJoin.targetQueryNodeKey());
            DataSet targetSet = queryStateNode.produceDataSet(insightRequestMetadata);
            dataSet = postJoin.merge(dataSet, targetSet, null, queryStateNode.neededItems, null, queryStateNode.dataSourceName, conn, 0, queryStateNode.feedID, operations).getDataSet();
        }

        if (insightRequestMetadata.isLogReport()) {
            dataSet.getAudits().addAll(auditStrings);
        }
        Pipeline pipeline = insightRequestMetadata.findPipeline(getName());
        if (pipeline != null) {
            WSListDefinition analysisDefinition = new WSListDefinition();
            analysisDefinition.setFieldToUniqueMap(insightRequestMetadata.getFieldToUniqueMap());
            analysisDefinition.setColumns(new ArrayList<AnalysisItem>(analysisItems));
            pipeline.setup(analysisDefinition, insightRequestMetadata, conn);
            dataSet = pipeline.toDataSet(dataSet);
        } else if (getFeedType().getType() == FeedType.BASECAMP_MASTER.getType() && !insightRequestMetadata.isOptimized() && !insightRequestMetadata.isTraverseAllJoins()) {
            CompositeReportPipeline compositePipeline = new CompositeReportPipeline();
            WSListDefinition analysisDefinition = new WSListDefinition();
            List<AnalysisItem> additionalFields = new ArrayList<AnalysisItem>();
            Map<String, AnalysisItem> displayMap = new HashMap<String, AnalysisItem>();
            Map<String, AnalysisItem> keyMap = new HashMap<String, AnalysisItem>();
            for (AnalysisItem analysisItem : getFields()) {
                displayMap.put(analysisItem.toDisplay(), analysisItem);
                keyMap.put(analysisItem.getKey().toKeyString(), analysisItem);
            }
            for (AnalysisItem analysisItem : analysisItems) {
                if (displayMap.get(analysisItem.toDisplay()) == null || keyMap.get(analysisItem.getKey().toKeyString()) == null) {
                    additionalFields.add(analysisItem);
                }
            }
            analysisDefinition.setAddedItems(additionalFields);
            analysisDefinition.setColumns(new ArrayList<AnalysisItem>(analysisItems));
            compositePipeline.setup(analysisDefinition, this, insightRequestMetadata, conn);
            dataSet = compositePipeline.toDataSet(dataSet);
        }
        return dataSet;
    }



    private FilterValueDefinition createJoinFilter(QueryStateNode sourceNode, DataSet dataSet, QueryStateNode targetNode, CompositeFeedConnection connection) {
        FilterValueDefinition filterValueDefinition = new FilterValueDefinition();
        if (connection.getTargetJoin() == null) {
            if (connection.getTargetItem() == null || connection.getTargetItem().hasType(AnalysisItemTypes.DATE_DIMENSION)) {
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
        if (connection.getSourceJoin() == null && connection.getSourceItem() != null) {
            AnalysisItem analysisItem = findFieldForItem(sourceNode.neededItems, connection.getSourceItem());
            if (analysisItem != null) {
                filterValueDefinition.setFilteredValues(obtainValues(dataSet, analysisItem));
            } else {
                return null;
            }
        } else if (connection.getSourceJoin() != null) {
            AnalysisItem analysisItem = findFieldForKey(sourceNode.neededItems, connection.getSourceJoin(), sourceNode.feedID);
            if (analysisItem != null) {
                filterValueDefinition.setFilteredValues(obtainValues(dataSet, analysisItem));
            } else {
                return null;
            }
        } else {
            return null;
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

    protected boolean alwaysPassThrough(AnalysisItem analysisItem) {
        return false;
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
        Set<Long> validChildren = new HashSet<Long>();
        for (CompositeFeedNode node : compositeFeedNodes) {
            if (node.getRefreshBehavior() == DataSourceInfo.COMPOSITE_PULL || node.getRefreshBehavior() == DataSourceInfo.STORED_PULL) {
                validChildren.add(node.getDataFeedID());
            }
        }
        DataSourceInfo dataSourceInfo;
        if (validChildren.size() >= 1) {
            dataSourceInfo = super.createSourceInfo(conn);
            dataSourceInfo.setType(DataSourceInfo.COMPOSITE_PULL);
        } else {
            dataSourceInfo = super.createSourceInfo(conn);
        }
        return dataSourceInfo;
    }
}
