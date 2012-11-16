package com.easyinsight.datafeeds;

import com.easyinsight.analysis.ReportException;
import com.easyinsight.analysis.ReportFault;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Key;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.storage.IDataStorage;
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

    void validateTableSetup(EIConnection conn) throws SQLException ;

    boolean hasNewData(Date lastRefreshDate, FeedDefinition parent, EIConnection conn) throws Exception;

    long create(EIConnection conn, List<AnalysisItem> externalAnalysisItems, FeedDefinition parentDefinition) throws Exception;

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
     *
     * @param keys the keys defined earlier by the getKeys() call
     * @param now
     * @param callDataID
     * @param lastRefreshDate
     * @return the data set
     */
    DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition feedDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException;

    /**
     * Retrieves the analysis items for the data source, defining such traits as dimensions, measures, dates, and so on. Use
     * the keys parameter to retrieve keys by their constants defined in getKeys() to associate with given analysis items.
     * For example, analysisItem.setKey(keys.get(CONSTANT));
     * @param keys the keys defined in the earlier getKeys() call
     * @return the analysis items for the data source
     */
    List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition);

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

    Map<String, Key> newDataSourceFields(FeedDefinition parentDefinition);

    boolean refreshData(long accountID, Date now, EIConnection conn, FeedDefinition parentDefinition, String callDataID, Date lastRefreshTime, boolean fullRefresh, List<ReportFault> warnings) throws Exception;

    /*boolean migrations(EIConnection conn, FeedDefinition parentDefinition) throws Exception;

    String tempLoad(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, String callDataID, Date lastRefreshTime) throws Exception;

    void applyTempLoad(EIConnection conn, long accountID, FeedDefinition parentDefinition, Date lastRefreshTime, String tempTable) throws Exception;*/

    Key getField(String sourceKey);

    List<AnalysisItem> getFields();
}
