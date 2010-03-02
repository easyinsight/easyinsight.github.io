<%@ page import="java.text.NumberFormat" %>
<%
    com.easyinsight.scorecard.Scorecard scorecard = null;
    NumberFormat nf = NumberFormat.getPercentInstance();
    nf.setMaximumFractionDigits(2);
    Long userID = (Long) request.getSession().getAttribute("userID");
    long scorecardID;
    String scorecardIDString = (String) request.getParameter("scorecardID");
    if (scorecardIDString == null) {
        // TODO: not sure what to do here
        throw new UnsupportedOperationException();
    } else {
        scorecardID = Long.parseLong(scorecardIDString);
    }

    com.easyinsight.scorecard.ScorecardWrapper scorecardWrapper = new com.easyinsight.scorecard.ScorecardService().getScorecard(scorecardID, userID,
            new java.util.ArrayList<com.easyinsight.datafeeds.CredentialFulfillment>(), false);
    if (scorecardWrapper.getCredentials() != null && scorecardWrapper.getCredentials().size() > 0) {
        // TODO: in this case, you need to manually prompt for credentials for the required data sources
    } else if (scorecardWrapper.isAsyncRefresh()) {
        // TODO: set up ajax async refresh here
    }

    scorecard = scorecardWrapper.getScorecard();

%>
            <%
                if(scorecard != null) {
                    boolean alternate = false;
                    for (com.easyinsight.kpi.KPI kpi : scorecard.getKpis()) { %>
                    <tr <% if(alternate) {%>class="alternate"<% } %>><td>
                    <%  if(kpi.getIconImage() != null) { %>
                        <img src="/app/assets/icons/32x32/<%= kpi.getIconImage() %>" alt="KPI Icon" />
                    <% } %>
                    </td>
                    <td><%= kpi.getName() %></td>
                    <td><%= kpi.getAnalysisMeasure().getFormattingConfiguration().createFormatter().format(kpi.getKpiOutcome().getOutcomeValue()) %></td>
                    <td><%= kpi.getDayWindow() %> Days</td>
                    <td><%= nf.format(kpi.getKpiOutcome().getPercentChange()/ 100.0) %></td>
                    <td></td>
                    </tr>
                <%
                        alternate = !alternate;
                    }
                }%>