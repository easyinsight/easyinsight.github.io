<%@ page import="com.easyinsight.datafeeds.CredentialFulfillment" %>
<%@ page import="com.easyinsight.datafeeds.CredentialRequirement" %>
<%@ page import="com.easyinsight.datafeeds.CredentialsDefinition" %>
<%@ page import="com.easyinsight.scorecard.ScorecardInternalService" %>
<%@ page import="com.easyinsight.scorecard.ScorecardWrapper" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.users.SalesforceCredentials" %>
<%@ page import="com.easyinsight.users.UserNamePWCredentials" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="java.util.List" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.users.User" %>
<%@ page import="com.easyinsight.core.InsightDescriptor" %>
<%@ page import="com.easyinsight.goals.GoalTreeDescriptor" %>
<%@ page import="com.easyinsight.reportpackage.ReportPackageDescriptor" %>
<%
    try {
    com.easyinsight.scorecard.Scorecard scorecard = null;
    boolean completeRefresh = false;
    NumberFormat nf = NumberFormat.getPercentInstance();
    nf.setMaximumFractionDigits(2);
    Long userID = (Long) request.getSession().getAttribute("userID");
    Session hibernateSession = Database.instance().createSession();
    try {
        User u = (User)hibernateSession.get(User.class, userID);
        SecurityUtil.populateThreadLocal(userID, u.getAccount().getAccountID(), u.getAccount().getAccountType(), false);
    } finally {
        hibernateSession.close();
    }
    String scorecardIDString = request.getParameter("scorecardID");
    ScorecardInternalService service = new com.easyinsight.scorecard.ScorecardInternalService();
    // table shouldn't get rendered without scorecard ID being set
    long scorecardID = Long.parseLong(scorecardIDString);
    boolean refresh = false;
    if("true".equals(request.getParameter("refresh"))) {
        refresh = true;
    }
    ScorecardWrapper scorecardWrapper = service.getScorecard(scorecardID, userID, new java.util.ArrayList<com.easyinsight.datafeeds.CredentialFulfillment>(), refresh);
    if (scorecardWrapper.getCredentials() != null && scorecardWrapper.getCredentials().size() > 0) {
        List<CredentialFulfillment> newCreds = new LinkedList<CredentialFulfillment>();
        HashSet<Long> dataSets = new HashSet<Long>();
        %>
        <div id="refreshDialogInfo" style="display:none">
        <% for(CredentialRequirement creds : scorecardWrapper.getCredentials()) {
            if(!dataSets.contains(creds.getDataSourceID())) {
                dataSets.add(creds.getDataSourceID());
                switch(creds.getCredentialsDefinition()) {
                        case CredentialsDefinition.STANDARD_USERNAME_PW:
                            if(request.getParameter("username_" + creds.getDataSourceID()) != null && !request.getParameter("username_" + creds.getDataSourceID()).isEmpty() &&
                                    request.getParameter("password_" + creds.getDataSourceID()) != null && !request.getParameter("password_" + creds.getDataSourceID()).isEmpty()) {
                                CredentialFulfillment cf = new CredentialFulfillment();
                                cf.setDataSourceID(creds.getDataSourceID());
                                UserNamePWCredentials credInfo = new UserNamePWCredentials();
                                credInfo.setUserName(request.getParameter("username_" + creds.getDataSourceID()));
                                credInfo.setPassword(request.getParameter("password_" + creds.getDataSourceID()));
                                cf.setCredentials(credInfo);
                                newCreds.add(cf);
                            } else {
                            %>
                            <div>
                                Please enter the credentials for <%= creds.getDataSourceName() %>:<br />
                                <label for="username_<%= creds.getDataSourceID() %>">Username:</label><input type="text" id="username_<%=creds.getDataSourceID()%>" name="username_<%=creds.getDataSourceID()%>" /><br />
                                <label for="password_<%= creds.getDataSourceID() %>">Password:</label><input type="password" id="password_<%=creds.getDataSourceID()%>" name="password_<%=creds.getDataSourceID()%>" />
                            </div>
                            <% }
                            break;
                        case CredentialsDefinition.SALESFORCE:
                            if(request.getParameter("publickey_" + creds.getDataSourceID()) != null && !request.getParameter("publickey_" + creds.getDataSourceID()).isEmpty() &&
                                    request.getParameter("privatekey_" + creds.getDataSourceID()) != null && !request.getParameter("privatekey_" + creds.getDataSourceID()).isEmpty()) {

                                CredentialFulfillment cf = new CredentialFulfillment();
                                cf.setDataSourceID(creds.getDataSourceID());
                                SalesforceCredentials credInfo = new SalesforceCredentials();
                                credInfo.setUserName(request.getParameter("publickey_" + creds.getDataSourceID()));
                                credInfo.setPassword(request.getParameter("privatekey_" + creds.getDataSourceID()));
                                cf.setCredentials(credInfo);
                                newCreds.add(cf);
                            }
                            else {
                            %>
                            <div>
                                Please enter the public/private keys for <%= creds.getDataSourceName() %>:<br />
                                <label for="publickey_<%= creds.getDataSourceID() %>">Public Key:</label><input type="text" id="publickey_<%=creds.getDataSourceID()%>" name="publickey_<%=creds.getDataSourceID()%>" /><br />
                                <label for="privatekey_<%= creds.getDataSourceID() %>">Private Key:</label><input type="text" id="privatekey_<%=creds.getDataSourceID()%>" name="privatekey_<%=creds.getDataSourceID()%>" />
                            </div>
                            <% }
                            break;
                        case CredentialsDefinition.NO_CREDENTIALS:
                        default:
                        break;
                    }
                }

            }

        %>
        </div>
        <script type="text/javascript">$("#credentialsData").html($("#refreshDialogInfo").html());
        resetCredentialsDialog();
        </script>
    <%
            if(scorecardWrapper.getCredentials().size() == newCreds.size()) { 
                scorecardWrapper = service.getScorecard(scorecardID, userID, newCreds, refresh);
                if(scorecardWrapper.getCredentials().size() > 0){ %>
                    <script type="text/javascript">$("#credentialsDialog").dialog('open');</script>
                <%
                } else {
                    completeRefresh = true;
                %>
                    <script type="text/javascript">$("#credentialsDialog").dialog("close");</script>
                <%
                }
            } else {
                    %>
                    <script type="text/javascript">$("#credentialsDialog").dialog("open");</script>
                    <%
                }
        } else {
            completeRefresh = true;
        }

        if(completeRefresh) {
            %>
                <script type="text/javascript">$("#credentialsDialog").dialog("close");</script>
            <%
        }

    scorecard = scorecardWrapper.getScorecard();

%>
<% if(request.getParameter("ajax") == null) { %>
    <div id="added" style="display:none">
<% } %>
            <table>
                <thead>
                <tr>
                    <th>&nbsp;</th>
                    <th>KPI Name</th>
                    <th>Value</th>
                    <th>% Change</th>
                    <th>&nbsp;</th>
                </tr>
                </thead>
                <tbody>
            <%
                if(scorecard != null) {
                    boolean alternate = false;
                    for (com.easyinsight.kpi.KPI kpi : scorecard.getKpis()) { %>
                    <tr id="kpi_<%= kpi.getKpiID() %>" <% if(alternate) {%>class="alternate"<% } %>>
                        <td>
                        <% if(scorecardWrapper.getAsyncRefreshKpis() != null && scorecardWrapper.getAsyncRefreshKpis().contains(kpi)) { %>
                            <img src="/images/scorecard/ajax-loader.gif" alt="Loading" />
                        <% } else if(kpi.getIconImage() != null) { %>
                        <img src="/app/assets/icons/16x16/<%= kpi.getIconImage() %>" alt="KPI Icon" />
                    <% } else {
                        %>&nbsp;<% } %>
                        </td>
                    <td class="kpiName"><%= kpi.getName() %></td>
                    <td class="value"><%= kpi.getAnalysisMeasure().getFormattingConfiguration().createFormatter().format(kpi.getKpiOutcome().getOutcomeValue()) %></td>
                    <td class="percent">
                        <% if (kpi.getKpiOutcome() == null || kpi.getKpiOutcome().getPercentChange() == null) { %>
                            &nbsp;            
                        <%
                            }
                            else {
                        %>
                            <%= nf.format(kpi.getKpiOutcome().getPercentChange()/ 100.0) %>
                        <% }%>
                    </td>
                    <td><img src="/images/scorecard/<%= kpi.createIconText() %>" /></td>
                    </tr>
                <%
                        alternate = !alternate;
                    }
                }%>
                </tbody>
            </table>
            <% for (com.easyinsight.kpi.KPI kpi : scorecard.getKpis()) { %>
                <ul id="kpi_<%= kpi.getKpiID() %>_menu" class="contextMenu">
                    <li><a href="#feedID=<%= kpi.getCoreFeedUrlKey() %>">Analyze this KPI...</a></li>
                    <% if(kpi.getConnectionID() > 0) { %>
                        <li class="separator"><a href="#page=exchange&view=1&display=0&subTopicID=<%= kpi.getConnectionID()%>">Find reports for this KPI...</a></li>
                    <% } %>
                    <li class="separator"><a href="#multiReportID=<%= kpi.getCoreFeedUrlKey() %>">View all reports for this KPI...</a></li>
                    <% for(InsightDescriptor report :kpi.getReports()) {%>
                        <li class="separator"><a href="#reportID=<%= report.getUrlKey() %>"><%= report.getName() %></a> </li>
                    <% } %>
                    <% for(GoalTreeDescriptor goalTree: kpi.getKpiTrees()) { %>
                        <li class="separator"><a href="#goalTreeID=<%= goalTree.getUrlKey() %>"><%= goalTree.getName() %></a> </li>
                    <% } %>
                    <% for(ReportPackageDescriptor reportPackage : kpi.getPackages()) { %>
                        <li class="separator"><a href="#packageID=<%=reportPackage.getUrlKey()%>"><%=reportPackage.getName()%></a></li>
                    <% } %>
                </ul>
            <% } %>
        <% if(request.getParameter("ajax") == null) { %>
        </div>
        <script type="text/javascript">
            $(document).ready(function() {
                $("#scorecard td").destroyContextMenu();
                $("#scorecard").html($("#added").html());
                <% for (com.easyinsight.kpi.KPI kpi : scorecard.getKpis()) { %>
                    $("#kpi_<%= kpi.getKpiID() %>").contextMenu({
                        menu: 'kpi_<%= kpi.getKpiID() %>_menu'
                    },
                    function(action, el, pos) {
                        var loc = window.location
                        loc.hash = action
                        loc.pathname = "/app/"
                        window.location = loc;
                    });
                <% } %>
            });
            <% if(scorecardWrapper.getAsyncRefreshKpis() != null && !scorecardWrapper.getAsyncRefreshKpis().isEmpty()) { %>
                asyncRefresh = setInterval(checkRefresh, 5000);    
            <% } %>
        </script>
        <% }
        else { %>
            <script type="text/javascript">
                $(document).ready(function() {
            <%for (com.easyinsight.kpi.KPI kpi : scorecard.getKpis()) { %>
                    $("#kpi_<%= kpi.getKpiID() %>").contextMenu({
                        menu: 'kpi_<%= kpi.getKpiID() %>_menu'
                    },
                    function(action, el, pos) {
                       var loc = window.location
                        loc.hash = action
                        loc.pathname = "/app/"
                        window.location = loc;
                    });
                <% } %> });
            </script>
        <%
            }
        }
        finally {
            SecurityUtil.clearThreadLocal();
        }
        %>