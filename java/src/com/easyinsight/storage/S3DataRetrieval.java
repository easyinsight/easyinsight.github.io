package com.easyinsight.storage;

import com.easyinsight.dataset.PersistableDataSetForm;
import com.easyinsight.dataset.ColumnSegment;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.logging.LogClass;
import com.easyinsight.datafeeds.FeedPersistenceMetadata;
import com.easyinsight.database.Database;
import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.security.AWSCredentials;

import java.io.*;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * User: James Boe
 * Date: Jul 23, 2008
 * Time: 3:25:13 PM
 */
public class S3DataRetrieval implements IDataRetrieval {

    private S3Service s3Service;
    private S3Bucket bucket;

    private String bucketAppenderName;

    private S3Service getS3Service() {
        if (s3Service == null) {
            try {
                AWSCredentials credentials = new AWSCredentials("0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
                s3Service = new RestS3Service(credentials);
            } catch (S3ServiceException e) {
                throw new RuntimeException(e);
            }
        }
        return s3Service;
    }

    public void storeData(long dataFeedID, PersistableDataSetForm dataSet) {
        long size = 0;
        S3Service service = getS3Service();
        try {
            // challenge here is, how to add new entries...
            // in theory, you break up each chunk by indices
            // so for stock data, you'd want symbol X date segment
            // okay, if there *is* no index...
            // you still need that primary key, basically
            // in theory, you take every grouping
            FeedPersistenceMetadata metadata = getMetadata(dataFeedID);
            if (metadata == null) {
                metadata = createDefaultMetadata();
            } else {
                metadata.setVersion(metadata.getVersion() + 1);
            }
            for (Map.Entry<Key, ColumnSegment> entry : dataSet.getColumnSegmentMap().entrySet()) {
                if (entry.getKey() == null || entry.getKey().getKeyID() == null) {
                    throw new RuntimeException("Attempt made to persist data segment before the matching Key has been persisted to create an ID.");
                }

                size += persistColumnSegment(service, entry.getValue(), dataFeedID, entry.getKey().getKeyID(), metadata.getVersion());
                /*byte[] byteArray = getBytes(entry.getValue());
                LogClass.debug("writing out segment with size = " + byteArray.length);
                size += byteArray.length;
                ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
                S3Bucket bucket = new S3Bucket("eidatafeed" + dataFeedID);
                //service.deleteBucket(bucket);
                service.createBucket(bucket);
                S3Object sObj = new S3Object(bucket, String.valueOf(entry.getKey().getKeyID()));
                sObj.setDataInputStream(bais);
                service.putObject(bucket, sObj);*/
            }
            metadata.setSize(size);
            addOrUpdateMetadata(dataFeedID, metadata);
        } catch (Exception se) {
            LogClass.error(se);
            throw new RuntimeException(se);
        }
    }

    private FeedPersistenceMetadata getMetadata(long dataFeedID) {
        FeedPersistenceMetadata metadata = null;
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT FEED_PERSISTENCE_METADATA_ID, SIZE, VERSION " +
                    "FROM FEED_PERSISTENCE_METADATA WHERE FEED_ID = ?");
            queryStmt.setLong(1, dataFeedID);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                long metadataID = rs.getLong(1);
                metadata = new FeedPersistenceMetadata();
                metadata.setMetadataID(metadataID);
                metadata.setSize(rs.getLong(2));
                metadata.setVersion(rs.getInt(3));
                // TODO: add buckets
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        return metadata;
    }

