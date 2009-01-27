package com.easyinsight.logging;

import org.apache.log4j.Logger;

/**
 * User: James Boe
 * Date: Aug 29, 2008
 * Time: 11:01:14 AM
 */
public class LogClass {
    private static org.apache.log4j.Logger log = Logger.getLogger(LogClass.class);

    public static void main(String[] args) {
        log.info("info");
        log.debug("debug");
        log.error("error");
    }

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
