<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.core.DataSourceDescriptor" %>
<%@ page import="com.easyinsight.core.EIDescriptor" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.util.Collections" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="com.easyinsight.tag.Tag" %>
<%@ page import="com.easyinsight.userupload.UserUploadService" %>
<%@ page import="com.easyinsight.export.ExportService" %>
<%@ page import="com.easyinsight.analysis.AnalysisDateDimension" %>
<%@ page import="java.text.DateFormat" %>
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
        try {
            java.util.List<DataSourceDescriptor> dataSources = new com.easyinsight.datafeeds.FeedService().searchForSubscribedFeeds();
            Collections.sort(dataSources, new Comparator<EIDescriptor>() {

                public int compare(EIDescriptor eiDescriptor, EIDescriptor eiDescriptor1) {
                    String name1 = eiDescriptor.getName() != null ? eiDescriptor.getName().toLowerCase() : "";
                    String name2 = eiDescriptor1.getName() != null ? eiDescriptor1.getName().toLowerCase() : "";
                    return name1.compareTo(name2);
                }
            });
            JSONArray ja = new JSONArray();
            DateFormat dateFormat = ExportService.getDateFormatForAccount(AnalysisDateDimension.MINUTE_LEVEL, null);
            for (DataSourceDescriptor d : dataSources) {
                ja.put(d.toJSON(dateFormat));
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
        <jsp:include page="../recent_actions.jsp"/>
        <div class="col-md-9">
            <div class="tag-list">
                <% for(Tag t : new UserUploadService().getDataSourceTags()) { %>
                <a class="btn btn-default tag-select" data-toggle="button" data-tag-id="<%= t.getId() %>" href="#"><%= StringEscapeUtils.escapeHtml(t.getName()) %></a>
                <% } %>
            </div>

            <div id="data_sources">

            </div>

        </div>
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
            <td style="font-weight:500">
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
        SecurityUtil.clearThreadLocal();
    }
%>
</html>