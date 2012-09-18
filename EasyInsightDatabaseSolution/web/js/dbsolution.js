
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

function prepDialogs() {
    $(".errorDialog").each(function(i, curElement) {
        $(".statusImage", $(curElement).parent().parent()).click(function() {
           $(curElement).dialog("open");
        });
        $(curElement).dialog({autoOpen: false, resizable: false, modal: true, width: $(curElement).width(), title: "Error!" })
    });
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
var updateFieldList = createAjaxFunction('query/update_fields.jsp', 'queryResults');
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
//    populateSelectStatement();
}

function refreshQueries() {
    $(".errorDialog").dialog("destroy");
    $(".errorDialog").remove();
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

            $("form#editQuery textarea[name=queryValue]")[0].value = result.query;
            $("form#editQuery input[name=queryDataSource]")[0].value = result.dataSource;
            $("form#editQuery input[name=schedule]")[0].checked = result.schedule;
            if(result.append)
                $("form#editQuery input[name=uploadType]")[0].value = "append";
            else
                $("form#editQuery input[name=uploadType]")[0].value = "replace";

            editQueryTab();
        }
    });
}

function editQueryTab() {
    $("form#createQuery").hide();
    $("form#createQuery")[0].reset();
    $("form#editQuery").show();
    $("#newQueryButton").hide();
    $('#queryList').hide();
    $("#queryResults").html("");
    $("#queryInfoText").hide();
    $("#updateFields").hide();
}

function showFieldList() {
    $.ajax({
        url: 'query/list_fields.jsp',
        data: 'id=' + $("#editQuery input[name=id]").val(),
        type: 'get',
        success: function(result) {
            $("form#updateFields #fieldContent").html(result);
            displayFieldList();
        }
    })
}

function displayFieldList() {
    $('#newQueryButton').hide();
    $('#createQuery')[0].reset();
    $('#editQuery').hide();
    $('#createQuery').hide();
    $('#queryList').hide();
    $("#queryResults").html("");
    $("#queryInfoText").hide();
    $("#updateFields").show();
}

function resetQueryTab() {
    $('form#editQuery')[0].reset();
    $('form#editQuery').hide();
    $("form#createQuery")[0].reset();
    $("form#createQuery").hide();
    $('#newQueryButton').show();
    $('#queryList').show();
    $("#queryResults").html("");
    $("#queryInfoText").show();
    $("#updateFields").hide();
}

function showCreateQuery() {
    $('#newQueryButton').hide();
    $('#createQuery')[0].reset();
    $('#editQuery')[0].reset();
    $('#editQuery').hide();
    $('#createQuery').show();
    $('#queryList').hide();
    $("#queryResults").html("");
    $("#queryInfoText").hide();
    $("#updateFields").hide();
}

function showUpdateFields() {
    $('#newQueryButton').hide();
    $('#editQuery').hide();
    $('#createQuery').show();
    $('#queryList').hide();
    $("#queryResults").html("");
    $("#queryInfoText").hide();
    $("#updateFields").hide();
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
    $('#databaseList').show();
    $.ajax({
                url: "connection/list.jsp",
                success: function(data) {
                    if(data.length == 0)
                        showCreateConnection();
                    else {
                        editConnection(data[0]["id"])
                    }
                }
        });
}

function editConnectionTab() {
    $('#createConnection').hide();
    $('#newConnectionButton').hide();
    $('#databaseList').hide();
    $('#editConnection').show();
    $("#updateFields").hide();
}

function showCreateConnection() {
    $('#newConnectionButton').hide();
    $('#editConnection')[0].reset();
    $('#editConnection').hide();
    $('#createConnection')[0].reset();
    $('#createConnection').show();
    $('#databaseList').hide();
}

$(document).ready(function() {
    $("#loadingDiv").ajaxStart(function() {
        $(this).show();
    });
    $("#loadingDiv").ajaxStop(function() {
        $(this).hide();
    })
    $("#connectionDetails div").hide();
    $("#editConnectionInfo div").hide();
    $("#createConnection").hide();
    $("#editConnection").hide();
    $("#editQuery").hide();
    $("#createQuery").hide();
    $("#updateFields").hide();
    $("#updateFields").hide();
    $("#mainTabs").tabs({selected: 3});

    $.ajax({
            url: "connection/list.jsp",
            success: function(data) {
                if(data.length == 0)
                    showCreateConnection();
                else {
                    editConnection(data[0]["id"])
                }
            }
    });
});
