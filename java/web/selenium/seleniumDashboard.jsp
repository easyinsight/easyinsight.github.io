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
        String dashboardID = request.getParameter("dashboardID");
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String width = request.getParameter("width");
        String height = request.getParameter("height");
        String seleniumID = request.getParameter("seleniumID");
        String versionDir = new com.easyinsight.users.UserService().getBuildPath().getVersion();
        String orientation = request.getParameter("orientation");
        String showHeader = request.getParameter("showHeader");
        String pdfWidth = request.getParameter("pdfWidth");
        String pdfHeight = request.getParameter("pdfHeight");

        if (pdfWidth != null) {
            width = pdfWidth;
        }
        if (pdfHeight != null) {
            height = pdfHeight;
        }
    %>
  <object width="<%= width %>" height="<%= height %>" codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
      <param name="movie" value="/app/<%=versionDir%>/SeleniumDashboardView.swf" />
      <param name="quality" value="high" />
      <param name="bgcolor" value="#869ca7" />
      <param name="allowScriptAccess" value="always" />
      <param name="flashvars" value="dashboardID=<%=dashboardID%>&userName=<%=userName%>&password=<%=password%>&seleniumID=<%=seleniumID%>&reportWidth=<%=width%>&reportHeight=<%=height%>&orientation=<%=orientation%>&showHeader=<%=showHeader%>"/>
      <embed src="https://www.easy-insight.com/app/<%=versionDir%>/SeleniumDashboardView.swf" quality="high" bgcolor="#869ca7" width="<%= width %>" height="<%= height %>"
             name="PrimaryWorkspace" align="middle" play="true" loop="false" quality="high" allowScriptAccess="always"
             flashvars="dashboardID=<%=dashboardID%>&userName=<%=userName%>&password=<%=password%>&seleniumID=<%=seleniumID%>&reportWidth=<%=width%>&reportHeight=<%=height%>&orientation=<%=orientation%>&showHeader=<%=showHeader%>"
             type="application/x-shockwave-flash" pluginspage="http://www.adobe.com/go/getflashplayer">
      </embed>
  </object>
  </body>
</html>