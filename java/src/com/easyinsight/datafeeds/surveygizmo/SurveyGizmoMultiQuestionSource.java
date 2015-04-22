package com.easyinsight.datafeeds.surveygizmo;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.core.StringValue;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.*;


/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 2/20/14
 * Time: 3:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class SurveyGizmoMultiQuestionSource extends SurveyGizmoBaseSource {

    private String parentQuestionID;
    private Map<String, String> qIDs;

    public String getParentQuestionID() {
        return parentQuestionID;
    }

    public void setParentQuestionID(String parentQuestionID) {
        this.parentQuestionID = parentQuestionID;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return new ArrayList<String>();
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.SURVEYGIZMO_MULTIPLE;
    }

    public void setqIDs(Map<String, String> qIDs) {
        this.qIDs = qIDs;
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(getFeedName() + " Row ID", new AnalysisDimension());
        List<AnalysisItem> queuedFields = getQueuedFields();
        for (AnalysisItem item : queuedFields) {
            NamedKey namedKey = (NamedKey) item.getKey();
            fieldBuilder.addField(namedKey.getName(), item);
        }
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM survey_gizmo_multi_source WHERE DATA_SOURCE_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        clearStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO survey_gizmo_multi_source (multi_question_id, DATA_SOURCE_ID) VALUES (?, ?)");
        insertStmt.setString(1, parentQuestionID);
        insertStmt.setLong(2, getDataFeedID());
        insertStmt.execute();
        insertStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement getStmt = conn.prepareStatement("SELECT multi_question_id FROM survey_gizmo_multi_source WHERE DATA_SOURCE_ID = ?");
        getStmt.setLong(1, getDataFeedID());
        ResultSet rs = getStmt.executeQuery();
        if (rs.next()) {
            parentQuestionID = rs.getString(1);
        }
        getStmt.close();
    }

    private transient List<AnalysisItem> queuedFields = new ArrayList<>();

    public List<AnalysisItem> getQueuedFields() {
        if (queuedFields == null) {
            queuedFields = new ArrayList<>();
        }
        return queuedFields;
    }

    private int tableType;

    public void setTableType(int tableType) {
        this.tableType = tableType;
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {

        DataSet ds = new DataSet();

        SurveyGizmoCompositeSource surveyGizmoCompositeSource = (SurveyGizmoCompositeSource) parentDefinition;

        List<Map<String, Object>> results = surveyGizmoCompositeSource.getResults();

        Map<String, Key> keyByName = new HashMap<>();
        if (tableType == 2) {
            for (AnalysisItem field : getFields()) {
                keyByName.put(field.toUnqualifiedDisplay(), field.getKey());
            }
        }

        for (Map<String, Object> result : results) {


            if (qIDs != null) {
                for (Map.Entry<String, String> qIDEntry : qIDs.entrySet()) {
                    IRow row = ds.createRow();
                    row.addValue(keys.get(getFeedName() + " Row ID"), result.get("row_id").toString());
                    row.addValue(keys.get("Category"), qIDEntry.getValue());
                    for (Map.Entry<String, Key> keyEntry : keys.entrySet()) {
                        if (tableType == 1) {
                            if (keyEntry.getKey().contains(":")) {
                                String endKey = keyEntry.getKey().split(":")[1];
                                String concat = qIDEntry.getKey() + ":" + endKey;
                                Object concatValue = result.get(concat);
                                if (concatValue != null) {
                                    row.addValue(keyEntry.getKey(), concatValue.toString());
                                }
                            }
                        } else if (tableType == 2) {
                            if (keyEntry.getKey().contains(":")) {
                                String endKey = keyEntry.getKey().split(":")[0];
                                //String concat = qIDEntry.getKey() + ":" + endKey;
                                Object concatValue = result.get(qIDEntry.getKey());
                                if (concatValue != null) {
                                    Key validKey = keyByName.get(concatValue.toString());
                                    if (validKey != null && validKey == keyEntry.getValue()) {
                                        row.addValue(keyEntry.getKey(), new StringValue("1"));
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (tableType == 3) {
                IRow row = ds.createRow();
                row.addValue(keys.get(getFeedName() + " Row ID"), result.get("row_id").toString());
                for (Map.Entry<String, Key> keyEntry : keys.entrySet()) {
                    Object val = result.get(keyEntry.getKey());

                    if (val != null) {
                        row.addValue(keyEntry.getValue(), 1);
                    }
                    //row.addValue(keyEntry.getValue(), );
                }
            } else {
                IRow row = ds.createRow();
                row.addValue(keys.get(getFeedName() + " Row ID"), result.get("row_id").toString());
                for (Map.Entry<String, Key> keyEntry : keys.entrySet()) {
                    Object val = result.get(keyEntry.getKey());

                    if (val != null) {
                        row.addValue(keyEntry.getValue(), val.toString());
                    }
                    //row.addValue(keyEntry.getValue(), );
                }
            }
        }

        return ds;
    }

}
