<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="com.easyinsight.users.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Easy Insight Mobile</title>
    <link rel="stylesheet" href="/css/jquery.mobile-1.0a1.min.css" />
    <script src="/js/jquery-1.4.3.min.js"></script>
    <script src="/js/jquery.mobile-1.0a1.min.js"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>

<div data-role="page">

	<div data-role="header">
		<h1>Easy Insight Mobile</h1>
	</div><!-- /header -->

	<div data-role="content">
        <%
            if(request.getSession().getAttribute("accountID") != null) {
                org.hibernate.Session s = com.easyinsight.database.Database.instance().createSession();
                try {
                    long accountID = (Long) request.getSession().getAttribute("accountID");
                    long userID = (Long) request.getSession().getAttribute("userID");
                    User user = (User) s.createQuery("from User where userID = ?").setLong(0, userID).list().get(0);
                    Account account = (Account) s.createQuery("from Account where accountID = ?").setLong(0, accountID).list().get(0);
                    session.setAttribute("accountType", account.getAccountType());
                    session.setAttribute("userName", user.getUserName());
                    response.sendRedirect("menu.jsp");
                    return;
                } finally {
                    s.close();
                }
            }
            /*if (request.getSession().getAttribute("accountID") != null) {
                long accountID = (Long) request.getSession().getAttribute("accountID");
                long userID = (Long) request.getSession().getAttribute("userID");
                out.println("<p>Current account ID = " + accountID + "</p>");
            }*/

            if (request.getParameter("error") != null) {
                out.println("<p>There was an error in your login information.</p>");
            }
        %>
        <form action="LoginCheck.jsp" method="post">
            <fieldset>
                <label for="name">User Name:</label>
                <input type="text" name="userName" id="userName" value=""  />
                <label for="name">Password:</label>
                <input type="password" name="password" id="password" value=""  />
                <button type="submit" data-theme="a">Submit</button>
            </fieldset>
        </form>
	</div><!-- /content -->

	<div data-role="footer">
		<h4>Easy Insight Mobile</h4>
	</div><!-- /footer -->
</div><!-- /page -->

</body>
</html>