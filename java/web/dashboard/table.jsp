<%@ page import="java.text.NumberFormat" %>
<%@ page import="com.easyinsight.users.User" %>
<%@ page import="com.easyinsight.scorecard.ScorecardService" %>
<%@ page import="com.easyinsight.datafeeds.CredentialRequirement" %>
<%@ page import="com.easyinsight.datafeeds.CredentialsDefinition" %>
<%@ page import="com.easyinsight.scorecard.ScorecardWrapper" %>
<%@ page import="com.easyinsight.datafeeds.CredentialFulfillment" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="com.easyinsight.users.UserNamePWCredentials" %>
<%@ page import="com.easyinsight.users.SalesforceCredentials" %>
<%@ page import="java.util.HashSet" %>
<%
    com.easyinsight.scorecard.Scorecard scorecard = null;
    NumberFormat nf = NumberFormat.getPercentInstance();
    nf.setMaximumFractionDigits(2);
    Long userID = (Long) request.getSession().getAttribute("userID"); 

    String scorecardIDString = request.getParameter("scorecardID");
    ScorecardService service = new com.easyinsight.scorecard.ScorecardService();
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
                %>
                    <script type="text/javascript">$("#credentialsDialog").dialog("close");</script>
                <%
                }
            } else {
                    %>
                    <script type="text/javascript">$("#credentialsDialog").dialog("open");</script>
                    <%
                }
        } else { %>
        <script type="text/javascript">$("#credentialsDialog").dialog("close");</script>
                <%
                if (scorecardWrapper.isAsyncRefresh()) {
                // TODO: set up ajax async refresh here
                    scorecardWrapper.setAsyncRefresh(true);
                }
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
                    <tr <% if(alternate) {%>class="alternate"<% } %>><td>
                    <%  if(kpi.getIconImage() != null) { %>
                        <img src="/app/assets/icons/16x16/<%= kpi.getIconImage() %>" alt="KPI Icon" />
                    <% } %>
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
        <% if(request.getParameter("ajax") == null) { %>
        </div>
        <script type="text/javascript">$(document).ready(function() {$("#scorecard").html($("#added").html());})</script>
        <% } %>