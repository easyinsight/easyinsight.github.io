<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.logging.LogClass" %>
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
    });

    function getResults() {
        $("#primaryDiv").show();
        var fields = _.template($("#fieldsTemplate").html());
        var usages = _.template($("#usageTemplate").html());
        var whatHappened = _.template($("#whatHappenedTemplate").html());
        $.getJSON('/app/reportMarkdown?urlKey=<%=urlKey %>', function (data) {
            $("#primaryDiv").hide();

            $("#reportContents").html(fields({ data: data["reportContents"] }));
            $("#reportUsages").html(usages({ data: data["reportUsages"] }));
            $("#reportEvents").html(whatHappened({ data: data["reportEvents"] }));

            $("#reportResults").show();
        });
    }
</script>
<jsp:include page="../header.jsp">
    <jsp:param name="userName" value="<%= userName %>"/>
    <jsp:param name="headerActive" value="<%= HtmlConstants.NONE %>"/>
</jsp:include>
<div class="container">
    <div class="row" style="margin-top:20px">
        <div class="col-md-12">
            <button onclick="getResults()" class="btn btn-primary">Generate Docs</button>
        </div>
    </div>
    <div class="row" style="display:none" id="primaryDiv">
        <div class="col-md-12">
            <div class="well" style="background-color: #FFFFFF">
                <div>
                    Generating report documentation...
                </div>
                <div class="progress progress-striped active">
                    <div class="progress-bar" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%;"></div>
                </div>
            </div>
        </div>
    </div>
    <div class="row" id="reportResults" style="display:none">
        <div class="col-md-12 well" style="text-align:left;background-color: #FFFFFF">

                <ul class="nav nav-tabs">
                    <li class="active"><a href="#reportContents" data-toggle="tab">What's in this report?</a></li>
                    <li><a href="#reportUsages" data-toggle="tab">What uses this report?</a></li>
                    <li><a href="#reportEvents" data-toggle="tab">What happens when this report runs?</a></li>
                </ul>
                <div class="tab-content">
                    <div class="tab-pane active" id="reportContents"></div>
                    <div class="tab-pane" id="reportUsages"></div>
                    <div class="tab-pane" id="reportEvents"></div>
                </div>

        </div>
    </div>
</div>
<script id="whatHappenedTemplate" type="text/template">
    <div class="row">
        <div class="col-md-12">
            <table>
                <@ _.each(data, function(e, i, l) { @>
                <tr>
                    <td><@- e.label @></td>
                </tr>
                <@ }); @>
            </table>
        </div>
    </div>
</script>
<script id="fieldsTemplate" type="text/template">
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
            <div class="row">
                <div class="col-md-12">
                    <@ _.each(data.filters, function(e, i, l) { @>
                    <div class="row" style="margin-top: 10px;border-style: solid;border-color: #CFCCCC">
                        <div class="col-md-12">
                            <div class="row">
                                <div class="col-md-12" style="background-color: #EEEEEE">
                                    <h2><@- e.name @></h2>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="panel-group" id="<@= e.id @>filterAccordion" style="margin-top:10px">
                                        <div class="panel panel-default">
                                            <div class="panel-heading">
                                                <h4 class="panel-title">
                                                    <a data-toggle="collapse" data-parent="<@= e.id @>filterAccordion" href="#<@= e.id @>whatIsFilter">
                                                        What is this filter?
                                                    </a>
                                                </h4>
                                            </div>
                                            <div id="<@= e.id @>whatIsFilter" class="panel-collapse collapse">
                                                <div class="panel-body">
                                                    <@= e.whatIsFilter @>
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
<script id="usageTemplate" type="text/template">
    <div class="row">
        <div class="col-md-12" style="text-align:center;margin: 0;padding: 0">
            <h2><@- data.report.name @></h2>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <@- data.report.description @>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <ul>
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
                        <li><a href="/a/data_sources/<@= e.url_key @>"><@- e.name @></a></li>
                        <@ }); @>
                        <@ } @>
                    </ul>
                </li>
            </ul>
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