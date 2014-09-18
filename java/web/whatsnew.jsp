<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.logging.LogClass" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="com.easyinsight.admin.AdminService" %>
<%@ page import="com.easyinsight.admin.NewsEntry" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.*" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page import="com.easyinsight.jsphelpers.EIHelper" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>What's New with Easy Insight</title>
    <jsp:include page="html/bootstrapHeader.jsp"/>
    <jsp:include page="html/reportDashboardHeader.jsp"/>
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
<div class="container-fluid">
    <div class="row">
        <div class="col-md-12" style="background-color:#0084b4">
            <div class="row">
                <div class="col-md-8 col-md-offset-2" style="text-align:center">
                    <h2 style="color:#FFFFFF;margin-top: 20px;margin-bottom: 20px">
                        What's New With Easy Insight
                    </h2>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="container corePageWell" style="margin-top:20px">
    <div class="row">
        <%
            if (userName != null) {
                EIConnection conn = Database.instance().getConnection();
                try {
                    PreparedStatement uStmt = conn.prepareStatement("UPDATE USER SET NEWS_DISMISS_DATE = ? WHERE USER_ID = ?");
                    uStmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                    uStmt.setLong(2, SecurityUtil.getUserID());
                    uStmt.executeUpdate();
                    uStmt.close();
                } finally {
                    Database.closeConnection(conn);
                }
            }
            String article = request.getParameter("article");
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
            EIHelper.sort(tagList);

        %>
        <div class="col-md-3">
            <div class="container">
                <div class="row">
                    <div class="col-md-2">
                        <img src="/images/logo2.png" alt="Easy Insight Logo"/>
                    </div>
                </div>
                <div class="row" style="margin-top: 20px">
                    <div class="col-md-3">
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
        <div class="col-md-9">
            <%
                /*String pageNumberString = request.getParameter("page");
                int pageNumber = 0;
                if (pageNumberString != null) {
                    pageNumber = Integer.parseInt(pageNumberString);
                }*/



                String tag = request.getParameter("tag");

                Collections.reverse(newsEntryList);
                for (NewsEntry newsEntry : newsEntryList) {
                    if (newsEntry.getTitle() == null) {
                        continue;
                    }
                    if (article != null) {
                        String aliased = newsEntry.getTitle().replace(" ", "-").replace(",", "").toLowerCase();
                        if (!article.equals(aliased)) {
                            continue;
                        }
                    } else if (tag != null) {
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
                    String permalink = "/app/whatsnew.jsp?article="+newsEntry.getTitle().replace(" ", "-").replace(",", "").toLowerCase();
            %>
            <div class="row" style="padding-top:10px">
                <div class="col-md-12" style="text-align:left">
                    <a class="productHeader" style="color: #0084B4" href="<%=permalink%>"><%= newsEntry.getTitle() %></a>
                </div>
            </div>
            <div class="row" style="padding-top:10px">
                <div class="col-md-12" style="text-align: left">
                    Posted on <a href="<%=permalink%>" style="color: #0084B4"><%=new SimpleDateFormat("MMMM dd, yyyy").format(newsEntry.getDate()) %></a> by <a style="color:#0084B4" href="<%=permalink%>">James Boe</a>
                </div>
            </div>
            <div class="row" style="padding-top:10px">
                <div class="col-md-12" style="padding-top:10px">
                    <div style="width:100%;text-align:left">
                        <p style="text-align: left"><%= newsEntry.getNews() %></p>
                    </div>
                </div>
            </div>
            <hr style="color: #DDDDDD; border-color: #DDDDDD; background-color: #DDDDDD; height: 1px; border: 0"/>
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