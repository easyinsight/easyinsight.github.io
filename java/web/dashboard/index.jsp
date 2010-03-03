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
        <script type=”text/javascript” src=”http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js”></script>
        <link type="text/css" href="http://jqueryui.com/latest/themes/base/jquery.ui.all.css" rel="stylesheet" />
        <script type="text/javascript" src="http://jqueryui.com/latest/jquery-1.4.2.js"></script>
        <script type="text/javascript" src="http://jquery-ui.googlecode.com/svn/tags/1.8rc1/jquery-1.4.1.js"></script>
        <script type="text/javascript" src="http://jquery-ui.googlecode.com/svn/tags/1.8rc1/ui/jquery-ui.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $("#loginDialog").dialog({bigframe: true,
                    autoOpen: <%= loggedIn ? "false" : "true" %>,
                    modal: true, resizable: false, draggable: false, closeOnEscape: false,
                    buttons: {"Log In": login }
                })
                $("#loginDialog").keyup(function(e) {
                    if (e.keyCode == 13) {
                        login();
                    }
                });
            });

            function login() {
                var params = $("form#loginForm").serialize();
                alert(params);
                $("#notice").html("");
                jQuery.ajax({data: params,
                    url: 'login.jsp',
                    type: 'post',
                    success: function(request) {$("#results").html(request)}});
                return false;
            }
        </script>
    </head>
    <body>

        <div title="Please Login" id="loginDialog">
            <form id="loginForm" onsubmit="return false;">
                <label for="login">Username or Email:</label><input name="login" id="login" type="text" />
                <label for="password">Password</label><input name="password" id="password" type="password" />
                <input type="hidden" id="scorecardID" name="scorecardID" value="<%= request.getParameter("scorecardID") %>" />
                <div id="notice"></div>
            </form>
        </div>
        <div id="scorecard">
            <table>
                <thead>
                <tr>
                    <th></th>
                    <th>KPI Name</th>
                    <th>Latest Value</th>
                    <th>Time</th>
                    <th>% Change</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                    <% if(loggedIn) { %>
                        <jsp:include page="table.jsp" />
                    <% } %>
                </tbody>
            </table>
        </div>
        <div id="results"></div>
    </body>
</html>