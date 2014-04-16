<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Report Usages</title>
    <jsp:include page="bootstrapHeader.jsp"/>
    <%
        String userName = (String) session.getAttribute("userName");
        com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
        EIConnection conn = Database.instance().getConnection();
        try {
    %>
    <jsp:include page="bootstrapHeader.jsp"/>
    <jsp:include page="reportDashboardHeader.jsp"/>
    <script type="text/javascript">
        $(function () {
            _.templateSettings = {
                interpolate: /\<\@\=(.+?)\@\>/gim,
                evaluate: /\<\@(.+?)\@\>/gim,
                escape: /\<\@\-(.+?)\@\>/gim
            };
            var reports = _.template($("#reportsTemplate").html());
            $.getJSON("usage.json", function (data) {
                $("#reportTarget").html(reports({ data: data }));
            })
        })
    </script>
</head>
<body>
<jsp:include page="../header.jsp">
    <jsp:param name="userName" value="<%= userName %>"/>
    <jsp:param name="headerActive" value="<%= HtmlConstants.DATA_SOURCES_AND_REPORTS %>"/>
</jsp:include>
<div class="container corePageWell">
    <div class="row">
        <div class="col-md-12">
            <ul id="reportTarget"></ul>
        </div>
    </div>
    <script id="reportsTemplate" type="text/template">
        <li>
            Reports:
            <ul>
                <@ if(data.addons.length == 0) { @>
                <li>None.</li>
                <@ } else { @>
                <@ _.each(data.addons, function(e, i, l) { @>
                <li><a href="/app/html/report/<@= e.url_key @>"><@- e.name @></a></li>
                <@ }); @>
                <@ } @>
            </ul>
        </li>
        <li>
            Dashboards:
            <ul>
                <@ if(data.dashboards.length == 0) { @>
                <li>None.</li>
                <@ } else { @>
                <@ _.each(data.dashboards, function(e, i, l) { @>
                <li><a href="/app/html/dashboard/<@= e.url_key @>"><@- e.name @></a></li>
                <@ }); @>
                <@ } @>
            </ul>
        </li>
        <li>
            Data Sources:
            <ul>
                <@ if(data.data_sources.length == 0) { @>
                <li>None.</li>
                <@ } else { @>
                <@ _.each(data.data_sources, function(e, i, l) { @>
                <li><a href="/app/html/reports/<@= e.url_key @>"><@- e.name @></a></li>
                <@ }); @>
                <@ } @>
            </ul>
        </li>
    </script>
</body>
<%
    } finally {
        Database.closeConnection(conn);
        SecurityUtil.clearThreadLocal();
    }
%>
</html>