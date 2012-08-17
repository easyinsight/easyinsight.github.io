<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.core.DataSourceDescriptor" %>
<%@ page import="com.easyinsight.core.EIDescriptor" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.util.Collections" %>
<%@ page import="com.easyinsight.core.InsightDescriptor" %>
<%@ page import="java.util.List" %>
<%@ page import="com.easyinsight.userupload.UserUploadService" %>
<%@ page import="com.easyinsight.dashboard.DashboardDescriptor" %>
<%@ page import="com.easyinsight.audit.ActionLog" %>
<%@ page import="com.easyinsight.admin.AdminService" %>
<%@ page import="java.util.Collection" %>
<%@ page import="com.easyinsight.audit.ActionReportLog" %>
<%@ page import="com.easyinsight.audit.ActionDashboardLog" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="com.easyinsight.datafeeds.FeedStorage" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight Reports and Dashboards</title>
    <script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
    <link href="/css/bootstrap.css" rel="stylesheet">

    <style type="text/css">
        body {
            padding-top: 45px;
            padding-bottom: 40px;
        }
    </style>
    <link href="/css/bootstrap-responsive.css" rel="stylesheet">
    <script type="text/javascript" src="/js/bootstrap.js"></script>
</head>
<body>
<%
    String userName = (String) session.getAttribute("userName");
    com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    try {
        boolean phone = request.getHeader("User-Agent").toLowerCase().matches(".*(android|avantgo|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|symbian|treo|up\\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino).*")||request.getHeader("User-Agent").toLowerCase().substring(0,4).matches("1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|e\\-|e\\/|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(di|rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|xda(\\-|2|g)|yas\\-|your|zeto|zte\\-");
        String dataSourceKey = request.getParameter("dataSourceID");
        long dataSourceID = new FeedStorage().dataSourceIDForDataSource(dataSourceKey);
        DataSourceDescriptor dataSourceDescriptor = new FeedStorage().dataSourceURLKeyForDataSource(dataSourceID);

%>
<div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container-fluid">
            <a data-target=".nav-collapse" data-toggle="collapse" class="btn btn-navbar">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
            <div class="nav-collapse">
                <div class="btn-group pull-right">
                    <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                        <i class="icon-user"></i> <%= StringEscapeUtils.escapeHtml(userName) %>
                        <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <% if (phone) { %>
                            <li><a href="/app/html">Data Sources</a></li>
                            <li><a href="#"><%=StringEscapeUtils.escapeHtml(dataSourceDescriptor.getName())%></a></li>
                        <% } else { %>
                            <li><a href="/app/html/flashAppAction.jsp">Switch to Full Interface</a></li>
                        <% } %>
                        <%--<li><a href="#">Profile</a></li>--%>
                        <li class="divider"></li>
                        <li><a href="/app/logoutAction.jsp">Sign Out</a></li>
                    </ul>
                </div>
                <% if (!phone) { %>
                <div class="nav-collapse">
                    <ul class="nav">
                        <li><a href="/app/html">Data Sources</a></li>
                        <li class="active"><a href="#"><%=StringEscapeUtils.escapeHtml(dataSourceDescriptor.getName())%></a></li>
                    </ul>
                </div>
                <% } %>
            </div>
        </div>
    </div>
</div>
<div class="container-fluid">
    <div class="row-fluid">
        <div class="span3">
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
        <div class="span9">
            <table class="table table-striped table-bordered">
                <thead>
                <tr>
                    <th>Name</th>
                </tr>
                </thead>
            <%



                List<EIDescriptor> descriptors = new UserUploadService().getFeedAnalysisTreeForDataSource(new DataSourceDescriptor(null, dataSourceID, 0, false));
                Collections.sort(descriptors, new Comparator<EIDescriptor>() {

                    public int compare(EIDescriptor eiDescriptor, EIDescriptor eiDescriptor1) {
                        String name1 = eiDescriptor.getName() != null ? eiDescriptor.getName().toLowerCase() : "";
                        String name2 = eiDescriptor1.getName() != null ? eiDescriptor1.getName().toLowerCase() : "";
                        return name1.compareTo(name2);
                    }
                });
                for (EIDescriptor descriptor : descriptors) {
                    if (descriptor instanceof InsightDescriptor) {
                        out.println("<tr><td><a href=\"../report/" + descriptor.getUrlKey() + "\">" + descriptor.getName() + "</td></tr>");
                    } else if (descriptor instanceof DashboardDescriptor) {
                        out.println("<tr><td><a href=\"../dashboard/" + descriptor.getUrlKey() + "\">" + descriptor.getName() + "</td></tr>");
                    }
                }
            %>
            </table>
        </div>
    </div>
</div>
</body>
<%
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>
</html>