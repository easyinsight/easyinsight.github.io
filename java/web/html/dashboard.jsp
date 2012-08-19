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
<%@ page import="com.easyinsight.analysis.FeedMetadata" %>
<%@ page import="com.easyinsight.analysis.DataSourceInfo" %>
<%@ page import="com.easyinsight.analysis.DataService" %>
<%@ page import="com.easyinsight.analysis.ReportNotFoundException" %>
<%@ page import="com.easyinsight.logging.LogClass" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<%
    String userName = (String) session.getAttribute("userName");
    com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    try {
        String dashboardIDString = request.getParameter("dashboardID");
        long dashboardID = new DashboardService().canAccessDashboard(dashboardIDString);
        if (dashboardID == 0) {
            throw new ReportNotFoundException("Can't find the dashboard.");
        }
        Dashboard dashboard = new DashboardService().getDashboardView(dashboardID);
        DashboardUIProperties dashboardUIProperties = dashboard.findHeaderImage();
        boolean phone = request.getHeader("User-Agent").toLowerCase().matches(".*(android|avantgo|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|symbian|treo|up\\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino).*")||request.getHeader("User-Agent").toLowerCase().substring(0,4).matches("1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|e\\-|e\\/|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(di|rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|xda(\\-|2|g)|yas\\-|your|zeto|zte\\-");
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
                        <li><a href="/app/html/reports/<%= dataSourceDescriptor.getUrlKey() %>"><%=StringEscapeUtils.escapeHtml(dataSourceDescriptor.getName())%></a></li>
                        <li><a href="#" onclick="refreshReport()">Refresh Report</a></li>
                        <%
                            FeedMetadata feedMetadata = new DataService().getFeedMetadata(dashboard.getDataSourceID());
                            if (feedMetadata.getDataSourceInfo().getType() == DataSourceInfo.COMPOSITE_PULL || feedMetadata.getDataSourceInfo().getType() == DataSourceInfo.STORED_PULL) {
                        %>
                        <li><a href="#" onclick="refreshDataSource()">Refresh the Data Source</a></li>
                        <%
                            }
                        %>
                        <% } else { %>
                        <li><a href="/app/html/flashAppAction.jsp">Switch to Full Interface</a></li>
                        <% } %>
                        <%--<li><a href="#">Profile</a></li>--%>
                        <li class="divider"></li>
                        <li><a href="/app/logoutAction.jsp">Sign Out</a></li>
                    </ul>
                </div>
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
    } catch(ReportNotFoundException e) {
        LogClass.error(e);
        response.sendError(404);
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>
</html>