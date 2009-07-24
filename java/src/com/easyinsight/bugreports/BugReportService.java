package com.easyinsight.bugreports;

import com.easyinsight.security.SecurityUtil;
import com.easyinsight.datafeeds.basecamp.BaseCampBugReportProvider;

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
}
