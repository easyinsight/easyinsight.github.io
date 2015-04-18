<%@ page import="com.easyinsight.datafeeds.FeedResponse" %>
<%@ page import="com.easyinsight.datafeeds.FeedService" %>
<%@ page import="com.easyinsight.datafeeds.basecampnext.BasecampNextCompositeSource" %>
<%@ page import="com.easyinsight.datafeeds.FeedStorage" %>
<%@ page import="com.easyinsight.html.RedirectUtil" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.datafeeds.surveygizmo.SurveyGizmoCompositeSource" %>
<%@ page import="java.net.URLDecoder" %><%
    SecurityUtil.populateThreadLocalFromSession(request);
    try {
        FeedResponse feedResponse = new FeedService().openFeedIfPossible(request.getParameter("dataSourceID"));
        if (feedResponse.getStatus() == FeedResponse.SUCCESS) {
            SurveyGizmoCompositeSource dataSource = (SurveyGizmoCompositeSource) new FeedStorage().getFeedDefinitionData(feedResponse.getFeedDescriptor().getId());
            dataSource.setFormID(request.getParameter("formID"));
            dataSource.setFeedName(URLDecoder.decode(request.getParameter("formName"), "UTF-8"));
            new FeedStorage().updateDataFeedConfiguration(dataSource);
            response.sendRedirect(RedirectUtil.getURL(request, "/app/html/dataSources/" + dataSource.getApiKey() + "/createConnection"));
        } else {
            throw new RuntimeException();
        }
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>