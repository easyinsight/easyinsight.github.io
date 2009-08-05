package com.easyinsight.logging;

import org.apache.log4j.Logger;
import com.easyinsight.email.AuthSMTPConnection;
import com.easyinsight.config.ConfigLoader;

import java.io.StringWriter;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

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
                new AuthSMTPConnection().sendSSLMessage("errors@easy-insight.com", "Error: " + message, message , "donotreply@easy-insight.com");
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
            String msg = new String(baos.toByteArray());
            try {
                new AuthSMTPConnection().sendSSLMessage("errors@easy-insight.com", "Error: " + e.getClass().getName(), msg, "donotreply@easy-insight.com");
            }
            catch(Exception ex) {
                // do nothing, wtf do you do at this point?
                ex.printStackTrace();
            }
        }
        log.error(e.getMessage(), e);
    }
}
