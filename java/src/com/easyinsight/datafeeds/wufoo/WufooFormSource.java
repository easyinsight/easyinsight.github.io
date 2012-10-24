package com.easyinsight.datafeeds.wufoo;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 10/22/12
 * Time: 5:01 PM
 */
public class WufooFormSource extends WufooBaseSource {
    private String formID;

    public String getFormID() {
        return formID;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return new ArrayList<String>();
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        try {
            List<AnalysisItem> items = new ArrayList<AnalysisItem>();
            WufooCompositeSource wufooCompositeSource = (WufooCompositeSource) parentDefinition;
            HttpClient httpClient = getHttpClient(wufooCompositeSource.getWfApiKey(), "x");
            Document doc = runRestRequest("/api/v3/forms/" + formID + "/fields.xml", httpClient, wufooCompositeSource);
            Nodes fields = doc.query("/Fields/Field");
            for (int i = 0; i < fields.size(); i++) {
                Node fieldNode = fields.get(i);
                String title = fieldNode.query("Title/text()").get(0).getValue();
                String type = fieldNode.query("Type/text()").get(0).getValue();
                String id = fieldNode.query("ID/text()").get(0).getValue();
                Nodes subFieldNodes = fieldNode.query("SubFields/Subfield");
                if (subFieldNodes.size() == 0) {
                    Key key = keys.get(id);
                    if (key == null) {
                        key = new NamedKey(id);
                    }
                    if ("date".equals(type)) {
                        items.add(new AnalysisDateDimension(key, title, AnalysisDateDimension.DAY_LEVEL));
                    } else if ("number".equals(type)) {
                        items.add(new AnalysisMeasure(key, AggregationTypes.SUM));
                    } else {
                        items.add(new AnalysisDimension(key, title));
                    }
                } else {
                    for (int j = 0; j < subFieldNodes.size(); j++) {
                        Node subFieldNode = subFieldNodes.get(j);
                        String subFieldID = subFieldNode.query("ID/text()").get(0).getValue();
                        String label = subFieldNode.query("Label/text()").get(0).getValue();
                        Key key = keys.get(subFieldID);
                        if (key == null) {
                            key = new NamedKey(subFieldID);
                        }
                        items.add(new AnalysisDimension(key, label));
                    }
                }
            }
            return items;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            DataSet dataSet = new DataSet();
            WufooCompositeSource wufooCompositeSource = (WufooCompositeSource) parentDefinition;
            HttpClient httpClient = getHttpClient(wufooCompositeSource.getWfApiKey(), "x");
            int count;
            int page = 0;
            do {
                count = 0;
                Document doc;
                if (page == 0) {
                    doc = runRestRequest("/api/v3/forms/" + formID + "/entries.xml", httpClient, wufooCompositeSource);
                } else {
                    doc = runRestRequest("/api/v3/forms/" + formID + "/entries.xml?page=" + page, httpClient, wufooCompositeSource);
                }
                Nodes entryNodes = doc.query("/Entries/Entry");
                for (int i = 0; i < entryNodes.size(); i++) {
                    Node entryNode = entryNodes.get(i);
                    IRow row = dataSet.createRow();
                    for (AnalysisItem analysisItem : getFields()) {
                        System.out.println(analysisItem.getKey().toKeyString() + "/text()");
                        Nodes vals = entryNode.query(analysisItem.getKey().toKeyString() + "/text()");
                        if (vals.size() == 1) {
                            row.addValue(analysisItem.getKey(), vals.get(0).getValue());
                        }
                    }
                    count++;
                }
                page++;
            } while (count == 100);
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.WUFOO_FORM;
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM WUFOO_FORM_SOURCE WHERE DATA_SOURCE_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        clearStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO WUFOO_FORM_SOURCE (FORM_ID, DATA_SOURCE_ID) VALUES (?, ?)");
        insertStmt.setString(1, formID);
        insertStmt.setLong(2, getDataFeedID());
        insertStmt.execute();
        insertStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement getStmt = conn.prepareStatement("SELECT FORM_ID FROM WUFOO_FORM_SOURCE WHERE DATA_SOURCE_ID = ?");
        getStmt.setLong(1, getDataFeedID());
        ResultSet rs = getStmt.executeQuery();
        if (rs.next()) {
            formID = rs.getString(1);
        }
        getStmt.close();
    }
}
