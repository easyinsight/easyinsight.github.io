package com.easyinsight.datafeeds;

import com.easyinsight.database.EIConnection;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.users.Credentials;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Key;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.userupload.CredentialsResponse;
import com.easyinsight.userupload.UploadPolicy;

import java.util.Map;
import java.util.Date;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: Jun 16, 2009
 * Time: 9:40:09 AM
 */
public interface IServerDataSourceDefinition {
    /**
     * The account tier (matching constants on Account) required to see this data source in Connect External
     * @return the required account tier
     */
    int getRequiredAccountTier();

    long create(Credentials credentials, EIConnection conn) throws SQLException, CloneNotSupportedException;

    UploadPolicy getUploadPolicy();

    long getDataFeedID();

    String getFeedName();

    /**
     * The FeedType constant associated with this data source, used for loading purposes
     * @return the FeedType constant
     */
    FeedType getFeedType();

    int getCredentialsDefinition();

    void setCredentialsDefinition(int i);

    String validateCredentials(Credentials credentials);

    /**
     * Retrieves the actual data of the data source
     * @param credentials the credentials required for cnonection to the external source
     * @param keys the keys defined earlier by the getKeys() call
     * @param now
     * @return the data set
     */
    DataSet getDataSet(Credentials credentials, Map<String, Key> keys, Date now, FeedDefinition feedDefinition, DataStorage dataStorage, EIConnection conn);

    /**
     * Retrieves the analysis items for the data source, defining such traits as dimensions, measures, dates, and so on. Use
     * the keys parameter to retrieve keys by their constants defined in getKeys() to associate with given analysis items.
     * For example, analysisItem.setKey(keys.get(CONSTANT));
     * @param keys the keys defined in the earlier getKeys() call
     * @param dataSet the data set retrieved by getDataSet()
     * @param credentials
     * @return the analysis items for the data source
     */
    List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Credentials credentials, Connection conn);

    /**
     * Any custom logic for storage of the data source. Will execute in the transactional scope of saving the data source, on
     * new save and on update.
     * @param conn the connection object for JDBC persistence
     * @throws java.sql.SQLException
     */
    void customStorage(Connection conn) throws SQLException;

    /**
     * Any custom logic for retrieval of the data source.
     * @param conn the connection object for JDBC persistence
     * @throws java.sql.SQLException
     */
    void customLoad(Connection conn) throws SQLException;

    Map<String, Key> newDataSourceFields(Credentials credentials);

    CredentialsResponse refreshData(Credentials credentials, long accountID, Date now, FeedDefinition parentDefinition);

    boolean refreshData(Credentials credentials, long accountID, Date now, EIConnection conn, FeedDefinition parentDefinition) throws Exception;

    String getUsername();

    void setUsername(String username);

    String retrievePassword();

    String getPassword();

    void setPassword(String password);

    String getSessionId();

    void setSessionId(String sessionId);

    Key getField(String sourceKey);

    List<AnalysisItem> getFields();
}
