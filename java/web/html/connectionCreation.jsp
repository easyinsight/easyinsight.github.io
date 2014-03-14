<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page import="com.easyinsight.datafeeds.HTMLConnectionFactory" %>
<%@ page import="com.easyinsight.datafeeds.HTMLConnectionProperty" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<%
    String userName = (String) session.getAttribute("userName");
    com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    try {
        int connectionID = Integer.parseInt(request.getParameter("connectionID"));
        HTMLConnectionFactory factory = new HTMLConnectionFactory(connectionID);



%>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title><%= "Easy Insight Connection to " + factory.getName()%></title>
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
        <div class="col-md-6 col-md-offset-3">
            <form class="well" method="post" action="/app/html/connectionCreationAction.jsp">
                <div style="margin-bottom: 10px"><strong><%= factory.getTitle() %></strong></div>
                <%
                    if (request.getParameter("error") != null) {
                        %><div style="margin-bottom: 10px">Something went wrong in trying to create the connection. Check the configuration information you used.</div><%
                }   %>
                <input type="hidden" id="connectionType" name="connectionType" value="<%= connectionID%>"/>
                <%
                    for (HTMLConnectionProperty property : factory.getProperties()) {

                        %>
                <label for="<%= property.getSafeProperty()%>" class="promptLabel"><%=property.getField()%></label>
                <%
                    if (property.isPassword()) {
                %>
                <input type="password" class="form-control" name="<%= property.getSafeProperty()%>" id="<%= property.getSafeProperty()%>"/>
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
                <button class="btn btn-inverse" style="margin-top: 20px" type="submit" value="Create"><%= factory.getType() == HTMLConnectionFactory.TYPE_OAUTH ? "Authorize Access" : "Create the Connection" %></button>
            </form>
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