<%@ page import="com.easyinsight.html.RedirectUtil" %>
<%@ page import="com.easyinsight.logging.LogClass" %>
<%@ page import="com.easyinsight.email.SendGridEmail" %>
<%
    final String name = request.getParameter("firstName");
    final String email = request.getParameter("email");
    final String company = request.getParameter("company");
    final String phone = request.getParameter("phone");

    String wasSubmit = request.getParameter("wasSubmit");
    String errorString = null;


    if (wasSubmit != null) {
        if (name == null || "".equals(name)) {
            errorString = "Please provide your name.";
        } else if (email == null || "".equals(email)) {
            errorString = "Please provide an email address";
        } else if (company == null || "".equals(company)) {
            errorString = "Please provide your company name.";
        } else if (phone == null || "".equals(phone)) {
            errorString = "Please provide your phone number.";
        } else {
            new Thread(new Runnable() {

                public void run() {
                    String body = "Name: " + name + "\nEmail: " + email + "\nPhone: " + phone +
                            "\nCompany: " + company;
                    try {
                        new SendGridEmail().sendEmail("sales@easy-insight.com", "Request for Enterprise Account Info", body, "donotreply@easy-insight.com", false, "Easy Insight");
                    } catch (Exception e) {
                        LogClass.error(e);
                    }
                }
            }).start();
            response.sendRedirect(RedirectUtil.getURL(request, "/app/newaccount/salesContacted.jsp"));
        }
    }
    if (errorString == null) {
        return;
    } else {
        request.getSession().setAttribute("errorString", errorString);
        request.getSession().setAttribute("firstName", name);
        request.getSession().setAttribute("company", company);
        request.getSession().setAttribute("email", email);
        request.getSession().setAttribute("phone", phone);
        response.sendRedirect(RedirectUtil.getURL(request, "/app/newaccount/sales.jsp"));
        return;
    }
%>