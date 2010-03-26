<%@ page import="com.easyinsight.scorecard.ScorecardService" %>
<%@ page import="com.easyinsight.scorecard.ScorecardDescriptor" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.UUID" %>
<%@ page import="com.easyinsight.scorecard.ScorecardList" %>
<%
    Long userID = (Long) request.getSession().getAttribute("userID");
    ScorecardService service = new com.easyinsight.scorecard.ScorecardService();
    ScorecardList scorecards =  service.getScorecardDescriptors(userID);
%>
<% // TODO: If there's one scorecard, don't do this
%>
<div id="added" style="display:none">
<ul>
    <% for(ScorecardDescriptor scorecard : scorecards.getScorecardDescriptors()) { %>
        <li><a href="loadScorecard.jsp" onclick="loadScorecard('<%= scorecard.getId() %>');return false;"><%= scorecard.getName() %></a></li>
    <% } %>
</ul>
</div>
<script id="<%= UUID.randomUUID().toString() %>" type="text/javascript">$(document).ready(function() {$("#scorecardList").html($("#added").html());$("#scorecardList").dialog("open")})</script>