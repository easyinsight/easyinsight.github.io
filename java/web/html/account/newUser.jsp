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

        UserTransferObject uto = new UserTransferObject();
        if(request.getParameter("userID") != null) {
            long userID = Long.parseLong(request.getParameter("userID"));
            List<UserTransferObject> users = new UserAccountAdminService().getUsers();
            for(UserTransferObject uu: users) {
                if(uu.getUserID() == userID)
                    uto = uu;
            }
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
                    <a href="<%= RedirectUtil.getURL(request, "/account")%>">Account Administration</a>
                </div>
                <div class="btn-group topControlBtnGroup">
                    <a href="../account/users.jsp">Users</a>
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
                <form role="form" action="createUser" method="POST">
                    <input type="hidden" name="type" value="<%= uto.getUserID() == 0 ? request.getParameter("type") : (uto.isAnalyst() ? "designer" : "viewer") %>" />
                    <% if(request.getParameter("message") != null) { %>
                    <div class="form-group has-error">
                        <%= StringEscapeUtils.escapeHtml(request.getParameter("message")) %>
                    </div>
                    <% } %>
                    <div class="form-group">
                        <label for="user_name">User ID</label>
                        <input type="text" class="form-control" id="user_name" name="user_name" placeholder="User ID" <% if(uto.getUserName() != null) { %>value="<%= StringEscapeUtils.escapeHtml(uto.getUserName()) %>" <% } %>/>
                    </div>
                    <div class="form-group">
                        <label for="first_name">First Name</label>
                        <input type="text" class="form-control" id="first_name" name="first_name" placeholder="First Name" <% if(uto.getFirstName() != null) { %>value="<%= StringEscapeUtils.escapeHtml(uto.getFirstName()) %>" <% } %>/>
                    </div>
                    <div class="form-group">
                        <label for="last_name">Last Name</label>
                        <input type="text" class="form-control" id="last_name" name="last_name" placeholder="Last Name" <% if(uto.getName() != null) { %>value="<%= StringEscapeUtils.escapeHtml(uto.getName()) %>" <% } %>/>
                    </div>
                    <div class="form-group">
                        <label for="email_address">Email Address</label>
                        <input type="email" class="form-control" id="email_address" name="email_address" placeholder="Email Address" <% if(uto.getEmail() != null) { %>value="<%= StringEscapeUtils.escapeHtml(uto.getEmail()) %>" <% } %>/>
                    </div>
                    <% if("designer".equals(request.getParameter("type")) || uto.isAnalyst()) { %>
                    <div class="checkbox">
                        <label>
                            <input type="checkbox" name="account_admin" <% if(uto.isAccountAdmin()) { %>checked="checked" <% } %>/> Account Administrator
                        </label>
                    </div>
                    <% } %>
                    <div class="checkbox">
                        <label>
                            <input type="checkbox" name="all_reports_and_dashboards" <% if(uto.isTestAccountVisible()) { %>checked="checked" <% } %>/> User Can See All Reports and Dashboards
                        </label>
                    </div>
                    <div class="checkbox">
                        <label>
                            <input type="checkbox" name="invoice_recipient" <% if(uto.isInvoiceRecipient()) { %>checked="checked" <% } %>/> Invoice Recipient
                        </label>
                    </div>
                    <div class="checkbox">
                        <label>
                            <input type="checkbox" name="newsletter" <% if(uto.isOptInEmail()) { %>checked="checked" <% } %>/> Send Newsletter to User
                        </label>
                    </div>
                    <% if(request.getParameter("edit") == null) { %>
                    <div class="checkbox">
                        <label>
                            <input type="checkbox" name="require_password_change" /> Require Password Change
                        </label>
                    </div>
                    <% } else { %>
                        <input type="hidden" name="user_id" value="<%= uto.getUserID() %>" />
                    <% } %>
                    <div class="form-group">
                        <input type="submit" value="<%= uto.getUserID() == 0 ? "Create" : "Update" %> User" />
                    </div>
                </form>
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