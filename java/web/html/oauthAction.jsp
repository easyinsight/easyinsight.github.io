<%@ page import="com.easyinsight.users.TokenService" %>
<%@ page import="com.easyinsight.datafeeds.FeedDefinition" %>
<%@ page import="com.easyinsight.users.OAuthResponse" %>
<%@ page import="com.easyinsight.solutions.SolutionService" %>
<%@ page import="com.easyinsight.solutions.InstallationValidation" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %><%
    SecurityUtil.populateThreadLocalFromSession(request);
    try {
        long solutionID = Long.parseLong(request.getParameter("solutionID"));
        //InstallationValidation installationValidation = new SolutionService().connectionInstalled(solutionID);
        FeedDefinition dataSource = new SolutionService().installSolution(solutionID);
        OAuthResponse oauthResponse = new TokenService().getOAuthResponse(dataSource.getFeedType().getType(), true, dataSource, dataSource.isVisible() ? 2 : TokenService.HTML_SETUP, session);
        response.sendRedirect(oauthResponse.getRequestToken());
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>