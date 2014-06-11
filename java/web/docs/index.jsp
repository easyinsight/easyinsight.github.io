<%@ page import="com.easyinsight.documentation.DocReader" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<!DOCTYPE html>
<html lang="en">
<%
    String doc = request.getParameter("doc");
    String html = DocReader.toHTML(doc, request, DocReader.APP);
    String userName = (String) session.getAttribute("userName");
    if (userName != null) {
        com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    }

    try {
%>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight Documentation</title>
    <jsp:include page="../html/bootstrapHeader.jsp"/>
</head>
<body>
<jsp:include page="../header.jsp">
    <jsp:param name="userName" value="<%= userName %>"/>
    <jsp:param name="headerActive" value="<%= HtmlConstants.HELP %>"/>
</jsp:include>
<div class="container corePageWell">
    <%= html %>
</div>
</body>
<%
    } finally {
        if (userName != null) {
            SecurityUtil.clearThreadLocal();
        }
    }
%>
</html>