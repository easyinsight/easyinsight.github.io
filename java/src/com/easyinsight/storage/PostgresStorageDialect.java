package com.easyinsight.storage;

import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 4/10/14
 * Time: 1:56 PM
 */
public class PostgresStorageDialect implements IStorageDialect {

    private String tableName;
    private Map<Key, KeyMetadata> keys;

    public PostgresStorageDialect(String tableName, Map<Key, KeyMetadata> keys) {
        this.tableName = tableName;
        this.keys = keys;
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
        sqlBuilder.append(" SERIAL PRIMARY KEY,");
        int indexCount = 0;
        /*for (KeyMetadata keyMetadata : keys.values()) {
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
        }*/
        if (sqlBuilder.charAt(sqlBuilder.length() - 1) == ',') sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        sqlBuilder.append(" )");
        //sqlBuilder.append(" ) CHARSET=utf8");
        return sqlBuilder.toString();
    }

    public String getColumnDefinitionSQL(Key key, int type, boolean hugeTable) {
        String column;
        if (type == Value.DATE) {
            column = "k" + key.getKeyID() + " TIMESTAMP, datedim_" + key.getKeyID() + "_id integer";
        } else if (type == Value.NUMBER) {
            column = "k" + key.getKeyID() + " NUMERIC";
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

    private int maxLen = 255;

    public void createTempTable(String sql, Database database) throws SQLException {
        EIConnection storageConn = database.getConnection();
        try {
            ResultSet existsRS = storageConn.getMetaData().getTables(null, null, tableName, null);
            if (existsRS.next()) {
                storageConn.prepareStatement("DROP TABLE " + tableName).execute();
            }
            try {
                System.out.println(sql);
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

    private String getTempColumnDefinitionSQL(Key key, int type) {
        String column;
        if (type == Value.DATE) {
            column = "k" + key.getKeyID() + " TIMESTAMP, datedim_" + key.getKeyID() + "_id INTEGER";
        } else if (type == Value.NUMBER) {
            column = "k" + key.getKeyID() + " NUMERIC";
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
        if (sqlBuilder.charAt(sqlBuilder.length() - 1) == ',') sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        sqlBuilder.append(" )");
        return sqlBuilder.toString();
    }
}
