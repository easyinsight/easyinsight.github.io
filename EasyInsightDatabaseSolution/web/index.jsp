<%@ page import="com.easyinsight.connections.database.DataConnection" %>
<%@ page import="com.easyinsight.connections.database.data.EIUser" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="com.easyinsight.connections.database.data.Query" %>
<%--
  Created by IntelliJ IDEA.
  User: abaldwin
  Date: Apr 26, 2010
  Time: 2:35:27 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%
    if(session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
    }
%>
<html>
  <head>
    <title>Easy Insight Database Solution</title>
    <script type="text/javascript" src="js/jquery.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui.min.js"></script>
    <script type="text/javascript" src="js/dbsolution.js"></script>
    <link rel="stylesheet" href="css/jquery-ui.css" media="screen" />
    <style type="text/css">

        a img {
            border: none;
        }
        #header {
            width:100%;
            border-left-width: 1px;
            border-left-style: solid;
            border-left-color: #DDDDDD;
            border-right-width: 1px;
            border-right-style: solid;
            border-right-color: #DDDDDD;
        }

        #logo {
            padding: 7px 60px;
            display: inline;
        }
        body {
            width:100%;
            text-align:center;
            margin: 0px auto;
        }

        #mainTabs {
            border: none;
            padding:0px;
        }

        #mainTabs > div {
            margin-top: 10px;
            border-width: 1px;
            border-style: solid;
            border-color: #DDDDDD;
        }

        #mainTabs ul {
            background-image: url(images/Banner3.jpg);
            background-position: bottom;
        }

        body > div {
            font-family: Verdana, Helvetica, Arial, FreeSans, sans-serif;
            text-align:left;
            width:1000px;
            display:block;
            margin: 0px auto;
            position:relative;
        }

        span.success {
            color: #44FF44;
        }
        
        span.failure {
            color: #FF4444;
        }
        
        .controls {
            width:5.5em;
        }

        .scheduled {
            text-align:center;
        }
        
        .append {
            text-align:center;
        }

        table tr td {
            padding: 10px 5px;
        }

        #queryResults {
            width: 100%;
        }

        #queryResults div {
            width: 100%;
            padding: 0px;
            margin: 0px;
            overflow:auto;
        }

        #queryResults table tbody tr td {
            background-color: #FFFFFF;
            border-width:1px;
            border-style:solid;
            border-left-style:none;
            border-top-style:none;
            border-color: #666666;
        }

        #queryResults table tbody tr td:first-child {
            border-left-style:solid;
        }

        #queryResults table thead tr th {
            border-width:1px;
            border-style:solid;
            border-left-style:none;
            border-color: #666666;
            background-color: #EEEEEE;
        }

        .title {
            width:100%;height:60px;background-color:#848080;text-align:center;
        }

        .title h2 {
            margin:0px;padding:14px;color:#FFFFFF;font-size:24px;
        }

        #queryResults table thead tr th:first-child {
            border-left-style:solid;
        }

        .queryParam {
            display: inline-block;
            width: 15em;
        }

        .queryInput {
            width: 15em;
        }

        .ui-widget-content {
            background-color: transparent;
            background-image:inherit;
        }

        .ui-widget-content {
            background-color: transparent;
            background-image: none;
        }

        .ui-widget-header {
            background-color: transparent;
            background-image: none;
            border:none;
        }
        .ui-state-active, .ui-widget-content ui-state-active {
            border:none;
        }

        .ui-state-highlight {
            background-color:transparent;
            background-image:none;
        }
        .ui-state-highlight a {
            background-color:transparent;
            background-image:none;
            color: #EA003A;
        }

        .ui-state-default a, .ui-state-default a:link, .ui-state-default a:visited {
            color: #000000;
            font-weight: normal;
            font-size: 16px;
        }

        .ui-state-hover {
            border:none;
            background-color:transparent;
            background-image:none;
        }

        .ui-state-hover a, .ui-state-hover a:hover {
            color: #EA003A;
            font-weight: normal;
        }

        .ui-widget {
            font-family: Verdana, Helvetica, Arial, FreeSans, sans-serif;
        }

        .ui-state-active, .ui-widget-content .ui-state-active {
            border:none;
        }

        #signupHeadline {
            height: 24px;
            position: absolute;
            right: 10px;
            text-align: right;
            top: 10px;
            width: 200px;
        }

        .signupButton {
            background-image: url(images/buttons.png);
            background-repeat: no-repeat;
            display: inline-block;
            height: 24px;
            width: 70px;
        }

        .ui-tabs .ui-tabs-nav {
            width:100%;
            border-left-width: 1px;
            border-left-style: solid;
            border-left-color: #DDDDDD;
            border-right-width: 1px;
            border-right-style: solid;
            border-right-color: #DDDDDD;
            border-radius: 0px;
            padding: 0px;
        }

        .ui-tabs .ui-tabs-nav li {
            float: right;
        }

        .ui-state-hover, .ui-widget-content .ui-state-hover, .ui-state-focus, .ui-widget-content .ui-state-focus {
            background-image: none;
            background-color: transparent;
            border:none;
        }

        .ui-state-hover, .ui-widget-content .ui-state-hover, .ui-state-focus, .ui-widget-content .ui-state-focus {
            background-image:none;
            background-color: transparent;
            border:none;
        }

        .ui-state-default, .ui-widget-content .ui-state-default {
            background-image:none;
            background-color: transparent;
            border:none;
        }

        .ui-state-active a, .ui-state-active a:link, .ui-state-active a:visited {
            color: #EA003A;
        }

        .ui-tabs .ui-tabs-panel {
            padding: 0px;
        }

        .ui-tabs .ui-tabs-panel .content {
            padding: 1em 1.4em;
        }

        .ui-tabs .ui-tabs-nav li a {
            padding-bottom: 0.7em;
        }
        
    </style>
  </head>
  <body>
  <div>
      <div id="header">
          <a href="https://www.easy-insight.com/"><img id="logo" src="images/logo.jpg" alt="Easy Insight Logo" /></a>
          <div id="signupHeadline">
              <a href="https://www.easy-insight.com/app/" class="signupButton"></a>
          </div>
      </div>
      <div id="mainTabs">
          <ul>
              <li><a href="#securityTab">SECURITY</a></li>
              <li><a href="#queriesTab">QUERIES</a></li>
              <li><a href="#connectionsTab">DATA SOURCES</a></li>
              <li><a href="#userTab">USER INFO</a></li>
          </ul>
          <div id="securityTab">
              <div class="title">
                	<h2>
                    Change Your Password
                    </h2>
			  </div>
              <div class="content">
                  <form id="updateCurrentUser" action="user/update.jsp" onsubmit="return false;">
                      Current Password: <input type="password" name="currentPassword" /><br />
                      New Password: <input type="password" name="newPassword" /><br />
                      Confirm new password: <input type="password" name="confirmPassword" /><br />
                      <button onclick="updateUser('#updateCurrentUser');">Submit</button>
                  </form>
                  <div id="updateUserResults"></div>
              </div>
          </div>          
          <div id="queriesTab">
            <div class="title">
                	<h2>
                        Queries
                    </h2>
			  </div>
            <div class="content">
                <button id="newQueryButton" onclick="showCreateQuery();">New Query...</button>
                <div id="queryList">
                    <jsp:include page="query/show.jsp" />
                </div>
                <form id="editQuery" action="query/update.jsp" onsubmit="return false;">
                    <input type="hidden" name="edit" value="edit" />
                    <input type="hidden" name="id" value="" />
                    <span class="queryParam">Query Name:</span> <input type="text" name="queryName" /><br />
                    <span class="queryParam">Connection: </span> <select name="queryConnection"></select><br />
                    <span class="queryParam">Data Source Name: </span> <input type="text" name="queryDataSource" /><br />
                    <span class="queryParam">Schedule? </span><input type="checkbox" name="schedule" /><br />
                    Replace or Append Existing Data?<br />
                    <input name="uploadType" type="radio" value="replace" checked="checked" /> Replace<br />
                    <input name="uploadType" type="radio" value="append" /> Append<br />
                    Query:<br />
                    <textarea name="queryValue" rows="6" cols="80"></textarea><br />
                    <button onclick="testQuery('#editQuery')">Test</button><button onclick="updateQuery('#editQuery')">Update</button><button onclick="resetQueryTab();">Cancel</button>
                </form>
                <form id="createQuery" action="query/create.jsp" onsubmit="return false;">
                    <span class="queryParam">Query Name:</span> <input class="queryInput" type="text" name="queryName" /><br />
                    <span class="queryParam">Connection:</span> <select class="queryInput" name="queryConnection"></select><br />
                    <span class="queryParam">Data Source Name:</span> <input class="queryInput" type="text" name="queryDataSource" /><br />
                    <span class="queryParam">Schedule?</span> <input type="checkbox" name="schedule" /><br />
                    Replace or Append Existing Data?<br />
                    <input name="uploadType" type="radio" value="replace" checked="checked" /> Replace<br />
                    <input name="uploadType" type="radio" value="append" /> Append<br />
                    Query: <br />
                    <textarea name="queryValue" rows="6" cols="80"></textarea><br />
                    <button onclick="testQuery('#createQuery')">Test</button><button onclick="createQuery('#createQuery')">Create</button><button onclick="resetQueryTab();">Cancel</button>
                </form>
                <div id="queryResults"></div>
            </div>
          </div>
          <div id="connectionsTab">
              <div class="title">
                	<h2>
                    Database Connections
                    </h2>
			  </div>
            <div class="content">
                <button id="newConnectionButton" onclick="showCreateConnection();">New Connection...</button><br />
                <div id="databaseList">
                    <jsp:include page="connection/show.jsp" />
                </div>
                <form id="editConnection" action="connection/update.jsp" method="post" onsubmit="return false;">
                    <input type="hidden" name="id" value="" />
                    <input type="hidden" name="dbType" value="" />
                    <input type="hidden" name="edit" value="edit" />
                    Connection Name: <input name="dbName" type="text" /><br />
                    <div id="editConnectionInfo">
                        <div id="mssqlEditConnection">
                            Host Name: <input name="mssqlHostName" type="text" /><br />
                            Port: <input name="mssqlPort" type="text" /><br />
                            Instance Name: <input name="mssqlInstanceName" type="text"><br />
                        </div>
                        <div id="oracleEditConnection">
                            Host Name: <input name="oracleHostName" type="text" /><br />
                            Port: <input name="oraclePort" type="text" /><br />
                            Schema Name: <input name="oracleSchema" type="text"><br />
                        </div>
                        <div id="mysqlEditConnection">
                            Host Name: <input name="mysqlHostName" type="text" /><br />
                            Port: <input name="mysqlPort" type="text" /><br />
                            Database Name: <input name="mysqlDbName" type="text"><br />
                        </div>
                        <div id="jdbcEditConnection">
                            Connection String: <input name="connectionString" type="text" /><br />
                        </div>
                    </div>
                    DB Username: <input name="dbUsername" type="text" /><br />
                    DB Password: <input name="dbPassword" type="password" /><br />
                    <button onclick="testDbConnection('editConnection')">Test Connection</button><button onclick="updateDbConnection('editConnection')">Update</button><button onclick="resetConnectionTab();">Cancel</button>
                </form>
                <form id="createConnection" action="connection/create.jsp" method="post" onsubmit="return false;">
                    Connection Name: <input name="dbName" type="text" /><br />
                    Database Type: <select id="dbType" name="dbType" onchange="showConnectionDetails();">
                        <option></option>
                        <option value="mysql">MySQL</option>
                        <option value="oracle">Oracle</option>
                        <option value="mssql">SQL Server</option>
                        <option value="jdbc">Raw JDBC Connection</option>
                    </select><br />
                    <div id="connectionDetails">
                        <div id="mssqlConnection">
                            Host Name: <input name="mssqlHostName" type="text" /><br />
                            Port: <input name="mssqlPort" type="text" /><br />
                            Instance Name: <input name="mssqlInstanceName" type="text"><br />
                        </div>
                        <div id="oracleConnection">
                            Host Name: <input name="oracleHostName" type="text" /><br />
                            Port: <input name="oraclePort" type="text" /><br />
                            Schema Name: <input name="oracleSchema" type="text"><br />
                        </div>
                        <div id="mysqlConnection">
                            Host Name: <input name="mysqlHostName" type="text" /><br />
                            Port: <input name="mysqlPort" type="text" /><br />
                            Database Name: <input name="mysqlDbName" type="text"><br />
                        </div>
                        <div id="jdbcConnection">
                            Connection String: <input name="connectionString" type="text" /><br />
                        </div>
                    </div>
                    DB Username: <input name="dbUsername" type="text" /><br />
                    DB Password: <input name="dbPassword" type="password" /><br />

                    <button onclick="testDbConnection('createConnection')">Test Connection</button><button onclick="createDbConnection('createConnection')">Create</button><button onclick="resetConnectionTab();">Cancel</button>
                </form>
                <div id="connectionResults"></div>
              </div>
          </div>
          <div id="userTab">
              <div class="title">
                <h2>
                  API Key Management
                </h2>
			  </div>
              <div class="content">
                  <%
                      EIUser user = EIUser.instance();
                      if(EIUser.instance() == null) {
                          user = new EIUser();
                  %>
                    No API User yet! Input your data!
                  <%
                      }
                  %>
                <form id="credentialsForm" action="validateCredentials.jsp" method="post" onsubmit="return false;">
                    Public Key: <input name="publicKey" type="text" value="<%= user.getPublicKey() == null ? "" : user.getPublicKey() %>" /><br />
                    Secret Key: <input name="secretKey" type="password" value="<%= user.getSecretKey() == null ? "" : user.getSecretKey() %>" /><br />
                    <button onclick="validateCredentials('credentialsForm');">Validate</button>
                </form>
                <div id="credentialsResults"></div>
            </div>
          </div>
        </div>
      </div>
  </body>
</html>