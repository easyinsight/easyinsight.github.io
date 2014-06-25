<%@ page import="com.easyinsight.documentation.DocReader" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<!DOCTYPE html>
<html lang="en">
<%
    String doc = request.getParameter("doc");
    String html = DocReader.toHTML(doc, request, DocReader.APP);
    String main = null;
    if (doc != null && !"".equals(doc)) {
        main = DocReader.toHTML(null, request, DocReader.WEBSITE);
    } else {
        main = html;
        html = DocReader.toHTML("DocWelcome", request, DocReader.WEBSITE);
    }
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
    <div class="row">
        <div class="col-md-8">
            <%= html %>
        </div>
        <% if (main != null) { %>
        <div class="col-md-offset-1 col-md-3">
            <div class="row">
                <div class="col-md-12" style="background-color: #F5F5F5;border-color: #DCDCDC;border-style: ridge;border-width: 1px;border-radius: 3px">
                    <%= main %>
                </div>
            </div>
        </div>
        <% } %>
    </div>
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