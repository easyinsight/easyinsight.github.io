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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 9/9/11
 * Time: 10:27 AM
 */
public class TempStorage implements IDataStorage {

    private Map<Key, KeyMetadata> keys;
    private Database storageDatabase;
    private static DateDimCache dateDimCache = new DateDimCache();

    private String tableName;

    public TempStorage(long feedID, Map<Key, KeyMetadata> keys, Database storageDatabase) {
        this.keys = keys;
        tableName = "dt" + feedID + System.currentTimeMillis();
        this.storageDatabase = storageDatabase;
    }

    public String getTableName() {
        return tableName;
    }

    public void createTable(String sql) throws SQLException {
        EIConnection storageConn = storageDatabase.getConnection();
        try {
            ResultSet existsRS = storageConn.getMetaData().getTables(null, null, tableName, null);
            if (existsRS.next()) {
                storageConn.prepareStatement("DROP TABLE " + tableName).execute();
            }
            PreparedStatement createSQL = storageConn.prepareStatement(sql);
            createSQL.execute();
        } finally {
            Database.closeConnection(storageConn);
        }
    }

    public String defineTempInsertTable() {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE ");
        sqlBuilder.append(tableName);
        sqlBuilder.append("( ");
        for (KeyMetadata keyMetadata : keys.values()) {
            sqlBuilder.append(getColumnDefinitionSQL(keyMetadata.getKey(), keyMetadata.getType()));
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
            sqlBuilder.append(getColumnDefinitionSQL(keyMetadata.getKey(), keyMetadata.getType()));
            sqlBuilder.append(",");
        }
        sqlBuilder.append("update_key_field varchar(255)");
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
        } else if (type == Value.TEXT) {
            column = "k" + key.getKeyID() + " TEXT";
        } else {
            column = "k" + key.getKeyID() + " VARCHAR(255)";
        }
        return column;
    }

    public void commit() throws SQLException {

    }

    public void insertData(DataSet dataSet) throws Exception {
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

        try {
            PreparedStatement insertStmt = storageConn.prepareStatement(insertSQL);
            for (IRow row : dataSet.getRows()) {
                int i = 1;
                for (KeyMetadata keyMetadata : keys.values()) {
                    i = setValue(insertStmt, row, i, keyMetadata, storageConn);
                }
                insertStmt.execute();
            }
            insertStmt.close();
        } finally {
            Database.closeConnection(storageConn);
        }
    }

    public void updateData(IRow row, List<IWhere> wheres) throws Exception {
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

            int i = 1;
            for (KeyMetadata keyMetadata : keys.values()) {
                i = setValue(insertStmt, row, i, keyMetadata, storageConn);
            }
            insertStmt.setString(i, where.getValue());
            insertStmt.execute();

            insertStmt.close();
        } finally {
            Database.closeConnection(storageConn);
        }
    }

    public void updateData(DataSet dataSet, List<IWhere> wheres) throws Exception {
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
                Value transformedValue = analysisItem.transformValue(value, new InsightRequestMetadata(), false);
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
            if (date == null) {
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
                if (keyMetadata.getType() == Value.STRING && string.length() > 253) {
                    string = string.substring(0, 253);
                }
                insertStmt.setString(i++, string);
            }
        }
        return i;
    }
}
