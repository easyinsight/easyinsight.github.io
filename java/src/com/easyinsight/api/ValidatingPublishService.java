package com.easyinsight.api;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;

import javax.xml.ws.WebServiceContext;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * User: James Boe
 * Date: Jan 15, 2009
 * Time: 2:33:40 PM
 */
public abstract class ValidatingPublishService extends PublishService implements IValidatingPublishService {

    @Resource
    private WebServiceContext context;

    protected abstract String getUserName();

    protected abstract long getAccountID();

    protected abstract long getUserID();

    public boolean validateCredentials() {
        return true;
    }

    public void addRow(String dataSourceName, Row row) {
        EIConnection conn = Database.instance().getConnection();
        DataStorage dataStorage = null;
        try {
            long userID = getUserID();
            conn.setAutoCommit(false);
            FeedDefinition feedDefinition = getFeedDefinition(userID, dataSourceName, conn);
            DataTransformation dataTransformation = new DataTransformation(feedDefinition);
            dataStorage = DataStorage.writeConnection(feedDefinition, conn, getAccountID());
            DataSet dataSet = dataTransformation.toDataSet(row);
            dataStorage.insertData(dataSet);
            dataStorage.commit();
            conn.commit();
        } catch (ServiceRuntimeException sre) {
            LogClass.debug(sre.getMessage());
            if (dataStorage != null) {
                dataStorage.rollback();
            }
            conn.rollback();
            throw sre;
        } catch (Exception e) {
            LogClass.error(e);
            if (dataStorage != null) {
                dataStorage.rollback();
            }
            conn.rollback();
            throw new ServiceRuntimeException("An internal error occurred on attempting to process the provided data. The error has been logged for our engineers to examine.");
        } finally {
            conn.setAutoCommit(true);
            if (dataStorage != null) {
                dataStorage.closeConnection();
            }
            Database.closeConnection(conn);
        }
    }

    public void addRows(String dataSourceKey, Row[] rows) {
        EIConnection conn = Database.instance().getConnection();
        DataStorage dataStorage = null;
        try {
            conn.setAutoCommit(false);
            FeedDefinition feedDefinition = getFeedDefinition(getUserID(), dataSourceKey, conn);
            DataTransformation dataTransformation = new DataTransformation(feedDefinition);
            dataStorage = DataStorage.writeConnection(feedDefinition, conn, getAccountID());
            DataSet dataSet = dataTransformation.toDataSet(Arrays.asList(rows));
            dataStorage.insertData(dataSet);
            dataStorage.commit();
            conn.commit();
        } catch (ServiceRuntimeException sre) {
            LogClass.debug(sre.getMessage());
            if (dataStorage != null) {
                dataStorage.rollback();
            }
            conn.rollback();
            throw sre;
        } catch (Exception e) {
            LogClass.error(e);
            if (dataStorage != null) {
                dataStorage.rollback();
            }
            conn.rollback();
            throw new ServiceRuntimeException("An internal error occurred on attempting to process the provided data. The error has been logged for our engineers to examine.");
        } finally {
            conn.setAutoCommit(true);
            if (dataStorage != null) {
                dataStorage.closeConnection();
            }
            Database.closeConnection(conn);
        }
    }

    public void replaceRows(String dataSourceKey, Row[] rows) {
        EIConnection conn = Database.instance().getConnection();
        DataStorage dataStorage = null;
        try {
            conn.setAutoCommit(false);
            FeedDefinition feedDefinition = getFeedDefinition(getUserID(), dataSourceKey, conn);
            DataTransformation dataTransformation = new DataTransformation(feedDefinition);
            dataStorage = DataStorage.writeConnection(feedDefinition, conn, getAccountID());
            DataSet dataSet = dataTransformation.toDataSet(Arrays.asList(rows));
            dataStorage.truncate();
            dataStorage.insertData(dataSet);
            dataStorage.commit();
            conn.commit();
        } catch (ServiceRuntimeException sre) {
            LogClass.debug(sre.getMessage());
            if (dataStorage != null) {
                dataStorage.rollback();
            }
            conn.rollback();
            throw sre;
        } catch (Exception e) {
            LogClass.error(e);
            if (dataStorage != null) {
                dataStorage.rollback();
            }
            conn.rollback();
            throw new ServiceRuntimeException("An internal error occurred on attempting to process the provided data. The error has been logged for our engineers to examine.");
        } finally {
            conn.setAutoCommit(true);
            if (dataStorage != null) {
                dataStorage.closeConnection();
            }
            Database.closeConnection(conn);
        }
    }

    private FeedDefinition getFeedDefinition(long userID, String dataSourceKey, Connection conn) throws SQLException {
        List<Long> dataSourceIDs = findDataSourceIDsByName(userID, dataSourceKey, conn);
        if (dataSourceIDs.size() == 0) {
            throw new ServiceRuntimeException("No data source was found by that API key.");
        } else if (dataSourceIDs.size() > 1) {
            throw new ServiceRuntimeException("More than one data source was found by that API key--this should never happen.");
        } else {
            FeedStorage feedStorage = new FeedStorage();
            return feedStorage.getFeedDefinitionData(dataSourceIDs.get(0), conn);
        }
    }

