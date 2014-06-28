<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="com.easyinsight.salesautomation.SalesAutomation" %>
<%@ page import="com.easyinsight.salesautomation.SalesAccount" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight Data Sources</title>
    <jsp:include page="bootstrapHeader.jsp"/>
    <script type="text/javascript">
        function assignSelected() {
            var rep = $("#salesRep").val();
            var results = [];
            $('input[id^="accountSelection"]').each(function () {
                var checked = $("#" + this.id).is(':checked');
                if (checked) {
                    var substring = this.id.substring(16, this.id.length);
                    results.push(substring);
                }
            });
            var o = {sales_rep: rep, account_ids: results, approval_type:"assign"};
            $.ajax ( {
                url: '/app/approveSalesEmails',
                data: JSON.stringify(o),
                success: function(data) {
                    window.location.href = "/app/html/salesApproval.jsp";
                },
                type: "POST"
            });
        }

        function ignoreSelected() {

            var results = [];
            $('input[id^="accountSelection"]').each(function () {
                var checked = $("#" + this.id).is(':checked');
                if (checked) {
                    var substring = this.id.substring(16, this.id.length);
                    results.push(substring);
                }
            });
            var o = { account_ids: results, approval_type:"ignore"};
            $.ajax ( {
                url: '/app/approveSalesEmails',
                data: JSON.stringify(o),
                success: function(data) {
                    window.location.href = "/app/html/salesApproval.jsp";
                },
                type: "POST"
            });
        }
    </script>
</head>
<body>
<%
    String userName = (String) session.getAttribute("userName");
    com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    try {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        List<SalesAccount> accounts = new SalesAutomation().automate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
%>

<jsp:include page="../header.jsp">
    <jsp:param name="userName" value="<%= userName %>"/>
    <jsp:param name="headerActive" value="<%= HtmlConstants.DATA_SOURCES_AND_REPORTS %>"/>
</jsp:include>
<div class="container">
    <div class="row">
        <div class="col-md-12">

            <%--<form class="well" method="post" action="/app/approveSalesEmails" id="loginForm">--%>
                <div class="row" style="margin-bottom:30px">
                    <div class="col-md-4">
                        <select id="salesRep">
                            <option>James Boe</option>
                            <option>Cendie Lee</option>
                            <option>Alan Baldwin</option>
                        </select>
                    </div>
                    <div class="col-md-4">
                        <button class="btn btn-inverse" onclick="assignSelected()">Assign to Selected Sales Rep</button>
                    </div>
                    <div class="col-md-4">
                        <button class="btn btn-inverse" onclick="ignoreSelected()">Ignore Selected</button>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <table class="table table-striped table-bordered">
                            <thead>
                                <tr>
                                    <th></th>
                                    <th>Email</th>
                                    <th>First Name</th>
                                    <th>Last Name</th>
                                    <th>Creation Date</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    for (SalesAccount account : accounts) {
                                        %>
                                <tr>
                                    <td><input type="checkbox" id="accountSelection<%=account.getAccountID()%>" name="accountSelection<%=account.getAccountID()%>"></td>
                                    <td><%=account.getEmail()%></td>
                                    <td><%=account.getFirstName()%></td>
                                    <td><%=account.getLastName()%></td>
                                    <td><%=sdf.format(account.getCreationDate())%></td>
                                </tr>
                                        <%
                                    }
                                %>
                            </tbody>
                        </table>
                    </div>
                </div>
            <%--</form>--%>
        </div>
    </div>
</div>
</body>
<%
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>
</html>