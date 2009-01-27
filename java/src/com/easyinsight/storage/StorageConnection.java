package com.easyinsight.storage;

import com.easyinsight.datafeeds.FeedDefinition;

import java.sql.Connection;

/**
 * User: James Boe
 * Date: Nov 10, 2008
 * Time: 12:08:12 AM
 */
public class StorageConnection {
    public static IReadableConnection readable(FeedDefinition feedDefinition) {
        return null;
    }

    public static IWriteableConnection writeable(FeedDefinition feedDefinition, Connection coreDBConnection) {
        return null;
    }
}
