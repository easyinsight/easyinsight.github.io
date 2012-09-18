<!DOCTYPE html>
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
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
    }
%>
<html>
<head>
<title>Easy Insight Database Connection</title>
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jNotify.jquery.js"></script>
<script type="text/javascript" src="js/dbsolution.js"></script>
<link rel="stylesheet" href="css/jquery-ui.css" media="screen"/>
<link rel="stylesheet" href="css/jNotify.jquery.css" media="screen"/>
<link rel="stylesheet" href="css/bootstrap.min.css"/>
<script src="js/bootstrap.min.js" language="javascript" type="text/javascript"></script>
<style type="text/css">
#loadingDiv {
    width: 100%;
    height: 100%;
    text-align: center;
    position: fixed;
    top: 0;
    left: 0;
    display: none;
    z-index: 1;
    margin-top: 300px;
}

#loadingBlock {
    background-color: #FFFFFF;
    background-image: none;
    border-color: #000000;
    border-style: solid;
    border-width: 1px;
    border-radius: 3px;
    padding: 5px;
    opacity: 1;
    z-index: 1;
    position: relative;
}

#loadingDiv .background {
    width: 100%;
    height: 100%;
    position: fixed;
    top: 0;
    left: 0;
    opacity: .5;
    background-color: #CCCCCC;
    z-index: 0;
}

a img {
    border: none;
}

#header {
    width: 100%;
    padding: 20px;
}

    /*#logo {*/
    /*padding: 7px 60px;*/
    /*display: inline;*/
    /*}*/
    /*body {*/
    /*text-align:center;*/
    /*margin: 0px auto;*/
    /*}*/

    /*#mainTabs {*/
    /*border: none;*/
    /*padding:0px;*/
    /*}*/

    /*#mainTabs > div {*/
    /*margin-top: 10px;*/
    /*border-width: 1px;*/
    /*border-style: solid;*/
    /*border-color: #DDDDDD;*/
    /*}*/

    /*ul {*/
    /*background-image: url(images/Banner3.jpg) !important;*/
    /*background-position: bottom !important;*/
    /*}*/

    /*body > div {*/
    /*font-family: Verdana, Helvetica, Arial, FreeSans, sans-serif;*/
    /*text-align:left;*/
    /*width:1000px;*/
    /*display:block;*/
    /*margin: 0px auto;*/
    /*position:relative;*/
    /*}*/

.statusImage {
    cursor: pointer;
}

span.success {
    color: #44FF44;
}

span.failure {
    color: #FF4444;
}

.controls {
    width: 4em;
}

.scheduled {
    text-align: center;
}

.append {
    text-align: center;
}

button {
    margin-right: 10px;
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
    overflow: auto;
}

#queryResults table tbody tr td {
    background-color: #FFFFFF;
    border-width: 1px;
    border-style: solid;
    border-left-style: none;
    border-top-style: none;
    border-color: #666666;
}

#newQueryButton {
    margin-top: 10px;
    margin-bottom: 10px;
}

#queryResults table tbody tr td:first-child {
    border-left-style: solid;
}

#queryResults table thead tr th {
    border-width: 1px;
    border-style: solid;
    border-left-style: none;
    border-color: #666666;
    background-color: #EEEEEE;
}

.title {
    width: 100%;
    height: 60px;
    background-color: #848080;
    text-align: center;
}

.title h2 {
    margin: 0px;
    padding: 14px;
    color: #FFFFFF;
    font-size: 24px;
}

#queryResults table thead tr th:first-child {
    border-left-style: solid;
}

.queryParam {
    display: inline-block;
    width: 15em;
}

.queryInput {
    width: 15em;
}

#mainTabs.ui-widget-content, #mainTabs .ui-widget-content {
    /*background-color: transparent;*/
    background-image: none;
}

.ui-widget-overlay {
    background-image: none;
}

#mainTabs .ui-widget-header {
    background-color: transparent;
    background-image: none;
    border: none;
}

#mainTabs .ui-state-active, #mainTabs .ui-widget-content ui-state-active {
    border: none;
}