    private List<Long> findDataSourceIDsByName(long userID, String dataSourceName, Connection conn) throws SQLException {
        List<Long> dataSourceIDs = new ArrayList<Long>();
        PreparedStatement queryStmt = conn.prepareStatement("SELECT DISTINCT DATA_FEED.DATA_FEED_ID" +
                    " FROM UPLOAD_POLICY_USERS, DATA_FEED WHERE " +
                    "UPLOAD_POLICY_USERS.user_id = ? AND DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_USERS.FEED_ID AND DATA_FEED.API_KEY = ?");
        queryStmt.setLong(1, userID);
        queryStmt.setString(2, dataSourceName);
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            dataSourceIDs.add(rs.getLong(1));
        }
        return dataSourceIDs;
    }

    public void updateRow(String dataSourceKey, Row row, Where where) {
        EIConnection conn = Database.instance().getConnection();
        DataStorage dataStorage = null;
        try {
            conn.setAutoCommit(false);
            FeedDefinition feedDefinition = getFeedDefinition(getUserID(), dataSourceKey, conn);
            DataTransformation dataTransformation = new DataTransformation(feedDefinition);
            dataStorage = DataStorage.writeConnection(feedDefinition, conn, getAccountID());
            DataSet dataSet = dataTransformation.toDataSet(row);
            dataStorage.updateData(dataSet, createWheres(where));
            dataStorage.commit();
            conn.commit();
        } catch (ServiceRuntimeException sre) {
            LogClass.debug(sre.getMessage());
            if (dataStorage != null) {
                dataStorage.rollback();
            }
            conn.rollback();
            throw sre;
        } catch (Exception e) {
            LogClass.error(e);
            if (dataStorage != null) {
                dataStorage.rollback();
            }
            conn.rollback();
            throw new ServiceRuntimeException("An internal error occurred on attempting to process the provided data. The error has been logged for our engineers to examine.");
        } finally {
            conn.setAutoCommit(true);
            if (dataStorage != null) {
                dataStorage.closeConnection();
            }
            Database.closeConnection(conn);
        }
    }

    public void updateRows(String dataSourceKey, Row[] rows, Where where) {
        EIConnection conn = Database.instance().getConnection();
        DataStorage dataStorage = null;
        try {
            conn.setAutoCommit(false);
            FeedDefinition feedDefinition = getFeedDefinition(getUserID(), dataSourceKey, conn);
            DataTransformation dataTransformation = new DataTransformation(feedDefinition);
            dataStorage = DataStorage.writeConnection(feedDefinition, conn, getAccountID());
            DataSet dataSet = dataTransformation.toDataSet(Arrays.asList(rows));
            dataStorage.updateData(dataSet, createWheres(where));
            dataStorage.commit();
            conn.commit();
        } catch (ServiceRuntimeException sre) {
            LogClass.debug(sre.getMessage());
            if (dataStorage != null) {
                dataStorage.rollback();
            }
            conn.rollback();
            throw sre;
        } catch (Exception e) {
            LogClass.error(e);
            if (dataStorage != null) {
                dataStorage.rollback();
            }
            conn.rollback();
            throw new ServiceRuntimeException("An internal error occurred on attempting to process the provided data. The error has been logged for our engineers to examine.");
        } finally {
            conn.setAutoCommit(true);
            if (dataStorage != null) {
                dataStorage.closeConnection();
            }
            Database.closeConnection(conn);
        }
    }

    public void deleteRows(String dataSourceKey, Where where) {
        EIConnection conn = Database.instance().getConnection();
        DataStorage dataStorage = null;
        try {
            conn.setAutoCommit(false);
            FeedDefinition feedDefinition = getFeedDefinition(getUserID(), dataSourceKey, conn);
            dataStorage = DataStorage.writeConnection(feedDefinition, conn, getAccountID());
            dataStorage.deleteData(createWheres(where));
            dataStorage.commit();
            conn.commit();
        } catch (ServiceRuntimeException sre) {
            LogClass.debug(sre.getMessage());
            if (dataStorage != null) {
                dataStorage.rollback();
            }
            conn.rollback();
            throw sre;
        } catch (Exception e) {
            LogClass.error(e);
            if (dataStorage != null) {
                dataStorage.rollback();
            }
            conn.rollback();
            throw new ServiceRuntimeException("An internal error occurred on attempting to process the provided data. The error has been logged for our engineers to examine.");
        } finally {
            conn.setAutoCommit(true);
            if (dataStorage != null) {
                dataStorage.closeConnection();
            }
            Database.instance().closeConnection(conn);
        }
    }
}
