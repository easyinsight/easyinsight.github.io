<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.core.DataSourceDescriptor" %>
<%@ page import="com.easyinsight.core.EIDescriptor" %>
<%@ page import="com.easyinsight.core.InsightDescriptor" %>
<%@ page import="com.easyinsight.userupload.UserUploadService" %>
<%@ page import="com.easyinsight.dashboard.DashboardDescriptor" %>
<%@ page import="com.easyinsight.audit.ActionLog" %>
<%@ page import="com.easyinsight.admin.AdminService" %>
<%@ page import="com.easyinsight.audit.ActionReportLog" %>
<%@ page import="com.easyinsight.audit.ActionDashboardLog" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="com.easyinsight.datafeeds.FeedStorage" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="com.easyinsight.userupload.CustomFolder" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="java.util.*" %>
<%@ page import="com.easyinsight.core.DataFolder" %>
<%@ page import="com.easyinsight.html.Utils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <style type="text/css">
        #refreshDiv {
            display: none;
        }
    </style>
    <title>Easy Insight Reports and Dashboards</title>
    <jsp:include page="bootstrapHeader.jsp"/>
    <script type="text/javascript" src="/js/dashboard.js"></script>
</head>
<body>
<%
    String userName = (String) session.getAttribute("userName");
    com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    try {
        boolean phone = Utils.isPhone(request);
        boolean iPad = Utils.isTablet(request);
        boolean designer = Utils.isDesigner();

        String dataSourceKey = request.getParameter("dataSourceID");
        long dataSourceID = new FeedStorage().dataSourceIDForDataSource(dataSourceKey);
        DataSourceDescriptor dataSourceDescriptor = new FeedStorage().dataSourceURLKeyForDataSource(dataSourceID);

%>
<jsp:include page="../header.jsp">
    <jsp:param name="userName" value="<%= userName %>"/>
    <jsp:param name="headerActive" value="<%= HtmlConstants.DATA_SOURCES_AND_REPORTS %>"/>
</jsp:include>
<div class="container corePageWell">
    <div class="row">
        <jsp:include page="../recent_actions.jsp"/>
        <div class="col-md-9">
            <div class="container">
                <div class="row">
                    <div class="col-md-12">
                        <h2><%= dataSourceDescriptor.getName() %></h2>
                    </div>
                </div>
                <% if (designer && !phone && !iPad) { %>
                <jsp:include page="refreshingDataSource.jsp"/>
                <div class="row" style="margin-bottom:10px">
                    <div class="col-md-6">
                        <div class="btn-toolbar pull-left">
                            <div class="btn-group reportControlBtnGroup">
                                <a class="reportControl" data-toggle="dropdown" href="#">
                                    Create
                                    <span class="caret"></span>
                                </a>
                                <ul class="dropdown-menu">
                                    <li>
                                        <button class="btn btn-inverse" type="button"
                                                onclick="window.location.href='/app/embeddedReportEditor.jsp?dataSourceID=<%= dataSourceDescriptor.getUrlKey() %>'"
                                                style="padding:5px;margin:5px;width:150px">Create Report
                                        </button>
                                    </li>
                                    <li>
                                        <button class="btn btn-inverse" type="button"
                                                onclick="window.location.href='/app/embeddedDashboardEditor.jsp?dataSourceID=<%= dataSourceDescriptor.getUrlKey() %>'"
                                                style="padding:5px;margin:5px;width:150px">Create Dashboard
                                        </button>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="btn-toolbar pull-right">
                            <div class="btn-group reportControlBtnGroup">
                                <a class="reportControl" href="#" onclick="refreshDataSource('<%= dataSourceDescriptor.getUrlKey() %>')">Refresh the Data Source</a>
                            </div>
                            <div class="btn-group reportControlBtnGroup">
                                <a class="reportControl" href="/app/embeddedConfigureDataSource.jsp?dataSourceID=<%= dataSourceDescriptor.getUrlKey() %>">Configure the Data Source</a>
                            </div>
                        </div>
                    </div>
                </div>
                <% } %>
                <div class="row">
                    <div class="col-md-12">
                        <%
                            Map<Long, CustomFolder> folderMap = new HashMap<Long, CustomFolder>();
                            EIConnection conn = Database.instance().getConnection();
                            try {
                                PreparedStatement getFoldersStmt = conn.prepareStatement("SELECT REPORT_FOLDER_ID, FOLDER_NAME, DATA_SOURCE_ID FROM REPORT_FOLDER WHERE DATA_SOURCE_ID = ?");
                                getFoldersStmt.setLong(1, dataSourceID);

                                ResultSet folderRS = getFoldersStmt.executeQuery();
                                while (folderRS.next()) {
                                    long id = folderRS.getLong(1);
                                    String name = folderRS.getString(2);
                                    CustomFolder customFolder = new CustomFolder();
                                    customFolder.setName(name);
                                    customFolder.setId(id);
                                    folderMap.put(id, customFolder);
                                }
                            } finally {
                                Database.closeConnection(conn);
                            }


                            List<EIDescriptor> descriptors = new UserUploadService().getFeedAnalysisTreeForDataSource(new DataSourceDescriptor(null, dataSourceID, 0, false, 0));

                            List<EIDescriptor> forThisLevel = new ArrayList<EIDescriptor>();
                            boolean additionalViewsUsed = false;
                            for (EIDescriptor desc : descriptors) {
                                int folder = desc.getFolder();
                                if (folder == 1) {
                                    forThisLevel.add(desc);
                                } else if (folder == 2) {
                                    additionalViewsUsed = true;
                                } else {

                                }
                            }

                            Collections.sort(forThisLevel, new Comparator<EIDescriptor>() {

                                public int compare(EIDescriptor eiDescriptor, EIDescriptor eiDescriptor1) {
                                    String name1 = eiDescriptor.getName() != null ? eiDescriptor.getName().toLowerCase() : "";
                                    String name2 = eiDescriptor1.getName() != null ? eiDescriptor1.getName().toLowerCase() : "";
                                    return name1.compareTo(name2);
                                }
                            });
                            List<DataFolder> folders = new ArrayList<DataFolder>();
                            if (additionalViewsUsed) {
                                DataFolder dataFolder = new DataFolder();
                                dataFolder.setName("Additional Views");
                                dataFolder.setUrlKey("AdditionalViews");
                                folders.add(dataFolder);
                            }
                            for (CustomFolder customFolder : folderMap.values()) {
                                DataFolder dataFolder = new DataFolder();
                                dataFolder.setName(customFolder.getName());
                                dataFolder.setUrlKey(String.valueOf(customFolder.getId()));
                                folders.add(dataFolder);
                            }

                            if (forThisLevel.size() > 0) {
                        %>
                        <table class="table table-striped table-bordered">
                            <thead>
                            <tr>
                                <th>Name</th>
                                <th>Type</th>
                            </tr>
                            </thead>
                            <%


                                for (EIDescriptor descriptor : forThisLevel) {
                                    if (descriptor instanceof InsightDescriptor || descriptor instanceof DashboardDescriptor) {
                                        out.println("<tr>");
                                        if (descriptor instanceof InsightDescriptor) {
                                            out.println("<td><a href=\"../report/" + descriptor.getUrlKey() + "\">" + descriptor.getName() + "</td>");
                                            out.println("<td>Report</td>");
                                        } else if (descriptor instanceof DashboardDescriptor) {
                                            out.println("<tr><td><a href=\"../dashboard/" + descriptor.getUrlKey() + "\">" + descriptor.getName() + "</td>");
                                            out.println("<td>Dashboard</td>");
                                        }
                                        out.println("</tr>");
                                    }
                                }
                            %>
                        </table>
                        <% } else { %>
                            You don't have any reports yet for this data source.
                        <% } %>
                        <%
                            if (folders.size() > 0) {
                        %>
                        <table class="table table-striped table-bordered">
                            <thead>
                            <tr>
                                <th>Folder Name</th>
                            </tr>
                            </thead>
                            <%
                                for (DataFolder dataFolder : folders) {
                                    out.println("<tr><td><a href=\"../reportsFolder/" + dataSourceDescriptor.getUrlKey() + "/" + dataFolder.getUrlKey() + "\">" + dataFolder.getName() + "</td></tr>");
                                }
                            %>
                        </table>
                        <%
                            }
                        %>
                    </div>
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