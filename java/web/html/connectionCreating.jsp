<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.core.DataSourceDescriptor" %>
<%@ page import="com.easyinsight.core.EIDescriptor" %>
<%@ page import="com.easyinsight.core.InsightDescriptor" %>
<%@ page import="com.easyinsight.userupload.UserUploadService" %>
<%@ page import="com.easyinsight.dashboard.DashboardDescriptor" %>
<%@ page import="com.easyinsight.audit.ActionLog" %>
<%@ page import="com.easyinsight.admin.AdminService" %>
<%@ page import="com.easyinsight.audit.ActionReportLog" %>
<%@ page import="com.easyinsight.audit.ActionDashboardLog" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="com.easyinsight.userupload.CustomFolder" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="java.util.*" %>
<%@ page import="com.easyinsight.core.DataFolder" %>
<%@ page import="com.easyinsight.html.Utils" %>
<%@ page import="com.easyinsight.datafeeds.*" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <style type="text/css">
        #refreshDiv {
            display: none;
        }
    </style>
    <title>Easy Insight Reports and Dashboards</title>
    <jsp:include page="bootstrapHeader.jsp"/>
    <jsp:include page="reportDashboardHeader.jsp"/>
</head>
<body>
<%
    String userName = (String) session.getAttribute("userName");
    com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    try {
        // find the data source
        // if it requires additional setup (i.e. Basecamp or Smartsheet, redirect appropriately)

        FeedResponse feedResponse = new FeedService().openFeedIfPossible(request.getParameter("dataSourceID"));
        if (feedResponse.getStatus() == FeedResponse.SUCCESS) {
            FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(feedResponse.getFeedDescriptor().getId());
            if (dataSource.postOAuthSetup(request) != null) {
                response.sendRedirect(dataSource.postOAuthSetup(request));
                return;
            }
        }
%>
<script type="text/javascript">

    $(document).ready(function() {
        startRefresh();
    });

    function startRefresh() {
        $.getJSON('/app/completeInstallation?dataSourceID=<%= request.getParameter("dataSourceID") %>', function(data) {
            var callDataID = data["callDataID"];
            again(callDataID);
        });
    }

    function onCallData(data, callDataID) {
        var status = data["status"];
        if (status == 1) {
            // running
            again(callDataID);
        } else if (status == 2) {
            // done
            $.getJSON('/app/connectionInstalled?dataSourceID=<%= request.getParameter("dataSourceID") %>', function(data) {
                // redirect to result specified by data
                window.location.replace(data["url"]);
            });
        } else {
            // problem
        }
    }

    function again(callDataID) {
        setTimeout(function() {
            $.getJSON('/app/refreshStatus?callDataID=' + callDataID, function(data) {
                onCallData(data, callDataID);
            });
        }, 5000);
    }
</script>
<jsp:include page="../header.jsp">
    <jsp:param name="userName" value="<%= userName %>"/>
    <jsp:param name="headerActive" value="<%= HtmlConstants.DATA_SOURCES_AND_REPORTS %>"/>
</jsp:include>
<div class="container corePageWell">
    <div class="row">
        <div class="col-md-6 col-md-offset-3">
            <div class="row">
                    <div id="messageDiv" style="font-weight: bold;margin-bottom: 10px">Refreshing the data source...</div>
                    <div class="progress progress-striped active">
                        <div class="progress-bar"
                             style="width: 100%;"></div>
                    </div>
                </div>
                <div class="col-md-12" style="text-align:center" id="problemHTML">
                </div>
            </div>
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