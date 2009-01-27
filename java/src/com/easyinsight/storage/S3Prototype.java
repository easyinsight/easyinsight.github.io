package com.easyinsight.storage;

import org.jets3t.service.security.AWSCredentials;
import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

import com.easyinsight.dataset.ColumnSegment;
import com.easyinsight.core.Value;
import com.easyinsight.core.StringValue;

/**
 * User: James Boe
 * Date: Jul 22, 2008
 * Time: 11:45:26 PM
 */
public class S3Prototype {

    private S3Bucket bucket;

    // operations...

    // basically add or update in bulk

    private void addOrReplaceColumnSegment(S3Service service, ColumnSegment columnSegment, long feedID, long keyID) {
        
    }

    private void persistColumnSegment(S3Service service, ColumnSegment columnSegment, long feedID, long keyID) {
        try {
            S3Bucket bucket = findBucket(service, keyID);
            byte[] byteArray = getBytes(columnSegment);
            ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
            String segmentKey = "df" + feedID + "k" + keyID + "t" + System.currentTimeMillis();
            S3Object sObj = new S3Object(bucket, segmentKey);
            sObj.setDataInputStream(bais);
            service.putObject(bucket, sObj);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private List<ColumnSegment> getSegments(S3Service service, long feedID, long keyID) {
        try {
            S3Bucket bucket = findBucket(service, keyID);
            List<ColumnSegment> list = new ArrayList<ColumnSegment>();
            String startKey = "df" + feedID + "k" + keyID;
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



    private byte[] getBytes(ColumnSegment columnSegment) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(columnSegment);
        oos.flush();
        oos.close();
        return baos.toByteArray();
    }

    private S3Bucket findBucket(S3Service service, long keyID) throws S3ServiceException {
        // is there a bucket already assigned to this key?
        if (this.bucket == null) {
            this.bucket = service.getBucket("eibucket1");
            if (this.bucket == null) {
                this.bucket = service.createBucket("eibucket1");
            }
        }
        return this.bucket;
    }


    public static void main(String[] args) {
        AWSCredentials credentials = new AWSCredentials("0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
        String bucketString = "dmsanalysis";
        String prefix = "ds";
        int dataFeedID = 1;
        long startTime = System.currentTimeMillis();
        try {

            S3Service s3Service = new RestS3Service(credentials);
            ColumnSegment seg1 = new ColumnSegment(new Value[] { new StringValue("s1"), new StringValue("s2") });
            ColumnSegment seg2 = new ColumnSegment(new Value[] { new StringValue("s3"), new StringValue("s4") });
            S3Prototype proto = new S3Prototype();
            //proto.persistColumnSegment(s3Service, seg1, 1, 1);
            //proto.persistColumnSegment(s3Service, seg2, 1, 1);
            List<ColumnSegment> segments = proto.getSegments(s3Service, 1, 1);
            System.out.println("segments size = " + segments.size());
            System.out.println((System.currentTimeMillis() - startTime) / 1000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
