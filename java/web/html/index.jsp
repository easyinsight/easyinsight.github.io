<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.core.DataSourceDescriptor" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="com.easyinsight.tag.Tag" %>
<%@ page import="com.easyinsight.userupload.UserUploadService" %>
<%@ page import="com.easyinsight.export.ExportService" %>
<%@ page import="com.easyinsight.jsphelpers.EIHelper" %>
<%@ page import="com.easyinsight.export.ExportMetadata" %>
<%@ page import="com.easyinsight.analysis.InsightRequestMetadata" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="java.util.List" %>
<%@ page import="com.easyinsight.core.EIDescriptor" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight Data Sources</title>
    <jsp:include page="bootstrapHeader.jsp"/>
    <%
        String userName = (String) session.getAttribute("userName");
        com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
        EIConnection conn = Database.instance().getConnection();
        try {

            java.util.List<DataSourceDescriptor> dataSources = new com.easyinsight.datafeeds.FeedService().searchForSubscribedFeeds();
            EIHelper.sortStuff(dataSources);
            JSONArray ja = new JSONArray();
            ExportMetadata md = ExportService.createExportMetadata(SecurityUtil.getAccountID(), conn, new InsightRequestMetadata());
            for (DataSourceDescriptor d : dataSources) {
                ja.put(d.toJSON(md));
            }
    %>
    <script type="text/javascript" src="/js/underscore-min.js"></script>
    <script type="text/javascript" src="/js/datasources.js"></script>
    <script type="text/javascript" language="JavaScript">
        var dataSources = <%= ja.toString() %>;
    </script>
</head>
<body>
<jsp:include page="../header.jsp">
    <jsp:param name="userName" value="<%= userName %>"/>
    <jsp:param name="headerActive" value="<%= HtmlConstants.DATA_SOURCES_AND_REPORTS %>"/>
</jsp:include>
<div class="container corePageWell">
    <div class="row">
        <%
            if (dataSources.size() == 0) {
        %>

        <div class="col-md-6 col-md-offset-3 no-data-sources-container">
            <div class="well">

                    <div class="row">
                        <div class="col-md-12">
                            <div class="center-block no-data-sources">You don't have any data sources yet. Let's get started!</div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="center-block no-data-sources"><a href="connections.jsp">Add Your Own Data</a></div>
                        </div>
                    </div>
                </div>
        </div>

        <%
        } else {
        %>
        <jsp:include page="../recent_actions.jsp"/>
        <div class="col-md-9">
            <%
                List<EIDescriptor> accountReports = new UserUploadService().getAccountReports();
                if (accountReports.size() > 0) {
            %>

                <div class="row">
                    <div class="col-md-9">
                        <table class="table table-striped table-bordered table-condensed"
                               style="margin-bottom: 0;padding-bottom: 0">
                            <thead>
                            <tr>
                                <th colspan="2">Your Top Reports and Dashboards</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                for (EIDescriptor accountReport : accountReports) {
                            %>
                            <tr>
                                <td class="header-tr">
                                    <%
                                        if (accountReport.getType() == EIDescriptor.REPORT) {
                                    %>
                                    <a href="reports/<%=accountReport.getUrlKey()%>"><%=StringEscapeUtils.escapeHtml(accountReport.getName())%>
                                    </a>
                                    <%
                                    } else if (accountReport.getType() == EIDescriptor.DASHBOARD) {
                                    %>
                                    <a href="dashboard/<%=accountReport.getUrlKey()%>"><%=StringEscapeUtils.escapeHtml(accountReport.getName())%>
                                    </a>
                                    <%
                                        }
                                    %>
                                </td>
                                <td>
                                    <%
                                        if (accountReport.getType() == EIDescriptor.REPORT) {
                                    %>
                                    <a href="reports/<%=accountReport.getUrlKey()%>"><%=StringEscapeUtils.escapeHtml(accountReport.getDescription())%>
                                    </a>
                                    <%
                                    } else if (accountReport.getType() == EIDescriptor.DASHBOARD) {
                                    %>
                                    <a href="dashboard/<%=accountReport.getUrlKey()%>"><%=StringEscapeUtils.escapeHtml(accountReport.getDescription())%>
                                    </a>
                                    <%
                                        }
                                    %>
                                </td>
                            </tr>
                            <%
                                }
                            %>
                            </tbody>
                        </table>
                    </div>
                </div>

            <%
                }
            %>



                <div class="row">
                    <div class="col-md-12">
                        <h2>Data Sources</h2>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <%
                            List<Tag> tags = new UserUploadService().getDataSourceTags();
                            if (tags.size() > 0) {
                        %>

                            <div class="row">
                                <div class="browse-by-tag">
                                    Browse by Tag:
                                </div>
                                <div class="col-md-7">
                                    <div class="tag-list">
                                        <% for (Tag t : tags) { %>
                                        <a class="btn btn-default tag-select" data-toggle="button"
                                           data-tag-id="<%= t.getId() %>"
                                           href="#"><%= StringEscapeUtils.escapeHtml(t.getName()) %>
                                        </a>
                                        <% } %>
                                    </div>
                                </div>
                            </div>

                        <%
                            }
                        %>
                        <div id="data_sources">

                        </div>
                    </div>
                </div>

            </div>

        <%
            }
        %>
    </div>
</div>

<script id="data_source_template" type="text/template">
    <table class="table table-striped table-bordered table-condensed">
        <thead>
        <tr>
            <th>Data Source Name</th>
            <th style="width:150px">Last Refresh Time</th>
        </tr>
        </thead>
        <tbody>
        <@ _.each(data_sources, function(e,i,l) { @>
        <tr>
            <td class="header-tr">
                <a href="reports/<@- e.url_key @>"><@- e.name @>
                </a>
            </td>
            <td>
                <@- e.last_refresh_time @>
            </td>
        </tr>
        <@ }) @>
        </tbody>
    </table>
</script>
</body>
<%
    } finally {
        Database.closeConnection(conn);
        SecurityUtil.clearThreadLocal();
    }
%>
</html>