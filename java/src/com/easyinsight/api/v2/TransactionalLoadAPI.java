package com.easyinsight.api.v2;

import com.easyinsight.analysis.*;
import com.easyinsight.api.*;
import com.easyinsight.api.Row;
import com.easyinsight.core.*;
import com.easyinsight.core.StringValue;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.util.RandomTextGenerator;

import javax.jws.WebParam;
import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * User: jamesboe
 * Date: Jun 21, 2010
 * Time: 12:13:29 PM
 */
public abstract class TransactionalLoadAPI implements ITransactionalLoadAPI {

    private static final int TRANSACTION_OPENED = 1;
    private static final int TRANSACTION_FAILED = 2;
    private static final int TRANSACTION_COMMITTED = 3;
    private static final int TRANSACTION_ROLLED_BACK = 4;

    protected abstract long getUserID();

    protected abstract long getAccountID();

    public boolean validateCredentials() {
        return true;
    }

    private void saveLoad(SerializedLoad load, EIConnection conn, long transactionDatabaseID) throws SQLException, IOException {
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO data_transaction_command (data_transaction_id, command_blob) VALUES (?, ?)");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(load);
        byte[] bytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        BufferedInputStream bis = new BufferedInputStream(bais, 1024);
        insertStmt.setLong(1, transactionDatabaseID);
        insertStmt.setBinaryStream(2, bis, bytes.length);
        insertStmt.execute();
    }

    private List<Row> loadRows(long transactionID, EIConnection conn) throws SQLException, IOException, ClassNotFoundException {
        List<Row> rows = new ArrayList<Row>();
        PreparedStatement queryStmt = conn.prepareStatement("SELECT COMMAND_BLOB FROM DATA_TRANSACTION_COMMAND WHERE DATA_TRANSACTION_ID = ?", ResultSet.TYPE_FORWARD_ONLY);
        queryStmt.setLong(1, transactionID);
        ResultSet blobRS = queryStmt.executeQuery();
        while (blobRS.next()) {
            byte[] bytes = blobRS.getBytes(1);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            SerializedLoad load = (SerializedLoad) ois.readObject();
            rows.addAll(load.toRows());
        }
        return rows;
    }

    private long identifyTransactionID(String transactionID, EIConnection conn) throws SQLException {
        PreparedStatement txnQueryStmt = conn.prepareStatement("SELECT data_transaction_id FROM data_transaction where external_txn_id = ? AND user_id = ?");
        txnQueryStmt.setString(1, transactionID);
        txnQueryStmt.setLong(2, getUserID());
        ResultSet rs = txnQueryStmt.executeQuery();
        rs.next();
        return rs.getLong(1);
    }

