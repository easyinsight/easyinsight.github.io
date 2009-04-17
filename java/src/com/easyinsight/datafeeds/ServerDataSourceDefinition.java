package com.easyinsight.datafeeds;

import com.easyinsight.users.Credentials;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.userupload.CredentialsResponse;
import com.easyinsight.database.Database;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.logging.LogClass;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Date;
import java.sql.Connection;
import java.sql.SQLException;

import org.jetbrains.annotations.NotNull;
import flex.messaging.MessageBroker;
import flex.messaging.messages.AsyncMessage;
import flex.messaging.util.UUIDUtils;

/**
 * User: James Boe
 * Date: Mar 30, 2009
 * Time: 8:50:59 PM
 */
public abstract class ServerDataSourceDefinition extends FeedDefinition {    

    /**
     * The account tier (matching constants on Account) required to see this data source in Connect External
     * @return the required account tier
     */
    public abstract int getRequiredAccountTier();

    

    /**
     * The FeedType constant associated with this data source, used for loading purposes
     * @return the FeedType constant
     */
    public abstract FeedType getFeedType();
    public abstract int getCredentialsDefinition();
    public abstract String validateCredentials(Credentials credentials);

    /**
     * Retrieves the actual data of the data source
     * @param credentials the credentials required for cnonection to the external source
     * @param keys the keys defined earlier by the getKeys() call
     * @param now
     * @return the data set
     */
    public abstract DataSet getDataSet(Credentials credentials, Map<String, Key> keys, Date now);

    /**
     * Retrieves the analysis items for the data source, defining such traits as dimensions, measures, dates, and so on. Use
     * the keys parameter to retrieve keys by their constants defined in getKeys() to associate with given analysis items.
     * For example, analysisItem.setKey(keys.get(CONSTANT));
     * @param keys the keys defined in the earlier getKeys() call
     * @param dataSet the data set retrieved by getDataSet()
     * @return the analysis items for the data source
     */
    public abstract List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet);

    /**
     * Any custom logic for storage of the data source. Will execute in the transactional scope of saving the data source, on
     * new save and on update.
     * @param conn the connection object for JDBC persistence
     * @throws SQLException
     */
    public abstract void customStorage(Connection conn) throws SQLException;

    /**
     * Any custom logic for retrieval of the data source.
     * @param conn the connection object for JDBC persistence
     * @throws SQLException
     */
    public abstract void customLoad(Connection conn) throws SQLException;

    /**
     * The names of the fields used in this data source
     * @return the key names
     */
    @NotNull
    protected abstract List<String> getKeys();

    public final Map<String, Key> newDataSourceFields(Credentials credentials) {
        Map<String, Key> keyMap = new HashMap<String, Key>();
        if (getDataFeedID() == 0) {
            List<String> keys = getKeys();
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

    protected void addData(DataStorage dataStorage, DataSet dataSet) throws SQLException {
        dataStorage.truncate();
        dataStorage.insertData(dataSet);
    }

    public CredentialsResponse refreshData(Credentials credentials, long accountID, Date now) {
        Connection conn = Database.instance().getConnection();
        DataStorage dataStorage = null;
        try {
            conn.setAutoCommit(false);
            DataSet dataSet = getDataSet(credentials, newDataSourceFields(credentials), now);
            dataStorage = DataStorage.writeConnection(this, conn, accountID);
            addData(dataStorage, dataSet);
            dataStorage.commit();
            conn.commit();
            notifyOfDataUpdate();
            return new CredentialsResponse(true);
        } catch (Exception e) {
            LogClass.error(e);
            if (dataStorage != null) {
                dataStorage.rollback();
            }
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
            return new CredentialsResponse(false, e.getMessage());
        } finally {
            if (dataStorage != null) {
                dataStorage.closeConnection();
            }
            Database.instance().closeConnection(conn);
        }
    }

    private void notifyOfDataUpdate() {
        MessageBroker msgBroker = MessageBroker.getMessageBroker(null);
        String clientID = UUIDUtils.createUUID();
        AsyncMessage msg = new AsyncMessage();
        msg.setDestination("dataUpdates");
        msg.setHeader(AsyncMessage.SUBTOPIC_HEADER_NAME, String.valueOf(getDataFeedID()));
        msg.setMessageId(clientID);
        msg.setTimestamp(System.currentTimeMillis());
        msgBroker.routeMessageToService(msg, null);
    }
}
