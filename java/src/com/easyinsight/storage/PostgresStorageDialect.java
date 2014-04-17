package com.easyinsight.storage;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.csvreader.CsvWriter;
import com.easyinsight.analysis.IRow;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.userupload.FlatFileUploadContext;

import java.io.*;
import java.nio.charset.Charset;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

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

    private String fileName;

    private CsvWriter csvWriter;
    private List<File> files = new ArrayList<File>();
    private File file;
    private FileOutputStream fos;
    private int rows = 0;

    public void insertData(DataSet dataSet, List<IDataTransform> transforms, EIConnection coreDBConn, Database storageDatabase, DateDimCache dateDimCache) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (IRow row : dataSet.getRows()) {
            rows++;
            String[] rowValues = new String[keys.size()];
            int j = 0;
            for (Key key : keys.keySet()) {
                Value value = row.getValue(key);
                if (value.type() == Value.DATE) {
                    rowValues[j++] = sdf.format(((DateValue) value).getDate());
                } else {
                    rowValues[j++] = String.valueOf(value.toString());
                }
            }
            csvWriter.writeRecord(rowValues);
        }
        csvWriter.flush();
        if (rows > 10000000) {
            rows = 0;
            csvWriter.flush();
            csvWriter.close();
            fos.close();
            files.add(file);
            System.out.println("starting new file of " + tableName + files.size() + ".csv" );
            file = new File(tableName + files.size() + ".csv");
            fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos, 512);
            csvWriter = new CsvWriter(bos, ',', Charset.forName("UTF-8"));
        }
    }

    public void commit() {
        try {
            csvWriter.flush();
            csvWriter.close();
            fos.close();

            files.add(this.file);

            AmazonS3 s3 = new AmazonS3Client(new BasicAWSCredentials("0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI"));
            String bucketName = "refresh" + tableName;
            s3.createBucket(bucketName);

            for (File file : files) {

                ByteArrayOutputStream archiveStream = new ByteArrayOutputStream();

                GZIPOutputStream zos = new GZIPOutputStream(archiveStream);
                BufferedOutputStream bufOS = new BufferedOutputStream(zos, 1024);
                byte[] buffer = new byte[1024];
                InputStream bfis = new FileInputStream(file);
                int nBytes;
                while ((nBytes = bfis.read(buffer)) != -1) {
                    bufOS.write(buffer, 0, nBytes);
                }
                bufOS.flush();
                zos.flush();

                bfis.close();

                archiveStream.flush();

                zos.close();

                archiveStream.close();

                byte[] bytes = archiveStream.toByteArray();
                ByteArrayInputStream stream = new ByteArrayInputStream(bytes);

                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentLength(bytes.length);
                s3.putObject(new PutObjectRequest(bucketName, fileName, stream, objectMetadata));
                boolean success = file.delete();
                if (!success) {
                    LogClass.error("Could not delete " + fileName);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        sqlBuilder.append(" int identity,");
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
        try {
            fileName = tableName + ".csv";

            file = new File(fileName);
            fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos, 512);
            csvWriter = new CsvWriter(bos, ',', Charset.forName("UTF-8"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }



    public String defineTempInsertTable() {
        return "";
    }

    public String defineTempUpdateTable() {
        return "";
    }
}
