package com.easyinsight.datafeeds.treasuredata;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.users.Account;
import com.treasure_data.auth.TreasureDataCredentials;
import com.treasure_data.client.TreasureDataClient;
import com.treasure_data.model.Database;
import com.treasure_data.model.Job;
import com.treasure_data.model.JobResult;
import com.treasure_data.model.JobSummary;
import org.jetbrains.annotations.NotNull;
import org.msgpack.type.ArrayValue;
import org.msgpack.type.Value;
import org.msgpack.type.ValueType;
import org.msgpack.unpacker.Unpacker;
import org.msgpack.unpacker.UnpackerIterator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: 8/29/12
 * Time: 8:43 AM
 */
public class TreasureDataQuerySource extends ServerDataSourceDefinition {

    private String databaseID;
    private String tdApiKey;
    private String query;

    public String getDatabaseID() {
        return databaseID;
    }

    public void setDatabaseID(String databaseID) {
        this.databaseID = databaseID;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getTdApiKey() {
        return tdApiKey;
    }

    public void setTdApiKey(String tdApiKey) {
        this.tdApiKey = tdApiKey;
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM treasure_data_table WHERE DATA_SOURCE_ID = ?");
        deleteStmt.setLong(1, getDataFeedID());
        deleteStmt.executeUpdate();
        deleteStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO treasure_data_table (DATA_SOURCE_ID, DATABASE_ID, API_KEY, QUERY) VALUES (?, ?, ?, ?)");
        insertStmt.setLong(1, getDataFeedID());
        insertStmt.setString(2, databaseID);
        insertStmt.setString(3, tdApiKey);
        insertStmt.setString(4, query);
        insertStmt.execute();
        insertStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT database_id, api_key, query from treasure_data_table WHERE DATA_SOURCE_ID = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            setDatabaseID(rs.getString(1));
            setTdApiKey(rs.getString(2));
            setQuery(rs.getString(3));
        }
        queryStmt.close();
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return new ArrayList<String>();
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.TREASURE_DATA;
    }

    private transient DataSet cached;

    @Override
    public String validateCredentials() {
        return null;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.PLUS;
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.STORED_PULL;
    }

    protected void beforeRefresh(EIConnection conn) {
        if (getDataFeedID() != 0) {
            List<AnalysisItem> fields = new ArrayList<AnalysisItem>();
            conn.setAutoCommit(true);
            Map<String, Key> keys = newDataSourceFields(null);
            Map<String, AnalysisItem> map = new HashMap<String, AnalysisItem>();
            for (AnalysisItem analysisItem : getFields()) {
                fields.add(analysisItem);
                map.put(analysisItem.getKey().toKeyString(), analysisItem);
            }
            try {
                if (tdApiKey != null && !"".equals(tdApiKey) && databaseID != null && !"".equals(databaseID) && query != null &&
                        !"".equals(query)) {
                    List<String> fieldNames = fieldNames(query);
                    TreasureDataClient client = new TreasureDataClient();
                    client.setTreasureDataCredentials(new TreasureDataCredentials(tdApiKey));
                    Job job = new Job(new Database(databaseID), query);
                    client.submitJob(job);
                    String jobID = job.getJobID();
                    boolean jobFinished = false;
                    while (!jobFinished) {
                        JobSummary jobSummary = client.showJob(job);
                        System.out.println(jobSummary.getStatus());
                        if (JobSummary.Status.SUCCESS.equals(jobSummary.getStatus())) {
                            break;
                        }
                        Thread.sleep(5000);
                    }
                    JobResult jobResult = client.getJobResult(job);
                    Unpacker unpacker = jobResult.getResult();
                    UnpackerIterator iter = unpacker.iterator();
                    DataSet dataSet = new DataSet();

                    while (iter.hasNext()) {
                        IRow row = dataSet.createRow();
                        ArrayValue arrayValue = (ArrayValue) iter.next();
                        for (int i = 0; i < arrayValue.size(); i++) {
                            String field = fieldNames.get(i);
                            AnalysisItem analysisItem = map.get(field);
                            Value value = arrayValue.get(i);
                            if (analysisItem == null) {
                                Key key = keys.get(field);
                                if (key == null) {
                                    key = new NamedKey(field);
                                    keys.put(field, key);
                                }

                                if (value.getType().equals(ValueType.FLOAT) || arrayValue.getType().equals(ValueType.INTEGER)) {
                                    analysisItem = new AnalysisMeasure(key, AggregationTypes.SUM);
                                } else {
                                    analysisItem = new AnalysisDimension(key);
                                }
                                map.put(field, analysisItem);
                                fields.add(analysisItem);
                            }
                            String string;
                            if (value.getType().equals(ValueType.RAW)) {
                                string = value.asRawValue().getString();
                            } else {
                                string = value.toString();
                            }
                            row.addValue(analysisItem.getKey(), string);
                        }
                    }
                    cached = dataSet;
                }

            } catch (Exception e) {
                throw new ReportException(new DataSourceConnectivityReportFault(e.getMessage(), this));
            }
            cacheFields = fields;
            conn.setAutoCommit(false);
        }
    }

    private transient List<AnalysisItem> cacheFields = new ArrayList<AnalysisItem>();

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> fields = new ArrayList<AnalysisItem>();

        if (getDataFeedID() == 0) {
            return fields;
        }

        return cacheFields;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = cached;
        cached = null;
        return dataSet;
    }

    private List<String> fieldNames(String query) {
        List<String> fieldNames = new ArrayList<String>();
        query = query.substring(7);
        query = query.substring(0, query.indexOf("FROM"));
        String[] parts = query.split(",");
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part.contains(" as ")) {
                part = part.substring(part.indexOf(" as ") + 4);
                fieldNames.add(part.trim());
            } else {
                fieldNames.add(part.trim());
            }
        }
        return fieldNames;
    }
}
