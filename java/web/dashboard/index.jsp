<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.scorecard.ScorecardService" %>
<%@ page import="com.easyinsight.scorecard.Scorecard" %>
<%@ page import="java.util.List" %>
<%@ page import="com.easyinsight.kpi.KPI" %>
<%@ page import="com.easyinsight.scorecard.ScorecardStorage" %>
<%@ page import="com.easyinsight.datafeeds.CredentialFulfillment" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.easyinsight.scorecard.ScorecardWrapper" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<table>
    <tr>
        <td></td>
        <td>KPI Name</td>
        <td>Latest Value</td>
        <td>Time</td>
        <td>% Change</td>
        <td></td>
    </tr>
<%
    
    String userIDString = (String) request.getSession().getAttribute("userID");
    long userID;
    long scorecardID;
    if (userIDString == null) {
        // TODO: force login
        throw new UnsupportedOperationException();
    } else {
        userID = Long.parseLong(userIDString);
    }
    String scorecardIDString = (String) request.getSession().getAttribute("scorecardID");
    if (scorecardIDString == null) {
        // TODO: not sure what to do here
        throw new UnsupportedOperationException();
    } else {
        scorecardID = Long.parseLong(scorecardIDString);    
    }

    com.easyinsight.scorecard.ScorecardWrapper scorecardWrapper = new com.easyinsight.scorecard.ScorecardService().getScorecard(scorecardID,
            new java.util.ArrayList<com.easyinsight.datafeeds.CredentialFulfillment>(), false);
    if (scorecardWrapper.getCredentials() != null && scorecardWrapper.getCredentials().size() > 0) {
        // TODO: in this case, you need to manually prompt for credentials for the required data sources
    } else if (scorecardWrapper.isAsyncRefresh()) {
        // TODO: set up ajax async refresh here
    }
    com.easyinsight.scorecard.Scorecard scorecard = scorecardWrapper.getScorecard();
    for (com.easyinsight.kpi.KPI kpi : scorecard.getKpis()) {
        StringBuilder rowBuilder = new StringBuilder();
        rowBuilder.append("<tr>");
        rowBuilder.append("<td>");
        if (kpi.getIconImage() != null) {
            rowBuilder.append("<img src='").append("/app/assets/icons/32x32/").append(kpi.getIconImage()).append("'/>");
        }
        rowBuilder.append("</td>");
        rowBuilder.append("<td>");
        rowBuilder.append(kpi.getName());
        rowBuilder.append("</td>");
        rowBuilder.append("<td>");
        rowBuilder.append(kpi.getKpiOutcome().getOutcomeValue());
        rowBuilder.append("</td>");
        rowBuilder.append("<td>");
        rowBuilder.append(kpi.getDayWindow());
        rowBuilder.append("</td>");
        rowBuilder.append("<td>");
        rowBuilder.append(kpi.getKpiOutcome().getPercentChange());
        rowBuilder.append("</td>");
        rowBuilder.append("<td></td>");
        rowBuilder.append("</tr>");
        out.println(rowBuilder.toString());
    }
%>
</table>