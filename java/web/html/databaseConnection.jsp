<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page import="com.easyinsight.datafeeds.HTMLConnectionFactory" %>
<%@ page import="com.easyinsight.datafeeds.HTMLConnectionProperty" %>
<%@ page import="com.easyinsight.html.RedirectUtil" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<%
    String userName = (String) session.getAttribute("userName");
    com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    try {
        int connectionID = Integer.parseInt(request.getParameter("connectionID"));

        HTMLConnectionFactory factory;
        try {
            factory = new HTMLConnectionFactory(connectionID);
        } catch (UnsupportedOperationException e) {
            response.sendRedirect(RedirectUtil.getURL(request, "/app/html/fullAppRequired.jsp"));
            return;
        }


%>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>New Easy Insight Connection</title>
    <jsp:include page="bootstrapHeader.jsp"/>
    <jsp:include page="reportDashboardHeader.jsp"/>
</head>
<body>

<jsp:include page="../header.jsp">
    <jsp:param name="userName" value="<%= userName %>"/>
    <jsp:param name="headerActive" value="<%= HtmlConstants.NONE %>"/>
</jsp:include>
<div class="container corePageWell" style="margin-top: 20px">
    <div class="row">
        <div class="col-md-10 col-md-offset-1">
            <h3>We offer two options for connecting Easy Insight to MySQL:</h3>
        </div>
    </div>
    <div class="row">
        <div class="col-md-10 col-md-offset-1">
            <p>You can download a Java web application to run behind your firewall and publish the data out to Easy Insight. This web application can run in any standard Java servlet container. The web application can be downloaded <a href="">here.</a></p>
        </div>
    </div>
    <div class="row">
        <div class="col-md-10 col-md-offset-1">
            <p>You can also directly connect Easy Insight to your database by granting firewall permissions to a fixed IP of db.easy-insight.com (107.21.250.229) with the configuration form below.</p>
        </div>
    </div>
    <div class="row">
        <div class="col-md-6 col-md-offset-2">
            <form class="well" method="post" action="/app/html/connectionCreationAction.jsp">
                <div style="margin-bottom: 10px"><strong>Let's create your connection to the database...</strong></div>
                <%
                    if (request.getParameter("error") != null) {
                        %><div style="margin-bottom: 10px">Something went wrong in trying to create the connection. Check the configuration information you used.</div><%
                }   %>
                <input type="hidden" id="connectionType" name="connectionType" value="<%= connectionID%>"/>
                <label for="pdataSourceName" class="promptLabel">Data Source Name</label>
                <input type="text" class="form-control" name="pdataSourceName" id="pdataSourceName"/>
                <%
                    for (HTMLConnectionProperty property : factory.getProperties()) {

                %>
                <label for="<%= property.getSafeProperty()%>" class="promptLabel"><%=property.getField()%></label>
                <%
                    if (property.isPassword()) {
                %>
                <input type="password" class="form-control" name="<%= property.getSafeProperty()%>" id="<%= property.getSafeProperty()%>"/>
                <%
                } else if (property.getType() == HTMLConnectionProperty.TEXT) {
                %>
                <textarea class="form-control" rows="5" name="<%= property.getSafeProperty()%>" id="<%= property.getSafeProperty()%>"></textarea>
                <%
                } else {
                %>
                <input type="text" class="form-control" name="<%= property.getSafeProperty()%>" id="<%= property.getSafeProperty()%>"/>
                <%
                    }
                %>
                <%
                    }
                %>

                <button class="btn btn-inverse" style="margin-top: 20px" type="submit" value="Create">Create the Connection</button>
            </form>
        </div>
        <div class="col-md-3">
            <p>For the server side database connection to work, you'll need to open up your firewall to allow access on the database port to db.easy-insight.com (107.21.250.229). If you can't allow access through the firewall, you'll need to download and use the alternate database connection, hosting that behind your firewall. If you're running on the Amazon Web Services cloud, the configuration will be a little different--please contact us at support@easy-insight.com or 1-720-316-8174 and we'll work with you to complete setup.</p>
            <hr>
            <p>The query defines what fields and data to pull back into Easy Insight. For example, you might do 'select * from table_name', 'select a, b from table_name' or 'select a as A, b as B from table_name where X = Y'.</p>
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