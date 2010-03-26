<%@ page import="com.easyinsight.scorecard.LongKPIRefreshListener" %><%
    Long userID = (Long) request.getSession().getAttribute("userID");
    if(!LongKPIRefreshListener.instance().getEventsForUser(userID).isEmpty()) {
%>
<jsp:include page="table.jsp">
    <jsp:param name="refresh" value="false" />
</jsp:include>
<script type="text/javascript">
    clearInterval(asyncRefresh);
</script>
<% } %>