package com.easyinsight.datafeeds;

import com.easyinsight.analysis.ReportCache;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.database.EIConnection;
import com.easyinsight.scorecard.DataSourceRefreshEvent;
import com.easyinsight.users.User;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.userupload.CredentialsResponse;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.database.Database;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;

import java.util.*;
import java.sql.Connection;
import java.sql.SQLException;

import com.easyinsight.util.ServiceUtil;
import org.jetbrains.annotations.NotNull;
import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;

/**
 * User: James Boe
 * Date: Mar 30, 2009
 * Time: 8:50:59 PM
 */
public abstract class ServerDataSourceDefinition extends FeedDefinition implements IServerDataSourceDefinition {

    public void loadingProgress(int current, int total, String message, String callDataID) {
        if (callDataID != null) {
            DataSourceRefreshEvent info = new DataSourceRefreshEvent();
            info.setDataSourceID(getParentSourceID() == 0 ? getDataFeedID() : getParentSourceID());
            info.setDataSourceName(message);
            info.setType(DataSourceRefreshEvent.PROGRESS);
            info.setUserId(SecurityUtil.getUserID());
            info.setCurrent(current);
            info.setMax(total);
            ServiceUtil.instance().updateStatus(callDataID, ServiceUtil.RUNNING, info);
        }
    }

    public void defineCustomFields() {

    }

    public void exchangeTokens(EIConnection conn, HttpServletRequest request, String externalPin) throws Exception {
    }

    public long create(EIConnection conn, List<AnalysisItem> externalAnalysisItems, FeedDefinition parentDefinition) throws Exception {
        DataStorage metadata = null;
        try {
            Map<String, Key> keys = newDataSourceFields(parentDefinition);
            setFields(createAnalysisItems(keys, conn, parentDefinition));
            setOwnerName(retrieveUser(conn, SecurityUtil.getUserID()).getUserName());
            UploadPolicy uploadPolicy = new UploadPolicy(SecurityUtil.getUserID(), SecurityUtil.getAccountID());
            setUploadPolicy(uploadPolicy);
            FeedCreationResult feedCreationResult = new FeedCreation().createFeed(this, conn, new DataSet(), uploadPolicy);
            metadata = feedCreationResult.getTableDefinitionMetadata();
            if (metadata != null) metadata.commit();
            return feedCreationResult.getFeedID();
        } catch (SQLException e) {
            if (metadata != null) {
                metadata.rollback();
            }
            throw e;
        } finally {
            if (metadata != null) {
                metadata.closeConnection();
            }
        }
    }

    public static User retrieveUser(Connection conn, long userID) {
        try {
            User user = null;
            Session session = Database.instance().createSession(conn);
            List results;
            try {
                session.beginTransaction();
                results = session.createQuery("from User where userID = ?").setLong(0, userID).list();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw new RuntimeException(e);
            } finally {
                session.close();
            }
            if (results.size() > 0) {
                user = (User) results.get(0);
            }
            return user;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * The names of the fields used in this data source
     * @return the key names
     * @param parentDefinition
     */
    @NotNull
    protected abstract List<String> getKeys(FeedDefinition parentDefinition);

    public Map<String, Key> newDataSourceFields(FeedDefinition parentDefinition) {
        Map<String, Key> keyMap = new HashMap<String, Key>();
        if (getFields().size() == 0) {
            List<String> keys = getKeys(parentDefinition);
            for (String key : keys) {
                keyMap.put(key, new NamedKey(key));
            }
        } else {
            for (AnalysisItem field : getFields()) {
                keyMap.put(field.getKey().toKeyString(), field.getKey());
            }
        }
        return keyMap;
    }

    protected void addData(DataStorage dataStorage, DataSet dataSet) throws Exception {

        if (dataSet != null) {
            dataStorage.insertData(dataSet);
        }
    }

    public CredentialsResponse refreshData(long accountID, Date now, FeedDefinition parentDefinition, String callDataID, Date lastRefreshTime) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            refreshData(accountID, now, conn, parentDefinition, callDataID, lastRefreshTime);
            conn.commit();
            ReportCache.instance().flushResults(getDataFeedID());
            return new CredentialsResponse(true, getDataFeedID());
        } catch (ReportException re) {
            return new CredentialsResponse(false, re.getReportFault());
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            return new CredentialsResponse(false, e.getMessage(), getDataFeedID());
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public CredentialsResponse refreshData(long accountID, Date now, FeedDefinition parentDefinition, EIConnection conn, String callDataID, Date lastRefreshTime) {
        try {
            refreshData(accountID, now, conn, parentDefinition, callDataID, lastRefreshTime);
            return new CredentialsResponse(true, getDataFeedID());
        } catch (ReportException re) {
            return new CredentialsResponse(false, re.getReportFault());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean refreshData(long accountID, Date now, EIConnection conn, FeedDefinition parentDefinition, String callDataID, Date lastRefreshTime) throws Exception {
        DataStorage dataStorage = null;
        try {
            Map<String, Key> keys = newDataSourceFields(parentDefinition);
            dataStorage = DataStorage.writeConnection(this, conn, accountID);
            System.out.println("Refreshing " + getDataFeedID() + " for account " + accountID + " at " + new Date());
            if (clearsData() || lastRefreshTime == null || lastRefreshTime.getTime() < 100) {
                dataStorage.truncate(); 
            }
            DataSet dataSet = getDataSet(newDataSourceFields(parentDefinition), now, parentDefinition, dataStorage, conn, callDataID, lastRefreshTime);
            //List<AnalysisItem> items = createAnalysisItems(keys, dataSet, credentials, conn);
            //int version = dataStorage.getVersion();
            //int newVersion = dataStorage.migrate(getFields(), items);
            addData(dataStorage, dataSet);
            dataStorage.commit();
            System.out.println("Completed refresh of " + getDataFeedID() + " for account " + accountID + " at " + new Date());
            //notifyOfDataUpdate();
            return true;
        } catch (Exception e) {
            if (dataStorage != null) {
                dataStorage.rollback();
            }
            throw e;
        } finally {
            if (dataStorage != null) {
                dataStorage.closeConnection();
            }
        }
    }

    protected boolean clearsData() {
        return true;
    }

}
