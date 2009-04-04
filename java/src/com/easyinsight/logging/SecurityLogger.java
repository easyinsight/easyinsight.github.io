package com.easyinsight.logging;

import org.apache.log4j.Logger;

/**
 * User: Alan Baldwin
 * Date: April 3, 2009
 * Time: 5:47 PM
 */
public class SecurityLogger {
    private static Logger log = Logger.getLogger(SecurityLogger.class);

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