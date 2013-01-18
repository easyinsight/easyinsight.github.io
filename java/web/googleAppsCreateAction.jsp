<%@ page import="com.easyinsight.util.RandomTextGenerator" %>
<%@ page import="com.easyinsight.users.*" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.datafeeds.FeedType" %>
<%@ page import="com.easyinsight.html.RedirectUtil" %><%

    String domain = (String) session.getAttribute("googleDomain");
    String email = (String) session.getAttribute("googleAppsSetupEmail");
    String firstName = (String) session.getAttribute("googleAppsSetupFirstName");
    String lastName = (String) session.getAttribute("googleAppsSetupLastName");
    UserTransferObject admin = new UserTransferObject();
    admin.setEmail(email);
    admin.setUserName(email);
    admin.setFirstName(firstName);
    admin.setName(lastName);
    admin.setAccountAdmin(true);
    admin.setAnalyst(true);
    admin.setOptInEmail(true);
    admin.setInitialSetupDone(true);
    AccountTransferObject accountTransferObject = new AccountTransferObject();
    accountTransferObject.setAccountType(Account.PLUS);
    accountTransferObject.setName(request.getParameter("companyName"));
    accountTransferObject.setGoogleAppsDomain(domain);
    accountTransferObject.setAccountState(Account.TRIAL);
    String password = RandomTextGenerator.generateText(15);
    String exists = new com.easyinsight.users.UserService().doesUserExist(admin.getUserName(), admin.getEmail(), request.getParameter("companyName"));
    if (exists == null) {
        new UserService().createAccount(admin, accountTransferObject, password);
        UserServiceResponse userServiceResponse = new UserService().authenticate(email, password, false);
        SecurityUtil.populateSession(session, userServiceResponse);
    //    response.sendRedirect(new TokenService().getHttpOAuthURL(FeedType.GOOGLE_PROVISIONING.getType(), true, TokenService.USER_SOURCE, session).getRequestToken());
        request.getSession().removeAttribute("appsErrorString");
        response.sendRedirect(RedirectUtil.getURL(request, "/app/googleAppsUserList.jsp"));
    } else {
        request.getSession().setAttribute("appsErrorString", exists);
        response.sendRedirect(RedirectUtil.getURL(request, "/app/googleAppsSettings.jsp"));
    }
%>