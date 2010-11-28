<!DOCTYPE html>
<html>
	<head>
	<title>Easy Insight Mobile</title>
	<link rel="stylesheet" href="http://code.jquery.com/mobile/1.0a1/jquery.mobile-1.0a1.min.css" />
	<script src="http://code.jquery.com/jquery-1.4.3.min.js"></script>
	<script src="http://code.jquery.com/mobile/1.0a1/jquery.mobile-1.0a1.min.js"></script>
</head>
<body>

<div data-role="page">

	<div data-role="header">
		<h1>Easy Insight Mobile</h1>
	</div><!-- /header -->

	<div data-role="content">
        <%
            if (request.getSession().getAttribute("accountID") != null) {
                long accountID = (Long) request.getSession().getAttribute("accountID");
                long userID = (Long) request.getSession().getAttribute("userID");
                out.println("<p>Current account ID = " + accountID + "</p>");
            }

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
		<h4>Page Footer</h4>
	</div><!-- /footer -->
</div><!-- /page -->

</body>
</html>