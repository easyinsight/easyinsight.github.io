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
            var fields = _.template($("#fieldsTemplate").html());
            $.getJSON("fields.json", function (data) {
                $("#reportTarget").html(fields({ data: data }));
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
            <div id="reportTarget"></div>
    <script id="reportsTemplate" type="text/template">

    </script>
</body>
<%
    } finally {
        Database.closeConnection(conn);
        SecurityUtil.clearThreadLocal();
    }
%>
</html>