#mainTabs .ui-state-highlight {
    background-color: transparent;
    background-image: none;
}

#mainTabs .ui-state-highlight a {
    background-color: transparent;
    background-image: none;
    color: #EA003A;
}

#mainTabs .ui-state-default a, #mainTabs .ui-state-default a:link, #mainTabs .ui-state-default a:visited {
    color: #000000;
    font-weight: normal;
    font-size: 16px;
}

#mainTabs .ui-state-hover {
    border: none;
    background-color: transparent;
    background-image: none;
}

.white-well {
    background-color: #FFFFFF;
}

.dark-well {
    background-color: rgb(210, 210, 210);
}

.darker-well {
    background-color: #333333;
    color: #FFFFFF;
    padding-top: 9px;
    padding-bottom: 9px;
}

.darker-well h4 {
    margin-top: 3px;
    margin-bottom: 3px;
}

label {
    margin-bottom: 0px;
}

input[type=radio], input[type=checkbox] {
    margin-top: 0px;
    margin-bottom: 5px;
}

    /*#mainTabs .ui-state-hover a, .ui-state-hover a:hover {*/
    /*color: #EA003A;*/
    /*font-weight: normal;*/
    /*}*/

    /*#mainTabs.ui-widget {*/
    /*font-family: Verdana, Helvetica, Arial, FreeSans, sans-serif;*/
    /*}*/

    /*#mainTabs .ui-state-active, #mainTabs .ui-widget-content .ui-state-active {*/
    /*border:none;*/
    /*}*/

    /*#signupHeadline {*/
    /*height: 24px;*/
    /*position: absolute;*/
    /*right: 10px;*/
    /*text-align: right;*/
    /*top: 10px;*/
    /*width: 200px;*/
    /*}*/

    /*.signupButton {*/
    /*background-image: url(images/buttons.png);*/
    /*background-repeat: no-repeat;*/
    /*display: inline-block;*/
    /*height: 24px;*/
    /*width: 70px;*/
    /*}*/

    /*#mainTabs.ui-tabs .ui-tabs-nav {*/
    /*width:100%;*/
    /*border-left-width: 1px;*/
    /*border-left-style: solid;*/
    /*border-left-color: #DDDDDD;*/
    /*border-right-width: 1px;*/
    /*border-right-style: solid;*/
    /*border-right-color: #DDDDDD;*/
    /*border-radius: 0px;*/
    /*padding: 0px;*/
    /*}*/

    /*#mainTabs.ui-tabs .ui-tabs-nav li {*/
    /*float: right;*/
    /*}*/

#mainTabs .ui-state-hover, #mainTabs .ui-widget-content .ui-state-hover, #mainTabs .ui-state-focus, #mainTabs .ui-widget-content .ui-state-focus {
    background-image: none;
    background-color: transparent;
    border: none;
}

#mainTabs .ui-state-hover, #mainTabs .ui-widget-content .ui-state-hover, #mainTabs .ui-state-focus, #mainTabs .ui-widget-content .ui-state-focus {
    background-image: none;
    background-color: transparent;
    border: none;
}

#mainTabs .ui-state-default, #mainTabs .ui-widget-content .ui-state-default {
    background-image: none;
    background-color: transparent;
    border: none;
}

#mainTabs .ui-state-active a, #mainTabs .ui-state-active a:link, #mainTabs .ui-state-active a:visited {
    color: #EA003A;
}

    /*#mainTabs.ui-tabs .ui-tabs-panel {*/
    /*padding: 0px;*/
    /*}*/

    /*#mainTabs.ui-tabs .ui-tabs-panel .content {*/
    /*padding: 1em 1.4em;*/
    /*}*/

    /*#mainTabs.ui-tabs .ui-tabs-nav li a {*/
    /*padding-bottom: 0.7em;*/
    /*}*/

    /*#credentialsForm label, #editQuery label, #createQuery label, #editConnection label, #createConnection label,#updateCurrentUser label {*/
    /*display: inline-block;*/
    /*width: 250px;*/
    /*}*/

    /*#credentialsForm input, #editQuery input, #createQuery input, #editConnection input, #createConnection input, #updateCurrentUser input {*/
    /*width: 250px;*/
    /*}*/

