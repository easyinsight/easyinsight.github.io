<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.logging.LogClass" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight Report Explanation</title>
    <jsp:include page="bootstrapHeader.jsp"/>
    <jsp:include page="reportDashboardHeader.jsp"/>
</head>
<body>
<%

    String userName = (String) session.getAttribute("userName");
    if (userName != null) {
        com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    }

    Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
    if (exception != null) {
        LogClass.error(exception);
    }
    try {
        String urlKey = request.getParameter("urlKey");
%>
<script type="text/javascript">
    $(function() {
        _.templateSettings = {
            interpolate: /\<\@\=(.+?)\@\>/gim,
            evaluate: /\<\@(.+?)\@\>/gim,
            escape: /\<\@\-(.+?)\@\>/gim
        };
        var fields = _.template($("#fieldsTemplate").html());
        $.getJSON('/app/reportMarkdown?urlKey=<%=urlKey %>', function (data) {
            $("#primaryDiv").html(fields({ data: data }));
        });
    });
</script>
<jsp:include page="../header.jsp">
    <jsp:param name="userName" value="<%= userName %>"/>
    <jsp:param name="headerActive" value="<%= HtmlConstants.NONE %>"/>
</jsp:include>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <div style="width:100%;text-align: center">
                <img src="/images/logo2.PNG" alt="Easy Insight Logo"/>
            </div>
        </div>
        <div class="col-md-12">
            <div class="well" style="text-align:left;background-color: #FFFFFF" id="primaryDiv">
                <div>
                    Generating report documentation...
                </div>
                <div class="progress progress-striped active">
                    <div class="progress-bar" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%;"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<script id="fieldsTemplate" type="text/template">
    <div class="row">
        <div class="col-md-12">
            <h1>Fields for <a href="/app/html/report/<@- data.reportID @>"><@- data.reportName @></a></h1>
        </div>
    </div>
            <table>
                <@ _.each(data.fields, function(e, i, l) { @>
                    <tr>
                        <td><@- e.name @></td>
                    </tr>
                <@ }); @>
            </table>
    <div class="row">
        <div class="col-md-12">
    <@ _.each(data.fields, function(e, i, l) { @>
            <div class="row" style="margin-top: 10px;border-style: solid;border-color: #CFCCCC">
                <div class="col-md-12">
    <div class="row">
        <div class="col-md-12" style="background-color: #EEEEEE">
            <h2><@- e.name @></h2>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <div class="panel-group" id="<@= e.id @>accordion" style="margin-top:10px">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="<@= e.id @>accordion" href="#<@= e.id @>what">
                                What is this field?
                            </a>
                        </h4>
                    </div>
                    <div id="<@= e.id @>what" class="panel-collapse collapse">
                        <div class="panel-body">
                            <@= e.whatIsField @>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="<@= e.id @>accordion" href="#<@= e.id @>where">
                                Where does this field come from?
                            </a>
                        </h4>
                    </div>
                    <div id="<@= e.id @>where" class="panel-collapse collapse">
                        <div class="panel-body">
                            <@= e.whereFrom @>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
                </div>
            </div>
    <@ }); @>
        </div>
    </div>
</script>

<%
    } finally {
        if (userName != null) {
            SecurityUtil.clearThreadLocal();
        }
    }
%>
</body>
</html>