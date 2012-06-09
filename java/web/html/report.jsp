<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.preferences.ApplicationSkin" %>
<%@ page import="com.easyinsight.analysis.*" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="com.easyinsight.datafeeds.FeedStorage" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<%
    String userName = (String) session.getAttribute("userName");
    com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    try {
        String reportIDString = request.getParameter("reportID");
        InsightResponse insightResponse = new AnalysisService().openAnalysisIfPossible(reportIDString);
        long reportID;
        if (insightResponse.getStatus() == InsightResponse.SUCCESS) {
            reportID = insightResponse.getInsightDescriptor().getId();
        } else {
            throw new com.easyinsight.security.SecurityException();
        }
        WSAnalysisDefinition report = new AnalysisStorage().getAnalysisDefinition(reportID);
        if (report == null) {
            throw new RuntimeException("Attempt made to load report " + reportID + " which doesn't exist.");
        }
        String dataSourceURLKey = new FeedStorage().dataSourceURLKeyForDataSource(report.getDataFeedID());
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
    <title>Easy Insight &mdash; <%= StringEscapeUtils.escapeHtml(report.getName()) %></title>
    <script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="/js/jquery-ui-1.8.20.custom.min.js"></script>
    <link href="/css/bootstrap.css" rel="stylesheet">
    <link href="/css/dark-hive/jquery-ui-1.8.20.custom.css" rel="stylesheet">

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
    <link href="/css/bootstrap-responsive.css" rel="stylesheet">
    <script type="text/javascript" src="/js/bootstrap.js"></script>
    <%= report.javaScriptIncludes() %>
    <script type="text/javascript">

        var filterBase = {};

        $(document).ready(function() {
            refreshReport();
        });

        function updateFilter(name) {
            var optionMenu = document.getElementById(name);
            var chosenOption = optionMenu.options[optionMenu.selectedIndex];
            filterBase[name] = chosenOption.value;
            refreshReport();
        }

        function updateRollingFilter(name) {
            var optionMenu = document.getElementById(name);
            var chosenOption = optionMenu.value;
            if (chosenOption == '18') {
                filterBase[name + "direction"] = document.getElementById('customDirection' + name).value;
                filterBase[name + "value"] = document.getElementById('customValue' + name).value;
                filterBase[name + "interval"] = document.getElementById('customInterval' + name).value;
            }
            filterBase[name] = chosenOption;
            refreshReport();
        }

        function updateMultiMonth(name) {
            var startName = name + "start";
            var endName = name + "end";
            var startMonth = $("#"+startName).val();
            var endMonth = $("#"+endName).val();
            filterBase[name + "start"] = startMonth;
            filterBase[name + "end"] = endMonth;
            refreshReport();
        }

        function updateMultiFilter(name) {
            var selects = $("#"+name).val();
            filterBase[name] = selects;
            refreshReport();
        }

        function refreshDataSource() {
            $("#refreshDiv").show();
            $.getJSON('/app/refreshDataSource?dataSourceID=<%= report.getDataFeedID() %>', function(data) {
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

        function email() {
            var format = $('input:radio[name=emailGroup]:checked').val();
            var recipient = $('#input01').val();
            var subject = $('#input02').val();
            var body = $('#input03').value;
            $.getJSON('/app/emailReport?reportID=<%= report.getAnalysisID()%>&format=' + format + "&recipient="+recipient + "&subject=" + subject + "&body=" + body, function(data) {
                alert('Email sent.');
            });
        }

        function again(callDataID) {
            setTimeout(function() {
                $.getJSON('/app/refreshStatus?callDataID=' + callDataID, function(data) {
                    onDataSourceResult(data, callDataID);
                });
            }, 5000);
        }

        function filterEnable(name) {
            filterBase[name + "enabled"] = document.getElementById(name + 'enabled').checked;
            refreshReport();
        }

        function refreshReport() {
            $('#refreshingReport').modal(true, true, true);
            var strParams = "";
            for (var filterValue in filterBase) {
                var value = filterBase[filterValue];
                strParams += filterValue + "=" + value + "&";
            }
            <%= report.toHTML("reportTarget") %>
        }

        function afterRefresh() {
            $('#refreshingReport').modal('hide');
        }
    </script>
</head>
<body style="background-color: #f5f5f5">

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

            <div class="nav-collapse pull-left">
                <ul class="nav">
                    <li><a href="/app/html">Data Sources</a></li>
                    <li><a href="/app/html/reports/<%= dataSourceURLKey %>">Reports and Dashboards</a></li>
                    <li><a href="flashAppAction.jsp">Full Interface</a></li>
                </ul>
            </div>
            <div class="nav-collapse btn-group pull-left">
                <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                    Export the Report
                    <span class="caret"></span>
                </a>
                <ul class="dropdown-menu pull-right">
                    <li><button class="btn" type="button" onclick="window.location.href='../exportExcel?reportID=<%= report.getAnalysisID() %>'" style="padding:5px;margin:5px;width:150px">Export to Excel</button></li>
                    <li><button class="btn" type="button" onclick="$('#emailReportWindow').modal(true, true, true)" style="padding:5px;margin:5px;width:150px">Email the Report</button></li>
                </ul>
            </div>
            <div class="nav-collapse btn-group pull-left">
                <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                    Refresh Data
                    <span class="caret"></span>
                </a>
                <ul class="dropdown-menu pull-right">
                    <li><button class="btn" type="button" onclick="refreshReport()" style="padding:5px;margin:5px;width:150px">Refresh the Report</button></li>
                    <%
                        FeedMetadata feedMetadata = new DataService().getFeedMetadata(report.getDataFeedID());
                        if (feedMetadata.getDataSourceInfo().getType() == DataSourceInfo.COMPOSITE_PULL || feedMetadata.getDataSourceInfo().getType() == DataSourceInfo.STORED_PULL) {
                            %>
                    <li><button class="btn" type="button" id="refreshDataSourceButton" onclick="refreshDataSource()" style="padding:5px;margin:5px;width:150px">Refresh Data Source</button></li>
                            <%
                        }
                    %>

                </ul>
            </div>
        </div>
    </div>
</div>
<div class="modal hide" id="refreshingReport">
    <div class="modal-body">
        Refreshing the report...
        <div class="progress progress-striped active">
            <div class="bar"
                 style="width: 100%;"></div>
        </div>
    </div>
</div>
<div class="modal hide fade" id="exportModalWindow">
    <div class="modal-header">
        <button data-dismiss="modal">×</button>
        <h3>Export Options</h3>
    </div>
    <div class="modal-body">
        <a href="../exportExcel?reportID=<%= report.getAnalysisID() %>" class="btn">Export to Excel</a>
        <button class="btn" onclick="$('#exportModalWindow').modal('hide'); $('(#emailReportWindow').modal(true, true, true)">Email Report</button>
    </div>
</div>
<div class="modal hide fade" id="emailReportWindow">
    <div class="modal-header">
        <button class="close" data-dismiss="modal">×</button>
        <h3>Email Report</h3>
    </div>
    <div class="modal-body">
    <form class="form-horizontal">
        <div class="control-group">
            <%--<label class="control-label" for="input01">Which format?</label>--%>
            <div class="controls">
                <input type="radio" name="emailGroup" value="4">HTML
                <input type="radio" name="emailGroup" value="1">Excel
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="input01">Who will this email go to?</label>
            <div class="controls">
                <input type="text" class="input-xlarge" id="input01">
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="input02">What should the subject of the email be?</label>
            <div class="controls">
                <input type="text" class="input-xlarge" id="input02">
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="textarea">Any message to include with the email?</label>
            <div class="controls">
                <textarea class="input-xlarge" id="textarea" rows="5"></textarea>
            </div>
        </div>
    </form>
    </div>
    <div class="modal-footer">
        <button class="btn" data-dismiss="modal" onclick="email()">Send</button>
    </div>
</div>
<% if (applicationSkin != null && applicationSkin.isReportHeader()) { %>
<div style="<%= headerStyle %>">
    <div style="background-color: #FFFFFF;padding: 5px;float:left">
    <%

        if (applicationSkin.getReportHeaderImage() != null) {
            out.println("<img src=\"/app/reportHeader\"/>");
        }
    %>
    </div>
    <div style="<%= headerTextStyle %>">
        <%= StringEscapeUtils.escapeHtml(report.getName()) %>
    </div>
</div>
<% } else { %>
<div style="<%= headerTextStyle %>">
    <%= StringEscapeUtils.escapeHtml(report.getName()) %>
</div>
<% } %>
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
        <div class="row-fluid" id="filterRow">
            <div class="span12">
            <%
                for (FilterDefinition filterDefinition : report.getFilterDefinitions()) {
                    if (filterDefinition.isShowOnReportView()) {
                        out.println("<div style=\"float:left\">" + filterDefinition.toHTML(report) + "</div>");
                    }
                }
            %>
            </div>
        </div>
        <div class="row">
            <div class="span12">
                <div class="well" style="background-color: #ffffff">
                    <div id="reportTarget"></div>
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