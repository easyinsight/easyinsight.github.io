<%@ page import="com.easyinsight.audit.ActionDashboardLog" %>
<%@ page import="com.easyinsight.audit.ActionReportLog" %>
<%@ page import="com.easyinsight.audit.ActionLog" %>
<%@ page import="com.easyinsight.admin.AdminService" %>
<%@ page import="java.util.Collection" %>
<div class="col-md-3">
    <img src="/images/logo2.PNG"/>

    <div class="well sidebar-nav">
        <ul class="nav nav-list">
            <li class="nav-header">Recent Actions</li>
            <%
                Collection<ActionLog> actions = new AdminService().getRecentHTMLActions();
                for (ActionLog actionLog : actions) {
                    if (actionLog instanceof ActionReportLog && actionLog.getActionType() == ActionReportLog.VIEW) {
                        ActionReportLog actionReportLog = (ActionReportLog) actionLog;
                        out.println("<li><a href=\"../report/" + actionReportLog.getInsightDescriptor().getUrlKey() + "\">View " + actionReportLog.getInsightDescriptor().getName() + "</a></li>");
                    } else if (actionLog instanceof ActionDashboardLog && actionLog.getActionType() == ActionDashboardLog.VIEW) {
                        ActionDashboardLog actionDashboardLog = (ActionDashboardLog) actionLog;
                        out.println("<li><a href=\"../dashboard/" + actionDashboardLog.getDashboardDescriptor().getUrlKey() + "\">View " + actionDashboardLog.getDashboardDescriptor().getName() + "</a></li>");
                    }
                }
            %>
        </ul>
    </div>
</div>