<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.scorecard.ScorecardService" %>
<%@ page import="com.easyinsight.scorecard.Scorecard" %>
<%@ page import="java.util.List" %>
<%@ page import="com.easyinsight.kpi.KPI" %>
<%@ page import="com.easyinsight.scorecard.ScorecardStorage" %>
<%@ page import="com.easyinsight.datafeeds.CredentialFulfillment" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.easyinsight.scorecard.ScorecardWrapper" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    boolean loggedIn = true;
    com.easyinsight.scorecard.Scorecard scorecard = null;
    NumberFormat nf = NumberFormat.getPercentInstance();
    nf.setMaximumFractionDigits(2);
    Long userID = (Long) request.getSession().getAttribute("userID");
    long scorecardID;
    if (userID == null || userID == 0) {
        loggedIn = false;
    }
%>
<html>
    <head>
        <title>Easy Insight Scorecard - <%= scorecard != null ? scorecard.getName() : "Unknown" %></title>
        <meta name = "viewport" content = "width = device-width" />
        <link type="text/css" href="/css/jquery.contextMenu.css" rel="stylesheet" media="screen" />
        <link type="text/css" href="/css/jquery-ui.css" rel="stylesheet" media="screen" />
        <link type="text/css" href="/css/scorecard.css" rel="stylesheet" media="screen" />
        <script type="text/javascript" src="/js/jquery.min.js"></script>
        <script type="text/javascript" src="/js/jquery-ui.min.js"></script>
        <script type="text/javascript" src="/js/jquery.contextMenu.js"></script>
        <%
            String googleLibs = request.getParameter("libs");
            if(googleLibs != null && !googleLibs.isEmpty() && !"null".equals(googleLibs)) {
                for(String lib : googleLibs.split(",")) {
                    %>
            <script type="text/javascript" src="http://www.google.com/ig/f/<%= lib %>"></script>
        <%
                }
            }
        %>
        <script type="text/javascript">
            $(document).ready(function() {
                $("#progressbar").hide();
                $("#progressbar").progressbar({
			        value: 100
		        });
                $("#progressbar").ajaxStart(function() {
                    $(this).show();
                });
                $("#progressbar").ajaxStop(function() {
                    $(this).hide();
                });
                $("#loginDialog").dialog({bigframe: true,
                    autoOpen: <%= loggedIn ? "false" : "true" %>,
                    modal: true, resizable: false, draggable: false, closeOnEscape: false,
                    minheight: 250,
                    maxheight: 250,
                    buttons: {"Log In": login }
                })
                $("#loginDialog").keyup(function(e) {
                    if (e.keyCode == 13) {
                        login();
                    }
                });
                $("#scorecardList").dialog({
                    bigframe: true,
                    autoOpen: false,
                    modal: true, resizable: false, draggable: false, closeOnEscape: false,
                    minheight: 250,
                    maxheight: 250,
                    buttons: {}
                })
                $("#credentialsDialog").dialog({
                    bigframe: true,
                    autoOpen: false,
                    modal: true, resizable: false, draggable: false, closeOnEscape: false,
                    minheight: 250,
                    maxheight: 250,
                    buttons: {
                        'Next': nextDialog,
                        'Previous': backDialog,
                        'Cancel': function() { $("#credentialsDialog").dialog('close'); }
                    }
                })

                if(typeof(_IG_Prefs) == typeof(Function)) {
                    prefs = new _IG_Prefs();
                    var scorecardID = prefs.getString("scorecardID");
                    if(scorecardID) {
                        $("#scorecardID").val(scorecardID);
                        $("#credsScorecardID").val(scorecardID);
                    <% if (loggedIn) {%>
                        loadScorecard(scorecardID);
                   <% } %>
                    }
                } else {
                    alert("_IG_Prefs could not be found.");
                }
            });

            function login() {
                var params = $("form#loginForm").serialize();
                $("#notice").html("");
                $.ajax({data: params,
                    url: 'login.jsp',
                    type: 'post',
                    success: loadResults 
                });
                return false;
            }

            function signout() {
                $.ajax({
                    url: 'logout.jsp',
                    type: 'post',
                    success: loadResults
                });
            }
            
            function loadScorecardList() {
                $.ajax({
                    url: 'scorecardList.jsp',
                    type: 'post',
                    success: loadResults
                })
            }
            function loadScorecard(scorecardID) {
                $("#scorecardList").dialog("close");
                if(typeof(prefs) != "undefined" && typeof(prefs.set) == typeof(Function)) {
                    prefs.set("scorecardID", scorecardID)
                }
                $("#scorecardID").val(scorecardID);
                $("#credsScorecardID").val(scorecardID);
                var params = $("form#loginForm").serialize();
                $.ajax({
                    data: params,
                    url: 'loadScorecard.jsp',
                    type: 'post',
                    success: loadResults
                })
            }

            function refresh() {
                var params = $("form#credentialsForm").serialize() + "&refresh=true";
                $.ajax({
                    data: params,
                    url: 'loadScorecard.jsp',
                    type: 'post',
                    success: loadResults
                });
            }

            function loadResults(request) {
                $("#results").html(request)
            }

            function resetCredentialsDialog() {
                credentialsIndex = 0;
                showCredentialsIndex();
            }

            function nextDialog() {
                credentialsIndex = credentialsIndex + 1;
                showCredentialsIndex();
            }

            function backDialog() {
                credentialsIndex = credentialsIndex - 1;
                showCredentialsIndex();
            }

            function showCredentialsIndex() {
                if(credentialsIndex == ($("#credentialsData > div").size() - 1)) {
                    $("#credentialsDialog").dialog("option", "buttons", {
                        'Cancel': function() { $("#credentialsDialog").dialog('close'); },
                        'Finish': refresh,
                        'Previous': backDialog
                    })
                } else if(credentialsIndex == 0) {
                    $("#credentialsDialog").dialog("option", "buttons", {
                        'Cancel': function() { $("#credentialsDialog").dialog('close'); },
                        'Next': nextDialog
                    })
                } else {
                    $("#credentialsDialog").dialog("option", "buttons", {
                        'Cancel': function() { $("#credentialsDialog").dialog('close'); },
                        'Next': nextDialog,
                        'Previous': backDialog
                    })
                }
                $("#credentialsData div:eq(" + credentialsIndex + ")").show();
                $("#credentialsData div:not(:eq(" + credentialsIndex + "))").hide();
            }

            function checkRefresh() {
                var params = $("form#loginForm").serialize();
                $.ajax({
                    data: params,
                    url: 'ajaxRefresh.jsp',
                    type: 'post',
                    success: loadResults
                })
            }

        </script>
    </head>
    <body>
        <div title="Select a Scorecard" id="scorecardList">
        </div>
        <div title="Credentials Required" id="credentialsDialog">
            <form id="credentialsForm" action="loadScorecard.jsp" onsubmit="return false;">
                <input type="hidden" name="scorecardID" id="credsScorecardID" value="<%= request.getParameter("scorecardID") %>" />
                <div id="credentialsData">
                    
                </div>
            </form>
        </div>
        <div title="Please Login" id="loginDialog">
            <form id="loginForm" action="login.jsp" onsubmit="return false;">
                <label for="login">Username or Email</label><br />
                <input name="login" id="login" type="text" /><br />
                <br />
                <label for="password">Password</label><br />
                <input name="password" id="password" type="password" />
                <input type="hidden" id="scorecardID" name="scorecardID" value="<%= request.getParameter("scorecardID") %>" />
                <div id="notice"></div>
            </form>
            <span id="signup">Don't have an account? sign up <a href="https://www.easy-insight.com/app" target="_blank">here!</a></span>
        </div>
        <div id="buttons">
            <span id="refresh"><button class="ui-state-default ui-corner-all" type="button" onclick="refresh();">Refresh</button></span><span id="showScorecardList"><button class="ui-state-default ui-corner-all" type="button" onclick="loadScorecardList();">Scorecards</button></span> <span id="signout"><button class="ui-state-default ui-corner-all" type="button" onclick="signout();">Sign Out</button></span>
        </div>
        <div id="scorecard">
            <% if(loggedIn && request.getParameter("scorecardID") != null && !request.getParameter("scorecardID").isEmpty()) { %>
                <jsp:include page="table.jsp">
                    <jsp:param name="ajax" value="false" />
                </jsp:include>
            <% } %>
        </div>
        <div id="progressbar"></div>
        <div id="results"></div>
    </body>
</html>