.errorDialog {
    display: none;
    width: 800px;
}

    /*.ui-dialog .ui-widget-header {*/
    /*border-color: #B4B4B4;*/
    /*background: #BEBEBE url(css/images/ui-bg_gloss-wave_35_888888_500x100.png);*/
    /*}*/

    /*.ui-dialog .ui-state-hover, .ui-dialog .ui-widget-content .ui-state-hover, .ui-dialog .ui-state-focus, .ui-dialog .ui-widget-content .ui-state-focus {*/
    /*border: 1px solid #B4B4B4;*/
    /*background: #E8E8E8 url(css/images/ui-bg_gloss-wave_35_888888_500x100.png) 50% 50% repeat-x;*/
    /*font-weight: bold;*/
    /*color: #AAAAAA;*/
    /*}*/

    /*.ui-dialog .ui-state-hover .ui-icon, .ui-dialog .ui-state-focus .ui-icon {*/
    /*background-image: url(css/images/ui-icons_ffffff_256x240.png);*/
    /*}*/

    /*:focus {*/
    /*outline: 0;*/
    /*}*/

</style>
</head>
<body>
<div class="container">
    <div id="header">
        <a href="https://www.easy-insight.com/"><img id="logo" src="images/logo.jpg" alt="Easy Insight Logo"/></a>
    </div>
    <div class="well" id="mainTabs">
        <ul>
            <li><a href="#securityTab">SECURITY</a></li>
            <li><a href="#queriesTab">QUERIES</a></li>
            <li><a href="#connectionsTab">DATA SOURCE</a></li>
            <li><a href="#userTab">USER INFO</a></li>
        </ul>
        <div id="securityTab">
            <div class="content">
                <div class="well white-well">
                    <div class="well dark-well">
                        <p>This is where you change the Easy Insight Database Connection application password to prevent
                            unauthorized access.</p>
                    </div>
                    <form id="updateCurrentUser" action="user/update.jsp" onsubmit="return false;">
                        <label for="currentPassword">Current Password:</label> <input id="currentPassword"
                                                                                      type="password"
                                                                                      name="currentPassword"/><br/>
                        <label for="newPassword">New Password:</label> <input id="newPassword" type="password"
                                                                              name="newPassword"/><br/>
                        <label for="confirmPassword">Confirm new password:</label> <input id="confirmPassword"
                                                                                          type="password"
                                                                                          name="confirmPassword"/><br/>
                        <button class="btn btn-inverse" onclick="updateUser('#updateCurrentUser');">Submit</button>
                    </form>
                    <div id="updateUserResults"></div>
                </div>
            </div>
        </div>
        <div id="queriesTab">
            <div class="content">
                <div class="well white-well">
                    <div id="queryInfoText" class="well dark-well">
                        <p>Below is the list of queries you have selected to upload to Easy Insight. You can add a new
                            query by clicking the "New Query" button.
                            When you edit a query, you can choose to schedule it to execute nightly at midnight. You can
                            test the query by clicking the Test link next to the query to verify that the results match
                            your intent.</p>
                    </div>
                    <button id="newQueryButton" class="btn btn-inverse" onclick="showCreateQuery();">New Query...
                    </button>
                    <div id="queryList">
                        <jsp:include page="query/show.jsp"/>
                    </div>
                    <form id="editQuery" action="query/update.jsp" onsubmit="return false;">
                        <div class="well darker-well"><h4>Editing Query</h4></div>
                        <input type="hidden" name="edit" value="edit"/>
                        <input type="hidden" name="id" value=""/>

                        <div class="row">
                            <div class="span2"><label for="queryDataSource" class="queryParam">Data Source
                                Name: </label></div>
                            <div class="span3"><input id="queryDataSource" type="text" name="queryDataSource"/></div>
                        </div>
                        <div class="row">
                            <div class="span2"><label for="schedule" class="queryParam">Schedule? </label></div>
                            <div class="span3"><input id="schedule" type="checkbox" name="schedule"/></div>
                        </div>
                        <div class="row">
                            <div class="span6">Replace or Append Existing Data?</div>
                        </div>
                        <div class="row">
                            <div class="span1"><input id="uploadTypeReplace" name="uploadType" type="radio"
                                                      value="replace" checked="checked"/></div>
                            <div class="span4"><label for="uploadTypeReplace">Replace</label></div>
                        </div>
                        <div class="row">
                            <div class="span1"><input id="uploadTypeAppend" name="uploadType" type="radio"
                                                      value="append"/></div>
                            <div class="span4"><label for="uploadTypeAppend">Append</label></div>
                        </div>
                        <div class="row">
                            <div class="span6"><label for="queryValue">Query:</label></div>
                        </div>
                        <div class="row">
                            <div class="span8"><textarea id="queryValue" class="span8" name="queryValue"
                                                         rows="6"></textarea></div>
                        </div>
                        <div class="row">
                            <div class="span6">
                                <button class="btn btn-inverse" onclick="showFieldList()">Update Field Info</button>
                                <button class="btn btn-inverse" onclick="testQuery('#editQuery')">Test</button>
                                <button class="btn btn-inverse" onclick="updateQuery('#editQuery')">Update</button>
                                <button class="btn btn-inverse" onclick="resetQueryTab();">Cancel</button>
                            </div>
                        </div>
                    </form>
                    <form id="createQuery" action="query/create.jsp" onsubmit="return false;">
                        <div class="well darker-well"><h4>Creating New Query</h4></div>
                        <div class="row">
                            <div class="span2"><label for="createQueryDataSource" class="queryParam">Data Source
                                Name:</label></div>
                            <div class="span6"><input id="createQueryDataSource" class="queryInput" type="text"
                                                      name="queryDataSource"/></div>
                        </div>
                        <div class="row">
                            <div class="span2"><label for="createSchedule" class="queryParam">Schedule?</label></div>
                            <div class="span3"><input id="createSchedule" type="checkbox" name="schedule"/></div>
                        </div>
                        <div class="row">
                            <div class="span6">Replace or Append Existing Data?</div>
                        </div>
                        <div class="row">
                            <div class="span1"><input id="createUploadTypeReplace" name="uploadType" type="radio"
                                                      value="replace" checked="checked"/></div>
                            <div class="span4"><label for="createUploadTypeReplace">Replace</label></div>
                        </div>
                        <div class="row">
                            <div class="span1"><input id="createUploadTypeAppend" name="uploadType" type="radio"
                                                      value="append"/></div>
                            <div class="span4"><label for="createUploadTypeAppend">Append</label><br/></div>
                        </div>
                        <div class="row">
                            <div class="span6"><label for="createQueryValue">Query:</label></div>
                        </div>
                        <div class="row">
                            <div class="span8"><textarea class="span8" id="createQueryValue" name="queryValue"
                                                         rows="6"></textarea></div>
                        </div>
                        <div class="row">
                            <div class="span6">
                                <button class="btn btn-inverse" onclick="testQuery('#createQuery')">Test</button>
                                <button class="btn btn-inverse" onclick="createQuery('#createQuery')">Create</button>
                                <button class="btn btn-inverse" onclick="resetQueryTab();">Cancel</button>
                            </div>
                        </div>
                    </form>
                    <div id="queryResults"></div>
                    <form id="updateFields" action="query/update_fields.jsp" method="post" onsubmit="return false;">
                        <div id="fieldContent">

                        </div>
                        <button class="btn btn-inverse" onclick="updateFieldList()">Save Fields</button>
                    </form>
                </div>
            </div>
        </div>
        <div id="connectionsTab">
            <div class="content">
                <div class="well white-well">
                    <div class="well dark-well">
                        <p>Below is the database connection you have created. You can change the connection info used to
                            connect to a given database by clicking the Edit link next to the connection.
                            After creating a database connection, go to the Queries tab and create a new Query to upload
                            the data from that query to Easy Insight.
                        </p>
                    </div>
                    <form id="editConnection" action="connection/update.jsp" method="post" onsubmit="return false;">
                        <input type="hidden" name="id" value=""/>
                        <input type="hidden" name="dbType" value=""/>
                        <input type="hidden" name="edit" value="edit"/>

                        <div id="editConnectionInfo">
                            <div id="mssqlEditConnection">
                                <label for="mssqlHostName">Host Name:</label> <input id="mssqlHostName" class="span6"
                                                                                     name="mssqlHostName"
                                                                                     type="text"/><br/>
                                <label for="mssqlPort">Port:</label> <input id="mssqlPort" class="span6"
                                                                            name="mssqlPort" type="text"/><br/>
                                <label for="mssqlInstanceName">Instance Name:</label> <input id="mssqlInstanceName"
                                                                                             class="span6"
                                                                                             name="mssqlInstanceName"
                                                                                             type="text"><br/>
                            </div>
                            <div id="oracleEditConnection">
                                <label for="oracleHostName">Host Name:</label> <input id="oracleHostName" class="span6"
                                                                                      name="oracleHostName"
                                                                                      type="text"/><br/>
                                <label for="oraclePort">Port:</label> <input id="oraclePort" name="oraclePort"
                                                                             class="span6" type="text"/><br/>
                                <label for="oracleSchema">Schema Name:</label> <input id="oracleSchema" class="span6"
                                                                                      name="oracleSchema"
                                                                                      type="text"><br/>
                            </div>
                            <div id="mysqlEditConnection">
                                <label for="mysqlHostName">Host Name:</label> <input id="mysqlHostName" class="span6"
                                                                                     name="mysqlHostName"
                                                                                     type="text"/><br/>
                                <label for="mysqlPort">Port:</label> <input id="mysqlPort" name="mysqlPort"
                                                                            class="span6" type="text"/><br/>
                                <label for="mysqlDbName">Database Name:</label> <input id="mysqlDbName" class="span6"
                                                                                       name="mysqlDbName"
                                                                                       type="text"><br/>
                            </div>
                            <div id="jdbcEditConnection">
                                <label for="connectionString">Connection String:</label> <input id="connectionString"
                                                                                                name="connectionString"
                                                                                                class="span6"
                                                                                                type="text"/><br/>
                            </div>
                        </div>
                        <label for="dbUsername">DB Username:</label> <input id="dbUsername" name="dbUsername"
                                                                            class="span6" type="text"/><br/>
                        <label for="dbPassword">DB Password:</label> <input id="dbPassword" name="dbPassword"
                                                                            class="span6" type="password"/><br/>
                        <button class="btn btn-inverse" onclick="testDbConnection('editConnection')">Test Connection
                        </button>
                        <button class="btn btn-inverse" onclick="updateDbConnection('editConnection')">Update</button>
                        <button class="btn btn-inverse" onclick="resetConnectionTab();">Cancel</button>
                    </form>
                    <form id="createConnection" action="connection/create.jsp" method="post" onsubmit="return false;">
                        <label for="dbType">Database Type:</label> <select id="dbType" name="dbType"
                                                                           onchange="showConnectionDetails();">
                        <option></option>
                        <option value="mysql">MySQL</option>
                        <option value="oracle">Oracle</option>
                        <option value="mssql">SQL Server</option>
                        <option value="jdbc">Raw JDBC Connection</option>
                    </select><br/>

                        <div id="connectionDetails">
                            <div id="mssqlConnection">
                                <label for="createMssqlHostName">Host Name:</label> <input id="createMssqlHostName"
                                                                                           class="span6"
                                                                                           name="mssqlHostName"
                                                                                           type="text"/><br/>
                                <label for="createMssqlPort">Port:</label> <input id="createMssqlPort" class="span6"
                                                                                  name="mssqlPort" type="text"/><br/>
                                <label for="createMssqlInstanceName">Instance Name:</label> <input
                                    id="createMssqlInstanceName" class="span6" name="mssqlInstanceName"
                                    type="text"><br/>
                            </div>
                            <div id="oracleConnection">
                                <label for="createOracleHostName">Host Name:</label> <input id="createOracleHostName"
                                                                                            class="span6"
                                                                                            name="oracleHostName"
                                                                                            type="text"/><br/>
                                <label for="createOraclePort">Port:</label> <input id="createOraclePort" class="span6"
                                                                                   name="oraclePort" type="text"/><br/>
                                <label for="createOracleSchema">Schema Name:</label> <input id="createOracleSchema"
                                                                                            class="span6"
                                                                                            name="oracleSchema"
                                                                                            type="text"><br/>
                            </div>
                            <div id="mysqlConnection">
                                <label for="createMysqlHostName">Host Name:</label> <input id="createMysqlHostName"
                                                                                           class="span6"
                                                                                           name="mysqlHostName"
                                                                                           type="text"/><br/>
                                <label for="createMysqlPort">Port:</label> <input id="createMysqlPort" class="span6"
                                                                                  name="mysqlPort" type="text"/><br/>
                                <label for="createMysqlDbName">Database Name:</label> <input id="createMysqlDbName"
                                                                                             class="span6"
                                                                                             name="mysqlDbName"
                                                                                             type="text"><br/>
                            </div>
                            <div id="jdbcConnection">
                                <label for="createConnectionString">Connection String:</label> <input
                                    id="createConnectionString" class="span6" name="connectionString" type="text"/><br/>
                            </div>
                        </div>
                        <label for="createDbUsername">DB Username:</label> <input id="createDbUsername" class="span6"
                                                                                  name="dbUsername" type="text"/><br/>
                        <label for="createDbPassword">DB Password:</label> <input id="createDbPassword" class="span6"
                                                                                  name="dbPassword"
                                                                                  type="password"/><br/>

                        <button class="btn btn-inverse" onclick="testDbConnection('createConnection')">Test Connection
                        </button>
                        <button class="btn btn-inverse" onclick="createDbConnection('createConnection')">Create</button>
                        <button class="btn btn-inverse" onclick="resetConnectionTab();">Cancel</button>
                    </form>

                    <div id="connectionResults"></div>
                </div>
            </div>
        </div>
        <div id="userTab">
            <div class="content">
                <div class="well white-well">
                    <div class="well dark-well">
                        <%
                            EIUser user = EIUser.instance();
                            if (EIUser.instance() == null) {
                                user = new EIUser();
                        %>
                        No API User yet!
                        <%
                            }
                        %>
                        Please enter either your username/password or your API key/secret key. We recommend using a
                        key/secret key
                        combination, as this is easier to invalidate for security purposes at a later date, but to get
                        started quickly, you can still use your username/password.
                    </div>
                    <form id="credentialsForm" action="validateCredentials.jsp" method="post" onsubmit="return false;">
                        <p><label for="publicKey">Public Key/Username:</label> <input id="publicKey" name="publicKey"
                                                                                      class="span6" type="text"
                                                                                      value="<%= user.getPublicKey() == null ? "" : user.getPublicKey() %>"/><br/>
                            <label for="secretKey">Secret Key/Password:</label> <input id="secretKey" name="secretKey"
                                                                                       class="span6" type="password"
                                                                                       value="<%= user.getSecretKey() == null ? "" : user.getSecretKey() %>"/><br/>
                            <label for="currentUrl">Current URL:</label> <input id="currentUrl" name="currentUrl"
                                                                                class="span6" type="text"
                                                                                value="<%= user.getCurrentUrl() == null ? "" : user.getCurrentUrl() %>"/><br/>
                        </p>
                        <button class="btn btn-inverse" onclick="validateCredentials('credentialsForm');">Save</button>
                    </form>
                    <div id="credentialsResults"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="loadingDiv" class="ie9hack">
    <div class="background"></div>
    <span id="loadingBlock"><img alt="Loading..." src="images/ajax-loader.gif"/> <span id="loadingText">Please wait, loading...</span></span>
</div>

</body>
</html>