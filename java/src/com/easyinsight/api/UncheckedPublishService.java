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

    public void addRow(String dataSourceName, Row row) {
        Connection conn = Database.instance().getConnection();
        TableDefinitionMetadata tableDefinitionMetadata = null;
        try {
            String userName = getUserName();
            conn.setAutoCommit(false);
            tableDefinitionMetadata = getMetadata(userName, dataSourceName, row, conn);
            DataSet dataSet = toDataSet(row);
            tableDefinitionMetadata.truncate();
            tableDefinitionMetadata.insertData(dataSet);
            tableDefinitionMetadata.commit();
            conn.commit();
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

    public void addRows(String dataSourceName, Row[] rows) {
        Connection conn = Database.instance().getConnection();
        TableDefinitionMetadata tableDefinitionMetadata = null;
        try {
            String userName = getUserName();
            conn.setAutoCommit(false);
            tableDefinitionMetadata = getMetadata(userName, dataSourceName, rows[0], conn);
            DataSet dataSet = toDataSet(rows);
            tableDefinitionMetadata.truncate();
            tableDefinitionMetadata.insertData(dataSet);
            tableDefinitionMetadata.commit();
            conn.commit();
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

    

    public void replaceRows(String dataSourceName, Row[] rows) {
        Connection conn = Database.instance().getConnection();
        TableDefinitionMetadata tableDefinitionMetadata = null;
        try {
            String userName = getUserName();
            conn.setAutoCommit(false);
            tableDefinitionMetadata = getMetadata(userName, dataSourceName, rows[0], conn);
            DataSet dataSet = toDataSet(rows);
            tableDefinitionMetadata.truncate();
            tableDefinitionMetadata.insertData(dataSet);
            tableDefinitionMetadata.commit();
            conn.commit();
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

    private TableDefinitionMetadata getMetadata(String userName, String dataSourceName, Row row, Connection conn) throws SQLException {
        TableDefinitionMetadata tableDefinitionMetadata;
        List<Long> dataSourceIDs = findDataSourceIDsByName(userName, dataSourceName, conn);
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
            UploadPolicy uploadPolicy = new UploadPolicy(userID);
            feedDefinition.setUploadPolicy(uploadPolicy);
            feedDefinition.setFields(analysisItems);
            FeedCreationResult result = new FeedCreation().createFeed(feedDefinition, conn, new DataSet(), userID);
            tableDefinitionMetadata = result.getTableDefinitionMetadata();
        } else if (dataSourceIDs.size() > 1) {
            throw new RuntimeException("More than one data source was found by that name.");
        } else {
            FeedStorage feedStorage = new FeedStorage();
            FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(dataSourceIDs.get(0));
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
                tableDefinitionMetadata = TableDefinitionMetadata.readConnection(feedDefinition, conn);
                tableDefinitionMetadata.migrate(previousItems, analysisItems, false);
            } else {
                tableDefinitionMetadata = TableDefinitionMetadata.readConnection(feedDefinition, conn);
            }
        }
        return tableDefinitionMetadata;
    }

    private List<Long> findDataSourceIDsByName(String userName, String dataSourceName, Connection conn) throws SQLException {
        List<Long> dataSourceIDs = new ArrayList<Long>();
        PreparedStatement queryStmt = conn.prepareStatement("SELECT DISTINCT DATA_FEED.DATA_FEED_ID" +
                    " FROM UPLOAD_POLICY_USERS, DATA_FEED, user WHERE " +
                    "UPLOAD_POLICY_USERS.user_id = user.user_id AND user.username = ? AND DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_USERS.FEED_ID AND DATA_FEED.FEED_NAME = ?");
        queryStmt.setString(1, userName);
        queryStmt.setString(2, dataSourceName);
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            dataSourceIDs.add(rs.getLong(1));
        }
        return dataSourceIDs;
    }



    public void updateRow(String dataSourceName, Row row, Where where) {
        Connection conn = Database.instance().getConnection();
        TableDefinitionMetadata tableDefinitionMetadata = null;
        try {
            String userName = getUserName();
            List<IWhere> wheres = createWheres(where);
            conn.setAutoCommit(false);
            tableDefinitionMetadata = getMetadata(userName, dataSourceName, row, conn);
            DataSet dataSet = toDataSet(row);
            tableDefinitionMetadata.updateData(dataSet, wheres);
            tableDefinitionMetadata.commit();
            conn.commit();
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

    public void updateRows(String dataSourceName, Row[] rows, Where where) {
        Connection conn = Database.instance().getConnection();
        TableDefinitionMetadata tableDefinitionMetadata = null;
        try {
            String userName = getUserName();
            List<IWhere> wheres = createWheres(where);            
            conn.setAutoCommit(false);
            tableDefinitionMetadata = getMetadata(userName, dataSourceName, rows[0], conn);
            DataSet dataSet = toDataSet(rows);
            tableDefinitionMetadata.updateData(dataSet, wheres);
            tableDefinitionMetadata.commit();
            conn.commit();
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
