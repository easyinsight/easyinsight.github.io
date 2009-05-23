package com.easyinsight.dbservice;

import org.apache.log4j.Logger;

/**
 * User: James Boe
 * Date: May 23, 2009
 * Time: 9:25:36 AM
 */
public class LogClass {
    private static org.apache.log4j.Logger log = Logger.getLogger(LogClass.class);

    public static void info(String message) {
        log.info(message);
    }

    public static void debug(String message) {
        log.debug(message);
    }

    public static void error(String message) {
        log.error(message);
    }

    public static void error(Throwable e) {
        log.error(e.getMessage(), e);
    }
}
