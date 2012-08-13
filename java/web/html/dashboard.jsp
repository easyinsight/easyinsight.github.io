<!DOCTYPE html>
<%@ page import="com.easyinsight.dashboard.DashboardService" %>
<%@ page import="com.easyinsight.dashboard.Dashboard" %>
<%@ page import="com.easyinsight.preferences.ApplicationSkin" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="com.easyinsight.datafeeds.FeedStorage" %>
<%@ page import="com.easyinsight.analysis.FilterHTMLMetadata" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.Set" %>
<%@ page import="com.easyinsight.preferences.ImageDescriptor" %>
<%@ page import="com.easyinsight.preferences.ApplicationSkinSettings" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.dashboard.DashboardUIProperties" %>
<%@ page import="com.easyinsight.core.DataSourceDescriptor" %>
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
        Dashboard dashboard = new DashboardService().getDashboardView(dashboardID);
        DashboardUIProperties dashboardUIProperties = dashboard.findHeaderImage();

        session.setAttribute("dashboard", dashboard);
        DataSourceDescriptor dataSourceDescriptor = new FeedStorage().dataSourceURLKeyForDataSource(dashboard.getDataSourceID());
        ApplicationSkin applicationSkin;

        String headerTextStyle = "width: 100%;text-align: center;font-size: 14px;padding-top: 10px;";
        String headerStyle;
        Session hibernateSession = Database.instance().createSession();
        try {
            applicationSkin = ApplicationSkinSettings.retrieveSkin(SecurityUtil.getUserID(), hibernateSession, SecurityUtil.getAccountID());
            headerStyle = "width:100%;overflow: hidden;";
        } finally {
            hibernateSession.close();
        }
        ImageDescriptor fullBackgroundImage = null;
        if (dashboardUIProperties != null) {
            fullBackgroundImage = dashboardUIProperties.getHeader();
            headerStyle += "text-align:center;background-color: " + String.format("#%06X", (0xFFFFFF & dashboardUIProperties.getColor()));
        }


        ImageDescriptor headerImageDescriptor = null;

        if (applicationSkin != null && applicationSkin.isReportHeader() && fullBackgroundImage == null) {
            headerImageDescriptor = applicationSkin.getReportHeaderImage();
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
    <script type="text/javascript" src="/js/date.js"></script>
    <script type="text/javascript" src="/js/jquery.datePicker.js"></script>
    <link href="/css/bootstrap.css" rel="stylesheet">

    <link href="/css/datePicker.css" rel="stylesheet" />
    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <script src="/js/html5.js"></script>
    <![endif]-->
    <!--[if lt IE 9]><script language="javascript" type="text/javascript" src="/js/excanvas.js"></script><![endif]-->
    <script type="text/javascript" src="/js/jquery.jqplot.js"></script>

    <style type="text/css">
        body {
            padding-top: 40px;
            padding-bottom: 40px;
        }

        #refreshDiv {
            display: none;
        }

        #problemHTML {
            display: none;
        }
    </style>
    <link href="/css/bootstrap-responsive.css" rel="stylesheet" />
    <link href="/css/app.css" rel="stylesheet" />
    <script type="text/javascript" src="/js/bootstrap.js"></script>
    <%
        Set<String> jsIncludes = new HashSet<String>(dashboard.getRootElement().jsIncludes());
        for (String jsInclude : jsIncludes) {
            out.println("<script type=\"text/javascript\" src=\"" + jsInclude + "\"></script>");
        }
        Set<String> cssIncludes = new HashSet<String>(dashboard.getRootElement().cssIncludes());
        for (String cssInclude : cssIncludes) {
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\""+cssInclude+"\" />");
        }
    %>
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
                $("#refreshDiv").hide();
                refreshReport();
            } else {
                $("#refreshDiv").hide();
                $("#problemHTML").show();
                $("#problemHTML").html(data["problemHTML"]);
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
            <div class="btn-group pull-right">
                <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                    <i class="icon-user"></i> <%= StringEscapeUtils.escapeHtml(userName) %>
                    <span class="caret"></span>
                </a>
                <ul class="dropdown-menu">
                    <li><a href="/app/html/flashAppAction.jsp">Switch to Full Interface</a></li>
                    <%--<li><a href="#">Profile</a></li>--%>
                    <li class="divider"></li>
                    <li><a href="/app/logoutAction.jsp">Sign Out</a></li>
                </ul>
            </div>

            <div class="nav-collapse">
                <ul class="nav">
                    <li><a href="/app/html">Data Sources</a></li>
                    <li><a href="/app/html/reports/<%= dataSourceDescriptor.getUrlKey() %>"><%=StringEscapeUtils.escapeHtml(dataSourceDescriptor.getName())%></a></li>
                    <li class="active"><a href="#"><%= StringEscapeUtils.escapeHtml(dashboard.getName()) %></a></li>
                </ul>
            </div>
        </div>
    </div>
</div>
<div class="container-fluid">
    <% if (fullBackgroundImage == null) { %>
    <div style="<%= headerStyle %>">
        <div style="padding:10px;float:left">
            <div style="background-color: #FFFFFF;padding: 5px">
                <%

                    if (headerImageDescriptor != null) {
                        out.println("<img src=\"/app/reportHeader?imageID="+headerImageDescriptor.getId()+"\"/>");
                    }
                %>
            </div>
        </div>
        <div style="<%= headerTextStyle %>">
            <%= StringEscapeUtils.escapeHtml(dashboard.getName()) %>
        </div>
    </div>
    <% } else { %>
    <div style="<%= headerStyle %>">
        <% out.println("<img src=\"/app/reportHeader?imageID="+fullBackgroundImage.getId()+"\"/>"); %>
    </div>
    <% } %>
</div>
<div class="container">
    <div class="row-fluid">
        <div class="span12" style="text-align:center" id="refreshDiv">
            Refreshing the data source...
            <div class="progress progress-striped active">
                <div class="bar"
                     style="width: 100%;"></div>
            </div>
        </div>
        <div class="span12" style="text-align:center" id="problemHTML">
        </div>
    </div>
    <div class="row">
        <div class="span12">
            <%= dashboard.getRootElement().toHTML(new FilterHTMLMetadata(dashboard, request)) %>
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