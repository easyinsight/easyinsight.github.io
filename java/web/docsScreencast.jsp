0<%@ page import="com.easyinsight.documentation.DocReader" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<!DOCTYPE html>
<html lang="en">
<%
    String main = DocReader.toHTML(null, request, DocReader.APP);
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
    <title>Easy Insight Screencasts</title>
    <jsp:include page="html/bootstrapHeader.jsp"/>
</head>
<body>
<jsp:include page="header.jsp">
    <jsp:param name="userName" value="<%= userName %>"/>
    <jsp:param name="headerActive" value="<%= HtmlConstants.HELP %>"/>
</jsp:include>
<div class="container corePageWell">
    <div class="row">
        <div class="col-md-8 col-md-offset-1">
            <div class="row">
                <h2 class="productHeader">Screencasts</h2>
                <ul>
                    <li><a href="#creatingConnection">Creating a Connection in Easy Insight</a></li>
                    <li><a href="#navigating">Navigating the Easy Insight user interface</a></li>
                    <li><a href="#basicReportEditing">Basic Report Editing</a></li>
                    <li><a href="#creatingFilters">Creating Filters</a></li>
                </ul>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <h2 class="productHeader" id="creatingConnection">Creating a Connection in Easy Insight</h2>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <iframe width="600" height="359" src="//www.youtube.com/embed/gnqhnJN_xtE?modestbranding=1&rel=0&theme=light" frameborder="0" allowfullscreen></iframe>
                </div>
            </div>
            <hr style="color: #DDDDDD; border-color: #DDDDDD; background-color: #DDDDDD; height: 1px; border: 0"/>
            <div class="row">
                <div class="col-md-12">
                    <h2 class="productHeader" id="navigating">Navigating the Easy Insight user interface</h2>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <iframe width="600" height="359" src="//www.youtube.com/embed/XCzydb2eyrs?modestbranding=1&rel=0&theme=light" frameborder="0" allowfullscreen></iframe>
                </div>
            </div>
            <hr style="color: #DDDDDD; border-color: #DDDDDD; background-color: #DDDDDD; height: 1px; border: 0"/>
            <div class="row">
                <div class="col-md-12">
                    <h2 class="productHeader" id="basicReportEditing">Basic Report Editing</h2>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <iframe width="600" height="359" src="//www.youtube.com/embed/sIN5tGyWI1Y?modestbranding=1&rel=0&theme=light" frameborder="0" allowfullscreen></iframe>
                </div>
            </div>
            <hr style="color: #DDDDDD; border-color: #DDDDDD; background-color: #DDDDDD; height: 1px; border: 0"/>
            <div class="row">
                <div class="col-md-12">
                    <h2 class="productHeader" id="creatingFilters">Creating Filters</h2>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <iframe width="600" height="359" src="//www.youtube.com/embed/So2IW_3CmdI?modestbranding=1&rel=0&theme=light" frameborder="0" allowfullscreen></iframe>
                </div>
            </div>
        </div>
        <% if (main != null) { %>
        <div class="col-md-3 sidedocs well">
            <%= main %>
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