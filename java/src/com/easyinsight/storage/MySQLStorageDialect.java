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
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 4/10/14
 * Time: 1:56 PM
 */
public class MySQLStorageDialect implements IStorageDialect {

    private String tableName;
    private Map<Key, KeyMetadata> keys;

    public MySQLStorageDialect(String tableName, Map<Key, KeyMetadata> keys) {
        this.tableName = tableName;
        this.keys = keys;
    }

    private int maxLen = 255;

    public void createTempTable(String sql, Database database) throws SQLException {
        EIConnection storageConn = database.getConnection();
        try {
            ResultSet existsRS = storageConn.getMetaData().getTables(null, null, tableName, null);
            if (existsRS.next()) {
                storageConn.prepareStatement("DROP TABLE " + tableName).execute();
            }
            try {
                PreparedStatement createSQL = storageConn.prepareStatement(sql);
                createSQL.execute();
            } catch (SQLException e) {
                if (e.getMessage().contains("Row size too large")) {
                    maxLen = 100;
                    String nextTry = defineTempInsertTable();
                    PreparedStatement createSQL = storageConn.prepareStatement(nextTry);
                    createSQL.execute();
                } else {
                    throw e;
                }
            }
        } finally {
            Database.closeConnection(storageConn);
        }
    }

    private int setValue(PreparedStatement insertStmt, IRow row, int i, KeyMetadata keyMetadata, Connection storageConn, Database storageDatabase, DateDimCache dateDimCache) throws SQLException {
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

    public void insertData(DataSet dataSet, List<IDataTransform> transforms, EIConnection coreDBConn, Database storageDatabase, DateDimCache dateDimCache) throws Exception {
        for (IRow row : dataSet.getRows()) {
            for (IDataTransform transform : transforms) {
                transform.handle(coreDBConn, row);
            }
        }
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
        String insertSQL = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + parameters + ")";
        EIConnection storageConn = storageDatabase.getConnection();
        storageConn.setAutoCommit(false);
        try {
            PreparedStatement insertStmt = storageConn.prepareStatement(insertSQL);
            int counter = 0;
            for (IRow row : dataSet.getRows()) {
                int i = 1;
                for (KeyMetadata keyMetadata : keys.values()) {
                    i = setValue(insertStmt, row, i, keyMetadata, storageConn, storageDatabase, dateDimCache);
                }
                insertStmt.execute();
                counter++;
                /*if (counter == 1000) {
                    counter = 0;
                    insertStmt.executeBatch();
                }*/
            }
            insertStmt.close();
            storageConn.commit();
        } catch (Exception e) {
            storageConn.rollback();
            if (e.getMessage() != null && e.getMessage().contains("Data truncated")) {
                PreparedStatement insertStmt = storageConn.prepareStatement(insertSQL);
                for (IRow row : dataSet.getRows()) {
                    int i = 1;
                    for (KeyMetadata keyMetadata : keys.values()) {
                        i = setValue(insertStmt, row, i, keyMetadata, storageConn, storageDatabase, dateDimCache);
                    }
                    try {
                        insertStmt.execute();
                    } catch (SQLException e1) {
                        if (e1.getMessage() != null && e.getMessage().contains("Data truncated")) {
                            LogClass.info(e1.getMessage());
                        } else {
                            throw e1;
                        }
                    }
                }
                insertStmt.close();
                storageConn.commit();
            }
        } finally {
            storageConn.setAutoCommit(true);
            Database.closeConnection(storageConn);
        }
    }

    public void commit() {

    }

    private String getTempColumnDefinitionSQL(Key key, int type) {
        String column;
        if (type == Value.DATE) {
            column = "k" + key.getKeyID() + " DATETIME, datedim_" + key.getKeyID() + "_id BIGINT(11)";
        } else if (type == Value.NUMBER) {
            column = "k" + key.getKeyID() + " DOUBLE";
        } else if (type == Value.TEXT) {
            column = "k" + key.getKeyID() + " TEXT";
        } else {
            column = "k" + key.getKeyID() + " VARCHAR("+maxLen+")";
        }
        return column;
    }

    public String defineTempInsertTable() {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE ");
        sqlBuilder.append(tableName);
        sqlBuilder.append("( ");
        for (KeyMetadata keyMetadata : keys.values()) {
            sqlBuilder.append(getTempColumnDefinitionSQL(keyMetadata.getKey(), keyMetadata.getType()));
            sqlBuilder.append(",");
        }
        if (sqlBuilder.charAt(sqlBuilder.length() - 1) == ',') sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        sqlBuilder.append(" )");
        return sqlBuilder.toString();
    }

    public String defineTempUpdateTable() {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE ");
        sqlBuilder.append(tableName);
        sqlBuilder.append("( ");
        for (KeyMetadata keyMetadata : keys.values()) {
            sqlBuilder.append(getTempColumnDefinitionSQL(keyMetadata.getKey(), keyMetadata.getType()));
            sqlBuilder.append(",");
        }
        sqlBuilder.append("update_key_field varchar(255),");
        sqlBuilder.append("index(update_key_field),");
        if (sqlBuilder.charAt(sqlBuilder.length() - 1) == ',') sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        sqlBuilder.append(" )");
        return sqlBuilder.toString();
    }

    public String defineTableSQL(boolean hugeTable) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE ");
        sqlBuilder.append(tableName);
        sqlBuilder.append("( ");
        for (KeyMetadata keyMetadata : keys.values()) {
            sqlBuilder.append(getColumnDefinitionSQL(keyMetadata.getKey(), keyMetadata.getType(), hugeTable));
            sqlBuilder.append(",");
        }
        String primaryKey = tableName + "_ID";
        sqlBuilder.append(primaryKey);
        sqlBuilder.append(" BIGINT NOT NULL AUTO_INCREMENT,");
        sqlBuilder.append("PRIMARY KEY (");
        sqlBuilder.append(primaryKey);
        sqlBuilder.append("),");
        int indexCount = 0;
        for (KeyMetadata keyMetadata : keys.values()) {
            if (!hugeTable && keyMetadata.getType() == Value.STRING) {
                sqlBuilder.append("INDEX (");
                String column = keyMetadata.getKey().toSQL();
                sqlBuilder.append(column);
                sqlBuilder.append(")");
                sqlBuilder.append(",");
                indexCount++;
            } else if (keyMetadata.getType() == Value.DATE) {
                sqlBuilder.append("INDEX (");
                String column = keyMetadata.getKey().toSQL();
                sqlBuilder.append(column);
                sqlBuilder.append(")");
                sqlBuilder.append(",");
                indexCount++;
            }
            if (keyMetadata.getType() == Value.DATE) {
                sqlBuilder.append("INDEX (");
                String column = "datedim_" + keyMetadata.getKey().getKeyID() + "_id";
                sqlBuilder.append(column);
                sqlBuilder.append(")");
                sqlBuilder.append(",");
                indexCount++;
            }
            if (indexCount >= 60) {
                break;
            }
        }
        if (sqlBuilder.charAt(sqlBuilder.length() - 1) == ',') sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        sqlBuilder.append(" )");
        //sqlBuilder.append(" ) CHARSET=utf8");
        return sqlBuilder.toString();
    }

    public String getColumnDefinitionSQL(Key key, int type, boolean hugeTable) {
        String column;
        if (type == Value.DATE) {
            column = "k" + key.getKeyID() + " DATETIME, datedim_" + key.getKeyID() + "_id BIGINT(11)";
        } else if (type == Value.NUMBER) {
            column = "k" + key.getKeyID() + " DOUBLE";
        } else if (type == Value.TEXT) {
            column = "k" + key.getKeyID() + " TEXT";
        } else {
            if (hugeTable) {
                column = "k" + key.getKeyID() + " TEXT";
            } else {
                column = "k" + key.getKeyID() + " VARCHAR(255)";
            }
        }
        return column;
    }
}
