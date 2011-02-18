package com.easyinsight.datafeeds;

import com.easyinsight.analysis.ReportException;
import com.easyinsight.database.EIConnection;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Key;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.userupload.CredentialsResponse;
import com.easyinsight.userupload.UploadPolicy;

import javax.servlet.http.HttpServletRequest;
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

    long create(EIConnection conn, List<AnalysisItem> externalAnalysisItems) throws Exception;

    UploadPolicy getUploadPolicy();

    long getDataFeedID();

    String getFeedName();

    void exchangeTokens(EIConnection conn, HttpServletRequest request, String externalPin) throws Exception;

    /**
     * The FeedType constant associated with this data source, used for loading purposes
     * @return the FeedType constant
     */
    FeedType getFeedType();

    String validateCredentials();

    /**
     * Retrieves the actual data of the data source
     *
     * @param keys the keys defined earlier by the getKeys() call
     * @param now
     * @param callDataID
     * @return the data set
     */
    DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition feedDefinition, DataStorage dataStorage, EIConnection conn, String callDataID) throws ReportException;

    /**
     * Retrieves the analysis items for the data source, defining such traits as dimensions, measures, dates, and so on. Use
     * the keys parameter to retrieve keys by their constants defined in getKeys() to associate with given analysis items.
     * For example, analysisItem.setKey(keys.get(CONSTANT));
     * @param keys the keys defined in the earlier getKeys() call
     * @param dataSet the data set retrieved by getDataSet()
     * @return the analysis items for the data source
     */
    List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Connection conn);

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

    Map<String, Key> newDataSourceFields();

    CredentialsResponse refreshData(long accountID, Date now, FeedDefinition parentDefinition, String callDataID);

    boolean refreshData(long accountID, Date now, EIConnection conn, FeedDefinition parentDefinition, String callDataID) throws Exception;

    Key getField(String sourceKey);

    List<AnalysisItem> getFields();

    CredentialsResponse refreshData(long accountID, Date now, FeedDefinition parentDefinition, EIConnection conn, String callDataID);
}
