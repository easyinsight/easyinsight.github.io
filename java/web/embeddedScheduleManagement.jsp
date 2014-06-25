<!DOCTYPE html>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<html lang="en">

<head>
    <%
        String userName = (String) session.getAttribute("userName");
        com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
        try {
        String versionDir = new com.easyinsight.users.UserService().getBuildPath().getVersion();




            String swf = "EmbeddedComponentApplication";

            String flashVars = "embedded=4";



        /*String ua=request.getHeader("User-Agent").toLowerCase();
        if(ua.matches(".*(android|avantgo|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|symbian|treo|up\\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino).*")||ua.substring(0,4).matches("1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|e\\-|e\\/|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(di|rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|xda(\\-|2|g)|yas\\-|your|zeto|zte\\-")) {
            response.sendRedirect(RedirectUtil.getURL(request, "/app/html/embeddedReport/" + dashboardURLKey));
            return;
        } else if (ua.matches("ipad")) {
            response.sendRedirect(RedirectUtil.getURL(request, "/app/html/embeddedReport/" + dashboardURLKey));
            return;
        }*/

    %>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>



    <script src="AC_OETags.js" language="javascript"></script>

    <!--  BEGIN Browser History required section -->
    <script src="history/history.js" language="javascript"></script>
    <!--  END Browser History required section -->

    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight Reports and Dashboards</title>
    <jsp:include page="html/bootstrapHeader.jsp"/>

    <style type="text/css">
    </style>

    <script language="JavaScript" type="text/javascript">
        <!--
        // -----------------------------------------------------------------------------
        // Globals
        // Major version of Flash required
        var requiredMajorVersion = 10;
        // Minor version of Flash required
        var requiredMinorVersion = 0;
        // Minor version of Flash required
        var requiredRevision = 0;
        // -----------------------------------------------------------------------------
        // -->
    </script>
</head>


<body>
<jsp:include page="header.jsp">
    <jsp:param name="userName" value="<%= userName %>"/>
    <jsp:param name="headerActive" value="<%= HtmlConstants.SCHEDULING %>"/>
</jsp:include>
    <div>
            <script language="JavaScript" type="text/javascript">

                var hasProductInstall = DetectFlashVer(6, 0, 65);
                var hasRequestedVersion = DetectFlashVer(requiredMajorVersion, requiredMinorVersion, requiredRevision);
                if (hasProductInstall && !hasRequestedVersion) {

                    var MMPlayerType = (isIE == true) ? "ActiveX" : "PlugIn";
                    var MMredirectURL = window.location;
                    document.title = document.title.slice(0, 47) + " - Flash Player Installation";
                    var MMdoctitle = document.title;

                    AC_FL_RunContent(
                            "src", "playerProductInstall",
                            "FlashVars", "MMredirectURL=" + MMredirectURL + '&MMplayerType=' + MMPlayerType + '&MMdoctitle=' + MMdoctitle + "",
                            "width", "100%",
                            "height", "100%",
                            "align", "middle",
                            "id", "<%=swf%>",
                            "quality", "high",
                            "bgcolor", "#ffffff",
                            "name", "<%=swf%>",
                            "allowScriptAccess", "sameDomain",
                            "type", "application/x-shockwave-flash",
                            "pluginspage", "http://www.adobe.com/go/getflashplayer",
                            "wmode", "window"
                    );
                } else if (hasRequestedVersion) {
                    // if we've detected an acceptable version
                    // embed the Flash Content SWF when all tests are passed
                    AC_FL_RunContent(
                            "src", "<%=versionDir%>/<%=swf%>",
                            "width", "100%",
                            "height", "100%",
                            "align", "middle",
                            "id", "<%=swf%>",
                            "quality", "high",
                            "bgcolor", "#ffffff",
                            "name", "<%=swf%>",
                            "flashvars", "<%=flashVars%>",
                            "allowScriptAccess", "sameDomain",
                            "type", "application/x-shockwave-flash",
                            "pluginspage", "http://www.adobe.com/go/getflashplayer",
                            "wmode", "window",
                            "allowFullScreen", "true"
                    );
                } else {  // flash is too old or we can't detect the plugin
                    var alternateContent = 'Alternate HTML content should be placed here. '
                            + 'This content requires the Adobe Flash Player. '
                            + '<a href=http://www.adobe.com/go/getflash/>Get Flash</a>';
                    document.write(alternateContent);  // insert non-flash content
                }
                // -->
            </script>
            <noscript>
                <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
                        id="<%=swf%>" width="100%" height="100%"
                        codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
                    <param name="movie" value="<%=swf%>.swf"/>
                    <param name="quality" value="high"/>
                    <param name="bgcolor" value="#ffffff"/>
                    <param name="allowScriptAccess" value="sameDomain"/>
                    <param name="allowFullScreen" value="true"/>
                    <embed src="<%= versionDir%>/<%=swf%>.swf" quality="high" bgcolor="#ffffff"
                           width="100%" height="100%" name="<%=swf%>" align="middle"
                           play="true"
                           loop="false"
                           quality="high"
                           flashvars="<%=flashVars%>"
                           allowScriptAccess="sameDomain"
                           type="application/x-shockwave-flash"
                           pluginspage="http://www.adobe.com/go/getflashplayer">
                    </embed>
                </object>
            </noscript>

    </div>
</body>
<% } finally {
    SecurityUtil.clearThreadLocal();
}
%>

</html>