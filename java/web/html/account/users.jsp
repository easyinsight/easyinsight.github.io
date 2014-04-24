<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page import="com.easyinsight.users.UserAccountAdminService" %>
<%@ page import="com.easyinsight.users.UserTransferObject" %>
<%@ page import="java.util.List" %>
<%@ page import="com.easyinsight.html.RedirectUtil" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight Account</title>
    <jsp:include page="../bootstrapHeader.jsp"/>
</head>
<body>
<%
    String userName = (String) session.getAttribute("userName");
    com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    try {

        if (!SecurityUtil.isAccountAdmin()) {
            response.sendRedirect(RedirectUtil.getURL(request, "/app/html/nonAdminProfile.jsp"));
            return;
        }
%>
<jsp:include page="../../header.jsp">
    <jsp:param name="userName" value="<%= userName %>"/>
    <jsp:param name="headerActive" value="<%= HtmlConstants.ACCOUNT %>"/>
</jsp:include>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <div class="btn-toolbar pull-right topControlToolbar">
                <div class="btn-group topControlBtnGroup">
                    <a href="<%= RedirectUtil.getURL(request, "/app/billing/account.jsp")%>">Account Administration</a>
                </div>
                <div class="btn-group topControlBtnGroup">
                    <a href="#">Users</a>
                </div>
                <div class="btn-group topControlBtnGroup">
                    <a href="../profile.jsp">My Profile</a>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <div class="container corePageWell">
                <div class="row">
                    <div class="col-md-12">
                        <div class="btn-grp pull-right"><a href="newDesigner" class="btn">Add Designer</a><a href="newViewer" class="btn">Add Viewer</a></div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">

                        <table class="table table-striped table-bordered">
                            <thead>
                            <tr>
                                <th style="width: 8em;"></th>
                                <th>Email</th>
                                <th>Name</th>
                                <th>Type</th>
                            </tr>
                            </thead>
                            <%
                                List<UserTransferObject> users = new UserAccountAdminService().getUsers();
                                for (UserTransferObject user : users) {
                            %>
                            <tr>
                                <td><a href="edit?userID=<%= user.getUserID() %>">Edit</a> <a href="delete?userID=<%= user.getUserID() %>">Delete</a></td>
                                <td><%=StringEscapeUtils.escapeHtml(user.getEmail()) %>
                                </td>
                                <td><%= StringEscapeUtils.escapeHtml(user.getFirstName() + " " + user.getName()) %>
                                </td>
                                <td><%= user.isAnalyst() ? "Designer" : "Viewer" %>
                                </td>
                            </tr>
                            <%
                                }
                            %>
                        </table>
                    </div>
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