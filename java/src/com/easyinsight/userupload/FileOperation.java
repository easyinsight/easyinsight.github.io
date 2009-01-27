package com.easyinsight.userupload;

/**
 * User: James Boe
 * Date: Oct 11, 2008
 * Time: 1:53:33 PM
 */
public interface FileOperation {
    public byte[] retrieve(long fileID, long userID);
}
