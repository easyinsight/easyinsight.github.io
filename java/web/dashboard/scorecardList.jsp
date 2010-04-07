<%@ page import="com.easyinsight.scorecard.ScorecardInternalService" %>
<%@ page import="com.easyinsight.scorecard.ScorecardDescriptor" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.UUID" %>
<%@ page import="com.easyinsight.scorecard.ScorecardList" %>
<%
    Long userID = (Long) request.getSession().getAttribute("userID");
    ScorecardInternalService service = new com.easyinsight.scorecard.ScorecardInternalService();
    ScorecardList scorecards =  service.getScorecardDescriptors(userID, true);
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