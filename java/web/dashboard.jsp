<%@ page import="com.easyinsight.users.UserService" %>
<html lang="en">

<head>
    <%
    String versionDir = new com.easyinsight.users.UserService().getBuildPath().getVersion();
    String width = request.getParameter("width");
    String height = request.getParameter("height");
    String dashboardID = request.getParameter("id");

    String flashVars = "id=" + dashboardID + "&showToolbar=";
    String showToolbar = request.getParameter("showToolbar");
    if(showToolbar != null) {
        flashVars = flashVars + "&showToolbar=" + showToolbar;
    }
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!--  BEGIN Browser History required section -->
<link rel="stylesheet" type="text/css" href="history/history.css" />
<link rel="icon" type="image/ico" href="favicon.ico"/>
<!--  END Browser History required section -->

<title>Easy Insight</title>
<meta name="description" content="Easy Insight Business Analytics Application" />
<script src="AC_OETags.js" language="javascript"></script>

<!--  BEGIN Browser History required section -->
<script src="history/history.js" language="javascript"></script>
<!--  END Browser History required section -->

<style>
body { margin: 0px; overflow:hidden }
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



<body scroll="no">
<script language="JavaScript" type="text/javascript">
<!--
// Version check for the Flash Player that has the ability to start Player Product Install (6.0r65)
var hasProductInstall = DetectFlashVer(6, 0, 65);

// Version check based upon the values defined in globals
var hasRequestedVersion = DetectFlashVer(requiredMajorVersion, requiredMinorVersion, requiredRevision);

if ( hasProductInstall && !hasRequestedVersion ) {
	// DO NOT MODIFY THE FOLLOWING FOUR LINES
	// Location visited after installation is complete if installation is required
	var MMPlayerType = (isIE == true) ? "ActiveX" : "PlugIn";
	var MMredirectURL = window.location;
    document.title = document.title.slice(0, 47) + " - Flash Player Installation";
    var MMdoctitle = document.title;

	AC_FL_RunContent(
		"src", "playerProductInstall",
		"FlashVars", "MMredirectURL="+MMredirectURL+'&MMplayerType='+MMPlayerType+'&MMdoctitle='+MMdoctitle+"",
		"width", "<%=width%>",
		"height", "<%=height%>",
		"align", "middle",
		"id", "EmbeddedInsight",
		"quality", "high",
		"bgcolor", "#ffffff",
		"name", "EmbeddedInsight",
		"allowScriptAccess","sameDomain",
		"type", "application/x-shockwave-flash",
		"pluginspage", "http://www.adobe.com/go/getflashplayer",
		"wmode", "window"
	);
} else if (hasRequestedVersion) {
	// if we've detected an acceptable version
	// embed the Flash Content SWF when all tests are passed
	AC_FL_RunContent(
			"src", "<%=versionDir%>/EmbeddedDashboard",
			"width", "<%=width%>",
			"height", "<%=height%>",
			"align", "middle",
			"id", "EmbeddedDashboard",
			"quality", "high",
			"bgcolor", "#ffffff",
			"name", "EmbeddedDashboard",
            "flashvars", "<%=flashVars%>",
			"allowScriptAccess","sameDomain",
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
			id="EmbeddedInsight" width="100%" height="100%"
			codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
			<param name="movie" value="EmbeddedDashboard.swf" />
			<param name="quality" value="high" />
			<param name="bgcolor" value="#ffffff" />
			<param name="allowScriptAccess" value="sameDomain" />
            <param name="allowFullScreen" value="true" />
			<embed src="<%= versionDir%>/EmbeddedDashboard.swf" quality="high" bgcolor="#ffffff"
				width="<%=width%>" height="<%=height%>" name="EmbeddedDashboard" align="middle"
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

</body>
</html>