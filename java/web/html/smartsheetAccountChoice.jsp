<%@ page import="com.easyinsight.datafeeds.FeedResponse" %>
<%@ page import="com.easyinsight.datafeeds.FeedService" %>
<%@ page import="com.easyinsight.datafeeds.basecampnext.BasecampNextCompositeSource" %>
<%@ page import="com.easyinsight.datafeeds.FeedStorage" %>
<%@ page import="com.easyinsight.html.RedirectUtil" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.datafeeds.smartsheet.SmartsheetTableSource" %>
<%@ page import="java.net.URLDecoder" %><%
    SecurityUtil.populateThreadLocalFromSession(request);
    try {
        FeedResponse feedResponse = new FeedService().openFeedIfPossible(request.getParameter("dataSourceID"));
        if (feedResponse.getStatus() == FeedResponse.SUCCESS) {
            SmartsheetTableSource dataSource = (SmartsheetTableSource) new FeedStorage().getFeedDefinitionData(feedResponse.getFeedDescriptor().getId());
            dataSource.setTable(request.getParameter("accountID"));
            dataSource.setFeedName(URLDecoder.decode(request.getParameter("tableName"), "UTF-8"));
            new FeedStorage().updateDataFeedConfiguration(dataSource);
            response.sendRedirect(RedirectUtil.getURL(request, "/app/html/dataSources/" + dataSource.getApiKey() + "/createConnection"));
        } else {
            throw new RuntimeException();
        }
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>