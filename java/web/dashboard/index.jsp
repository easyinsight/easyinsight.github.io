<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.scorecard.ScorecardService" %>
<%@ page import="com.easyinsight.scorecard.Scorecard" %>
<%@ page import="java.util.List" %>
<%@ page import="com.easyinsight.kpi.KPI" %>
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
    java.util.List<com.easyinsight.scorecard.Scorecard> scorecards = new com.easyinsight.scorecard.ScorecardService().getScorecardsForUser();
    com.easyinsight.scorecard.Scorecard scorecard = (com.easyinsight.scorecard.Scorecard) scorecards.get(0);
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
    }
%>
</table>