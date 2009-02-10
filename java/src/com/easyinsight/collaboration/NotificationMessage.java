package com.easyinsight.collaboration;

/**
 * User: James Boe
 * Date: Feb 9, 2009
 * Time: 1:28:09 PM
 */
public abstract class NotificationMessage {
    public static final int DATA_SOURCE_ACCESS = 1;
    public static final int REPORT_ACCESS = 2;
    public static final int GROUP_ACCESS = 3;
    public static final int GROUP_COMMENT_ADDED = 4;
    public static final int REPORT_COMMENT_ADDED = 5;
    public static final int DATA_SOURCE_COMMENT_ADDED = 6;

    public abstract int messageType(); 
}
