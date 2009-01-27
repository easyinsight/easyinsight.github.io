package com.easyinsight.datafeeds;

import java.util.Map;
import java.util.HashMap;

/**
 * User: James Boe
 * Date: Sep 6, 2008
 * Time: 6:14:28 PM
 */
public class FeedPersistenceMetadata {
    private long metadataID;
    private long size;
    private int version;
    private String database;

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public long getMetadataID() {
        return metadataID;
    }

    public void setMetadataID(long metadataID) {
        this.metadataID = metadataID;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
