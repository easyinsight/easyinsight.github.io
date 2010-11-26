<%--
  Created by IntelliJ IDEA.
  User: abaldwin
  Date: Jul 3, 2009
  Time: 10:17:02 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
<!-- InstanceBeginEditable name="doctitle" -->
            <title>Easy Insight - Billing Complete</title>
<!-- InstanceEndEditable -->
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link href="/css/base.css" rel="stylesheet" type="text/css" />
    <link rel="icon" type="image/ico" href="/favicon.ico"/>
    <!-- InstanceBeginEditable name="head" -->
    <style type="text/css">
        #centerPage p {
            text-indent: 0px;
        }
        #centerPage {
            height:400px;
        }

        .loginButton {
            height: 25px;
            width: 61px;
            background-image: url('login.gif');
        }

        .loginButton:hover {
            background-image: url('loginSelected.gif');
        }

        .loginButton:active {
            background-image: url('loginActivated.gif');
        }

    </style>
    <!-- InstanceEndEditable -->
    <link type="text/css" rel="stylesheet" media="screen" href="/css/base.css" />
</head>
<body style="width:100%;text-align:center;margin:0px auto;">
    <div style="width:1000px;border-left-style:solid;border-left-color:#DDDDDD;border-left-width:1px;border-right-style:solid;border-right-color:#DDDDDD;border-right-width:1px;margin:0 auto;">
        <div id="content">
        <!-- InstanceBeginEditable name="content" -->
            <div style="width:100%;background-color:#FFFFFF">
              <form method="post" action="index.jsp" style="width:100%">
                  <table><tbody><tr>
                      <td>User Name:</td>
                      <td> <input type="text" name="username" /></td></tr>
                  <tr><td>Password:</td>
                  <td><input type="password" name="password" /></td></tr>
                  <tr><td colspan="2" style="text-align:center"><input type="submit" value="Log In"/></td></tr>
                      </tbody></table>

          </form>
                </div>

        </div>
        </div>
    </body>
</html>