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
    <script type="text/javascript">

        function createAjaxFunction(action, resultDiv) {
            return function(formID) {
                var params = $("form#" + formID).serialize();
                $("#" + resultDiv).html("");
                $.ajax({
                    data: params,
                    url: action,
                    type: 'post',
                    success: loadResults("#" + resultDiv)
                });
            }
        }

        function loadResults(resultDiv) {
            return function(request) {
                $(resultDiv).html(request);
            }
        }

        function fromJSONtoSelect(select, json, valueField, printField) {
            while(select.hasChildNodes()) {
                select.removeChild(select.firstChild);
            }
            for(var i = 0;i < json.length;i++) {
                var option = document.createElement('option');
                option.setAttribute('value', json[i][valueField]);
                var text = document.createTextNode(json[i][printField]);
                option.appendChild(text);
                select.appendChild(option);
            }
        }

        function createRestFunction(action, resultDiv) {
            return function(id) {
                $("#" + resultDiv).html("");
                $.ajax({
                    data: "id=" + id,
                    url: action,
                    type: 'post',
                    success: loadResults("#" + resultDiv)
                });
            }
        }

        var testQuery = createAjaxFunction('query/test.jsp', 'queryResults');
        var testIdQuery = createRestFunction('query/test.jsp', 'queryResults');
        var updateQuery = createAjaxFunction('query/update.jsp', 'queryResults');
        var createQuery = createAjaxFunction('query/create.jsp', 'queryResults');
        var deleteQuery = createRestFunction('query/delete.jsp', 'queryResults');
        var uploadQuery = createRestFunction('query/upload.jsp', 'queryResults');
        var deleteConnection = createRestFunction('connection/delete.jsp', 'connectionResults');
        var updateDbConnection = createAjaxFunction('connection/update.jsp', 'connectionResults');
        var testDbConnection = createAjaxFunction('connection/test.jsp', 'connectionResults');
        var createDbConnection = createAjaxFunction('connection/create.jsp', 'connectionResults');
        var validateCredentials = createAjaxFunction('validateCredentials.jsp', 'credentialsResults');
        var updateUser = createAjaxFunction('user/update.jsp', 'updateUserResults');

        function deleteQueryWithConfirm(id) {
            if(confirm('Are you sure you want to delete this query?') == true) {
                deleteQuery(id);
            }
        }

        function refreshDataSources() {
            $.ajax({
                url: 'connection/show.jsp',
                type: 'get',
                success: loadResults("#databaseList")
            });
            populateSelectStatement();
        }

        function refreshQueries() {
            $.ajax({
                url: 'query/show.jsp',
                type: 'get',
                success: loadResults("#queryList")
            });
        }

        function populateSelectStatement() {
            $.ajax({
                url: 'connection/list.jsp',
                type: 'get',
                success: function(result) {
                    var selects = $("select[name=queryConnection]");
                    for(var i = 0;i < selects.length;i++) {
                        fromJSONtoSelect(selects[i], result, "id", "name");
                    }
                }
            });
        }

        function editConnection(id) {
            $.ajax({
                url: 'connection/edit.jsp',
                data: 'id=' + id,
                type: 'get',
                success: function(result) {
                    $("form#editConnection input[name=id]")[0].value = result.id;
                    $("form#editConnection input[name=dbUsername]")[0].value = result.username;
                    $("form#editConnection input[name=dbPassword]")[0].value = result.password;
                    $("form#editConnection input[name=dbName]")[0].value = result.name;
                    $("#editConnectionInfo div").hide();
                    $("#" + result.type + "EditConnection").show();
                    $("form#editConnection input[name=dbType]")[0].value = result.type;
                    if(result.type == "mysql") {
                        $("form#editConnection input[name=mysqlHostName]")[0].value = result.hostname;
                        $("form#editConnection input[name=mysqlPort]")[0].value = result.port;
                        $("form#editConnection input[name=mysqlDbName]")[0].value = result.databaseName;
                    }
                    if(result.type == "mssql") {
                        $("form#editConnection input[name=mssqlHostName]")[0].value = result.hostname;
                        $("form#editConnection input[name=mssqlPort]")[0].value = result.port;
                        $("form#editConnection input[name=mssqlInstanceName]")[0].value = result.instanceName;
                    }
                    if(result.type == "oracle") {
                        $("form#editConnection input[name=oracleHostName]")[0].value = result.hostname;
                        $("form#editConnection input[name=oraclePort]")[0].value = result.port;
                        $("form#editConnection input[name=oracleSchema]")[0].value = result.schema;
                    }
                    if(result.type == "jdbc") {
                        $("form#editConnection input[name=connectionString]")[0].value = result.connectionString;
                    }
                    editConnectionTab();
                }
            });
        }

        function editQuery(id) {
            $.ajax({
                url: 'query/edit.jsp',
                data: 'id=' + id,
                type: 'get',
                success: function(result) {
                    $("form#editQuery input[name=id]")[0].value = result.id;
                    $("form#editQuery input[name=queryName]")[0].value = result.name;
                    $("form#editQuery select[name=queryConnection]")[0].value = result.connectionId;
                    $("form#editQuery input[name=queryValue]")[0].value = result.query;
                    $("form#editQuery input[name=queryDataSource]")[0].value = result.dataSource;
                    $("form#editQuery input[name=schedule]")[0].checked = result.schedule;
                    if(result.append)
                        $("form#editQuery input[name=uploadType]")[0].value = "append";
                    else
                        $("form#editQuery input[name=uploadType]")[0].value = "replace";
                    $("form#createQuery").hide();
                    $("form#createQuery")[0].reset();
                    $("form#editQuery").show();
                    $("#newQueryButton").hide();
                }
            });
        }

        function showConnectionDetails() {
            $("#connectionDetails div").hide();
            if($("#dbType")[0].value != "")
                $("#" + $("#dbType")[0].value + "Connection").show(); 
        }

        function resetConnectionTab() {
            $('#createConnection').hide();
            $('#editConnection').hide();
            $('#newConnectionButton').show();
        }

        function editConnectionTab() {
            $('#createConnection').hide();
            $('#newConnectionButton').hide();
            $('#editConnection').show();
        }

        function showCreateConnection() {
            $('#newConnectionButton').hide();
            $('#editConnection')[0].reset();
            $('#editConnection').hide();
            $('#createConnection')[0].reset();
            $('#createConnection').show();
        }

        $(document).ready(function() {
            $("#connectionDetails div").hide();
            $("#editConnectionInfo div").hide();
            $("#createConnection").hide();
            $("#editConnection").hide();
            $("#editQuery").hide();
            $("#createQuery").hide();
            $("#mainTabs").tabs();
            populateSelectStatement();
        });

    </script>
    <style type="text/css">
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
    </style>
    <link rel="stylesheet" href="css/jquery-ui.css" media="screen" />
  </head>
  <body>
  <div id="mainTabs">
      <ul>
          <li><a href="#userTab">User Info</a></li>
          <li><a href="#connectionsTab">Data Sources</a></li>
          <li><a href="#queriesTab">Queries</a></li>
          <li><a href="#securityTab">Security</a></li>
      </ul>
      <div id="userTab">
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
      <div id="connectionsTab">
        <div id="databaseList">
            <jsp:include page="connection/show.jsp" />
        </div>
        <hr />
        <button id="newConnectionButton" onclick="showCreateConnection();">New Connection...</button><br />
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
      <div id="queriesTab">
        <div id="queryList">
            <jsp:include page="query/show.jsp" />
        </div>
        <hr />
        <form id="editQuery" action="query/update.jsp" onsubmit="return false;">
            <input type="hidden" name="edit" value="edit" /> 
            <input type="hidden" name="id" value="" />
            Query Name: <input type="text" name="queryName" /><br />
            Connection: <select name="queryConnection"></select><br />
            Query: <input type="text" name="queryValue" /><br />
            Data Source Name: <input type="text" name="queryDataSource" /><br />
            Schedule? <input type="checkbox" name="schedule" /><br />
            Replace or Append Existing Data?<br />
            <input name="uploadType" type="radio" value="replace" checked="checked" /> Replace<br />
            <input name="uploadType" type="radio" value="append" /> Append<br />
            <button onclick="testQuery('#editQuery')">Test</button><button onclick="updateQuery('#editQuery')">Update</button><button onclick="$('#editQuery')[0].reset();$('#editQuery').hide();$('#newQueryButton').show();">Cancel</button>
        </form>
        <button id="newQueryButton" onclick="$('#newQueryButton').hide();$('#createQuery')[0].reset();$('#editQuery')[0].reset();$('#editQuery').hide();$('#createQuery').show();">New Query...</button>
        <form id="createQuery" action="query/create.jsp" onsubmit="return false;">
            Query Name: <input type="text" name="queryName" /><br />
            Connection: <select name="queryConnection"></select><br />
            Query: <input type="text" name="queryValue" /><br />
            Data Source Name: <input type="text" name="queryDataSource" /><br />
            Schedule? <input type="checkbox" name="schedule" /><br />
            Replace or Append Existing Data?<br />
            <input name="uploadType" type="radio" value="replace" checked="checked" /> Replace<br />
            <input name="up
            loadType" type="radio" value="append" /> Append<br />
            <button onclick="testQuery('#createQuery')">Test</button><button onclick="createQuery('#createQuery')">Create</button><button onclick="$('#createQuery')[0].reset();$('#createQuery').hide();$('#newQueryButton').show()">Cancel</button>
        </form>
        <div id="queryResults"></div>
      </div>
      <div id="securityTab">
          <h2>Change Passwords</h2>
          <form id="updateCurrentUser" action="user/update.jsp" onsubmit="return false;">
              Current Password: <input type="password" name="currentPassword" /><br />
              New Password: <input type="password" name="newPassword" /><br />
              Confirm new password: <input type="password" name="confirmPassword" /><br />
              <button onclick="updateUser('#updateCurrentUser');">Submit</button>
          </form>
          <div id="updateUserResults"></div>
      </div>
    </div>
  </body>
</html>