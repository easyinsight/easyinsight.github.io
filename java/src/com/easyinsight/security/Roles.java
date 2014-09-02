package com.easyinsight.security;

/**
 * User: James Boe
 * Date: Aug 12, 2008
 * Time: 10:59:37 AM
 */
public interface Roles {
    // owner = can save, edit, delete, etc
    public static final int OWNER = 1;
    // can edit, but can't delete
    public static final int SHARER = 2;
    public static final int EDITOR = 2;
    // can view, but can't make changes
    public static final int SUBSCRIBER = 3;
    public static final int VIEWER = 3;
    public static final int PUBLIC = 4;
    public static final int NONE = 5;
}
