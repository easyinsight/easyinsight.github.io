package com.easyinsight.core;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import nu.xom.Node;
import nu.xom.Nodes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: 5/17/12
 * Time: 10:52 AM
 */
public class XMLImportMetadata {
    private EIConnection conn;
    private FeedDefinition dataSource;
    private List<AnalysisItem> additionalReportItems = new ArrayList<AnalysisItem>();
    private Map<String, AnalysisItem> unknownMappings = new HashMap<String, AnalysisItem>();
    private Set<String> unknownFields = new HashSet<String>();

    public String getValue(Node node, String query) {
        Nodes nodes = node.query(query);
        if (nodes.size() == 0) {
            return "";
        }
        return nodes.get(0).getValue();
    }

    public List<AnalysisItem> getAdditionalReportItems() {
        return additionalReportItems;
    }

    public void setAdditionalReportItems(List<AnalysisItem> additionalReportItems) {
        this.additionalReportItems = additionalReportItems;
    }

    public EIConnection getConn() {
        return conn;
    }

    public void setConn(EIConnection conn) {
        this.conn = conn;
    }

    public FeedDefinition getDataSource() {
        return dataSource;
    }

    public void setDataSource(FeedDefinition dataSource) {
        this.dataSource = dataSource;
    }

    public void addUnknownField(String field) {
        unknownFields.add(field);
    }

    public Set<String> getUnknownFields() {
        return unknownFields;
    }

    public FeedDefinition dataSourceForURLKey(String urlKey) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT DATA_FEED_ID FROM DATA_FEED WHERE API_KEY = ?");
            stmt.setString(1, urlKey);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            long id = rs.getLong(1);
            stmt.close();
            return new FeedStorage().getFeedDefinitionData(id, conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