    public String beginTransaction(@WebParam(name="dataSourceName") String dataSourceName,
                                   @WebParam(name="transactionOperation") boolean replaceData,
                                   @WebParam(name = "changeDataSourceToMatch") boolean changeDataSourceToMatch) {
        long userID = getUserID();
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            String txnString = RandomTextGenerator.generateText(15);
            PreparedStatement insertTxnStmt = conn.prepareStatement("INSERT INTO DATA_TRANSACTION (USER_ID, external_txn_id, txn_date, txn_status, data_source_name," +
                    "replace_data, change_data_source_to_match) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)");
            insertTxnStmt.setLong(1, userID);
            insertTxnStmt.setString(2, txnString);
            insertTxnStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            insertTxnStmt.setInt(4, TRANSACTION_OPENED);
            insertTxnStmt.setString(5, dataSourceName);
            insertTxnStmt.setBoolean(6, replaceData);
            insertTxnStmt.setBoolean(7, changeDataSourceToMatch);
            insertTxnStmt.execute();
            conn.commit();
            return txnString;
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new ServiceRuntimeException(e.getMessage());
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public CommitResult commit(@WebParam(name = "transactionID") String transactionID) {
        EIConnection conn = Database.instance().getConnection();
        DataStorage dataStorage = null;
        CommitResult commitResult = new CommitResult();
        try {
            conn.setAutoCommit(false);
            PreparedStatement txnQueryStmt = conn.prepareStatement("SELECT data_transaction_id, data_source_name, replace_data, change_data_source_to_match " +
                    "FROM data_transaction where external_txn_id = ? AND user_id = ?");
            txnQueryStmt.setString(1, transactionID);
            txnQueryStmt.setLong(2, getUserID());
            ResultSet rs = txnQueryStmt.executeQuery();
            rs.next();
            long transactionDatabaseID = rs.getLong(1);
            String dataSourceName = rs.getString(2);
            boolean replaceData = rs.getBoolean(3);
            boolean changeDataSourceToMatch = rs.getBoolean(4);
            List<Row> rows = loadRows(transactionDatabaseID, conn);
            CallData callData = convertData(dataSourceName, rows, conn, changeDataSourceToMatch);
            dataStorage = callData.dataStorage;
            if (replaceData) dataStorage.truncate();
            dataStorage.insertData(callData.dataSet);
            dataStorage.commit();
            conn.commit();

            commitResult.setSuccessful(true);
            commitResult.setFailedRows(new RowStatus[] {});
            //commitResult.setDataSourceAPIKey();
            //commitResult.setDataSourceURL();
            return commitResult;
        } catch (ServiceRuntimeException sre) {
            LogClass.debug(sre.getMessage());
            if (dataStorage != null) {
                dataStorage.rollback();
            }
            conn.rollback();
            commitResult.setSuccessful(false);
            commitResult.setFailureMessage(sre.getMessage());
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            if (dataStorage != null) {
                dataStorage.rollback();
            }
            commitResult.setSuccessful(false);
            commitResult.setFailureMessage(e.getMessage());
        } finally {
            conn.setAutoCommit(true);
            if (dataStorage != null) {
                dataStorage.closeConnection();
            }
            Database.closeConnection(conn);
        }
        return commitResult;
    }

    public void rollback(@WebParam(name = "transactionID") String transactionID) {
        throw new UnsupportedOperationException();
    }

    public void loadRows(@WebParam(name = "rows") Row[] rows,
                        @WebParam(name="transactionID") String transactionID) {
        EIConnection conn = Database.instance().getConnection();
        if (rows == null) {
            throw new ServiceRuntimeException("You must specify at least one Row.");
        }
        try {
            conn.setAutoCommit(false);
            long transactionDatabaseID = identifyTransactionID(transactionID, conn);
            SerializedLoad load = SerializedLoad.fromRows(Arrays.asList(rows));
            saveLoad(load, conn, transactionDatabaseID);
            conn.commit();
        } catch (ServiceRuntimeException sre) {
            LogClass.debug(sre.getMessage());
            conn.rollback();
            throw sre;
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new ServiceRuntimeException("An internal error occurred on attempting to process the provided data. The error has been logged for our engineers to examine.");
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    private static class CallData {
        DataStorage dataStorage;
        DataSet dataSet;

        private CallData(DataStorage dataStorage, DataSet dataSet) {
            this.dataStorage = dataStorage;
            this.dataSet = dataSet;
        }
    }

    private CallData convertData(String dataSourceName, Row row, EIConnection conn, boolean updateIfNecessary) throws SQLException {
        if (row == null) {
            throw new ServiceRuntimeException("You must include at least one row for this operation.");
        }
        return convertData(dataSourceName, Arrays.asList(row), conn, updateIfNecessary);
    }

    private CallData convertData(String dataSourceName, Row[] rows, EIConnection conn, boolean updateIfNecessary) throws SQLException {
        if (rows == null) {
            throw new ServiceRuntimeException("You must include at least one row for this operation.");
        }
        return convertData(dataSourceName, Arrays.asList(rows), conn, updateIfNecessary);
    }

    private CallData convertData(String dataSourceName, List<Row> rows, EIConnection conn, boolean updateIfNecessary) throws SQLException {
        if (dataSourceName == null) {
            throw new ServiceRuntimeException("You must specify a data source name or API key.");
        }
        if (rows == null || rows.size() == 0) {
            throw new ServiceRuntimeException("You must include at least one row for this operation.");
        }
        DataStorage dataStorage;
        DataSet dataSet;
        Map<Long, Boolean> dataSourceIDs = findDataSourceIDsByName(dataSourceName, conn);
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        Row firstRow = rows.get(0);
        StringPair[] stringPairs = firstRow.getStringPairs();
        if (stringPairs != null) {
            for (StringPair stringPair : stringPairs) {
                analysisItems.add(new AnalysisDimension(stringPair.getKey(), true));
            }
        }
        NumberPair[] numberPairs = firstRow.getNumberPairs();
        if (numberPairs != null) {
            for (NumberPair numberPair : numberPairs) {
                analysisItems.add(new AnalysisMeasure(numberPair.getKey(), AggregationTypes.SUM));
            }
        }
        DatePair[] datePairs = firstRow.getDatePairs();
        if (datePairs != null) {
            for (DatePair datePair : datePairs) {
                analysisItems.add(new AnalysisDateDimension(datePair.getKey(), true, AnalysisDateDimension.DAY_LEVEL));
            }
        }
        if (updateIfNecessary) {
            if (dataSourceIDs.size() == 0) {
                long userID = getUserID();
                // create new data source
                FeedDefinition feedDefinition = new FeedDefinition();
                if (dataSourceName.length() < 3) {
                    throw new ServiceRuntimeException("The data source name must be at least three characters.");
                }
                if (dataSourceName.length() > 30) {
                    throw new ServiceRuntimeException("The data source name must be less than thirty characters.");
                }
                feedDefinition.setFeedName(dataSourceName);
                feedDefinition.setUncheckedAPIEnabled(true);
                feedDefinition.setValidatedAPIEnabled(true);
                UploadPolicy uploadPolicy = new UploadPolicy(userID, getAccountID());
                feedDefinition.setUploadPolicy(uploadPolicy);
                feedDefinition.setFields(analysisItems);
                FeedCreationResult result = new FeedCreation().createFeed(feedDefinition, conn, new DataSet(), uploadPolicy);
                dataStorage = result.getTableDefinitionMetadata();
            } else if (dataSourceIDs.size() > 1) {
                throw new ServiceRuntimeException("More than one data source was found by that name. Please specify an API key for the data source instead.");
            } else {
                Map.Entry<Long, Boolean> entry = dataSourceIDs.entrySet().iterator().next();
                if (!entry.getValue()) {
                    throw new ServiceRuntimeException("This data source has been set to prohibit use of changeDataSourceToMatch as true.");
                }
                FeedStorage feedStorage = new FeedStorage();
                FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(entry.getKey());
                boolean newFieldsFound = false;
                List<AnalysisItem> previousItems = feedDefinition.getFields();
                for (AnalysisItem newItem : analysisItems) {
                    boolean newKey = true;
                    for (AnalysisItem previousItem : previousItems) {
                        if (newItem.getKey().equals(previousItem.getKey()) && newItem.getType() == previousItem.getType()) {
                            // matched the item...
                            newKey = false;
                        }
                    }
                    if (newKey) {
                        newFieldsFound = true;
                    }
                }
                if (newFieldsFound) {
                    feedDefinition.setFields(analysisItems);
                    feedStorage.updateDataFeedConfiguration(feedDefinition, conn);
                    dataStorage = DataStorage.writeConnection(feedDefinition, conn, getAccountID());
                    dataStorage.migrate(previousItems, analysisItems, false);
                    new DataSourceInternalService().updateComposites(feedDefinition, conn);
                } else {
                    dataStorage = DataStorage.writeConnection(feedDefinition, conn, getAccountID());
                }
            }
            dataSet = toDataSet(rows);
        } else {
            if (dataSourceIDs.size() == 0) {
                throw new ServiceRuntimeException("No data source was found by that name or API key.");
            } else if (dataSourceIDs.size() > 1) {
                throw new ServiceRuntimeException("More than one data source was found by that name. Please specify an API key for the data source instead.");
            } else {
                FeedStorage feedStorage = new FeedStorage();
                FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(dataSourceIDs.keySet().iterator().next(), conn);
                DataTransformation dataTransformation = new DataTransformation(feedDefinition);
                dataSet = dataTransformation.toDataSet(rows);
                dataStorage = DataStorage.writeConnection(feedDefinition, conn, getAccountID());
            }
        }
        return new CallData(dataStorage, dataSet);
    }

    private Map<Long, Boolean> findDataSourceIDsByName(String dataSourceName, Connection conn) throws SQLException {
        Map<Long, Boolean> dataSourceIDs = new HashMap<Long, Boolean>();
        PreparedStatement queryStmt = conn.prepareStatement("SELECT DISTINCT DATA_FEED.DATA_FEED_ID, DATA_FEED.UNCHECKED_API_ENABLED" +
                    " FROM UPLOAD_POLICY_USERS, DATA_FEED, user WHERE " +
                    "UPLOAD_POLICY_USERS.user_id = user.user_id AND user.user_id = ? AND DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_USERS.FEED_ID AND (DATA_FEED.FEED_NAME = ? OR " +
                "DATA_FEED.API_KEY = ?)");
        queryStmt.setLong(1, getUserID());
        queryStmt.setString(2, dataSourceName);
        queryStmt.setString(3, dataSourceName);
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            dataSourceIDs.put(rs.getLong(1), rs.getBoolean(2));
        }
        return dataSourceIDs;
    }

    protected final DataSet toDataSet(Row row) {
        DataSet dataSet = new DataSet();
        dataSet.addRow(toRow(row));
        return dataSet;
    }

    protected final DataSet toDataSet(List<Row> rows) {
        DataSet dataSet = new DataSet();
        for (Row row : rows) {
            dataSet.addRow(toRow(row));
        }
        return dataSet;
    }

    private IRow toRow(Row row) {
        IRow transformedRow = new com.easyinsight.analysis.Row();
        StringPair[] stringPairs = row.getStringPairs();
        if (stringPairs != null) {
            for (StringPair stringPair : stringPairs) {
                if (stringPair.getKey() == null) {
                    throw new ServiceRuntimeException("StringPair with value " + stringPair.getValue() + " had no key--key is a required field.");
                }
                transformedRow.addValue(stringPair.getKey(), stringPair.getValue() == null ? new EmptyValue() : new StringValue(stringPair.getValue()));
            }
        }
        NumberPair[] numberPairs = row.getNumberPairs();
        if (numberPairs != null) {
            for (NumberPair numberPair : numberPairs) {
                if (numberPair.getKey() == null) {
                    throw new ServiceRuntimeException("NumberPair with value " + numberPair.getValue() + " had no key--key is a required field.");
                }
                transformedRow.addValue(numberPair.getKey(), new NumericValue(numberPair.getValue()));
            }
        }
        DatePair[] datePairs = row.getDatePairs();
        if (datePairs != null) {
            for (DatePair datePair : datePairs) {
                if (datePair.getKey() == null) {
                    throw new ServiceRuntimeException("DatePair with value " + datePair.getValue() + " had no key--key is a required field.");
                }
                transformedRow.addValue(datePair.getKey(), datePair.getValue() == null ? new EmptyValue() : new DateValue(datePair.getValue()));
            }
        }
        return transformedRow;
    }
}