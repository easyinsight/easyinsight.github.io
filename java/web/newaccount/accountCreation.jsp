<%@ page import="java.net.URLEncoder" %>
<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="com.easyinsight.users.UserService" %>
<%@ page import="com.easyinsight.html.RedirectUtil" %><%
    String firstName = request.getParameter("firstName");
    String lastName = request.getParameter("lastName");
    String email = request.getParameter("email");
    String company = request.getParameter("company");
    String password = request.getParameter("password");
    String wasSubmit = request.getParameter("wasSubmit");
    String errorString = UserService.checkPassword(password);


    if (wasSubmit != null) {
        if(errorString != null) {
            // fall through
        } else if (firstName == null || "".equals(firstName)) {
            errorString = "Please specify a first name.";
        } else if (lastName == null || "".equals(lastName)) {
            errorString = "Please specify a last name.";
        } else if (email == null || "".equals(email)) {
            errorString = "Please specify an email address";
        } else if (company == null || "".equals(company)) {
            errorString = "Please specify a company name.";
        }  else if (password == null || "".equals(password)) {
            errorString = "Please specify a password.";
        } else if (password.length() < 8) {
            errorString = "Your password must be at least eight characters.";
        } else if (password.length() > 20) {
            errorString = "Your password must be less than twenty characters.";
        } else {
            com.easyinsight.users.UserTransferObject user = new com.easyinsight.users.UserTransferObject();
            user.setUserName(email);
            user.setFirstName(firstName);
            user.setName(lastName);
            user.setEmail(email);
            user.setAccountAdmin(true);
            user.setOptInEmail(true);
            user.setInitialSetupDone(true);
            user.setAnalyst(true);
            com.easyinsight.users.AccountTransferObject account = new com.easyinsight.users.AccountTransferObject();
            account.setName(company);
            account.setAccountType(Account.PROFESSIONAL);

            String exists = new com.easyinsight.users.UserService().doesUserExist(user.getUserName(), user.getEmail(), account.getName());
            if (exists == null) {
                String url = "https://www.easy-insight.com/app";
                new com.easyinsight.users.UserService().createAccount(user, account, request.getParameter("password"), url);
            } else {
                errorString = exists;
            }
        }
    }
    if (errorString == null) {
        response.sendRedirect(RedirectUtil.getURL(request, "/app/newaccount/accountCreated.jsp"));
        return;
    } else {
        request.getSession().setAttribute("errorString", errorString);
        request.getSession().setAttribute("firstName", firstName);
        request.getSession().setAttribute("lastName", lastName);
        request.getSession().setAttribute("email", email);
        request.getSession().setAttribute("company", company);
        response.sendRedirect(RedirectUtil.getURL(request, "/app/newaccount/index.jsp"));
        return;
    }
%>