package com.easyinsight.database.migration;

/**
 * User: James Boe
 * Date: Apr 27, 2009
 * Time: 11:46:24 AM
 */
public interface Migration {

    boolean needToRun();

    void migrate();
}
