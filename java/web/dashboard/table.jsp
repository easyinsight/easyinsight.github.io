<%@ page import="com.easyinsight.scorecard.ScorecardInternalService" %>
<%@ page import="com.easyinsight.scorecard.ScorecardWrapper" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.users.User" %>
<%@ page import="com.easyinsight.core.InsightDescriptor" %>
<%@ page import="com.easyinsight.goals.GoalTreeDescriptor" %>
<%@ page import="com.easyinsight.analysis.InsightRequestMetadata" %>
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
        SecurityUtil.populateThreadLocal(u.getUserName(), userID, u.getAccount().getAccountID(), u.getAccount().getAccountType(), false, false);
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
    ScorecardWrapper scorecardWrapper = service.getScorecard(scorecardID, userID, refresh, new InsightRequestMetadata());
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
                    <li><a target="_blank" href="/app/#feedID=<%= kpi.getCoreFeedUrlKey() %>">Analyze this KPI...</a></li>
                    <% if(kpi.getConnectionID() > 0) { %>
                        <li class="separator"><a target="_blank" href="/app/#page=exchange&view=1&display=0&subTopicID=<%= kpi.getConnectionID()%>">Find reports for this KPI...</a></li>
                    <% } %>
                    <li class="separator"><a target="_blank" href="/app/#multiReportID=<%= kpi.getCoreFeedUrlKey() %>">View all reports for this KPI...</a></li>
                    <% for(InsightDescriptor report :kpi.getReports()) {%>
                        <li class="separator"><a target="_blank" href="/app/#reportID=<%= report.getUrlKey() %>"><%= report.getName() %></a> </li>
                    <% } %>
                    <% for(GoalTreeDescriptor goalTree: kpi.getKpiTrees()) { %>
                        <li class="separator"><a target="_blank" href="/app/#goalTreeID=<%= goalTree.getUrlKey() %>"><%= goalTree.getName() %></a> </li>
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
                    null);
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
                    null);
                <% } %> });
            </script>
        <%
            }
        }
        finally {
            SecurityUtil.clearThreadLocal();
        }
        %>