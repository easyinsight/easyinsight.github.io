package com.easyinsight.bugreports;

import com.easyinsight.config.ConfigLoader;
import com.easyinsight.email.AuthSMTPConnection;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.datafeeds.basecamp.BaseCampBugReportProvider;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jul 24, 2009
 * Time: 10:49:36 AM
 */
public class BugReportService {
    public void reportBug(String bugArea, String bugText) {
        String username = SecurityUtil.getUserName();
        if(username == null) return;
        BaseCampBugReportProvider  bugReport = new BaseCampBugReportProvider();
        bugReport.reportBug(bugArea, bugText, username);
    }

    public void reportFlexError(String errorMessage, String stackTrace) {
        if(ConfigLoader.instance().isProduction()) {
            String username = null;
            try {
                username = SecurityUtil.getUserName();
            } catch(Exception e1) {
            }
            try {
                new AuthSMTPConnection().sendSSLMessage("errors@easy-insight.com",
                        "Error! " + (username != null ? username : "Unknown") + ": " + errorMessage, stackTrace, "donotreply@easy-insight.com");
            }
            catch(Exception ex) {
                // do nothing, wtf do you do at this point?
                ex.printStackTrace();
            }
        }
    }
}
