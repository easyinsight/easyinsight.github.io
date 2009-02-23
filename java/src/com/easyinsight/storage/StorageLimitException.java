package com.easyinsight.storage;

/**
 * User: James Boe
 * Date: Feb 17, 2009
 * Time: 11:39:01 AM
 */
public class StorageLimitException extends RuntimeException {
    public StorageLimitException(String message) {
        super(message);
    }
}
