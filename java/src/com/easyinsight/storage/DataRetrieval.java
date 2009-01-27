package com.easyinsight.storage;

import com.easyinsight.dataset.PersistableDataSetForm;
import com.easyinsight.dataset.ColumnSegment;
import com.easyinsight.database.Database;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.logging.LogClass;
import com.easyinsight.datafeeds.FeedPersistenceMetadata;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.*;
import java.io.*;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * User: James Boe
 * Date: Apr 7, 2008
 * Time: 11:55:01 AM
 */
public class DataRetrieval implements IDataRetrieval {

    private DataSource dataSource;
    private static DataRetrieval instance;

    public DataRetrieval() {
        dataSource = setupDataSource();
        instance = this;
    }

    public static DataRetrieval instance() {
        return instance;
    }

    private Connection getConnection() {
        try {                                           
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void storeData(long dataFeedID, PersistableDataSetForm dataSet) {
        long size = 0;
        Connection conn = getConnection();
        try {
            /*FeedPersistenceMetadata metadata = getMetadata(dataFeedID);
            if (metadata == null) {
                metadata = createDefaultMetadata();
            } else {
                metadata.setVersion(metadata.getVersion() + 1);
            } */
            PreparedStatement alreadyThereStmt = conn.prepareStatement("SELECT DATA_FEED_ID FROM DATA_STORE WHERE DATA_FEED_ID = ?");
            PreparedStatement insertColumnStmt = conn.prepareStatement("INSERT INTO DATA_STORE (DATA_FEED_ID, COLUMN_KEY, DATA_SET) " +
                    "VALUES (?, ?, ?)");
            alreadyThereStmt.setLong(1, dataFeedID);
            ResultSet existsRS = alreadyThereStmt.executeQuery();
            if (existsRS.next()) {
                PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM DATA_STORE WHERE DATA_FEED_ID = ?");
                deleteStmt.setLong(1, dataFeedID);
                deleteStmt.executeUpdate();
            }
            for (Map.Entry<Key, ColumnSegment> entry : dataSet.getColumnSegmentMap().entrySet()) {
                insertColumnStmt.setLong(1, dataFeedID);
                if (entry.getKey() == null || entry.getKey().getKeyID() == null) {
                    throw new RuntimeException("Attempt made to persist data segment before the matching Key has been persisted to create an ID.");
                }
                insertColumnStmt.setLong(2, entry.getKey().getKeyID());
                byte[] byteArray = getBytes(entry.getValue());
                size += byteArray.length;
                ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
                insertColumnStmt.setBinaryStream(3, bais, byteArray.length);
                insertColumnStmt.execute();
            }
            alreadyThereStmt.close();
            insertColumnStmt.close();
            //metadata.setSize(size);
            //addOrUpdateMetadata(dataFeedID, metadata, conn);
        } catch (Exception se) {
            LogClass.error(se);
            throw new RuntimeException(se);
        } finally {
            Database.instance().closeConnection(conn);
        }
        //return size;
    }

    public void deleteData(long dataFeedID) {
        try {
            Connection conn = getConnection();
            try {
                PreparedStatement alreadyThereStmt = conn.prepareStatement("DELETE FROM DATA_STORE WHERE DATA_FEED_ID = ?");
                alreadyThereStmt.setLong(1, dataFeedID);
                alreadyThereStmt.executeUpdate();
                alreadyThereStmt.close();
            } catch (SQLException se) {
                LogClass.error(se);
            } finally {
                conn.close();
            }
        } catch (Exception e) {
            LogClass.error(e);
        }
    }

    private byte[] getBytes(ColumnSegment columnSegment) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(columnSegment);
        oos.flush();
        oos.close();
        return baos.toByteArray();
    }

    // separate data source here...
    
    private DataSource setupDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.gjt.mm.mysql.Driver");
        ds.setUsername("dms");
        ds.setPassword("dms");
        ds.setUrl("jdbc:mysql://localhost:3306/dms");
        return ds;
    }

    public ColumnSegment getSegment(long feedID, Key column) {
        try {
            Connection conn = getConnection();
            try {
                Statement queryStmt = conn.createStatement();
                ResultSet storeRS = queryStmt.executeQuery("SELECT DATA_SET FROM DATA_STORE WHERE DATA_FEED_ID = " + feedID + " AND " +
                        "COLUMN_KEY = '" + column.getKeyID() + "'");
                /*queryStmt.clearParameters();
                queryStmt.setLong(1, feedID);
                queryStmt.setString(2, column);
                ResultSet storeRS = queryStmt.executeQuery();*/
                List<ColumnSegment> columnSegments = new ArrayList<ColumnSegment>();
                while (storeRS.next()) {
                    byte[] bytes = storeRS.getBytes(1);
                    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    ColumnSegment columnSegment = (ColumnSegment) ois.readObject();
                    ois.close();
                    bais.close();
                    columnSegments.add(columnSegment);
                }
                if (columnSegments.size() == 1) {
                    return columnSegments.get(0);
                } else {
                    int totalSize = 0;
                    for (ColumnSegment columnSegment : columnSegments) {
                        totalSize += columnSegment.getValues().length;
                    }
                    Value[] totalValues = new Value[totalSize];
                    int targetPosition = 0;
                    for (ColumnSegment columnSegment : columnSegments) {
                        System.arraycopy(columnSegment.getValues(), 0, totalValues, targetPosition, columnSegment.getValues().length);
                        targetPosition += columnSegment.getValues().length;
                    }
                    return new ColumnSegment(totalValues);
                }
            } finally {
                conn.close();
            }
        } catch (Exception e) {
            LogClass.error(e);
        }
        return null;
    }

    public void appendData(long feedID, PersistableDataSetForm dataSet) {
        Connection conn = getConnection();
        try {
            /*FeedPersistenceMetadata metadata = getMetadata(feedID);
            if (metadata == null) {
                throw new RuntimeException("Trying to append data to a feed which has never received any data");
            }
            long size = metadata.getSize();*/
            PreparedStatement insertColumnStmt = conn.prepareStatement("UPDATE DATA_STORE SET DATA_SET = ? WHERE COLUMN_KEY = ? AND DATA_FEED_ID = ?");
            for (Map.Entry<Key, ColumnSegment> entry : dataSet.getColumnSegmentMap().entrySet()) {
                if (entry.getKey() == null || entry.getKey().getKeyID() == null) {
                    throw new RuntimeException("Attempt made to persist data segment before the matching Key has been persisted to create an ID.");
                }
                ColumnSegment existingSegment = getSegment(feedID, entry.getKey());
                Value[] existingValues = existingSegment.getValues();
                Value[] newValues = entry.getValue().getValues();
                Value[] newArray = new Value[existingValues.length + newValues.length];
                System.arraycopy(existingValues, 0, newArray, 0, existingValues.length);
                System.arraycopy(newValues, 0, newArray, existingValues.length, newValues.length);
                entry.getValue().setValues(newArray);
                byte[] byteArray = getBytes(entry.getValue());
                //size += byteArray.length;
                ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
                insertColumnStmt.setBinaryStream(1, bais, byteArray.length);
                insertColumnStmt.setLong(2, entry.getKey().getKeyID());
                insertColumnStmt.setLong(3, feedID);
                insertColumnStmt.executeUpdate();
            }
            insertColumnStmt.close();
           // metadata.setSize(size);
           // addOrUpdateMetadata(feedID, metadata, conn);
        } catch (Exception se) {
            LogClass.error(se);
            throw new RuntimeException(se);
        } finally {
            Database.instance().closeConnection(conn);
        }
        //return size;
    }


}