    public void addOrUpdateMetadata(long dataFeedID, FeedPersistenceMetadata metadata, Connection conn) {
        try {
            if (metadata.getMetadataID() > 0) {
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE FEED_PERSISTENCE_METADATA SET SIZE = ?, VERSION = ? WHERE " +
                        "FEED_PERSISTENCE_METADATA_ID = ?");
                updateStmt.setLong(1, metadata.getSize());
                updateStmt.setLong(2, metadata.getVersion());
                updateStmt.setLong(3, metadata.getMetadataID());
                updateStmt.executeUpdate();
            } else {
                PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO FEED_PERSISTENCE_METADATA (FEED_ID, " +
                        "VERSION, SIZE) VALUES (?, ?, ?)");
                insertStmt.setLong(1, dataFeedID);
                insertStmt.setInt(2, metadata.getVersion());
                insertStmt.setLong(3, metadata.getSize());
                insertStmt.execute();
            }
        } catch (SQLException e) {
           LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void addOrUpdateMetadata(long dataFeedID, FeedPersistenceMetadata metadata) {
        Connection conn = Database.instance().getConnection();
        try {
            addOrUpdateMetadata(dataFeedID, metadata, conn);
        } finally {
            Database.instance().closeConnection(conn);
        }
    }

    private FeedPersistenceMetadata createNewVersion(FeedPersistenceMetadata previousVersion) {
        FeedPersistenceMetadata metadata = new FeedPersistenceMetadata();
        metadata.setVersion(previousVersion.getVersion() + 1);
        return metadata;
    }

    private FeedPersistenceMetadata createDefaultMetadata() {
        FeedPersistenceMetadata metadata = new FeedPersistenceMetadata();
        metadata.setVersion(1);
        return metadata;
    }

    public void appendData(long feedID, PersistableDataSetForm dataSet) {
        S3Service service = getS3Service();
        try {
            FeedPersistenceMetadata metadata = getMetadata(feedID);
            if (metadata == null) {
                throw new RuntimeException("Trying to append data to a feed which has never received any data");
            }
            long size = metadata.getSize();
            for (Map.Entry<Key, ColumnSegment> entry : dataSet.getColumnSegmentMap().entrySet()) {
                if (entry.getKey() == null || entry.getKey().getKeyID() == null) {
                    throw new RuntimeException("Attempt made to persist data segment before the matching Key has been persisted to create an ID.");
                }
                // ColumnSegment existingSegment = getSegment(feedID, entry.getKey());
                size += persistColumnSegment(service, entry.getValue(), feedID, entry.getKey().getKeyID(), metadata.getVersion());
                /*Value[] existingValues = existingSegment.getValues();
                Value[] newValues = entry.getValue().getValues();
                Value[] newArray = new Value[existingValues.length + newValues.length];
                System.arraycopy(existingValues, 0, newArray, 0, existingValues.length);
                System.arraycopy(newValues, 0, newArray, existingValues.length, newValues.length);
                entry.getValue().setValues(newArray);
                byte[] byteArray = getBytes(entry.getValue());
                LogClass.debug("writing out segment with size = " + byteArray.length);
                size += byteArray.length;
                ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
                S3Bucket bucket = new S3Bucket("eidatafeed" + feedID);
                //service.deleteBucket(bucket);
                service.createBucket(bucket);
                S3Object sObj = new S3Object(bucket, String.valueOf(entry.getKey().getKeyID()));
                sObj.setDataInputStream(bais);
                service.putObject(bucket, sObj);*/
            }
            metadata.setSize(size);
        } catch (Exception se) {
            LogClass.error(se);
            throw new RuntimeException(se);
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

    public void deleteData(long dataFeedID) {
        try {
            S3Service service = getS3Service();
            S3Bucket bucket = new S3Bucket("eidatafeed" + dataFeedID);
            service.deleteBucket(bucket);
        } catch (S3ServiceException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public ColumnSegment getSegment(long feedID, Key column) {
        try {
            S3Service service = getS3Service();
            FeedPersistenceMetadata metadata = getMetadata(feedID);
            List<ColumnSegment> columnSegments = getSegments(service, feedID, column.getKeyID(), metadata.getVersion());
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
            /*S3Bucket bucket = new S3Bucket("eidatafeed" + feedID);
            S3Object retrievedObj = service.getObject(bucket, String.valueOf(column.getKeyID()));
            byte retrieveBuf[];
            retrieveBuf = new byte[1];
            InputStream bfis = retrievedObj.getDataInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while (bfis.read(retrieveBuf) != -1) {
                baos.write(retrieveBuf);
            }

            byte[] resultBytes = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(resultBytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            ColumnSegment columnSegment = (ColumnSegment) ois.readObject();
            ois.close();
            bais.close();
            return columnSegment;*/
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private long persistColumnSegment(S3Service service, ColumnSegment columnSegment, long feedID, long keyID, long versionID) {
        try {
            S3Bucket bucket = findBucket(service, keyID);
            byte[] byteArray = getBytes(columnSegment);
            ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
            String segmentKey = "df" + feedID + "v" + versionID + "k" + keyID + "t" + System.currentTimeMillis();
            S3Object sObj = new S3Object(bucket, segmentKey);
            sObj.setDataInputStream(bais);
            service.putObject(bucket, sObj);
            return byteArray.length; 
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private List<ColumnSegment> getSegments(S3Service service, long feedID, long keyID, long versionID) {
        try {
            S3Bucket bucket = findBucket(service, keyID);
            List<ColumnSegment> list = new ArrayList<ColumnSegment>();
            String startKey = "df" + feedID +"v" + versionID + "k" + keyID;
            S3Object[] objects = service.listObjects(bucket, startKey, null);
            for (S3Object object : objects) {
                S3Object retrievedObject = service.getObject(bucket, object.getKey());
                byte retrieveBuf[];
                retrieveBuf = new byte[1];
                InputStream bfis = retrievedObject.getDataInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while (bfis.read(retrieveBuf) != -1) {
                    baos.write(retrieveBuf);
                }

                byte[] resultBytes = baos.toByteArray();
                ByteArrayInputStream bais = new ByteArrayInputStream(resultBytes);
                ObjectInputStream ois = new ObjectInputStream(bais);
                ColumnSegment columnSegment = (ColumnSegment) ois.readObject();
                list.add(columnSegment);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private S3Bucket findBucket(S3Service service, long keyID) throws S3ServiceException {
        // is there a bucket already assigned to this key?
        if (this.bucket == null) {
            String bucketName = "eibucket1" + getBucketAppenderName();
            this.bucket = service.getBucket(bucketName);
            if (this.bucket == null) {
                this.bucket = service.createBucket(bucketName);
            }
        }
        return this.bucket;
    }

    private String getBucketAppenderName() {
        if (this.bucketAppenderName == null) {
            try {
                this.bucketAppenderName = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                this.bucketAppenderName = "";
            }
        }
        return this.bucketAppenderName;
    }
}
