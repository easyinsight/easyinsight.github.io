package com.easyinsight.api;

import com.easyinsight.database.Database;
import com.easyinsight.storage.TableDefinitionMetadata;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.*;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedCreationResult;
import com.easyinsight.datafeeds.FeedCreation;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.users.UserService;
import com.easyinsight.analysis.AggregationTypes;

import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.WebServiceContext;
import javax.annotation.Resource;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.ws.security.handler.WSHandlerResult;
import org.apache.ws.security.WSSecurityEngineResult;
import org.apache.ws.security.WSUsernameTokenPrincipal;

/**
 * User: James Boe
 * Date: Jan 15, 2009
 * Time: 2:33:40 PM
 */
public class ValidatingPublishService extends PublishService implements IValidatingPublishService {

    @Resource
    private WebServiceContext context;

    public void addRow(String dataSourceName, Row row) {
        Connection conn = Database.instance().getConnection();
        TableDefinitionMetadata tableDefinitionMetadata = null;
        try {
            String userName = getUserName();
            conn.setAutoCommit(false);
            FeedDefinition feedDefinition = getFeedDefinition(userName, dataSourceName, conn);
            DataTransformation dataTransformation = new DataTransformation(feedDefinition);
            tableDefinitionMetadata = TableDefinitionMetadata.readConnection(feedDefinition, conn);
            DataSet dataSet = dataTransformation.toDataSet(row);
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

    public void addRows(String dataSourceKey, Row[] rows) {
        Connection conn = Database.instance().getConnection();
        TableDefinitionMetadata tableDefinitionMetadata = null;
        try {
            String userName = getUserName();
            conn.setAutoCommit(false);
            FeedDefinition feedDefinition = getFeedDefinition(userName, dataSourceKey, conn);
            DataTransformation dataTransformation = new DataTransformation(feedDefinition);
            tableDefinitionMetadata = TableDefinitionMetadata.readConnection(feedDefinition, conn);
            DataSet dataSet = dataTransformation.toDataSet(rows);
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

    public void replaceRows(String dataSourceKey, Row[] rows) {
        Connection conn = Database.instance().getConnection();
        TableDefinitionMetadata tableDefinitionMetadata = null;
        try {
            String userName = getUserName();
            conn.setAutoCommit(false);
            FeedDefinition feedDefinition = getFeedDefinition(userName, dataSourceKey, conn);
            DataTransformation dataTransformation = new DataTransformation(feedDefinition);
            tableDefinitionMetadata = TableDefinitionMetadata.readConnection(feedDefinition, conn);
            DataSet dataSet = dataTransformation.toDataSet(rows);
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

    private FeedDefinition getFeedDefinition(String userName, String dataSourceKey, Connection conn) throws SQLException {
        List<Long> dataSourceIDs = findDataSourceIDsByName(userName, dataSourceKey, conn);
        if (dataSourceIDs.size() == 0) {
            throw new RuntimeException("No data source was found by that API key.");
        } else if (dataSourceIDs.size() > 1) {
            throw new RuntimeException("More than one data source was found by that API key--this should never happen.");
        } else {
            FeedStorage feedStorage = new FeedStorage();
            return feedStorage.getFeedDefinitionData(dataSourceIDs.get(0));
        }
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

    public void updateRow(String dataSourceKey, Row row, Where where) {
        Connection conn = Database.instance().getConnection();
        TableDefinitionMetadata tableDefinitionMetadata = null;
        try {
            String userName = getUserName();
            conn.setAutoCommit(false);
            FeedDefinition feedDefinition = getFeedDefinition(userName, dataSourceKey, conn);
            DataTransformation dataTransformation = new DataTransformation(feedDefinition);
            tableDefinitionMetadata = TableDefinitionMetadata.readConnection(feedDefinition, conn);
            DataSet dataSet = dataTransformation.toDataSet(row);
            tableDefinitionMetadata.updateData(dataSet, createWheres(where));
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

    public void updateRows(String dataSourceKey, Row[] rows, Where where) {
        Connection conn = Database.instance().getConnection();
        TableDefinitionMetadata tableDefinitionMetadata = null;
        try {
            String userName = getUserName();
            conn.setAutoCommit(false);
            FeedDefinition feedDefinition = getFeedDefinition(userName, dataSourceKey, conn);
            DataTransformation dataTransformation = new DataTransformation(feedDefinition);
            tableDefinitionMetadata = TableDefinitionMetadata.readConnection(feedDefinition, conn);
            DataSet dataSet = dataTransformation.toDataSet(rows);
            tableDefinitionMetadata.updateData(dataSet, createWheres(where));
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

    public void deleteRows(String dataSourceKey, Where where) {
        Connection conn = Database.instance().getConnection();
        TableDefinitionMetadata tableDefinitionMetadata = null;
        try {
            String userName = getUserName();
            conn.setAutoCommit(false);
            FeedDefinition feedDefinition = getFeedDefinition(userName, dataSourceKey, conn);
            tableDefinitionMetadata = TableDefinitionMetadata.readConnection(feedDefinition, conn);
            tableDefinitionMetadata.deleteData(createWheres(where));
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

    private String getUserName() {
        MessageContext ctx = context.getMessageContext();
        Vector results = (Vector) ctx.get("RECV_RESULTS");
        WSHandlerResult wsHandlerResult = (WSHandlerResult) results.get(0);
        WSSecurityEngineResult securityResult = (WSSecurityEngineResult) wsHandlerResult.getResults().get(0);
        WSUsernameTokenPrincipal principal = (WSUsernameTokenPrincipal) securityResult.getPrincipal();
        return principal.getName();
    }
}
