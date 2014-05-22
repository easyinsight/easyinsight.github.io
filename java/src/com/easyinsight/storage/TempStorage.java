package com.easyinsight.storage;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.InsightRequestMetadata;
import com.easyinsight.core.*;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;

import java.sql.*;
import java.util.*;

/**
 * User: jamesboe
 * Date: 9/9/11
 * Time: 10:27 AM
 */
public class TempStorage implements IDataStorage {

    private Map<Key, KeyMetadata> keys;
    private Database storageDatabase;
    private static DateDimCache dateDimCache = new DateDimCache();
    private List<AnalysisItem> cachedCalculations = new ArrayList<AnalysisItem>();
    private EIConnection coreDBConn;

    private List<IDataTransform> transforms = new ArrayList<IDataTransform>();

    private String tableName;
    private Key distKey;

    public TempStorage(long feedID, Map<Key, KeyMetadata> keys, Database storageDatabase, List<AnalysisItem> cachedCalculations, List<IDataTransform> transforms,
                       EIConnection conn, Key distKey) {
        this.keys = keys;
        this.tableName = "dt" + feedID + System.currentTimeMillis();
        this.storageDatabase = storageDatabase;
        this.cachedCalculations = cachedCalculations;
        this.transforms = transforms;
        this.coreDBConn = conn;
        this.distKey = distKey;
    }

    public TempStorage(Map<Key, KeyMetadata> keys, Database storageDatabase, String tableName) {
        this.keys = keys;
        this.tableName = tableName;
        this.storageDatabase = storageDatabase;
    }

    public List<AnalysisItem> getCachedCalculations() {
        return cachedCalculations;
    }

    public void setCachedCalculations(List<AnalysisItem> cachedCalculations) {
        this.cachedCalculations = cachedCalculations;
    }

    public List<IDataTransform> getTransforms() {
        return transforms;
    }

    public void setTransforms(List<IDataTransform> transforms) {
        this.transforms = transforms;
    }

    public String getTableName() {
        return tableName;
    }

    private int maxLen = 255;

    public void createTable(String sql, boolean insert) throws SQLException {
        getStorageDialect(getTableName()).createTempTable(sql, storageDatabase, insert);
    }

    private IStorageDialect dialect;

    private IStorageDialect getStorageDialect(String tableName) {
        if (dialect == null) {
            if (storageDatabase.getDialect() == Database.MYSQL) {
                dialect = new MySQLStorageDialect(tableName, keys);
            } else if (storageDatabase.getDialect() == Database.POSTGRES) {
                dialect = new AltPostgresStorageDialect(tableName, keys, distKey);
            } else {
                throw new RuntimeException();
            }
        }
        return dialect;
    }

    public String defineTempInsertTable() {
        return getStorageDialect(getTableName()).defineTempInsertTable();
    }

    public String defineTempUpdateTable() {
        return getStorageDialect(getTableName()).defineTempUpdateTable();
    }

    public void commit() throws SQLException {
        getStorageDialect(getTableName()).commit();
    }

    public void insertData(DataSet dataSet) throws Exception {
        getStorageDialect(getTableName()).insertData(dataSet, transforms, coreDBConn, storageDatabase, dateDimCache);
    }

    public void updateData(DataSet dataSet, List<IWhere> wheres) throws Exception {
        IStorageDialect dialect = getStorageDialect(getTableName());
        if (dialect instanceof AltPostgresStorageDialect) {
            dialect.insertData(dataSet, transforms, coreDBConn, storageDatabase, dateDimCache);
        } else {
            StringWhere where = (StringWhere) wheres.get(0);
            StringBuilder columnBuilder = new StringBuilder();
            StringBuilder paramBuilder = new StringBuilder();
            for (KeyMetadata keyMetadata : keys.values()) {
                columnBuilder.append(keyMetadata.createInsertClause());
                paramBuilder.append(keyMetadata.createInsertQuestionMarks());
                columnBuilder.append(",");
                paramBuilder.append(",");
            }
            columnBuilder.append("update_key_field");
            paramBuilder.append("?");
            String columns = columnBuilder.toString();
            String parameters = paramBuilder.toString();
            String insertSQL = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + parameters + ")";
            EIConnection storageConn = storageDatabase.getConnection();

            try {
                PreparedStatement insertStmt = storageConn.prepareStatement(insertSQL);
                for (IRow row : dataSet.getRows()) {
                    int i = 1;
                    for (KeyMetadata keyMetadata : keys.values()) {
                        i = setValue(insertStmt, row, i, keyMetadata, storageConn);
                    }
                    insertStmt.setString(i, where.getValue());
                    insertStmt.execute();
                }
                insertStmt.close();
            } finally {
                Database.closeConnection(storageConn);
            }
        }
    }

    private int setValue(PreparedStatement insertStmt, IRow row, int i, KeyMetadata keyMetadata, Connection storageConn) throws SQLException {
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
                int prevLevel = analysisDateDimension.getDateLevel();
                analysisDateDimension.setDateLevel(AnalysisDateDimension.DAY_LEVEL);
                Calendar calendar = Calendar.getInstance();
                Value transformedValue = analysisItem.transformValue(value, new InsightRequestMetadata(), false, calendar);
                analysisDateDimension.setDateLevel(prevLevel);
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
            if (date == null || storageDatabase.getDialect() == Database.POSTGRES) {
                insertStmt.setNull(i++, Types.BIGINT);
            } else {
                try {
                    long id = dateDimCache.getDateDimID(date, storageConn);
                    insertStmt.setLong(i++, id);
                } catch (SQLException e) {
                    LogClass.info(e.getMessage());
                    insertStmt.setNull(i++, Types.BIGINT);
                }
            }
        } else if (keyMetadata.getType() == Value.NUMBER) {
            Double num = null;
            if (value.type() == Value.STRING || value.type() == Value.TEXT) {
                num = NumericValue.produceDoubleValue(value.toString());
            } else if (value.type() == Value.NUMBER) {
                NumericValue numericValue = (NumericValue) value;
                num = numericValue.toDouble();
            }
            if (num == null || Double.isNaN(num) || Double.isInfinite(num)) {
                insertStmt.setNull(i++, Types.DOUBLE);
            } else {
                insertStmt.setDouble(i++, num);
            }
        } else {
            String string = null;
            if (value.type() == Value.STRING || value.type() == Value.TEXT) {
                StringValue stringValue = (StringValue) value;
                string = stringValue.getValue();
            } else if (value.type() == Value.NUMBER) {
                NumericValue numericValue = (NumericValue) value;
                string = numericValue.toString();
            } else if (value.type() == Value.DATE) {
                DateValue dateValue = (DateValue) value;
                if (dateValue.getDate() != null) {
                    string = dateValue.getDate().toString();
                }
            }
            if (string == null) {
                insertStmt.setNull(i++, Types.VARCHAR);
            } else {
                if (keyMetadata.getType() == Value.STRING && string.length() > (maxLen - 2)) {
                    string = string.substring(0, maxLen - 2);
                } else if (keyMetadata.getType() == Value.TEXT && string.length() > 65530) {
                    string = string.substring(0, 65530);
                }
                insertStmt.setString(i++, string);
            }
        }
        return i;
    }
}
