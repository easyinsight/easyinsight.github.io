<%--
  User: jamesboe
  Date: Sep 16, 2010
  Time: 9:17:46 AM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head><title></title></head>
  <body>
    <%
        String reportID = request.getParameter("reportID");
        String reportType = request.getParameter("reportType");
        String dataSourceID = request.getParameter("dataSourceID");
        String reportName = request.getParameter("reportName");
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String width = request.getParameter("width");
        String height = request.getParameter("height");
        String seleniumID = request.getParameter("seleniumID");
        String versionDir = new com.easyinsight.users.UserService().getBuildPath();
    %>
  <object width="<%= width %>" height="<%= height %>" codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
      <param name="movie" value="/app/<%=versionDir%>/SeleniumReportView.swf" />
      <param name="quality" value="high" />
      <param name="bgcolor" value="#869ca7" />
      <param name="allowScriptAccess" value="always" />
      <param name="flashvars" value="analysisID=<%=reportID%>&reportType=<%=reportType%>&dataSourceID=<%=dataSourceID%>&reportName=<%=reportName%>&userName=<%=userName%>&password=<%=password%>&seleniumID=<%=seleniumID%>"/> 
      <embed src="https://staging.easy-insight.com/app/<%=versionDir%>/SeleniumReportView.swf" quality="high" bgcolor="##869ca7" width="<%= width %>" height="<%= height %>"
             name="PrimaryWorkspace" align="middle" play="true" loop="false" quality="high" allowScriptAccess="always"
             flashvars="analysisID=<%=reportID%>&reportType=<%=reportType%>&dataSourceID=<%=dataSourceID%>&reportName=<%=reportName%>&userName=<%=userName%>&password=<%=password%>&seleniumID=<%=seleniumID%>"
             type="application/x-shockwave-flash" pluginspage="http://www.adobe.com/go/getflashplayer">
      </embed>
  </object>
  </body>
</html>