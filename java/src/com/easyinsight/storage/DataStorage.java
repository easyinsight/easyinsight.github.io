package com.easyinsight.storage;

import com.easyinsight.analysis.*;
import com.easyinsight.security.Roles;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.logging.LogClass;
import com.easyinsight.database.Database;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedPersistenceMetadata;
import com.easyinsight.core.*;
import com.easyinsight.cache.Cache;
import com.easyinsight.users.Account;

import java.util.*;
import java.util.Date;
import java.sql.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;

/**
 * User: James Boe
 * Date: Nov 8, 2008
 * Time: 4:08:06 PM
 */
public class DataStorage {
    private Map<Key, KeyMetadata> keys;
    private long feedID;
    private long accountID;
    private int version;
    private Database database;
    private Connection storageConn;
    private Connection coreDBConn;
    private boolean committed = false;
    private FeedPersistenceMetadata metadata;
    private static DateDimCache dateDimCache = new DateDimCache();

    private JCS reportCache = Cache.getCache(Cache.EMBEDDED_REPORTS);
                               
    /**
     * Creates a read only connection for retrieving data.
     *
     * @param fields the analysis items you want to retrieve
     * @param feedID the ID of the data source
     * @return a DataStorage object for making read calls
     */

    public static DataStorage readConnection(List<AnalysisItem> fields, long feedID) {
        DataStorage dataStorage = new DataStorage();
        Map<Key, KeyMetadata> keyMetadatas = new HashMap<Key, KeyMetadata>();
        for (AnalysisItem analysisItem : fields) {
            if (analysisItem.isDerived()) {
                continue;
            }
            Key key = analysisItem.getKey();
            if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                keyMetadatas.put(key, new KeyMetadata(key, Value.DATE, analysisItem));
            } else if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                keyMetadatas.put(key, new KeyMetadata(key, Value.NUMBER, analysisItem));
            } else {
                keyMetadatas.put(key, new KeyMetadata(key, Value.STRING, analysisItem));
            }
        }
        Connection conn = Database.instance().getConnection();
        try {
            dataStorage.metadata = getMetadata(feedID, conn);
            if (dataStorage.metadata == null) {
                dataStorage.metadata = createDefaultMetadata(conn);
            }
        } finally {
            Database.closeConnection(conn);
        }
        dataStorage.keys = keyMetadatas;
        dataStorage.feedID = feedID;
        dataStorage.version = dataStorage.metadata.getVersion();
        dataStorage.database = DatabaseManager.instance().getDatabase(dataStorage.metadata.getDatabase());
        dataStorage.storageConn = dataStorage.database.getConnection();
        try {
            dataStorage.storageConn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataStorage;
    }

    /**
     * Creates a DataStorage object for write purposes.
     *
     * @param feedDefinition the definition of the data source
     * @param conn           a connection with an existing transaction open
     * @return the new DataStorage object for writing
     * @throws java.sql.SQLException if something goes wrong
     */

    public static DataStorage writeConnection(FeedDefinition feedDefinition, Connection conn) throws SQLException {
        return writeConnection(feedDefinition, conn, SecurityUtil.getAccountID());
    }

    public static DataStorage writeConnection(FeedDefinition feedDefinition, Connection conn, long accountID) throws SQLException {
        DataStorage dataStorage = new DataStorage();
        dataStorage.accountID = accountID;
        Map<Key, KeyMetadata> keyMetadatas = new HashMap<Key, KeyMetadata>();
        for (AnalysisItem analysisItem : feedDefinition.getFields()) {
            if (analysisItem.isDerived()) {
                continue;
            }
            Key key = analysisItem.getKey();
            if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                keyMetadatas.put(key, new KeyMetadata(key, Value.DATE, analysisItem));
            } else if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                keyMetadatas.put(key, new KeyMetadata(key, Value.NUMBER, analysisItem));
            } else {
                keyMetadatas.put(key, new KeyMetadata(key, Value.STRING, analysisItem));
            }
        }
        dataStorage.metadata = getMetadata(feedDefinition.getDataFeedID(), conn);

        if (dataStorage.metadata == null) {
            dataStorage.metadata = createDefaultMetadata(conn);
        }
        dataStorage.keys = keyMetadatas;
        dataStorage.feedID = feedDefinition.getDataFeedID();
        dataStorage.version = dataStorage.metadata.getVersion();
        dataStorage.coreDBConn = conn;
        dataStorage.database = DatabaseManager.instance().getDatabase(dataStorage.metadata.getDatabase());
        dataStorage.storageConn = dataStorage.database.getConnection();
        dataStorage.storageConn.setAutoCommit(false);
        return dataStorage;
    }

    private void validateSpace(Connection conn) throws SQLException, StorageLimitException {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT SUM(SIZE) " +
                "FROM FEED_PERSISTENCE_METADATA, upload_policy_users, user WHERE feed_persistence_metadata.feed_id = upload_policy_users.feed_id and " +
                "upload_policy_users.role = ? and upload_policy_users.user_id = user.user_id and user.account_id = ?");
        queryStmt.setInt(1, Roles.OWNER);
        queryStmt.setLong(2, accountID);
        ResultSet currentSpaceRS = queryStmt.executeQuery();
        currentSpaceRS.next();
        long size = currentSpaceRS.getLong(1);
        PreparedStatement spaceAllowed = conn.prepareStatement("SELECT ACCOUNT_TYPE FROM ACCOUNT WHERE account_id = ?");
        spaceAllowed.setLong(1, accountID);
        ResultSet spaceRS = spaceAllowed.executeQuery();
        spaceRS.next();
        int accountType = spaceRS.getInt(1);
        long allowed;
        if (accountType == Account.ENTERPRISE) {
            allowed = Account.ENTERPRISE_MAX;
        } else if (accountType == Account.PROFESSIONAL) {
            allowed = Account.PROFESSIONAL_MAX;
        } else if (accountType == Account.INDIVIDUAL) {
            allowed = Account.INDIVIDUAL_MAX;
        } else if (accountType == Account.FREE) {
            allowed = Account.FREE_MAX;
        } else if (accountType == Account.GROUP) {
            allowed = Account.GROUP_MAX;
        } else if (accountType == Account.ADMINISTRATOR) {
            allowed = Account.ADMINISTRATOR_MAX;           
        } else {
            throw new RuntimeException("Unknown account type");
        }
        if (size > allowed) {
            throw new StorageLimitException("Storage boundary for this account has been reached.");
        }
    }

    public int getVersion() {
        return version;
    }

    public static void delete(long feedID, Connection conn) throws SQLException {
        FeedPersistenceMetadata metadata = getMetadata(feedID, conn);
        if (metadata != null) {
            deleteMetadata(feedID, conn);
            if (DatabaseManager.instance().getDatabase(metadata.getDatabase()) != null) {
                String dropSQL = "DROP TABLE " + "df" + feedID + "v" + metadata.getVersion();
                Connection storageConn = DatabaseManager.instance().getDatabase(metadata.getDatabase()).getConnection();
                try {
                    PreparedStatement dropTableStmt = storageConn.prepareStatement(dropSQL);
                    dropTableStmt.execute();
                } catch (SQLException se) {
                    if (se.getMessage().contains("Unknown table")) {
                        LogClass.error("Data source " + feedID + " did not have a storage table. Continuing with delete, screwed up data.");
                    } else {
                        throw se;
                    }
                } finally {
                    storageConn.close();
                }
            }
        }
    }

    public long calculateSize() throws SQLException {
        PreparedStatement countStmt = storageConn.prepareStatement("SHOW TABLE STATUS LIKE ?");
        countStmt.setString(1, getTableName());
        ResultSet countRS = countStmt.executeQuery();
        if (countRS.next()) {
            long dataLength = countRS.getLong("Data_length");
            long indexLength = countRS.getLong("Index_length");
            return dataLength + indexLength;
        } else {
            return 0;
        }
    }

    public void createTable() throws SQLException {
        ResultSet existsRS = storageConn.getMetaData().getTables(null, null, getTableName(), null);
        if (existsRS.next()) {
            storageConn.prepareStatement("DROP TABLE " + getTableName()).execute();
        }
        String sql = defineTableSQL();
        PreparedStatement createSQL = storageConn.prepareStatement(sql);
        createSQL.execute();
    }

    public void commit() throws SQLException {
        long size = calculateSize();
        metadata.setSize(size);
        metadata.setLastData(new Date());
        if (coreDBConn == null) {
            Connection conn = Database.instance().getConnection();
            try {
                addOrUpdateMetadata(feedID, metadata, conn);
            } finally {
                Database.closeConnection(conn);
            }
        } else {
            addOrUpdateMetadata(feedID, metadata, coreDBConn);
            validateSpace(coreDBConn);
        }
        storageConn.commit();
        try {
            if (reportCache != null) reportCache.remove(feedID);
        } catch (CacheException e) {
            LogClass.error(e);
        }
        committed = true;
    }

    public Connection getStorageConn() {
        return storageConn;
    }

    public void rollback() {
        try {
            if (!committed) {
                storageConn.rollback();
            }
        } catch (SQLException e) {
            LogClass.error(e);
        }
    }

    public void closeConnection() {
        try {
            storageConn.setAutoCommit(true);
        } catch (SQLException e) {
            LogClass.error(e);
        }
        Database.closeConnection(storageConn);
        storageConn = null;
    }


    private static class FieldMigration {
        // private int previousType;
        private int newType;
        private Key key;

        public FieldMigration(AnalysisItem newItem) {
            key = newItem.getKey();
            /*if (previousItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                previousType = Value.DATE;
            } else if (previousItem.hasType(AnalysisItemTypes.MEASURE)) {
                previousType = Value.NUMBER;
            } else {
                previousType = Value.STRING;
            } */
            if (newItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                newType = Value.DATE;
            } else if (newItem.hasType(AnalysisItemTypes.MEASURE)) {
                newType = Value.NUMBER;
            } else {
                newType = Value.STRING;
            }
        }
    }

    public int migrate(List<AnalysisItem> previousItems, List<AnalysisItem> newItems) throws SQLException {
        return migrate(previousItems, newItems, true);
    }

    public int migrate(List<AnalysisItem> previousItems, List<AnalysisItem> newItems, boolean migrateData) throws SQLException {
        // did any items change in a way that requires us to migrate...
        List<FieldMigration> fieldMigrations = new ArrayList<FieldMigration>();
        boolean newFieldsFound = false;
        for (AnalysisItem newItem : newItems) {
            if (newItem.isDerived()) {
                continue;
            }
            boolean newKey = true;
            for (AnalysisItem previousItem : previousItems) {
                if (newItem.getKey().equals(previousItem.getKey())) {
                    // matched the item...
                    newKey = false;
                    if ((newItem.hasType(AnalysisItemTypes.DATE_DIMENSION) && !previousItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) ||
                            (newItem.hasType(AnalysisItemTypes.MEASURE) && !previousItem.hasType(AnalysisItemTypes.MEASURE)) ||
                            (newItem.hasType(AnalysisItemTypes.DIMENSION) && previousItem.hasType(AnalysisItemTypes.MEASURE))) {
                        fieldMigrations.add(new FieldMigration(newItem));
                    }
                }
            }
            if (newKey) {
                newFieldsFound = true;
            }
        }

        if (newFieldsFound || !fieldMigrations.isEmpty()) {
            int previousVersion = metadata.getVersion();
            this.version = previousVersion + 1;
            this.metadata.setVersion(this.version);
            List<AnalysisItem> previousKeys = new ArrayList<AnalysisItem>();
            for (AnalysisItem previousItem : previousItems) {
                if (!previousItem.isDerived()) {
                    previousKeys.add(previousItem);
                }
            }
            DataSet existing = null;
            if (migrateData) {
                if (previousKeys.isEmpty()) {
                    existing = new DataSet();
                } else {
                    Map<Key, KeyMetadata> keyMetadatas = new HashMap<Key, KeyMetadata>();
                    for (AnalysisItem analysisItem : previousItems) {
                        if (analysisItem.isDerived()) {
                            continue;
                        }
                        Key key = analysisItem.getKey();
                        if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                            keyMetadatas.put(key, new KeyMetadata(key, Value.DATE, analysisItem));
                        } else if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                            keyMetadatas.put(key, new KeyMetadata(key, Value.NUMBER, analysisItem));
                        } else {
                            keyMetadatas.put(key, new KeyMetadata(key, Value.STRING, analysisItem));
                        }
                    }
                    InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
                    insightRequestMetadata.setNow(new Date());
                    insightRequestMetadata.setAggregateQuery(false);
                    existing = retrieveData(previousKeys, null, null, 0, keyMetadatas, previousVersion, insightRequestMetadata);
                    //existing = new DataSet();
                }
            }

            ResultSet existsRS = storageConn.getMetaData().getTables(null, null, getTableName(), null);
            if (existsRS.next()) {
                storageConn.prepareStatement("DROP TABLE " + getTableName()).execute();
            }
            String sql = defineTableSQL();
            LogClass.info("Creating new storage table in migration with sql " + sql);
            PreparedStatement createSQL = storageConn.prepareStatement(sql);
            createSQL.execute();

            if (migrateData) {
                for (FieldMigration fieldMigration : fieldMigrations) {
                    for (IRow row : existing.getRows()) {
                        Value existingValue = row.getValue(fieldMigration.key);
                        String string = existingValue.toString();
                        if (fieldMigration.newType == Value.DATE) {

                        } else if (fieldMigration.newType == Value.NUMBER) {
                            double doubleValue = NumericValue.produceDoubleValue(string);
                            row.addValue(fieldMigration.key, new NumericValue(doubleValue));
                        } else {
                            if (existingValue.type() == Value.NUMBER) {
                                string = ((Integer) ((NumericValue) existingValue).getValue().intValue()).toString();
                            }
                            row.addValue(fieldMigration.key, new StringValue(string));
                        }
                    }
                }
                insertData(existing);
            }

            String dropSQL = "DROP TABLE " + "df" + feedID + "v" + previousVersion;
            PreparedStatement dropTableStmt = storageConn.prepareStatement(dropSQL);
            dropTableStmt.execute();
        }
        return this.version;
    }

    /**
     * Clears out all data in the data source.
     *
     * @throws java.sql.SQLException if something goes wrong
     */

    public void truncate() throws SQLException {
        PreparedStatement truncateStmt = storageConn.prepareStatement("TRUNCATE " + getTableName());
        truncateStmt.execute();
    }

    /**
     * Retrieves the requested data set from the database.
     *
     * @param reportItems    the analysis items you're looking to retrieve
     * @param filters        any filter definitions you want to constrain data by
     * @param additionalKeys any additional keys not associated to analysis items, like data scrubs or composite connections
     * @param limit          optional limit on result set
     * @return the created data set
     * @throws java.sql.SQLException if something goes wrong
     */

    public DataSet retrieveData(@NotNull Collection<AnalysisItem> reportItems, @Nullable Collection<FilterDefinition> filters, @Nullable Collection<Key> additionalKeys, @Nullable Integer limit) throws SQLException {
        return retrieveData(reportItems, filters, additionalKeys, limit, keys, version, null);
    }

    /**
     * Retrieves the requested data set from the database.
     *
     * @param reportItems            the analysis items you're looking to retrieve
     * @param filters                any filter definitions you want to constrain data by
     * @param additionalKeys         any additional keys not associated to analysis items, like data scrubs or composite connections
     * @param limit                  optional limit on result set
     * @param insightRequestMetadata the request metadata
     * @return the created data set
     * @throws java.sql.SQLException if something goes wrong
     */

    public DataSet retrieveData(Collection<AnalysisItem> reportItems, Collection<FilterDefinition> filters, Collection<Key> additionalKeys, Integer limit,
                                InsightRequestMetadata insightRequestMetadata) throws SQLException {
        return retrieveData(reportItems, filters, additionalKeys, limit, keys, version, insightRequestMetadata);
    }

    private DataSet retrieveData(@NotNull Collection<AnalysisItem> reportItems, @Nullable Collection<FilterDefinition> filters, @Nullable Collection<Key> additionalKeys, @Nullable Integer limit,
                                 @NotNull Map<Key, KeyMetadata> keys, int version, @Nullable InsightRequestMetadata insightRequestMetadata) throws SQLException {
        if (insightRequestMetadata == null) {
            insightRequestMetadata = new InsightRequestMetadata();
            insightRequestMetadata.setNow(new Date());
        }
        filters = eligibleFilters(filters);
        StringBuilder queryBuilder = new StringBuilder();
        StringBuilder selectBuilder = new StringBuilder();
        StringBuilder fromBuilder = new StringBuilder();
        StringBuilder whereBuilder = new StringBuilder();
        StringBuilder groupByBuilder = new StringBuilder();
        Collection<Key> groupByItems = new HashSet<Key>();
        createSelectClause(reportItems, selectBuilder, groupByItems, insightRequestMetadata.isAggregateQuery());
        addAdditionalKeysToSelect(additionalKeys, selectBuilder, groupByItems);
        selectBuilder = selectBuilder.deleteCharAt(selectBuilder.length() - 1);
        createFromClause(version, fromBuilder);
        createWhereClause(filters, whereBuilder);
        groupByBuilder = createGroupByClause(groupByBuilder, groupByItems);
        createSQL(filters, limit, queryBuilder, selectBuilder, fromBuilder, whereBuilder, groupByBuilder, groupByItems);
        PreparedStatement queryStmt = storageConn.prepareStatement(queryBuilder.toString());
        populateParameters(filters, keys, queryStmt, insightRequestMetadata);
        DataSet dataSet = new DataSet();
        ResultSet dataRS = queryStmt.executeQuery();
        processQueryResults(reportItems, keys, dataSet, dataRS, additionalKeys);
        dataSet.setLastTime(metadata.getLastData());
        return dataSet;
    }

    @NotNull
    private Collection<FilterDefinition> eligibleFilters(@Nullable Collection<FilterDefinition> filters) {
        Collection<FilterDefinition> eligibleFilters = new ArrayList<FilterDefinition>();
        if (filters != null) {
            for (FilterDefinition filterDefinition : filters) {
                if (filterDefinition.isApplyBeforeAggregation() && filterDefinition.validForQuery()) {
                    eligibleFilters.add(filterDefinition);
                }
            }
        }
        return eligibleFilters;
    }

    private void processQueryResults(@NotNull Collection<AnalysisItem> reportItems, @NotNull Map<Key, KeyMetadata> keys, @NotNull DataSet dataSet, @NotNull ResultSet dataRS, Collection<Key> additionalKeys) throws SQLException {
        while (dataRS.next()) {
            IRow row = dataSet.createRow();
            int i = 1;
            for (AnalysisItem analysisItem : reportItems) {
                Key key = analysisItem.getKey().toBaseKey();
                KeyMetadata keyMetadata = keys.get(key);
                if (keyMetadata != null) {
                    if (keyMetadata.getType() == Value.DATE) {
                        Timestamp time = dataRS.getTimestamp(i++);
                        if (dataRS.wasNull()) {
                            row.addValue(key, new EmptyValue());
                        } else {
                            row.addValue(key, new DateValue(new java.util.Date(time.getTime())));
                        }
                    } else if (keyMetadata.getType() == Value.NUMBER) {
                        double value = dataRS.getDouble(i++);
                        if (dataRS.wasNull()) {
                            row.addValue(key, new EmptyValue());
                        } else {
                            row.addValue(key, new NumericValue(value));
                        }
                    } else {
                        String value = dataRS.getString(i++);
                        if (dataRS.wasNull()) {
                            row.addValue(key, new EmptyValue());
                        } else {
                            row.addValue(key, new StringValue(value));
                        }
                    }
                }
            }
            if (additionalKeys != null) {
                for (Key key : additionalKeys) {
                    KeyMetadata keyMetadata = keys.get(key);
                    if (keyMetadata != null) {
                        if (keyMetadata.getType() == Value.DATE) {
                            Timestamp time = dataRS.getTimestamp(i++);
                            if (dataRS.wasNull()) {
                                row.addValue(key, new EmptyValue());
                            } else {
                                row.addValue(key, new DateValue(new java.util.Date(time.getTime())));
                            }
                        } else if (keyMetadata.getType() == Value.NUMBER) {
                            double value = dataRS.getDouble(i++);
                            if (dataRS.wasNull()) {
                                row.addValue(key, new EmptyValue());
                            } else {
                                row.addValue(key, new NumericValue(value));
                            }
                        } else {
                            String value = dataRS.getString(i++);
                            if (dataRS.wasNull()) {
                                row.addValue(key, new EmptyValue());
                            } else {
                                row.addValue(key, new StringValue(value));
                            }
                        }
                    }
                }
            }
        }
    }

    private void populateParameters(@NotNull Collection<FilterDefinition> filters, @NotNull Map<Key, KeyMetadata> keys, @NotNull PreparedStatement queryStmt, @NotNull InsightRequestMetadata insightRequestMetadata) throws SQLException {
        if (filters.size() > 0) {
            int i = 1;
            for (FilterDefinition filterDefinition : filters) {
                KeyMetadata keyMetadata = keys.get(filterDefinition.getField().getKey().toBaseKey());
                int type = keyMetadata.type;
                i = filterDefinition.populatePreparedStatement(queryStmt, i, type, insightRequestMetadata);
            }
        }
    }

    private void createSQL(@NotNull Collection<FilterDefinition> filters, @Nullable Integer limit, @NotNull StringBuilder queryBuilder, @NotNull StringBuilder selectBuilder,
                           @NotNull StringBuilder fromBuilder, @NotNull StringBuilder whereBuilder, @NotNull StringBuilder groupByBuilder, @NotNull Collection<Key> groupByItems) {
        queryBuilder.append("SELECT ");
        queryBuilder.append(selectBuilder.toString());
        queryBuilder.append(" FROM ");
        queryBuilder.append(fromBuilder.toString());
        if (filters.size() > 0) {
            queryBuilder.append(" WHERE ");
            queryBuilder.append(whereBuilder.toString());
        }
        if (groupByItems.size() > 0) {
            queryBuilder.append(" GROUP BY ");
            queryBuilder.append(groupByBuilder.toString());
        }
        if (limit != null && limit > 0) {
            queryBuilder.append(" LIMIT ").append(limit);
        }
    }

    private StringBuilder createGroupByClause(StringBuilder groupByBuilder, Collection<Key> groupByItems) {
        if (groupByItems.size() > 0) {
            for (Key key : groupByItems) {
                String columnName = key.toSQL();
                groupByBuilder.append(columnName);
                groupByBuilder.append(",");
            }
            groupByBuilder = groupByBuilder.deleteCharAt(groupByBuilder.length() - 1);
        }
        return groupByBuilder;
    }

    private void createWhereClause(Collection<FilterDefinition> filters, StringBuilder whereBuilder) {
        if (filters != null && filters.size() > 0) {
            Iterator<FilterDefinition> filterIter = filters.iterator();
            while (filterIter.hasNext()) {
                FilterDefinition filterDefinition = filterIter.next();
                whereBuilder.append(filterDefinition.toQuerySQL(getTableName()));
                if (filterIter.hasNext()) {
                    whereBuilder.append(" AND ");
                }
            }
        }
    }

    private void createFromClause(int version, StringBuilder fromBuilder) {
        String tableName = "df" + feedID + "v" + version;
        fromBuilder.append(tableName);
    }

    private void addAdditionalKeysToSelect(Collection<Key> additionalKeys, StringBuilder selectBuilder, Collection<Key> groupByItems) {
        if (additionalKeys != null) {
            for (Key key : additionalKeys) {
                String columnName = key.toSQL();
                selectBuilder.append(columnName);
                selectBuilder.append(",");
                groupByItems.add(key);
            }
        }
    }

    private void createSelectClause(Collection<AnalysisItem> reportItems, StringBuilder selectBuilder, Collection<Key> groupByItems, boolean aggregateQuery) {
        for (AnalysisItem analysisItem : reportItems) {
            if (analysisItem.isDerived()) {
                throw new RuntimeException("Attempt made to query a derived analysis item");
            }
            String columnName = analysisItem.toKeySQL();
            if (analysisItem.hasType(AnalysisItemTypes.MEASURE) && aggregateQuery) {
                AnalysisMeasure analysisMeasure = (AnalysisMeasure) analysisItem;
                if (analysisMeasure.getAggregation() == AggregationTypes.SUM) {
                    columnName = "SUM(" + columnName + ")";
                } else if (analysisMeasure.getAggregation() == AggregationTypes.AVERAGE) {
                    columnName = "AVG(" + columnName + ")";
                } else if (analysisMeasure.getAggregation() == AggregationTypes.COUNT) {
                } else if (analysisMeasure.getAggregation() == AggregationTypes.MAX) {
                    columnName = "MAX(" + columnName + ")";
                } else if (analysisMeasure.getAggregation() == AggregationTypes.MIN) {
                    columnName = "MIN(" + columnName + ")";
                } else {
                    groupByItems.add(analysisItem.getKey());
                }
            } else {
                groupByItems.add(analysisItem.getKey());
            }
            selectBuilder.append(columnName);
            selectBuilder.append(",");
        }
    }

    public void insertData(DataSet dataSet) throws SQLException {
        StringBuilder columnBuilder = new StringBuilder();
        StringBuilder paramBuilder = new StringBuilder();
        Iterator<KeyMetadata> keyIter = keys.values().iterator();
        while (keyIter.hasNext()) {
            KeyMetadata keyMetadata = keyIter.next();
            columnBuilder.append(keyMetadata.createInsertClause());
            //columnBuilder.append("k").append(keyMetadata.key.getKeyID());
            //paramBuilder.append("?");
            paramBuilder.append(keyMetadata.createInsertQuestionMarks());
            if (keyIter.hasNext()) {
                columnBuilder.append(",");
                paramBuilder.append(",");
            }
        }
        String columns = columnBuilder.toString();
        String parameters = paramBuilder.toString();
        String insertSQL = "INSERT INTO " + getTableName() + " (" + columns + ") VALUES (" + parameters + ")";
        PreparedStatement insertStmt = storageConn.prepareStatement(insertSQL);
        try {
            for (IRow row : dataSet.getRows()) {
                int i = 1;
                for (KeyMetadata keyMetadata : keys.values()) {
                    i = setValue(insertStmt, row, i, keyMetadata);
                }
                insertStmt.execute();
            }
        } catch (SQLException e) {
            LogClass.error("Failure on persistence where SQL = " + insertSQL + ", database = " + database.getID());
            throw e;
        }
    }

    public void updateData(DataSet dataSet, List<IWhere> wheres) throws SQLException {
        StringBuilder fieldBuilder = new StringBuilder();
        List<KeyMetadata> updateKeys = new ArrayList<KeyMetadata>();
        for (KeyMetadata keyMetadata : keys.values()) {
            //boolean inWhereClause = false;
            for (IWhere where : wheres) {
                if (where.getKey().equals(keyMetadata.getKey())) {
                    where.getKey().setKeyID(keyMetadata.getKey().getKeyID());
                    //inWhereClause = true;
                }
            }
            //if (!inWhereClause) {
            updateKeys.add(keyMetadata);
            //}
        }
        Iterator<KeyMetadata> keyIter = updateKeys.iterator();
        while (keyIter.hasNext()) {
            KeyMetadata keyMetadata = keyIter.next();
            fieldBuilder.append(keyMetadata.createUpdateClause());
            if (keyIter.hasNext()) {
                fieldBuilder.append(",");
            }
        }

        StringBuilder whereBuilder = new StringBuilder();
        Iterator<IWhere> whereIter = wheres.iterator();
        while (whereIter.hasNext()) {
            IWhere where = whereIter.next();
            whereBuilder.append(where.createWhereSQL());
            if (whereIter.hasNext()) {
                whereBuilder.append(" AND ");
            }
        }
        StringBuilder tableBuilder = new StringBuilder();
        for (IWhere where : wheres) {
            for (String extraTable : where.getExtraTables()) {
                tableBuilder.append(extraTable).append(",");
            }
        }
        tableBuilder.append(getTableName());
        String updateSQL = "DELETE " + getTableName() + " FROM " + tableBuilder.toString() + " WHERE " + whereBuilder.toString();
        System.out.println(updateSQL);
        PreparedStatement updateStmt = storageConn.prepareStatement(updateSQL);
        int i = 1;
        for (IWhere where : wheres) {
            i = where.setValue(updateStmt, i);
        }
        updateStmt.executeUpdate();
        dataSet.mergeWheres(wheres);
        insertData(dataSet);
        for (IRow row : dataSet.getRows()) {
            /*int i = 1;
            for (KeyMetadata keyMetadata : updateKeys) {
                i = setValue(updateStmt, row, i, keyMetadata);
            }
            for (IWhere where : wheres) {
                i = where.setValue(updateStmt, i);
            }

            int rows = updateStmt.executeUpdate();
            if (rows == 0) {*/
            //}
        }
    }

    private int setValue(PreparedStatement insertStmt, IRow row, int i, KeyMetadata keyMetadata) throws SQLException {
        Value value = row.getValue(keyMetadata.getKey());
        if (value == null || value.type() == Value.EMPTY) {
            int sqlType;
            if (keyMetadata.getType() == Value.DATE) {
                sqlType = Types.DATE;
            } else if (keyMetadata.getType() == Value.NUMBER) {
                sqlType = Types.DOUBLE;
            } else {
                sqlType = Types.VARCHAR;
            }
            insertStmt.setNull(i++, sqlType);
            if (keyMetadata.getType() == Value.DATE) {
                insertStmt.setNull(i++, Types.BIGINT);
            }
        } else if (keyMetadata.getType() == Value.DATE) {
            java.util.Date date = null;
            if (value.type() != Value.DATE) {
                AnalysisItem analysisItem = keyMetadata.getAnalysisItem();
                AnalysisDateDimension analysisDateDimension = (AnalysisDateDimension) analysisItem;
                analysisDateDimension.setDateLevel(AnalysisDateDimension.DAY_LEVEL);
                Value transformedValue = analysisItem.transformValue(value, new InsightRequestMetadata());
                if (transformedValue.type() == Value.EMPTY)
                    insertStmt.setNull(i++, Types.DATE);
                else {
                    DateValue dateValue = (DateValue) transformedValue;
                    date = dateValue.getDate();
                    java.sql.Timestamp sqlDate = new java.sql.Timestamp(date.getTime());
                    insertStmt.setTimestamp(i++, sqlDate);
                }
            } else {
                DateValue dateValue = (DateValue) value;
                date = dateValue.getDate();
                if (date == null) {
                    insertStmt.setNull(i++, Types.DATE);
                } else {
                    java.sql.Timestamp sqlDate = new java.sql.Timestamp(date.getTime());
                    insertStmt.setTimestamp(i++, sqlDate);
                }
            }
            if (date == null) {
                insertStmt.setNull(i++, Types.BIGINT);
            } else {
                long id = dateDimCache.getDateDimID(date, storageConn);
                insertStmt.setLong(i++, id);
            }
        } else if (keyMetadata.getType() == Value.NUMBER) {
            Double num = null;
            if (value.type() == Value.STRING) {
                num = NumericValue.produceDoubleValue(value.toString());
            } else if (value.type() == Value.NUMBER) {
                NumericValue numericValue = (NumericValue) value;
                num = numericValue.toDouble();
            }
            if (num == null) {
                insertStmt.setNull(i++, Types.DOUBLE);
            } else {
                insertStmt.setDouble(i++, num);
            }
        } else {
            String string = null;
            if (value.type() == Value.STRING) {
                StringValue stringValue = (StringValue) value;
                string = stringValue.getValue();
            } else if (value.type() == Value.NUMBER) {
                NumericValue numericValue = (NumericValue) value;
                string = String.valueOf(numericValue.toDouble());
            } else if (value.type() == Value.DATE) {
                DateValue dateValue = (DateValue) value;
                string = dateValue.getDate().toString();
            }
            if (string == null) {
                insertStmt.setNull(i++, Types.VARCHAR);
            } else {
                if (string.length() > 253) {
                    string = string.substring(0, 253);
                }
                insertStmt.setString(i++, string);
            }
        }
        return i;
    }

    public void deleteData(List<IWhere> wheres) throws SQLException {
        StringBuilder whereBuilder = new StringBuilder();
        Iterator<IWhere> whereIter = wheres.iterator();
        while (whereIter.hasNext()) {
            IWhere where = whereIter.next();
            whereBuilder.append(where.createWhereSQL());
            if (whereIter.hasNext()) {
                whereBuilder.append(",");
            }
        }

        PreparedStatement deleteStmt = storageConn.prepareStatement("DELETE FROM " + getTableName() + " WHERE " + whereBuilder.toString());
        int i = 1;
        for (IWhere where : wheres) {
            where.setValue(deleteStmt, i++);
        }
        deleteStmt.executeUpdate();
    }

    public String defineTableSQL() {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE ");
        sqlBuilder.append(getTableName());
        sqlBuilder.append("( ");
        for (KeyMetadata keyMetadata : keys.values()) {
            sqlBuilder.append(getColumnDefinitionSQL(keyMetadata.getKey(), keyMetadata.getType()));
            sqlBuilder.append(",");
        }
        String primaryKey = getTableName() + "_ID";
        sqlBuilder.append(primaryKey);
        sqlBuilder.append(" BIGINT NOT NULL AUTO_INCREMENT,");
        sqlBuilder.append("PRIMARY KEY (");
        sqlBuilder.append(primaryKey);
        sqlBuilder.append("),");
        for (KeyMetadata keyMetadata : keys.values()) {
            if (keyMetadata.getType() == Value.STRING || keyMetadata.getType() == Value.DATE) {
                sqlBuilder.append("INDEX (");
                String column = keyMetadata.getKey().toSQL();
                sqlBuilder.append(column);
                sqlBuilder.append(")");
                sqlBuilder.append(",");
            }
            if (keyMetadata.getType() == Value.DATE) {
                sqlBuilder.append("INDEX (");
                String column = "datedim_" + keyMetadata.getKey().getKeyID() + "_id";
                sqlBuilder.append(column);
                sqlBuilder.append(")");
                sqlBuilder.append(",");
            }
        }
        if (sqlBuilder.charAt(sqlBuilder.length() - 1) == ',') sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        sqlBuilder.append(" )");
        return sqlBuilder.toString();
    }

    private String getColumnDefinitionSQL(Key key, int type) {
        String column;
        if (type == Value.DATE) {
            column = "k" + key.getKeyID() + " DATETIME, datedim_" + key.getKeyID() + "_id BIGINT(11)";
        } else if (type == Value.NUMBER) {
            column = "k" + key.getKeyID() + " DOUBLE";
        } else {
            column = "k" + key.getKeyID() + " VARCHAR(255)";
        }
        return column;
    }

    private String getTableName() {
        return "df" + feedID + "v" + version;
    }

    private static class KeyMetadata {
        private Key key;
        private int type;
        private AnalysisItem analysisItem;

        private KeyMetadata(Key key, int type, AnalysisItem analysisItem) {
            this.key = key;
            this.type = type;
            this.analysisItem = analysisItem;
        }

        public Key getKey() {
            return key;
        }

        public void setKey(Key key) {
            this.key = key;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public AnalysisItem getAnalysisItem() {
            return analysisItem;
        }

        public void setAnalysisItem(AnalysisItem analysisItem) {
            this.analysisItem = analysisItem;
        }

        public String createInsertClause() {
            if (type == Value.DATE) {
                return key.toSQL() + ", datedim_" + key.getKeyID() + "_id";
            } else {
                return key.toSQL();
            }
        }

        public String createUpdateClause() {
            if (type == Value.DATE) {
                return key.toSQL() + " = ?, datedim_" + key.getKeyID() + "_id = ?";
            } else {
                return key.toSQL() + " = ?";
            }
        }

        public String createInsertQuestionMarks() {
            if (type == Value.DATE) {
                return "?, ?";
            } else {
                return "?";
            }
        }
    }

    private static void addOrUpdateMetadata(long dataFeedID, FeedPersistenceMetadata metadata, Connection conn) {
        try {
            if (metadata.getMetadataID() > 0) {
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE FEED_PERSISTENCE_METADATA SET SIZE = ?, VERSION = ?, DATABASE_NAME = ?," +
                        "LAST_DATA_TIME = ? WHERE " +
                        "FEED_PERSISTENCE_METADATA_ID = ?");
                updateStmt.setLong(1, metadata.getSize());
                updateStmt.setLong(2, metadata.getVersion());
                updateStmt.setString(3, metadata.getDatabase());
                updateStmt.setTimestamp(4, new Timestamp(metadata.getLastData().getTime()));
                updateStmt.setLong(5, metadata.getMetadataID());
                updateStmt.executeUpdate();
            } else {
                PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO FEED_PERSISTENCE_METADATA (FEED_ID, " +
                        "VERSION, SIZE, DATABASE_NAME, LAST_DATA_TIME) VALUES (?, ?, ?, ?, ?)");
                insertStmt.setLong(1, dataFeedID);
                insertStmt.setInt(2, metadata.getVersion());
                insertStmt.setLong(3, metadata.getSize());
                insertStmt.setString(4, metadata.getDatabase());
                insertStmt.setTimestamp(5, new Timestamp(metadata.getLastData().getTime()));
                insertStmt.execute();
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public static void addOrUpdateMetadata(long dataFeedID, FeedPersistenceMetadata metadata) {
        Connection conn = Database.instance().getConnection();
        try {
            addOrUpdateMetadata(dataFeedID, metadata, conn);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public static void liveDataSource(long dataSourceID, Connection conn) {
        FeedPersistenceMetadata metadata = new FeedPersistenceMetadata();
        metadata.setVersion(1);
        metadata.setLastData(new Date());
        addOrUpdateMetadata(dataSourceID, metadata, conn);
    }

    private static FeedPersistenceMetadata createDefaultMetadata(Connection conn) {
        try {
            FeedPersistenceMetadata metadata = new FeedPersistenceMetadata();
            metadata.setVersion(1);
            metadata.setLastData(new Date());
            metadata.setDatabase(DatabaseManager.instance().chooseDatabase(conn));
            return metadata;
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private static FeedPersistenceMetadata getMetadata(long dataFeedID, Connection conn) {
        FeedPersistenceMetadata metadata = null;
        try {
            PreparedStatement versionStmt = conn.prepareStatement("SELECT MAX(VERSION) FROM FEED_PERSISTENCE_METADATA WHERE " +
                    "FEED_ID = ?");
            versionStmt.setLong(1, dataFeedID);
            ResultSet versionRS = versionStmt.executeQuery();
            if (versionRS.next()) {
                long version = versionRS.getLong(1);
                PreparedStatement queryStmt = conn.prepareStatement("SELECT FEED_PERSISTENCE_METADATA_ID, SIZE, VERSION, DATABASE_NAME, LAST_DATA_TIME " +
                        "FROM FEED_PERSISTENCE_METADATA WHERE FEED_ID = ? AND VERSION = ?");
                queryStmt.setLong(1, dataFeedID);
                queryStmt.setLong(2, version);
                ResultSet rs = queryStmt.executeQuery();
                if (rs.next()) {
                    long metadataID = rs.getLong(1);
                    metadata = new FeedPersistenceMetadata();
                    metadata.setMetadataID(metadataID);
                    metadata.setSize(rs.getLong(2));
                    metadata.setVersion(rs.getInt(3));
                    metadata.setDatabase(rs.getString(4));
                    metadata.setLastData(rs.getTimestamp(5));
                }
            } else {
                throw new RuntimeException("No metadata found for " + dataFeedID);
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return metadata;
    }

    private static void deleteMetadata(long feedID, Connection conn) throws SQLException {
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM feed_persistence_metadata WHERE feed_id = ?");
        deleteStmt.setLong(1, feedID);
        deleteStmt.executeUpdate();
    }

    public DataSet allData(Collection<FilterDefinition> filters) throws SQLException {
        InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT ");
        for (Map.Entry<Key, KeyMetadata> keyEntry : keys.entrySet()) {
            sqlBuilder.append(keyEntry.getKey().toSQL());
            sqlBuilder.append(",");
        }
        sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        sqlBuilder.append(" FROM ");
        sqlBuilder.append(getTableName());
        sqlBuilder.append(" WHERE ");
        for (FilterDefinition filterDefinition : filters) {
            sqlBuilder.append(filterDefinition.toQuerySQL(getTableName()));
            sqlBuilder.append(",");
        }
        sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        sqlBuilder.append(" LIMIT 10");
        DataSet dataSet = new DataSet();
        PreparedStatement queryStmt = storageConn.prepareStatement(sqlBuilder.toString());
        int position = 1;
        for (FilterDefinition filterDefinition : filters) {
            KeyMetadata keyMetadata = keys.get(filterDefinition.getField().getKey());
            int type = keyMetadata.type;
            filterDefinition.populatePreparedStatement(queryStmt, position, type, insightRequestMetadata);
        }
        System.out.println(sqlBuilder.toString());
        ResultSet dataRS = queryStmt.executeQuery();
        while (dataRS.next()) {
            int i = 1;
            IRow row = dataSet.createRow();
            for (Map.Entry<Key, KeyMetadata> keyEntry : keys.entrySet()) {
                Key key = keyEntry.getKey();
                KeyMetadata keyMetadata = keyEntry.getValue();
                if (keyMetadata.getType() == Value.DATE) {
                    Timestamp time = dataRS.getTimestamp(i++);
                    if (dataRS.wasNull()) {
                        row.addValue(key, new EmptyValue());
                    } else {
                        row.addValue(key, new DateValue(new Date(time.getTime())));
                    }
                } else if (keyMetadata.getType() == Value.NUMBER) {
                    double value = dataRS.getDouble(i++);
                    if (dataRS.wasNull()) {
                        row.addValue(key, new EmptyValue());
                    } else {
                        row.addValue(key, new NumericValue(value));
                    }
                } else {
                    String value = dataRS.getString(i++);
                    if (dataRS.wasNull()) {
                        row.addValue(key, new EmptyValue());
                    } else {
                        row.addValue(key, new StringValue(value));
                    }
                }
            }
        }
        return dataSet;
    }
}
