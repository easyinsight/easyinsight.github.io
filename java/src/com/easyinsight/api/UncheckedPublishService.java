package com.easyinsight.api;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.*;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedCreationResult;
import com.easyinsight.datafeeds.FeedCreation;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.users.UserService;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.*;
import com.easyinsight.analysis.AggregationTypes;
import com.easyinsight.logging.LogClass;
import com.easyinsight.database.Database;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * User: James Boe
 * Date: Aug 21, 2008
 * Time: 2:05:30 PM
 */

public abstract class UncheckedPublishService extends PublishService {

    protected abstract String getUserName();

    public boolean validateCredentials() {
        // if the user is able to connect to make this call, he's already met the test of valid credentials
        return true;
    }

    public void disableUnchecked(String dataSourceKey) {
        Connection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement stmt = conn.prepareStatement("UPDATE DATA_FEED SET unchecked_api_enabled = ? WHERE API_KEY = ?");
            stmt.setBoolean(1, false);
            stmt.setString(2, dataSourceKey);
            int rows = stmt.executeUpdate();
            if (rows != 1) {
                throw new RuntimeException("It does not appear that " + dataSourceKey + " is a valid data source key.");
            }
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LogClass.error(e);
            }
            Database.instance().closeConnection(conn);
        }
    }

    public String addRow(String dataSourceName, Row row) {
        Connection conn = Database.instance().getConnection();
        TableDefinitionMetadata tableDefinitionMetadata = null;
        try {
            String userName = getUserName();
            conn.setAutoCommit(false);
            CallData callData = getMetadata(userName, dataSourceName, row, conn);
            tableDefinitionMetadata = callData.tableDefinitionMetadata;
            DataSet dataSet = toDataSet(row);
            tableDefinitionMetadata.truncate();
            tableDefinitionMetadata.insertData(dataSet);
            tableDefinitionMetadata.commit();
            conn.commit();
            return callData.apiKey;
        } catch (Exception e) {
            LogClass.error(e);
            if (tableDefinitionMetadata != null) {
                tableDefinitionMetadata.rollback();
            }
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LogClass.error(e);
            }
            Database.instance().closeConnection(conn);
        }
    }

    public String addRows(String dataSourceName, Row[] rows) {
        Connection conn = Database.instance().getConnection();
        TableDefinitionMetadata tableDefinitionMetadata = null;
        try {
            String userName = getUserName();
            conn.setAutoCommit(false);
            CallData callData = getMetadata(userName, dataSourceName, rows[0], conn);
            tableDefinitionMetadata = callData.tableDefinitionMetadata;
            DataSet dataSet = toDataSet(rows);
            tableDefinitionMetadata.truncate();
            tableDefinitionMetadata.insertData(dataSet);
            tableDefinitionMetadata.commit();
            conn.commit();
            return callData.apiKey;
        } catch (Exception e) {
            LogClass.error(e);
            if (tableDefinitionMetadata != null) {
                tableDefinitionMetadata.rollback();
            }
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LogClass.error(e);
            }
            Database.instance().closeConnection(conn);
        }
    }

    

    public String replaceRows(String dataSourceName, Row[] rows) {
        Connection conn = Database.instance().getConnection();
        TableDefinitionMetadata tableDefinitionMetadata = null;
        try {
            String userName = getUserName();
            conn.setAutoCommit(false);
            CallData callData = getMetadata(userName, dataSourceName, rows[0], conn);
            tableDefinitionMetadata = callData.tableDefinitionMetadata;
            DataSet dataSet = toDataSet(rows);
            tableDefinitionMetadata.truncate();
            tableDefinitionMetadata.insertData(dataSet);
            tableDefinitionMetadata.commit();
            conn.commit();
            return callData.apiKey;
        } catch (Exception e) {
            LogClass.error(e);
            if (tableDefinitionMetadata != null) {
                tableDefinitionMetadata.rollback();
            }
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LogClass.error(e);
            }
            Database.instance().closeConnection(conn);
        }
    }

    private static class CallData {
        TableDefinitionMetadata tableDefinitionMetadata;
        String apiKey;

        private CallData(TableDefinitionMetadata tableDefinitionMetadata, String apiKey) {
            this.tableDefinitionMetadata = tableDefinitionMetadata;
            this.apiKey = apiKey;
        }
    }

    private CallData getMetadata(String userName, String dataSourceName, Row row, Connection conn) throws SQLException {
        String apiKey;
        TableDefinitionMetadata tableDefinitionMetadata;
        Map<Long, Boolean> dataSourceIDs = findDataSourceIDsByName(userName, dataSourceName, conn);
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        if (row.getStringPairs() != null) {
            for (StringPair stringPair : row.getStringPairs()) {
                analysisItems.add(new AnalysisDimension(stringPair.getKey(), true));
            }
        }
        if (row.getNumberPairs() != null) {
            for (NumberPair numberPair : row.getNumberPairs()) {
                analysisItems.add(new AnalysisMeasure(numberPair.getKey(), AggregationTypes.SUM));
            }
        }
        if (row.getDatePairs() != null) {
            for (DatePair datePair : row.getDatePairs()) {
                analysisItems.add(new AnalysisDateDimension(datePair.getKey(), true, AnalysisItemTypes.DAY_LEVEL));
            }
        }
        if (dataSourceIDs.size() == 0) {
            long userID = new UserService().getUserStub(userName).getUserID();
            // create new data source
            FeedDefinition feedDefinition = new FeedDefinition();
            feedDefinition.setFeedName(dataSourceName);
            feedDefinition.setOwnerName(userName);
            feedDefinition.setUncheckedAPIEnabled(true);
            feedDefinition.setValidatedAPIEnabled(true);
            UploadPolicy uploadPolicy = new UploadPolicy(userID);
            feedDefinition.setUploadPolicy(uploadPolicy);
            feedDefinition.setFields(analysisItems);
            FeedCreationResult result = new FeedCreation().createFeed(feedDefinition, conn, new DataSet(), userID);
            apiKey = feedDefinition.getApiKey();
            tableDefinitionMetadata = result.getTableDefinitionMetadata();
        } else if (dataSourceIDs.size() > 1) {
            throw new RuntimeException("More than one data source was found by that name.");
        } else {
            Map.Entry<Long, Boolean> entry = dataSourceIDs.entrySet().iterator().next();
            if (!entry.getValue()) {
                throw new RuntimeException("This data source does not allow use of the unchecked API.");
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
            apiKey = feedDefinition.getApiKey();
            if (newFieldsFound) {
                feedDefinition.setFields(analysisItems);
                feedStorage.updateDataFeedConfiguration(feedDefinition, conn);
                tableDefinitionMetadata = TableDefinitionMetadata.readConnection(feedDefinition, conn);
                tableDefinitionMetadata.migrate(previousItems, analysisItems, false);
            } else {
                tableDefinitionMetadata = TableDefinitionMetadata.readConnection(feedDefinition, conn);
            }
        }

        return new CallData(tableDefinitionMetadata, apiKey);
    }

    private Map<Long, Boolean> findDataSourceIDsByName(String userName, String dataSourceName, Connection conn) throws SQLException {
        Map<Long, Boolean> dataSourceIDs = new HashMap<Long, Boolean>();
        PreparedStatement queryStmt = conn.prepareStatement("SELECT DISTINCT DATA_FEED.DATA_FEED_ID, DATA_FEED.UNCHECKED_API_ENABLED" +
                    " FROM UPLOAD_POLICY_USERS, DATA_FEED, user WHERE " +
                    "UPLOAD_POLICY_USERS.user_id = user.user_id AND user.username = ? AND DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_USERS.FEED_ID AND (DATA_FEED.FEED_NAME = ? OR " +
                "DATA_FEED.API_KEY = ?)");
        queryStmt.setString(1, userName);
        queryStmt.setString(2, dataSourceName);
        queryStmt.setString(3, dataSourceName);
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            dataSourceIDs.put(rs.getLong(1), rs.getBoolean(2));
        }
        return dataSourceIDs;
    }



    public String updateRow(String dataSourceName, Row row, Where where) {
        Connection conn = Database.instance().getConnection();
        TableDefinitionMetadata tableDefinitionMetadata = null;
        try {
            String userName = getUserName();
            List<IWhere> wheres = createWheres(where);
            conn.setAutoCommit(false);
            CallData callData = getMetadata(userName, dataSourceName, row, conn);
            tableDefinitionMetadata = callData.tableDefinitionMetadata;
            DataSet dataSet = toDataSet(row);
            tableDefinitionMetadata.updateData(dataSet, wheres);
            tableDefinitionMetadata.commit();
            conn.commit();
            return callData.apiKey;
        } catch (Exception e) {
            LogClass.error(e);
            if (tableDefinitionMetadata != null) {
                tableDefinitionMetadata.rollback();
            }
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LogClass.error(e);
            }
            Database.instance().closeConnection(conn);
        }
    }

    public String updateRows(String dataSourceName, Row[] rows, Where where) {
        Connection conn = Database.instance().getConnection();
        TableDefinitionMetadata tableDefinitionMetadata = null;
        try {
            String userName = getUserName();
            List<IWhere> wheres = createWheres(where);            
            conn.setAutoCommit(false);
            CallData callData = getMetadata(userName, dataSourceName, rows[0], conn);
            tableDefinitionMetadata = callData.tableDefinitionMetadata;
            tableDefinitionMetadata = callData.tableDefinitionMetadata;
            DataSet dataSet = toDataSet(rows);
            tableDefinitionMetadata.updateData(dataSet, wheres);
            tableDefinitionMetadata.commit();
            conn.commit();
            return callData.apiKey;
        } catch (Exception e) {
            LogClass.error(e);
            if (tableDefinitionMetadata != null) {
                tableDefinitionMetadata.rollback();
            }
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LogClass.error(e);
            }
            Database.instance().closeConnection(conn);
        }
    }

    public void deleteRows(String dataSourceName, Where where) {

    }
}
