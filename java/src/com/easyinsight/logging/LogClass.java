package com.easyinsight.logging;

import com.easyinsight.email.SendGridEmail;
import org.apache.log4j.Logger;
import com.easyinsight.config.ConfigLoader;
import com.easyinsight.security.SecurityUtil;

import java.io.PrintStream;
import java.io.ByteArrayOutputStream;

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
        if(ConfigLoader.instance().isProduction()) {
            try {
                String username = null;
                try {
                    username = SecurityUtil.getUserName();
                } catch(Exception e) {
                }
                new SendGridEmail().sendEmail("errors@easy-insight.com", "Error! " + (username != null ? username : "Unknown") + ": " + message, message , "donotreply@easy-insight.com", false, "Easy Insight");
            }
            catch(Exception ex) {
                // do nothing, wtf do you do at this point?
                ex.printStackTrace();
            }
        }
        log.error(message);
    }

    public static void error(String message, Throwable e) {
        if(ConfigLoader.instance().isProduction()) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream writer = new PrintStream(baos);
            e.printStackTrace(writer);
            try {
                String username = null;
                try {
                    username = SecurityUtil.getUserName();
                } catch(Exception se) {
                }
                String msg = new String(baos.toByteArray());
                new SendGridEmail().sendEmail("errors@easy-insight.com", "Error! " + (username != null ? username : "Unknown") + ": " + message, msg , "donotreply@easy-insight.com", false, "Easy Insight");
            }
            catch(Exception ex) {
                // do nothing, wtf do you do at this point?
                ex.printStackTrace();
            }
        }
        log.error(message);
    }

    public static void error(Throwable e) {
        if(ConfigLoader.instance().isProduction()) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream writer = new PrintStream(baos);
            e.printStackTrace(writer);
            String username = null;
            try {
                username = SecurityUtil.getUserName();
            } catch(Exception e1) {
            }
            String msg = new String(baos.toByteArray());
            try {
                new SendGridEmail().sendEmail("errors@easy-insight.com", "Error! " + (username != null ? username : "Unknown") + ": " + e.getClass().getName(), msg, "donotreply@easy-insight.com", false, "Easy Insight");
            }
            catch(Exception ex) {
                // do nothing, wtf do you do at this point?
                ex.printStackTrace();
            }
        }
        log.error(e.getMessage(), e);
    }
}
