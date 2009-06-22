package com.easyinsight.datafeeds;

import com.easyinsight.core.Key;
import com.easyinsight.core.DerivedKey;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.logging.LogClass;
import com.easyinsight.database.Database;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.Roles;
import com.easyinsight.userupload.UserUploadInternalService;

import java.sql.*;
import java.util.*;

import org.hibernate.Session;

/**
 * User: James Boe
 * Date: May 31, 2008
 * Time: 11:55:05 PM
 */
public class CompositeFeedDefinition extends FeedDefinition {
    private List<CompositeFeedNode> compositeFeedNodes = new ArrayList<CompositeFeedNode>();
    private List<CompositeFeedConnection> connections = new ArrayList<CompositeFeedConnection>();

    public List<CompositeFeedNode> getCompositeFeedNodes() {
        return compositeFeedNodes;
    }

    public void setCompositeFeedNodes(List<CompositeFeedNode> compositeFeedNodes) {
        this.compositeFeedNodes = compositeFeedNodes;
    }

    public List<CompositeFeedConnection> getConnections() {
        return connections;
    }

    public void setConnections(List<CompositeFeedConnection> connections) {
        this.connections = connections;
    }

    public FeedType getFeedType() {
        return FeedType.COMPOSITE;
    }

    public void customStorage(Connection conn) throws SQLException {
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM COMPOSITE_FEED WHERE DATA_FEED_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        PreparedStatement nodeStmt = conn.prepareStatement("INSERT INTO COMPOSITE_FEED (DATA_FEED_ID) " +
                "VALUES (?)", Statement.RETURN_GENERATED_KEYS);
        nodeStmt.setLong(1, getDataFeedID());
        nodeStmt.execute();
        long compositeFeedID = Database.instance().getAutoGenKey(nodeStmt);
        nodeStmt.close();
        for (CompositeFeedNode node : compositeFeedNodes) {
            node.store(conn, compositeFeedID);
        }
        for (CompositeFeedConnection connection : connections) {
            connection.store(conn, compositeFeedID);
        }
    }

    public void customLoad(Connection conn) throws SQLException {
        PreparedStatement getCustomFeedIDStmt = conn.prepareStatement("SELECT COMPOSITE_FEED_ID FROM COMPOSITE_FEED WHERE " +
                "DATA_FEED_ID = ?");
        getCustomFeedIDStmt.setLong(1, getDataFeedID());
        ResultSet rs = getCustomFeedIDStmt.executeQuery();
        rs.next();
        long compositeFeedID = rs.getLong(1);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_FEED_ID FROM COMPOSITE_NODE WHERE COMPOSITE_FEED_ID = ?");
        queryStmt.setLong(1, compositeFeedID);
        ResultSet nodeRS = queryStmt.executeQuery();
        List<CompositeFeedNode> nodes = new ArrayList<CompositeFeedNode>();
        while (nodeRS.next()) {
            long feedID = nodeRS.getLong(1);
            nodes.add(new CompositeFeedNode(feedID));
        }
        PreparedStatement queryConnStmt = conn.prepareStatement("SELECT SOURCE_FEED_NODE_ID, TARGET_FEED_NODE_ID," +
                "SOURCE_JOIN, TARGET_JOIN FROM COMPOSITE_CONNECTION WHERE COMPOSITE_FEED_ID = ?");
        queryConnStmt.setLong(1, compositeFeedID);
        List<CompositeFeedConnection> edges = new ArrayList<CompositeFeedConnection>();
        ResultSet connectionRS = queryConnStmt.executeQuery();
        while (connectionRS.next()) {
            long sourceID = connectionRS.getLong(1);
            long targetID = connectionRS.getLong(2);
            Key sourceKey = getKey(conn, connectionRS.getLong(3));
            Key targetKey = getKey(conn, connectionRS.getLong(4));
            edges.add(new CompositeFeedConnection(sourceID, targetID, sourceKey, targetKey));
        }
        this.compositeFeedNodes = nodes;
        this.connections = edges;
        queryStmt.close();
    }

    public Feed createFeedObject() {
        return new CompositeFeed(compositeFeedNodes, connections);
    }
    
    private Key getKey(Connection conn, long targetJoinID) {
        Session session = Database.instance().createSession(conn);
        List results = session.createQuery("from Key where keyID = ?").setLong(0, targetJoinID).list();
        return (Key) results.get(0);
    }

