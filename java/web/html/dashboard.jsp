<!DOCTYPE html>
<%@ page import="com.easyinsight.dashboard.DashboardService" %>
<%@ page import="com.easyinsight.dashboard.Dashboard" %>
<%@ page import="com.easyinsight.preferences.ApplicationSkin" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="com.easyinsight.datafeeds.FeedStorage" %>
<%@ page import="com.easyinsight.analysis.FilterHTMLMetadata" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<%
    String userName = (String) session.getAttribute("userName");
    com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    try {
        String dashboardIDString = request.getParameter("dashboardID");
        long dashboardID = new DashboardService().canAccessDashboard(dashboardIDString);
        if (dashboardID == 0) {
            throw new com.easyinsight.security.SecurityException();
        }
        Dashboard dashboard = new DashboardService().getDashboard(dashboardID);
        session.setAttribute("dashboard", dashboard);
        String dataSourceURLKey = new FeedStorage().dataSourceURLKeyForDataSource(dashboard.getDataSourceID());
        ApplicationSkin applicationSkin = (ApplicationSkin) session.getAttribute("uiSettings");
        String headerStyle = "width:100%;overflow: hidden;padding: 10px;";
        String headerTextStyle = "width: 100%;text-align: center;font-size: 14px;padding-top: 10px;";
        if (applicationSkin != null && applicationSkin.isReportHeader()) {
            int reportBackgroundColor = applicationSkin.getReportBackgroundColor();
            headerStyle += "background-color: " + String.format("#%06X", (0xFFFFFF & reportBackgroundColor));
            headerTextStyle += "color: " + String.format("#%06X", (0xFFFFFF & applicationSkin.getReportTextColor()));
        }

%>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight &mdash; <%= StringEscapeUtils.escapeHtml(dashboard.getName()) %></title>
    <script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
    <link href="/css/bootstrap.css" rel="stylesheet">

    <style type="text/css">
        body {
            padding-top: 40px;
            padding-bottom: 40px;
        }
    </style>
    <link href="/css/bootstrap-responsive.css" rel="stylesheet">
    <script type="text/javascript" src="/js/bootstrap.js"></script>
    <script type="text/javascript" src="/js/jquery.jqplot.min.js"></script>
    <script type="text/javascript" src="/js/plugins/jqplot.barRenderer.min.js"></script>
    <script type="text/javascript" src="/js/plugins/jqplot.categoryAxisRenderer.min.js"></script>
    <script type="text/javascript" src="/js//plugins/jqplot.pointLabels.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/css/jquery.jqplot.min.css" />
    <link href="/css/app.css" rel="stylesheet">
    <script type="text/javascript">

        var filterBase = {};

        /*$(document).ready(function() {
         $.get('../htmlExport?reportID=1247', function(data) {
         $('#reportTarget').html(data)
         });
         });*/

        function updateFilter(name, key, refreshFunction) {
            var optionMenu = document.getElementById(name);
            var chosenOption = optionMenu.options[optionMenu.selectedIndex];
            var keyedFilter = filterBase[key];
            if (keyedFilter == null) {
                keyedFilter = {};
                filterBase[key] = keyedFilter;
            }
            keyedFilter[name] = chosenOption.value;
            refreshFunction();
        }

        function refreshDataSource() {
            $.getJSON('../refreshDataSource?dataSourceID=<%= dashboard.getDataSourceID() %>', function(data) {
                var callDataID = data["callDataID"];
                again(callDataID);
            });
        }

        function onDataSourceResult(data, callDataID) {
            var status = data["status"];
            if (status == 1) {
                // running
                again(callDataID);
            } else if (status == 2) {
                alert('done');
            } else {
                alert('failed');
            }
        }

        function afterRefresh() {
        }

        function again(callDataID) {
            setTimeout(function() {
                $.getJSON('../refreshStatus?callDataID=' + callDataID, function(data) {
                    onDataSourceResult(data, callDataID);
                });
            }, 5000);
        }

        function refreshReport(targetDiv, reportID) {
            $.get('../htmlExport?reportID='+reportID, function(data) {
                $(targetDiv).html(data)
            });
        }
    </script>
</head>
<body>
<div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container-fluid">
            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
            <div class="btn-group pull-right">
                <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                    <i class="icon-user"></i> <%= StringEscapeUtils.escapeHtml(userName) %>
                    <span class="caret"></span>
                </a>
                <ul class="dropdown-menu">
                    <%--<li><a href="#">Profile</a></li>
                    <li class="divider"></li>--%>
                    <li><a href="/app/logoutAction.jsp">Sign Out</a></li>
                </ul>
            </div>
            <div class="nav-collapse">
                <ul class="nav">
                    <li><a href="/app/html">Data Sources</a></li>
                    <li><a href="/app/html/reports/<%= dataSourceURLKey %>">Reports and Dashboards</a></li>
                    <li><a href="/app/html/flashAppAction.jsp">Full Interface</a></li>
                </ul>
            </div><!--/.nav-collapse -->
        </div>
    </div>
</div>
<div style="<%= headerStyle %>">
    <div style="background-color: #FFFFFF;padding: 5px;float:left">
        <%

            if (applicationSkin != null && applicationSkin.getReportHeaderImage() != null) {
                out.println("<img src=\"/app/reportHeader\"/>");
            }
        %>
    </div>
    <div style="<%= headerTextStyle %>">
        <%= StringEscapeUtils.escapeHtml(dashboard.getName()) %>
    </div>
</div>
<div class="container">
    <div class="row">
        <div class="span12">
            <%= dashboard.getRootElement().toHTML(new FilterHTMLMetadata(dashboard)) %>
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