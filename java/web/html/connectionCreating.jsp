<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page import="com.easyinsight.datafeeds.*" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
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
        FeedDefinition dataSource;
        String summary = "";
        if (feedResponse.getStatus() == FeedResponse.SUCCESS) {

            dataSource = new FeedStorage().getFeedDefinitionData(feedResponse.getFeedDescriptor().getId());
            if (dataSource.postOAuthSetup(request) != null) {
                response.sendRedirect(dataSource.postOAuthSetup(request));
                return;
            }

            int dataSourceType = dataSource.getFeedType().getType();
            try (EIConnection conn = Database.instance().getConnection()) {
                try (PreparedStatement ps = conn.prepareStatement("SELECT SOLUTION.SUMMARY FROM SOLUTION WHERE DATA_SOURCE_TYPE = ?")) {
                    ps.setInt(1, dataSourceType);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        summary = rs.getString(1);
                        if (summary == null) {
                            summary = "";
                        }
                    }
                }
            }
        } else {
            response.sendRedirect("/serverError.jsp");
            return;
        }
%>
<script type="text/javascript">

    $(document).ready(function() {
        startRefresh();
    });

    function startRefresh() {
        $.getJSON('/app/completeInstallation?dataSourceID=<%= request.getParameter("dataSourceID") %>', function(data) {
            if (typeof(data["failureMessage"]) != "undefined") {
                window.location.replace("/app/html/connections/<%= dataSource.getFeedType().getType() %>?error=true&problemCode="+data["problemCode"]);
            } else {
                var callDataID = data["callDataID"];
                again(callDataID, "Refreshing the data source...");
            }
        });
    }

    function onCallData(data, callDataID) {
        var status = data["status"];
        var message = data["statusMessage"];
        if (status == 1) {
            // running
            again(callDataID, message);
        } else if (status == 2) {
            // done
            $.getJSON('/app/connectionInstalled?dataSourceID=<%= request.getParameter("dataSourceID") %>&utcOffset=' + new Date().getTimezoneOffset(), function(data) {
                // redirect to result specified by data
                window.location.replace(data["url"]);
            });
        } else {
            window.location.replace("/app/html/connections/<%= dataSource.getFeedType().getType() %>?error=true&problemCode="+data["problemCode"]);
            // problem
        }
    }

    function again(callDataID, message) {
        if (message != null) {
            $("#messageDiv").html(message);
        }
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
<div class="container-fluid">
    <div class="row">
        <div class="col-md-12" style="background-color:#0084b4">
            <div class="row">
                <div class="col-md-8 col-md-offset-2" style="text-align:center">
                    <h2 style="color:#FFFFFF;margin-top: 20px;margin-bottom: 20px">
                        Creating your connection...
                    </h2>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="container corePageWell" style="margin-top: 20px;">
    <div class="row">
        <div class="col-md-6 col-md-offset-3">
            <div class="row">
                <div class="col-md-12">
                    <div class="row" style="margin-top:30px;margin-bottom: 30px">
                        <div class="col-md-12">
                            <p style="font-weight: normal;margin-bottom: 10px">Easy Insight is pulling over the data from your target connection. Depending on how much data you have, this process may take several minutes.</div>
                        </div>
                    </div>
                    <div class="row" style="margin-top:30px">
                        <div class="col-md-12">
                            <div id="messageDiv" style="font-weight: bold;margin-bottom: 10px">Refreshing the data source...</div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="progress progress-striped active">
                                <div class="progress-bar"
                                     style="width: 100%;"></div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div id="problemHTML">
                            </div>
                        </div>
                    </div>
                <div class="row" style="margin-top:30px">
                    <div class="col-md-12">
                        <p style="font-weight: normal;margin-bottom: 10px"><%= summary %></div>
                </div>
            </div>
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