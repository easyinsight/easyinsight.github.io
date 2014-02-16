<%@ page import="com.easyinsight.datafeeds.FeedResponse" %>
<%@ page import="com.easyinsight.datafeeds.FeedService" %>
<%@ page import="com.easyinsight.datafeeds.basecampnext.BasecampNextCompositeSource" %>
<%@ page import="com.easyinsight.datafeeds.FeedStorage" %>
<%@ page import="com.easyinsight.html.RedirectUtil" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %><%
    SecurityUtil.populateThreadLocalFromSession(request);
    try {
        FeedResponse feedResponse = new FeedService().openFeedIfPossible(request.getParameter("dataSourceID"));
        if (feedResponse.getStatus() == FeedResponse.SUCCESS) {
            BasecampNextCompositeSource dataSource = (BasecampNextCompositeSource) new FeedStorage().getFeedDefinitionData(feedResponse.getFeedDescriptor().getId());
            dataSource.setEndpoint(request.getParameter("accountID"));
            new FeedStorage().updateDataFeedConfiguration(dataSource);
            response.sendRedirect(RedirectUtil.getURL(request, "/app/html/dataSources/" + dataSource.getApiKey() + "/createConnection"));
        } else {
            throw new RuntimeException();
        }
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>