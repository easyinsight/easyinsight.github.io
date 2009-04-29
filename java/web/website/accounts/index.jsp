<%@ page import="com.easyinsight.users.UserAccountAdminService" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Easy Insight - Visibility, Insight, Improvement</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link href="../website.css" rel="stylesheet" type="text/css" />
    <link rel="icon" type="image/ico" href="../../favicon.ico"/>
</head>
<body>
<div id="allPage">
    <div id="header">
        <div id="navigationElements">
            <div id="topLinks">
                <table>
                    <tr>
                        <td width="370"/><td id="topMenuHome" width="80"><a href="../index.html">home</a></td><td id="topMenuBlog" width="80"><a href="../index.html">blog</a></td><td id="topMenuContactUs" width="80"><a href="../index.html">contact us</a></td>
                    </tr>
                </table>
            </div>
            <div id="mainLinks">
                <table>
                    <tr>
                        <td id="mainMenuHome" width="120"><a href="../index.html">HOME</a></td><td id="topMenuProduct" width="120"><a href="../index.html">PRODUCT</a></td><td id="topMenuSolutions" width="120"><a href="../index.html">SOLUTIONS</a></td><td id="topMenuCommunity" width="120"><a href="../index.html">COMMUNITY</a></td><td id="topMenuConsulting" width="120"><a href="../index.html">CONSULTING</a></td><td id="topMenuCompany" width="120"><a href="../index.html">COMPANY</a></td>
                    </tr>
                </table>
            </div>
        </div>
        <div id="logo">
            <img src="../logo2.PNG" alt="logo"/>
        </div>
    </div>
    <img src="../redbar.PNG" alt=""/>
    <div id="centerPage">
        <%
            String activationID = request.getParameter("activation");
            boolean result = new UserAccountAdminService().activateAccount(activationID);
            if (result) {
                out.println("Your account is now active!");
            } else {
                out.println("We could not find an activation ID matching the parameter passed in.");
            }
        %>
    </div>
    <div id="footer">
          <table>
              <tr>
                  <td width="400">
                      &copy; 2009 Easy Insight LLC. All rights reserved.
                  </td>
                  <td width="120">
                      <a href="../index.html">Home</a>
                  </td>
                  <td width="120">
                      <a href="../sitemap.html">Site Map</a>
                  </td>
                  <td width="120">
                      <a href="../privacy.html">Privacy Policy</a>
                  </td>
              </tr>
          </table>
    </div>

</div>
</body>
</html>