    public void populateFields(Connection conn) {
        // get fields from the composite feed nodes...
        try {
            AnalysisItemVisitor analysisItemVisitor = new AnalysisItemVisitor(conn);
            analysisItemVisitor.visit(this);
            Map<String, AnalysisItem> keyMap = new HashMap<String, AnalysisItem>();

            Map<String, List<AnalysisItem>> duplicateNameMap = new HashMap<String, List<AnalysisItem>>();
            for (AnalysisItem analysisItem : analysisItemVisitor.derivedKeys) {
                String displayName = analysisItem.getDisplayName() != null ? analysisItem.getDisplayName() : analysisItem.getKey().toKeyString();
                AnalysisItem existing = keyMap.get(displayName);
                if (existing == null) {
                    keyMap.put(displayName, analysisItem);
                } else {
                    List<AnalysisItem> analysisItems = duplicateNameMap.get(displayName);
                    if (analysisItems == null) {
                        analysisItems = new ArrayList<AnalysisItem>();
                        duplicateNameMap.put(displayName, analysisItems);
                        analysisItems.add(existing);
                    }
                    analysisItems.add(analysisItem);
                }
            }
            for (Map.Entry<String, List<AnalysisItem>> entry : duplicateNameMap.entrySet()) {
                keyMap.remove(entry.getKey());
                for (AnalysisItem analysisItem : entry.getValue()) {
                    DerivedKey derivedKey = (DerivedKey) analysisItem.getKey();
                    String name = getCompositeFeedName(derivedKey.getFeedID(), conn);
                    analysisItem.setDisplayName(name + " - " + entry.getKey());
                    keyMap.put(name, analysisItem);
                }
            }

            setFields(new ArrayList<AnalysisItem>(keyMap.values()));
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private String getCompositeFeedName(long feedID, Connection conn) {
        try {
            PreparedStatement nameStmt = conn.prepareStatement("SELECT FEED_NAME FROM DATA_FEED WHERE DATA_FEED_ID = ?");
            nameStmt.setLong(1, feedID);
            ResultSet rs = nameStmt.executeQuery();
            rs.next();
            return rs.getString(1);
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private class AnalysisItemVisitor extends CompositeFeedNodeVisitor {

        private List<AnalysisItem> derivedKeys = new ArrayList<AnalysisItem>();
        Map<Long, AnalysisItem> replacementMap = new HashMap<Long, AnalysisItem>();
        private Connection conn;

        private AnalysisItemVisitor(Connection conn) {
            this.conn = conn;
        }

        protected void accept(CompositeFeedNode compositeFeedNode) throws SQLException {
            List<AnalysisItem> analysisItemList = retrieveFields(compositeFeedNode.getDataFeedID(), conn);
            for (AnalysisItem analysisItem : analysisItemList) {
                AnalysisItem clonedItem;
                try {
                    clonedItem = analysisItem.clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
                Key key = analysisItem.getKey();
                DerivedKey derivedKey = new DerivedKey();
                derivedKey.setFeedID(compositeFeedNode.getDataFeedID());
                derivedKey.setParentKey(key);
                clonedItem.setKey(derivedKey);
                clonedItem.setAnalysisItemID(0);
                derivedKeys.add(clonedItem);
                replacementMap.put(analysisItem.getAnalysisItemID(), clonedItem);
            }
            for (Map.Entry<Long, AnalysisItem> replEntry : replacementMap.entrySet()) {
                replEntry.getValue().updateIDs(replacementMap);
            }
        }
    }

    private List<AnalysisItem> retrieveFields(long feedID, Connection conn) throws SQLException {
        return new FeedStorage().retrieveFields(feedID, conn);
    }

    @Override
    public FeedDefinition clone(Connection conn) throws CloneNotSupportedException, SQLException {
        CompositeFeedDefinition feedDefinition = (CompositeFeedDefinition) super.clone(conn);
        List<CompositeFeedNode> children = feedDefinition.getCompositeFeedNodes();
        Map<Long, FeedDefinition> replacementMap = new HashMap<Long, FeedDefinition>();
        for (CompositeFeedNode child : children) {
            FeedDefinition childDefinition = new FeedStorage().getFeedDefinitionData(child.getDataFeedID(), conn);
            FeedDefinition clonedDefinition = DataSourceCopyUtils.cloneFeed(SecurityUtil.getUserID(), conn, childDefinition);
            DataSourceCopyUtils.buildClonedDataStores(false, feedDefinition, clonedDefinition, conn);
            new UserUploadInternalService().createUserFeedLink(SecurityUtil.getUserID(), clonedDefinition.getDataFeedID(), Roles.OWNER, conn);
            replacementMap.put(child.getDataFeedID(), clonedDefinition);
            child.setDataFeedID(clonedDefinition.getDataFeedID());
        }
        for (CompositeFeedConnection connection : feedDefinition.getConnections()) {
            connection.setSourceJoin(replacementMap.get(connection.getSourceFeedID()).getField(connection.getSourceJoin().toKeyString()));
            connection.setTargetJoin(replacementMap.get(connection.getTargetFeedID()).getField(connection.getTargetJoin().toKeyString()));
            connection.setSourceFeedID(replacementMap.get(connection.getSourceFeedID()).getDataFeedID());
            connection.setTargetFeedID(replacementMap.get(connection.getTargetFeedID()).getDataFeedID());
        }
        feedDefinition.populateFields(conn);
        return feedDefinition;
    }

    public void postClone(Connection conn) throws SQLException {
        FeedStorage feedStorage = new FeedStorage();
        for (CompositeFeedNode child : getCompositeFeedNodes()) {
            FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(child.getDataFeedID(), conn);
            feedDefinition.setParentSourceID(getDataFeedID());
            feedStorage.updateDataFeedConfiguration(feedDefinition, conn);
        }
    }

    @Override
    public void delete(Connection conn) throws SQLException {
        super.delete(conn);
        for (CompositeFeedNode node : getCompositeFeedNodes()) {
            FeedDefinition feedDefinition = new FeedStorage().getFeedDefinitionData(node.getDataFeedID(), conn);
            if (feedDefinition != null && !feedDefinition.isVisible()) {
                feedDefinition.delete(conn);
            }
        }
    }

    @Override
    protected void onDelete(Connection conn) throws SQLException {

    }
}
