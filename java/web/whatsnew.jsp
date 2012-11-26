<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.logging.LogClass" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="com.easyinsight.admin.AdminService" %>
<%@ page import="com.easyinsight.admin.NewsEntry" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.*" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>What's New with Easy Insight</title>
    <script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="/js/jquery-ui-1.8.20.custom.min.js"></script>
    <link href="/css/bootstrap.css" rel="stylesheet">
    <link href="/css/smoothness/jquery-ui-1.8.20.custom.css" rel="stylesheet">

    <style type="text/css">
        body {
            padding-top: 45px;
            padding-bottom: 40px;
        }
    </style>
    <link href="/css/bootstrap-responsive.css" rel="stylesheet">
    <script type="text/javascript" src="/js/bootstrap.js"></script>
</head>
<body>
<%

    String userName = (String) session.getAttribute("userName");
    if (userName != null) {
        com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    }

    try {

%>
<jsp:include page="header.jsp">
    <jsp:param name="userName" value="<%= userName %>"/>
    <jsp:param name="headerActive" value="<%= HtmlConstants.WHATS_NEW %>"/>
</jsp:include>
<div class="container">
    <div class="row">
        <div class="span12">
            <div class="span6 offset3">
                <div style="width:100%;text-align: center">
                    <h2>What's New with Easy Insight</h2>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <%
            List<NewsEntry> newsEntryList = new AdminService().getNews();
            Set<String> tags = new HashSet<String>();
            for (NewsEntry newsEntry : newsEntryList) {
                String tagString = newsEntry.getTags();
                if (tagString == null) {
                    continue;
                }
                String[] tagArray = tagString.split(",");
                for (String tag : tagArray) {
                    String trimmedTag = tag.trim();
                    if (!"".equals(trimmedTag)) {
                        tags.add(trimmedTag);
                    }
                }
            }
            List<String> tagList = new ArrayList<String>(tags);
            Collections.sort(tagList);
        %>
        <div class="span3">
            <div class="container">
                <div class="row">
                    <div class="span2">
                        <img src="/images/logo2.PNG" alt="Easy Insight Logo"/>
                    </div>
                </div>
                <div class="row">
                    <div class="span3">
                        <div class="well">
                            <div><a href="whatsnew.jsp" style="font-size:16px">All</a></div>
                            <%
                                for (String tag : tagList) {
                            %>
                            <div><a href="whatsnew.jsp?tag=<%= tag %>" style="font-size:16px"><%= tag %></a></div>
                            <%
                                }
                            %>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="span9">
            <%
                /*String pageNumberString = request.getParameter("page");
                int pageNumber = 0;
                if (pageNumberString != null) {
                    pageNumber = Integer.parseInt(pageNumberString);
                }*/



                String tag = request.getParameter("tag");

                Collections.reverse(newsEntryList);
                for (NewsEntry newsEntry : newsEntryList) {
                    if (tag != null) {
                        boolean valid = false;
                        String tagString = newsEntry.getTags();
                        if (tagString == null) {
                            continue;
                        }
                        String[] tagArray = tagString.split(",");
                        for (String testTag : tagArray) {
                            String trimmed = testTag.trim();
                            if (trimmed.equals(tag)) {
                                valid = true;
                            }
                        }
                        if (!valid) {
                            continue;
                        }
                    }
            %>
            <div class="row" style="padding-top:10px">
                <div class="span9">
                    <div style="width:100%;text-align:center">
                        <h3><%= newsEntry.getTitle() %> - <%= new SimpleDateFormat("yyyy-MM-dd").format(newsEntry.getDate()) %></h3>
                    </div>
                </div>
                <div class="span9" style="padding-top:10px">
                    <div style="width:100%;text-align:center">
                        <p><%= newsEntry.getNews() %></p>
                    </div>
                </div>
            </div>
            <%
                }
            %>
        </div>
    </div>
</div>

<%
    } finally {
        if (userName != null) {
            SecurityUtil.clearThreadLocal();
        }
    }
%>
</body>
</html>